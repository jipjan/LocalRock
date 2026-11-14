package com.kodraliu.localrock.shared.vacuum

import com.kodraliu.localrock.shared.model.Room
import com.kodraliu.localrock.shared.mqtt.MqttClient
import com.kodraliu.localrock.shared.mqtt.MqttTopics
import com.kodraliu.localrock.shared.protocol.V1Response
import com.kodraliu.localrock.shared.protocol.saveDebugBlob
import com.kodraliu.localrock.shared.protocol.isDebugBuild
import com.kodraliu.localrock.shared.vacuum.map.FloorMaterial
import com.kodraliu.localrock.shared.vacuum.map.MapFormat
import com.kodraliu.localrock.shared.vacuum.map.MapZone
import com.kodraliu.localrock.shared.vacuum.map.LOW_SPACE_USER
import com.kodraliu.localrock.shared.vacuum.map.LOW_SPACE_ERASED
import com.kodraliu.localrock.shared.vacuum.map.ParsedMap
import com.kodraliu.localrock.shared.vacuum.map.ParsedMapRoom
import com.kodraliu.localrock.shared.vacuum.map.VirtualWall
import com.kodraliu.localrock.shared.vacuum.map.parseB01Map
import com.kodraliu.localrock.shared.vacuum.map.parseLegacyMap
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlin.time.TimeSource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.longOrNull


