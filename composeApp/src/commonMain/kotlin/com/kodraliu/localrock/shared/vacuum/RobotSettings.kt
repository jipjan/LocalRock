package com.kodraliu.localrock.shared.vacuum

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.intOrNull
import kotlin.math.roundToInt
import com.kodraliu.localrock.resources.*
import com.kodraliu.localrock.shared.text.UiText
import com.kodraliu.localrock.shared.text.rawText
import com.kodraliu.localrock.shared.text.uiText

data class RobotSettings(
    val volume: Int = 50,
    val childLock: Boolean = false,
    val ledEnabled: Boolean = true,
    val carpetMode: Int = 1, // 0=avoid, 1=standard, 2=boost
)

/** The range the volume slider covers, as change_sound_volume values, like the official app's slider. */
val VolumeRange = 0..100

/** The volume for a slider position: a whole value the robot accepts, kept within [VolumeRange]. */
fun volumeFromSlider(position: Float): Int = position.roundToInt().coerceIn(VolumeRange)

/** "Off" only at 0, as a robot at 10 still speaks; any other volume as its percentage, e.g. "45%". */
fun volumeLabel(volume: Int): String = volumeText(volume).english

/** [volumeLabel] for the UI, in the app's language. */
fun volumeText(volume: Int): UiText {
    val clamped = volume.coerceIn(VolumeRange)
    return if (clamped == 0) uiText(Res.string.mode_off, "Off") else rawText("$clamped%")
}

data class DockSettings(
    val washMode: Int? = null,
    val smartWash: SmartWashSettings? = null,
    val autoEmptyMode: Int? = null,
    val mopDrying: Boolean? = null,
    val dryTimeSeconds: Int? = null,
    val dryerAnswer: JsonObject? = null, // the whole answer, so a write can send back every field it held
    /** How drying starts: [DRY_START_OFF], [DRY_START_AFTER_CLEANING] or [DRY_START_SCHEDULED] (captured on an a296). */
    val dryStart: Int? = null,
    /** The time a scheduled drying starts, from the answer's `cron` ("M H * * *"). */
    val dryScheduleHour: Int? = null,
    val dryScheduleMinute: Int? = null,
    val washWaterTemperature: Int? = null,
)

data class SmartWashSettings(val mode: Int, val intervalSeconds: Int) {
    fun withFrequency(mode: Int, intervalMinutes: Int?): SmartWashSettings {
        require(mode in 0..2) { "Unsupported mop washing frequency" }
        require((mode == 0) == (intervalMinutes != null)) { "Choose an interval for time-based washing" }
        if (intervalMinutes != null) {
            require(intervalMinutes in 10..50 && intervalMinutes % 5 == 0) { "Invalid mop washing interval" }
        }
        return copy(mode = mode, intervalSeconds = intervalMinutes?.times(60) ?: intervalSeconds)
    }
}

internal fun parseSmartWashSettings(result: JsonElement?): SmartWashSettings? {
    val params = result as? JsonObject ?: return null
    val mode = (params["smart_wash"] as? JsonPrimitive)?.intOrNull ?: return null
    val interval = (params["wash_interval"] as? JsonPrimitive)?.intOrNull ?: return null
    return interval.takeIf { it > 0 }?.let { SmartWashSettings(mode, it) }
}

internal fun encodeSmartWashParams(settings: SmartWashSettings): JsonObject = JsonObject(mapOf(
    "smart_wash" to JsonPrimitive(settings.mode),
    "wash_interval" to JsonPrimitive(settings.intervalSeconds),
))

fun washFrequencyLabel(settings: SmartWashSettings?): String = washFrequencyText(settings).english

/** [washFrequencyLabel] for the UI, in the app's language. */
fun washFrequencyText(settings: SmartWashSettings?): UiText = when (settings?.mode) {
    null -> uiText(Res.string.mode_unavailable, "Unavailable")
    0 -> if (settings.intervalSeconds % 60 == 0) {
        (settings.intervalSeconds / 60).let { uiText(Res.string.duration_minutes, "$it min", it) }
    } else {
        uiText(Res.string.duration_sec, "${settings.intervalSeconds} sec", settings.intervalSeconds)
    }
    1 -> uiText(Res.string.dock_wash_after_each_room, "After each room")
    2 -> uiText(Res.string.mode_smart, "Smart")
    else -> uiText(Res.string.mode_unknown, "Unknown (${settings.mode})", settings.mode)
}

