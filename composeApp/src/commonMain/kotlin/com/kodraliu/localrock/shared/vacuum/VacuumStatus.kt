package com.kodraliu.localrock.shared.vacuum

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import com.kodraliu.localrock.resources.*
import com.kodraliu.localrock.shared.text.UiText
import com.kodraliu.localrock.shared.text.rawText
import com.kodraliu.localrock.shared.text.uiText


@Serializable
data class VacuumStatus(
    val battery: Int? = null,
    val state: Int? = null,
    @SerialName("fan_power") val fanPower: Int? = null,
    @SerialName("clean_area") val cleanArea: Long? = null,
    @SerialName("clean_time") val cleanTime: Long? = null,
    @SerialName("error_code") val errorCode: Int? = null,
    @SerialName("water_box_status") val waterBoxStatus: Int? = null,

    @SerialName("water_box_mode") val waterBoxCustomMode: Int? = null,
    @SerialName("charge_status") val chargeStatus: Int? = null,
    @SerialName("dock_error_status") val dockErrorStatus: Int? = null,
    @SerialName("dock_type") val dockType: Int? = null,
    @SerialName("dss") val dockStatus: Int? = null,
    @SerialName("dry_status") val dryStatus: Int? = null,
    @SerialName("rdt") val remainingDryTimeSec: Long? = null,
    /** Loaded map in the upper bits (see [loadedMapFlag]). */
    @SerialName("map_status") val mapStatus: Int? = null,
    /**
     * Bit [LAB_STATUS_MAP_SAVING] set while the robot keeps maps, bit [LAB_STATUS_MULTI_LEVEL] while
     * multi-level maps are on (captured 3 and 1; 0 on a robot that never met the official app).
     */
    @SerialName("lab_status") val labStatus: Int? = null,
    /** 0 = recognise the floor automatically, 1 = pick the map by hand (captured). */
    @SerialName("switch_map_mode") val switchMapMode: Int? = null,
    /**
     * 0 once a clean job is over. Stays non-zero through the breaks inside one (recharging,
     * washing the mop), which is how the end of a job is told from a pause in it.
     */
    @SerialName("in_cleaning") val inCleaning: Int? = null,
    /** Why it is heading to the dock: 4 is a mid-clean return to empty the bin (a296); -1 none. */
    @SerialName("back_type") val backType: Int? = null,
    /** How far through the current job the robot says it is, 0..100. */
    @SerialName("clean_percent") val cleanPercent: Int? = null,
    /** 1 when the robot vacuums first and mops after (captured from the official app on an a296), 0 for together. */
    @SerialName("seq_type") val seqType: Int? = null,
    /** How many passes the next clean makes. */
    @SerialName("repeat") val repeat: Int? = null,
    /** Intensive corner mopping, 0/1; only robots with [isCornerCleanSupported] report it. */
    @SerialName("corner_clean_mode") val cornerCleanMode: Int? = null,
    /** The custom water flow as a distance ([WaterDistance]); 0 when none has been set. */
    @SerialName("distance_off") val distanceOff: Int? = null,
    /** Option bits on some models; an a296 reports them through get_offline_map_status instead (common_switch_status). */
    @SerialName("switch_status") val switchStatus: Int? = null,
) {
    val vacuumThenMop: Boolean? get() = seqType?.let { it == SEQ_TYPE_VACUUM_THEN_MOP }

    val multiLevelEnabled: Boolean? get() = labStatus?.let { it and LAB_STATUS_MULTI_LEVEL != 0 }

    /** False on a robot that will not store a map: it refuses manual_segment_map (issue #17). */
    val mapSavingEnabled: Boolean? get() = labStatus?.let { it and LAB_STATUS_MAP_SAVING != 0 }

    /**
     * mapFlag of the map the robot has loaded: map_status >> 2 (captured 2026-09-29: 7 with map 1
     * loaded, 11 with map 2), or [UNSAVED_MAP_FLAG] while a new map is being built and not saved.
     */
    val loadedMapFlag: Int? get() = mapStatus?.let { it shr 2 }

    /**
     * True while the robot holds a newly built map that has not been saved. Bit 0 of map_status
     * follows map_present: 253 after a mapping run, 252 on a robot with no map at all.
     */
    val unsavedMapPresent: Boolean
        get() = loadedMapFlag == UNSAVED_MAP_FLAG && mapStatus?.and(MAP_STATUS_PRESENT) != 0
}

