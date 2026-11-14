package com.kodraliu.localrock.shared.vacuum

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.intOrNull

/**
 * A robot setting that newer firmware advertises through a `newFeatureSet` bit and exposes as a
 * `get_*` / `set_*` command pair. Bit indexes and command names follow python-roborock
 * (NewFeatureStrBit, RoborockCommand); a null [bit] means no `newFeatureSet` bit is known for it, so
 * it shows only if the robot answers its get command with 0 or 1; the set payload is not documented there, so it mirrors the
 * shape of the get reply (see [parseBetaReply] and [encodeBetaSetParams]).
 */
/** The settings page a feature is shown on, following the official app's grouping. */
enum class FeatureSection { CARPET, FLOOR, OBSTACLE, DOCK, ROBOT, BETA }

/**
 * [choices], when set, makes the feature a picker of those values instead of an on/off switch.
 *
 * Reads and writes take one of three forms:
 * - the plain [getMethod] / [setMethod] pair;
 * - with [robotSetting], the generic pair the official app uses for many options:
 *   `app_get_robot_setting {"name": K}` answered `{"status": n}`, and
 *   `app_set_robot_setting {"name": K, "status": n}` (captured from the official app, a296);
 * - with [readField], one field of [getMethod]'s answer, or with [readBit] one bit of it, such as
 *   `get_status` → `switch_status` bit 4; [setKey] then names the field the setter takes.
 *
 * [alsoRequiresBit] gates a feature on a second `newFeatureSet` bit, [unlessBit] hides it when that
 * bit is set (so a robot gets the variant of a feature it actually has).
 */
data class BetaFeatureDef(
    val id: String,
    val title: String,
    val description: String,
    val bit: Int?,
    val getMethod: String,
    val setMethod: String,
    val section: FeatureSection = FeatureSection.BETA,
    val choices: List<Pair<Int, String>>? = null,
    val robotSetting: String? = null,
    val readField: String? = null,
    val readBit: Int? = null,
    val setKey: String? = null,
    val alsoRequiresBit: Int? = null,
    val unlessBit: Int? = null,
    /** Send the set params as `[{key: n}]` only, as the official app does for this command. */
    val wrapSet: Boolean = false,
    /**
     * With [readBit] and no [readField]: the reply is one number holding several options (the camera's
     * `get_camera_status`, e.g. `[419]`), and a change is written back as the whole number with only
     * this bit changed, `[n]`, as the official app does with `set_camera_status`.
     */
    val bitmaskWrite: Boolean = false,
)

private const val GET_ROBOT_SETTING = "app_get_robot_setting"
private const val SET_ROBOT_SETTING = "app_set_robot_setting"

/** A switch read and written through the generic robot-setting pair. */
private fun robotSettingSwitch(
    id: String,
    name: String,
    title: String,
    description: String,
    bit: Int?,
    section: FeatureSection,
    choices: List<Pair<Int, String>>? = null,
) = BetaFeatureDef(
    id = id, title = title, description = description, bit = bit,
    getMethod = GET_ROBOT_SETTING, setMethod = SET_ROBOT_SETTING, section = section,
    choices = choices, robotSetting = name,
)

/**
 * A switch read as one bit of the option bitmask `get_offline_map_status` answers as
 * `{"common_switch_status": n}` (bit 0 offline map, 3 carpets first, 4 re-mop, 5 cross carpets; an
 * a296's get_status does not carry it), written with its own `{"status": n}` setter.
 */
private fun statusBitSwitch(
    id: String,
    setMethod: String,
    statusBit: Int,
    title: String,
    description: String,
    bit: Int?,
    section: FeatureSection,
) = BetaFeatureDef(
    id = id, title = title, description = description, bit = bit,
    getMethod = "get_offline_map_status", setMethod = setMethod, section = section,
    readField = "common_switch_status", readBit = statusBit, setKey = "status",
)

/**
 * One bit of the camera's option bitmask (`get_camera_status` / `set_camera_status [n]`). Bits seen on an
 * a296 with the official app (2026-11-14): 2 fill light automatic (385 ↔ 387), 32 "I have pets" (387 →
 * 419), 4096 Pet Snaps (419 ↔ 4515). Bit 4 (0x4) is the remote-viewing authorization and cannot be set.
 */