data class CleanSummary(
    val totalTimeSec: Long,
    val totalAreaMm2: Long,
    val totalCount: Int,
    /** How often the dock has emptied the bin (`dust_collection_count`, bit 100); null when not reported. */
    val dustCollectionCount: Int? = null,
)

data class DndSettings(
    val enabled: Boolean = false,
    val startHour: Int = 22,
    val startMinute: Int = 0,
    val endHour: Int = 8,
    val endMinute: Int = 0,
    /** What the robot holds back during quiet hours, e.g. `{"dry":0,"dust":0,"led":1,"resume":1,"vol":1}`; null when it reports none. */
    val actions: JsonObject? = null,
)

/** New feature bit 140: several quiet-hour periods, set with `set_multi_dnd_timer`. */
const val MULTI_DND_BIT = 140

/** The actions the official app sent with `set_multi_dnd_timer` on an a296, for a robot that reported none. */
internal val DEFAULT_DND_ACTIONS = JsonObject(mapOf(
    "dry" to JsonPrimitive(0), "dust" to JsonPrimitive(0), "led" to JsonPrimitive(1),
    "resume" to JsonPrimitive(1), "vol" to JsonPrimitive(1),
))

/** The official app allows at most this many quiet-hour periods (a296 plugin). */
const val MAX_DND_PERIODS = 3

/** One quiet-hour period of a robot with several (`get_multi_dnd_timer`). */
data class DndPeriod(
    val startHour: Int,
    val startMinute: Int,
    val endHour: Int,
    val endMinute: Int,
    val enabled: Boolean = true,
    val actions: JsonObject? = null,
) {
    val times: List<Int> get() = listOf(startHour, startMinute, endHour, endMinute)
}

/** The periods in a `get_multi_dnd_timer` answer, e.g. `[{"dnd":[22,0,8,0],"enabled":1,"actions":{...}}]`. */
internal fun parseMultiDnd(result: JsonElement?): List<DndPeriod>? {
    val list = result as? JsonArray ?: return null
    return list.mapNotNull { item ->
        val obj = item as? JsonObject ?: return@mapNotNull null
        val dnd = (obj["dnd"] as? JsonArray)?.map { (it as? JsonPrimitive)?.intOrNull ?: return@mapNotNull null }
            ?: return@mapNotNull null
        if (dnd.size != 4) return@mapNotNull null
        DndPeriod(dnd[0], dnd[1], dnd[2], dnd[3], (obj["enabled"] as? JsonPrimitive)?.intOrNull == 1, obj["actions"] as? JsonObject)
    }
}

/**
 * Parameters for `set_multi_dnd_timer`: every period, as the official app sent one (captured):
 * `[{"actions":{...},"dnd":[22,0,8,0],"enabled":1}]`. Each period keeps the robot's own actions.
 */
internal fun multiDndListParams(periods: List<DndPeriod>): JsonArray = JsonArray(periods.map { p ->
    JsonObject(mapOf(
        "actions" to (p.actions ?: DEFAULT_DND_ACTIONS),
        "dnd" to JsonArray(p.times.map { JsonPrimitive(it) }),
        "enabled" to JsonPrimitive(if (p.enabled) 1 else 0),
    ))
})

internal fun multiDndParams(startHour: Int, startMinute: Int, endHour: Int, endMinute: Int, actions: JsonObject?): JsonArray =
    multiDndListParams(listOf(DndPeriod(startHour, startMinute, endHour, endMinute, true, actions)))

/**
 * The body for `app_set_dryer_setting`: the answer the robot gave with only `status`, and `on.dry_time`
 * when given, changed. The answer carries more than those two (cliff timings, a count, the heating
 * film time, the whole `off` block), and it is not known whether the robot merges a shorter body or
 * replaces what it holds, so everything it reported is sent back.
 */
