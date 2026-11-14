package com.kodraliu.localrock.shared.vacuum

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlin.test.Test
import kotlin.test.assertEquals

class MultiDndTest {
    @Test
    fun matchesWhatTheOfficialAppSent() {
        // Captured from the official app on an a296.
        assertEquals(
            """[{"actions":{"dry":0,"dust":0,"led":1,"resume":1,"vol":1},"dnd":[22,0,8,0],"enabled":1}]""",
            multiDndParams(22, 0, 8, 0, actions = null).toString(),
        )
    }

    @Test
    fun keepsTheRobotsOwnActions() {
        val actions = JsonObject(mapOf("dry" to JsonPrimitive(1), "led" to JsonPrimitive(0)))
        assertEquals(
            """[{"actions":{"dry":1,"led":0},"dnd":[23,30,7,15],"enabled":1}]""",
            multiDndParams(23, 30, 7, 15, actions).toString(),
        )
    }

    @Test
    fun readsEveryPeriodTheRobotReports() {
        // The a296's answer to get_multi_dnd_timer, with a second period added.
        val answer = Json.parseToJsonElement(
            """[{"dnd":[22,0,8,0],"enabled":1,"actions":{"resume":1,"vol":1,"led":1,"dry":0,"dust":0}},""" +
                """{"dnd":[12,30,14,0],"enabled":0}]""",
        )
        val periods = parseMultiDnd(answer)!!
        assertEquals(listOf(listOf(22, 0, 8, 0), listOf(12, 30, 14, 0)), periods.map { it.times })
        assertEquals(listOf(true, false), periods.map { it.enabled })
        assertEquals(JsonPrimitive(1), periods[0].actions!!["resume"])
    }

    @Test
    fun sendsEveryPeriodWithItsOwnSwitch() {
        val periods = listOf(DndPeriod(22, 0, 8, 0, true, null), DndPeriod(12, 0, 13, 0, false, null))
        assertEquals(
            """[{"actions":{"dry":0,"dust":0,"led":1,"resume":1,"vol":1},"dnd":[22,0,8,0],"enabled":1},""" +
                """{"actions":{"dry":0,"dust":0,"led":1,"resume":1,"vol":1},"dnd":[12,0,13,0],"enabled":0}]""",
            multiDndListParams(periods).toString(),
        )
    }

    @Test
    fun anEmptyListIsNoPeriods() {
        assertEquals(emptyList(), parseMultiDnd(Json.parseToJsonElement("[]")))
    }
}
