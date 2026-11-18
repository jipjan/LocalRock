package com.kodraliu.localrock.shared.vacuum

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.intOrNull
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.put

/**
 * A routine as the official app saves it ("scene", type WORKFLOW): steps run one after another, each
 * waiting for the robot to finish the one before. local_roborock_server stores routines and runs
 * them itself, sending each step as `app_start`, `app_segment_clean` or `app_zoned_clean` with its
 * suction, water flow and mop route.
 */
data class RoutineDraft(
    val name: String,
    val steps: List<RoutineStep>,
)

enum class RoutineStepKind { FULL, ROOMS, ZONES }

data class RoutineStep(
    val kind: RoutineStepKind,
    /** Room (segment) ids for [RoutineStepKind.ROOMS]. */
    val rooms: List<Int> = emptyList(),
    /** Rectangles in map millimetres for [RoutineStepKind.ZONES]. */
    val zones: List<ZoneRect> = emptyList(),
    val fanPower: Int = VacuumFanPower.BALANCED,
    val waterBoxMode: Int = WaterBoxMode.OFF,
    val mopMode: Int = MopRoute.STANDARD,
    /** Passes, 1–3. */
    val repeat: Int = 1,
    /** The map the rooms or zones belong to. */
    val mapFlag: Int = 0,
    /** The official app's per-step id ("tid", a millisecond time); kept when a routine is edited. */
    val tid: String? = null,
)

/** A step the robot cannot run: no rooms or no zones picked. */
fun RoutineStep.isComplete(): Boolean = when (kind) {
    RoutineStepKind.FULL -> true
    RoutineStepKind.ROOMS -> rooms.isNotEmpty()
    RoutineStepKind.ZONES -> zones.isNotEmpty()
}

/** The robot's "task complete" data point: a step is over when the robot reports it. */
private const val TASK_COMPLETE_DP = 130

/** What the official app puts in `source` for a step started by a routine. */
private const val ROUTINE_SOURCE = 101

/**
 * The routine's `param` in the official app's shape. Each step's own `param` is a JSON string, as
 * the official app stores it. [now] seeds the step ids of new steps.
 */
fun encodeRoutineParam(
    draft: RoutineDraft,
    duid: String,
    now: Long,
    roomName: (Int) -> String? = { null },
): JsonObject = buildJsonObject {
    put("triggers", JsonArray(emptyList()))
    put("action", buildJsonObject {
        put("type", "S")
        put("items", buildJsonArray {
            draft.steps.forEachIndexed { index, step ->
                val id = index + 1
                val tid = step.tid ?: (now + index).toString()
                val method = when (step.kind) {
                    RoutineStepKind.FULL -> "do_scenes_app_start"
                    RoutineStepKind.ROOMS -> "do_scenes_segments"
                    RoutineStepKind.ZONES -> "do_scenes_zones"
                }
                val entry = buildJsonObject {
                    put("tid", tid)
                    when (step.kind) {
                        RoutineStepKind.ROOMS -> put("segs", buildJsonArray {
                            step.rooms.forEach { add(buildJsonObject { put("sid", it) }) }
                        })
                        RoutineStepKind.ZONES -> put("zones", buildJsonArray {
                            step.zones.forEachIndexed { zid, z ->
                                add(buildJsonObject {
                                    put("zid", zid)
                                    // The server repeats each zone by its own count; the step's passes are that count.
                                    put("repeat", step.repeat.coerceIn(1, 3))
                                    put("range", buildJsonArray {
                                        add(JsonPrimitive(minOf(z.x1, z.x2))); add(JsonPrimitive(minOf(z.y1, z.y2)))
                                        add(JsonPrimitive(maxOf(z.x1, z.x2))); add(JsonPrimitive(maxOf(z.y1, z.y2)))
                                    })
                                })
                            }
                        })
                        RoutineStepKind.FULL -> {}
                    }
                    put("map_flag", step.mapFlag)
                    put("fan_power", step.fanPower)
                    put("water_box_mode", step.waterBoxMode)
                    put("mop_mode", step.mopMode)
                    put("mop_template_id", step.mopMode)
                    put("repeat", step.repeat.coerceIn(1, 3))
                    put("clean_order_mode", 1)
                    put("auto_dry", 1)
                    put("auto_dustCollection", 1)
                    put("region_num", 0)
                }
                val inner = buildJsonObject {
                    put("id", id)
                    put("method", method)
                    if (step.kind == RoutineStepKind.FULL) {
                        // The server reads a whole-home step's settings from a list, as `app_start` takes them.
                        put("params", JsonArray(listOf(entry)))
                    } else {
                        put("params", buildJsonObject {
                            put("data", JsonArray(listOf(entry)))
                            put("source", ROUTINE_SOURCE)
                        })
                    }
                }
                add(buildJsonObject {
                    put("id", id)
                    put("type", "CMD")
                    put("name", stepTitle(step, roomName))
                    put("entityId", duid)
                    put("param", Json.encodeToString(JsonObject.serializer(), inner))
                    put("finishDpIds", buildJsonArray { add(JsonPrimitive(TASK_COMPLETE_DP)) })
                })
            }
        })
    })
    put("matchType", "NONE")
}