internal fun dryerSettingBody(reported: JsonObject?, enabled: Boolean, dryTimeSeconds: Int?): JsonObject {
    val body = (reported ?: JsonObject(emptyMap())).toMutableMap()
    body["status"] = JsonPrimitive(if (enabled) 1 else 0)
    if (dryTimeSeconds != null) {
        val on = ((reported?.get("on") as? JsonObject) ?: JsonObject(emptyMap())).toMutableMap()
        on["dry_time"] = JsonPrimitive(dryTimeSeconds)
        body["on"] = JsonObject(on)
    }
    return JsonObject(body)
}

/**
 * The carpet modes to offer, as set_carpet_clean_mode values. Robots with bit 49 (carpet custom clean,
 * such as the a296) get the official app's choices, captured: 0 avoid, 3 vacuum only (the mops stay
 * off the carpet), 2 vacuum and mop; crossing carpets is its own switch there. Other robots keep the
 * list LocalRock had.
 */
fun carpetModeOptions(newFeatureSet: String?): List<Pair<Int, UiText>> {
    val avoid = uiText(Res.string.carpet_avoid, "Avoid carpet")
    return if (newFeatureBit(newFeatureSet, 49)) {
        listOf(0 to avoid, 3 to uiText(Res.string.run_vacuum_only, "Vacuum only"), 2 to uiText(Res.string.run_vacuum_and_mop, "Vacuum and mop"))
    } else {
        listOf(0 to avoid, 1 to uiText(Res.string.run_suction_standard, "Standard"), 2 to uiText(Res.string.carpet_boost, "Carpet boost"))
    }
}

const val DRY_START_OFF = 0
const val DRY_START_AFTER_CLEANING = 1
const val DRY_START_SCHEDULED = 2

/** The hour and minute of a daily `"M H * * *"` cron, or null for anything else. */
internal fun parseDailyCron(cron: String?): Pair<Int, Int>? {
    val parts = cron?.trim()?.split(Regex("\\s+")) ?: return null
    if (parts.size != 5 || parts.drop(2).any { it != "*" }) return null
    val minute = parts[0].toIntOrNull()?.takeIf { it in 0..59 } ?: return null
    val hour = parts[1].toIntOrNull()?.takeIf { it in 0..23 } ?: return null
    return hour to minute
}

/**
 * The body for `app_set_dryer_setting` that changes how drying starts. As [dryerSettingBody], every
 * field the robot reported is sent back; `status` and `cron` change. Captured from the official app:
 * `{"cron":"0 17 * * *","on":{"dry_time":10800},"status":2}` for a scheduled start, `"cron":""` otherwise.
 */
internal fun dryerStartBody(reported: JsonObject?, start: Int, hour: Int?, minute: Int?, dryTimeSeconds: Int?): JsonObject {
    require(start in DRY_START_OFF..DRY_START_SCHEDULED) { "Unknown drying start $start" }
    val body = (reported ?: JsonObject(emptyMap())).toMutableMap()
    body["status"] = JsonPrimitive(start)
    body["cron"] = JsonPrimitive(
        if (start == DRY_START_SCHEDULED) {
            requireNotNull(hour); requireNotNull(minute)
            "$minute $hour * * *"
        } else "",
    )
    if (body["on"] == null && dryTimeSeconds != null) {
        body["on"] = JsonObject(mapOf("dry_time" to JsonPrimitive(dryTimeSeconds)))
    }
    return JsonObject(body)
}

/** The off-peak electricity window the robot is limited to, in the robot's local time. */
data class ValleySettings(
    val enabled: Boolean = false,
    val startHour: Int = 22,
    val startMinute: Int = 0,
    val endHour: Int = 6,
    val endMinute: Int = 0,
)

/**
 * A saved map. [savedAt] is when the robot last stored it and [backupAt] when its on-robot backup
 * was made, both in the robot's own epoch seconds; [backupAt] is null when the map has no backup.
 */
data class FloorMap(val name: String, val mapFlag: Int, val savedAt: Long? = null, val backupAt: Long? = null)
