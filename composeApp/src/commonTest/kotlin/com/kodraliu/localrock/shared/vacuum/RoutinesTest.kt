package com.kodraliu.localrock.shared.vacuum

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class RoutinesTest {
    private val draft = RoutineDraft(
        "Evening",
        listOf(
            RoutineStep(RoutineStepKind.ROOMS, rooms = listOf(16, 17), fanPower = VacuumFanPower.TURBO, waterBoxMode = 235, mopMode = MopRoute.DEEP, repeat = 2, mapFlag = 1),
            RoutineStep(RoutineStepKind.ZONES, zones = listOf(ZoneRect(30000, 31000, 25000, 27000)), fanPower = VacuumFanPower.OFF, waterBoxMode = 245),
            RoutineStep(RoutineStepKind.FULL, fanPower = VacuumFanPower.QUIET, tid = "1712345601000"),
        ),
    )

    private fun items(param: JsonObject) = param["action"]!!.jsonObject["items"]!!.jsonArray.map { it.jsonObject }
    private fun inner(item: JsonObject) = Json.parseToJsonElement(item["param"]!!.jsonPrimitive.content).jsonObject

    @Test
    fun a_routine_is_saved_in_the_official_apps_shape() {
        val param = encodeRoutineParam(draft, "duid1", now = 1_000)
        val items = items(param)
        assertEquals(listOf("do_scenes_segments", "do_scenes_zones", "do_scenes_app_start"), items.map { inner(it)["method"]!!.jsonPrimitive.content })
        items.forEachIndexed { i, item ->
            assertEquals("CMD", item["type"]!!.jsonPrimitive.content)
            assertEquals(i + 1, item["id"]!!.jsonPrimitive.content.toInt())
            assertEquals("duid1", item["entityId"]!!.jsonPrimitive.content)
            assertEquals(JsonArray(listOf(JsonPrimitive(130))), item["finishDpIds"])
            // The step's own param is a JSON string, as the official app stores it.
            assertTrue(item["param"] is JsonPrimitive)
        }
        val rooms = inner(items[0])["params"]!!.jsonObject["data"]!!.jsonArray[0].jsonObject
        assertEquals("[{\"sid\":16},{\"sid\":17}]", rooms["segs"].toString())
        assertEquals("103", rooms["fan_power"].toString())
        assertEquals("1", rooms["map_flag"].toString())
        // A zone's corners are stored low to high, as app_zoned_clean takes them.
        val zone = inner(items[1])["params"]!!.jsonObject["data"]!!.jsonArray[0].jsonObject["zones"]!!.jsonArray[0].jsonObject
        assertEquals("[25000,27000,30000,31000]", zone["range"].toString())
        // A whole-home step keeps its settings in a list, as the server reads app_start's.
        val full = inner(items[2])["params"]!!.jsonArray[0].jsonObject
        assertEquals("1712345601000", full["tid"]!!.jsonPrimitive.content)
        assertEquals("1000", rooms["tid"]!!.jsonPrimitive.content)
    }

    @Test
    fun a_saved_routine_reads_back_as_it_was_made() {
        val param = encodeRoutineParam(draft, "duid1", now = 1_000)
        val back = decodeRoutine("Evening", JsonPrimitive(param.toString()))!!
        val expected = draft.steps.mapIndexed { i, s ->
            s.copy(
                tid = s.tid ?: (1_000L + i).toString(),
                zones = s.zones.map { ZoneRect(minOf(it.x1, it.x2), minOf(it.y1, it.y2), maxOf(it.x1, it.x2), maxOf(it.y1, it.y2), it.times) },
            )
        }
        assertEquals(expected, back.steps)
    }

    @Test
    fun a_routine_with_a_step_localrock_does_not_know_is_not_edited() {
        val param = """{"action":{"type":"S","items":[{"id":1,"type":"CMD","param":"{\"method\":\"app_charge\"}"}]}}"""
        assertNull(decodeRoutine("Odd", JsonPrimitive(param)))
    }

    @Test
    fun the_official_apps_sample_routine_reads() {
        val param = """{"triggers":[],"action":{"type":"S","items":[{"id":1,"type":"CMD","name":"Living Room","entityId":"d","param":"{\"id\":1,\"method\":\"do_scenes_segments\",\"params\":{\"data\":[{\"tid\":\"1712345601000\",\"segs\":[{\"sid\":16}],\"map_flag\":0,\"fan_power\":101,\"water_box_mode\":200,\"mop_mode\":300,\"mop_template_id\":300,\"repeat\":2}],\"source\":101}}","finishDpIds":[130]}]},"matchType":"NONE"}"""
        val step = decodeRoutine("After Dinner", JsonPrimitive(param))!!.steps.single()
        assertEquals(RoutineStep(RoutineStepKind.ROOMS, rooms = listOf(16), fanPower = 101, waterBoxMode = 200, mopMode = 300, repeat = 2, tid = "1712345601000"), step)
    }
}
