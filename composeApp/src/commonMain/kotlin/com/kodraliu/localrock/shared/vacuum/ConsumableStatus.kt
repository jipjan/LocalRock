package com.kodraliu.localrock.shared.vacuum

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import com.kodraliu.localrock.resources.*
import com.kodraliu.localrock.shared.text.UiText
import com.kodraliu.localrock.shared.text.uiText

@Serializable
data class ConsumableStatus(
    @SerialName("main_brush_work_time") val mainBrushWorkTime: Long? = null,
    @SerialName("side_brush_work_time") val sideBrushWorkTime: Long? = null,
    @SerialName("filter_work_time") val filterWorkTime: Long? = null,
    @SerialName("sensor_dirty_time") val sensorDirtyTime: Long? = null,
    // Wash-dock consumables; only reported by dock-equipped models.
    @SerialName("cleaning_brush_work_times") val cleaningBrushWorkTime: Long? = null,
    /**
     * A count, not seconds, as the name says: an a296 reported 5 after two cleans of about 35 min with
     * mop washes in between. The official app shows no lifetime for it, only "clean when needed".
     */
    @SerialName("strainer_work_times") val strainerWorkTime: Long? = null,
    /** How often the dock emptied the robot into its bag since the last reset (a296: 2 after two cleans). */
    @SerialName("dust_collection_work_times") val dustCollectionWorkTimes: Long? = null,
) {
    companion object {
        const val MAIN_BRUSH_LIFETIME_S = 300L * 3600
        const val SIDE_BRUSH_LIFETIME_S = 200L * 3600
        const val FILTER_LIFETIME_S = 150L * 3600
        const val SENSOR_LIFETIME_S = 30L * 3600
        const val CLEANING_BRUSH_LIFETIME_S = 300L * 3600
    }
}

/**
 * One line for the Maintenance entry, as the official app's "Normaal": the part that is worn out, the
 * one closest to it when it has under 10% left, or "Normal". Null before the robot has answered.
 */
fun ConsumableStatus.summary(): String? = summaryText()?.english

/** The parts [summary] weighs: name, what to do once worn out, used seconds and lifetime. */
private class ConsumablePart(val name: UiText, val wornOut: UiText, val used: Long, val life: Long)

/** [summary] for the UI, in the app's language. */
fun ConsumableStatus.summaryText(): UiText? {
    val parts = listOfNotNull(
        mainBrushWorkTime?.let {
            ConsumablePart(
                uiText(Res.string.consumable_main_brush, "Main brush"),
                uiText(Res.string.consumable_replace_main_brush, "Replace the main brush"),
                it, ConsumableStatus.MAIN_BRUSH_LIFETIME_S,
            )
        },
        sideBrushWorkTime?.let {
            ConsumablePart(
                uiText(Res.string.consumable_side_brush, "Side brush"),
                uiText(Res.string.consumable_replace_side_brush, "Replace the side brush"),
                it, ConsumableStatus.SIDE_BRUSH_LIFETIME_S,
            )
        },
        filterWorkTime?.let {
            ConsumablePart(
                uiText(Res.string.consumable_filter, "Filter"),
                uiText(Res.string.consumable_replace_filter, "Replace the filter"),
                it, ConsumableStatus.FILTER_LIFETIME_S,
            )
        },
        sensorDirtyTime?.let {
            ConsumablePart(
                uiText(Res.string.consumable_sensors, "Sensors"),
                uiText(Res.string.consumable_clean_sensors, "Clean the sensors"),
                it, ConsumableStatus.SENSOR_LIFETIME_S,
            )
        },
    )
    if (parts.isEmpty()) return null
    val part = parts.maxBy { it.used.toDouble() / it.life }
    val left = 100 - (part.used * 100 / part.life)
    return when {
        left <= 0 -> part.wornOut
        left < 10 -> uiText(Res.string.consumable_percent_left, "${part.name.english}: $left% left", part.name, left)
        else -> uiText(Res.string.consumable_normal, "Normal")
    }
}

