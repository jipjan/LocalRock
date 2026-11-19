package com.kodraliu.localrock.shared.vacuum

import com.kodraliu.localrock.resources.*
import com.kodraliu.localrock.shared.text.UiText
import com.kodraliu.localrock.shared.text.joinedText
import com.kodraliu.localrock.shared.text.uiPlural
import com.kodraliu.localrock.shared.text.uiText

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
    /** What was cleaned (`clean_type`): 1 full, 2 zones, 3 rooms, 4 quick mapping, 5 cruise, 6 pet search. */
    val cleanType: Int? = null,
) {
    /** [cleanType] as the official app's history names it, or null when unknown. */
    val typeLabel: String? get() = typeText?.english

    /** [typeLabel] for the UI, in the app's language. */
    val typeText: UiText?
        get() = when (cleanType) {
            1 -> uiText(Res.string.record_type_full, "Full")
            2 -> uiText(Res.string.record_type_zones, "Zones")
            3 -> uiText(Res.string.record_type_rooms, "Rooms")
            4 -> uiText(Res.string.record_type_quick_mapping, "Quick mapping")
            5 -> uiText(Res.string.record_type_cruise, "Cruise")
            6 -> uiText(Res.string.record_type_find_the_pet, "Find the pet")
            else -> null
        }

    /** A camera cruise or pet search: the official History lists these under "Navigating". */
    val isPatrol: Boolean get() = cleanType == 5 || cleanType == 6

    /** Square metres per minute of cleaning, the official app's "cleaning efficiency"; null without a duration. */
    val efficiencyM2PerMinute: Float?
        get() = if (durationSeconds >= 60 && areaMm2 > 0) (areaMm2 / 1_000_000f) / (durationSeconds / 60f) else null

    /** A short line of what the robot did, e.g. "47 obstacles avoided · 2 mop washes · bin emptied". */
    val detailLine: String? get() = detailText?.english

    /** [detailLine] for the UI, in the app's language. */
    val detailText: UiText?
        get() = joinedText(listOf(
            avoidCount?.takeIf { it > 0 }?.let {
                uiPlural(Res.plurals.record_obstacles_avoided, it, "$it obstacle${if (it == 1) "" else "s"} avoided", it)
            },
            washCount?.takeIf { it > 0 }?.let {
                uiPlural(Res.plurals.record_mop_washes, it, "$it mop wash${if (it == 1) "" else "es"}", it)
            },
            if (dustCollected == true) uiText(Res.string.record_bin_emptied, "bin emptied") else null,
            passes?.takeIf { it > 1 }?.let { uiText(Res.string.record_passes, "$it passes", it) },
        ))
}