private fun cameraBitSwitch(
    id: String,
    bitIndex: Int,
    title: String,
    description: String,
    bit: Int?,
    section: FeatureSection,
    choices: List<Pair<Int, String>>? = null,
) = BetaFeatureDef(
    id = id, title = title, description = description, bit = bit,
    getMethod = "get_camera_status", setMethod = "set_camera_status", section = section,
    choices = choices, readBit = bitIndex, bitmaskWrite = true,
)

/** The value to write for [def] set to [value] (0/1), when the robot's whole bitmask is [raw]. */
fun bitmaskWithBit(raw: Int, def: BetaFeatureDef, value: Int): Int {
    val mask = 1 shl requireNotNull(def.readBit)
    return if (value == 1) raw or mask else raw and mask.inv()
}

val BetaFeatureDefs = listOf(
    BetaFeatureDef(
        id = "right_brush_stretch",
        title = "FlexiArm side brush",
        description = "Extends the side brush to reach into corners and along edges",
        bit = 54,
        getMethod = "get_right_brush_stretch_status",
        setMethod = "set_right_brush_stretch_status",
        section = FeatureSection.FLOOR,
    ),
    BetaFeatureDef(
        id = "corner_mop_stretch",
        title = "FlexiArm mop",
        description = "Extends the mop to clean corners",
        bit = 40,
        getMethod = "get_stretch_tag_status",
        setMethod = "set_stretch_tag_status",
        section = FeatureSection.FLOOR,
    ),
    BetaFeatureDef(
        id = "gap_deep_clean",
        title = "Gap deep clean",
        description = "Cleans narrow gaps along walls and furniture",
        bit = 63,
        getMethod = "get_gap_deep_clean_status",
        setMethod = "set_gap_deep_clean_status",
        section = FeatureSection.FLOOR,
    ),
    BetaFeatureDef(
        id = "pet_supplies_deep_clean",
        title = "Pet supplies deep clean",
        description = "Extra cleaning around pet bowls and beds",
        bit = 43,
        getMethod = "get_pet_supplies_deep_clean_status",
        setMethod = "set_pet_supplies_deep_clean_status",
        section = FeatureSection.FLOOR,
    ),
    BetaFeatureDef(
        id = "dirty_object_detect",
        title = "Dirty object detection",
        description = "Spots dirty objects such as pet waste while cleaning",
        bit = 56,
        getMethod = "get_dirty_object_detect_status",
        setMethod = "set_dirty_object_detect_status",
        section = FeatureSection.OBSTACLE,
    ),
    BetaFeatureDef(
        id = "avoid_collision",
        title = "Collision avoidance",
        description = "Steers around obstacles instead of bumping them",
        bit = 36,
        getMethod = "get_collision_avoid_status",
        setMethod = "set_collision_avoid_status",
        section = FeatureSection.OBSTACLE,
        unlessBit = 118,
    ),
    // With bit 118 the official app offers only these two (captured on an a296: 1, then 2); 0/1 as a
    // switch would send "off" the robot does not have.
    BetaFeatureDef(
        id = "collision_mode",
        title = "Collision mode",
        description = "How closely the robot cleans around obstacles",
        bit = 36,
        getMethod = "get_collision_avoid_status",
        setMethod = "set_collision_avoid_status",
        section = FeatureSection.OBSTACLE,
        choices = listOf(1 to "Large coverage", 2 to "Less collision"),
        alsoRequiresBit = 118,
    ),
    BetaFeatureDef(
        id = "water_leak_check",
        title = "Water leak detection",
        description = "Stops and alerts if the robot detects a water leak",
        bit = 60,
        getMethod = "get_handle_leak_water_status",
        setMethod = "set_handle_leak_water_status",
        section = FeatureSection.ROBOT,
    ),
    BetaFeatureDef(
        id = "voice_control_led",
        title = "Voice control light",
        description = "Light on the robot that shows when it is listening",
        bit = 58,
        getMethod = "get_ap_mic_led_status",
        setMethod = "set_ap_mic_led_status",
        section = FeatureSection.ROBOT,
    ),
    BetaFeatureDef(
        id = "carpet_deep_clean",
        title = "Carpet deep clean",
        description = "Deeper cleaning passes over carpets",
        bit = 3,
        getMethod = "app_get_carpet_deep_clean_status",
        setMethod = "app_set_carpet_deep_clean_status",
        section = FeatureSection.CARPET,
    ),
    BetaFeatureDef(
        id = "clean_fluid_delivery",
        title = "Cleaning fluid dosing",
        description = "How much cleaning fluid the dock adds to the mop water",
        bit = 112,
        getMethod = "get_auto_delivery_cleaning_fluid",
        setMethod = "set_auto_delivery_cleaning_fluid",
        section = FeatureSection.DOCK,
        // Captured from the official app on an a296: 0, 1 and 3, labelled None / Standard / High volume.
        choices = listOf(0 to "None", 1 to "Standard", 3 to "High volume"),
    ),
    BetaFeatureDef(
        id = "dust_collection_switch",
        title = "Auto-empty frequency",
        description = "When the dock empties the robot's dust bin",
        bit = null,
        getMethod = "get_dust_collection_switch_status",
        setMethod = "set_dust_collection_switch_status",
        section = FeatureSection.DOCK,
        // Captured from the official app on an a296: Off, Balanced (after each clean), High (also during).
        choices = listOf(0 to "Off", 1 to "Balanced", 2 to "High"),
    ),
    BetaFeatureDef(
        id = "identify_furniture",
        title = "Furniture recognition",
        description = "Recognizes furniture on the map",
        bit = null,
        getMethod = "get_identify_furniture_status",
        setMethod = "set_identify_furniture_status",
        section = FeatureSection.OBSTACLE,
    ),
    BetaFeatureDef(
        id = "identify_ground_material",
        title = "Floor type recognition",
        description = "Recognizes carpet and hard floor",
        bit = null,
        getMethod = "get_identify_ground_material_status",
        setMethod = "set_identify_ground_material_status",
        section = FeatureSection.FLOOR,
    ),
    BetaFeatureDef(
        id = "follow_ground_material",
        title = "Clean by floor type",
        description = "Adjusts cleaning to the floor type it detects",
        bit = null,
        getMethod = "get_clean_follow_ground_material_status",
        setMethod = "set_clean_follow_ground_material_status",
        section = FeatureSection.FLOOR,
    ),

    // Below: captured from the official app talking to an a296 (Saros 20 Neo) through
    // local_roborock_server, or read from its device plugin; see docs/robot-commands-a296.md.
    statusBitSwitch(
        id = "dirty_replenish", setMethod = "app_set_dirty_replenish_clean_status", statusBit = 4,
        title = "Re-mop dirty spots", description = "Washes the mop and mops again where the floor is still dirty",
        bit = 34, section = FeatureSection.FLOOR,
    ),
    robotSettingSwitch(
        id = "clean_direction", name = "clean_direction_status",
        title = "Cleaning direction", description = "The pattern the robot follows across a room",
        bit = 70, section = FeatureSection.FLOOR,
        choices = listOf(0 to "Smart", 1 to "Along the floor", 2 to "Crosswise"),
    ),
    robotSettingSwitch(
        id = "low_area_access", name = "restrict_low_area_access_status",
        title = "Cleaning under low furniture", description = "How low a gap the robot drives into",
        bit = 116, section = FeatureSection.FLOOR,
        choices = listOf(2 to "Off", 1 to "Safe clearance", 0 to "Extreme clearance"),
    ),
    robotSettingSwitch(
        id = "lds_lifting", name = "lds_lifting_switch",
        title = "Lower the LiDAR", description = "Lowers the laser tower to reach under furniture",
        bit = 79, section = FeatureSection.FLOOR,
    ),
    robotSettingSwitch(
        id = "sway_arm_door_sill", name = "enable_sway_arm_for_high_door_sill_carpet",
        title = "Arm for high sills and carpets", description = "Uses the arm to climb high door sills and carpets",
        bit = null, section = FeatureSection.FLOOR,
    ),
    robotSettingSwitch(
        id = "remove_mop", name = "enable_remove_mop_status",
        title = "Detach mops automatically", description = "Leaves the mops in the dock when only vacuuming",
        bit = 80, section = FeatureSection.FLOOR,
    ),
    robotSettingSwitch(
        id = "dynamic_obstacle_replenish", name = "dynamic_obstacle_replenish_status",
        title = "Re-clean blocked areas", description = "Returns to spots that people or pets were blocking",
        bit = 131, section = FeatureSection.FLOOR,
    ),
    robotSettingSwitch(
        id = "omission_reminder", name = "enable_omission_area_clean_status",
        title = "Missed-area reminder", description = "Points out areas the robot could not clean",
        bit = 132, section = FeatureSection.FLOOR,
    ),
    statusBitSwitch(
        id = "priority_carpet", setMethod = "app_set_priority_carpet_cleaning_status", statusBit = 3,
        title = "Carpets first", description = "Detaches the mops, vacuums carpets first and then cleans the rest",
        bit = null, section = FeatureSection.CARPET,
    ),
    statusBitSwitch(
        id = "cross_carpet", setMethod = "app_set_cross_carpet_cleaning_status", statusBit = 5,
        title = "Cross carpets", description = "Drives over carpets without cleaning them",
        bit = 49, section = FeatureSection.CARPET,
    ),
    robotSettingSwitch(
        id = "long_haired_carpet", name = "long_haired_carpet_clean_status",
        title = "Adaptive lift", description = "Tilts up for high-pile carpets; works with Carpets first",
        bit = 75, section = FeatureSection.CARPET,
    ),
    robotSettingSwitch(
        id = "carpet_wire", name = "nearbycarpet_detecting_wire_status",
        title = "Avoid cords at carpet edges", description = "Better cord detection along carpet edges",
        bit = 105, section = FeatureSection.CARPET,
    ),
    BetaFeatureDef(
        id = "carpet_boost",
        title = "Carpet boost",
        description = "Maximum suction on carpets",
        bit = null,
        getMethod = "get_carpet_mode",
        setMethod = "set_carpet_mode",
        section = FeatureSection.CARPET,
        // The read answers [{"enable":1,"current_high":500,...}]; the official app writes [{"enable":n}].
        readField = "enable",
        setKey = "enable",
        wrapSet = true,
    ),
    robotSettingSwitch(
        id = "identify_room", name = "identify_room_status",
        title = "Room recognition", description = "Recognizes room types from what the camera sees",
        bit = 66, section = FeatureSection.OBSTACLE,
    ),
    robotSettingSwitch(
        id = "smart_wash", name = "smart_wash_status",
        title = "Smart mop washing", description = "Adapts water, time and temperature to how dirty the mop is",
        bit = 144, section = FeatureSection.DOCK,
    ),
    BetaFeatureDef(
        id = "offline_map",
        title = "Offline map",
        description = "Keeps showing where the robot was before it lost power",
        bit = null,
        getMethod = "get_offline_map_status",
        setMethod = "set_offline_map_status",
        section = FeatureSection.ROBOT,
        readField = "common_switch_status",
        readBit = 0,
        setKey = "status",
    ),
    cameraBitSwitch(
        id = "fill_light", bitIndex = 1,
        title = "Fill light", description = "Lights up dark spots so obstacles are recognised better",
        bit = null, section = FeatureSection.OBSTACLE,
        choices = listOf(1 to "Automatic", 0 to "Off"),
    ),
    cameraBitSwitch(
        id = "have_pets", bitIndex = 5,
        title = "I have pets", description = "Watches out for pet waste and cleans pet areas with more care",
        bit = null, section = FeatureSection.OBSTACLE,
    ),
    cameraBitSwitch(
        id = "pet_snaps", bitIndex = 12,
        title = "Pet Snaps", description = "Takes photos of your pets while cleaning",
        bit = 50, section = FeatureSection.OBSTACLE,
    ),
    BetaFeatureDef(
        id = "voice_assistant",
        title = "Voice assistant",
        description = "The robot listens for spoken commands; it processes your voice to do so",
        bit = 37,
        getMethod = "get_voice_service_switch",
        setMethod = "set_voice_service_switch",
        section = FeatureSection.ROBOT,
        setKey = "data",
    ),
    robotSettingSwitch(
        id = "product_experience", name = "product_experience_status",
        title = "Share usage data", description = "Lets the robot send usage data to Roborock to improve the product",
        bit = 121, section = FeatureSection.ROBOT,
    ),
    robotSettingSwitch(
        id = "voice_experience", name = "voice_experience_status",
        title = "Share voice data", description = "Lets the robot send voice recordings to Roborock to improve recognition",
        bit = 121, section = FeatureSection.ROBOT,
    ),
)