/**
 * Reads a saved routine back. Null when it holds anything LocalRock cannot show as a step (a
 * trigger, or a command other than the three cleaning kinds); such a routine can still be run.
 */
fun decodeRoutine(name: String, param: JsonElement?): RoutineDraft? {
    val outer = when {
        param is JsonObject -> param
        param is JsonPrimitive && param.isString -> runCatching { Json.parseToJsonElement(param.content).jsonObject }.getOrNull()
        else -> null
    } ?: return null
    val items = runCatching { outer["action"]!!.jsonObject["items"]!!.jsonArray }.getOrNull() ?: return null
    val steps = items.map { raw ->
        val item = raw as? JsonObject ?: return null
        if (item["type"]?.jsonPrimitive?.contentOrNull?.uppercase() != "CMD") return null
        val inner = when (val p = item["param"]) {
            is JsonObject -> p
            is JsonPrimitive -> runCatching { Json.parseToJsonElement(p.content).jsonObject }.getOrNull()
            else -> null
        } ?: return null
        val method = inner["method"]?.jsonPrimitive?.contentOrNull
        val kind = when (method) {
            "do_scenes_app_start" -> RoutineStepKind.FULL
            "do_scenes_segments" -> RoutineStepKind.ROOMS
            "do_scenes_zones" -> RoutineStepKind.ZONES
            else -> return null
        }
        val params = inner["params"]
        val entry = when (params) {
            is JsonArray -> params.firstOrNull() as? JsonObject
            is JsonObject -> (params["data"] as? JsonArray)?.firstOrNull() as? JsonObject
            else -> null
        } ?: JsonObject(emptyMap())
        fun int(key: String) = (entry[key] as? JsonPrimitive)?.intOrNull
        RoutineStep(
            kind = kind,
            rooms = (entry["segs"] as? JsonArray).orEmpty().mapNotNull { seg ->
                when (seg) {
                    is JsonObject -> (seg["sid"] as? JsonPrimitive)?.intOrNull
                    is JsonPrimitive -> seg.intOrNull
                    else -> null
                }
            },
            zones = (entry["zones"] as? JsonArray).orEmpty().mapNotNull { z ->
                val zone = z as? JsonObject ?: return@mapNotNull null
                val r = (zone["range"] as? JsonArray)?.mapNotNull { (it as? JsonPrimitive)?.intOrNull }
                if (r == null || r.size < 4) null
                else ZoneRect(r[0], r[1], r[2], r[3])
            },
            fanPower = int("fan_power") ?: VacuumFanPower.BALANCED,
            waterBoxMode = int("water_box_mode") ?: WaterBoxMode.OFF,
            mopMode = int("mop_mode") ?: MopRoute.STANDARD,
            repeat = int("repeat") ?: 1,
            mapFlag = int("map_flag") ?: 0,
            tid = (entry["tid"] as? JsonPrimitive)?.contentOrNull,
        )
    }
    return RoutineDraft(name, steps)
}

/** A step's title, also stored as the step's name for the official app's list. */
fun stepTitle(step: RoutineStep, roomName: (Int) -> String? = { null }): String = when (step.kind) {
    RoutineStepKind.FULL -> "Full clean"
    RoutineStepKind.ROOMS -> if (step.rooms.isEmpty()) "Rooms" else
        step.rooms.joinToString(", ") { roomName(it) ?: "Room $it" }
    RoutineStepKind.ZONES -> if (step.zones.size == 1) "1 zone" else "${step.zones.size} zones"
}
