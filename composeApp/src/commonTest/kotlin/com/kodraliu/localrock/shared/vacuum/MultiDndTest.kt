package com.kodraliu.localrock.shared.vacuum

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
}
