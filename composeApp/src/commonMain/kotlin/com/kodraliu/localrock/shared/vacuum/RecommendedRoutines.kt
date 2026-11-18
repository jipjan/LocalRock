package com.kodraliu.localrock.shared.vacuum

/**
 * The official app's recommended routines ("Aanbevolen routines"), as it saves them on an a296
 * (2026-11-18): one whole-home step each, at `repeat` 0, with the routine's `tagId`. "After meals" is
 * not saved straight away: like the official app it opens the editor, with its name and no rooms picked
 * yet (the official app picks none even where a room is typed kitchen or dining room).
 */
enum class RecommendedRoutine(val tagId: String?) {
    VACUUM_THEN_MOP("101"),
    AFTER_MEAL(null),
    DEEP("102"),
    INTENSIVE_VACUUM("103"),
    DEEP_PLUS("104"),
}

/**
 * The routine for [recommended], named [name] (in the app's language, as the official app does).
 * [waterSlide]: the robot takes the water slide's codes (a296); others get the three levels instead.
 */
fun recommendedRoutineDraft(
    recommended: RecommendedRoutine,
    name: String,
    waterSlide: Boolean,
    mapFlag: Int,
): RoutineDraft {
    fun water(slide: Int, plain: Int) = if (waterSlide) slide else plain
    fun full(fan: Int, water: Int, route: Int, seqType: Int?) =
        RoutineStep(RoutineStepKind.FULL, fanPower = fan, waterBoxMode = water, mopMode = route, repeat = 0, seqType = seqType)
    val step = when (recommended) {
        RecommendedRoutine.VACUUM_THEN_MOP ->
            full(VacuumFanPower.BALANCED, water(WaterBoxMode.SLIDE_MEDIUM, WaterBoxMode.MEDIUM), MopRoute.STANDARD, SEQ_TYPE_VACUUM_THEN_MOP)
        RecommendedRoutine.DEEP ->
            full(VacuumFanPower.TURBO, water(WaterBoxMode.SLIDE_HIGH, WaterBoxMode.HIGH), MopRoute.STANDARD, null)
        RecommendedRoutine.INTENSIVE_VACUUM ->
            full(VacuumFanPower.MAX, WaterBoxMode.OFF, MopRoute.STANDARD, null)
        RecommendedRoutine.DEEP_PLUS ->
            full(VacuumFanPower.OFF, water(WaterBoxMode.SLIDE_HIGH, WaterBoxMode.HIGH), MopRoute.DEEP_PLUS, null)
        RecommendedRoutine.AFTER_MEAL -> RoutineStep(
            RoutineStepKind.ROOMS,
            waterBoxMode = water(WaterBoxMode.SLIDE_MEDIUM, WaterBoxMode.MEDIUM),
            mapFlag = mapFlag,
        )
    }
    return RoutineDraft(name, listOf(step), tagId = recommended.tagId)
}
