package com.kodraliu.localrock.shared.vacuum

data class CleanRecord(
    val beginEpoch: Long,
    val endEpoch: Long,
    val durationSeconds: Long,
    val areaMm2: Long,
    val errorCode: Int,
    val complete: Int,
    // Newer robots (a296) report more in their object answer; null when a robot leaves them out.
    /** Obstacles the robot steered around (`avoid_count`). */
    val avoidCount: Int? = null,
    /** Times the dock washed the mops during the clean (`wash_count`). */
    val washCount: Int? = null,
    /** Whether the dock emptied the bin after the clean (`dust_collection_status`). */
    val dustCollected: Boolean? = null,
    /** Passes over the area (`clean_times`). */
    val passes: Int? = null,
    /** What was cleaned (`clean_type`): 1 full, 2 zones, 3 rooms, 4 quick mapping, 5 patrol. */
    val cleanType: Int? = null,
) {
    /** [cleanType] as the official app's history names it, or null when unknown. */
    val typeLabel: String?
        get() = when (cleanType) {
            1 -> "Full"
            2 -> "Zones"
            3 -> "Rooms"
            4 -> "Quick mapping"
            5 -> "Patrol"
            else -> null
        }

    /** Square metres per minute of cleaning, the official app's "cleaning efficiency"; null without a duration. */
    val efficiencyM2PerMinute: Float?
        get() = if (durationSeconds >= 60 && areaMm2 > 0) (areaMm2 / 1_000_000f) / (durationSeconds / 60f) else null

    /** A short line of what the robot did, e.g. "47 obstacles avoided · 2 mop washes · bin emptied". */
    val detailLine: String?
        get() = listOfNotNull(
            avoidCount?.takeIf { it > 0 }?.let { "$it obstacle${if (it == 1) "" else "s"} avoided" },
            washCount?.takeIf { it > 0 }?.let { "$it mop wash${if (it == 1) "" else "es"}" },
            if (dustCollected == true) "bin emptied" else null,
            passes?.takeIf { it > 1 }?.let { "$it passes" },
        ).joinToString(" · ").ifEmpty { null }
}