/** The features this robot's `newFeatureSet` advertises. */
fun supportedBetaFeatures(newFeatureSet: String?): List<BetaFeatureDef> =
    BetaFeatureDefs.filter { def ->
        (def.bit == null || newFeatureBit(newFeatureSet, def.bit)) &&
            (def.alsoRequiresBit == null || newFeatureBit(newFeatureSet, def.alsoRequiresBit)) &&
            (def.unlessBit == null || !newFeatureBit(newFeatureSet, def.unlessBit))
    }

/** The reads of [features] that belong on [section], in the order the definitions list them. */
fun List<BetaFeatureState>.inSection(section: FeatureSection): List<BetaFeatureState> =
    filter { it.def.section == section }

/**
 * Names for `newFeatureSet` bits from 32 up (python-roborock NewFeatureStrBit), for listing what a
 * robot advertises that LocalRock has no controls for. Bits the table above covers and bits the app
 * handles elsewhere are left out of that list.
 */
private val AdvertisedFeatureNames = mapOf(
    71 to "Map eraser",
    76 to "Cleaning history timeline",
    85 to "Return to dock midway",
    91 to "Cleaning timeline",
    100 to "Dust collection count",
    108 to "Cleaning efficiency",
    122 to "Floor plan map",
    136 to "Stair and door sill marks",
    138 to "Curtains on the map",
    147 to "Carpeted stairs",

    32 to "Two-key real-time video",
    33 to "Two-key video while charging",
    34 to "Dirty replenish clean",
    35 to "Auto delivery status",
    37 to "Voice control",
    38 to "New endpoint",
    39 to "Pumping water",
    41 to "Hot wash towel",
    42 to "Floor direction clean any time",
    45 to "Mop shake water max",
    47 to "Exact custom mode",
    48 to "Video patrol",
    49 to "Carpet custom clean",
    50 to "Pet snapshot",
    51 to "Custom clean mode count",
    52 to "New AI recognition",
    53 to "Auto collection 2",
    57 to "No carpet press setting",
    62 to "Min battery 15% to clean",
    64 to "Object detect check",
    66 to "Identify room",
    67 to "Matter",
    69 to "Workday and holiday",
    70 to "Clean direct status",
    72 to "Optimize battery",
    73 to "Video while charging and standby",
    75 to "Long-haired carpet",
    77 to "Max zones opened",
    78 to "Exhibition function",
    79 to "LDS lifting",
    80 to "Auto tear down mop",
    81 to "Small side mop",
    82 to "Side brush up and down",
    83 to "Dry interval timer",
    84 to "UVC sterilize",
    86 to "Main brush up and down",
    87 to "Egg dance mode",
    89 to "Mechanical arm mode",
    93 to "Clean then mop mode",
    94 to "Type identify",
    96 to "Get particular status",
    97 to "3D mapping inner test",
    98 to "Sync server name",
    99 to "Show arm overload",
    101 to "App stop grasp",
    102 to "Clean then mop with repeat",
    104 to "Side brush lift on carpet",
    105 to "Detect wire carpet",
    107 to "Soak and wash",
    109 to "Back wash new smart",
    110 to "Dual-band Wi-Fi",
    111 to "Program mode",
    113 to "Long-haired carpet EX",
    114 to "Overseas clean then mop",
    115 to "Full duplex switch",
    116 to "Low area access",
    117 to "Follow low obstacles",
    118 to "Two gears no collision",
    119 to "Carpet shape type",
    120 to "SR map",
    128 to "Roller mop",
)

