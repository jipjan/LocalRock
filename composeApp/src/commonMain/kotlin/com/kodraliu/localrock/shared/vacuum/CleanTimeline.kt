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
