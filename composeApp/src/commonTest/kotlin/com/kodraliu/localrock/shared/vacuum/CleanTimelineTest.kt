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

class CleanTimelineWordsTest {
    // Events of the a296 clean at 13:07, and what the official app showed for them.
    private fun ev(t: Long, i: Int, e: String) = CleanEvent(t, i, Json.parseToJsonElement(e) as kotlinx.serialization.json.JsonObject)
    private val events = listOf(
        ev(1794658027, 25, """{"0":254}"""),
        ev(1794658027, 25, """{"251":2,"252":1,"3":1,"4":1,"5":0,"6":1}"""),
        ev(1794658027, 52, """{"521":3,"522":0,"523":1}"""),
        ev(1794658187, 53, """{"531":0}"""),
        ev(1794658199, 42, """{"2":1,"421":0,"3":1,"4":1,"5":0,"6":1,"422":6,"10":0,"12":0}"""),
        ev(1794659791, 21, """{"211":1,"212":65553,"213":65569}"""),
        ev(1794659888, 42, """{"2":36,"421":3,"3":1,"4":2,"5":0,"6":1,"10":0,"12":0}"""),
        ev(1794660264, 52, """{"521":10,"522":1,"523":1}"""),
        ev(1794660747, 12, """{"121":123}"""),
        ev(1794660795, 23, """{"231":1,"232":0,"233":1,"234":2260}"""),
        ev(1794660799, 54, """{"541":2,"542":-1}"""),
    )

    @Test
    fun readsLikeTheOfficialApp() {
        val lines = describeCleanEvents(events) { id -> mapOf(1 to "Kitchen")[id] }
        assertEquals(
            listOf(
                "Started full clean", "Mop wash", "Resumed", "Cleaning Kitchen", "Settings changed by hand",
                "Re-cleaning areas blocked by people or pets", "Mop wash", "Clean finished", "Auto-empty",
            ),
            lines.map { it.title },
        )
        assertEquals("from the app · Vacuum and mop", lines[0].detail)
        assertEquals("Vacuum and mop · ×1", lines[3].detail)
        assertEquals("Smart · Warm water", lines[6].detail)
        assertEquals("Balanced", lines[8].detail)
    }
}

class CleanRunSettingsTest {
    @Test
    fun kitchenAndLivingRoomAsTheOfficialAppShowedThem() {
        // Kitchen: moon | drop 5 | standard route | ×1; living room: moon | drop 15 | standard | ×1.
        val kitchen = CleanRunSettings(suction = 1, water = 1, route = 0, passes = 1)
        assertEquals(listOf("Quiet", "Low", "Standard"), listOf(kitchen.suctionLabel, kitchen.waterLabel, kitchen.routeLabel))
        assertEquals("Medium", CleanRunSettings(1, 2, 0, 1).waterLabel)
        assertEquals("Vacuum only", CleanRunSettings(2, 0, 0, 1).mode)
        assertEquals("Mop only", CleanRunSettings(0, 2, 0, 1).mode)
    }
}

class CleanTimelineResumeTest {
    // The end of the a296 clean at 11:09 on 2026-11-15 (second floor). The official app listed:
    // "Kitchen · Reiniging hervatten", "Pauzeren", "Reiniging voltooid".
    private fun ev(t: Long, i: Int, e: String) = CleanEvent(t, i, Json.parseToJsonElement(e) as kotlinx.serialization.json.JsonObject)
    private val events = listOf(
        ev(1794737561, 42, """{"2":1,"421":0,"3":2,"4":35,"5":0,"6":1,"422":1,"10":0,"12":0}"""),
        ev(1794738975, 42, """{"2":1,"421":0,"3":2,"4":35,"5":0,"6":1,"422":1,"10":0,"12":0}"""),
        ev(1794738975, 42, """{"425":0,"2":1}"""),
        ev(1794739400, 11, """{"111":5,"112":10,"1":82,"113":1}"""),
        ev(1794739407, 12, """{"121":123}"""),
    )

    @Test
    fun aRoomGoneBackToIsAResumeAndAPauseIsListed() {
        val names = mapOf(1 to "Kitchen")
        val lines = describeCleanEvents(events) { names[it] }
        assertEquals(listOf("Cleaning Kitchen", "Resumed cleaning Kitchen", "Paused", "Clean finished"), lines.map { it.title })
    }

    @Test
    fun aSlideWaterLevelIsReadAsTheSlider() {
        val first = describeCleanEvents(events) { null }.first()
        assertEquals("15 · Medium", first.settings?.waterLabel)
    }
}