/**
 * Bits that have their own place in the app, so they are not listed as missing (names from the
 * official a296 plugin's NewFeatureStrBit table).
 */
private val HandledElsewhereBits = setOf(
    55, 106, // SmartPlan, water slide (Vac & Mop sheet)
    35, 112, // AutoDelivery, CleanFluidDelivery: cleaning fluid (dock)
    41, // HotWashTowel: washing water temperature (dock)
    42, // FloorDirCleanAnyTime: cleaning direction (floor)
    47, 51, // ExactCustomMode, CustomCleanModeCount: per-room settings
    53, // AutoCollection2: auto-empty (dock)
    83, // DryIntervalTimer: mop drying at a set time (dock)
    93, 102, 114, // CleanThenMopMode, CtmWithRepeat, OverSeaCTM: vacuum first, then mop
    107, 109, // SoakAndWash, BackWashNewSmart: wash modes (dock)
    113, // CarpetLongHairedEx: adaptive lift (carpet)
    118, // TwoGearsNoCollision: collision mode (obstacles)
    140, // SupportMultipleDNDTimePeriods: Do Not Disturb
    32, 33, 73, // TwoKeyRealTimeVideo, TwoKeyRTVInCharging, ActivateVideoChargingAndStandby: camera
    100, 108, // CollectDustCountShow, CleanEfficiency: cleaning history
)