const val SEQ_TYPE_TOGETHER = 0
const val SEQ_TYPE_VACUUM_THEN_MOP = 1
const val LAB_STATUS_MAP_SAVING = 1
const val LAB_STATUS_MULTI_LEVEL = 2
const val SWITCH_MAP_MODE_SMART = 0
const val SWITCH_MAP_MODE_MANUAL = 1

/** loadedMapFlag when no saved map is loaded: after a mapping run, or with no map at all (map_status 252-255). */
const val UNSAVED_MAP_FLAG = 63
/** map_status bit set while the robot holds any map (0 with map_present 0). */
const val MAP_STATUS_PRESENT = 1


object DockErrorCodes {
    const val OK = 0
    const val DUCT_BLOCKAGE = 34
    const val WATER_EMPTY = 38
    const val DIRTY_TANK_FULL = 39
    const val DIRTY_TANK_LATCH_OPEN = 44
    const val NO_DUSTBIN = 46
    const val CLEANING_TANK_FULL_OR_BLOCKED = 53

    fun describe(code: Int): String = text(code).english

    /** [describe] for the UI, in the app's language. */
    fun text(code: Int): UiText = when (code) {
        OK -> rawText("")
        DUCT_BLOCKAGE -> uiText(Res.string.dock_error_dock_dust_duct_blocked, "Dock dust duct blocked")
        WATER_EMPTY -> uiText(Res.string.dock_error_clean_water_tank_empty, "Clean water tank empty")
        DIRTY_TANK_FULL -> uiText(Res.string.dock_error_dirty_water_tank_full, "Dirty water tank full")
        DIRTY_TANK_LATCH_OPEN -> uiText(Res.string.dock_error_dirty_water_tank_latch_open, "Dirty water tank latch open")
        NO_DUSTBIN -> uiText(Res.string.dock_error_dock_dust_bag_missing, "Dock dust bag missing")
        CLEANING_TANK_FULL_OR_BLOCKED -> uiText(Res.string.dock_error_dock_cleaning_tray_full_or, "Dock cleaning tray full or blocked")
        else -> uiText(Res.string.dock_error_code, "Dock error $code", code)
    }
}


/**
 * A clean that is still open while the robot sits on the dock, e.g. after a mid-clean return to empty
 * the bin (a296: state 26, back_type 4, then 22, then 8 with in_cleaning still 1). The official app
 * shows it as paused, with Resume.
 */
/**
 * A clean is under way, also while the dock washes the mop before or during it (a296: state 23 with
 * in_cleaning 1). Not when it is paused, here or on the dock.
 */
val VacuumStatus.taskRunning: Boolean
    get() = (inCleaning ?: 0) != 0 && state != VacuumStateCodes.PAUSED && !taskPausedAtDock

val VacuumStatus.taskPausedAtDock: Boolean
    get() = (inCleaning ?: 0) != 0 &&
        state in setOf(VacuumStateCodes.CHARGING, VacuumStateCodes.CHARGING_COMPLETE, VacuumStateCodes.IDLE)