class VacuumRepository(
    duid: String,
    localKey: String,
    private val rriotKeyProvider: () -> String,
    private val mqttClient: MqttClient,
    topicsProvider: () -> MqttTopics,
    private val nowEpochSeconds: () -> Int,
    private val pollIntervalMs: Long = 5_000L,
    private val demo: Boolean = false,
    /** Where what the robot reported is kept between runs; null keeps nothing (demo, tests). */
    private val capabilityCache: CapabilityCacheStore? = null,
) {
    private val duidForCache = duid
    private val cached: CapabilityCache? = capabilityCache?.load(duid)

    private val _reading = MutableStateFlow(0)

    /** True while a settings page is being read from the robot, for a progress bar over cached values. */
    val reading: StateFlow<Boolean> = _reading.map { it > 0 }.stateIn(
        CoroutineScope(SupervisorJob() + Dispatchers.Default), SharingStarted.Eagerly, false,
    )

    private suspend fun <T> reading(block: suspend () -> T): T {
        _reading.update { it + 1 }
        try { return block() } finally { _reading.update { it - 1 } }
    }

    private fun persist() {
        val store = capabilityCache ?: return
        runCatching {
            store.save(duidForCache, CapabilityCache(
                featureSet = _robotFeatureSet.value,
                features = _betaFeatures.value.takeIf { featuresRead }?.toCache() ?: cached?.features,
                dock = _dockSettings.value.toCache(),
            ))
        }
    }

    val session: VacuumSession = VacuumSession(
        localKey = localKey,
        mqtt = mqttClient,
        topicsProvider = topicsProvider,
        nowEpochSeconds = nowEpochSeconds,
        demo = demo,
    )
    val duid: String = duid

    private val _status = MutableStateFlow(VacuumStatus())
    val status: StateFlow<VacuumStatus> = _status

    private val _mapBytes = MutableStateFlow<ByteArray?>(null)
    val mapBytes: StateFlow<ByteArray?> = _mapBytes

    private val _parsedMap = MutableStateFlow<ParsedMap?>(null)
    val parsedMap: StateFlow<ParsedMap?> = _parsedMap

    private val _mapStatus = MutableStateFlow("idle")
    val mapStatus: StateFlow<String> = _mapStatus

    private val _rooms = MutableStateFlow<List<ParsedMapRoom>>(emptyList())
    val rooms: StateFlow<List<ParsedMapRoom>> = _rooms


    private var homeRoomNames: Map<Long, String> = emptyMap()   // roomId -> name
    private var segmentToRoomId: Map<Int, Long> = emptyMap()     // map segment id -> roomId

    /**
     * The robot's segment table exactly as `get_room_mapping` returned it: segment, room id (kept
     * as the raw string) and tag. `name_segment` must be sent the whole table, so entries this app
     * does not change are written back unchanged. Null until read, or when the answer could not
     * be parsed; empty when the robot has no rooms assigned yet.
     */
    private var rawSegmentTable: List<SegmentEntry>? = null

    private val _consumableStatus = MutableStateFlow<ConsumableStatus?>(null)
    val consumableStatus: StateFlow<ConsumableStatus?> = _consumableStatus

    private val _consumableError = MutableStateFlow<String?>(null)
    val consumableError: StateFlow<String?> = _consumableError

    private val _cleanHistory = MutableStateFlow<List<CleanRecord>>(emptyList())
    val cleanHistory: StateFlow<List<CleanRecord>> = _cleanHistory

    private val _timers = MutableStateFlow<List<CleanTimer>>(emptyList())
    val timers: StateFlow<List<CleanTimer>> = _timers

    private val _robotSettings = MutableStateFlow(RobotSettings())
    val robotSettings: StateFlow<RobotSettings> = _robotSettings

    // Starts from what the robot said last time, so features gated on its own flags don't appear late.
    private val _robotFeatureSet = MutableStateFlow(cached?.featureSet)
    private var featureSetFresh = false

    /** The feature flags the robot reports itself; the cached ones until it has answered this run. */
    val robotFeatureSet: StateFlow<String?> = _robotFeatureSet

    suspend fun fetchRobotFeatureSet() {
        if (featureSetFresh) return
        featureSetFromInitStatus(session.getInitStatus().requireOk("app_get_init_status").result)?.let {
            _robotFeatureSet.value = it
        }
        featureSetFresh = true
        persist()
    }

    private val _dockSettings = MutableStateFlow(cached?.dock?.toSettings() ?: DockSettings())
    val dockSettings: StateFlow<DockSettings> = _dockSettings

    private val _cleanSummary = MutableStateFlow<CleanSummary?>(null)
    val cleanSummary: StateFlow<CleanSummary?> = _cleanSummary

    private val _dndSettings = MutableStateFlow<DndSettings?>(null)
    val dndSettings: StateFlow<DndSettings?> = _dndSettings

    private val _floorMaps = MutableStateFlow<List<FloorMap>>(emptyList())
    val floorMaps: StateFlow<List<FloorMap>> = _floorMaps

    /**
     * True once [floorMaps] holds a real answer from the robot. An empty list alone can't tell
     * "not loaded yet" from "no saved map", and map edits depend on the difference.
     */
    private val _floorMapsLoaded = MutableStateFlow(false)
    val floorMapsLoaded: StateFlow<Boolean> = _floorMapsLoaded

    /** Largest number of saved maps the robot allows (max_multi_map), once the list is read. */
    private val _maxFloorMaps = MutableStateFlow<Int?>(null)
    val maxFloorMaps: StateFlow<Int?> = _maxFloorMaps

    /** Last map this app loaded; only used until the robot's status names the loaded map. */
    private val _currentFloorFlag = MutableStateFlow(0)

    private val _mopMode = MutableStateFlow<Int?>(null)
    val mopMode: StateFlow<Int?> = _mopMode

    private val _roomCleanModes = MutableStateFlow<List<RoomCleanMode>>(emptyList())
    val roomCleanModes: StateFlow<List<RoomCleanMode>> = _roomCleanModes

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    /** The loaded map's mapFlag, from the robot's map_status when it reports one. */
    val currentFloorFlag: StateFlow<Int> = combine(_status, _currentFloorFlag) { status, loadedHere ->
        status.loadedMapFlag?.takeIf { it != UNSAVED_MAP_FLAG } ?: loadedHere
    }.stateIn(scope, SharingStarted.Eagerly, 0)

    /** True while the robot holds a newly built map that has not been saved yet. */
    val unsavedMapPresent: StateFlow<Boolean> = _status.map { it.unsavedMapPresent }
        .stateIn(scope, SharingStarted.Eagerly, false)
    private var pollJob: Job? = null
    private var dpsJob: Job? = null
    private var mapPollJob: Job? = null
    private var floorSwitchRefreshJob: Job? = null

    suspend fun attach() {
        check(pollJob == null) { "Already attached" }
        if (demo) {
            seedDemo()
            return
        }
        println("[VacLocal] attach duid=$duid")
        session.start()
        dpsJob = scope.launch {
            session.dpsUpdates.collect { dps ->
                println("[VacLocal] dps push: ${dps.keys}")
                _status.update { it.applyDpsPush(dps) }
            }
        }
        pollJob = scope.launch {

            var consecutiveFailures = 0
            while (isActive) {
                // While the link is (re)connecting a request fails at once. Wait for it instead,
                // so the first status goes out the moment it is up.
                mqttClient.connected.first { it }
                runCatching { session.ensureSubscribed() }
                runCatching { session.getStatus() }
                    .onSuccess { fresh ->
                        consecutiveFailures = 0
                        if (fresh != null) {
                            println("[VacLocal] get_status ok: state=${fresh.state} battery=${fresh.battery}")
                            _status.update { it.merge(fresh) }
                        } else {
                            println("[VacLocal] get_status returned null (empty result)")
                        }
                    }
                    .onFailure { e ->
                        consecutiveFailures++
                        println("[VacLocal] get_status FAILED (#$consecutiveFailures): ${e::class.simpleName}: ${e.message}")
                        if (consecutiveFailures >= STALE_LINK_FAILURE_THRESHOLD) {
                            // A timed-out RPC is not proof of a dead socket: a slow response or a
                            // big map transfer causes the same symptom, and reconnecting then
                            // drops every in-flight request and makes it worse. Only tear down
                            // when the link is reported down or has gone completely silent.
                            val silentMs = mqttClient.millisSinceLastMessage()
                            val linkDead = !mqttClient.connected.value || silentMs >= STALE_LINK_SILENCE_MS
                            if (linkDead) {
                                println("[VacLocal] $consecutiveFailures poll failures, link silent ${silentMs}ms — forcing MQTT reconnect")
                                runCatching { mqttClient.forceReconnect() }
                            } else {
                                println("[VacLocal] $consecutiveFailures poll failures but link is alive (${silentMs}ms since last message) — not reconnecting")
                            }
                            consecutiveFailures = 0
                        }
                    }
                delay(pollIntervalMs)
            }
        }
        mapPollJob = scope.launch {
            var loaded = false
            while (isActive) {
                mqttClient.connected.first { it }
                runCatching { fetchMap() }
                    .onSuccess { loaded = true }
                    .onFailure { println("[VacLocal] fetchMap FAILED: ${it::class.simpleName}: ${it.message}") }
                // Until a map has arrived, ask again soon: sitting out the idle minute after a
                // failed first request left the screen without a map for up to two minutes.
                delay(
                    when {
                        !loaded -> MAP_RETRY_MS
                        _status.value.state in ACTIVE_STATES -> MAP_FAST_POLL_MS
                        else -> MAP_IDLE_POLL_MS
                    }
                )
            }
        }
    }


    suspend fun forceReconnect() {
        runCatching { mqttClient.forceReconnect() }
    }


    suspend fun refreshStatus() {
        runCatching { session.getStatus() }.getOrNull()
            ?.let { fresh -> _status.update { it.merge(fresh) } }
    }

    fun detach() {
        pollJob?.cancel(); pollJob = null
        dpsJob?.cancel(); dpsJob = null
        mapPollJob?.cancel(); mapPollJob = null
        floorSwitchRefreshJob?.cancel(); floorSwitchRefreshJob = null
        scope.launch { session.close() }
    }

    suspend fun pause(): V1Response = session.appPause().requireOk("app_pause")
    suspend fun stopCleaning(): V1Response = session.appStop().requireOk("app_stop")
    suspend fun dock(): V1Response = session.appCharge().requireOk("app_charge")

    suspend fun resumeCleaning(): V1Response {
        val fresh = session.getStatus() ?: throw IllegalStateException("The robot's current task is unavailable")
        _status.update { it.merge(fresh) }
        return when (fresh.inCleaning) {
            1 -> session.appStart().requireOk("app_start")
            2 -> session.sendCommand("resume_zoned_clean").requireOk("resume_zoned_clean")
            3 -> session.sendCommand("resume_segment_clean").requireOk("resume_segment_clean")
            else -> throw IllegalStateException("There is no supported cleaning task to resume")
        }
    }


    suspend fun rooms(): V1Response {
        val resp = session.appRooms()
        val result = resp.result
        rawSegmentTable = parseSegmentTable(result)
        if (result is JsonArray) {
            // Names resolve from the first two columns, which every firmware returns.
            val mapping = parseRoomMapping(result)
            if (mapping.isNotEmpty()) {
                segmentToRoomId = mapping
                println("[VacLocal] room mapping seg->roomId=$mapping resolved=${mapping.mapValues { homeRoomNames[it.value] ?: "(no home name)" }}")
                applyRoomNames()
            }
        }
        return resp
    }


    fun setHomeRooms(rooms: List<Room>) {
        homeRoomNames = rooms.associate { it.id to it.name }
        println("[VacLocal] home rooms (roomId->name)=$homeRoomNames")
        applyRoomNames()
    }

    private fun resolveRoomName(room: ParsedMapRoom): String =
        segmentToRoomId[room.id]?.let { homeRoomNames[it] } ?: room.name

    private fun applyRoomNames() {
        _parsedMap.update { pm ->
            pm?.copy(rooms = pm.rooms.map { it.copy(name = resolveRoomName(it)) })
        }
        _rooms.update { list -> list.map { it.copy(name = resolveRoomName(it)) } }
    }

    /**
     * Clean [roomIds] in the order given. The robot follows its stored sequence, so that is
     * written first when it would clean them in another order. A robot that can't take the
     * sequence still cleans the rooms, in its own order.
     */
    suspend fun cleanRooms(roomIds: List<Int>, repeat: Int = 1): V1Response {
        if (roomIds.distinct().size >= 2) writeCleanSequence { stored -> cleanSequenceFor(roomIds, stored) }
        return session.appSegmentClean(roomIds, repeat).requireOk("app_segment_clean")
    }

    /**
     * Start a full clean. With a [defaultOrder] the robot's sequence is put back to it first,
     * since cleaning picked rooms may have rewritten it ([cleanRooms]). Rooms that are no longer on
     * the map are left out.
     */
    suspend fun clean(defaultOrder: List<Int> = emptyList()): V1Response {
        val onMap = _rooms.value.map { it.id }
        val order = if (onMap.isEmpty()) defaultOrder else defaultOrder.filter { it in onMap }
        if (order.isNotEmpty()) writeCleanSequence { stored -> order.takeIf { it != stored } }
        return session.appStart().requireOk("app_start")
    }

    /** Set once the robot fails a sequence command, so later cleans don't wait on it again. */
    private var cleanSequenceRefused = false

    /** Best effort: store what [wanted] returns for the robot's current sequence, if anything. */
    private suspend fun writeCleanSequence(wanted: (stored: List<Int>) -> List<Int>?) {
        if (demo || cleanSequenceRefused) return
        runCatching {
            val stored = parseCleanSequence(session.getCleanSequence().result)
            wanted(stored)?.let { session.setCleanSequence(it) }
        }.onFailure {
            if (it is CancellationException) throw it
            println("[VacLocal] room order not stored: $it")
            cleanSequenceRefused = true
        }
    }

    /**
     * Store [order] as the robot's cleaning sequence. Captured with the robot idle only, so a busy
     * robot is refused here. Confirmed by reading the sequence back.
     */
    suspend fun saveCleanSequence(order: List<Int>) {
        if (demo) throw MapEditException("The cleaning order can't be saved in demo mode")
        if (_status.value.state in ACTIVE_STATES) {
            throw MapEditException("The robot is busy. Dock it or let it finish first.")
        }
        session.setCleanSequence(order)
        if (parseCleanSequence(session.getCleanSequence().result) != order) {
            throw MapEditException("The robot accepted the cleaning order, but does not report it back.")
        }
        cleanSequenceRefused = false
    }

    suspend fun setFanPower(level: Int) =
        setOneMotorMode("set_custom_mode", session.setCustomMode(level)) { it.copy(fanPower = level) }

    suspend fun setWaterBoxMode(level: Int) =
        setOneMotorMode("set_water_box_custom_mode", session.setWaterBoxCustomMode(level)) { it.copy(waterBoxMode = level) }

    /**
     * Checks the robot's [answer] to a single suction, water or route change. On -10005 the change is
     * sent again as one set_clean_motor_mode with the other two as the robot reports them now (see
     * [motorModesForFallback]); a refusal that can't be retried, or a refused retry, is raised.
     */
    private suspend fun setOneMotorMode(method: String, answer: V1Response, change: (MotorModes) -> MotorModes) {
        val error = answer.error ?: return
        if (answer.errorCode() == PARAM_FORMAT_REFUSED_CODE) {
            refreshStatus()
            fetchMopMode()
        }
        val current = _status.value
        val modes = motorModesForFallback(answer.errorCode(), current.fanPower, current.waterBoxCustomMode, _mopMode.value)
            ?: throw RobotCommandException(method, error)
        val wanted = change(modes)
        setCleanMotorMode(wanted.fanPower, wanted.waterBoxMode, wanted.mopMode)
    }

    suspend fun collectDust(): V1Response = session.appStartCollectDust().requireOk("app_start_collect_dust")
    suspend fun washMop(): V1Response = session.appStartWash().requireOk("app_start_wash")
    suspend fun stopWash(): V1Response = session.appStopWash().requireOk("app_stop_wash")
    suspend fun startDryer(): V1Response = session.appSetDryerStatus(true).requireOk("app_set_dryer_status")

    suspend fun stopDryer(): V1Response = session.appSetDryerStatus(false).requireOk("app_set_dryer_status")

    suspend fun fetchMopMode() {
        runCatching { session.getMopMode() }.getOrNull()
            ?.result?.let { r ->
                val mode = when (r) {
                    is JsonArray -> (r.firstOrNull() as? JsonPrimitive)?.intOrNull
                    is JsonPrimitive -> r.intOrNull
                    else -> null
                }
                if (mode != null) _mopMode.value = mode
            }
    }

    suspend fun setMopMode(mode: Int) {
        setOneMotorMode("set_mop_mode", session.setMopMode(mode)) { it.copy(mopMode = mode) }
        _mopMode.value = mode
    }

    /**
     * Vacuum first and mop after, or both together, with the suction, water and route the robot holds
     * now, as the official app sends them. The status is read back so the screen shows what the robot took.
     */
    suspend fun setVacuumThenMop(enabled: Boolean, repeat: Int) {
        val current = _status.value
        val type = if (enabled) SEQ_TYPE_VACUUM_THEN_MOP else SEQ_TYPE_TOGETHER
        session.setCleanSequenceType(
            type = type,
            fanPower = current.fanPower ?: throw IllegalStateException("The robot's suction is unknown"),
            waterBoxMode = current.waterBoxCustomMode ?: throw IllegalStateException("The robot's water flow is unknown"),
            mopMode = _mopMode.value ?: throw IllegalStateException("The robot's route is unknown"),
            repeat = repeat,
        ).requireOk("app_set_clean_sequence_type")
        refreshStatus()
    }

    /** Intensive corner mopping on or off (robots with [isCornerCleanSupported]). */
    suspend fun setCornerClean(on: Boolean) {
        session.setCornerCleanMode(on).requireOk("set_corner_clean_mode")
        refreshStatus()
    }

    /**
     * A custom water flow as a distance, as the official plugin sets it: the custom water mode (207)
     * first when the robot is not in it, then the distance.
     */
    suspend fun setWaterDistance(distance: Int) {
        require(distance in WaterDistance.RANGE) { "Water flow out of range" }
        if (_status.value.waterBoxCustomMode != WaterBoxMode.USER_CUSTOM) setWaterBoxMode(WaterBoxMode.USER_CUSTOM)
        session.setWaterBoxDistanceOff(distance).requireOk("set_water_box_distance_off")
        refreshStatus()
    }

    private val _voicePack = MutableStateFlow(VoicePackState())

    /** The voice pack in use and any install in progress. */
    val voicePack: StateFlow<VoicePackState> = _voicePack

    suspend fun fetchVoicePack() = reading {
        parseCurrentSound(session.getCurrentSound().requireOk("get_current_sound").result)?.let { current ->
            _voicePack.update { it.copy(inUse = current.inUse, inUseVersion = current.inUseVersion, inProgress = current.inProgress) }
        }
    }

    /**
     * Tells the robot to download and install [pack], then follows `get_sound_progress` until it is done
     * (at most [timeoutMs]), and reads back which pack is in use. Throws when the robot reports an error.
     */
    suspend fun installVoicePack(pack: VoicePack, timeoutMs: Long = 300_000L) {
        session.installSound(pack).requireOk("dnld_install_sound")
        _voicePack.update { it.copy(inProgress = pack.id, progress = 0, error = null) }
        val started = TimeSource.Monotonic.markNow()
        var sawProgress = false
        while (started.elapsedNow().inWholeMilliseconds < timeoutMs) {
            delay(2_000)
            val p = parseSoundProgress(session.getSoundProgress().requireOk("get_sound_progress").result) ?: continue
            _voicePack.update { it.copy(progress = p.progress, error = p.error) }
            if ((p.error ?: 0) != 0) {
                _voicePack.update { it.copy(inProgress = null) }
                throw IllegalStateException("The robot could not install the voice pack (error ${p.error}).")
            }
            if (p.inProgress == pack.id) sawProgress = true
            // Done when the robot no longer reports this pack in progress, after having started it or at 100%.
            if (p.inProgress != pack.id && (sawProgress || p.progress == 100)) break
        }
        _voicePack.update { it.copy(inProgress = null) }
        fetchVoicePack()
        if (_voicePack.value.inUse != pack.id) {
            throw IllegalStateException("The robot did not switch to ${pack.title}; it may still be installing it.")
        }
    }

    /** Sets how many passes a clean makes; the robot keeps it and reports it as `repeat`. */
    suspend fun setCleanRepeat(repeat: Int) {
        require(repeat in 1..3) { "Unsupported cleaning count $repeat" }
        session.setCleanRepeatTimes(repeat).requireOk("set_clean_repeat_times")
        refreshStatus()
    }

    /** Switches suction, water and route in one command, the only way in or out of SmartPlan. */
    suspend fun setCleanMotorMode(fanPower: Int, waterBoxMode: Int, mopMode: Int) {
        session.setCleanMotorMode(fanPower, waterBoxMode, mopMode).requireOk("set_clean_motor_mode")
        _mopMode.value = mopMode
    }

    /** The per-room settings the robot holds for the loaded map. */
    suspend fun fetchRoomCleanModes() {
        _roomCleanModes.value = parseCustomizeCleanMode(session.getCustomizeCleanMode().result)
    }

    /**
     * Write per-room settings inside an edit session, as the official app does. Captured with the
     * robot idle only, so a busy robot is refused here. Confirmed by reading the settings back.
     */
    suspend fun saveRoomCleanModes(modes: List<RoomCleanMode>): Unit = mapEditMutex.withLock {
        if (demo) throw MapEditException("Room settings can't be saved in demo mode")
        if (_status.value.state in ACTIVE_STATES) {
            throw MapEditException("The robot is busy. Dock it or let it finish first.")
        }
        session.inEditSession { session.setCustomizeCleanMode(modes) }
        fetchRoomCleanModes()
        if (!roomCleanModesStored(modes, _roomCleanModes.value)) throw MapEditException(ROOM_MODES_NOT_CONFIRMED)
    }

    suspend fun fetchConsumableStatus() {
        val resp = session.getConsumableStatus()
        val result = resp.result ?: return
        val obj: JsonObject = when (result) {
            is JsonArray -> result.firstOrNull()?.jsonObject ?: return
            is JsonObject -> result
            else -> return
        }
        _consumableStatus.value = ConsumableJson.decodeFromJsonElement(ConsumableStatus.serializer(), obj)
    }


    suspend fun fetchCleanHistory() {
        val ids = fetchCleanSummary()
        val records = mutableListOf<CleanRecord>()
        for (id in ids.take(MAX_HISTORY_RECORDS)) {
            val result = runCatching { session.getCleanRecord(id) }.getOrNull()?.result ?: continue
            parseCleanRecord(result, id)?.let { records += it }
        }
        _cleanHistory.value = records
    }


    private fun parseCleanRecord(result: JsonElement, recordId: Long): CleanRecord? {
        val element = if (result is JsonArray) result.firstOrNull() ?: return null else result
        return when (element) {
            is JsonArray -> {
                if (element.size < 6) return null
                CleanRecord(
                    beginEpoch = (element[0] as? JsonPrimitive)?.longOrNull ?: recordId,
                    endEpoch = (element[1] as? JsonPrimitive)?.longOrNull ?: 0L,
                    durationSeconds = (element[2] as? JsonPrimitive)?.longOrNull ?: 0L,
                    areaMm2 = (element[3] as? JsonPrimitive)?.longOrNull ?: 0L,
                    errorCode = (element[4] as? JsonPrimitive)?.intOrNull ?: 0,
                    complete = (element[5] as? JsonPrimitive)?.intOrNull ?: 0,
                )
            }
            is JsonObject -> CleanRecord(
                beginEpoch = element["begin"]?.jsonPrimitive?.longOrNull ?: recordId,
                endEpoch = element["end"]?.jsonPrimitive?.longOrNull ?: 0L,
                durationSeconds = element["duration"]?.jsonPrimitive?.longOrNull ?: 0L,
                areaMm2 = element["area"]?.jsonPrimitive?.longOrNull ?: 0L,
                errorCode = element["error"]?.jsonPrimitive?.intOrNull ?: 0,
                complete = element["complete"]?.jsonPrimitive?.intOrNull ?: 0,
                avoidCount = (element["avoid_count"] as? JsonPrimitive)?.intOrNull,
                washCount = (element["wash_count"] as? JsonPrimitive)?.intOrNull,
                dustCollected = (element["dust_collection_status"] as? JsonPrimitive)?.intOrNull?.let { it == 1 },
                passes = (element["clean_times"] as? JsonPrimitive)?.intOrNull,
            )
            else -> null
        }
    }

    suspend fun resetConsumable(field: String) { session.resetConsumable(field) }

    suspend fun findMe() { session.findMe() }

    suspend fun startRemoteControl() { session.appRcStart() }

    suspend fun moveRemote(velocity: Double, omega: Double, durationMs: Int) {
        session.appRcMove(velocity, omega, durationMs)
    }

    suspend fun stopRemoteControl() { session.appRcEnd() }

    /** Sends the robot to ([x], [y]) in robot millimetres. A refusal is raised, not ignored. */
    suspend fun gotoTarget(x: Int, y: Int) { session.appGotoTarget(x, y).requireOk("app_goto_target") }

    suspend fun cleanZones(zones: List<ZoneRect>) { session.appZonedClean(zones).requireOk("app_zoned_clean") }

    private val mapEditMutex = Mutex()

    /** Why the map can't be edited right now, or null when it can. */
    fun editBlockedReason(map: ParsedMap? = _parsedMap.value, state: Int? = _status.value.state): String? = when {
        demo -> "Map editing is not available in demo mode"
        state in ACTIVE_STATES -> "The robot is busy. Dock it or let it finish before editing the map."
        map == null -> "The map has not loaded yet"
        map.format != MapFormat.LEGACY -> "Map editing is not supported for this robot's map format yet"
        else -> null
    }

    /** Current map, checked to be one this app can edit safely. */
    private fun editableMap(): ParsedMap {
        editBlockedReason()?.let { throw MapEditException(it) }
        return _parsedMap.value ?: throw MapEditException("The map has not loaded yet")
    }

    /**
     * Fetch the map until [check] passes or the attempts run out. The robot rewrites its map after
     * an edit, so the first map after the command can still be the old one.
     */
    private suspend fun awaitMap(check: (ParsedMap) -> Boolean): ParsedMap? {
        repeat(READBACK_ATTEMPTS) { attempt ->
            if (attempt > 0) delay(READBACK_DELAY_MS)
            runCatching { fetchMap() }
            val map = _parsedMap.value
            if (map != null && check(map)) return map
        }
        return null
    }

    /**
     * Write the edited no-go zones, walls, carpets and thresholds. Each category is its own
     * full-set command (save_map, set_carpet_area, app_set_smart_door_sill), sent only when that
     * category changed: whatever a command leaves out is deleted. save_map is refused when the map
     * holds restrictions this app can't write back ([restrictionSaveBlocker]). Succeeds only when a
     * fresh map shows every category as sent.
     */
    suspend fun saveMapEdits(
        noGoZones: List<MapZone>,
        walls: List<VirtualWall>,
        carpets: List<MapZone>,
        thresholds: List<MapZone>,
        /** All curtains, or null to leave them as they are (a map without the block). */
        curtains: List<MapZone>? = null,
        /** The under-furniture zones the user drew ([LOW_SPACE_USER]), or null to leave them. */
        lowSpaceZones: List<MapZone>? = null,
        /** All furniture: pieces with a [MapZone.furnitureId] are on the map already. Null leaves it. */
        furniture: List<MapZone>? = null,
    ): Unit = mapEditMutex.withLock {
        val map = editableMap()
        runCatching { fetchFloorMaps() }
        if (!_floorMapsLoaded.value) {
            throw MapEditException("Couldn't read the robot's saved maps, so nothing was saved. Try again.")
        }
        val mapIndex = saveMapIndex(_floorMaps.value)
        mapIndexBlocker(mapIndex)?.let { throw MapEditException(it) }
        val index = mapIndex!!
        val restrictionsChanged = !restrictionsMatch(map, noGoZones, map.noMopZones, walls)
        val carpetsChanged = !sameZones(carpets, map.carpetAreas)
        val thresholdsChanged = !sameZones(thresholds, map.thresholds)
        val curtainsChanged = curtains != null && map.curtains != null && !sameZones(curtains, map.curtains)
        val userLowSpace = map.lowSpaceZones.orEmpty().filter { it.lowSpaceKind == LOW_SPACE_USER }
        val lowSpaceChanged = lowSpaceZones != null && map.lowSpaceZones != null && !sameZones(lowSpaceZones, userLowSpace)
        val furnitureChange = if (furniture != null && map.furniture != null) furnitureChanges(map.furniture, furniture) else null
        val furnitureChanged = furnitureChange != null && (furnitureChange.first.isNotEmpty() || furnitureChange.second.isNotEmpty())
        if (!restrictionsChanged && !carpetsChanged && !thresholdsChanged &&
            !curtainsChanged && !lowSpaceChanged && !furnitureChanged
        ) return@withLock
        if (restrictionsChanged) restrictionSaveBlocker(map, mapIndex)?.let { throw MapEditException(it) }
        session.inEditSession {
            if (restrictionsChanged) session.saveMap(noGoZones, walls, index)
            if (carpetsChanged) session.setCarpetAreas(carpets, index, map.carpetFlagsPresent)
            if (thresholdsChanged) session.setThresholds(thresholds, index)
            if (curtainsChanged) session.setCurtains(curtains!!, index)
            if (lowSpaceChanged) {
                // Erase marks the user made in the official app stay; the robot keeps what it found itself.
                val erased = map.lowSpaceZones.orEmpty().filter { it.lowSpaceKind == LOW_SPACE_ERASED }
                // The robot keeps its own copy of a zone the user drew (kind 1), so a removed zone also
                // gets an erase mark, as the official app sends it.
                val removed = userLowSpace.filter { old -> lowSpaceZones!!.none { zoneMatches(it, old) } }
                    .map { it.copy(extra = listOf(LOW_SPACE_ERASED)) }
                session.setLowSpaceZones(lowSpaceZones!!.map { it.copy(extra = listOf(LOW_SPACE_USER)) } + erased + removed, index)
            }
            if (furnitureChanged) session.saveFurnitures(furnitureChange!!.first, furnitureChange.second, index)
        }
        awaitMap { m ->
            restrictionsMatch(m, noGoZones, map.noMopZones, walls) &&
                sameZones(carpets, m.carpetAreas) &&
                m.carpetAreas.count { it.isRoundCarpet } == carpets.count { it.isRoundCarpet } &&
                sameZones(thresholds, m.thresholds) &&
                (!curtainsChanged || sameZones(curtains!!, m.curtains.orEmpty())) &&
                (!lowSpaceChanged || sameZones(lowSpaceZones!!, m.lowSpaceZones.orEmpty().filter { it.lowSpaceKind == LOW_SPACE_USER })) &&
                (!furnitureChanged || (sameZones(furniture!!, m.furniture.orEmpty()) &&
                    furniture.mapNotNull { it.furnitureType }.sorted() == m.furniture.orEmpty().mapNotNull { it.furnitureType }.sorted()))
        } ?: throw MapEditException(NOT_CONFIRMED)
    }

    /**
     * Split a room along the line A-B (robot mm). The robot is picky about which end comes first
     * (see [splitLineAttempts]), so a refused line is tried once more reversed. A refused split
     * changes nothing. The map must gain a room.
     */
    suspend fun splitRoom(segment: Int, xA: Int, yA: Int, xB: Int, yB: Int): Unit = mapEditMutex.withLock {
        val map = editableMap()
        val before = map.rooms.map { it.id }.toSet()
        if (segment !in before) throw MapEditException("That room is not on the current map")
        val attempts = splitLineAttempts(xA to yA, xB to yB)
        // The official app sent split_segment without start/end_edit_map.
        for ((i, line) in attempts.withIndex()) {
            val (a, b) = line
            try {
                session.splitSegment(segment, a.first, a.second, b.first, b.second)
                break
            } catch (e: RobotCommandException) {
                if (e.code() != SPLIT_FAILED_CODE) throw e
                if (i == attempts.lastIndex) throw splitFailure(e)
                // After refusing a split the robot sends a second, late error for the same
                // request (~10 s later in the capture). Only try again once that has passed.
                delay(SPLIT_RETRY_DELAY_MS)
            }
        }
        awaitMap { m -> (m.rooms.map { it.id }.toSet() - before).isNotEmpty() }
            ?: throw MapEditException(NOT_CONFIRMED)
        runCatching { rooms() }
    }

    /** Merge two neighbouring rooms. The map must lose one of the two ids. */
    suspend fun mergeRooms(segmentA: Int, segmentB: Int): Unit = mapEditMutex.withLock {
        val map = editableMap()
        val before = map.rooms.map { it.id }.toSet()
        if (segmentA == segmentB || segmentA !in before || segmentB !in before) {
            throw MapEditException("Pick two different rooms on the current map")
        }
        // Captured without start/end_edit_map, like split.
        session.mergeSegment(segmentA, segmentB)
        awaitMap { m -> m.rooms.map { it.id }.toSet().let { segmentA !in it || segmentB !in it } }
            ?: throw MapEditException(NOT_CONFIRMED)
        runCatching { rooms() }
    }

    /**
     * Set a room's floor type. Returns false when the command succeeded but this robot's map does
     * not report floor types, so the change could not be confirmed.
     */
    suspend fun setFloorMaterial(segment: Int, material: FloorMaterial, direction: Int?): Boolean =
        mapEditMutex.withLock {
            val map = editableMap()
            if (map.rooms.none { it.id == segment }) throw MapEditException("That room is not on the current map")
            if (direction != null && material != FloorMaterial.WOOD) {
                throw MapEditException("Only wood floors have a direction")
            }
            session.inEditSession { session.setSegmentGroundMaterial(segment, material.id, direction) }
            if (map.segmentMaterials.isEmpty()) {
                runCatching { fetchMap() }
                return@withLock false
            }
            awaitMap { m ->
                m.segmentMaterials[segment] == material.id &&
                    (direction == null || m.segmentMaterialDirections[segment] == direction)
            } ?: throw MapEditException(NOT_CONFIRMED)
            true
        }

    /**
     * Fails when [segment] can't be given a room, so the caller can stop before it creates a cloud
     * room that would be left unused (#33).
     */
    suspend fun checkRoomTable(segment: Int): Unit = mapEditMutex.withLock { readSegmentTable(segment) }

    private suspend fun readSegmentTable(segment: Int): List<SegmentEntry> {
        val map = editableMap()
        if (map.rooms.none { it.id == segment }) throw MapEditException("That room is not on the current map")
        rooms()
        return rawSegmentTable
            ?: throw MapEditException("The robot did not return its room table. Nothing was changed.")
    }

    /**
     * Point [segment] at cloud room [roomId] with name_segment, sending the robot's full table with
     * only this entry changed. The robot may renumber segments in its answer; the check follows
     * that. Confirmed by reading the table back.
     */
    suspend fun assignRoom(segment: Int, roomId: Long, tagId: Int? = null): Unit = mapEditMutex.withLock {
        val table = readSegmentTable(segment)
        val existing = table.find { it.segment == segment }
        val entry = SegmentEntry(segment, roomId.toString(), tagId ?: existing?.tagId ?: NEW_SEGMENT_TAG)
        val resp = session.nameSegment(table.filter { it.segment != segment } + entry)
        val finalSegment = parseSegmentRenumbering(resp.result)[segment] ?: segment
        // The table can lag the robot's answer by a moment, so look a few times before giving up.
        var confirmed = false
        for (attempt in 0 until 3) {
            if (attempt > 0) delay(STATUS_READBACK_DELAY_MS)
            rooms()
            confirmed = rawSegmentTable?.find { it.segment == finalSegment }?.roomId == roomId.toString()
            if (confirmed) break
        }
        if (!confirmed) throw MapEditException(NOT_CONFIRMED)
    }

    /** Why the robot's saved maps can't be renamed, deleted or rebuilt right now, or null. */
    fun mapManageBlockedReason(state: Int? = _status.value.state): String? = when {
        demo -> "Managing maps is not available in demo mode"
        state in ACTIVE_STATES -> "The robot is busy. Dock it or let it finish first."
        else -> null
    }

    /** A fresh map list that holds [mapFlag], or an error saying why not. */
    private suspend fun requireFloorMap(mapFlag: Int): FloorMap {
        mapManageBlockedReason()?.let { throw MapEditException(it) }
        runCatching { fetchFloorMaps() }
        if (!_floorMapsLoaded.value) throw MapEditException("Couldn't read the robot's saved maps. Nothing was changed.")
        return _floorMaps.value.find { it.mapFlag == mapFlag }
            ?: throw MapEditException("That map is no longer on the robot")
    }

    /** Rename a saved map. Confirmed by reading the map list back. */
    suspend fun renameFloorMap(mapFlag: Int, name: String): Unit = mapEditMutex.withLock {
        val clean = name.trim()
        mapNameBlocker(clean)?.let { throw MapEditException(it) }
        requireFloorMap(mapFlag)
        session.nameMultiMap(mapFlag, clean)
        fetchFloorMaps()
        val saved = _floorMaps.value.find { it.mapFlag == mapFlag }?.name
        if (saved != clean) throw MapEditException(NOT_CONFIRMED)
    }

    /**
     * Delete a saved map. The robot forgets its rooms and everything drawn on it; room names stay
     * on the server but no longer point anywhere. Confirmed by the map leaving the list.
     */
    suspend fun deleteFloorMap(mapFlag: Int): Unit = mapEditMutex.withLock {
        requireFloorMap(mapFlag)
        session.deleteMap(mapFlag)
        fetchFloorMaps()
        if (_floorMaps.value.any { it.mapFlag == mapFlag }) throw MapEditException(NOT_CONFIRMED)
        _parsedMap.value = null
        _mapBytes.value = null
        _rooms.value = emptyList()
        rawSegmentTable = null
    }

    /**
     * Replace [mapFlag]'s on-robot backup with the map as it is now. Confirmed by the backup's time
     * changing in the map list; it is the robot's clock, so it is compared with the old time.
     */
    suspend fun backupFloorMap(mapFlag: Int): Unit = mapEditMutex.withLock {
        val before = requireFloorMap(mapFlag).backupAt
        session.backupMap(mapFlag)
        fetchFloorMaps()
        val after = _floorMaps.value.find { it.mapFlag == mapFlag }?.backupAt
        if (after == null || after == before) throw MapEditException(NOT_CONFIRMED)
    }

    /**
     * Put [mapFlag] back to its on-robot backup. Everything changed on the map since the backup is
     * lost. Only for the loaded map, the case that was captured. Confirmed by the map's saved time
     * changing, as it did right after the robot's "ok" in the capture.
     */
    suspend fun restoreFloorMapBackup(mapFlag: Int): Unit = mapEditMutex.withLock {
        val floor = requireFloorMap(mapFlag)
        if (floor.backupAt == null) throw MapEditException("This map has no backup on the robot yet")
        if (mapFlag != currentFloorFlag.value) throw MapEditException("Load this map first, then restore its backup")
        session.restoreMapBackup(mapFlag)
        var restored = false
        for (attempt in 0 until STATUS_READBACK_ATTEMPTS) {
            if (attempt > 0) delay(STATUS_READBACK_DELAY_MS)
            runCatching { fetchFloorMaps() }
            val savedAt = _floorMaps.value.find { it.mapFlag == mapFlag }?.savedAt
            restored = savedAt != null && savedAt != floor.savedAt
            if (restored) break
        }
        if (!restored) throw MapEditException(NOT_CONFIRMED)
        runCatching { refreshStatus() }
        rawSegmentTable = null
        runCatching { fetchMap() }
        runCatching { rooms() }
    }

    /**
     * Send the robot out to map the home from its dock, while the robot has room for another map
     * (captured with none left on a one-map robot, and with 2 of 4). The new map is kept only
     * once [saveNewMap] stores it.
     */
    suspend fun startMapping(): Unit = mapEditMutex.withLock {
        mapManageBlockedReason()?.let { throw MapEditException(it) }
        runCatching { fetchFloorMaps() }
        if (!_floorMapsLoaded.value) throw MapEditException("Couldn't read the robot's saved maps. Nothing was started.")
        val max = _maxFloorMaps.value ?: 1
        if (_floorMaps.value.size >= max) {
            throw MapEditException(
                if (max <= 1) "The robot still has a saved map. Delete it first to map the home again."
                else "The robot already holds $max maps, its limit. Delete one first."
            )
        }
        session.startBuildMap()
    }

    /**
     * Turn multi-level maps on or off. Turning them off keeps only [keepMapFlag] and the robot
     * deletes every other saved map (captured). Confirmed by status and the map list.
     */
    suspend fun setMultiLevel(enabled: Boolean, keepMapFlag: Int?): Unit = mapEditMutex.withLock {
        mapManageBlockedReason()?.let { throw MapEditException(it) }
        runCatching { refreshStatus() }
        val lab = _status.value.labStatus
            ?: throw MapEditException("The robot didn't report its map settings. Nothing was changed.")
        if (enabled) {
            // 3, what the official app sent. From 0 (map saving off) the multi-level bit alone
            // was refused with -10005 (issue #17).
            session.setLabStatus(lab or LAB_STATUS_MAP_SAVING or LAB_STATUS_MULTI_LEVEL, reserveMap = null)
            // The official app followed this with manual map selection.
            session.setSwitchMapMode(SWITCH_MAP_MODE_MANUAL)
        } else {
            runCatching { fetchFloorMaps() }
            if (!_floorMapsLoaded.value) throw MapEditException("Couldn't read the robot's saved maps. Nothing was changed.")
            val keep = keepMapFlag ?: _floorMaps.value.singleOrNull()?.mapFlag
                ?: throw MapEditException("Pick the map to keep")
            if (_floorMaps.value.none { it.mapFlag == keep }) throw MapEditException("That map is no longer on the robot")
            session.setLabStatus(lab and LAB_STATUS_MULTI_LEVEL.inv(), reserveMap = keep)
        }
        if (!awaitStatus { it.multiLevelEnabled == enabled }) throw MapEditException(NOT_CONFIRMED)
        runCatching { fetchFloorMaps() }
        if (!enabled) {
            rawSegmentTable = null
            runCatching { fetchMap() }
            runCatching { rooms() }
        }
    }

    /**
     * Turn on map saving for a robot that reports it off, which is how a robot starts out when it
     * never met the official app. Until then it refuses to store a map. Confirmed by status.
     */
    suspend fun enableMapSaving(): Unit = mapEditMutex.withLock {
        mapManageBlockedReason()?.let { throw MapEditException(it) }
        enableMapSavingLocked()
    }

    private suspend fun enableMapSavingLocked() {
        runCatching { refreshStatus() }
        val lab = _status.value.labStatus
            ?: throw MapEditException("The robot didn't report its map settings. Nothing was changed.")
        if (lab and LAB_STATUS_MAP_SAVING != 0) return
        session.setLabStatus(lab or LAB_STATUS_MAP_SAVING, reserveMap = null)
        if (!awaitStatus { it.mapSavingEnabled == true }) throw MapEditException(NOT_CONFIRMED)
    }

    /** Recognise the floor automatically, or pick the map by hand. Confirmed by status. */
    suspend fun setSmartMapSwitching(smart: Boolean): Unit = mapEditMutex.withLock {
        mapManageBlockedReason()?.let { throw MapEditException(it) }
        val mode = if (smart) SWITCH_MAP_MODE_SMART else SWITCH_MAP_MODE_MANUAL
        session.setSwitchMapMode(mode)
        if (!awaitStatus { it.switchMapMode == mode }) throw MapEditException(NOT_CONFIRMED)
    }

    /** Status caught up 1-2 s after the robot's "ok" in the capture, so poll briefly. */
    private suspend fun awaitStatus(check: (VacuumStatus) -> Boolean): Boolean {
        repeat(STATUS_READBACK_ATTEMPTS) { attempt ->
            if (attempt > 0) delay(STATUS_READBACK_DELAY_MS)
            runCatching { refreshStatus() }
            if (check(_status.value)) return true
        }
        return false
    }

    /**
     * Save the map the robot just built as a new saved map, as the official app does after a
     * mapping run is stopped. Confirmed by a new entry in the map list.
     */
    suspend fun saveNewMap(): Unit = mapEditMutex.withLock {
        mapManageBlockedReason()?.let { throw MapEditException(it) }
        if (!unsavedMapPresent.value) throw MapEditException("There is no new map to save")
        runCatching { fetchFloorMaps() }
        val before = _floorMaps.value.map { it.mapFlag }.toSet()
        // With map saving off the robot answers manual_segment_map with "The Lab Status is disable".
        if (_status.value.mapSavingEnabled == false) enableMapSavingLocked()
        // Not seen on a robot: whether a map built while map saving was off is still there once
        // it is on, so only ask for the save if the robot still reports the unsaved map.
        val stillUnsaved = _status.value.unsavedMapPresent
        if (stillUnsaved) session.saveNewMap()
        fetchFloorMaps()
        val added = _floorMaps.value.map { it.mapFlag }.toSet() - before
        if (added.isEmpty()) throw MapEditException(if (stillUnsaved) NOT_CONFIRMED else MAP_NOT_KEPT)
        runCatching { refreshStatus() }
        rawSegmentTable = null
        runCatching { fetchMap() }
        runCatching { rooms() }
    }

    suspend fun fetchTimers() {
        val resp = session.getTimer()
        val result = resp.result ?: return
        if (result !is JsonArray) return
        val parsed = result.mapNotNull { entry ->
            if (entry !is JsonArray || entry.size < 3) return@mapNotNull null
            val id = entry[0].jsonPrimitive.contentOrNull ?: return@mapNotNull null
            val enabled = entry[1].jsonPrimitive.contentOrNull == "on"
            val details = runCatching { entry[2].jsonArray }.getOrNull() ?: return@mapNotNull null
            val cron = details.firstOrNull()?.jsonPrimitive?.contentOrNull ?: return@mapNotNull null
            parseCronTimer(id, enabled, cron)
        }
        _timers.value = parsed
    }

    suspend fun createTimer(hour: Int, minute: Int, weekdays: Set<Int>): CleanTimer {
        val id = nowEpochSeconds().toString()
        val cron = buildCron(minute, hour, weekdays)
        session.setTimer(id, enabled = true, cron = cron).requireOk("set_timer")
        val timer = CleanTimer(id = id, enabled = true, hour = hour, minute = minute, weekdays = weekdays)
        _timers.value = _timers.value + timer
        return timer
    }

    suspend fun toggleTimer(timer: CleanTimer) {
        val updated = timer.copy(enabled = !timer.enabled)
        session.updTimer(timer.id, updated.enabled).requireOk("upd_timer")
        _timers.value = _timers.value.map { if (it.id == timer.id) updated else it }
    }

    suspend fun deleteTimer(timer: CleanTimer) {
        session.delTimer(timer.id).requireOk("del_timer")
        _timers.value = _timers.value.filter { it.id != timer.id }
    }

    // Each read stands alone: a Qrevo answers the volume as [45], the LED as [1] and the carpet mode
    // as [{...}], and one read that threw used to skip every read after it.
    suspend fun fetchRobotSettings() = reading { readRobotSettings() }

    private suspend fun readRobotSettings() {
        suspend fun read(key: String?, ask: suspend () -> V1Response): Int? =
            runCatching { ask().result.asAnswerInt(key) }.getOrNull()

        read(null) { session.getSoundVolume() }
            ?.let { _robotSettings.update { s -> s.copy(volume = it) } }
        read("lock_status") { session.getChildLock() }
            ?.let { _robotSettings.update { s -> s.copy(childLock = it == 1) } }
        read("led_status") { session.getLedStatus() }
            ?.let { _robotSettings.update { s -> s.copy(ledEnabled = it == 1) } }
        read("carpet_clean_mode") { session.getCarpetCleanMode() }
            ?.let { _robotSettings.update { s -> s.copy(carpetMode = it) } }
    }

    // The screen only changes once the robot has said ok, so a refused change is not shown as if it
    // had been applied (#32).
    // After the ok the robot plays a sample, so the new level can be heard. The sample is a courtesy:
    // a robot that does not know test_sound_volume, or does not answer it, still has the new volume.
    suspend fun setVolume(volume: Int) {
        session.setSoundVolume(volume).requireOk(CHANGE_SOUND_VOLUME)
        _robotSettings.update { it.copy(volume = volume) }
        if (volume > 0) runCatching { session.testSoundVolume() }
    }

    // Read back after the ok, so the switch shows what the robot holds; the tapped value stands in if
    // the read fails.
    suspend fun setChildLock(locked: Boolean) {
        session.setChildLock(locked).requireOk(SET_CHILD_LOCK_STATUS)
        val held = runCatching { session.getChildLock().result.asAnswerInt("lock_status") }.getOrNull()
        _robotSettings.update { it.copy(childLock = held?.let { v -> v == 1 } ?: locked) }
    }

    suspend fun setLed(enabled: Boolean) {
        session.setLedStatus(enabled).requireOk("set_led_status")
        _robotSettings.update { it.copy(ledEnabled = enabled) }
    }

    suspend fun setCarpetMode(mode: Int) {
        var answer = session.setCarpetCleanMode(mode)
        if (answer.error?.get("code")?.jsonPrimitive?.intOrNull in BetaShapeRefusalCodes) {
            answer = session.setCarpetCleanModeWrapped(mode)
        }
        answer.requireOk("set_carpet_clean_mode")
        _robotSettings.update { it.copy(carpetMode = mode) }
    }

    private val _betaFeatures = MutableStateFlow<List<BetaFeatureState>>(emptyList())
    val betaFeatures: StateFlow<List<BetaFeatureState>> = _betaFeatures

    private val _betaFeaturesLoading = MutableStateFlow(false)
    val betaFeaturesLoading: StateFlow<Boolean> = _betaFeaturesLoading

    /** Null when the robot did not reply at all, so a read that timed out is not mistaken for an answer. */
    private suspend fun readBetaFeature(def: BetaFeatureDef): BetaFeatureState? {
        val resp = runCatching { session.sendCommandRaw(def.getMethod, featureReadParams(def)) }.getOrNull() ?: return null
        val parsed = readFeatureReply(def, resp.result)
        return BetaFeatureState(
            def = def,
            value = parsed?.second,
            key = parsed?.first,
            raw = resp.result?.toString() ?: resp.error?.toString(),
            answered = resp.error == null,
        )
    }

    private var featuresRead = false

    /** Options set while [fetchBetaFeatures] runs; their newer value wins over the one it read before. */
    private val setDuringRead = mutableSetOf<String>()

    /** Shows the options the robot reported last time, until [fetchBetaFeatures] has read them again. */
    fun restoreCachedFeatures(defs: List<BetaFeatureDef>) {
        if (_betaFeatures.value.isEmpty()) cached?.featureStates(defs)?.let { _betaFeatures.value = it }
    }

    /**
     * Reads every option again. The list on screen stays until all have answered, then is replaced at
     * once, so rows don't appear one by one. Stops at the first option the robot does not reply to, so
     * an offline robot costs one timeout, not one per option, and keeps what was shown.
     */
    suspend fun fetchBetaFeatures(defs: List<BetaFeatureDef>) = reading {
        _betaFeaturesLoading.value = true
        setDuringRead.clear()
        try {
            val states = mutableListOf<BetaFeatureState>()
            var complete = true
            for (def in defs) {
                val state = readBetaFeature(def)
                if (state == null) { complete = false; break }
                if (state.answered && state.toggleable) states += state
            }
            if (complete || _betaFeatures.value.isEmpty()) {
                val current = _betaFeatures.value.associateBy { it.def.id }
                _betaFeatures.value = states.map { s -> if (s.def.id in setDuringRead) current[s.def.id] ?: s else s }
                featuresRead = complete
                if (complete) persist()
            }
        } finally {
            _betaFeaturesLoading.value = false
        }
    }

    /** Which [betaSetParamCandidates] shape each option last accepted, so later toggles send one write. */
    private val betaSetAcceptedShape = mutableMapOf<String, Int>()

    /**
     * Tries each parameter shape until the robot stops refusing the shape, starting with the one it took
     * last time, then reads the option back so the screen shows what the robot holds. Throws the robot's
     * refusal, if any, after that.
     */
    suspend fun setBetaFeature(id: String, on: Boolean) = setBetaFeature(id, if (on) 1 else 0)

    /** Sets a feature to [value]: 0/1 for a switch, one of its choices for a picker. */
    suspend fun setBetaFeature(id: String, value: Int) {
        val current = _betaFeatures.value.firstOrNull { it.def.id == id } ?: return
        val candidates = if (current.def.bitmaskWrite) {
            // Read the whole bitmask now, so other options in it (set elsewhere meanwhile) are kept.
            val raw = parseBetaReply(session.sendCommandRaw(current.def.getMethod, JsonArray(emptyList())).requireOk(current.def.getMethod).result)?.second
                ?: throw IllegalStateException("The robot did not report ${current.def.getMethod}")
            listOf(JsonArray(listOf(JsonPrimitive(bitmaskWithBit(raw, current.def, value)))))
        } else {
            featureSetParamCandidates(current.def, current.key, value)
        }
        var last: V1Response? = null
        for (i in betaSetTryOrder(candidates.size, betaSetAcceptedShape[id])) {
            last = session.sendCommandRaw(current.def.setMethod, candidates[i])
            if (last.error == null) betaSetAcceptedShape[id] = i
            if (last.error?.get("code")?.jsonPrimitive?.intOrNull !in BetaShapeRefusalCodes) break
        }
        val updated = readBetaFeature(current.def)?.takeIf { it.answered && it.toggleable }
        if (updated != null) {
            if (_betaFeaturesLoading.value) setDuringRead += id
            _betaFeatures.update { list -> list.map { if (it.def.id == id) updated else it } }
            if (featuresRead) persist()
        }
        last?.requireOk(current.def.setMethod)
    }

    /**
     * Reads the dock settings. What is shown stays until the robot has answered; a read that fails
     * keeps the last value instead of hiding the row.
     */
    suspend fun fetchDockSettings() = reading {
        val before = _dockSettings.value
        val fresh = MutableStateFlow(DockSettings())
        readDockSettingsInto(fresh)
        val r = fresh.value
        _dockSettings.value = r.copy(
            washMode = r.washMode ?: before.washMode,
            smartWash = r.smartWash ?: before.smartWash,
            autoEmptyMode = r.autoEmptyMode ?: before.autoEmptyMode,
            mopDrying = r.mopDrying ?: before.mopDrying,
            dryTimeSeconds = r.dryTimeSeconds ?: before.dryTimeSeconds,
            dryerAnswer = r.dryerAnswer ?: before.dryerAnswer,
            dryStart = r.dryStart ?: before.dryStart,
            dryScheduleHour = if (r.dryStart != null) r.dryScheduleHour else before.dryScheduleHour,
            dryScheduleMinute = if (r.dryStart != null) r.dryScheduleMinute else before.dryScheduleMinute,
            washWaterTemperature = r.washWaterTemperature ?: before.washWaterTemperature,
        )
        persist()
    }

    private suspend fun readDockSettingsInto(_dockSettings: MutableStateFlow<DockSettings>) {
        val wash = runCatching { session.getWashTowelMode().requireOk("get_wash_towel_mode").result as? JsonObject }.getOrNull()
        _dockSettings.update { it.copy(washMode = (wash?.get("wash_mode") as? JsonPrimitive)?.intOrNull) }
        val frequency = runCatching { parseSmartWashSettings(session.getSmartWashParams().requireOk("get_smart_wash_params").result) }.getOrNull()
        _dockSettings.update { it.copy(smartWash = frequency) }
        val empty = runCatching { session.getDustCollectionMode().requireOk("get_dust_collection_mode").result as? JsonObject }.getOrNull()
        _dockSettings.update { it.copy(autoEmptyMode = (empty?.get("mode") as? JsonPrimitive)?.intOrNull) }
        val dryer = runCatching { session.getDryerSetting().requireOk("app_get_dryer_setting").result as? JsonObject }.getOrNull()
        (dryer?.get("status") as? JsonPrimitive)?.intOrNull?.let { status ->
            val seconds = ((dryer["on"] as? JsonObject)?.get("dry_time") as? JsonPrimitive)?.intOrNull
            val schedule = parseDailyCron((dryer["cron"] as? JsonPrimitive)?.contentOrNull)
            _dockSettings.update {
                it.copy(
                    // A scheduled start (2) still dries the mops; only 0 leaves them wet.
                    mopDrying = status != DRY_START_OFF, dryTimeSeconds = seconds, dryerAnswer = dryer,
                    dryStart = status, dryScheduleHour = schedule?.first, dryScheduleMinute = schedule?.second,
                )
            }
        }
        // The read answers {"wash_water_temperature": n}, where the setter takes {"values": n}.
        val temperature = runCatching { session.getWashWaterTemperature().requireOk("get_wash_water_temperature").result as? JsonObject }.getOrNull()
        _dockSettings.update { it.copy(washWaterTemperature = (temperature?.get("wash_water_temperature") as? JsonPrimitive)?.intOrNull) }
    }

    suspend fun setMopDrying(enabled: Boolean) {
        // Sends back everything the robot reported, so the switch alone cannot change how long a run lasts.
        val reported = _dockSettings.value.dryerAnswer
        val body = dryerSettingBody(reported, enabled, if (reported == null) _dockSettings.value.dryTimeSeconds else null)
        session.setDryerSetting(body).requireOk("app_set_dryer_setting")
        _dockSettings.update { it.copy(mopDrying = enabled, dryerAnswer = body) }
    }

    /** Off, after each clean, or daily at [hour]:[minute]; read back so the screen shows what the robot holds. */
    suspend fun setDryStart(start: Int, hour: Int? = null, minute: Int? = null) {
        val current = _dockSettings.value
        val body = dryerStartBody(current.dryerAnswer, start, hour, minute, current.dryTimeSeconds)
        session.setDryerSetting(body).requireOk("app_set_dryer_setting")
        _dockSettings.update {
            it.copy(
                mopDrying = start != DRY_START_OFF, dryerAnswer = body, dryStart = start,
                dryScheduleHour = if (start == DRY_START_SCHEDULED) hour else it.dryScheduleHour,
                dryScheduleMinute = if (start == DRY_START_SCHEDULED) minute else it.dryScheduleMinute,
            )
        }
    }

    suspend fun setDryTime(seconds: Int) {
        val body = dryerSettingBody(_dockSettings.value.dryerAnswer, _dockSettings.value.mopDrying ?: true, seconds)
        session.setDryerSetting(body).requireOk("app_set_dryer_setting")
        _dockSettings.update { it.copy(dryTimeSeconds = seconds, dryerAnswer = body) }
    }

    suspend fun setWashWaterTemperature(value: Int) {
        session.setWashWaterTemperature(value).requireOk("set_wash_water_temperature")
        _dockSettings.update { it.copy(washWaterTemperature = value) }
    }

    private val _valleySettings = MutableStateFlow<ValleySettings?>(null)
    val valleySettings: StateFlow<ValleySettings?> = _valleySettings

    suspend fun fetchValleySettings() {
        val answer = session.getValleyElectricityTimer().requireOk("get_valley_electricity_timer").result
        val obj = (answer as? JsonArray)?.firstOrNull() as? JsonObject ?: answer as? JsonObject ?: return
        fun int(key: String) = (obj[key] as? JsonPrimitive)?.intOrNull
        _valleySettings.value = ValleySettings(
            enabled = int("enabled") == 1,
            startHour = int("start_hour") ?: 22,
            startMinute = int("start_minute") ?: 0,
            endHour = int("end_hour") ?: 6,
            endMinute = int("end_minute") ?: 0,
        )
    }

    suspend fun setValley(startHour: Int, startMinute: Int, endHour: Int, endMinute: Int) {
        session.setValleyElectricityTimer(startHour, startMinute, endHour, endMinute).requireOk("set_valley_electricity_timer")
        _valleySettings.value = ValleySettings(true, startHour, startMinute, endHour, endMinute)
    }

    suspend fun disableValley() {
        session.closeValleyElectricityTimer().requireOk("close_valley_electricity_timer")
        _valleySettings.update { it?.copy(enabled = false) }
    }

    suspend fun setWashMode(mode: Int) {
        session.setWashTowelMode(mode).requireOk("set_wash_towel_mode")
        _dockSettings.update { it.copy(washMode = mode) }
    }

    suspend fun setWashFrequency(mode: Int, intervalMinutes: Int?) {
        // Room/smart modes still carry an interval. Preserve the robot's current value.
        val current = parseSmartWashSettings(session.getSmartWashParams().requireOk("get_smart_wash_params").result)
            ?: throw IllegalStateException("The robot's mop washing frequency is unavailable")
        _dockSettings.update { it.copy(smartWash = current) }
        val updated = current.withFrequency(mode, intervalMinutes)
        session.setSmartWashParams(updated).requireOk("set_smart_wash_params")
        _dockSettings.update { it.copy(smartWash = updated) }
    }

    suspend fun setAutoEmptyMode(mode: Int) {
        session.setDustCollectionMode(mode).requireOk("set_dust_collection_mode")
        _dockSettings.update { it.copy(autoEmptyMode = mode) }
    }

    /** Pumps the robot's built-in water tank out through the dock. A refusal is raised, not ignored. */
    suspend fun emptyInbuiltWaterTank() {
        session.emptyInbuiltWaterTank().requireOk(APP_EMPTY_INBUILT_WATER_TANK)
    }


    suspend fun fetchCleanSummary(): List<Long> {
        val result = session.getCleanSummary().result ?: return emptyList()
        return when (result) {
            is JsonArray -> {
                if (result.size < 3) return emptyList()
                _cleanSummary.value = CleanSummary(
                    totalTimeSec = (result[0] as? JsonPrimitive)?.longOrNull ?: 0L,
                    totalAreaMm2 = (result[1] as? JsonPrimitive)?.longOrNull ?: 0L,
                    totalCount = (result[2] as? JsonPrimitive)?.intOrNull ?: 0,
                )
                (result.getOrNull(3) as? JsonArray)
                    ?.mapNotNull { (it as? JsonPrimitive)?.longOrNull }
                    ?: emptyList()
            }
            is JsonObject -> {
                _cleanSummary.value = CleanSummary(
                    totalTimeSec = result["clean_time"]?.jsonPrimitive?.longOrNull ?: 0L,
                    totalAreaMm2 = result["clean_area"]?.jsonPrimitive?.longOrNull ?: 0L,
                    totalCount = result["clean_count"]?.jsonPrimitive?.intOrNull ?: 0,
                    dustCollectionCount = (result["dust_collection_count"] as? JsonPrimitive)?.intOrNull,
                )
                (result["records"] as? JsonArray)
                    ?.mapNotNull { (it as? JsonPrimitive)?.longOrNull }
                    ?: emptyList()
            }
            else -> emptyList()
        }
    }


    private val _dndPeriods = MutableStateFlow<List<DndPeriod>?>(null)

    /** Every quiet-hour period, on robots with several (bit 140); null on others or before the robot answered. */
    val dndPeriods: StateFlow<List<DndPeriod>?> = _dndPeriods

    /** Reads the periods of a robot with several; does nothing on other robots. */
    suspend fun fetchDndPeriods() = reading { readDndPeriods() }

    private suspend fun readDndPeriods() {
        runCatching { fetchRobotFeatureSet() }
        if (!newFeatureBit(_robotFeatureSet.value, MULTI_DND_BIT)) return
        _dndPeriods.value = parseMultiDnd(session.getMultiDndTimer().requireOk("get_multi_dnd_timer").result)
    }

    /** Replaces every period with [periods], as the official app does, and reads them back. */
    suspend fun setDndPeriods(periods: List<DndPeriod>) {
        require(periods.size <= MAX_DND_PERIODS) { "At most $MAX_DND_PERIODS quiet-hour periods" }
        require(periods.map { it.times }.distinct().size == periods.size) { "This period already exists" }
        session.setMultiDndTimer(periods).requireOk("set_multi_dnd_timer")
        readDndPeriods()
        // The robot keeps its periods sorted by start time (a296), so the order is not compared.
        fun key(list: List<DndPeriod>?) = list?.map { it.times to it.enabled }?.sortedBy { it.first[0] * 60 + it.first[1] }
        if (key(_dndPeriods.value) != key(periods)) {
            throw IllegalStateException("The robot accepted the quiet hours, but reports different ones back.")
        }
    }

    suspend fun fetchDndSettings() = reading { readDndSettings() }

    private suspend fun readDndSettings() {
        val resp = session.getDndTimer()
        val obj = resp.result?.let { r ->
            when (r) {
                is JsonArray -> r.firstOrNull()?.jsonObject
                is JsonObject -> r
                else -> null
            }
        } ?: return
        _dndSettings.value = DndSettings(
            enabled = obj["enabled"]?.jsonPrimitive?.intOrNull == 1,
            startHour = obj["start_hour"]?.jsonPrimitive?.intOrNull ?: 22,
            startMinute = obj["start_minute"]?.jsonPrimitive?.intOrNull ?: 0,
            endHour = obj["end_hour"]?.jsonPrimitive?.intOrNull ?: 8,
            endMinute = obj["end_minute"]?.jsonPrimitive?.intOrNull ?: 0,
            actions = obj["actions"] as? JsonObject,
        )
    }

    /**
     * Turns quiet hours on. Robots with several periods (bit 140) or that report quiet-hour actions get
     * `set_multi_dnd_timer`, which the official app sends; an a296 refused `set_dnd_timer`.
     */
    suspend fun setDnd(startHour: Int, startMinute: Int, endHour: Int, endMinute: Int) {
        val actions = _dndSettings.value?.actions
        if (actions != null || newFeatureBit(_robotFeatureSet.value, MULTI_DND_BIT)) {
            session.setMultiDndTimer(startHour, startMinute, endHour, endMinute, actions).requireOk("set_multi_dnd_timer")
        } else {
            session.setDndTimer(startHour, startMinute, endHour, endMinute).requireOk("set_dnd_timer")
        }
        _dndSettings.update {
            it?.copy(enabled = true, startHour = startHour, startMinute = startMinute, endHour = endHour, endMinute = endMinute)
                ?: DndSettings(true, startHour, startMinute, endHour, endMinute)
        }
    }

    suspend fun disableDnd() {
        session.closeDndTimer().requireOk("close_dnd_timer")
        _dndSettings.update { it?.copy(enabled = false) }
    }


    /** Loads the robot's saved-map list into [floorMaps]. */
    suspend fun fetchFloorMaps() {
        val resp = session.getMultiMapsList()
        val obj = resp.result?.let { r ->
            when (r) {
                is JsonArray -> r.firstOrNull()?.jsonObject
                is JsonObject -> r
                else -> null
            }
        } ?: return
        val mapInfoArr = obj["map_info"]?.jsonArray ?: return
        _maxFloorMaps.value = obj["max_multi_map"]?.jsonPrimitive?.intOrNull
        _floorMaps.value = parseFloorMapList(mapInfoArr)
        _floorMapsLoaded.value = true
    }

    suspend fun switchFloor(mapFlag: Int) {
        mapManageBlockedReason()?.let { throw MapEditException(it) }
        session.loadMultiMap(mapFlag)
        _currentFloorFlag.value = mapFlag
        runCatching { refreshStatus() }
        rawSegmentTable = null
        _parsedMap.value = null
        _mapBytes.value = null
        runCatching { fetchMap() }
        // A Saros 20 took up to a minute to load the other map and kept sending the old one until
        // then, which showed under the new map's name (#54). Ask again often until it has caught up.
        floorSwitchRefreshJob?.cancel()
        floorSwitchRefreshJob = scope.launch {
            repeat(FLOOR_SWITCH_MAP_REFRESHES) {
                delay(MAP_FAST_POLL_MS)
                runCatching { fetchMap() }
            }
        }
    }


    /** Seeds fabricated state for offline demo mode; no MQTT session is started. */
    private fun seedDemo() {
        _status.value = com.kodraliu.localrock.shared.demo.DemoData.status
        _consumableStatus.value = com.kodraliu.localrock.shared.demo.DemoData.consumable
        _cleanSummary.value = com.kodraliu.localrock.shared.demo.DemoData.cleanSummary
        _mapStatus.value = "Demo mode — live map unavailable"
    }

    private val mapFetchMutex = Mutex()

    /**
     * Serialised because the poll loop and the user's refresh button both call this, and
     * [VacuumSession] tracks a single outstanding map request — two at once orphaned the first.
     */
    suspend fun fetchMap(): MapResponse = mapFetchMutex.withLock { fetchMapLocked() }

    /**
     * A saved map, or with [backup] its on-robot backup, for showing in the map list. It takes its
     * turn with the live map, since the session tracks one map request at a time.
     */
    suspend fun fetchSavedMap(mapFlag: Int, backup: Boolean): ParsedMap = mapFetchMutex.withLock {
        val resp = session.fetchSavedMap(createSecurityData(rriotKeyProvider()), mapFlag, backup)
        runCatching { parseB01Map(resp.data) }.getOrNull()
            ?: runCatching { parseLegacyMap(resp.data) }.getOrNull()
            ?: throw MapEditException("The robot sent a map this app can't read")
    }

    /** The map of a past clean (its path included); null when the robot no longer has it. */
    suspend fun fetchCleanRecordMap(begin: Long): ParsedMap? = mapFetchMutex.withLock {
        val resp = runCatching { session.fetchCleanRecordMap(createSecurityData(rriotKeyProvider()), begin) }.getOrNull()
            ?: return@withLock null
        runCatching { parseB01Map(resp.data) }.getOrNull() ?: runCatching { parseLegacyMap(resp.data) }.getOrNull()
    }

    /** Every event of a past clean, page by page as the robot hands them out (at most 20 pages). */
    suspend fun fetchCleanEvents(begin: Long): List<CleanEvent> {
        val all = mutableListOf<CleanEvent>()
        repeat(20) {
            val page = parseCleanEventPage(session.getCleanDetail(begin, all.size).requireOk("get_clean_detail").result)
                ?: return all
            all += page.events
            if (!page.more || page.events.isEmpty()) return all
        }
        return all
    }

    private val photoMutex = Mutex()
    private val obstaclePhotos = mutableMapOf<String, ByteArray>()

    /** The photo the robot keeps for an obstacle on the map (#36), fetched once per photo id. */
    suspend fun fetchObstaclePhoto(photoId: String): ByteArray = photoMutex.withLock {
        obstaclePhotos[photoId]
            ?: session.fetchPhoto(createSecurityData(rriotKeyProvider()), photoId).also { obstaclePhotos[photoId] = it }
    }

    private suspend fun fetchMapLocked(): MapResponse {
        if (demo) {
            _mapStatus.value = "Demo mode — live map unavailable"
            return MapResponse(requestId = 0, data = ByteArray(0))
        }
        _mapStatus.value = "requesting…"
        val sd = createSecurityData(rriotKeyProvider())
        try {
            val resp = session.fetchMap(sd, timeoutMs = 60_000L)
            _mapBytes.value = resp.data
            val sizeKb = resp.data.size / 1024
            // Try B01 first (Q-series / newer firmware), then legacy "rr"-magic (S-series).
            val b01 = runCatching { parseB01Map(resp.data) }
            val parsed = b01.getOrNull()
                ?: runCatching { parseLegacyMap(resp.data) }.getOrNull()
            _parsedMap.value = parsed
            // Debug builds keep the latest map, to work out blocks (curtains, furniture, ...) with adb.
            if (isDebugBuild) runCatching { saveDebugBlob("map-latest.bin", resp.data) }
            if (parsed != null && parsed.rooms.isNotEmpty()) {
                _rooms.value = parsed.rooms
            }
            applyRoomNames()
            if (parsed == null) {
                val first8 = resp.data.take(8).joinToString("") {
                    val v = it.toInt() and 0xff
                    "${HEX[v ushr 4]}${HEX[v and 0x0f]}"
                }
                val savedPath = saveDebugBlob("vaclocal-map-${duid}-${nowEpochSeconds()}.bin", resp.data)
                val saved = savedPath?.let { " • saved → $it" } ?: ""
                _mapStatus.value = "ok: $sizeKb KB, no parser matched — head=$first8$saved"
            } else {
                val format = if (b01.isSuccess) "B01" else "legacy"
                _mapStatus.value = "ok: $sizeKb KB, ${parsed.width}×${parsed.height} $format"
            }
            return resp
        } catch (e: Throwable) {
            _mapStatus.value = "failed: ${e::class.simpleName}: ${e.message ?: "(no message)"}"
            throw e
        }
    }

    private companion object {
        val HEX = charArrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f')
        val ConsumableJson = Json { ignoreUnknownKeys = true; explicitNulls = false }
        const val MAP_FAST_POLL_MS = 8_000L
        const val MAP_IDLE_POLL_MS = 60_000L
        const val MAP_RETRY_MS = 5_000L
        /** Map requests after a floor switch, [MAP_FAST_POLL_MS] apart: about a minute and a half. */
        const val FLOOR_SWITCH_MAP_REFRESHES = 12
        const val MAX_HISTORY_RECORDS = 20

        const val READBACK_ATTEMPTS = 4
        const val READBACK_DELAY_MS = 2_000L
        const val SPLIT_RETRY_DELAY_MS = 12_000L
        /**
         * Tag for a segment that gets its first table entry. In the capture the official app gave
         * the previously unlisted segment 24 tag 12 when the user named it.
         */
        const val NEW_SEGMENT_TAG = 12
        const val STATUS_READBACK_ATTEMPTS = 6
        const val STATUS_READBACK_DELAY_MS = 1_000L

        const val NOT_CONFIRMED =
            "The robot accepted the change, but the map does not show it yet. Refresh the map in a moment to check."

        const val ROOM_MODES_NOT_CONFIRMED =
            "The robot accepted the room settings, but does not report them back. Open the cleaning mode again to check."

        const val MAP_NOT_KEPT =
            "Map saving is now on, but the robot did not keep the map it had built. " +
                "Let it clean or map the home once more, then save the map."

        const val STALE_LINK_FAILURE_THRESHOLD = 3

        /** Silence that, together with failing polls, means the link really is gone. */
        const val STALE_LINK_SILENCE_MS = 30_000L
        val ACTIVE_STATES = setOf(
            VacuumStateCodes.STARTING,
            VacuumStateCodes.CLEANING,
            VacuumStateCodes.RETURNING_HOME,
            VacuumStateCodes.MANUAL_MODE,
            VacuumStateCodes.PAUSED,
            VacuumStateCodes.SPOT_CLEANING,
            VacuumStateCodes.DOCKING,
            VacuumStateCodes.GOING_TO_TARGET,
            VacuumStateCodes.ZONED_CLEANING,
            VacuumStateCodes.SEGMENT_CLEANING,
            VacuumStateCodes.MAPPING,
        )
    }
}