/**
 * Bits that tell the app how the robot behaves, with nothing to switch: the official app has no setting
 * for them either (it changes a label, a limit or a protocol detail). They are left out of the list.
 */
private val NoSettingBits = setOf(
    38, // NewEndpoint: which Roborock host serves resources
    52, // NewAIRecognition
    57, // NoNeedCarpetPressSet: no carpet pressure setting needed
    62, // MinBattery15ToCleanTask: starts a clean from 15 % battery
    64, // ObjectDetectCheck
    67, // Matter: pairing with a Matter hub, which needs Roborock's cloud
    77, // MaxZoneOpened: more zones per clean
    86, // SupportMainBrushUpDown: lifts the main brush by itself
    87, // EggDanceMode: a demo
    96, // SupportGetParticularStatus
    98, // SyncServerName
    99, // ShouldShowArmOverLoad: a message for robots with an arm
    101, // supportApiAppStopGrasp: robots with an arm
    110, // DualBandWiFi
    119, // CarpetShapeType: how carpets are drawn on the map
    143, // CancelOneTimeSweepThenMop
    148, // SupportWashPhasePumping
)

/** A `newFeatureSet` bit the robot advertises that has no switch here; [name] is null when python-roborock has none. */
data class AdvertisedFeature(val bit: Int, val name: String?)