object VacuumStateCodes {
    const val STARTING = 1
    const val CHARGER_DISCONNECTED = 2
    const val IDLE = 3
    const val REMOTE_CONTROL_ACTIVE = 4
    const val CLEANING = 5
    const val RETURNING_HOME = 6
    const val MANUAL_MODE = 7
    const val CHARGING = 8
    const val CHARGING_ERROR = 9
    const val PAUSED = 10
    const val SPOT_CLEANING = 11
    const val ERROR = 12
    const val SHUTTING_DOWN = 13
    const val UPDATING = 14
    const val DOCKING = 15
    const val GOING_TO_TARGET = 16
    const val ZONED_CLEANING = 17
    const val SEGMENT_CLEANING = 18
    const val EMPTYING_BIN = 22
    const val NOT_CHARGING = 24
    const val WASHING_MOP = 23
    /** Also washing the mop (python-roborock washing_the_mop_2): an a296 reports it at the dock before and during a clean. */
    const val WASHING_MOP_2 = 25
    const val GOING_TO_WASH_MOP = 26
    const val DRYING_MOP = 27
    const val RETURNING_TO_DOCK_FOR_DRYING = 28
    /** Captured 2026-09-29: the robot reports 29 for the whole app_start_build_map run. */
    const val MAPPING = 29
    // From the a296 plugin's state list (2026-11-16). 33 was seen after a clean: the robot refits its
    // mop mounts in the dock (lock_status 1, then water_box_carriage_status 0 -> 1).
    const val CUPID_MODE = 30
    const val PATROL = 32
    const val REINSTALLING_MOP_MOUNTS = 33
    const val DETACHING_MOP_MOUNTS = 34
    const val EXHIBITION_MODE = 36
    const val DANCING = 37
    const val EMERGENCY_STOP = 40
    const val ARM_RESETTING = 41
    const val PROGRAM_MODE = 42
    const val CHANGING_MOP = 43
    const val CHARGING_COMPLETE = 100

    fun describe(state: Int): String = text(state).english

    /** [describe] for the UI, in the app's language. */
    fun text(state: Int): UiText = when (state) {
        STARTING -> uiText(Res.string.state_starting, "Starting")
        CHARGER_DISCONNECTED -> uiText(Res.string.state_charger_disconnected, "Charger disconnected")
        IDLE -> uiText(Res.string.state_idle, "Idle")
        REMOTE_CONTROL_ACTIVE -> uiText(Res.string.state_remote_control, "Remote control")
        CLEANING -> uiText(Res.string.state_cleaning, "Cleaning")
        RETURNING_HOME -> uiText(Res.string.state_returning_home, "Returning home")
        MANUAL_MODE -> uiText(Res.string.state_manual_mode, "Manual mode")
        CHARGING -> uiText(Res.string.state_charging, "Charging")
        CHARGING_ERROR -> uiText(Res.string.state_charging_error, "Charging error")
        PAUSED -> uiText(Res.string.state_paused, "Paused")
        SPOT_CLEANING -> uiText(Res.string.state_spot_cleaning, "Spot cleaning")
        ERROR -> uiText(Res.string.state_error, "Error")
        SHUTTING_DOWN -> uiText(Res.string.state_shutting_down, "Shutting down")
        UPDATING -> uiText(Res.string.state_updating, "Updating")
        DOCKING -> uiText(Res.string.state_docking, "Docking")
        GOING_TO_TARGET -> uiText(Res.string.state_going_to_target, "Going to target")
        ZONED_CLEANING -> uiText(Res.string.state_zoned_cleaning, "Zoned cleaning")
        SEGMENT_CLEANING -> uiText(Res.string.state_room_cleaning, "Room cleaning")
        MAPPING -> uiText(Res.string.state_mapping, "Mapping")
        NOT_CHARGING -> uiText(Res.string.state_idle, "Idle")
        WASHING_MOP, WASHING_MOP_2 -> uiText(Res.string.state_washing_mop, "Washing mop")
        GOING_TO_WASH_MOP -> uiText(Res.string.state_going_to_wash_mop, "Going to wash mop")
        DRYING_MOP -> uiText(Res.string.state_drying_mop, "Drying mop")
        RETURNING_TO_DOCK_FOR_DRYING -> uiText(Res.string.state_returning_to_dock, "Returning to dock")
        EMPTYING_BIN -> uiText(Res.string.state_emptying_the_bin, "Emptying the bin")
        CUPID_MODE -> uiText(Res.string.state_cupid_mode, "Cupid mode")
        PATROL -> uiText(Res.string.state_patrolling, "Patrolling")
        REINSTALLING_MOP_MOUNTS -> uiText(Res.string.state_reinstalling_mop_mounts, "Reinstalling mop mounts")
        DETACHING_MOP_MOUNTS -> uiText(Res.string.state_detaching_mop_mounts, "Detaching mop mounts")
        EXHIBITION_MODE -> uiText(Res.string.state_exhibition_mode, "Exhibition mode")
        DANCING -> uiText(Res.string.state_dancing, "Dancing")
        EMERGENCY_STOP -> uiText(Res.string.state_emergency_stop, "Emergency stop")
        ARM_RESETTING -> uiText(Res.string.state_resetting_the_arm, "Resetting the arm")
        PROGRAM_MODE -> uiText(Res.string.state_program_mode, "Program mode")
        CHANGING_MOP -> uiText(Res.string.state_changing_mop, "Changing mop")
        CHARGING_COMPLETE -> uiText(Res.string.state_fully_charged, "Fully charged")
        else -> uiText(Res.string.state_unknown, "State $state", state)
    }
}

