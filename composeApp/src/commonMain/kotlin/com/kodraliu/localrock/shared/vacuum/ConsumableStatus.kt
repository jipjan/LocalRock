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
