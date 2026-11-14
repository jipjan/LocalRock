package com.kodraliu.localrock.shared.vacuum

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.longOrNull

/** One event of a past clean (`get_clean_detail`): when, its type `i`, and its coded fields `e`. */
data class CleanEvent(val epoch: Long, val type: Int, val fields: JsonObject)

/** A page of `get_clean_detail`: its events, and whether the robot has more after them. */
data class CleanEventPage(val events: List<CleanEvent>, val more: Boolean)

/** The robot changed state during the clean: event 11, fields 111 (from), 112 (to) and 1 (battery %). */
data class CleanStateChange(val epoch: Long, val from: Int, val to: Int, val battery: Int?)

/** Event type 11 carries a state change (seen on an a296: `{"111":8,"112":25,"1":73,"113":1}`). */
const val CLEAN_EVENT_STATE_CHANGE = 11

/**
 * Reads a `get_clean_detail` answer: `{"begin":…,"from":0,"num":47,"more":1,"events":[{"t":…,"i":25,
 * "e":"{\"0\":254}"}, …]}`. Each event's `e` is itself JSON in a string.
 */
fun parseCleanEventPage(result: JsonElement?): CleanEventPage? {
    val obj = (if (result is JsonArray) result.firstOrNull() else result) as? JsonObject ?: return null
    val events = (obj["events"] as? JsonArray).orEmpty().mapNotNull { item ->
        val ev = item as? JsonObject ?: return@mapNotNull null
        val t = (ev["t"] as? JsonPrimitive)?.longOrNull ?: return@mapNotNull null
        val i = (ev["i"] as? JsonPrimitive)?.intOrNull ?: return@mapNotNull null
        val fields = when (val e = ev["e"]) {
            is JsonObject -> e
            is JsonPrimitive -> e.contentOrNull?.let { runCatching { Json.parseToJsonElement(it) as? JsonObject }.getOrNull() }
            else -> null
        } ?: JsonObject(emptyMap())
        CleanEvent(t, i, fields)
    }
    return CleanEventPage(events, (obj["more"] as? JsonPrimitive)?.intOrNull == 1)
}

/** The state changes among [events], in time order; repeats of the same state are left out. */
fun cleanStateChanges(events: List<CleanEvent>): List<CleanStateChange> = events
    .filter { it.type == CLEAN_EVENT_STATE_CHANGE }
    .mapNotNull { e ->
        fun f(k: String) = (e.fields[k] as? JsonPrimitive)?.intOrNull
        val from = f("111") ?: return@mapNotNull null
        val to = f("112") ?: return@mapNotNull null
        CleanStateChange(e.epoch, from, to, f("1"))
    }
    .filter { it.from != it.to }
    .sortedBy { it.epoch }

internal fun cleanDetailParams(begin: Long, from: Int) = JsonObject(mapOf(
    "begin" to JsonPrimitive(begin), "from" to JsonPrimitive(from), "id" to JsonPrimitive(0), "type" to JsonPrimitive(0),
))

/** A line of a past clean's timeline, as the official app words it. */
data class CleanTimelineEntry(
    val epoch: Long,
    val title: String,
    val detail: String? = null,
    val milestone: Boolean = false,
    /** How a room (or the whole clean) was cleaned, shown as the official app's row of icons. */
    val settings: CleanRunSettings? = null,
)

/**
 * The settings a start or room event carries: fields 3 (suction), 4 (water), 5 (route) and 6 (passes).
 * Checked on an a296 against the official app's icons: suction 1 is quiet (moon icon); water 1 and 2
 * showed as the drop with 5 and 15, i.e. low and medium (the plugin has water icons 0-3: off, low,
 * medium, high); route 0 is standard. Higher suction values follow the robot's usual order.
 */