/** What the robot advertises in `newFeatureSet` (bit 32 and up) that has no switch here, in bit order. */
fun unlistedAdvertisedFeatures(newFeatureSet: String?): List<AdvertisedFeature> {
    val s = newFeatureSet ?: return emptyList()
    val covered = BetaFeatureDefs.mapNotNull { it.bit }.toSet() + HandledElsewhereBits + NoSettingBits
    return (32 until s.length * 4)
        .filter { it !in covered && newFeatureBit(s, it) }
        .map { AdvertisedFeature(it, AdvertisedFeatureNames[it]) }
}

/** Last read of one feature. [key] is the field name in the reply, null when the reply was a bare number. */
data class BetaFeatureState(
    val def: BetaFeatureDef,
    val value: Int? = null,
    val key: String? = null,
    val raw: String? = null,
    val answered: Boolean = true,
) {
    val enabled: Boolean get() = value == 1

    /** True when [value] is one the page can show: 0/1 for a switch, one of [BetaFeatureDef.choices] for a picker. */
    val toggleable: Boolean
        get() = def.choices?.any { it.first == value } ?: (value == 0 || value == 1)

    /** The label of [value] for a picker, or null for a switch. */
    val choiceLabel: String? get() = def.choices?.firstOrNull { it.first == value }?.second
}

