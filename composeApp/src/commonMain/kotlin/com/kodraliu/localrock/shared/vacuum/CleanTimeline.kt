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
/** [rawCount] counts every entry the robot sent, including ones not read, so the next page starts after them. */
data class CleanEventPage(val events: List<CleanEvent>, val more: Boolean, val rawCount: Int = events.size)

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
    val raw = (obj["events"] as? JsonArray).orEmpty()
    val events = raw.mapNotNull { item ->
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
    return CleanEventPage(events, (obj["more"] as? JsonPrimitive)?.intOrNull == 1, raw.size)
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
    /** The start's cleaning preferences, the official app's bullet lines under "Start full clean". */
    val preferences: List<CleanPreference> = emptyList(),
)

/** One line of the official app's start card (plugin strings `clean_node_clean_pref_line*`). */
enum class CleanPreference {
    HOME_PERCEPTION, ORDER_SEQUENTIAL, ORDER_CUSTOM, ORDER_DEFAULT,
    PATH_AI, PATH_FLOOR_DIRECTION, PATH_DIRECTION_SWITCH, COVERAGE_HIGH, COVERAGE_LESS_COLLISION,
}

/**
 * The start card's lines from field 254 of the start event (four numbers), as the a296 plugin reads
 * them: the room order from bits 24–27 of the first (1 sequential, 2 custom, 3 default), the path from
 * bits 28–31 (1 along the floor direction, 2 automatic direction switch, otherwise AI path planning),
 * the coverage from the low 4 bits of the third (1 high coverage, 2 fewer collisions). The first line
 * is always there. Checked against the official app's timeline of two a296 cleans (2026-11-18).
 */
fun cleanPreferences(values: List<Long>): List<CleanPreference> {
    if (values.isEmpty()) return emptyList()
    val first = values[0]
    val order = ((first shr 24) and 15).toInt()
    val path = ((first shr 28) and 15).toInt()
    val coverage = values.getOrNull(2)?.let { (it and 15).toInt() }
    return listOfNotNull(
        CleanPreference.HOME_PERCEPTION,
        when (order) { 1 -> CleanPreference.ORDER_SEQUENTIAL; 2 -> CleanPreference.ORDER_CUSTOM; 3 -> CleanPreference.ORDER_DEFAULT; else -> null },
        when (path) { 1 -> CleanPreference.PATH_FLOOR_DIRECTION; 2 -> CleanPreference.PATH_DIRECTION_SWITCH; else -> CleanPreference.PATH_AI },
        when (coverage) { 1 -> CleanPreference.COVERAGE_HIGH; 2 -> CleanPreference.COVERAGE_LESS_COLLISION; else -> null },
    )
}

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
        get() = when (water) {
            null, 0 -> null
            1 -> "Low"; 2 -> "Medium"; 3 -> "High"
            // Water slide robots log the slider code without its 200 (a296: 35 for 235, the medium mark).
            else -> WaterBoxMode.slideLabel(water + 200) ?: "Water $water"
        }
    val routeLabel: String?
        get() = when (route) { null -> null; 0 -> "Standard"; else -> "Route $route" }
}

/*
 * Event types, matched on an a296 (2026-11-14) against the official app's timeline of the same clean:
 * 25 start (251 source, 252 clean type), 42 cleaning a room (2 room id, 421 kind, 3 suction, 4 water,
 * 6 passes), 52 mop wash (521 wash mode, 522 water temperature), 53 resumed after a wash, 21 settings
 * changed by hand (211 what), 12 clean finished, 23 auto-empty (231 frequency).
 *
 * Matched on an a296 (2026-11-18) against the official app's timeline of the same clean: 31 mop wash
 * failed (311 the dock error; shown once per error, the robot logs it again on every retry), 22 with
 * field 3 = 5 switched to vacuum only, 54 auto-empty during the clean (541 = 1 "Light"; not shown right
 * after a 23, which the official app shows instead), 13 with 131 = 14 resumed.
 * Not shown, as in the official app: 51 back to the dock mid-clean (512: 1 wash, 4 empty), 13 with 131 = 15
 * (back from a wash that failed), 22 with other values, 24 (241–246) and 47 (471 the room order).
 */
