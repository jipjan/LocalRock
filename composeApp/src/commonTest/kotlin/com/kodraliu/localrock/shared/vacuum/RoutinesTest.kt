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
            RoutineStep(RoutineStepKind.FULL, fanPower = VacuumFanPower.QUIET),
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
        assertEquals(null, full["tid"])
        assertEquals("1000", rooms["tid"]!!.jsonPrimitive.content)
    }

    @Test
    fun a_saved_routine_reads_back_as_it_was_made() {
        val param = encodeRoutineParam(draft, "duid1", now = 1_000)
        val back = decodeRoutine("Evening", JsonPrimitive(param.toString()))!!
        val expected = draft.steps.mapIndexed { i, s ->
            s.copy(
                tid = if (s.kind == RoutineStepKind.FULL) null else s.tid ?: (1_000L + i).toString(),
                mapFlag = if (s.kind == RoutineStepKind.FULL) 0 else s.mapFlag,
                zoneIds = s.zones.indices.toList(),
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
        assertEquals(RoutineStep(RoutineStepKind.ROOMS, rooms = listOf(16), fanPower = 101, waterBoxMode = 200, mopMode = 300, repeat = 2, seqType = null, tid = "1712345601000"), step)
    }

    @Test
    fun matches_what_the_official_app_saved_for_the_same_routine() {
        // Made in the official app on an a296 (2026-11-18): Hall and Bedroom, then a full clean, both at
        // the default settings; the robot handed out the room step's tid and map. Stored by the server as:
        val official = Json.parseToJsonElement("""{"action":{"items":[{"entityId":"d","finishDpIds":[130],"id":1,"name":"Hall Bedroom","param":{"id":1,"method":"do_scenes_segments","params":{"data":[{"tid":"1795028618174","segs":[{"sid":3},{"sid":4}],"map_flag":1,"fan_power":102,"water_box_mode":235,"mop_mode":300,"mop_template_id":300,"repeat":1,"clean_order_mode":1,"auto_dry":1,"auto_dustCollection":1,"region_num":0,"seq_type":0}],"source":101}},"type":"CMD"},{"entityId":"d","finishDpIds":[130],"id":2,"name":"","param":{"id":2,"method":"do_scenes_app_start","params":[{"fan_power":102,"water_box_mode":235,"mop_mode":300,"mop_template_id":300,"repeat":1,"auto_dustCollection":1,"source":101,"seq_type":0}]},"type":"CMD"}],"type":"S"},"matchType":"NONE","triggers":[]}""").jsonObject
        val reg = parseSceneRegistration(Json.parseToJsonElement("""[{"tid":"1795028618174","map_flag":1,"segs":[{"sid":3},{"sid":4}]}]"""))!!
        val mine = encodeRoutineParam(
            RoutineDraft("Claude test", listOf(
                RoutineStep(RoutineStepKind.ROOMS, rooms = listOf(3, 4), waterBoxMode = 235).registered(reg),
                RoutineStep(RoutineStepKind.FULL, waterBoxMode = 235),
            )),
            "d", now = 0, roomName = { mapOf(3 to "Hall", 4 to "Bedroom")[it] },
        )
        // Step params are JSON strings in LocalRock's copy; compare them parsed.
        fun normal(o: JsonObject) = JsonObject(o + ("action" to JsonObject(o["action"]!!.jsonObject + ("items" to JsonArray(
            o["action"]!!.jsonObject["items"]!!.jsonArray.map { item ->
                val p = item.jsonObject["param"]!!
                JsonObject(item.jsonObject + ("param" to if (p is JsonPrimitive) Json.parseToJsonElement(p.content) else p))
            },
        )))))
        assertEquals(normal(official), normal(mine))
    }

    @Test
    fun the_robots_ids_are_kept_until_the_steps_rooms_or_zones_change() {
        val reg = parseSceneRegistration(Json.parseToJsonElement("""[{"tid":"77","zones":[{"zid":8},{"zid":9}]}]"""))!!
        assertEquals(SceneRegistration("77", null, listOf(8, 9)), reg)
        val step = RoutineStep(RoutineStepKind.ZONES, zones = listOf(ZoneRect(0, 0, 1, 1), ZoneRect(2, 2, 3, 3))).registered(reg)
        assertEquals("77", step.edited(step.copy(fanPower = VacuumFanPower.MAX)).tid)
        val moved = step.edited(step.copy(zones = listOf(ZoneRect(0, 0, 5, 5))))
        assertEquals(null, moved.tid)
        assertEquals(emptyList(), moved.zoneIds)
        val zone = inner(items(encodeRoutineParam(RoutineDraft("z", listOf(step)), "d", 0))[0])["params"]!!
            .jsonObject["data"]!!.jsonArray[0].jsonObject["zones"]!!.jsonArray.map { it.jsonObject["zid"].toString() }
        assertEquals(listOf("8", "9"), zone)
    }
}