private fun buildCron(minute: Int, hour: Int, weekdays: Set<Int>): String {
    val days = if (weekdays.isEmpty()) "*" else weekdays.sorted().joinToString(",")
    return "$minute $hour * * $days"
}

private fun parseCronTimer(id: String, enabled: Boolean, cron: String): CleanTimer? {
    val parts = cron.trim().split(Regex("\\s+"))
    if (parts.size < 5) return null
    val minute = parts[0].toIntOrNull() ?: return null
    val hour = parts[1].toIntOrNull() ?: return null
    val weekdays = if (parts[4] == "*") emptySet()
    else parts[4].split(",").mapNotNull { it.toIntOrNull() }.toSet()
    return CleanTimer(id = id, enabled = enabled, hour = hour, minute = minute, weekdays = weekdays)
}

const val CONSUMABLE_UNSUPPORTED = "unsupported"

/**
 * Segment id -> Roborock room id from a `get_room_mapping` result (`[[segment, "roomId", tag], ...]`).
 * Only the first two columns are read, which every firmware returns.
 */
internal fun parseRoomMapping(result: JsonElement?): Map<Int, Long> {
    if (result !is JsonArray) return emptyMap()
    return result.mapNotNull { entry ->
        if (entry is JsonArray && entry.size >= 2) {
            val segment = entry[0].jsonPrimitive.intOrNull ?: return@mapNotNull null
            val roomId = entry[1].jsonPrimitive.content.toLongOrNull() ?: return@mapNotNull null
            segment to roomId
        } else null
    }.toMap()
}


