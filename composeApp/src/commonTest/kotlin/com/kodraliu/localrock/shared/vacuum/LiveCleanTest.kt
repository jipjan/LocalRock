package com.kodraliu.localrock.shared.vacuum

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class LiveCleanTest {
    private fun ev(t: Long, i: Int, e: String) = CleanEvent(t, i, Json.parseToJsonElement(e) as JsonObject)

    // From the a296's get_rt_clean_detail on 2026-11-16: Slaapkamer (1), Hall (3), Bedroom (4), then
    // back to Slaapkamer to finish it.
    private val events = listOf(
        ev(1794828671, 41, """{"2":1}"""),
        ev(1794828672, 42, """{"2":1,"421":0,"3":2,"4":35,"5":0,"6":1,"422":1}"""),
        ev(1794829062, 43, """{"2":1}"""),
        ev(1794829063, 41, """{"2":3}"""),
        ev(1794829395, 43, """{"2":3}"""),
        ev(1794829441, 41, """{"2":4}"""),
        ev(1794830060, 43, """{"2":4}"""),
        ev(1794830060, 41, """{"2":1}"""),
    )

    @Test
    fun currentRoomIsTheLastStartedAndNotFinished() {
        assertEquals(1, currentCleanRoom(events))
        assertEquals(4, currentCleanRoom(events.take(6)))
        assertNull(currentCleanRoom(events.take(7)))
        assertNull(currentCleanRoom(emptyList()))
    }

    @Test
    fun aRealTimePageParsesLikeAPastClean() {
        val page = parseCleanEventPage(Json.parseToJsonElement(
            """{"begin":1794828508,"from":47,"num":2,"more":0,"events":[{"t":1794830095,"i":42,"e":"{\"2\":1,\"3\":2}"},{"t":1794830096,"i":22,"e":"{\"4\":35,\"222\":0}"}]}"""
        ))!!
        assertEquals(2, page.rawCount)
        assertEquals(false, page.more)
        assertEquals(listOf(42, 22), page.events.map { it.type })
    }
}