/** An end-of-clean auto-empty (23) is followed by a 54 that the official app does not show again. */
private const val AUTO_EMPTY_PAIR_SECONDS = 60L

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
fun describeCleanEvents(events: List<CleanEvent>, roomName: (Int) -> String?): List<CleanTimelineEntry> {
    // A room the robot goes back to is logged twice at once: the usual room event, and one with field
    // 425 that the official app shows as "<room> · resume cleaning" (a296, 2026-11-15). Only that one is shown.
    val resumed = events.filter { it.type == 42 && it.fields.int("425") != null }.map { it.epoch to it.fields.int("2") }.toSet()
    val washFailures = mutableSetOf<Int>()
    val autoEmptyEnds = events.filter { it.type == 23 }.map { it.epoch }
    return events.sortedBy { it.epoch }.mapNotNull { e ->
        val f = e.fields
        when (e.type) {
            25 -> if (f.int("252") == null && f.int("251") == null) null else {
                val type = when (f.int("252")) { 1 -> "full clean"; else -> "clean" }
                val source = when (f.int("251")) { 2 -> "from the app"; else -> null }
                // The preferences come in a separate start event at the same moment.
                val prefs = events.firstOrNull { it.type == 25 && it.epoch == e.epoch && it.fields["254"] is JsonArray }
                    ?.let { p -> (p.fields["254"] as JsonArray).mapNotNull { (it as? JsonPrimitive)?.longOrNull } }
                    ?.let(::cleanPreferences).orEmpty()
                CleanTimelineEntry(
                    e.epoch, "Started $type", listOfNotNull(source, cleanMode(f)).joinToString(" · ").ifEmpty { null },
                    milestone = true, preferences = prefs,
                )
            }
            42 -> run {
                val room = f.int("2")
                val name = room?.let(roomName) ?: "room ${room ?: "?"}"
                if (f.int("425") != null) return@run CleanTimelineEntry(e.epoch, "Resumed cleaning $name", milestone = true)
                if ((e.epoch to room) in resumed) return@run null
                val title = if (f.int("421") == 3) "Re-cleaning areas blocked by people or pets"
                else "Cleaning $name"
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
            31 -> f.int("311")?.takeIf { it != 0 && washFailures.add(it) }?.let {
                CleanTimelineEntry(e.epoch, "Mop wash failed ($it)", DockErrorCodes.describe(it))
            }
            22 -> if (f.int("3") == 5) CleanTimelineEntry(e.epoch, "Switched to vacuum only") else null
            13 -> if (f.int("131") == 14) CleanTimelineEntry(e.epoch, "Resumed") else null
            54 -> if (autoEmptyEnds.any { e.epoch - it in 0..AUTO_EMPTY_PAIR_SECONDS }) null
            else CleanTimelineEntry(e.epoch, "Auto-empty", if (f.int("541") == 1) "Light" else null)
            21 -> CleanTimelineEntry(e.epoch, "Settings changed by hand", if (f.int("211") == 1) "Water flow changed" else null)
            12 -> CleanTimelineEntry(e.epoch, "Clean finished", milestone = true)
            23 -> CleanTimelineEntry(e.epoch, "Auto-empty", when (f.int("231")) { 1 -> "Balanced"; 2 -> "High"; else -> null })
            // State changes are not shown, except a pause, which the official app lists ("Pauzeren").
            CLEAN_EVENT_STATE_CHANGE -> if (f.int("112") == VacuumStateCodes.PAUSED) CleanTimelineEntry(e.epoch, "Paused") else null
            else -> null
        }
    }
}

/**
 * The room the running clean is on: the last room started (event 41 or 42, field 2 the segment) that
 * has not been finished since (event 43). From the a296's `get_rt_clean_detail`.
 */
fun currentCleanRoom(events: List<CleanEvent>): Int? {
    var room: Int? = null
    for (e in events.sortedBy { it.epoch }) {
        val seg = (e.fields["2"] as? JsonPrimitive)?.intOrNull ?: continue
        when (e.type) {
            41, 42 -> room = seg
            43 -> if (room == seg) room = null
        }
    }
    return room
}