internal fun VacuumStatus.merge(fresh: VacuumStatus): VacuumStatus = fresh.copy(
    battery = fresh.battery ?: battery,
    state = fresh.state ?: state,
    fanPower = fresh.fanPower ?: fanPower,
    cleanArea = fresh.cleanArea ?: cleanArea,
    cleanTime = fresh.cleanTime ?: cleanTime,
    errorCode = fresh.errorCode ?: errorCode,
    waterBoxStatus = fresh.waterBoxStatus ?: waterBoxStatus,
    waterBoxCustomMode = fresh.waterBoxCustomMode ?: waterBoxCustomMode,
    chargeStatus = fresh.chargeStatus ?: chargeStatus,
    dockErrorStatus = fresh.dockErrorStatus ?: dockErrorStatus,
    dockType = fresh.dockType ?: dockType,
    dockStatus = fresh.dockStatus ?: dockStatus,
    dryStatus = fresh.dryStatus ?: dryStatus,
    remainingDryTimeSec = fresh.remainingDryTimeSec ?: remainingDryTimeSec,
    mapStatus = fresh.mapStatus ?: mapStatus,
    labStatus = fresh.labStatus ?: labStatus,
    switchMapMode = fresh.switchMapMode ?: switchMapMode,
    inCleaning = fresh.inCleaning ?: inCleaning,
    cleanPercent = fresh.cleanPercent ?: cleanPercent,
)


