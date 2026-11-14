package com.kodraliu.localrock.shared.vacuum

import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class CleanTimelineTest {
    // Part of an a296's get_clean_detail answer (2026-11-14).
    private val page = Json.parseToJsonElement(
        """{"begin":1794658027,"from":0,"num":4,"more":1,"events":[
          {"t":1794658027,"i":25,"e":"{\"0\":254}"},
          {"t":1794658027,"i":11,"e":"{\"111\":8,\"112\":25,\"1\":73,\"113\":1}"},
          {"t":1794658188,"i":11,"e":"{\"111\":25,\"112\":5,\"1\":73,\"113\":5}"},
          {"t":1794658190,"i":11,"e":"{\"111\":5,\"112\":5,\"1\":72}"}]}""",
    )

    @Test
    fun readsEventsAndWhetherMoreFollow() {
        val p = parseCleanEventPage(page)!!
        assertEquals(4, p.events.size)
        assertEquals(true, p.more)
        assertEquals(254, (p.events[0].fields["0"] as kotlinx.serialization.json.JsonPrimitive).content.toInt())
    }

    @Test
    fun stateChangesSkipRepeats() {
        val changes = cleanStateChanges(parseCleanEventPage(page)!!.events)
        assertEquals(listOf(8 to 25, 25 to 5), changes.map { it.from to it.to })
        assertEquals(73, changes[0].battery)
    }
}
