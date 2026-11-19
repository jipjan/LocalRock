package com.kodraliu.localrock.ui.vacuum

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kodraliu.localrock.shared.AppContainer
import com.kodraliu.localrock.shared.messages.MessageSeverity
import com.kodraliu.localrock.shared.model.Scene
import com.kodraliu.localrock.shared.model.SceneRunResult
import com.kodraliu.localrock.shared.vacuum.RoutineDraft
import com.kodraliu.localrock.shared.vacuum.RoutineStep
import com.kodraliu.localrock.shared.vacuum.RoutineStepKind
import com.kodraliu.localrock.shared.vacuum.decodeRoutine
import com.kodraliu.localrock.shared.vacuum.encodeRoutineParam
import com.kodraliu.localrock.shared.vacuum.isComplete
import com.kodraliu.localrock.shared.vacuum.RecommendedRoutine
import com.kodraliu.localrock.shared.vacuum.recommendedRoutineDraft
import com.kodraliu.localrock.resources.Res
import com.kodraliu.localrock.resources.routine_editor_need_name
import com.kodraliu.localrock.resources.routine_editor_need_step
import com.kodraliu.localrock.resources.routine_editor_need_target
import com.kodraliu.localrock.resources.routines_deleted
import com.kodraliu.localrock.resources.routines_not_editable
import com.kodraliu.localrock.resources.routines_saved
import com.kodraliu.localrock.resources.robot_firmware_no_info
import com.kodraliu.localrock.resources.robot_firmware_not_checked
import com.kodraliu.localrock.resources.robot_no_answer
import com.kodraliu.localrock.resources.robot_order_removed
import com.kodraliu.localrock.resources.robot_order_saved
import com.kodraliu.localrock.resources.robot_routine_not_started
import com.kodraliu.localrock.resources.robot_routine_not_started_status
import com.kodraliu.localrock.resources.robot_routine_started
import com.kodraliu.localrock.resources.robot_routine_still_running
import com.kodraliu.localrock.resources.robot_routine_stopping
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString
import com.kodraliu.localrock.shared.vacuum.edited
import kotlin.coroutines.cancellation.CancellationException
import com.kodraliu.localrock.shared.model.namesAVersion
import com.kodraliu.localrock.shared.vacuum.carpetModeOptions
import com.kodraliu.localrock.shared.vacuum.newFeatureBit
import com.kodraliu.localrock.shared.vacuum.DockCapabilities
import com.kodraliu.localrock.shared.vacuum.FeatureSection
import com.kodraliu.localrock.shared.vacuum.MapEditException
import com.kodraliu.localrock.shared.vacuum.RoomType
import com.kodraliu.localrock.shared.vacuum.VacuumErrorCodes
import com.kodraliu.localrock.shared.vacuum.VacuumRepository
import com.kodraliu.localrock.shared.vacuum.isSmartPlanSupported
import com.kodraliu.localrock.shared.vacuum.isWaterSlideSupported
import com.kodraliu.localrock.shared.vacuum.mopRouteOptions
import com.kodraliu.localrock.shared.vacuum.VoicePack
import com.kodraliu.localrock.shared.vacuum.voicePackBucket
import com.kodraliu.localrock.shared.vacuum.isWaterDistanceSupported
import com.kodraliu.localrock.shared.vacuum.isCornerCleanSupported
import com.kodraliu.localrock.shared.vacuum.supportedBetaFeatures
import com.kodraliu.localrock.shared.vacuum.unlistedAdvertisedFeatures
import kotlinx.coroutines.Job
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class VacuumViewModel(
    duid: String,
    private val container: AppContainer,
) : ViewModel() {

    private val device = requireNotNull(container.deviceRepository.devices.value.find { it.duid == duid }) {
        "Device $duid not found — navigate to DeviceList first to load devices"
    }

    val deviceName: String = device.name
    val modelName: String? = container.deviceRepository.productsByDuid.value[duid]?.model
    val firmwareVersion: String? = device.fv
    private val serverFeatureSet = device.newFeatureSet
    val dockCapabilities = DockCapabilities(modelName, device.featureSet, device.newFeatureSet)
    val betaFeatureDefs = supportedBetaFeatures(device.newFeatureSet)
    val unlistedFeatures = unlistedAdvertisedFeatures(device.newFeatureSet)
    val carpetModes = carpetModeOptions(device.newFeatureSet)
    /** Remote viewing: TwoKeyRealTimeVideo (32) or TwoKeyRTVInCharging (33). */
    val hasCamera = newFeatureBit(device.newFeatureSet, 32) || newFeatureBit(device.newFeatureSet, 33)
    /** MidwayBackToDock (85): during a clean, back to dock offers emptying the bin or washing the mop and carrying on. */
    val hasMidwayReturn = newFeatureBit(device.newFeatureSet, 85)
    /** The last clean whose bubble on the map was closed, so it stays closed while the app runs. */
    var dismissedLastClean: Long? = null
    private val _requestedPanel = kotlinx.coroutines.flow.MutableStateFlow<DetailPanel?>(null)
    /** A sheet another screen asked the robot page to open (Settings → Maps); taken once by that page. */
    internal val requestedPanel: kotlinx.coroutines.flow.StateFlow<DetailPanel?> = _requestedPanel
    internal fun requestPanel(panel: DetailPanel?) { _requestedPanel.value = panel }

    val showsBetaFeatures = betaFeatureDefs.any { it.section == FeatureSection.BETA && it.bit != null } || unlistedFeatures.isNotEmpty()
    val repository: VacuumRepository = container.vacuumRepositoryFor(device).also { it.restoreCachedFeatures(betaFeatureDefs) }

    // The robot's own answer wins; the server's record is the fallback until it has replied.
    val supportsSmartPlan: StateFlow<Boolean> = repository.robotFeatureSet
        .map { isSmartPlanSupported(it ?: serverFeatureSet) }
        .stateIn(viewModelScope, SharingStarted.Eagerly, isSmartPlanSupported(serverFeatureSet))
    val supportsWaterSlide: StateFlow<Boolean> = repository.robotFeatureSet
        .map { isWaterSlideSupported(it ?: serverFeatureSet) }
        .stateIn(viewModelScope, SharingStarted.Eagerly, isWaterSlideSupported(serverFeatureSet))
    /** Bit 93 (CleanThenMopMode): the robot can vacuum first and mop after. */
    val supportsVacuumThenMop: StateFlow<Boolean> = repository.robotFeatureSet
        .map { newFeatureBit(it ?: serverFeatureSet, 93) }
        .stateIn(viewModelScope, SharingStarted.Eagerly, newFeatureBit(serverFeatureSet, 93))
    /** Intensive corner mopping (plugin: bit 31 of the hex flags' low word). */
    val supportsCornerClean: StateFlow<Boolean> = repository.robotFeatureSet
        .map { isCornerCleanSupported(it ?: serverFeatureSet) }
        .stateIn(viewModelScope, SharingStarted.Eagerly, isCornerCleanSupported(serverFeatureSet))
    /** A custom water flow set as a distance (no water slide, bit 31 of the decimal featureSet). */
    val supportsWaterDistance: StateFlow<Boolean> = repository.robotFeatureSet
        .map { isWaterDistanceSupported(device.featureSet, it ?: serverFeatureSet) }
        .stateIn(viewModelScope, SharingStarted.Eagerly, isWaterDistanceSupported(device.featureSet, serverFeatureSet))
    /** The mop routes this robot offers ([mopRouteOptions]); null while unknown. */
    val routeOptions: StateFlow<Set<Int>?> = repository.robotFeatureSet
        .map { mopRouteOptions(device.featureSet, it ?: serverFeatureSet) }
        .stateIn(viewModelScope, SharingStarted.Eagerly, mopRouteOptions(device.featureSet, serverFeatureSet))
    val status = repository.status

    /** Where this model's voice packs live on the server; null when unknown, which hides the page. */
    val voicePackBucket: String? = voicePackBucket(modelName)

    /** The packs the server offers for this robot. */
    suspend fun loadVoicePacks(): List<VoicePack> =
        container.voicePacks(requireNotNull(voicePackBucket) { "No voice packs are known for this model" })

    private val errorBannerKey = "robot-error-${device.duid}"

    private val _busy = MutableStateFlow(false)
    val busy: StateFlow<Boolean> = _busy.asStateFlow()
    private val actionMutex = Mutex()
    private var pendingActions = 0

    private val _firmwareUpdate = MutableStateFlow<FirmwareUpdateState>(FirmwareUpdateState.Idle)
    val firmwareUpdate: StateFlow<FirmwareUpdateState> = _firmwareUpdate.asStateFlow()

    private val _routines = MutableStateFlow<RoutinesState>(RoutinesState.Loading)
    val routines: StateFlow<RoutinesState> = _routines.asStateFlow()


    fun checkFirmwareUpdate() {
        viewModelScope.launch {
            _firmwareUpdate.value = FirmwareUpdateState.Checking
            _firmwareUpdate.value = try {
                val info = container.deviceRepository.checkFirmwareUpdate(device.duid)
                when {
                    info == null -> FirmwareUpdateState.Unavailable(getString(Res.string.robot_firmware_no_info))
                    !info.namesAVersion -> FirmwareUpdateState.Unavailable(
                        getString(Res.string.robot_firmware_not_checked),
                    )
                    info.updatable -> FirmwareUpdateState.Available(
                        current = info.currentVersion ?: firmwareVersion,
                        latest = info.version,
                        notes = info.desc,
                        force = info.forceUpdate,
                    )
                    else -> FirmwareUpdateState.UpToDate(info.currentVersion ?: firmwareVersion)
                }
            } catch (e: Throwable) {
                FirmwareUpdateState.Unavailable(describe(e))
            }
        }
    }

    init {
        viewModelScope.launch {
            pendingActions++
            _busy.value = true
            try {
                actionMutex.withLock {
                    try {
                        container.ensureMqttConnected()
                        repository.attach()
                    } catch (e: Throwable) {
                        coroutineContext.ensureActive()
                        container.messages.error(describe(e))
                    }
                }
            } finally {
                pendingActions--
                _busy.value = pendingActions > 0
            }
            // One-shot reads: the link may still be coming up, so wait for it rather than let
            // them fail and leave the consumables and room names empty.
            launch {
                container.awaitMqttConnected()
                runCatching { repository.fetchConsumableStatus() }
                runCatching { repository.rooms() }
                runCatching { repository.fetchRobotFeatureSet() }
            }
        }

        viewModelScope.launch {
            status.collect { s ->
                val code = s.errorCode ?: 0
                if (code != 0) {
                    container.messages.showBanner(
                        key = errorBannerKey,
                        text = "$deviceName: ${VacuumErrorCodes.describe(code)}",
                        severity = MessageSeverity.ERROR,
                    )
                } else {
                    container.messages.dismissBanner(errorBannerKey)
                }
            }
        }
    }

    override fun onCleared() {
        container.messages.dismissBanner(errorBannerKey)
        repository.detach()
    }

    fun refreshMap() {
        viewModelScope.launch {
            pendingActions++
            _busy.value = true
            try {
                actionMutex.withLock {
                    try {
                        repository.fetchMap()
                        runCatching { repository.rooms() }
                        coroutineContext.ensureActive()
                    } catch (e: Throwable) {
                        coroutineContext.ensureActive()
                        container.messages.error(describe(e))
                        runCatching { repository.forceReconnect() }
                        coroutineContext.ensureActive()
                    }
                }
            } finally {
                pendingActions--
                _busy.value = pendingActions > 0
            }
        }
    }


    /**
     * Give a room a custom name the way the Roborock app does: the name becomes a cloud room and the
     * segment points at it, keeping the room's type tag so the robot still treats it as that type.
     */
    suspend fun renameRoom(segment: Int, name: String) {
        val validName = validateRoomName(name)
        repository.checkRoomTable(segment)
        val room = container.deviceRepository.createRoom(validName)
        repository.assignRoom(segment, room.id)
        container.deviceRepository.refresh()
        repository.setHomeRooms(container.deviceRepository.home.value?.rooms ?: emptyList())
    }

    /**
     * Set a room's type the way the Roborock app does: get a cloud room for the type's label from
     * the server, point the robot's segment at it with `name_segment` along with the type's tag
     * (which the robot uses to adjust its cleaning), then reload the home so the name resolves.
     * Throws with a user-facing message on any failure.
     */
    suspend fun setRoomType(segment: Int, type: RoomType) {
        repository.checkRoomTable(segment)
        val room = container.deviceRepository.createRoom(type.label)
        repository.assignRoom(segment, room.id, type.tag)
        container.deviceRepository.refresh()
        repository.setHomeRooms(container.deviceRepository.home.value?.rooms ?: emptyList())
    }

    fun loadRoutines() {
        viewModelScope.launch {
            _routines.value = RoutinesState.Loading
            _routines.value = try {
                RoutinesState.Loaded(container.deviceRepository.scenes(device.duid))
            } catch (e: Throwable) {
                RoutinesState.Failed(describe(e))
            }
        }
    }

    /** The server runs the routine's steps itself; the robot's status shows how far it got. */
    fun runRoutine(scene: Scene) = run {
        val result = container.deviceRepository.runScene(scene.id)
        container.messages.success(routineRunMessage(scene.name, result))
    }

    /** The routine open in the editor: a saved one ([RoutineEdit.sceneId]) or a new one. */
    private val _routineEdit = MutableStateFlow<RoutineEdit?>(null)
    val routineEdit: StateFlow<RoutineEdit?> = _routineEdit.asStateFlow()

    /** Which step's zones the zone screen is picking, while it is open for a routine. */
    var routineZoneStep: Int? = null

    private val _routineSaving = MutableStateFlow(false)
    val routineSaving: StateFlow<Boolean> = _routineSaving.asStateFlow()

    /** [name] is the new routine's default name, "Routine <n>" in the app's language. */
    fun newRoutine(name: String) {
        _routineEdit.value = RoutineEdit(
            sceneId = null,
            savedName = null,
            draft = RoutineDraft(name, listOf(RoutineStep(RoutineStepKind.FULL, mapFlag = currentMapFlag()))),
        )
    }

    /**
     * Adds one of the official app's recommended routines, named [name] in the app's language. As in the
     * official app it is saved at once, except "After meals", which opens in the editor to check its
     * rooms; returns true then.
     */
    fun addRecommendedRoutine(recommended: RecommendedRoutine, name: String, addedMessage: String): Boolean {
        val draft = recommendedRoutineDraft(
            recommended, name, supportsWaterSlide.value, currentMapFlag(),
        )
        _routineEdit.value = RoutineEdit(sceneId = null, savedName = null, draft = draft)
        if (recommended == RecommendedRoutine.AFTER_MEAL) return true
        saveRoutine(savedMessage = addedMessage) {}
        return false
    }

    /** False when the routine holds something LocalRock cannot edit; it can still be run. */
    fun editRoutine(scene: Scene): Boolean {
        val draft = decodeRoutine(scene.name, scene.param) ?: return false
        _routineEdit.value = RoutineEdit(scene.id, scene.name, draft)
        return true
    }

    fun routineNotEditable() = viewModelScope.launch {
        container.messages.error(getString(Res.string.routines_not_editable))
    }

    fun updateRoutine(change: (RoutineDraft) -> RoutineDraft) {
        _routineEdit.value = _routineEdit.value?.let { it.copy(draft = change(it.draft)) }
    }

    fun updateRoutineStep(index: Int, change: (RoutineStep) -> RoutineStep) = updateRoutine { d ->
        d.copy(steps = d.steps.mapIndexed { i, step -> if (i == index) step.edited(change(step)) else step })
    }

    fun currentMapFlag(): Int = repository.currentFloorFlag.value

    fun saveRoutine(savedMessage: String? = null, onSaved: () -> Unit) {
        val edit = _routineEdit.value ?: return
        val name = edit.draft.name.trim()
        viewModelScope.launch {
            val problem = when {
                name.isEmpty() -> Res.string.routine_editor_need_name
                edit.draft.steps.isEmpty() -> Res.string.routine_editor_need_step
                edit.draft.steps.any { !it.isComplete() } -> Res.string.routine_editor_need_target
                else -> null
            }
            if (problem != null) { container.messages.error(getString(problem)); return@launch }
            _routineSaving.value = true
            try {
                // The robot hands out each new or changed room and zone step's id, as for the official
                // app. Without the robot the routine is still saved: the server runs steps without them.
                val steps = edit.draft.steps.map { step ->
                    if (step.tid != null) step
                    else try { repository.registerRoutineStep(step) } catch (e: CancellationException) { throw e } catch (e: Throwable) { step }
                }
                val param = encodeRoutineParam(
                    edit.draft.copy(name = name, steps = steps),
                    device.duid,
                    kotlin.time.Clock.System.now().toEpochMilliseconds(),
                    roomName = { id -> repository.rooms.value.find { it.id == id }?.name?.takeIf { n -> n.isNotBlank() } },
                )
                if (edit.sceneId == null) {
                    container.deviceRepository.createScene(device.duid, name, param)
                } else {
                    container.deviceRepository.updateScene(edit.sceneId, device.duid, name.takeIf { it != edit.savedName }, param)
                }
                container.messages.success(savedMessage ?: getString(Res.string.routines_saved))
                _routineEdit.value = null
                loadRoutines()
                onSaved()
            } catch (e: Throwable) {
                container.messages.error(describe(e))
            } finally {
                _routineSaving.value = false
            }
        }
    }

    fun deleteRoutine(scene: Scene) {
        viewModelScope.launch {
            try {
                container.deviceRepository.deleteScene(scene.id)
                container.messages.success(getString(Res.string.routines_deleted))
            } catch (e: Throwable) {
                container.messages.error(describe(e))
            }
            loadRoutines()
        }
    }

    /** The room order a full clean follows on the loaded map; empty when none has been set. */
    fun defaultCleanOrder(): List<Int> =
        container.appSettings.cleanOrder(device.duid, repository.currentFloorFlag.value)

    /** Kept only once the robot has taken the order, so the app never remembers one it refused. */
    fun saveDefaultCleanOrder(order: List<Int>) = run(Res.string.robot_order_saved) {
        it.saveCleanSequence(order)
        container.appSettings.setCleanOrder(device.duid, it.currentFloorFlag.value, order)
    }

    fun removeDefaultCleanOrder() {
        container.appSettings.setCleanOrder(device.duid, repository.currentFloorFlag.value, emptyList())
        viewModelScope.launch { container.messages.success(getString(Res.string.robot_order_removed)) }
    }

    /**
     * Runs a settings change and shows the error if the robot refuses it. Unlike [run] it neither
     * marks the screen busy nor reads the status back, which would be too heavy for a slider; the
     * settings are read again on a failure so the screen goes back to what the robot really holds.
     * The returned job ends after that read, so a slider can hold its position until then.
     */
    fun report(action: suspend (VacuumRepository) -> Unit): Job =
        viewModelScope.launch {
            try {
                action(repository)
            } catch (e: Throwable) {
                container.messages.error(describe(e))
                runCatching { repository.fetchRobotSettings() }
            }
        }

    /**
     * Runs [action] with the screen marked busy and reads the status back. A failure goes to
     * [onError] when one is given, for a bottom sheet that would hide the screen's message.
     */
    fun run(
        successMessage: String? = null,
        onError: ((String) -> Unit)? = null,
        action: suspend (VacuumRepository) -> Unit,
    ) = runAction({ successMessage }, onError, action)

    /** [run] with the success message as a string resource, read in the app's language. */
    fun run(
        successMessage: StringResource?,
        onError: ((String) -> Unit)? = null,
        action: suspend (VacuumRepository) -> Unit,
    ) = runAction({ successMessage?.let { getString(it) } }, onError, action)

    private fun runAction(
        successMessage: suspend () -> String?,
        onError: ((String) -> Unit)?,
        action: suspend (VacuumRepository) -> Unit,
    ) {
        viewModelScope.launch {
            pendingActions++
            _busy.value = true
            try {
                actionMutex.withLock {
                    try {
                        action(repository)

                        runCatching { repository.refreshStatus() }
                        coroutineContext.ensureActive()
                        successMessage()?.let { container.messages.success(it) }
                    } catch (e: Throwable) {
                        coroutineContext.ensureActive()
                        val text = describe(e)
                        if (onError != null) onError(text) else container.messages.error(text)
                    }
                }
            } finally {
                pendingActions--
                _busy.value = pendingActions > 0
            }
        }
    }
}