internal fun VacuumStatus.applyDpsPush(push: Map<Int, JsonElement>): VacuumStatus {
    if (push.isEmpty()) return this
    val state = push[121]?.jsonPrimitive?.intOrNull ?: state
    val battery = push[122]?.jsonPrimitive?.intOrNull ?: battery
    val fanPower = push[123]?.jsonPrimitive?.intOrNull ?: fanPower
    val waterBoxMode = push[124]?.jsonPrimitive?.intOrNull ?: waterBoxCustomMode
    val errorCode = push[120]?.jsonPrimitive?.intOrNull ?: this.errorCode
    val chargeStatus = push[133]?.jsonPrimitive?.intOrNull ?: this.chargeStatus
    val dryStatus = push[134]?.jsonPrimitive?.intOrNull ?: this.dryStatus
    return copy(
        state = state,
        battery = battery,
        fanPower = fanPower,
        waterBoxCustomMode = waterBoxMode,
        errorCode = errorCode,
        chargeStatus = chargeStatus,
        dryStatus = dryStatus,
    )
}

/**
 * A number out of a command answer, whatever shape the firmware used: `45`, `[45]`, `{"key": 45}`
 * or `[{"key": 45}]`. [key] names the field when the answer is an object. Any other shape is null.
 */
internal fun JsonElement?.asAnswerInt(key: String? = null): Int? = when (this) {
    is JsonPrimitive -> intOrNull
    is JsonArray -> firstOrNull().asAnswerInt(key)
    is JsonObject -> if (key == null) null else get(key).asAnswerInt()
    else -> null
}
