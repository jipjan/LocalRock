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
import com.kodraliu.localrock.resources.*
import com.kodraliu.localrock.shared.text.UiText
import com.kodraliu.localrock.shared.text.rawText
import com.kodraliu.localrock.shared.text.uiPlural
import com.kodraliu.localrock.shared.text.uiText

/**
 * A routine as the official app saves it ("scene", type WORKFLOW): steps run one after another, each
 * waiting for the robot to finish the one before. local_roborock_server stores routines and runs
 * them itself, sending each step as `app_start`, `app_segment_clean` or `app_zoned_clean` with its
 * suction, water flow and mop route.
 */
data class RoutineDraft(
    val name: String,
    val steps: List<RoutineStep>,
    /**
     * The official app's routine kind: 101–104 for its recommended routines (the icon it shows), 1004
     * seen on one made by hand. Kept as it was; LocalRock sets it only for the recommended ones.
     */
    val tagId: String? = null,
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
    /** Passes, 1–3; 0 (the official app's recommended routines) leaves it to the robot, which cleans once. */
    val repeat: Int = 1,
    /**
     * Vacuum and mop together (0) or vacuum first, then mop (1), the official app's `seq_type`; null where
     * the official app leaves it out (some recommended routines).
     */
    val seqType: Int? = SEQ_TYPE_TOGETHER,
    /** The map the rooms or zones belong to. */
    val mapFlag: Int = 0,
    /**
     * The step's id ("tid"), handed out by the robot when the step's rooms or zones are registered with
     * it (`set_scenes_segments` / `set_scenes_zones`), as the official app does; kept when a routine is
     * edited and cleared when the step's rooms or zones change. A full clean has none.
     */
    val tid: String? = null,
    /** The robot's ids for [zones], from `set_scenes_zones`, in the same order. */
    val zoneIds: List<Int> = emptyList(),
)

/** What the robot answers when a step's rooms or zones are registered: the step id, map and zone ids. */
data class SceneRegistration(val tid: String, val mapFlag: Int?, val zoneIds: List<Int>)

/**
 * Reads the answer to `set_scenes_segments` / `set_scenes_zones`, e.g. (a296, 2026-11-18)
 * `[{"tid":"1795028618174","map_flag":1,"segs":[{"sid":3},{"sid":4}]}]` or `[{"tid":"…","zones":[{"zid":8}]}]`.
 */
fun parseSceneRegistration(result: JsonElement?): SceneRegistration? {
    val entry = ((result as? JsonArray)?.firstOrNull() ?: result) as? JsonObject ?: return null
    val tid = (entry["tid"] as? JsonPrimitive)?.contentOrNull?.takeIf { it.isNotBlank() } ?: return null
    val zids = (entry["zones"] as? JsonArray).orEmpty().mapNotNull { ((it as? JsonObject)?.get("zid") as? JsonPrimitive)?.intOrNull }
    return SceneRegistration(tid, (entry["map_flag"] as? JsonPrimitive)?.intOrNull, zids)
}

/** The step with what the robot handed out for it. */
fun RoutineStep.registered(reg: SceneRegistration): RoutineStep =
    copy(tid = reg.tid, mapFlag = reg.mapFlag ?: mapFlag, zoneIds = reg.zoneIds)