data class CleanRunSettings(val suction: Int?, val water: Int?, val route: Int?, val passes: Int?) {
    val mode: String?
        get() = when {
            suction == null || water == null -> null
            suction > 0 && water > 0 -> "Vacuum and mop"
            water > 0 -> "Mop only"
            suction > 0 -> "Vacuum only"
            else -> null
        }
    val suctionLabel: String?
        get() = when (suction) { null, 0 -> null; 1 -> "Quiet"; 2 -> "Standard"; 3 -> "Strong"; 4 -> "MAX"; 5 -> "MAX+"; else -> "Suction $suction" }
    val waterLabel: String?
        get() = when (water) { null, 0 -> null; 1 -> "Low"; 2 -> "Medium"; 3 -> "High"; else -> "Water $water" }
    val routeLabel: String?
        get() = when (route) { null -> null; 0 -> "Standard"; else -> "Route $route" }
}

/*
 * Event types, matched on an a296 (2026-11-14) against the official app's timeline of the same clean:
 * 25 start (251 source, 252 clean type), 42 cleaning a room (2 room id, 421 kind, 3 suction, 4 water,
 * 6 passes), 52 mop wash (521 wash mode, 522 water temperature), 53 resumed after a wash, 21 settings
 * changed by hand (211 what), 12 clean finished, 23 auto-empty (231 frequency).
 */
private fun JsonObject.int(key: String) = (this[key] as? JsonPrimitive)?.intOrNull

private fun runSettings(f: JsonObject): CleanRunSettings? =
    if (f.int("3") == null && f.int("4") == null) null
    else CleanRunSettings(f.int("3"), f.int("4"), f.int("5"), f.int("6"))

private fun cleanMode(f: JsonObject): String? = runSettings(f)?.mode

/**
 * The timeline of a past clean in the official app's words: start, each room, mop washes, resumes,
 * manual changes, the end and auto-emptying. Events whose meaning is unknown are left out, as the
 * official app shows only these. [roomName] turns a room id into its name.
 */
fun describeCleanEvents(events: List<CleanEvent>, roomName: (Int) -> String?): List<CleanTimelineEntry> =
    events.sortedBy { it.epoch }.mapNotNull { e ->
        val f = e.fields
        when (e.type) {
            25 -> if (f.int("252") == null && f.int("251") == null) null else {
                val type = when (f.int("252")) { 1 -> "full clean"; else -> "clean" }
                val source = when (f.int("251")) { 2 -> "from the app"; else -> null }
                CleanTimelineEntry(e.epoch, "Started $type", listOfNotNull(source, cleanMode(f)).joinToString(" · ").ifEmpty { null }, milestone = true)
            }
            42 -> {
                val room = f.int("2")
                val title = if (f.int("421") == 3) "Re-cleaning areas blocked by people or pets"
                else "Cleaning " + (room?.let(roomName) ?: "room ${room ?: "?"}")
                val passes = f.int("6")?.takeIf { it > 0 }?.let { "×$it" }
                CleanTimelineEntry(
                    e.epoch, title, listOfNotNull(cleanMode(f), passes).joinToString(" · ").ifEmpty { null },
                    milestone = true, settings = runSettings(f),
                )
            }
            52 -> {
                val mode = if (f.int("521") == 10) "Smart" else null
                val temp = when (f.int("522")) { 0 -> "Room temperature"; 1 -> "Warm water"; 2 -> "Hot water"; else -> null }
                CleanTimelineEntry(e.epoch, "Mop wash", listOfNotNull(mode, if (mode != null) temp else null).joinToString(" · ").ifEmpty { null })
            }
            53 -> CleanTimelineEntry(e.epoch, "Resumed")
            21 -> CleanTimelineEntry(e.epoch, "Settings changed by hand", if (f.int("211") == 1) "Water flow changed" else null)
            12 -> CleanTimelineEntry(e.epoch, "Clean finished", milestone = true)
            23 -> CleanTimelineEntry(e.epoch, "Auto-empty", when (f.int("231")) { 1 -> "Balanced"; 2 -> "High"; else -> null })
            else -> null
        }
    }