sealed interface FirmwareUpdateState {
    data object Idle : FirmwareUpdateState
    data object Checking : FirmwareUpdateState
    data class UpToDate(val current: String?) : FirmwareUpdateState
    data class Available(
        val current: String?,
        val latest: String?,
        val notes: String?,
        val force: Boolean,
    ) : FirmwareUpdateState
    data class Unavailable(val reason: String) : FirmwareUpdateState
}

data class RoutineEdit(val sceneId: Long?, val savedName: String?, val draft: RoutineDraft)

sealed interface RoutinesState {
    data object Loading : RoutinesState
    data class Loaded(val routines: List<Scene>) : RoutinesState
    data class Failed(val reason: String) : RoutinesState
}

/** The message for a run request's answer, as a string resource and its arguments. */
internal data class RoutineRunText(val accepted: Boolean, val resource: StringResource, val args: List<String>)

/** What to tell the user once the server has answered a run request. */
internal fun routineRunText(name: String, result: SceneRunResult): RoutineRunText {
    if (!result.accepted) {
        val active = result.activeSceneName
        val status = result.status
        return when {
            status == "routine_in_progress" && active != null ->
                RoutineRunText(false, Res.string.robot_routine_still_running, listOf(active))
            status != null -> RoutineRunText(false, Res.string.robot_routine_not_started_status, listOf(name, status))
            else -> RoutineRunText(false, Res.string.robot_routine_not_started, listOf(name))
        }
    }
    return if (result.status == "cancel_requested") RoutineRunText(true, Res.string.robot_routine_stopping, listOf(name))
    else RoutineRunText(true, Res.string.robot_routine_started, listOf(name))
}

