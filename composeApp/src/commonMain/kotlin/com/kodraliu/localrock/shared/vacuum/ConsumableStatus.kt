package com.kodraliu.localrock.shared.vacuum

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

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
fun ConsumableStatus.summary(): String? {
    val parts = listOfNotNull(
        mainBrushWorkTime?.let { Triple("Main brush", it, ConsumableStatus.MAIN_BRUSH_LIFETIME_S) },
        sideBrushWorkTime?.let { Triple("Side brush", it, ConsumableStatus.SIDE_BRUSH_LIFETIME_S) },
        filterWorkTime?.let { Triple("Filter", it, ConsumableStatus.FILTER_LIFETIME_S) },
        sensorDirtyTime?.let { Triple("Sensors", it, ConsumableStatus.SENSOR_LIFETIME_S) },
    )
    if (parts.isEmpty()) return null
    val (name, used, life) = parts.maxBy { (_, used, life) -> used.toDouble() / life }
    val left = 100 - (used * 100 / life)
    return when {
        left <= 0 -> if (name == "Sensors") "Clean the sensors" else "Replace the ${name.lowercase()}"
        left < 10 -> "$name: $left% left"
        else -> "Normal"
    }
}