/** The params for reading [def]: `{"name": K}` for a robot setting, none otherwise. */
fun featureReadParams(def: BetaFeatureDef): JsonElement =
    def.robotSetting?.let { JsonObject(mapOf("name" to JsonPrimitive(it))) } ?: JsonArray(emptyList())

/**
 * The value of [def] in a reply, with the field name the setter takes. A [BetaFeatureDef.readField]
 * is read out of a larger answer (such as `get_status`), optionally one bit of it.
 */
fun readFeatureReply(def: BetaFeatureDef, result: JsonElement?): Pair<String?, Int>? {
    if (def.bitmaskWrite) {
        val raw = parseBetaReply(result)?.second ?: return null
        return null to ((raw shr requireNotNull(def.readBit)) and 1)
    }
    val field = def.readField ?: return parseBetaReply(result)
    val obj = (if (result is JsonArray) result.firstOrNull() else result) as? JsonObject ?: return null
    val raw = (obj[field] as? JsonPrimitive)?.intOrNull ?: return null
    val value = def.readBit?.let { (raw shr it) and 1 } ?: raw
    return def.setKey to value
}

/**
 * Param shapes to try for setting [def] to [value], in order. A robot setting has exactly one shape;
 * [BetaFeatureDef.wrapSet] commands take only the wrapped object; the rest try [betaSetParamCandidates].
 */
fun featureSetParamCandidates(def: BetaFeatureDef, replyKey: String?, value: Int): List<JsonElement> {
    def.robotSetting?.let { name ->
        return listOf(JsonObject(mapOf("name" to JsonPrimitive(name), "status" to JsonPrimitive(value))))
    }
    val key = def.setKey ?: replyKey
    if (def.wrapSet && key != null) return listOf(JsonArray(listOf(JsonObject(mapOf(key to JsonPrimitive(value))))))
    return betaSetParamCandidates(key, value)
}

/** Reads a reply that holds one number: `1`, `[1]`, `{"status":1}` or `[{"status":1}]`. */
fun parseBetaReply(result: JsonElement?): Pair<String?, Int>? {
    val element = if (result is JsonArray && result.size == 1) result[0] else result
    return when (element) {
        is JsonPrimitive -> element.intOrNull?.let { null to it }
        is JsonObject -> {
            val entry = element.entries.singleOrNull() ?: return null
            (entry.value as? JsonPrimitive)?.intOrNull?.let { entry.key to it }
        }
        else -> null
    }
}

/**
 * Param shapes to try for a set command, in order. The bare object comes first: that is what the
 * official app sends (captured on an a296, e.g. set_gap_deep_clean_status {"status":1}); the wrapped
 * form and the bare value follow for robots that refuse it.
 */
fun betaSetParamCandidates(key: String?, value: Int): List<JsonElement> {
    val v = JsonPrimitive(value)
    val obj = key?.let { JsonObject(mapOf(it to v)) }
    return listOfNotNull(
        obj,
        JsonArray(listOf(obj ?: v)),
        JsonArray(listOf(v)).takeIf { obj != null },
    )
}

fun betaSetParamCandidates(key: String?, on: Boolean): List<JsonElement> = betaSetParamCandidates(key, if (on) 1 else 0)

/**
 * Codes a robot answers to a param shape it does not take. Most options say -10007 "invalid params";
 * a Qrevo Curv 2 Flow (a245) says -10005 "Invalid parameters" to the wrapped set_identify_ground_material_status,
 * as a Qrevo Pro does to the wrapped set_child_lock_status.
 */
val BetaShapeRefusalCodes = setOf(-10007, -10005)

/** Indices into the [betaSetParamCandidates] list in the order to try them, the shape the robot last took first. */
fun betaSetTryOrder(count: Int, accepted: Int?): List<Int> {
    val all = (0 until count).toList()
    return if (accepted == null || accepted !in all) all else listOf(accepted) + all.filter { it != accepted }
}