object VacuumErrorCodes {
    fun describe(code: Int): String = text(code).english

    /** [describe] for the UI, in the app's language. */
    fun text(code: Int): UiText = when (code) {
        0 -> rawText("")
        1 -> uiText(Res.string.robot_error_laser_sensor_fault, "Laser sensor fault")
        2 -> uiText(Res.string.robot_error_collision_sensor_fault, "Collision sensor fault")
        3 -> uiText(Res.string.robot_error_wheel_floating, "Wheel floating")
        4 -> uiText(Res.string.robot_error_cliff_sensor_fault, "Cliff sensor fault")
        5 -> uiText(Res.string.robot_error_main_brush_blocked, "Main brush blocked")
        6 -> uiText(Res.string.robot_error_side_brush_blocked, "Side brush blocked")
        7 -> uiText(Res.string.robot_error_wheel_blocked, "Wheel blocked")
        8 -> uiText(Res.string.robot_error_robot_stuck, "Robot stuck")
        9 -> uiText(Res.string.robot_error_dustbin_missing, "Dustbin missing")
        10 -> uiText(Res.string.robot_error_filter_blocked, "Filter blocked")
        11 -> uiText(Res.string.robot_error_magnetic_field_detected, "Magnetic field detected")
        12 -> uiText(Res.string.robot_error_low_battery, "Low battery")
        13 -> uiText(Res.string.robot_error_charging_problem, "Charging problem")
        14 -> uiText(Res.string.robot_error_battery_failure, "Battery failure")
        15 -> uiText(Res.string.robot_error_wall_sensor_fault, "Wall sensor fault")
        16 -> uiText(Res.string.robot_error_uneven_surface, "Uneven surface")
        17 -> uiText(Res.string.robot_error_side_brush_failure, "Side brush failure")
        18 -> uiText(Res.string.robot_error_suction_fan_failure, "Suction fan failure")
        19 -> uiText(Res.string.robot_error_unpowered_dock, "Unpowered dock")
        21 -> uiText(Res.string.robot_error_laser_pressure_sensor_problem, "Laser pressure sensor problem")
        22 -> uiText(Res.string.robot_error_charge_sensor_problem, "Charge sensor problem")
        23 -> uiText(Res.string.robot_error_dock_problem, "Dock problem")
        24 -> uiText(Res.string.robot_error_no_go_zone_or_invisible, "No-go zone or invisible wall detected")
        25 -> uiText(Res.string.robot_error_mop_pad_dirty, "Mop pad dirty")
        26 -> uiText(Res.string.robot_error_mop_wash_tank_empty, "Mop wash tank empty")
        27 -> uiText(Res.string.robot_error_dirty_water_tank_full, "Dirty water tank full")
        28 -> uiText(Res.string.robot_error_mop_pad_not_installed, "Mop pad not installed")
        29 -> uiText(Res.string.robot_error_battery_overheating, "Battery overheating")
        30 -> uiText(Res.string.robot_error_mop_pad_blocked, "Mop pad blocked")
        31 -> uiText(Res.string.robot_error_mop_pad_not_installed, "Mop pad not installed")
        32 -> uiText(Res.string.robot_error_dustbin_not_installed, "Dustbin not installed")
        else -> uiText(Res.string.robot_error_code, "Error code $code", code)
    }
}