/** A changed step loses its robot ids, so it is registered again on saving. */
fun RoutineStep.edited(next: RoutineStep): RoutineStep =
    if (next.kind != kind || next.rooms != rooms || next.zones != zones) next.copy(tid = null, zoneIds = emptyList()) else next

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
                // A step the robot could not register still needs an id; the server runs it without one.
                val tid = step.tid ?: (now + index).toString()
                val method = when (step.kind) {
                    RoutineStepKind.FULL -> "do_scenes_app_start"
                    RoutineStepKind.ROOMS -> "do_scenes_segments"
                    RoutineStepKind.ZONES -> "do_scenes_zones"
                }
                val entry = if (step.kind == RoutineStepKind.FULL) buildJsonObject {
                    // The official app's whole-home step: settings only (a296, 2026-11-18).
                    put("fan_power", step.fanPower)
                    put("water_box_mode", step.waterBoxMode)
                    put("mop_mode", step.mopMode)
                    put("mop_template_id", step.mopMode)
                    put("repeat", stepRepeat(step))
                    put("auto_dustCollection", 1)
                    put("source", ROUTINE_SOURCE)
                    step.seqType?.let { put("seq_type", it) }
                } else buildJsonObject {
                    put("tid", tid)
                    when (step.kind) {
                        RoutineStepKind.ROOMS -> put("segs", buildJsonArray {
                            step.rooms.forEach { add(buildJsonObject { put("sid", it) }) }
                        })
                        RoutineStepKind.ZONES -> put("zones", buildJsonArray {
                            step.zones.forEachIndexed { zid, z ->
                                add(buildJsonObject {
                                    put("zid", step.zoneIds.getOrElse(zid) { zid })
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
                    put("repeat", stepRepeat(step))
                    put("clean_order_mode", 1)
                    put("auto_dry", 1)
                    put("auto_dustCollection", 1)
                    put("region_num", 0)
                    put("seq_type", step.seqType ?: SEQ_TYPE_TOGETHER)
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
                    // The official app names a room step by its rooms, space-separated, and leaves the others blank.
                    put("name", when (step.kind) {
                        RoutineStepKind.ROOMS -> step.rooms.joinToString(" ") { roomName(it) ?: "Room $it" }
                        else -> ""
                    })
                    put("entityId", duid)
                    put("param", Json.encodeToString(JsonObject.serializer(), inner))
                    put("finishDpIds", buildJsonArray { add(JsonPrimitive(TASK_COMPLETE_DP)) })
                })
            }
        })
    })
    put("matchType", "NONE")
    draft.tagId?.let { put("tagId", it) }
}

private fun stepRepeat(step: RoutineStep): Int = if (step.repeat == 0) 0 else step.repeat.coerceIn(1, 3)

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
            zoneIds = (entry["zones"] as? JsonArray).orEmpty().mapNotNull { ((it as? JsonObject)?.get("zid") as? JsonPrimitive)?.intOrNull },
            fanPower = int("fan_power") ?: VacuumFanPower.BALANCED,
            waterBoxMode = int("water_box_mode") ?: WaterBoxMode.OFF,
            mopMode = int("mop_mode") ?: MopRoute.STANDARD,
            repeat = int("repeat") ?: 1,
            mapFlag = int("map_flag") ?: 0,
            seqType = int("seq_type"),
            tid = (entry["tid"] as? JsonPrimitive)?.contentOrNull,
        )
    }
    return RoutineDraft(name, steps, tagId = (outer["tagId"] as? JsonPrimitive)?.contentOrNull)
}

/** A step's title, also stored as the step's name for the official app's list. */
fun stepTitle(step: RoutineStep, roomName: (Int) -> String? = { null }): String = stepTitleText(step, roomName).english

/** [stepTitle] for the UI, in the app's language. */
fun stepTitleText(step: RoutineStep, roomName: (Int) -> String? = { null }): UiText = when (step.kind) {
    RoutineStepKind.FULL -> uiText(Res.string.step_full_clean, "Full clean")
    RoutineStepKind.ROOMS -> if (step.rooms.isEmpty()) {
        uiText(Res.string.step_rooms, "Rooms")
    } else {
        UiText.Joined(step.rooms.map { id -> roomName(id)?.let(::rawText) ?: uiText(Res.string.step_room_number, "Room $id", id) }, ", ")
    }
    RoutineStepKind.ZONES -> step.zones.size.let {
        uiPlural(Res.plurals.step_zones, it, if (it == 1) "1 zone" else "$it zones", it)
    }
}
