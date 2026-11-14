package com.kodraliu.localrock.shared.vacuum

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.intOrNull
import kotlin.math.roundToInt

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
fun volumeLabel(volume: Int): String {
    val clamped = volume.coerceIn(VolumeRange)
    return if (clamped == 0) "Off" else "$clamped%"
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

fun washFrequencyLabel(settings: SmartWashSettings?): String = when (settings?.mode) {
    null -> "Unavailable"
    0 -> if (settings.intervalSeconds % 60 == 0) "${settings.intervalSeconds / 60} min" else "${settings.intervalSeconds} sec"
    1 -> "After each room"
    2 -> "Smart"
    else -> "Unknown (${settings.mode})"
}

data class CleanSummary(
    val totalTimeSec: Long,
    val totalAreaMm2: Long,
    val totalCount: Int,
)

data class DndSettings(
    val enabled: Boolean = false,
    val startHour: Int = 22,
    val startMinute: Int = 0,
    val endHour: Int = 8,
    val endMinute: Int = 0,
)

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
fun carpetModeOptions(newFeatureSet: String?): List<Pair<Int, String>> =
    if (newFeatureBit(newFeatureSet, 49)) {
        listOf(0 to "Avoid carpet", 3 to "Vacuum only", 2 to "Vacuum and mop")
    } else {
        listOf(0 to "Avoid carpet", 1 to "Standard", 2 to "Carpet boost")
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