object VacuumFanPower {
    const val QUIET = 101
    const val BALANCED = 102
    const val TURBO = 103
    const val MAX = 104
    /** No suction: what the official app sets for its Mop tab (captured on an S8 Pro Ultra). */
    const val OFF = 105
    const val CUSTOM = 106
    const val SMART = 110
}


object WaterBoxMode {
    const val OFF = 200
    const val LOW = 201
    const val MEDIUM = 202
    const val HIGH = 203
    const val CUSTOM = 204
    const val SMART = 209
    const val SLIDE_MEDIUM = 235

    /** The custom water flow of robots with [isWaterDistanceSupported]; its amount is a [WaterDistance]. */
    const val USER_CUSTOM = 207

    /**
     * Water slide robots (new feature bit 106, e.g. a296) take any water flow from 221 to 250, the
     * official app's slider. Its marks: 225 low, 235 medium, 245 high; from 248 it calls the flow extra high.
     */
    val SLIDE_RANGE = 221..250
    const val SLIDE_LOW = 225
    const val SLIDE_HIGH = 245
    const val SLIDE_EXTRA_HIGH_FROM = 248

    /** A slide code as a 1-30 level, or null for a code outside the slide. */
    fun slideLevel(code: Int?): Int? = code?.takeIf { it in SLIDE_RANGE }?.let { it - SLIDE_RANGE.first + 1 }

    /** A slide code with its nearest mark, e.g. "12 · Medium"; null for a code outside the slide. */
    fun slideLabel(code: Int?): String? = slideText(code)?.english

    /** [slideLabel] for the UI, in the app's language. */
    fun slideText(code: Int?): UiText? {
        val level = slideLevel(code) ?: return null
        val band = when {
            code!! >= SLIDE_EXTRA_HIGH_FROM -> uiText(Res.string.water_extra_high, "Extra high")
            code >= (SLIDE_MEDIUM + SLIDE_HIGH) / 2 -> uiText(Res.string.water_high, "High")
            code >= (SLIDE_LOW + SLIDE_MEDIUM) / 2 -> uiText(Res.string.water_medium, "Medium")
            else -> uiText(Res.string.water_low, "Low")
        }
        return UiText.Joined(listOf(rawText("$level"), band))
    }

    /** Pure-water-flow robots (e.g. Saros 10R) report 201/202/203 back as 225/235/245. */
    fun normalize(code: Int?): Int? = when (code) {
        225 -> LOW
        SLIDE_MEDIUM -> MEDIUM
        245 -> HIGH
        else -> code
    }
}


/**
 * The custom water flow as `distance_off`, from the official a296 plugin: values from 60 to 205,
 * where a larger value gives less water, so the slider runs the other way. When the robot reports
 * none (0) the plugin starts at (60 + 205 + 5) / 2. Some models (spin mops) use 85 to 230; LocalRock
 * cannot tell them apart and keeps to the base range, which lies within it.
 */
object WaterDistance {
    val RANGE = 60..205
    const val DEFAULT = (60 + 205 + 5) / 2

    /** The distance to start from: the robot's own, or [DEFAULT] when it reports none. */
    fun initial(reported: Int?): Int = reported?.takeIf { it in RANGE } ?: DEFAULT

    /** The water level shown for a distance, 1 (least) to 146 (most). */
    fun level(distance: Int): Int = RANGE.last - distance.coerceIn(RANGE) + 1

    /** The distance for a shown level. */
    fun distanceFor(level: Int): Int = (RANGE.last + 1 - level).coerceIn(RANGE)
}

object MopRoute {
    const val STANDARD = 300
    const val DEEP = 301
    const val CUSTOM = 302
    const val DEEP_PLUS = 303
    const val FAST = 304
    const val SMART = 306
}