/** [routineRunText] in the app's language; a refusal is thrown. */
internal suspend fun routineRunMessage(name: String, result: SceneRunResult): String {
    val text = routineRunText(name, result)
    val message = getString(text.resource, *text.args.toTypedArray())
    if (!text.accepted) throw IllegalStateException(message)
    return message
}

/**
 * Longest room name allowed. The name is stored by the server, not the robot, and its real limit
 * is unknown; 23 keeps names short enough for the room labels on the map.
 */
const val MAX_ROOM_NAME_LENGTH = 23

/**
 * Trimmed name, or [MapEditException] if unusable. A leading `{` or `[` makes the server treat
 * the form body as JSON and verify the request signature differently, so it is rejected here.
 */
fun validateRoomName(name: String): String {
    val clean = name.trim()
    if (clean.isEmpty()) throw MapEditException("Enter a room name")
    if (clean.length > MAX_ROOM_NAME_LENGTH) throw MapEditException("Room names can be at most $MAX_ROOM_NAME_LENGTH characters")
    if (clean.startsWith("{") || clean.startsWith("[")) throw MapEditException("Room names can't start with { or [")
    return clean
}

private suspend fun describe(e: Throwable): String {
    val parts = mutableListOf<String>()
    var current: Throwable? = e
    while (current != null) {
        val msg = current.message
        parts += when {
            current is TimeoutCancellationException -> getString(Res.string.robot_no_answer)
            msg.isNullOrBlank() -> current::class.simpleName ?: "Throwable"
            else -> msg
        }
        current = current.cause?.takeIf { it !== current }
    }
    return parts.distinct().joinToString(" → ")

}
