package com.kodraliu.localrock.shared.vacuum

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlin.test.Test
import kotlin.test.assertEquals

class RecommendedRoutinesTest {
    private fun stepAndTag(recommended: RecommendedRoutine): Pair<JsonObject, String?> {
        val draft = recommendedRoutineDraft(recommended, "x", waterSlide = true, mapFlag = 0)
        val param = encodeRoutineParam(draft, "d", now = 0)
        val item = param["action"]!!.jsonObject["items"]!!.jsonArray.single().jsonObject
        val inner = Json.parseToJsonElement(item["param"]!!.jsonPrimitive.content).jsonObject
        return inner["params"]!!.jsonArray.single().jsonObject to (param["tagId"] as? JsonPrimitive)?.content
    }

    /** What the official app saved for each recommended routine on an a296 (2026-11-18). */
    private val official = mapOf(
        RecommendedRoutine.VACUUM_THEN_MOP to ("""{"fan_power":102,"water_box_mode":235,"mop_mode":300,"mop_template_id":300,"repeat":0,"auto_dustCollection":1,"source":101,"seq_type":1}""" to "101"),
        RecommendedRoutine.DEEP to ("""{"fan_power":103,"water_box_mode":245,"mop_mode":300,"mop_template_id":300,"repeat":0,"auto_dustCollection":1,"source":101}""" to "102"),
        RecommendedRoutine.INTENSIVE_VACUUM to ("""{"fan_power":104,"water_box_mode":200,"mop_mode":300,"mop_template_id":300,"repeat":0,"auto_dustCollection":1,"source":101}""" to "103"),
        RecommendedRoutine.DEEP_PLUS to ("""{"fan_power":105,"water_box_mode":245,"mop_mode":303,"mop_template_id":303,"repeat":0,"auto_dustCollection":1,"source":101}""" to "104"),
    )

    @Test
    fun each_recommended_routine_is_saved_as_the_official_app_saves_it() {
        official.forEach { (recommended, expected) ->
            val (step, tag) = stepAndTag(recommended)
            assertEquals(Json.parseToJsonElement(expected.first), step, recommended.name)
            assertEquals(expected.second, tag, recommended.name)
        }
    }

    @Test
    fun after_meals_opens_as_an_empty_room_step_like_the_official_app() {
        val draft = recommendedRoutineDraft(RecommendedRoutine.AFTER_MEAL, "Na de maaltijd", true, mapFlag = 1)
        assertEquals(RoutineStepKind.ROOMS, draft.steps.single().kind)
        assertEquals(emptyList(), draft.steps.single().rooms)
        assertEquals(1, draft.steps.single().mapFlag)
    }

    @Test
    fun robots_without_the_water_slide_get_the_three_levels() {
        val draft = recommendedRoutineDraft(RecommendedRoutine.DEEP, "x", waterSlide = false, mapFlag = 0)
        assertEquals(WaterBoxMode.HIGH, draft.steps.single().waterBoxMode)
    }

    @Test
    fun a_recommended_routine_reads_back_with_its_tag_and_settings() {
        val draft = recommendedRoutineDraft(RecommendedRoutine.VACUUM_THEN_MOP, "Stofzuigen, dan dweilen", true, 0)
        val back = decodeRoutine(draft.name, JsonPrimitive(encodeRoutineParam(draft, "d", 0).toString()))!!
        assertEquals(draft, back)
    }
}
