package com.kodraliu.localrock.ui.vacuum

import com.kodraliu.localrock.ui.text.areaTenths
import com.kodraliu.localrock.ui.text.decimalSeparator
import com.kodraliu.localrock.resources.robot_action_dock
import com.kodraliu.localrock.resources.robot_action_history
import com.kodraliu.localrock.resources.robot_action_maps
import com.kodraliu.localrock.resources.robot_action_pin_go
import com.kodraliu.localrock.resources.robot_action_remote
import com.kodraliu.localrock.resources.robot_action_rooms
import com.kodraliu.localrock.resources.robot_action_routines
import com.kodraliu.localrock.resources.robot_action_schedule
import com.kodraliu.localrock.resources.robot_action_zone_clean
import com.kodraliu.localrock.resources.robot_add_area
import com.kodraliu.localrock.resources.robot_area_last_clean
import com.kodraliu.localrock.resources.robot_area_skipped
import com.kodraliu.localrock.resources.robot_area_so_far
import com.kodraliu.localrock.resources.robot_auto_empty_mode
import com.kodraliu.localrock.resources.robot_back
import com.kodraliu.localrock.resources.robot_back_up
import com.kodraliu.localrock.resources.robot_back_up_now
import com.kodraliu.localrock.resources.robot_backup_from
import com.kodraliu.localrock.resources.robot_backup_text
import com.kodraliu.localrock.resources.robot_backup_text_replace
import com.kodraliu.localrock.resources.robot_backup_title
import com.kodraliu.localrock.resources.robot_battery
import com.kodraliu.localrock.resources.robot_battery_charging
import com.kodraliu.localrock.resources.robot_checking_maps
import com.kodraliu.localrock.resources.robot_choose_map
import com.kodraliu.localrock.resources.robot_clean
import com.kodraliu.localrock.resources.robot_clean_rooms
import com.kodraliu.localrock.resources.robot_clean_selected_rooms
import com.kodraliu.localrock.resources.robot_clean_water_tank
import com.kodraliu.localrock.resources.robot_cleaning_count
import com.kodraliu.localrock.resources.robot_cleaning_resumed
import com.kodraliu.localrock.resources.robot_cleaning_rooms
import com.kodraliu.localrock.resources.robot_cleaning_started
import com.kodraliu.localrock.resources.robot_clear_selection
import com.kodraliu.localrock.resources.robot_close
import com.kodraliu.localrock.resources.robot_corner_text
import com.kodraliu.localrock.resources.robot_corner_title
import com.kodraliu.localrock.resources.robot_couldnt_load
import com.kodraliu.localrock.resources.robot_currently_loaded
import com.kodraliu.localrock.resources.robot_custom_amount
import com.kodraliu.localrock.resources.robot_default_order
import com.kodraliu.localrock.resources.robot_default_order_text
import com.kodraliu.localrock.resources.robot_delete_map
import com.kodraliu.localrock.resources.robot_delete_named
import com.kodraliu.localrock.resources.robot_delete_text
import com.kodraliu.localrock.resources.robot_delete_title
import com.kodraliu.localrock.resources.robot_dirty_water_tank
import com.kodraliu.localrock.resources.robot_dock_charge
import com.kodraliu.localrock.resources.robot_dock_control
import com.kodraliu.localrock.resources.robot_dock_empty
import com.kodraliu.localrock.resources.robot_dock_empty_action
import com.kodraliu.localrock.resources.robot_dock_settings
import com.kodraliu.localrock.resources.robot_dock_title
import com.kodraliu.localrock.resources.robot_dock_wash
import com.kodraliu.localrock.resources.robot_dock_wash_action
import com.kodraliu.localrock.resources.robot_dry
import com.kodraliu.localrock.resources.robot_drying
import com.kodraliu.localrock.resources.robot_drying_left
import com.kodraliu.localrock.resources.robot_drying_mop
import com.kodraliu.localrock.resources.robot_drying_mop_left
import com.kodraliu.localrock.resources.robot_duct_blocked
import com.kodraliu.localrock.resources.robot_dust_bag
import com.kodraliu.localrock.resources.robot_earlier
import com.kodraliu.localrock.resources.robot_edit_map
import com.kodraliu.localrock.resources.robot_empty_refill
import com.kodraliu.localrock.resources.robot_fan_max
import com.kodraliu.localrock.resources.robot_fan_quiet
import com.kodraliu.localrock.resources.robot_fan_standard
import com.kodraliu.localrock.resources.robot_fan_strong
import com.kodraliu.localrock.resources.robot_full_empty_it
import com.kodraliu.localrock.resources.robot_go_back
import com.kodraliu.localrock.resources.robot_hide_timeline
import com.kodraliu.localrock.resources.robot_keep_confirm
import com.kodraliu.localrock.resources.robot_keep_text
import com.kodraliu.localrock.resources.robot_keep_title
import com.kodraliu.localrock.resources.robot_last_clean_completed
import com.kodraliu.localrock.resources.robot_last_clean_ended_early
import com.kodraliu.localrock.resources.robot_latch_open
import com.kodraliu.localrock.resources.robot_left_hm
import com.kodraliu.localrock.resources.robot_left_m
import com.kodraliu.localrock.resources.robot_less_water
import com.kodraliu.localrock.resources.robot_live_view
import com.kodraliu.localrock.resources.robot_load
import com.kodraliu.localrock.resources.robot_load_map
import com.kodraliu.localrock.resources.robot_load_map_first
import com.kodraliu.localrock.resources.robot_loaded
import com.kodraliu.localrock.resources.robot_loading
import com.kodraliu.localrock.resources.robot_map_another_floor
import com.kodraliu.localrock.resources.robot_map_backed_up
import com.kodraliu.localrock.resources.robot_map_deleted
import com.kodraliu.localrock.resources.robot_map_name
import com.kodraliu.localrock.resources.robot_map_renamed
import com.kodraliu.localrock.resources.robot_map_restored
import com.kodraliu.localrock.resources.robot_map_saving_off
import com.kodraliu.localrock.resources.robot_map_saving_off_text
import com.kodraliu.localrock.resources.robot_map_saving_on
import com.kodraliu.localrock.resources.robot_map_the_home
import com.kodraliu.localrock.resources.robot_mapping_started
import com.kodraliu.localrock.resources.robot_maps_intro
import com.kodraliu.localrock.resources.robot_missing
import com.kodraliu.localrock.resources.robot_mode_mop
import com.kodraliu.localrock.resources.robot_mode_vac_mop
import com.kodraliu.localrock.resources.robot_mode_vacuum
import com.kodraliu.localrock.resources.robot_mop_dryer
import com.kodraliu.localrock.resources.robot_mop_wash_frequency
import com.kodraliu.localrock.resources.robot_more_actions
import com.kodraliu.localrock.resources.robot_more_cleaning_settings
import com.kodraliu.localrock.resources.robot_more_water
import com.kodraliu.localrock.resources.robot_move_down
import com.kodraliu.localrock.resources.robot_move_up
import com.kodraliu.localrock.resources.robot_multi_level
import com.kodraliu.localrock.resources.robot_multi_level_off
import com.kodraliu.localrock.resources.robot_multi_level_on
import com.kodraliu.localrock.resources.robot_multi_level_text
import com.kodraliu.localrock.resources.robot_new_map_saved
import com.kodraliu.localrock.resources.robot_new_map_unsaved
import com.kodraliu.localrock.resources.robot_new_map_unsaved_text
import com.kodraliu.localrock.resources.robot_new_routine_name
import com.kodraliu.localrock.resources.robot_no_backup
import com.kodraliu.localrock.resources.robot_no_cleaning_yet
import com.kodraliu.localrock.resources.robot_no_go_drop
import com.kodraliu.localrock.resources.robot_no_map
import com.kodraliu.localrock.resources.robot_no_map_text
import com.kodraliu.localrock.resources.robot_no_saved_maps
import com.kodraliu.localrock.resources.robot_no_saved_maps_text
import com.kodraliu.localrock.resources.robot_off
import com.kodraliu.localrock.resources.robot_ok
import com.kodraliu.localrock.resources.robot_once
import com.kodraliu.localrock.resources.robot_other
import com.kodraliu.localrock.resources.robot_page_vac_then_mop
import com.kodraliu.localrock.resources.robot_pause
import com.kodraliu.localrock.resources.robot_paused_at_dock
import com.kodraliu.localrock.resources.robot_plan_custom
import com.kodraliu.localrock.resources.robot_plan_general
import com.kodraliu.localrock.resources.robot_plan_smart
import com.kodraliu.localrock.resources.robot_preview_backup
import com.kodraliu.localrock.resources.robot_preview_map
import com.kodraliu.localrock.resources.robot_reading_room_settings
import com.kodraliu.localrock.resources.robot_reading_timeline
import com.kodraliu.localrock.resources.robot_recheck
import com.kodraliu.localrock.resources.robot_refresh_map
import com.kodraliu.localrock.resources.robot_remove_order
import com.kodraliu.localrock.resources.robot_rename
import com.kodraliu.localrock.resources.robot_rename_map
import com.kodraliu.localrock.resources.robot_rename_named
import com.kodraliu.localrock.resources.robot_restore
import com.kodraliu.localrock.resources.robot_restore_backup
import com.kodraliu.localrock.resources.robot_restore_text
import com.kodraliu.localrock.resources.robot_restore_title
import com.kodraliu.localrock.resources.robot_resume
import com.kodraliu.localrock.resources.robot_return_to_dock
import com.kodraliu.localrock.resources.robot_returning_to_dock
import com.kodraliu.localrock.resources.robot_returning_to_empty
import com.kodraliu.localrock.resources.robot_returning_to_wash
import com.kodraliu.localrock.resources.robot_room_fallback
import com.kodraliu.localrock.resources.robot_room_generic
import com.kodraliu.localrock.resources.robot_room_modes_load_map
import com.kodraliu.localrock.resources.robot_room_modes_text
import com.kodraliu.localrock.resources.robot_room_settings_failed
import com.kodraliu.localrock.resources.robot_room_settings_saved
import com.kodraliu.localrock.resources.robot_rooms_selected
import com.kodraliu.localrock.resources.robot_rooms_selected_hint
import com.kodraliu.localrock.resources.robot_route
import com.kodraliu.localrock.resources.robot_route_deep
import com.kodraliu.localrock.resources.robot_route_deep_plus
import com.kodraliu.localrock.resources.robot_route_fast
import com.kodraliu.localrock.resources.robot_route_standard
import com.kodraliu.localrock.resources.robot_save_new_map
import com.kodraliu.localrock.resources.robot_save_order
import com.kodraliu.localrock.resources.robot_save_room_settings
import com.kodraliu.localrock.resources.robot_saved_maps
import com.kodraliu.localrock.resources.robot_select_rooms
import com.kodraliu.localrock.resources.robot_settings
import com.kodraliu.localrock.resources.robot_show_timeline
import com.kodraliu.localrock.resources.robot_skip
import com.kodraliu.localrock.resources.robot_skip_area
import com.kodraliu.localrock.resources.robot_skip_text
import com.kodraliu.localrock.resources.robot_skip_title
import com.kodraliu.localrock.resources.robot_smart_floor
import com.kodraliu.localrock.resources.robot_smart_floor_text
import com.kodraliu.localrock.resources.robot_smartplan_text
import com.kodraliu.localrock.resources.robot_spot_clean
import com.kodraliu.localrock.resources.robot_start_cleans_selected
import com.kodraliu.localrock.resources.robot_stop_cleaning
import com.kodraliu.localrock.resources.robot_stop_dry
import com.kodraliu.localrock.resources.robot_subtitle_mop
import com.kodraliu.localrock.resources.robot_subtitle_vac_mop
import com.kodraliu.localrock.resources.robot_subtitle_vac_then_mop
import com.kodraliu.localrock.resources.robot_subtitle_vacuum
import com.kodraliu.localrock.resources.robot_suction_power
import com.kodraliu.localrock.resources.robot_summary_mop
import com.kodraliu.localrock.resources.robot_summary_vac_mop
import com.kodraliu.localrock.resources.robot_summary_vacuum
import com.kodraliu.localrock.resources.robot_switch_map
import com.kodraliu.localrock.resources.robot_tab_full
import com.kodraliu.localrock.resources.robot_tab_room
import com.kodraliu.localrock.resources.robot_tab_routine
import com.kodraliu.localrock.resources.robot_tab_zones
import com.kodraliu.localrock.resources.robot_tap_rooms
import com.kodraliu.localrock.resources.robot_tap_rooms_hint
import com.kodraliu.localrock.resources.robot_turn_on_map_saving
import com.kodraliu.localrock.resources.robot_twice
import com.kodraliu.localrock.resources.robot_unknown
import com.kodraliu.localrock.resources.robot_vac_then_mop_text
import com.kodraliu.localrock.resources.robot_vac_then_mop_title
import com.kodraliu.localrock.resources.robot_washing_mode
import com.kodraliu.localrock.resources.robot_water_custom
import com.kodraliu.localrock.resources.robot_water_flow
import com.kodraliu.localrock.resources.robot_water_high
import com.kodraliu.localrock.resources.robot_water_low
import com.kodraliu.localrock.resources.robot_water_med
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.StringResource
import com.kodraliu.localrock.resources.routines_add
import com.kodraliu.localrock.resources.routines_added
import com.kodraliu.localrock.resources.routines_recommended
import com.kodraliu.localrock.resources.routines_run
import com.kodraliu.localrock.resources.routines_edit
import com.kodraliu.localrock.resources.routines_new
import com.kodraliu.localrock.resources.routines_empty
import com.kodraliu.localrock.resources.common_try_again
import com.kodraliu.localrock.resources.routines_intro
import com.kodraliu.localrock.resources.routines_title
import com.kodraliu.localrock.resources.common_cancel
import com.kodraliu.localrock.resources.common_delete
import com.kodraliu.localrock.resources.routines_delete_text
import com.kodraliu.localrock.resources.routines_delete_title
import com.kodraliu.localrock.shared.vacuum.RecommendedRoutine
import org.jetbrains.compose.resources.stringResource
import com.kodraliu.localrock.resources.Res
import androidx.compose.material.icons.filled.Add
import com.kodraliu.localrock.shared.vacuum.taskRunning
import com.kodraliu.localrock.shared.vacuum.CleanRecord
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.AddBox
import com.kodraliu.localrock.shared.vacuum.taskPausedAtDock
import androidx.compose.runtime.snapshotFlow
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.material3.SecondaryScrollableTabRow
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.material.icons.outlined.WaterDrop
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.material.icons.filled.Cyclone
import com.kodraliu.localrock.shared.vacuum.CleanTimelineEntry
import com.kodraliu.localrock.shared.vacuum.currentCleanRoom
import com.kodraliu.localrock.shared.vacuum.describeCleanEvents
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.BatteryUnknown
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.PlaylistPlay
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.EditLocationAlt
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Battery1Bar
import androidx.compose.material.icons.filled.Battery2Bar
import androidx.compose.material.icons.filled.Battery3Bar
import androidx.compose.material.icons.filled.Battery4Bar
import androidx.compose.material.icons.filled.Battery5Bar
import androidx.compose.material.icons.filled.Battery6Bar
import androidx.compose.material.icons.filled.BatteryChargingFull
import androidx.compose.material.icons.filled.BatteryFull
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Dock
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Gamepad
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.SelectAll
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.material3.Slider
import kotlin.math.roundToInt
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kodraliu.localrock.shared.model.Scene
import com.kodraliu.localrock.shared.vacuum.WaterDistance
import com.kodraliu.localrock.shared.vacuum.DockCapabilities
import com.kodraliu.localrock.shared.vacuum.DockErrorCodes
import com.kodraliu.localrock.shared.vacuum.DockSettings
import com.kodraliu.localrock.shared.vacuum.FloorMap
import com.kodraliu.localrock.shared.vacuum.MopRoute
import com.kodraliu.localrock.shared.vacuum.RoomCleanMode
import com.kodraliu.localrock.shared.vacuum.SWITCH_MAP_MODE_SMART
import com.kodraliu.localrock.shared.vacuum.VacuumErrorCodes
import com.kodraliu.localrock.shared.vacuum.VacuumFanPower
import com.kodraliu.localrock.shared.vacuum.VacuumRepository
import com.kodraliu.localrock.shared.vacuum.VacuumStateCodes
import com.kodraliu.localrock.shared.vacuum.VacuumStatus
import com.kodraliu.localrock.shared.vacuum.WaterBoxMode
import com.kodraliu.localrock.shared.vacuum.isCustomizeModeSet
import com.kodraliu.localrock.shared.vacuum.isSmartModeSet
import com.kodraliu.localrock.shared.vacuum.mapNameBlocker
import com.kodraliu.localrock.shared.vacuum.roomCleanModesFor
import com.kodraliu.localrock.shared.vacuum.map.ParsedMap
import com.kodraliu.localrock.shared.vacuum.map.ParsedMapRoom
import com.kodraliu.localrock.shared.vacuum.map.MapObstacle
import com.kodraliu.localrock.shared.vacuum.map.roomAtNorm
import com.kodraliu.localrock.ui.AppColors
import com.kodraliu.localrock.ui.adaptive.LocalWindowLayout
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.kodraliu.localrock.shared.vacuum.washFrequencyText
import com.kodraliu.localrock.ui.text.text


private val FAN_OPTIONS = listOf(
    VacuumFanPower.QUIET to Res.string.robot_fan_quiet,
    VacuumFanPower.BALANCED to Res.string.robot_fan_standard,
    VacuumFanPower.TURBO to Res.string.robot_fan_strong,
    VacuumFanPower.MAX to Res.string.robot_fan_max,
)

/** A room can also go without suction, the official app's mop-only setting. */
private val ROOM_FAN_OPTIONS = listOf(VacuumFanPower.OFF to Res.string.robot_off) + FAN_OPTIONS

private val WATER_OPTIONS = listOf(
    WaterBoxMode.OFF to Res.string.robot_off,
    WaterBoxMode.LOW to Res.string.robot_water_low,
    WaterBoxMode.MEDIUM to Res.string.robot_water_med,
    WaterBoxMode.HIGH to Res.string.robot_water_high,
)

private val ROUTE_OPTIONS = listOf(
    MopRoute.STANDARD to Res.string.robot_route_standard,
    MopRoute.DEEP to Res.string.robot_route_deep,
    MopRoute.DEEP_PLUS to Res.string.robot_route_deep_plus,
    MopRoute.FAST to Res.string.robot_route_fast,
)

private val DEEP_ROUTES = setOf(MopRoute.DEEP, MopRoute.DEEP_PLUS)

/** The route buttons for a robot: the ones it offers, and the one it has now even if not offered. */
private fun routeChoices(offered: Set<Int>?, current: Int?): List<Pair<Int, StringResource>> =
    ROUTE_OPTIONS.filter { offered == null || it.first in offered || it.first == current }

internal val ACTIVE_CLEANING_STATES = setOf(
    VacuumStateCodes.CLEANING, VacuumStateCodes.SEGMENT_CLEANING,
    VacuumStateCodes.ZONED_CLEANING, VacuumStateCodes.SPOT_CLEANING,
)


@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun VacuumDetailScreen(
    viewModel: VacuumViewModel,
    onBack: () -> Unit,
    onSettings: () -> Unit,
    onZoneClean: () -> Unit,
    onAddArea: () -> Unit = {},
    onMapEdit: () -> Unit,
    onSchedule: () -> Unit,
    onCamera: () -> Unit,
    onRemote: () -> Unit,
    onPinGo: () -> Unit,
    onHistory: () -> Unit,
    onEditRoutine: () -> Unit = {},
) {
    val status by viewModel.status.collectAsState()
    val busy by viewModel.busy.collectAsState()
    val supportsSmartPlan by viewModel.supportsSmartPlan.collectAsState()
    val supportsWaterSlide by viewModel.supportsWaterSlide.collectAsState()
    val routeOptions by viewModel.routeOptions.collectAsState()
    val supportsVacuumThenMop by viewModel.supportsVacuumThenMop.collectAsState()
    val supportsCornerClean by viewModel.supportsCornerClean.collectAsState()
    val supportsWaterDistance by viewModel.supportsWaterDistance.collectAsState()
    val parsedMap by viewModel.repository.parsedMap.collectAsState()
    val floorMaps by viewModel.repository.floorMaps.collectAsState()
    val currentFloorFlag by viewModel.repository.currentFloorFlag.collectAsState()
    val maxFloorMaps by viewModel.repository.maxFloorMaps.collectAsState()
    val unsavedMap by viewModel.repository.unsavedMapPresent.collectAsState()
    val mopMode by viewModel.repository.mopMode.collectAsState()
    val roomCleanModes by viewModel.repository.roomCleanModes.collectAsState()
    val dockSettings by viewModel.repository.dockSettings.collectAsState()
    var selectedRoomIds by remember { mutableStateOf(emptyList<Int>()) }

    var sessionRoomIds by remember { mutableStateOf(emptyList<Int>()) }
    var panel by remember { mutableStateOf<DetailPanel?>(null) }
    val requestedPanel by viewModel.requestedPanel.collectAsState()
    LaunchedEffect(requestedPanel) {
        requestedPanel?.let { panel = it; viewModel.requestPanel(null) }
    }
    var openObstacle by remember { mutableStateOf<MapObstacle?>(null) }
    val twoPane = LocalWindowLayout.current.twoPane
    BackHandler(enabled = twoPane && sidePaneShowsPanel(panel)) { panel = null }
    val routines by viewModel.routines.collectAsState()
    var recoverLoading by remember { mutableStateOf(false) }
    var recoverTick by remember { mutableIntStateOf(0) }
    var renamingMap by remember { mutableStateOf<FloorMap?>(null) }
    var deletingMap by remember { mutableStateOf<FloorMap?>(null) }
    var backingUpMap by remember { mutableStateOf<FloorMap?>(null) }
    var backingUpPicture by remember { mutableStateOf<ParsedMap?>(null) }
    var restoringMap by remember { mutableStateOf<FloorMap?>(null) }
    var restoringPicture by remember { mutableStateOf<ParsedMap?>(null) }
    var choosingMapToKeep by remember { mutableStateOf(false) }
    var lastCleanArea by remember { mutableStateOf<Long?>(null) }
    var cleaningCount by remember { mutableIntStateOf(1) }
    // The robot keeps its own count (get_status repeat); follow it when it reports one.
    LaunchedEffect(status.repeat) { status.repeat?.takeIf { it in 1..2 }?.let { cleaningCount = it } }
    // The map's pan/zoom lives here so it survives the compact ↔ two-pane switch.
    val mapViewport = remember { LiveMapViewport() }


    val sessionRooms = remember(sessionRoomIds, status.state, parsedMap) {
        if (status.state == VacuumStateCodes.SEGMENT_CLEANING && sessionRoomIds.isNotEmpty()) {
            val byId = parsedMap?.rooms?.associateBy { it.id } ?: emptyMap()
            sessionRoomIds.map { id -> id to byId[id]?.name }
        } else {
            emptyList()
        }
    }
    val sessionRoomNames = sessionRooms.map { (id, name) -> name ?: stringResource(Res.string.robot_room_fallback, id) }

    LaunchedEffect(status.cleanArea) {
        if ((status.cleanArea ?: 0L) > 0L) lastCleanArea = status.cleanArea
    }
    // The first request can go out before the robot link is up, so retry until the list arrives.
    // map_status and lab_status change when a map is saved, deleted or switched (also from the
    // official app), so the list is read again then and the floor picker stays current.
    LaunchedEffect(status.mapStatus, status.labStatus) {
        do {
            runCatching { viewModel.repository.fetchFloorMaps() }
            if (!viewModel.repository.floorMapsLoaded.value) delay(MAP_LIST_RETRY_MS)
        } while (!viewModel.repository.floorMapsLoaded.value)
    }
    LaunchedEffect(panel == DetailPanel.CleaningMode) {
        if (panel == DetailPanel.CleaningMode) runCatching { viewModel.repository.fetchMopMode() }
    }
    val perRoomMode = isCustomizeModeSet(status.fanPower, status.waterBoxCustomMode, mopMode)
    // Null while the robot's room settings are being read, false when that failed. Saving is only
    // offered once they are known, or the defaults shown meanwhile would overwrite them.
    var roomModesLoaded by remember { mutableStateOf<Boolean?>(null) }
    LaunchedEffect(panel == DetailPanel.CleaningMode, perRoomMode) {
        roomModesLoaded = null
        if (panel == DetailPanel.CleaningMode && perRoomMode) {
            roomModesLoaded = runCatching { viewModel.repository.fetchRoomCleanModes() }.isSuccess
        }
    }
    LaunchedEffect(panel == DetailPanel.Dock) {
        if (panel == DetailPanel.Dock) runCatching { viewModel.repository.fetchDockSettings() }
    }
    LaunchedEffect(panel == DetailPanel.Routines) {
        if (panel == DetailPanel.Routines) viewModel.loadRoutines()
    }

    LaunchedEffect(panel == DetailPanel.Maps, recoverTick) {
        if (panel == DetailPanel.Maps) {
            recoverLoading = true
            runCatching { viewModel.repository.fetchFloorMaps() }
            recoverLoading = false
        }
    }

    val onMoreAction: (MoreAction) -> Unit = { action ->
        when (action) {
            MoreAction.ROOMS -> panel = DetailPanel.Rooms
            MoreAction.DOCK -> panel = DetailPanel.Dock
            MoreAction.MAPS -> panel = DetailPanel.Maps
            MoreAction.ROUTINES -> panel = DetailPanel.Routines
            MoreAction.ZONES -> { panel = null; onZoneClean() }
            MoreAction.PIN_GO -> { panel = null; onPinGo() }
            MoreAction.REMOTE -> { panel = null; onRemote() }
            MoreAction.SCHEDULE -> { panel = null; onSchedule() }
            MoreAction.CAMERA -> { panel = null; onCamera() }
            MoreAction.HISTORY -> { panel = null; onHistory() }
            MoreAction.EDIT_MAP -> { panel = null; onMapEdit() }
        }
    }

    val onLoadMap: () -> Unit = { viewModel.refreshMap() }
    val onRecover: () -> Unit = { panel = DetailPanel.Maps }
    val onRoomTap: (Int) -> Unit = { roomId ->
        selectedRoomIds = if (roomId in selectedRoomIds)
            selectedRoomIds.filter { it != roomId }
        else selectedRoomIds + roomId
    }
    val onFloorSwitch: (Int) -> Unit = { flag -> viewModel.run { it.switchFloor(flag) } }
    val newRoutineName = stringResource(
        Res.string.robot_new_routine_name,
        ((routines as? RoutinesState.Loaded)?.routines?.size ?: 0) + 1,
    )

    val PanelBody: @Composable (DetailPanel) -> Unit = { currentPanel ->
        when (currentPanel) {
            DetailPanel.More -> MoreActionsSheetContent(onAction = onMoreAction)

            DetailPanel.Routines -> RoutinesSheetContent(
                state = routines,
                onNew = {
                    viewModel.newRoutine(newRoutineName)
                    panel = null
                    onEditRoutine()
                },
                onEdit = { scene ->
                    if (viewModel.editRoutine(scene)) {
                        panel = null
                        onEditRoutine()
                    } else {
                        viewModel.routineNotEditable()
                    }
                },
                onDelete = { scene -> viewModel.deleteRoutine(scene) },
                onAddRecommended = { recommended, name, added ->
                    if (viewModel.addRecommendedRoutine(recommended, name, added)) {
                        panel = null
                        onEditRoutine()
                    }
                },
                busy = busy,
                onRun = { scene ->
                    panel = null
                    viewModel.runRoutine(scene)
                },
                onRetry = { viewModel.loadRoutines() },
            )

            DetailPanel.Rooms -> RoomsSheetContent(
                rooms = parsedMap?.rooms ?: emptyList(),
                selectedRoomIds = selectedRoomIds,
                busy = busy,
                onRoomToggle = { roomId ->
                    selectedRoomIds = if (roomId in selectedRoomIds)
                        selectedRoomIds.filter { it != roomId }
                    else selectedRoomIds + roomId
                },
                onCleanRooms = {
                    val toClean = selectedRoomIds
                    sessionRoomIds = toClean
                    selectedRoomIds = emptyList()
                    panel = null
                    viewModel.run { it.cleanRooms(toClean, cleaningCount) }
                },
                onLoadMap = {
                    panel = null
                    viewModel.refreshMap()
                },
                onDefaultOrder = {
                    panel = DetailPanel.Order
                },
            )

            DetailPanel.Order -> CleanOrderSheetContent(
                rooms = parsedMap?.rooms ?: emptyList(),
                savedOrder = remember(currentFloorFlag) { viewModel.defaultCleanOrder() },
                busy = busy,
                onSave = { order ->
                    panel = null
                    viewModel.saveDefaultCleanOrder(order)
                },
                onRemove = {
                    panel = null
                    viewModel.removeDefaultCleanOrder()
                },
            )

            DetailPanel.Dock -> DockSheetContent(
                status = status,
                busy = busy,
                dockSettings = dockSettings,
                dockCapabilities = viewModel.dockCapabilities,
                onEmptyDustbin = { panel = null; viewModel.run { it.collectDust() } },
                onWashMop = { panel = null; viewModel.run { it.washMop() } },
                // Dry keeps the sheet open so the drying status row appears in place.
                onDryMop = { viewModel.run { it.startDryer() } },
                onStopDry = { viewModel.run { it.stopDryer() } },
                onWashModeChange = { mode -> viewModel.run { repo -> repo.setWashMode(mode) } },
                onWashFrequencyChange = { mode, minutes -> viewModel.run { repo -> repo.setWashFrequency(mode, minutes) } },
                onAutoEmptyModeChange = { mode -> viewModel.run { repo -> repo.setAutoEmptyMode(mode) } },
            )

            DetailPanel.CleaningMode -> {
                // A refusal is shown in the sheet; the screen's own message would be behind it.
                var modeError by remember { mutableStateOf<String?>(null) }
                fun runMode(successMessage: StringResource? = null, action: suspend (VacuumRepository) -> Unit) {
                    modeError = null
                    viewModel.run(successMessage, onError = { modeError = it }, action = action)
                }
                CleaningModeSheetContent(
                    onMoreSettings = { panel = null; onSettings() },
                    currentFanPower = status.fanPower,
                    currentWaterMode = status.waterBoxCustomMode,
                    currentRoute = mopMode,
                    cleaningCount = cleaningCount,
                    waterSlide = supportsWaterSlide,
                    routeOptions = routeOptions,
                    busy = busy,
                    supportsSmartPlan = supportsSmartPlan,
                    rooms = parsedMap?.rooms ?: emptyList(),
                    roomCleanModes = roomCleanModes,
                    roomModesLoaded = roomModesLoaded,
                    error = modeError,
                    onPlanSelected = { plan ->
                        runMode {
                            when (plan) {
                                CleanPlan.SMART -> it.setCleanMotorMode(VacuumFanPower.SMART, WaterBoxMode.SMART, MopRoute.SMART)
                                CleanPlan.MANUAL -> {
                                    // Water slide robots get a slide code, as python-roborock does.
                                    val water = if (supportsWaterSlide) WaterBoxMode.SLIDE_MEDIUM else WaterBoxMode.LOW
                                    it.setCleanMotorMode(VacuumFanPower.BALANCED, water, MopRoute.STANDARD)
                                }
                                // The three codes the official app sent for its Customize tab.
                                CleanPlan.PER_ROOM -> it.setCleanMotorMode(VacuumFanPower.CUSTOM, WaterBoxMode.CUSTOM, MopRoute.CUSTOM)
                            }
                        }
                    },
                    onSaveRoomModes = { modes -> runMode(Res.string.robot_room_settings_saved) { it.saveRoomCleanModes(modes) } },
                    onFanPowerSelected = { level -> runMode { it.setFanPower(level) } },
                    onWaterModeSelected = { level -> runMode { it.setWaterBoxMode(level) } },
                    onRouteSelected = { mode -> runMode { it.setMopMode(mode) } },
                    onCountChange = { count ->
                        cleaningCount = count
                        // Saved on the robot as the official app does, so a full clean uses it too.
                        if (status.repeat != null && count != status.repeat) runMode { it.setCleanRepeat(count) }
                    },
                    vacuumThenMop = status.vacuumThenMop.takeIf { supportsVacuumThenMop },
                    cornerClean = status.cornerCleanMode?.let { it == 1 }?.takeIf { supportsCornerClean },
                    onCornerCleanChange = { on -> runMode { it.setCornerClean(on) } },
                    waterDistance = WaterDistance.initial(status.distanceOff).takeIf { supportsWaterDistance },
                    onWaterDistanceChange = { distance -> runMode { it.setWaterDistance(distance) } },
                    onVacuumThenMopChange = { on -> runMode { it.setVacuumThenMop(on, cleaningCount) } },
                )
            }

            DetailPanel.Maps -> MapsSheetContent(
                floorMaps = floorMaps,
                currentFloorFlag = currentFloorFlag,
                loading = recoverLoading,
                busy = busy,
                blockedReason = viewModel.repository.mapManageBlockedReason(status.state)?.text(),
                canAddMap = floorMaps.size < (maxFloorMaps ?: 1),
                unsavedMap = unsavedMap,
                mapSaving = status.mapSavingEnabled,
                multiLevel = status.multiLevelEnabled,
                smartSwitching = status.switchMapMode?.let { it == SWITCH_MAP_MODE_SMART },
                onMultiLevelChange = { on ->
                    when {
                        on -> viewModel.run(Res.string.robot_multi_level_on) { it.setMultiLevel(true, null) }
                        floorMaps.size > 1 -> choosingMapToKeep = true
                        else -> viewModel.run(Res.string.robot_multi_level_off) { it.setMultiLevel(false, null) }
                    }
                },
                onSmartSwitchingChange = { smart -> viewModel.run { it.setSmartMapSwitching(smart) } },
                onEnableMapSaving = { viewModel.run(Res.string.robot_map_saving_on) { it.enableMapSaving() } },
                onLoad = { flag ->
                    panel = null
                    viewModel.run { it.switchFloor(flag) }
                },
                onRename = { renamingMap = it },
                onDelete = { deletingMap = it },
                onBackup = { floor, picture ->
                    backingUpPicture = picture
                    backingUpMap = floor
                },
                onRestore = { floor, picture ->
                    restoringPicture = picture
                    restoringMap = floor
                },
                loadSavedMap = { flag, backup -> viewModel.repository.fetchSavedMap(flag, backup) },
                onStartMapping = {
                    panel = null
                    viewModel.run(Res.string.robot_mapping_started) { it.startMapping() }
                },
                onSaveNewMap = { viewModel.run(Res.string.robot_new_map_saved) { it.saveNewMap() } },
                onRecheck = { recoverTick++ },
            )
        }
    }

    // The running clean's events, read as the official app does while its card is open (and now and
    // then otherwise, for the room name on the card).
    val isPausedNow = status.state == VacuumStateCodes.PAUSED || status.taskPausedAtDock
    var confirmSkip by remember { mutableStateOf(false) }
    var dockChoice by remember { mutableStateOf(false) }
    var lastBubbleOpen by remember { mutableStateOf(true) }
    val cleanHistory by viewModel.repository.cleanHistory.collectAsState()
    val lastCleanForBubble = remember(cleanHistory) { cleanHistory.filter { !it.isPatrol }.maxByOrNull { it.beginEpoch } }
    LaunchedEffect(Unit) { runCatching { viewModel.repository.fetchCleanHistory() } }
    val liveEvents by viewModel.repository.liveCleanEvents.collectAsState()
    var statusExpanded by remember { mutableStateOf(false) }
    val cleaningNow = status.state in ACTIVE_CLEANING_STATES || status.taskRunning
    LaunchedEffect(cleaningNow, statusExpanded) {
        while (cleaningNow) {
            runCatching { viewModel.repository.refreshLiveCleanEvents() }
            delay(if (statusExpanded) 5_000 else 15_000)
        }
    }
    val roomNameOf: (Int) -> String? = { id -> parsedMap?.rooms?.firstOrNull { it.id == id }?.name?.takeIf { it.isNotBlank() } }
    val liveTimeline = remember(liveEvents, parsedMap) { if (cleaningNow) describeCleanEvents(liveEvents, roomNameOf) else emptyList() }
    val liveRoom = remember(liveEvents, parsedMap) { if (cleaningNow) currentCleanRoom(liveEvents)?.let(roomNameOf) else null }

    // Status + controls live in one band so the phone and two-pane layouts cannot silently diverge.
    val ControlBand: @Composable () -> Unit = {
        StatusBar(
            status = status,
            lastCleanArea = lastCleanArea,
            onHistory = onHistory,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 12.dp, bottom = 10.dp),
            currentRoom = liveRoom,
            timeline = liveTimeline,
            expanded = statusExpanded,
            onToggleExpanded = { statusExpanded = !statusExpanded },
        )

        ControlBar(
            status = status,
            busy = busy,
            selectedRoomCount = selectedRoomIds.size,
            onClean = {
                sessionRoomIds = emptyList()
                val order = viewModel.defaultCleanOrder()
                viewModel.run(Res.string.robot_cleaning_started) { it.clean(order) }
            },
            onCleanRooms = {
                val toClean = selectedRoomIds
                sessionRoomIds = toClean
                selectedRoomIds = emptyList()
                viewModel.run { it.cleanRooms(toClean, cleaningCount) }
            },
            onClearSelection = { selectedRoomIds = emptyList() },
            onPause = { viewModel.run { it.pause() } },
            onResume = { viewModel.run(Res.string.robot_cleaning_resumed) { it.resumeCleaning() } },
            onStop = {
                sessionRoomIds = emptyList()
                viewModel.run { it.stopCleaning() }
            },
            onDock = {
                // During a clean the official app asks how: back to charge, or empty / wash and carry on.
                if (viewModel.hasMidwayReturn && (cleaningNow || status.state == VacuumStateCodes.PAUSED)) {
                    dockChoice = true
                } else {
                    sessionRoomIds = emptyList()
                    viewModel.run(Res.string.robot_returning_to_dock) { it.dock() }
                }
            },
            onCleaningMode = { panel = DetailPanel.CleaningMode },
            onMore = { panel = DetailPanel.More },
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp),
            onZoneClean = onZoneClean,
            onRoutines = { panel = DetailPanel.Routines },
        )
    }

    if (confirmSkip) {
        AlertDialog(
            onDismissRequest = { confirmSkip = false },
            title = { Text(stringResource(Res.string.robot_skip_title)) },
            text = { Text(stringResource(Res.string.robot_skip_text)) },
            confirmButton = { TextButton(onClick = { confirmSkip = false; viewModel.run(Res.string.robot_area_skipped) { it.skipCleaningArea() } }) { Text(stringResource(Res.string.robot_skip)) } },
            dismissButton = { TextButton(onClick = { confirmSkip = false }) { Text(stringResource(Res.string.common_cancel)) } },
        )
    }
    if (dockChoice) {
        AlertDialog(
            onDismissRequest = { dockChoice = false },
            title = { Text(stringResource(Res.string.robot_dock_title)) },
            text = {
                Column {
                    TextButton(onClick = {
                        dockChoice = false
                        sessionRoomIds = emptyList()
                        viewModel.run(Res.string.robot_returning_to_dock) { it.dock() }
                    }, modifier = Modifier.fillMaxWidth()) { Text(stringResource(Res.string.robot_dock_charge)) }
                    TextButton(onClick = {
                        dockChoice = false
                        viewModel.run(Res.string.robot_returning_to_empty) { it.returnMidClean(empty = true) }
                    }, modifier = Modifier.fillMaxWidth()) { Text(stringResource(Res.string.robot_dock_empty)) }
                    if (viewModel.dockCapabilities.washIntervals.isNotEmpty()) {
                        TextButton(onClick = {
                            dockChoice = false
                            viewModel.run(Res.string.robot_returning_to_wash) { it.returnMidClean(empty = false) }
                        }, modifier = Modifier.fillMaxWidth()) { Text(stringResource(Res.string.robot_dock_wash)) }
                    }
                }
            },
            confirmButton = {},
            dismissButton = { TextButton(onClick = { dockChoice = false }) { Text(stringResource(Res.string.common_cancel)) } },
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(viewModel.deviceName, style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(Res.string.robot_go_back))
                    }
                },
                // Same three icons, in the same order, as the official app's robot page.
                actions = {
                    if (viewModel.hasCamera) {
                        IconButton(onClick = onCamera) {
                            Icon(Icons.Default.Videocam, contentDescription = stringResource(Res.string.robot_live_view))
                        }
                    }
                    IconButton(onClick = onMapEdit) {
                        Icon(Icons.Default.EditLocationAlt, contentDescription = stringResource(Res.string.robot_edit_map))
                    }
                    IconButton(onClick = onSettings) {
                        Icon(Icons.Default.Settings, contentDescription = stringResource(Res.string.robot_settings))
                    }
                },
            )
        },
    ) { paddingValues ->
        val dividerColor = MaterialTheme.colorScheme.outlineVariant
        // Over the map, as the official app: add or skip an area while cleaning, the last clean's
        // bubble while idle.
        val MapOverlay: @Composable BoxScope.() -> Unit = {
            if (cleaningNow) {
                Column(
                    Modifier.align(Alignment.CenterEnd).padding(end = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(14.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    MapSideAction(Icons.Default.AddBox, stringResource(Res.string.robot_add_area)) { onAddArea() }
                    MapSideAction(Icons.Default.Block, stringResource(Res.string.robot_skip_area)) { confirmSkip = true }
                }
            } else if (!isPausedNow) {
                val last = lastCleanForBubble
                if (last != null && last.beginEpoch != viewModel.dismissedLastClean && lastBubbleOpen) {
                    LastCleanBubble(
                        record = last,
                        onOpen = onHistory,
                        onClose = { viewModel.dismissedLastClean = last.beginEpoch; lastBubbleOpen = false },
                        modifier = Modifier.align(Alignment.TopStart).padding(12.dp),
                    )
                }
            }
        }

        val MapHero: @Composable (Modifier) -> Unit = { modifier ->
            MapHeroSection(
                deviceId = viewModel.repository.duid,
                parsedMap = parsedMap,
                busy = busy,
                selectedRoomIds = selectedRoomIds,
                sessionRoomNames = sessionRoomNames,
                floorMaps = floorMaps,
                currentFloorFlag = currentFloorFlag,
                onLoadMap = onLoadMap,
                onRecover = onRecover,
                onRoomTap = onRoomTap,
                onObstacleTap = { openObstacle = it },
                onFloorSwitch = onFloorSwitch,
                onEditMap = onMapEdit,
                mapViewport = mapViewport,
                modifier = modifier,
                overlay = { MapOverlay() },
            )
        }

        if (twoPane) {
            Row(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
                MapHero(Modifier.weight(1f).fillMaxSize())

                Column(
                    modifier = Modifier
                        .widthIn(min = 320.dp, max = 480.dp)
                        .fillMaxWidth(0.42f)
                        .fillMaxHeight()
                        .background(MaterialTheme.colorScheme.surface)
                        .drawBehind {
                            drawLine(dividerColor, Offset.Zero, Offset(0f, size.height), 1.dp.toPx())
                        },
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .consumeWindowInsets(WindowInsets.navigationBars),
                    ) {
                        val headed = panel.takeIf { sidePaneShowsPanel(it) }
                        if (headed != null) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                IconButton(onClick = { panel = null }) {
                                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(Res.string.robot_back))
                                }
                                Text(headed.title(), style = MaterialTheme.typography.titleMedium)
                            }
                            PanelBody(headed)
                        } else {
                            MoreActionsSheetContent(onAction = onMoreAction)
                        }
                    }

                    ControlBand()
                }
            }
        } else {
            // The map is the screen. Status and controls sit in a fixed band beneath it so the hero
            // never scrolls out of view and the primary action never moves under the thumb.
            Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
                MapHero(Modifier.weight(1f).fillMaxSize())

                ControlBand()
            }
        }
    }

    openObstacle?.let { obstacle ->
        ModalBottomSheet(onDismissRequest = { openObstacle = null }) {
            ObstacleSheetContent(obstacle, loadPhoto = { viewModel.repository.fetchObstaclePhoto(it) })
        }
    }

    if (!twoPane) panel?.let { current ->
        // The Maps sheet has map pictures and buttons below the fold, so it opens at full height.
        val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = current == DetailPanel.Maps)
        ModalBottomSheet(onDismissRequest = { panel = null }, sheetState = sheetState) { PanelBody(current) }
    }

    renamingMap?.let { floor ->
        RenameMapDialog(
            floor = floor,
            onConfirm = { name ->
                renamingMap = null
                viewModel.run(Res.string.robot_map_renamed) { it.renameFloorMap(floor.mapFlag, name) }
            },
            onDismiss = { renamingMap = null },
        )
    }

    if (choosingMapToKeep && floorMaps.isNotEmpty()) {
        KeepMapDialog(
            floorMaps = floorMaps,
            currentFloorFlag = currentFloorFlag,
            onConfirm = { keep ->
                choosingMapToKeep = false
                viewModel.run(Res.string.robot_multi_level_off) { it.setMultiLevel(false, keep) }
            },
            onDismiss = { choosingMapToKeep = false },
        )
    }

    deletingMap?.let { floor ->
        DeleteMapDialog(
            floor = floor,
            onConfirm = {
                deletingMap = null
                viewModel.run(Res.string.robot_map_deleted) { it.deleteFloorMap(floor.mapFlag) }
            },
            onDismiss = { deletingMap = null },
        )
    }

    backingUpMap?.let { floor ->
        BackupMapDialog(
            floor = floor,
            picture = backingUpPicture,
            onConfirm = {
                backingUpMap = null
                viewModel.run(Res.string.robot_map_backed_up) { it.backupFloorMap(floor.mapFlag) }
            },
            onDismiss = { backingUpMap = null },
        )
    }

    restoringMap?.let { floor ->
        RestoreBackupDialog(
            floor = floor,
            picture = restoringPicture,
            onConfirm = {
                restoringMap = null
                viewModel.run(Res.string.robot_map_restored) { it.restoreFloorMapBackup(floor.mapFlag) }
            },
            onDismiss = { restoringMap = null },
        )
    }
}


@Composable
private fun MapHeroSection(
    deviceId: String,
    parsedMap: ParsedMap?,
    busy: Boolean,
    selectedRoomIds: List<Int>,
    sessionRoomNames: List<String>,
    floorMaps: List<FloorMap>,
    currentFloorFlag: Int,
    onLoadMap: () -> Unit,
    onRecover: () -> Unit,
    onRoomTap: (Int) -> Unit,
    onObstacleTap: (MapObstacle) -> Unit,
    onFloorSwitch: (Int) -> Unit,
    onEditMap: () -> Unit,
    mapViewport: LiveMapViewport,
    modifier: Modifier = Modifier,
    /** Controls drawn over the map (add/skip area, the last clean's bubble). */
    overlay: @Composable BoxScope.() -> Unit = {},
) {
    val scheme = MaterialTheme.colorScheme
    val heroBackground = if (scheme.surface.luminance() < 0.5f) {
        // Black behind the map, as the official app's dark map.
        Color.Black
    } else {
        // Near-white, so the tinted rooms advance instead of sinking into a grey slab.
        scheme.surfaceContainerLowest
    }
    Box(
        modifier = modifier
            .clipToBounds()
            .background(heroBackground),
    ) {
        if (parsedMap != null) {
            VacuumMapImage(
                map = parsedMap,
                viewportScope = deviceId to currentFloorFlag,
                selectedRoomIds = selectedRoomIds,
                onRoomTap = onRoomTap,
                onObstacleTap = onObstacleTap,
                providedMapViewport = mapViewport,
                modifier = Modifier.fillMaxSize(),
            )
        } else {
            MapEmptyState(busy = busy, onLoadMap = onLoadMap, onRecover = onRecover, modifier = Modifier.fillMaxSize())
        }

        // Refresh is the only button on the map; editing is in the top bar, as in the official app,
        // and the map's name sits in the bottom-left corner where the official app shows it.
        if (floorMaps.size > 1) {
            FloorPicker(
                floorMaps = floorMaps,
                currentFloorFlag = currentFloorFlag,
                enabled = !busy,
                onFloorSwitch = onFloorSwitch,
                modifier = Modifier.align(Alignment.BottomStart).padding(start = 12.dp, bottom = 16.dp),
            )
        }

        FilledTonalIconButton(
            onClick = onLoadMap,
            enabled = !busy,
            modifier = Modifier.align(Alignment.BottomEnd).padding(12.dp).size(48.dp),
        ) {
            if (busy) CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
            else Icon(Icons.Default.Refresh, contentDescription = stringResource(Res.string.robot_refresh_map), modifier = Modifier.size(22.dp))
        }

        Column(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            if (sessionRoomNames.isNotEmpty()) {
                SessionRoomsBadge(roomNames = sessionRoomNames)
            }
        }
        overlay()
    }
}

/** Loaded map's name over the map; tapping it lists the other saved maps. */
@Composable
private fun FloorPicker(
    floorMaps: List<FloorMap>,
    currentFloorFlag: Int,
    enabled: Boolean,
    onFloorSwitch: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }
    val current = floorMaps.find { it.mapFlag == currentFloorFlag }
    Box(modifier) {
        // Plain text with a small arrow, as the official app's "Kaart2 ▾", not a chip.
        Surface(
            onClick = { expanded = true },
            enabled = enabled,
            shape = MaterialTheme.shapes.small,
            color = Color.Transparent,
            modifier = Modifier.widthIn(max = 220.dp),
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    current?.name ?: stringResource(Res.string.robot_choose_map),
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f, fill = false),
                )
                Icon(Icons.Default.ArrowDropDown, contentDescription = stringResource(Res.string.robot_switch_map), modifier = Modifier.size(20.dp))
            }
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            floorMaps.forEach { floor ->
                val selected = floor.mapFlag == currentFloorFlag
                DropdownMenuItem(
                    text = { Text(floor.name) },
                    leadingIcon = {
                        if (selected) Icon(Icons.Default.Check, contentDescription = stringResource(Res.string.robot_loaded))
                        else Spacer(Modifier.size(24.dp))
                    },
                    onClick = {
                        expanded = false
                        if (!selected) onFloorSwitch(floor.mapFlag)
                    },
                )
            }
        }
    }
}

/**
 * The robot as the official app shows it: a small white dot where it is, and over it a green pin with
 * a white swirl, of a fixed size so it stays visible at any zoom.
 */
private fun DrawScope.drawRobotPin(at: Offset, icon: Painter) {
    val green = Color(0xFF2BBF8A)
    drawCircle(Color.Black.copy(alpha = 0.25f), radius = 5.5.dp.toPx(), center = at)
    drawCircle(Color.White, radius = 4.dp.toPx(), center = at)
    val r = 13.dp.toPx()
    val center = Offset(at.x, at.y - 9.dp.toPx() - r)
    val tip = Path().apply {
        moveTo(center.x - r * 0.45f, center.y + r * 0.8f)
        lineTo(center.x, at.y - 5.dp.toPx())
        lineTo(center.x + r * 0.45f, center.y + r * 0.8f)
        close()
    }
    drawPath(tip, Color.White)
    drawCircle(Color.Black.copy(alpha = 0.2f), radius = r + 3.dp.toPx(), center = center.copy(y = center.y + 1.dp.toPx()))
    drawCircle(Color.White, radius = r + 2.dp.toPx(), center = center)
    drawCircle(green, radius = r, center = center)
    val size = r * 1.3f
    translate(left = center.x - size / 2f, top = center.y - size / 2f) {
        with(icon) { draw(Size(size, size), colorFilter = ColorFilter.tint(Color.White)) }
    }
}

/** A round button with its name under it, beside the map, as the official app's Add area / Skip area. */
@Composable
private fun MapSideAction(icon: ImageVector, label: String, onClick: () -> Unit) {
    // The caption is part of the button, as in the official app.
    Column(
        Modifier.clip(MaterialTheme.shapes.medium).clickable(onClickLabel = label, onClick = onClick).padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        FilledTonalIconButton(onClick = onClick, modifier = Modifier.size(48.dp)) { Icon(icon, contentDescription = label) }
        Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurface)
    }
}

/** The official app's bubble over the map with how the last clean went; tap for History, × to close. */
@Composable
private fun LastCleanBubble(record: CleanRecord, onOpen: () -> Unit, onClose: () -> Unit, modifier: Modifier = Modifier) {
    Surface(
        onClick = onOpen,
        modifier = modifier.widthIn(max = 260.dp),
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surfaceContainerHigh.copy(alpha = 0.95f),
        shadowElevation = 2.dp,
    ) {
        Row(Modifier.padding(start = 12.dp, top = 8.dp, bottom = 8.dp, end = 4.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(
                if (record.complete == 1) Icons.Default.CheckCircle else Icons.Default.Warning,
                contentDescription = null,
                tint = if (record.complete == 1) AppColors.Good else AppColors.Warn,
                modifier = Modifier.size(22.dp),
            )
            Spacer(Modifier.width(10.dp))
            Column(Modifier.weight(1f, fill = false)) {
                Text(formatCleanRecordTime(record.beginEpoch), style = MaterialTheme.typography.titleSmall)
                Text(
                    stringResource(
                        if (record.complete == 1) Res.string.robot_last_clean_completed else Res.string.robot_last_clean_ended_early,
                        record.durationSeconds / 60,
                        (record.areaMm2 / 1_000_000f).roundToInt(),
                    ),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            IconButton(onClick = onClose, modifier = Modifier.size(32.dp)) {
                Icon(Icons.Default.Close, contentDescription = stringResource(Res.string.robot_close), modifier = Modifier.size(18.dp))
            }
        }
    }
}

@Composable
private fun SessionRoomsBadge(roomNames: List<String>, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.widthIn(max = 220.dp),
        shape = MaterialTheme.shapes.small,
        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.92f),
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            PulsingDot(MaterialTheme.colorScheme.onPrimary)
            Text(
                text = stringResource(Res.string.robot_cleaning_rooms, roomNames.joinToString(", ")),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onPrimary,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

@Composable
private fun VacuumMapImage(
    map: ParsedMap,
    selectedRoomIds: List<Int>,
    onRoomTap: (Int) -> Unit,
    onObstacleTap: (MapObstacle) -> Unit,
    modifier: Modifier = Modifier,
    viewportScope: Any? = null,
    providedMapViewport: LiveMapViewport? = null,
) {
    val bitmap = rememberMapBitmap(map, drawRobot = false)
    val robotIcon = rememberVectorPainter(Icons.Default.Cyclone)
    val labelRooms = remember(map.rooms) { map.rooms.filter { it.labelNormX != null && it.labelNormY != null } }
    val textMeasurer = rememberTextMeasurer()
    val noGoDropLabel = stringResource(Res.string.robot_no_go_drop)
    // Labels follow the scheme rather than being a fixed black scrim: over a light map a dark
    // scrim reads as a scattering of heavy blobs and dominates the surface it sits on.
    val labelStyle = TextStyle(
        fontSize = 13.sp,
        fontWeight = FontWeight.Medium,
        color = MaterialTheme.colorScheme.onSurface,
    )
    // As in the official app: the name in a dark shade of its room's colour, after a round badge.
    val darkMap = MaterialTheme.colorScheme.surface.luminance() < 0.5f
    val roomColors = remember(map) { map.roomColorIndex() }
    val badgeStyle = TextStyle(fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.White)
    // Painters for the room-type badges, made once per set of types on the map.
    val tagIcons = remember(labelRooms) { labelRooms.mapNotNull { it.tag }.distinct() }
        .associateWith { roomTagIcon(it)?.let { icon -> rememberVectorPainter(icon) } }
    val bgColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.88f)
    val selectedBgColor = MaterialTheme.colorScheme.primary
    // In dark mode `primary` is a bright cyan, so white-on-primary would be unreadable.
    val selectedLabelStyle = labelStyle.copy(color = MaterialTheme.colorScheme.onPrimary)
    val currentOnRoomTap by rememberUpdatedState(onRoomTap)
    val currentOnObstacleTap by rememberUpdatedState(onObstacleTap)

    // A parsed map is mostly blank grid, so open framed on the floor plan rather than on padding.
    val bounds = remember(map) { map.contentBoundsNorm() }
    var viewport by remember { mutableStateOf(Size.Zero) }
    // Null until the view has been measured, so the framing is never applied from a stale size.
    val fit: Pair<Float, Offset>? = remember(bounds, viewport) {
        if (viewport.width == 0f || viewport.height == 0f) null
        else frameMap(bounds, map.fittedSize(viewport), viewport)
    }
    val currentFit by rememberUpdatedState(fit)

    val identity = map.viewportIdentity(viewportScope)
    val mapViewport = providedMapViewport ?: remember { LiveMapViewport() }
    val currentMap by rememberUpdatedState(map)
    LaunchedEffect(identity, viewport, fit) {
        mapViewport.frameIfNeeded(identity, viewport, fit)
    }

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .clipToBounds()
                .onSizeChanged { viewport = it.toSize() }
                .pointerInput(identity) {
                    detectTransformGestures { centroid, pan, zoom, _ ->
                        val view = size.toSize()
                        val (newScale, newOffset) =
                            zoomMap(mapViewport.scale, mapViewport.offset, centroid, pan, zoom, currentMap.fittedSize(view), view)
                        mapViewport.scale = newScale
                        mapViewport.offset = newOffset
                    }
                }
                .pointerInput(identity) {
                    detectTapGestures(
                        // Double tap returns to the framed view, not to the padded whole grid.
                        onDoubleTap = {
                            currentFit?.let { (s, o) ->
                                mapViewport.scale = s
                                mapViewport.offset = o
                            }
                        },
                        onTap = { tapOffset ->
                            val content = currentMap.fittedSize(size.toSize())
                            // An obstacle dot is small, so a tap near one opens it before any room.
                            currentMap.obstacleAt(tapOffset, content, mapViewport.offset, mapViewport.scale, 20.dp.toPx())
                                ?.let { currentOnObstacleTap(it); return@detectTapGestures }
                            val p = (tapOffset - mapViewport.offset) / mapViewport.scale
                            val normX = p.x / content.width
                            val normY = p.y / content.height
                            // The view is larger than the map; a tap beside it is not on a room.
                            if (normX in 0f..1f && normY in 0f..1f) {
                                currentMap.roomAtNorm(normX, normY)?.let { currentOnRoomTap(it.id) }
                            }
                        },
                    )
                },
        ) {
            val content = map.fittedSize(size)
            withTransform({
                translate(left = mapViewport.offset.x, top = mapViewport.offset.y)
                scale(mapViewport.scale, mapViewport.scale, pivot = Offset.Zero)
            }) {
                drawImage(
                    image = bitmap,
                    srcOffset = IntOffset.Zero,
                    srcSize = IntSize(bitmap.width, bitmap.height),
                    dstOffset = IntOffset.Zero,
                    dstSize = IntSize(content.width.toInt(), content.height.toInt()),
                    filterQuality = FilterQuality.Low,
                )
            }

            // Under the labels, so a name is never hidden by a badge. A tap still opens the obstacle first.
            drawObstacles(map, content, mapViewport.offset, mapViewport.scale)

            map.robotMm?.let { robot ->
                val cell = map.mmToCell(robot.x, robot.y)
                val at = Offset(
                    mapViewport.offset.x + cell.x / map.width * content.width * mapViewport.scale,
                    mapViewport.offset.y + cell.y / map.height * content.height * mapViewport.scale,
                )
                drawRobotPin(at, robotIcon)
            }

            // The robot's no-go zones at a drop get a small red tag, as on the official map.
            for (zone in map.cliffZones) {
                val cx = (zone.x0 + zone.x1 + zone.x2 + zone.x3) / 4
                val cy = (zone.y0 + zone.y1 + zone.y2 + zone.y3) / 4
                val cell = map.mmToCell(cx, cy)
                val at = Offset(
                    mapViewport.offset.x + cell.x / map.width * content.width * mapViewport.scale,
                    mapViewport.offset.y + cell.y / map.height * content.height * mapViewport.scale,
                )
                val tag = textMeasurer.measure(noGoDropLabel, badgeStyle)
                val padX = 6.dp.toPx()
                val padY = 3.dp.toPx()
                val left = at.x - tag.size.width / 2f - padX
                val top = at.y - 14.dp.toPx() - tag.size.height - padY * 2
                drawRoundRect(
                    color = Color(0xFFD92D20),
                    topLeft = Offset(left, top),
                    size = Size(tag.size.width + padX * 2, tag.size.height + padY * 2),
                    cornerRadius = CornerRadius(8.dp.toPx()),
                )
                drawText(tag, topLeft = Offset(left + padX, top + padY))
            }

            // Labels are drawn outside the zoom transform so they stay a constant, legible size
            // instead of growing to 8x with the map. Selected rooms carry their cleaning order,
            // so selection reads without relying on the highlight colour alone.
            for (room in labelRooms) {
                val normX = room.labelNormX ?: continue
                val normY = room.labelNormY ?: continue
                val cx = mapViewport.offset.x + normX * content.width * mapViewport.scale
                val cy = mapViewport.offset.y + normY * content.height * mapViewport.scale
                if (cx < 0f || cx > size.width || cy < 0f || cy > size.height) continue
                val order = selectedRoomIds.indexOf(room.id)
                val selected = order >= 0
                val tag = roomColors[room.id]?.let { (if (darkMap) RoomTagDark else RoomTagLight)[it - 1] }
                if (!selected && tag != null) {
                    val measured = textMeasurer.measure(room.name, labelStyle.copy(color = tag))
                    val r = 9.dp.toPx()
                    val gap = 4.dp.toPx()
                    val total = r * 2 + gap + measured.size.width
                    val left = cx - total / 2
                    drawCircle(color = tag, radius = r, center = Offset(left + r, cy))
                    val icon = room.tag?.let { tagIcons[it] }
                    if (icon != null) {
                        val iconSize = r * 1.25f
                        translate(left = left + r - iconSize / 2f, top = cy - iconSize / 2f) {
                            with(icon) { draw(Size(iconSize, iconSize), colorFilter = ColorFilter.tint(Color.White)) }
                        }
                    } else {
                        val initial = textMeasurer.measure(room.name.take(1).uppercase(), badgeStyle)
                        drawText(initial, topLeft = Offset(left + r - initial.size.width / 2f, cy - initial.size.height / 2f))
                    }
                    drawText(measured, topLeft = Offset(left + r * 2 + gap, cy - measured.size.height / 2f))
                    continue
                }
                val text = if (selected) "${order + 1} · ${room.name}" else room.name
                val style = if (selected) selectedLabelStyle else labelStyle
                val measured = textMeasurer.measure(text, style)
                val tw = measured.size.width.toFloat()
                val th = measured.size.height.toFloat()
                val padX = 6.dp.toPx()
                val padY = 3.dp.toPx()
                drawRoundRect(
                    color = if (selected) selectedBgColor else bgColor,
                    topLeft = Offset(cx - tw / 2 - padX, cy - th / 2 - padY),
                    size = Size(tw + padX * 2, th + padY * 2),
                    cornerRadius = CornerRadius(6.dp.toPx()),
                )
                drawText(
                    textMeasurer = textMeasurer,
                    text = text,
                    style = style,
                    topLeft = Offset(cx - tw / 2, cy - th / 2),
                )
            }
        }

        // Zoom is reported relative to the framed view, which is now the resting state.
        val baseScale = fit?.first ?: 1f
        val relativeZoom = if (baseScale > 0f) mapViewport.scale / baseScale else mapViewport.scale
        if (relativeZoom > 1.05f) {
            Surface(
                modifier = Modifier.align(Alignment.TopEnd).padding(8.dp),
                color = Color.Black.copy(alpha = 0.6f),
                shape = MaterialTheme.shapes.small,
            ) {
                Text(
                    "${(relativeZoom * 100).toInt()}%",
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                    style = MaterialTheme.typography.labelSmall,
                    color = Color.White,
                )
            }
        }
    }
}

@Composable
private fun MapEmptyState(busy: Boolean, onLoadMap: () -> Unit, onRecover: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Icon(Icons.Default.Map, null, Modifier.size(48.dp), MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(16.dp))
        Text(stringResource(Res.string.robot_no_map), style = MaterialTheme.typography.titleMedium)
        Spacer(Modifier.height(4.dp))
        Text(stringResource(Res.string.robot_no_map_text), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(20.dp))
        Button(onClick = onLoadMap, enabled = !busy) {
            Icon(Icons.Default.Refresh, null)
            Spacer(Modifier.width(8.dp))
            Text(if (busy) stringResource(Res.string.robot_loading) else stringResource(Res.string.robot_load_map))
        }
        Spacer(Modifier.height(4.dp))
        TextButton(onClick = onRecover, enabled = !busy) {
            Text(stringResource(Res.string.robot_saved_maps))
        }
    }
}


@Composable
private fun StatusBar(
    status: VacuumStatus,
    lastCleanArea: Long?,
    onHistory: () -> Unit,
    modifier: Modifier = Modifier,
    /** The room being cleaned now, from the running clean's events. */
    currentRoom: String? = null,
    /** The running clean's timeline; the card opens on it while cleaning, as the official app's does. */
    timeline: List<CleanTimelineEntry> = emptyList(),
    expanded: Boolean = false,
    onToggleExpanded: () -> Unit = {},
) {
    // clean_area is reported in mm²; show m² with one decimal.
    val separator = decimalSeparator()
    val area = lastCleanArea?.let { mm2 ->
        "${areaTenths(mm2, separator)} m²"
    }
    val time = status.cleanTime?.takeIf { it > 0 }?.let { s ->
        val m = s / 60
        if (m < 60) "${m}m" else "${m / 60}h ${m % 60}m"
    }

    val errorCode = status.errorCode ?: 0
    val drying = status.dryStatus == 1 || status.state == VacuumStateCodes.DRYING_MOP
    val active = drying || isBusyState(status.state)
    // The robot's own error is the most important thing it can tell you, so it replaces the state
    // label in place rather than only living in the app-wide banner.
    val headline = when {
        errorCode != 0 -> VacuumErrorCodes.text(errorCode).text()
        drying -> dryingLabel(status.remainingDryTimeSec)
        status.taskPausedAtDock -> stringResource(Res.string.robot_paused_at_dock)
        status.state == VacuumStateCodes.GOING_TO_WASH_MOP && status.backType == 4 -> stringResource(Res.string.robot_returning_to_empty)
        else -> stateLabel(status.state)
    }
    val headlineColor = when {
        errorCode != 0 -> MaterialTheme.colorScheme.error
        drying -> MaterialTheme.colorScheme.tertiary
        active -> stateTint(status.state)
        else -> MaterialTheme.colorScheme.onSurface
    }

    val cleaningNow = status.state in ACTIVE_CLEANING_STATES || status.taskRunning
    val areaSoFar = stringResource(Res.string.robot_area_so_far, area ?: "")
    val areaLastClean = stringResource(Res.string.robot_area_last_clean, area ?: "")
    val modeLabel = cleanModeLabel(status)
    val detail = if (status.state == VacuumStateCodes.SPOT_CLEANING) {
        // The robot keeps reporting the last clean's area and time during a spot clean.
        stringResource(Res.string.robot_spot_clean)
    } else buildList {
        // While cleaning, lead with where and how, as the official app's card does.
        if (cleaningNow) {
            currentRoom?.let { add(it) }
            add(modeLabel)
        }
        if (area != null) add(if (cleaningNow) areaSoFar else areaLastClean)
        if (time != null) add(time)
    }.joinToString(" · ").ifEmpty { stringResource(Res.string.robot_no_cleaning_yet) }

    val charging = status.chargeStatus == 1 || status.state == VacuumStateCodes.CHARGING
    val battery = status.battery
    val batteryIcon = when {
        charging -> Icons.Default.BatteryChargingFull
        battery == null -> Icons.AutoMirrored.Filled.BatteryUnknown
        battery >= 95 -> Icons.Default.BatteryFull
        battery >= 80 -> Icons.Default.Battery6Bar
        battery >= 65 -> Icons.Default.Battery5Bar
        battery >= 50 -> Icons.Default.Battery4Bar
        battery >= 35 -> Icons.Default.Battery3Bar
        battery >= 20 -> Icons.Default.Battery2Bar
        else -> Icons.Default.Battery1Bar
    }

    Surface(
        onClick = if (cleaningNow) onToggleExpanded else onHistory,
        modifier = modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surfaceContainerLow,
        shape = MaterialTheme.shapes.large,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
    ) {
      Column {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    when {
                        errorCode != 0 -> Icon(
                            Icons.Default.Error,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp),
                            tint = MaterialTheme.colorScheme.error,
                        )
                        active -> PulsingDot(headlineColor)
                    }
                    Text(
                        headline,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                        color = headlineColor,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                Text(
                    detail,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Icon(
                    batteryIcon,
                    contentDescription = stringResource(if (charging) Res.string.robot_battery_charging else Res.string.robot_battery),
                    modifier = Modifier.size(20.dp),
                    tint = batteryColor(battery),
                )
                Text(
                    battery?.let { "$it%" } ?: "—",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                )
                if (cleaningNow) {
                    Icon(
                        if (expanded) Icons.Default.KeyboardArrowDown else Icons.Default.KeyboardArrowUp,
                        contentDescription = stringResource(if (expanded) Res.string.robot_hide_timeline else Res.string.robot_show_timeline),
                        modifier = Modifier.size(22.dp),
                        tint = MaterialTheme.colorScheme.primary,
                    )
                }
            }
        }
        // A dock problem in the card itself, as the official app's banner ("Check the clean water tank").
        val dockError = status.dockErrorStatus?.takeIf { it != 0 }
        if (dockError != null) {
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            Row(
                Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Icon(Icons.Default.Warning, contentDescription = null, tint = MaterialTheme.colorScheme.error, modifier = Modifier.size(18.dp))
                Text(
                    DockErrorCodes.text(dockError).text(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error,
                )
            }
        }
        if (cleaningNow && expanded) {
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            Column(
                Modifier.fillMaxWidth().heightIn(max = 360.dp).verticalScroll(rememberScrollState()).padding(vertical = 8.dp),
            ) {
                if (timeline.isEmpty()) {
                    Text(
                        stringResource(Res.string.robot_reading_timeline),
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                timeline.forEachIndexed { i, entry ->
                    TimelineRow(entry, first = i == 0, last = i == timeline.lastIndex)
                }
            }
        }
      }
    }
}


/** The cleaning mode as the mode button names it: SmartPlan, per room, Mop, Vac & Mop or Vacuum. */
@Composable
private fun cleanModeLabel(status: VacuumStatus): String {
    val mopping = (status.waterBoxCustomMode ?: WaterBoxMode.OFF) != WaterBoxMode.OFF
    return stringResource(when {
        isSmartModeSet(status.fanPower, status.waterBoxCustomMode, null) -> CleanPlan.SMART.label
        isCustomizeModeSet(status.fanPower, status.waterBoxCustomMode, null) -> CleanPlan.PER_ROOM.label
        mopping && status.fanPower == VacuumFanPower.OFF -> Res.string.robot_mode_mop
        mopping -> Res.string.robot_mode_vac_mop
        else -> Res.string.robot_mode_vacuum
    })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ControlBar(
    status: VacuumStatus,
    busy: Boolean,
    selectedRoomCount: Int,
    onClean: () -> Unit,
    onCleanRooms: () -> Unit,
    onClearSelection: () -> Unit,
    onPause: () -> Unit,
    onResume: () -> Unit,
    onStop: () -> Unit,
    onDock: () -> Unit,
    onCleaningMode: () -> Unit,
    onMore: () -> Unit,
    modifier: Modifier = Modifier,
    onZoneClean: () -> Unit = {},
    onRoutines: () -> Unit = {},
) {
    val isCleaning = status.state in ACTIVE_CLEANING_STATES || status.taskRunning
    val isPaused = status.state == VacuumStateCodes.PAUSED || status.taskPausedAtDock
    val mopping = (status.waterBoxCustomMode ?: WaterBoxMode.OFF) != WaterBoxMode.OFF

    Column(modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        // What the official app has no place for: the room selection, stop and the other actions.
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            // Tapping rooms on the map used to have no visible consequence. The selection now
            // surfaces here and retargets the primary action.
            if (selectedRoomCount > 0) {
                InputChip(
                    selected = true,
                    onClick = onClearSelection,
                    label = {
                        Text(pluralStringResource(Res.plurals.robot_rooms_selected, selectedRoomCount, selectedRoomCount))
                    },
                    trailingIcon = {
                        Icon(
                            Icons.Default.Close,
                            contentDescription = stringResource(Res.string.robot_clear_selection),
                            modifier = Modifier.size(18.dp),
                        )
                    },
                )
            }
            Spacer(Modifier.weight(1f))
            if (isCleaning || isPaused) {
                FilledTonalIconButton(onClick = onStop, enabled = !busy, modifier = Modifier.size(40.dp)) {
                    Icon(Icons.Default.Stop, contentDescription = stringResource(Res.string.robot_stop_cleaning), modifier = Modifier.size(20.dp))
                }
            }
            FilledTonalIconButton(onClick = onMore, modifier = Modifier.size(40.dp)) {
                Icon(Icons.Default.MoreHoriz, contentDescription = stringResource(Res.string.robot_more_actions), modifier = Modifier.size(20.dp))
            }
        }

        // While idle, what the start button cleans, as the official app's Full / Room / Zones / Routine.
        var roomMode by remember { mutableStateOf(false) }
        val idle = !isCleaning && !isPaused
        if (idle) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                val roomsChosen = roomMode || selectedRoomCount > 0
                StartModeTab(stringResource(Res.string.robot_tab_full), selected = !roomsChosen) { roomMode = false; if (selectedRoomCount > 0) onClearSelection() }
                StartModeTab(stringResource(Res.string.robot_tab_room), selected = roomsChosen) { roomMode = true }
                StartModeTab(stringResource(Res.string.robot_tab_zones), selected = false, onClick = onZoneClean)
                StartModeTab(stringResource(Res.string.robot_tab_routine), selected = false, onClick = onRoutines)
            }
            if (roomMode && selectedRoomCount == 0) {
                Text(
                    stringResource(Res.string.robot_tap_rooms),
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                )
            }
        }
        val waitingForRooms = idle && roomMode && selectedRoomCount == 0

        // The official app's bar: mode on the left, a large round start/pause in the middle with the
        // clean's progress around it, back to the dock on the right.
        Surface(
            shape = MaterialTheme.shapes.extraLarge,
            color = MaterialTheme.colorScheme.surfaceContainerHigh,
            modifier = Modifier.fillMaxWidth(),
        ) {
            Row(Modifier.padding(horizontal = 8.dp, vertical = 8.dp), verticalAlignment = Alignment.CenterVertically) {
                BarAction(
                    icon = Icons.Default.Tune,
                    label = cleanModeLabel(status),
                    onClick = onCleaningMode,
                    modifier = Modifier.weight(1f),
                )
                val primaryLabel = stringResource(when {
                    isCleaning -> Res.string.robot_pause
                    selectedRoomCount > 0 -> Res.string.robot_clean_rooms
                    isPaused -> Res.string.robot_resume
                    else -> Res.string.robot_clean
                })
                Box(contentAlignment = Alignment.Center, modifier = Modifier.size(84.dp)) {
                    val progress = status.cleanPercent?.takeIf { (isCleaning || isPaused) && it in 0..100 }
                    if (progress != null) {
                        CircularProgressIndicator(
                            progress = { progress / 100f },
                            modifier = Modifier.fillMaxSize(),
                            strokeWidth = 4.dp,
                            trackColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                        )
                    }
                    FilledIconButton(
                        onClick = {
                            when {
                                isCleaning -> onPause()
                                selectedRoomCount > 0 -> onCleanRooms()
                                isPaused -> onResume()
                                else -> onClean()
                            }
                        },
                        enabled = !busy && !waitingForRooms,
                        modifier = Modifier.size(68.dp),
                    ) {
                        Icon(
                            if (isCleaning) Icons.Default.Pause else Icons.Default.PlayArrow,
                            contentDescription = primaryLabel,
                            modifier = Modifier.size(34.dp),
                        )
                    }
                }
                BarAction(
                    icon = Icons.Default.Home,
                    label = stringResource(Res.string.robot_return_to_dock),
                    onClick = onDock,
                    enabled = !busy,
                    modifier = Modifier.weight(1f),
                )
            }
        }
        if (selectedRoomCount > 0 && !isCleaning) {
            Text(
                stringResource(Res.string.robot_start_cleans_selected),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.align(Alignment.CenterHorizontally),
            )
        }
    }
}

/** A text tab over the control bar: chosen in bold with a short line under it. */
@Composable
private fun StartModeTab(label: String, selected: Boolean, onClick: () -> Unit) {
    Surface(onClick = onClick, color = Color.Transparent, shape = MaterialTheme.shapes.small) {
        Column(Modifier.padding(horizontal = 10.dp, vertical = 4.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                label,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
                color = if (selected) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Box(Modifier.padding(top = 3.dp).size(width = 18.dp, height = 3.dp).background(
                if (selected) MaterialTheme.colorScheme.primary else Color.Transparent, CircleShape,
            ))
        }
    }
}

/** An icon with its name under it, for the sides of the control bar. */
@Composable
private fun BarAction(icon: ImageVector, label: String, onClick: () -> Unit, modifier: Modifier = Modifier, enabled: Boolean = true) {
    Surface(onClick = onClick, enabled = enabled, shape = MaterialTheme.shapes.large, color = Color.Transparent, modifier = modifier) {
        Column(Modifier.padding(vertical = 8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(26.dp), tint = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.height(4.dp))
            Text(
                label,
                style = MaterialTheme.typography.labelMedium,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}


private enum class MoreAction { ROOMS, ZONES, PIN_GO, REMOTE, SCHEDULE, CAMERA, DOCK, HISTORY, MAPS, EDIT_MAP, ROUTINES }

@Composable
private fun MoreActionsSheetContent(onAction: (MoreAction) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            // Scrolls on short screens where the half-expanded sheet can't show every row.
            .verticalScroll(rememberScrollState())
            .navigationBarsPadding()
            .padding(horizontal = 16.dp)
            .padding(bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            stringResource(Res.string.robot_more_actions),
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(start = 4.dp, bottom = 4.dp),
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            MoreActionItem(Modifier.weight(1f), Icons.Default.GridView, stringResource(Res.string.robot_action_rooms), MaterialTheme.colorScheme.primary) { onAction(MoreAction.ROOMS) }
            MoreActionItem(Modifier.weight(1f), Icons.Default.SelectAll, stringResource(Res.string.robot_action_zone_clean), AppColors.AccentIndigo) { onAction(MoreAction.ZONES) }
            MoreActionItem(Modifier.weight(1f), Icons.Default.Place, stringResource(Res.string.robot_action_pin_go), AppColors.AccentPurple) { onAction(MoreAction.PIN_GO) }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            MoreActionItem(Modifier.weight(1f), Icons.Default.Gamepad, stringResource(Res.string.robot_action_remote), AppColors.AccentCyan) { onAction(MoreAction.REMOTE) }
            MoreActionItem(Modifier.weight(1f), Icons.Default.Schedule, stringResource(Res.string.robot_action_schedule), AppColors.Water) { onAction(MoreAction.SCHEDULE) }
            MoreActionItem(Modifier.weight(1f), Icons.Default.Edit, stringResource(Res.string.robot_edit_map), AppColors.AccentRed) { onAction(MoreAction.EDIT_MAP) }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            MoreActionItem(Modifier.weight(1f), Icons.Default.Dock, stringResource(Res.string.robot_action_dock), AppColors.Good) { onAction(MoreAction.DOCK) }
            MoreActionItem(Modifier.weight(1f), Icons.Default.History, stringResource(Res.string.robot_action_history), MaterialTheme.colorScheme.tertiary) { onAction(MoreAction.HISTORY) }
            MoreActionItem(Modifier.weight(1f), Icons.Default.Map, stringResource(Res.string.robot_action_maps), AppColors.Warn) { onAction(MoreAction.MAPS) }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            MoreActionItem(Modifier.weight(1f), Icons.AutoMirrored.Filled.PlaylistPlay, stringResource(Res.string.robot_action_routines), MaterialTheme.colorScheme.secondary) { onAction(MoreAction.ROUTINES) }
            Spacer(Modifier.weight(1f))
            Spacer(Modifier.weight(1f))
        }
    }
}

/** Routines made in the official app. local_roborock_server stores and runs them; the app only lists and starts them. */
@Composable
private fun RoutinesSheetContent(
    state: RoutinesState,
    busy: Boolean,
    onRun: (Scene) -> Unit,
    onRetry: () -> Unit,
    onNew: () -> Unit,
    onEdit: (Scene) -> Unit,
    onDelete: (Scene) -> Unit,
    onAddRecommended: (RecommendedRoutine, name: String, addedMessage: String) -> Unit,
) {
    var confirmDelete by remember { mutableStateOf<Scene?>(null) }
    confirmDelete?.let { scene ->
        AlertDialog(
            onDismissRequest = { confirmDelete = null },
            title = { Text(stringResource(Res.string.routines_delete_title, scene.name)) },
            text = { Text(stringResource(Res.string.routines_delete_text)) },
            confirmButton = { TextButton(onClick = { onDelete(scene); confirmDelete = null }) { Text(stringResource(Res.string.common_delete)) } },
            dismissButton = { TextButton(onClick = { confirmDelete = null }) { Text(stringResource(Res.string.common_cancel)) } },
        )
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 24.dp)
            .navigationBarsPadding()
            .padding(bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text(stringResource(Res.string.routines_title), style = MaterialTheme.typography.headlineSmall)
        Text(
            stringResource(Res.string.routines_intro),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        when (state) {
            RoutinesState.Loading -> Box(Modifier.fillMaxWidth().padding(24.dp), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
            is RoutinesState.Failed -> {
                Text(state.reason, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.error)
                OutlinedButton(onClick = onRetry, modifier = Modifier.fillMaxWidth()) { Text(stringResource(Res.string.common_try_again)) }
            }
            is RoutinesState.Loaded -> if (state.routines.isEmpty()) {
                Text(
                    stringResource(Res.string.routines_empty),
                    style = MaterialTheme.typography.bodyMedium,
                )
                FilledTonalButton(onClick = onNew, modifier = Modifier.fillMaxWidth()) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(6.dp))
                    Text(stringResource(Res.string.routines_new))
                }
            } else {
                state.routines.forEach { scene ->
                    Surface(shape = MaterialTheme.shapes.large, color = MaterialTheme.colorScheme.surfaceContainerHigh) {
                        Row(
                            Modifier.fillMaxWidth().padding(start = 16.dp, end = 8.dp, top = 8.dp, bottom = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            Text(
                                scene.name,
                                style = MaterialTheme.typography.titleSmall,
                                modifier = Modifier.weight(1f),
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                            )
                            IconButton(onClick = { onEdit(scene) }) { Icon(Icons.Default.Edit, stringResource(Res.string.routines_edit, scene.name)) }
                            IconButton(onClick = { confirmDelete = scene }) { Icon(Icons.Default.Delete, stringResource(Res.string.routines_delete_title, scene.name)) }
                            FilledTonalButton(onClick = { onRun(scene) }, enabled = !busy) {
                                Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(Modifier.width(6.dp))
                                Text(stringResource(Res.string.routines_run))
                            }
                        }
                    }
                }
                OutlinedButton(onClick = onNew, modifier = Modifier.fillMaxWidth()) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(6.dp))
                    Text(stringResource(Res.string.routines_new))
                }
            }
        }
        if (state is RoutinesState.Loaded) {
            Spacer(Modifier.height(8.dp))
            Text(stringResource(Res.string.routines_recommended), style = MaterialTheme.typography.titleMedium)
            RecommendedRoutine.entries.forEach { recommended ->
                val (nameRes, textRes) = recommendedRoutineStrings(recommended)
                val name = stringResource(nameRes)
                val added = stringResource(Res.string.routines_added, name)
                Surface(shape = MaterialTheme.shapes.large, color = MaterialTheme.colorScheme.surfaceContainer) {
                    Row(
                        Modifier.fillMaxWidth().padding(start = 16.dp, end = 8.dp, top = 10.dp, bottom = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Column(Modifier.weight(1f)) {
                            Text(name, style = MaterialTheme.typography.titleSmall)
                            Text(
                                stringResource(textRes),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                        TextButton(onClick = { onAddRecommended(recommended, name, added) }, enabled = !busy) {
                            Text(stringResource(Res.string.routines_add))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MoreActionItem(
    modifier: Modifier,
    icon: ImageVector,
    label: String,
    iconTint: Color,
    onClick: () -> Unit,
) {
    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
    ) {
        Column(
            modifier = Modifier.padding(vertical = 16.dp, horizontal = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Surface(shape = CircleShape, color = iconTint.copy(alpha = 0.15f), modifier = Modifier.size(40.dp)) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(icon, contentDescription = null, modifier = Modifier.size(20.dp), tint = iconTint)
                }
            }
            Text(
                label,
                style = MaterialTheme.typography.labelMedium,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CleaningModeSheetContent(
    currentFanPower: Int?,
    currentWaterMode: Int?,
    currentRoute: Int?,
    cleaningCount: Int,
    /** Water slide robots get the official app's 30-step slider instead of the four water buttons. */
    waterSlide: Boolean = false,
    /** The routes this robot takes ([mopRouteOptions]); null offers all of them. */
    routeOptions: Set<Int>? = null,
    busy: Boolean,
    supportsSmartPlan: Boolean,
    rooms: List<ParsedMapRoom>,
    roomCleanModes: List<RoomCleanMode>,
    roomModesLoaded: Boolean?,
    error: String?,
    onPlanSelected: (CleanPlan) -> Unit,
    onSaveRoomModes: (List<RoomCleanMode>) -> Unit,
    onFanPowerSelected: (Int) -> Unit,
    onWaterModeSelected: (Int) -> Unit,
    onRouteSelected: (Int) -> Unit,
    onCountChange: (Int) -> Unit,
    /** Null when the robot cannot vacuum first and mop after, or has not said which it does. */
    vacuumThenMop: Boolean? = null,
    onVacuumThenMopChange: (Boolean) -> Unit = {},
    /** Null when the robot has no intensive corner mopping. */
    cornerClean: Boolean? = null,
    onCornerCleanChange: (Boolean) -> Unit = {},
    /** The custom water flow as a distance; null when the robot cannot set one. */
    waterDistance: Int? = null,
    onWaterDistanceChange: (Int) -> Unit = {},
    /** Opens the carpet, floor and dock settings, as the official sheet's "More cleaning settings". */
    onMoreSettings: (() -> Unit)? = null,
) {
    val initialTab = when {
        (currentWaterMode ?: WaterBoxMode.OFF) == WaterBoxMode.OFF -> 2
        currentFanPower == VacuumFanPower.OFF -> 1
        else -> 0
    }
    var selectedTab by remember { mutableIntStateOf(initialTab) }
    // SmartPlan owns suction, water and route; the robot rejects manual changes until it is left.
    val smart = isSmartModeSet(currentFanPower, currentWaterMode, currentRoute)
    // Per-room settings report their own codes for all three as well.
    val perRoom = isCustomizeModeSet(currentFanPower, currentWaterMode, currentRoute)
    val plan = when {
        smart -> CleanPlan.SMART
        perRoom -> CleanPlan.PER_ROOM
        else -> CleanPlan.MANUAL
    }
    val plans = buildList {
        if (supportsSmartPlan || smart) add(CleanPlan.SMART)
        add(CleanPlan.MANUAL)
        if (rooms.isNotEmpty() || perRoom) add(CleanPlan.PER_ROOM)
    }

    Column(Modifier.fillMaxWidth().navigationBarsPadding().padding(bottom = 16.dp)) {
        Spacer(Modifier.height(12.dp))
        if (plans.size > 1) {
            // The plans as the official sheet's large text tabs, the chosen one bright.
            Row(
                Modifier.padding(horizontal = 16.dp).padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                plans.forEach { p ->
                    val chosen = p == plan
                    Surface(
                        onClick = { if (!busy && !chosen) onPlanSelected(p) },
                        shape = MaterialTheme.shapes.medium,
                        color = Color.Transparent,
                    ) {
                        Text(
                            stringResource(p.label),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp),
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = if (chosen) FontWeight.SemiBold else FontWeight.Normal,
                            color = if (chosen) MaterialTheme.colorScheme.onSurface
                                else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                        )
                    }
                }
            }
        }
        error?.let {
            Text(
                it,
                modifier = Modifier.padding(horizontal = 24.dp).padding(bottom = 8.dp),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error,
            )
        }
        if (smart) {
            Column(Modifier.padding(horizontal = 24.dp, vertical = 16.dp), verticalArrangement = Arrangement.spacedBy(20.dp)) {
                Text(stringResource(Res.string.robot_smartplan_text), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                CountSelector(cleaningCount, onCountChange)
            }
        } else if (perRoom) {
            if (roomModesLoaded == true) {
                RoomCleanModesEditor(rooms, roomCleanModes, busy, onSaveRoomModes, routeOptions)
            } else {
                Text(
                    if (roomModesLoaded == null) stringResource(Res.string.robot_reading_room_settings)
                    else stringResource(Res.string.robot_room_settings_failed),
                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        } else {
            val pages = buildList {
                if (vacuumThenMop != null) add(ModePage.VAC_THEN_MOP)
                add(ModePage.VAC_AND_MOP); add(ModePage.MOP); add(ModePage.VACUUM)
            }
            val startPage = when {
                initialTab == 0 && vacuumThenMop == true -> ModePage.VAC_THEN_MOP
                initialTab == 0 -> ModePage.VAC_AND_MOP
                initialTab == 1 -> ModePage.MOP
                else -> ModePage.VACUUM
            }
            val pagerState = rememberPagerState(initialPage = pages.indexOf(startPage)) { pages.size }
            val pagerScope = rememberCoroutineScope()
            // Only a page the user settles on changes the robot; opening the sheet sends nothing.
            var appliedPage by remember { mutableStateOf(startPage) }
            // The collector below lives as long as the sheet; it must see the robot's current values,
            // not the ones from when the sheet opened (switching back sent nothing otherwise).
            val water by rememberUpdatedState(currentWaterMode)
            val fan by rememberUpdatedState(currentFanPower)
            val thenMop by rememberUpdatedState(vacuumThenMop)
            val setWater by rememberUpdatedState(onWaterModeSelected)
            val setFan by rememberUpdatedState(onFanPowerSelected)
            val setThenMop by rememberUpdatedState(onVacuumThenMopChange)
            LaunchedEffect(pagerState) {
                snapshotFlow { pagerState.settledPage }.collect { index ->
                    val page = pages[index]
                    if (page == appliedPage) return@collect
                    appliedPage = page
                    selectedTab = page.tab
                    val waterOff = (water ?: WaterBoxMode.OFF) == WaterBoxMode.OFF
                    val fanOff = fan == VacuumFanPower.OFF
                    when (page) {
                        ModePage.VAC_THEN_MOP, ModePage.VAC_AND_MOP -> {
                            if (waterOff) setWater(WaterBoxMode.LOW)
                            if (fanOff) setFan(VacuumFanPower.BALANCED)
                            val wantThenMop = page == ModePage.VAC_THEN_MOP
                            val now = thenMop
                            if (now != null && now != wantThenMop) setThenMop(wantThenMop)
                        }
                        ModePage.MOP -> { if (waterOff) setWater(WaterBoxMode.LOW); setFan(VacuumFanPower.OFF) }
                        ModePage.VACUUM -> { if (fanOff) setFan(VacuumFanPower.BALANCED); setWater(WaterBoxMode.OFF) }
                    }
                }
            }
            SecondaryScrollableTabRow(selectedTabIndex = pagerState.currentPage, edgePadding = 16.dp) {
                pages.forEachIndexed { i, page ->
                    Tab(
                        selected = pagerState.currentPage == i,
                        onClick = { pagerScope.launch { pagerState.animateScrollToPage(i) } },
                        text = { Text(stringResource(page.title)) },
                    )
                }
            }

            HorizontalPager(state = pagerState, verticalAlignment = Alignment.Top) { pageIndex ->
            val pageTab = pages[pageIndex].tab
            Box(Modifier.fillMaxWidth().heightIn(min = 380.dp)) {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 24.dp, vertical = 16.dp)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                ) {
                    val subtitle = when (pageTab) {
                        0 -> if (pages[pageIndex] == ModePage.VAC_THEN_MOP) stringResource(Res.string.robot_subtitle_vac_then_mop) else stringResource(Res.string.robot_subtitle_vac_mop)
                        1 -> stringResource(Res.string.robot_subtitle_mop)
                        else -> stringResource(Res.string.robot_subtitle_vacuum)
                    }
                    Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)

                    val suction = @Composable {
                        ModeOptionSelector(stringResource(Res.string.robot_suction_power), FAN_OPTIONS.map { stringResource(it.second) }, FAN_OPTIONS.indexOfFirst { it.first == currentFanPower }, !busy, icons = FAN_OPTIONS.map { suctionIcon(it.first) }) { onFanPowerSelected(FAN_OPTIONS[it].first) }
                    }
                    val water = @Composable {
                        if (waterSlide) WaterSlideSelector(currentWaterMode, !busy, onWaterModeSelected)
                        else if (waterDistance != null) WaterDistanceSelector(currentWaterMode, waterDistance, !busy, onWaterModeSelected, onWaterDistanceChange)
                        else ModeOptionSelector(stringResource(Res.string.robot_water_flow), WATER_OPTIONS.map { stringResource(it.second) }, WATER_OPTIONS.indexOfFirst { it.first == WaterBoxMode.normalize(currentWaterMode) }, !busy) { onWaterModeSelected(WATER_OPTIONS[it].first) }
                    }
                    val routes = routeChoices(routeOptions, currentRoute)
                    val route = @Composable {
                        ModeOptionSelector(stringResource(Res.string.robot_route), routes.map { stringResource(it.second) }, routes.indexOfFirst { it.first == currentRoute }, !busy, icons = routes.map { routeIcon(it.first) }) { onRouteSelected(routes[it].first) }
                    }

                    when (pageTab) {
                        0 -> {
                            suction(); water(); CountSelector(cleaningCount, onCountChange); route()
                            cornerClean?.let { CornerCleanRow(it, !busy, onCornerCleanChange) }
                        }
                        1 -> { water(); CountSelector(cleaningCount, onCountChange); route(); cornerClean?.let { CornerCleanRow(it, !busy, onCornerCleanChange) } }
                        2 -> { suction(); CountSelector(cleaningCount, onCountChange); route() }
                    }
                }
            }
            }
        }
        onMoreSettings?.let { open ->
            Surface(
                onClick = open,
                modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp).padding(top = 16.dp),
                shape = MaterialTheme.shapes.large,
                color = MaterialTheme.colorScheme.surfaceContainerHigh,
            ) {
                Row(Modifier.padding(horizontal = 16.dp, vertical = 16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Text(stringResource(Res.string.robot_more_cleaning_settings), style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = null)
                }
            }
        }
    }
}

/** The official app's three plans: SmartPlan, General (one mode for all) and Custom (per room). */
private enum class CleanPlan(val label: StringResource) {
    SMART(Res.string.robot_plan_smart),
    MANUAL(Res.string.robot_plan_general),
    PER_ROOM(Res.string.robot_plan_custom),
}

/** The modes of the General plan, as the official sheet's swipeable strip. */
private enum class ModePage(val title: StringResource, val tab: Int) {
    VAC_THEN_MOP(Res.string.robot_page_vac_then_mop, 0),
    VAC_AND_MOP(Res.string.robot_mode_vac_mop, 0),
    MOP(Res.string.robot_mode_mop, 1),
    VACUUM(Res.string.robot_mode_vacuum, 2),
}

@Composable
private fun roomModeSummary(mode: RoomCleanMode): String {
    val fan = stringResource(ROOM_FAN_OPTIONS.firstOrNull { it.first == mode.fanPower }?.second ?: Res.string.robot_other)
    val water = stringResource(WATER_OPTIONS.firstOrNull { it.first == WaterBoxMode.normalize(mode.waterBoxMode) }?.second ?: Res.string.robot_other)
    return when {
        mode.waterBoxMode == WaterBoxMode.OFF -> stringResource(Res.string.robot_summary_vacuum, fan)
        mode.fanPower == VacuumFanPower.OFF -> stringResource(Res.string.robot_summary_mop, water)
        else -> stringResource(Res.string.robot_summary_vac_mop, fan, water)
    }
}

/**
 * One row per room on the map; a tapped room opens its suction, water and route. Nothing is sent
 * until Save, which writes every room in one command.
 */
@Composable
private fun RoomCleanModesEditor(
    rooms: List<ParsedMapRoom>,
    stored: List<RoomCleanMode>,
    busy: Boolean,
    onSave: (List<RoomCleanMode>) -> Unit,
    routeOptions: Set<Int>? = null,
) {
    val saved = remember(rooms, stored) { roomCleanModesFor(rooms.map { it.id }, stored) }
    var draft by remember(saved) { mutableStateOf(saved) }
    var openSegment by remember { mutableStateOf<Int?>(null) }
    // Rooms the robot has no settings for yet still need the first save.
    val unsaved = draft != saved || rooms.any { room -> stored.none { it.segment == room.id } }

    Column(Modifier.padding(horizontal = 24.dp, vertical = 16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(
            if (rooms.isEmpty()) stringResource(Res.string.robot_room_modes_load_map)
            else stringResource(Res.string.robot_room_modes_text),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Column(Modifier.weight(1f, fill = false).verticalScroll(rememberScrollState())) {
            rooms.forEachIndexed { index, room ->
                val mode = draft.firstOrNull { it.segment == room.id } ?: return@forEachIndexed
                val open = openSegment == room.id
                val update = { changed: RoomCleanMode ->
                    draft = draft.map { if (it.segment == room.id) changed else it }
                }
                if (index > 0) HorizontalDivider()
                Surface(onClick = { openSegment = if (open) null else room.id }, color = Color.Transparent) {
                    Row(Modifier.fillMaxWidth().padding(vertical = 14.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            room.name,
                            modifier = Modifier.weight(1f),
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                        Text(roomModeSummary(mode), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
                    }
                }
                if (open) {
                    Column(Modifier.padding(bottom = 16.dp), verticalArrangement = Arrangement.spacedBy(20.dp)) {
                        ModeOptionSelector(stringResource(Res.string.robot_suction_power), ROOM_FAN_OPTIONS.map { stringResource(it.second) }, ROOM_FAN_OPTIONS.indexOfFirst { it.first == mode.fanPower }, !busy, icons = ROOM_FAN_OPTIONS.map { suctionIcon(it.first) }) {
                            val fan = ROOM_FAN_OPTIONS[it].first
                            // A room with neither suction nor water would not be cleaned at all.
                            val water = if (fan == VacuumFanPower.OFF && mode.waterBoxMode == WaterBoxMode.OFF) WaterBoxMode.LOW else mode.waterBoxMode
                            update(mode.copy(fanPower = fan, waterBoxMode = water))
                        }
                        ModeOptionSelector(stringResource(Res.string.robot_water_flow), WATER_OPTIONS.map { stringResource(it.second) }, WATER_OPTIONS.indexOfFirst { it.first == WaterBoxMode.normalize(mode.waterBoxMode) }, !busy) {
                            val water = WATER_OPTIONS[it].first
                            val fan = if (water == WaterBoxMode.OFF && mode.fanPower == VacuumFanPower.OFF) VacuumFanPower.BALANCED else mode.fanPower
                            // The route is hidden without water. A Qrevo S Pro (02.33.88) refuses Deep and Deep+ in a
                            // room that vacuums and a Qrevo Curv 2 Flow refuses Deep in any room, so a hidden one
                            // would make the whole save fail.
                            val route = if (water == WaterBoxMode.OFF && mode.mopMode in DEEP_ROUTES) MopRoute.STANDARD else mode.mopMode
                            update(mode.copy(fanPower = fan, waterBoxMode = water, mopMode = route))
                        }
                        if (mode.waterBoxMode != WaterBoxMode.OFF) {
                            val routes = routeChoices(routeOptions, mode.mopMode)
                            ModeOptionSelector(stringResource(Res.string.robot_route), routes.map { stringResource(it.second) }, routes.indexOfFirst { it.first == mode.mopMode }, !busy) {
                                update(mode.copy(mopMode = routes[it].first))
                            }
                        }
                    }
                }
            }
        }
        if (rooms.isNotEmpty()) {
            Button(onClick = { onSave(draft) }, enabled = !busy && unsaved, modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(Res.string.robot_save_room_settings))
            }
        }
    }
}

@Composable
private fun ModeOptionSelector(
    label: String,
    options: List<String>,
    selectedIndex: Int,
    enabled: Boolean,
    /** One icon per option: drawn as the official app's icon segments, the name stays in the title. */
    icons: List<ImageVector?>? = null,
    onSelect: (Int) -> Unit,
) {
    val currentValue = options.getOrElse(selectedIndex) { "" }
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(label, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
            Text(currentValue, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary)
        }
        if (icons != null && icons.size == options.size && icons.all { it != null }) {
            SegmentedChoice(options.size, selectedIndex, enabled, onSelect) { index, selected ->
                Icon(
                    icons[index]!!,
                    contentDescription = options[index],
                    modifier = Modifier.size(24.dp),
                    tint = if (selected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            return@Column
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            options.forEachIndexed { index, option ->
                val selected = selectedIndex == index
                Surface(
                    onClick = { if (enabled) onSelect(index) },
                    modifier = Modifier.weight(1f),
                    shape = CircleShape,
                    color = if (selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceContainerHigh,
                ) {
                    Text(option, modifier = Modifier.padding(vertical = 14.dp), style = MaterialTheme.typography.labelMedium,
                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal,
                        color = if (selected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant,
                        textAlign = TextAlign.Center)
                }
            }
        }
    }
}

/** The official app's segmented control: one rounded track with the chosen segment filled. */
@Composable
private fun SegmentedChoice(
    count: Int,
    selectedIndex: Int,
    enabled: Boolean,
    onSelect: (Int) -> Unit,
    content: @Composable (index: Int, selected: Boolean) -> Unit,
) {
    Surface(shape = CircleShape, color = MaterialTheme.colorScheme.surfaceContainerHigh, modifier = Modifier.fillMaxWidth()) {
        Row(Modifier.padding(4.dp), horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            for (index in 0 until count) {
                val selected = index == selectedIndex
                Surface(
                    onClick = { if (enabled && !selected) onSelect(index) },
                    modifier = Modifier.weight(1f).height(44.dp),
                    shape = CircleShape,
                    color = if (selected) MaterialTheme.colorScheme.primaryContainer else Color.Transparent,
                ) {
                    Box(contentAlignment = Alignment.Center) { content(index, selected) }
                }
            }
        }
    }
}

/** Vacuum the whole area first, then mop it, as the official app's "Vacuum, then mop". */
@Composable
private fun LabeledSwitchRow(title: String, subtitle: String, checked: Boolean, enabled: Boolean, onChange: (Boolean) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Column(Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Switch(checked = checked, enabled = enabled, onCheckedChange = onChange)
    }
}

@Composable
private fun VacuumThenMopRow(checked: Boolean, enabled: Boolean, onChange: (Boolean) -> Unit) =
    LabeledSwitchRow(stringResource(Res.string.robot_vac_then_mop_title), stringResource(Res.string.robot_vac_then_mop_text), checked, enabled, onChange)

@Composable
private fun CornerCleanRow(checked: Boolean, enabled: Boolean, onChange: (Boolean) -> Unit) =
    LabeledSwitchRow(stringResource(Res.string.robot_corner_title), stringResource(Res.string.robot_corner_text), checked, enabled, onChange)

/** The four water buttons plus Custom, whose amount is set with a slider ([WaterDistance]). */
@Composable
private fun WaterDistanceSelector(current: Int?, distance: Int, enabled: Boolean, onMode: (Int) -> Unit, onDistance: (Int) -> Unit) {
    val options = WATER_OPTIONS + (WaterBoxMode.USER_CUSTOM to Res.string.robot_water_custom)
    val custom = current == WaterBoxMode.USER_CUSTOM
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        ModeOptionSelector(stringResource(Res.string.robot_water_flow), options.map { stringResource(it.second) }, options.indexOfFirst { it.first == WaterBoxMode.normalize(current) }, enabled) {
            val code = options[it].first
            if (code == WaterBoxMode.USER_CUSTOM) onDistance(distance) else onMode(code)
        }
        if (custom) {
            val range = WaterDistance.RANGE
            var level by remember(distance) { mutableFloatStateOf(WaterDistance.level(distance).toFloat()) }
            val max = WaterDistance.level(range.first)
            Text(stringResource(Res.string.robot_custom_amount, level.roundToInt(), max), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
            Slider(
                value = level,
                onValueChange = { level = it },
                // Read the state here: a tap ends before a recomposition.
                onValueChangeFinished = {
                    val chosen = WaterDistance.distanceFor(level.roundToInt())
                    if (chosen != distance) onDistance(chosen)
                },
                valueRange = 1f..max.toFloat(),
                enabled = enabled,
            )
        }
    }
}

@Composable
private fun WaterSlideSelector(current: Int?, enabled: Boolean, onSelect: (Int) -> Unit) {
    val range = WaterBoxMode.SLIDE_RANGE
    val reported = current?.takeIf { it in range } ?: WaterBoxMode.SLIDE_MEDIUM
    var position by remember(reported) { mutableFloatStateOf(reported.toFloat()) }
    val code = position.roundToInt().coerceIn(range)
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(stringResource(Res.string.robot_water_flow), style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
            Text(WaterBoxMode.slideText(code)?.text().orEmpty(), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary)
        }
        // Less water on the left, more on the right, as the official slider's two drops show.
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Icon(Icons.Outlined.WaterDrop, contentDescription = stringResource(Res.string.robot_less_water), modifier = Modifier.size(16.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant)
            Slider(
                value = position,
                onValueChange = { position = it },
                // Read the state here: a tap ends before a recomposition, so a captured `code` would be stale.
                onValueChangeFinished = {
                    val chosen = position.roundToInt().coerceIn(range)
                    if (chosen != current) onSelect(chosen)
                },
                valueRange = range.first.toFloat()..range.last.toFloat(),
                steps = range.last - range.first - 1,
                enabled = enabled,
                modifier = Modifier.weight(1f),
            )
            Icon(Icons.Default.WaterDrop, contentDescription = stringResource(Res.string.robot_more_water), modifier = Modifier.size(24.dp),
                tint = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun CountSelector(selected: Int, onSelect: (Int) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalAlignment = Alignment.CenterVertically) {
            Text(stringResource(Res.string.robot_cleaning_count), style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
            Text(stringResource(if (selected == 1) Res.string.robot_once else Res.string.robot_twice), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary)
        }
        SegmentedChoice(2, selected - 1, enabled = true, onSelect = { onSelect(it + 1) }) { index, isSelected ->
            Text("×${index + 1}", style = MaterialTheme.typography.labelLarge,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DockSheetContent(
    status: VacuumStatus,
    busy: Boolean,
    dockSettings: DockSettings,
    dockCapabilities: DockCapabilities,
    onEmptyDustbin: () -> Unit,
    onWashMop: () -> Unit,
    onDryMop: () -> Unit,
    onStopDry: () -> Unit,
    onWashModeChange: (Int) -> Unit,
    onWashFrequencyChange: (Int, Int?) -> Unit,
    onAutoEmptyModeChange: (Int) -> Unit,
) {
    var selectedTab by remember { mutableIntStateOf(0) }

    Column(Modifier.fillMaxWidth().navigationBarsPadding().padding(bottom = 16.dp)) {
        Spacer(Modifier.height(12.dp))
        SecondaryTabRow(selectedTabIndex = selectedTab) {
            listOf(Res.string.robot_dock_control, Res.string.robot_dock_settings).forEachIndexed { i, title ->
                Tab(selected = selectedTab == i, onClick = { selectedTab = i }, text = { Text(stringResource(title)) })
            }
        }
        Box(Modifier.fillMaxWidth().heightIn(min = 340.dp)) {
            when (selectedTab) {
                0 -> DockControlTab(status, busy, onEmptyDustbin, onWashMop, onDryMop, onStopDry)
                1 -> DockSettingsTab(
                    dockSettings, dockCapabilities, status.dockType, status.dockStatus, busy,
                    onWashModeChange, onWashFrequencyChange, onAutoEmptyModeChange,
                )
            }
        }
    }
}

@Composable
private fun DockControlTab(
    status: VacuumStatus,
    busy: Boolean,
    onEmptyDustbin: () -> Unit,
    onWashMop: () -> Unit,
    onDryMop: () -> Unit,
    onStopDry: () -> Unit,
) {
    val waterAttached = status.waterBoxStatus == 1
    val dockError = status.dockErrorStatus
    val drying = status.dryStatus == 1 || status.state == VacuumStateCodes.DRYING_MOP

    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
    ) {
        Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            DockStatusRow(
                label = stringResource(Res.string.robot_clean_water_tank),
                value = when {
                    dockError == null -> stringResource(Res.string.robot_unknown)
                    dockError == DockErrorCodes.WATER_EMPTY -> stringResource(Res.string.robot_empty_refill)
                    else -> stringResource(Res.string.robot_ok)
                },
                color = when {
                    dockError == null -> MaterialTheme.colorScheme.onSurfaceVariant
                    dockError == DockErrorCodes.WATER_EMPTY -> MaterialTheme.colorScheme.error
                    else -> AppColors.Good
                },
            )
            DockStatusRow(
                label = stringResource(Res.string.robot_dirty_water_tank),
                value = when (dockError) {
                    null -> stringResource(Res.string.robot_unknown)
                    DockErrorCodes.DIRTY_TANK_FULL -> stringResource(Res.string.robot_full_empty_it)
                    DockErrorCodes.DIRTY_TANK_LATCH_OPEN -> stringResource(Res.string.robot_latch_open)
                    else -> stringResource(Res.string.robot_ok)
                },
                color = when (dockError) {
                    null -> MaterialTheme.colorScheme.onSurfaceVariant
                    DockErrorCodes.DIRTY_TANK_FULL, DockErrorCodes.DIRTY_TANK_LATCH_OPEN -> MaterialTheme.colorScheme.error
                    else -> AppColors.Good
                },
            )
            DockStatusRow(
                label = stringResource(Res.string.robot_dust_bag),
                value = when (dockError) {
                    null -> stringResource(Res.string.robot_unknown)
                    DockErrorCodes.NO_DUSTBIN -> stringResource(Res.string.robot_missing)
                    DockErrorCodes.DUCT_BLOCKAGE -> stringResource(Res.string.robot_duct_blocked)
                    else -> stringResource(Res.string.robot_ok)
                },
                color = when (dockError) {
                    null -> MaterialTheme.colorScheme.onSurfaceVariant
                    DockErrorCodes.NO_DUSTBIN, DockErrorCodes.DUCT_BLOCKAGE -> MaterialTheme.colorScheme.error
                    else -> AppColors.Good
                },
            )
            if (drying) {
                DockStatusRow(
                    label = stringResource(Res.string.robot_mop_dryer),
                    value = status.remainingDryTimeSec
                        ?.takeIf { it > 0 }
                        ?.let { sec -> stringResource(Res.string.robot_drying_left, (sec + 59) / 60) }
                        ?: stringResource(Res.string.robot_drying),
                    color = AppColors.Warn,
                )
            }
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            DockActionButton(Modifier.weight(1f), Icons.Default.Delete, AppColors.Good, stringResource(Res.string.robot_dock_empty_action), !busy, onEmptyDustbin)
            DockActionButton(Modifier.weight(1f), Icons.Default.WaterDrop, AppColors.Water, stringResource(Res.string.robot_dock_wash_action), !busy && waterAttached, onWashMop)
            if (drying) {
                DockActionButton(Modifier.weight(1f), Icons.Default.Stop, AppColors.Warn, stringResource(Res.string.robot_stop_dry), !busy, onStopDry)
            } else {
                DockActionButton(Modifier.weight(1f), Icons.Default.Air, AppColors.Warn, stringResource(Res.string.robot_dry), !busy, onDryMop)
            }
        }
    }
}

@Composable
private fun DockStatusRow(label: String, value: String, color: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        Box(Modifier.size(8.dp).background(color, CircleShape))
        Text(label, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f))
        Text(value, style = MaterialTheme.typography.bodyMedium, color = color, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun DockActionButton(modifier: Modifier = Modifier, icon: ImageVector, iconTint: Color, label: String, enabled: Boolean, onClick: () -> Unit) {
    Surface(onClick = onClick, modifier = modifier, enabled = enabled, shape = MaterialTheme.shapes.extraLarge, color = MaterialTheme.colorScheme.surfaceContainerHigh) {
        Column(modifier = Modifier.padding(vertical = 14.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Surface(shape = CircleShape, color = if (enabled) iconTint.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant, modifier = Modifier.size(40.dp)) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(icon, null, modifier = Modifier.size(20.dp), tint = if (enabled) iconTint else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.38f))
                }
            }
            Text(label, style = MaterialTheme.typography.labelMedium, color = if (enabled) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.38f))
        }
    }
}


@Composable
private fun DockSettingsTab(
    dockSettings: DockSettings,
    dockCapabilities: DockCapabilities,
    dockType: Int?,
    dockStatus: Int?,
    busy: Boolean,
    onWashModeChange: (Int) -> Unit,
    onWashFrequencyChange: (Int, Int?) -> Unit,
    onAutoEmptyModeChange: (Int) -> Unit,
) {
    var dialogTarget by remember { mutableStateOf<String?>(null) }
    val washFrequencyOptions = dockCapabilities.washFrequencyOptions
    val smartWash = dockSettings.smartWash
    val washFrequencyIndex = washFrequencyOptions.indexOfFirst {
        smartWash != null && it.mode == smartWash.mode && (it.mode != 0 || it.intervalMinutes?.times(60) == smartWash.intervalSeconds)
    }
    val washModes = dockCapabilities.washModes(dockType, dockStatus)
    val washModeIndex = washModes.indexOfFirst { it.first == dockCapabilities.washModeCode(dockSettings.washMode, dockType) }
    val autoEmptyModes = dockCapabilities.autoEmptyModes
    val dockKnown = dockType != null && dockType != 0
    val washFrequencyEnabled = !busy && dockKnown && smartWash != null && washFrequencyOptions.isNotEmpty()
    val washModeEnabled = !busy && dockKnown && dockSettings.washMode != null && washModes.isNotEmpty()
    val autoEmptyEnabled = !busy && dockSettings.autoEmptyMode != null && autoEmptyModes.isNotEmpty()

    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            DockSettingCard(Modifier.weight(1f), Icons.Default.Repeat, AppColors.Water, stringResource(Res.string.robot_mop_wash_frequency), washFrequencyText(smartWash).text(), washFrequencyEnabled) { dialogTarget = "wash_freq" }
            DockSettingCard(Modifier.weight(1f), Icons.Default.WaterDrop, AppColors.Water, stringResource(Res.string.robot_washing_mode), dockCapabilities.washModeText(dockSettings.washMode, dockType).text(), washModeEnabled) { dialogTarget = "wash_mode" }
        }
        DockSettingCard(Modifier.fillMaxWidth(), Icons.Default.Delete, MaterialTheme.colorScheme.primary, stringResource(Res.string.robot_auto_empty_mode), dockCapabilities.autoEmptyModeText(dockSettings.autoEmptyMode).text(), autoEmptyEnabled) { dialogTarget = "auto_empty" }
    }

    when (dialogTarget) {
        "wash_freq" -> RadioPickerDialog(
            title = stringResource(Res.string.robot_mop_wash_frequency),
            options = washFrequencyOptions.map { it.text.text() },
            selected = washFrequencyIndex,
            onSelect = { washFrequencyOptions[it].let { option -> onWashFrequencyChange(option.mode, option.intervalMinutes) } },
            enabled = washFrequencyEnabled,
            onDismiss = { dialogTarget = null },
        )
        "wash_mode" -> RadioPickerDialog(
            title = stringResource(Res.string.robot_washing_mode),
            options = washModes.map { it.second.text() },
            selected = washModeIndex,
            onSelect = { onWashModeChange(washModes[it].first) },
            enabled = washModeEnabled,
            onDismiss = { dialogTarget = null },
        )
        "auto_empty" -> RadioPickerDialog(
            title = stringResource(Res.string.robot_auto_empty_mode),
            options = autoEmptyModes.map { it.second.text() },
            selected = autoEmptyModes.indexOfFirst { it.first == dockSettings.autoEmptyMode },
            onSelect = { onAutoEmptyModeChange(autoEmptyModes[it].first) },
            enabled = autoEmptyEnabled,
            onDismiss = { dialogTarget = null },
        )
    }
}

@Composable
private fun DockSettingCard(modifier: Modifier, icon: ImageVector, iconTint: Color, title: String, value: String, enabled: Boolean, onClick: () -> Unit) {
    Surface(onClick = onClick, modifier = modifier, enabled = enabled, shape = MaterialTheme.shapes.extraLarge, color = MaterialTheme.colorScheme.surfaceContainerHigh) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Surface(shape = CircleShape, color = iconTint.copy(alpha = 0.15f), modifier = Modifier.size(36.dp)) {
                Box(contentAlignment = Alignment.Center) { Icon(icon, null, Modifier.size(18.dp), iconTint) }
            }
            Text(title, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, maxLines = 2)
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(value, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold, modifier = Modifier.weight(1f), maxLines = 1)
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null, Modifier.size(14.dp), MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
private fun RadioPickerDialog(title: String, options: List<String>, selected: Int, onSelect: (Int) -> Unit, enabled: Boolean, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Column(Modifier.verticalScroll(rememberScrollState())) {
                options.forEachIndexed { index, label ->
                    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp)) {
                        RadioButton(selected = selected == index, enabled = enabled, onClick = { onSelect(index); onDismiss() })
                        Spacer(Modifier.width(8.dp))
                        Text(label, style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        },
        confirmButton = { TextButton(onClick = onDismiss) { Text(stringResource(Res.string.robot_close)) } },
    )
}


@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun RoomsSheetContent(
    rooms: List<ParsedMapRoom>,
    selectedRoomIds: List<Int>,
    busy: Boolean,
    onRoomToggle: (Int) -> Unit,
    onCleanRooms: () -> Unit,
    onLoadMap: () -> Unit,
    onDefaultOrder: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState()).padding(horizontal = 24.dp).navigationBarsPadding().padding(bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(stringResource(Res.string.robot_select_rooms), style = MaterialTheme.typography.headlineSmall)
        if (rooms.isEmpty()) {
            Text(stringResource(Res.string.robot_load_map_first), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            OutlinedButton(onClick = onLoadMap, modifier = Modifier.fillMaxWidth()) {
                Icon(Icons.Default.Refresh, null); Spacer(Modifier.width(8.dp)); Text(stringResource(Res.string.robot_load_map))
            }
        } else {
            val hint = if (selectedRoomIds.isEmpty())
                stringResource(Res.string.robot_tap_rooms_hint)
            else
                stringResource(Res.string.robot_rooms_selected_hint, selectedRoomIds.size)
            Text(hint, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                rooms.forEach { room ->
                    val order = selectedRoomIds.indexOf(room.id).takeIf { it >= 0 }?.plus(1)
                    FilterChip(
                        selected = room.id in selectedRoomIds,
                        onClick = { onRoomToggle(room.id) },
                        enabled = !busy,
                        label = {
                            if (order != null) {
                                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Surface(shape = CircleShape, color = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp)) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Text("$order", style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onPrimary)
                                        }
                                    }
                                    Text(room.name)
                                }
                            } else {
                                Text(room.name)
                            }
                        },
                    )
                }
            }
            Button(onClick = onCleanRooms, enabled = !busy && selectedRoomIds.isNotEmpty(), modifier = Modifier.fillMaxWidth().height(56.dp), shape = MaterialTheme.shapes.extraLarge) {
                Icon(Icons.Default.PlayArrow, null)
                Spacer(Modifier.width(8.dp))
                Text(stringResource(Res.string.robot_clean_selected_rooms))
            }
            TextButton(onClick = onDefaultOrder, enabled = !busy, modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(Res.string.robot_default_order))
            }
        }
    }
}

/**
 * The order a full clean goes through the rooms in. Starts from the saved order, with rooms it
 * does not name added at the end; arrows move a room up or down.
 */
@Composable
private fun CleanOrderSheetContent(
    rooms: List<ParsedMapRoom>,
    savedOrder: List<Int>,
    busy: Boolean,
    onSave: (List<Int>) -> Unit,
    onRemove: () -> Unit,
) {
    val initial = remember(rooms, savedOrder) {
        val onMap = rooms.map { it.id }
        (savedOrder.filter { it in onMap } + onMap).distinct()
    }
    var order by remember(initial) { mutableStateOf(initial) }
    val names = remember(rooms) { rooms.associate { it.id to it.name } }
    fun move(from: Int, to: Int) {
        order = order.toMutableList().apply { add(to, removeAt(from)) }
    }

    Column(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp).navigationBarsPadding().padding(bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(stringResource(Res.string.robot_default_order), style = MaterialTheme.typography.headlineSmall)
        Text(
            if (rooms.isEmpty()) stringResource(Res.string.robot_load_map_first)
            else stringResource(Res.string.robot_default_order_text),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Column(Modifier.weight(1f, fill = false).verticalScroll(rememberScrollState())) {
            order.forEachIndexed { index, roomId ->
                if (index > 0) HorizontalDivider()
                Row(Modifier.fillMaxWidth().padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                    Surface(shape = CircleShape, color = MaterialTheme.colorScheme.primary, modifier = Modifier.size(24.dp)) {
                        Box(contentAlignment = Alignment.Center) {
                            Text("${index + 1}", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onPrimary)
                        }
                    }
                    Text(
                        names[roomId] ?: stringResource(Res.string.robot_room_fallback, roomId),
                        modifier = Modifier.weight(1f).padding(horizontal = 12.dp),
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    val movedRoom = names[roomId] ?: stringResource(Res.string.robot_room_generic)
                    IconButton(onClick = { move(index, index - 1) }, enabled = !busy && index > 0) {
                        Icon(Icons.Default.KeyboardArrowUp, contentDescription = stringResource(Res.string.robot_move_up, movedRoom))
                    }
                    IconButton(onClick = { move(index, index + 1) }, enabled = !busy && index < order.lastIndex) {
                        Icon(Icons.Default.KeyboardArrowDown, contentDescription = stringResource(Res.string.robot_move_down, movedRoom))
                    }
                }
            }
        }
        if (rooms.isNotEmpty()) {
            Button(
                onClick = { onSave(order) },
                enabled = !busy && order != savedOrder,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = MaterialTheme.shapes.extraLarge,
            ) {
                Text(stringResource(Res.string.robot_save_order))
            }
        }
        if (savedOrder.isNotEmpty()) {
            TextButton(onClick = onRemove, enabled = !busy, modifier = Modifier.fillMaxWidth()) {
                Text(stringResource(Res.string.robot_remove_order))
            }
        }
    }
}


@Composable
private fun MapsSheetContent(
    floorMaps: List<FloorMap>,
    currentFloorFlag: Int,
    loading: Boolean,
    busy: Boolean,
    blockedReason: String?,
    canAddMap: Boolean,
    unsavedMap: Boolean,
    mapSaving: Boolean?,
    multiLevel: Boolean?,
    smartSwitching: Boolean?,
    onMultiLevelChange: (Boolean) -> Unit,
    onSmartSwitchingChange: (Boolean) -> Unit,
    onEnableMapSaving: () -> Unit,
    onLoad: (Int) -> Unit,
    onRename: (FloorMap) -> Unit,
    onDelete: (FloorMap) -> Unit,
    onBackup: (FloorMap, picture: ParsedMap?) -> Unit,
    onRestore: (FloorMap, backupPicture: ParsedMap?) -> Unit,
    loadSavedMap: suspend (mapFlag: Int, backup: Boolean) -> ParsedMap,
    onStartMapping: () -> Unit,
    onSaveNewMap: () -> Unit,
    onRecheck: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState()).padding(horizontal = 24.dp).navigationBarsPadding().padding(bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(stringResource(Res.string.robot_action_maps), style = MaterialTheme.typography.headlineSmall)
        Text(
            stringResource(Res.string.robot_maps_intro),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        if (blockedReason != null) {
            Text(blockedReason, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.error)
        }
        val canManage = !busy && !loading && blockedReason == null

        // A picture of each saved map and its backup, fetched when the sheet opens and again for a
        // map or backup the robot reports as changed.
        val previews = remember { mutableStateMapOf<String, ParsedMap>() }
        val failedPreviews = remember { mutableStateMapOf<String, Boolean>() }
        LaunchedEffect(floorMaps) {
            for (floor in floorMaps) {
                for (backup in listOf(false, true)) {
                    if (backup && floor.backupAt == null) continue
                    val key = previewKey(floor, backup)
                    if (key in previews) continue
                    failedPreviews.remove(key)
                    try {
                        previews[key] = loadSavedMap(floor.mapFlag, backup)
                    } catch (e: CancellationException) {
                        throw e
                    } catch (e: Exception) {
                        failedPreviews[key] = true
                    }
                }
            }
        }

        // A robot that holds saved maps keeps maps, whatever its status says.
        if (mapSaving == false && floorMaps.isEmpty() && !loading) {
            Surface(shape = MaterialTheme.shapes.large, color = MaterialTheme.colorScheme.surfaceContainerHigh) {
                Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(stringResource(Res.string.robot_map_saving_off), style = MaterialTheme.typography.titleSmall)
                    Text(
                        stringResource(Res.string.robot_map_saving_off_text),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    OutlinedButton(onClick = onEnableMapSaving, enabled = canManage, modifier = Modifier.fillMaxWidth()) {
                        Text(stringResource(Res.string.robot_turn_on_map_saving))
                    }
                }
            }
        }

        if (multiLevel != null) {
            SettingSwitchRow(
                title = stringResource(Res.string.robot_multi_level),
                subtitle = stringResource(Res.string.robot_multi_level_text),
                checked = multiLevel,
                enabled = canManage,
                onCheckedChange = onMultiLevelChange,
            )
            if (multiLevel && smartSwitching != null) {
                SettingSwitchRow(
                    title = stringResource(Res.string.robot_smart_floor),
                    subtitle = stringResource(Res.string.robot_smart_floor_text),
                    checked = smartSwitching,
                    enabled = canManage,
                    onCheckedChange = onSmartSwitchingChange,
                )
            }
            HorizontalDivider()
        }

        if (unsavedMap) {
            Surface(shape = MaterialTheme.shapes.large, color = MaterialTheme.colorScheme.primaryContainer) {
                Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(stringResource(Res.string.robot_new_map_unsaved), style = MaterialTheme.typography.titleSmall)
                    Text(
                        stringResource(Res.string.robot_new_map_unsaved_text),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    Button(onClick = onSaveNewMap, enabled = canManage, modifier = Modifier.fillMaxWidth()) {
                        Text(stringResource(Res.string.robot_save_new_map))
                    }
                }
            }
        }

        when {
            loading && floorMaps.isEmpty() -> {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                    Text(stringResource(Res.string.robot_checking_maps), style = MaterialTheme.typography.bodyMedium)
                }
            }

            floorMaps.isEmpty() -> {
                Text(stringResource(Res.string.robot_no_saved_maps), style = MaterialTheme.typography.titleSmall)
                Text(
                    stringResource(Res.string.robot_no_saved_maps_text),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Button(onClick = onStartMapping, enabled = canManage && !unsavedMap, modifier = Modifier.fillMaxWidth()) {
                    Icon(Icons.Default.Map, null); Spacer(Modifier.width(8.dp)); Text(stringResource(Res.string.robot_map_the_home))
                }
                OutlinedButton(onClick = onRecheck, enabled = !busy, modifier = Modifier.fillMaxWidth()) {
                    Icon(Icons.Default.Refresh, null); Spacer(Modifier.width(8.dp)); Text(stringResource(Res.string.robot_recheck))
                }
            }

            else -> {
                floorMaps.forEach { floor ->
                    val selected = floor.mapFlag == currentFloorFlag
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(floor.name, style = MaterialTheme.typography.bodyLarge)
                            if (selected) {
                                Text(stringResource(Res.string.robot_currently_loaded), style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.primary)
                            }
                            Text(
                                floor.backupAt?.let { stringResource(Res.string.robot_backup_from, formatCleanRecordTime(it)) } ?: stringResource(Res.string.robot_no_backup),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                        IconButton(onClick = { onRename(floor) }, enabled = canManage) {
                            Icon(Icons.Default.Edit, contentDescription = stringResource(Res.string.robot_rename_named, floor.name))
                        }
                        IconButton(onClick = { onDelete(floor) }, enabled = canManage) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = stringResource(Res.string.robot_delete_named, floor.name),
                                tint = if (canManage) MaterialTheme.colorScheme.error else LocalContentColor.current,
                            )
                        }
                        // Loading only matters when there is another map to switch to.
                        if (floorMaps.size > 1) {
                            Button(onClick = { onLoad(floor.mapFlag) }, enabled = !busy && !selected) {
                                Text(if (selected) stringResource(Res.string.robot_loaded) else stringResource(Res.string.robot_load))
                            }
                        }
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        SavedMapPreview(
                            label = stringResource(Res.string.robot_preview_map),
                            map = previews[previewKey(floor, backup = false)],
                            failed = failedPreviews[previewKey(floor, backup = false)] == true,
                            modifier = Modifier.weight(1f),
                        )
                        if (floor.backupAt != null) {
                            SavedMapPreview(
                                label = stringResource(Res.string.robot_preview_backup),
                                map = previews[previewKey(floor, backup = true)],
                                failed = failedPreviews[previewKey(floor, backup = true)] == true,
                                modifier = Modifier.weight(1f),
                            )
                        } else {
                            Spacer(Modifier.weight(1f))
                        }
                    }
                    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally)) {
                        OutlinedButton(onClick = { onBackup(floor, previews[previewKey(floor, backup = false)]) }, enabled = canManage) {
                            Text(stringResource(Res.string.robot_back_up_now))
                        }
                        // Restoring was only captured for the loaded map.
                        OutlinedButton(onClick = { onRestore(floor, previews[previewKey(floor, backup = true)]) }, enabled = canManage && selected && floor.backupAt != null) {
                            Text(stringResource(Res.string.robot_restore_backup))
                        }
                    }
                    HorizontalDivider()
                }
                if (canAddMap) {
                    OutlinedButton(onClick = onStartMapping, enabled = canManage && !unsavedMap, modifier = Modifier.fillMaxWidth()) {
                        Icon(Icons.Default.Map, null); Spacer(Modifier.width(8.dp)); Text(stringResource(Res.string.robot_map_another_floor))
                    }
                }
                TextButton(onClick = onRecheck, enabled = !busy, modifier = Modifier.fillMaxWidth()) {
                    Icon(Icons.Default.Refresh, null); Spacer(Modifier.width(8.dp)); Text(stringResource(Res.string.robot_recheck))
                }
            }
        }
    }
}

@Composable
private fun RenameMapDialog(floor: FloorMap, onConfirm: (String) -> Unit, onDismiss: () -> Unit) {
    var name by remember(floor) { mutableStateOf(floor.name) }
    val problem = mapNameBlocker(name.trim())
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(Res.string.robot_rename_map)) },
        text = {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                singleLine = true,
                label = { Text(stringResource(Res.string.robot_map_name)) },
                isError = problem != null && name.isNotEmpty(),
                supportingText = { if (problem != null && name.isNotEmpty()) Text(problem) },
            )
        },
        confirmButton = {
            TextButton(onClick = { onConfirm(name.trim()) }, enabled = problem == null && name.trim() != floor.name) {
                Text(stringResource(Res.string.robot_rename))
            }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text(stringResource(Res.string.common_cancel)) } },
    )
}

@Composable
private fun SettingSwitchRow(
    title: String,
    subtitle: String,
    checked: Boolean,
    enabled: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Column(Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.bodyLarge)
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Switch(checked = checked, onCheckedChange = onCheckedChange, enabled = enabled)
    }
}

/** Turning multi-level maps off keeps one map; the robot deletes the others (captured). */
@Composable
private fun KeepMapDialog(
    floorMaps: List<FloorMap>,
    currentFloorFlag: Int,
    onConfirm: (Int) -> Unit,
    onDismiss: () -> Unit,
) {
    var keep by remember(floorMaps) {
        mutableStateOf(floorMaps.find { it.mapFlag == currentFloorFlag }?.mapFlag ?: floorMaps.first().mapFlag)
    }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(Res.string.robot_keep_title)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    stringResource(Res.string.robot_keep_text),
                )
                floorMaps.forEach { floor ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        RadioButton(selected = keep == floor.mapFlag, onClick = { keep = floor.mapFlag })
                        Text(floor.name, style = MaterialTheme.typography.bodyLarge)
                    }
                }
            }
        },
        confirmButton = {
            TextButton(
                onClick = { onConfirm(keep) },
                colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error),
            ) { Text(stringResource(Res.string.robot_keep_confirm)) }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text(stringResource(Res.string.common_cancel)) } },
    )
}

/** Changes when the robot stores the map or its backup again, so a new picture is fetched. */
private fun previewKey(floor: FloorMap, backup: Boolean): String =
    if (backup) "${floor.mapFlag}:backup:${floor.backupAt}" else "${floor.mapFlag}:map:${floor.savedAt}"

/** A small picture of a saved map or its backup in the map list. */
@Composable
private fun SavedMapPreview(label: String, map: ParsedMap?, failed: Boolean, modifier: Modifier = Modifier) {
    Column(modifier, verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(MaterialTheme.shapes.medium)
                .background(MaterialTheme.colorScheme.surfaceContainerHigh),
            contentAlignment = Alignment.Center,
        ) {
            when {
                map != null -> SavedMapImage(map, Modifier.fillMaxSize().padding(6.dp))
                failed -> Text(stringResource(Res.string.robot_couldnt_load), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                else -> CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
            }
        }
    }
}

/** The floor plan of [map], cut to the part that holds floor and fitted into the space. */
@Composable
internal fun SavedMapImage(map: ParsedMap, modifier: Modifier = Modifier) {
    val bitmap = rememberMapBitmap(map)
    val bounds = remember(map) { map.contentBoundsNorm() }
    Canvas(modifier) {
        val usable = bounds.isUsable
        val srcLeft = if (usable) bounds.left * bitmap.width else 0f
        val srcTop = if (usable) bounds.top * bitmap.height else 0f
        val srcW = (if (usable) bounds.width * bitmap.width else bitmap.width.toFloat()).coerceAtLeast(1f)
        val srcH = (if (usable) bounds.height * bitmap.height else bitmap.height.toFloat()).coerceAtLeast(1f)
        val scale = minOf(size.width / srcW, size.height / srcH)
        val dstW = srcW * scale
        val dstH = srcH * scale
        drawImage(
            image = bitmap,
            srcOffset = IntOffset(srcLeft.toInt(), srcTop.toInt()),
            srcSize = IntSize(srcW.toInt(), srcH.toInt()),
            dstOffset = IntOffset(((size.width - dstW) / 2).toInt(), ((size.height - dstH) / 2).toInt()),
            dstSize = IntSize(dstW.toInt(), dstH.toInt()),
            filterQuality = FilterQuality.Low,
        )
    }
}

@Composable
private fun BackupMapDialog(floor: FloorMap, picture: ParsedMap?, onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(Res.string.robot_backup_title, floor.name)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                // The map as the robot will save it; absent while the sheet is still fetching it.
                picture?.let { DialogMapPicture(it) }
                Text(
                    floor.backupAt?.let { stringResource(Res.string.robot_backup_text_replace, formatCleanRecordTime(it)) }
                        ?: stringResource(Res.string.robot_backup_text),
                )
            }
        },
        confirmButton = { TextButton(onClick = onConfirm) { Text(stringResource(Res.string.robot_back_up)) } },
        dismissButton = { TextButton(onClick = onDismiss) { Text(stringResource(Res.string.common_cancel)) } },
    )
}

@Composable
private fun RestoreBackupDialog(floor: FloorMap, picture: ParsedMap?, onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(Res.string.robot_restore_title, floor.name)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                // The backup the map goes back to; absent while the sheet is still fetching it.
                picture?.let { DialogMapPicture(it) }
                Text(
                    stringResource(
                        Res.string.robot_restore_text,
                        floor.backupAt?.let { formatCleanRecordTime(it) } ?: stringResource(Res.string.robot_earlier),
                    ),
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error),
            ) { Text(stringResource(Res.string.robot_restore)) }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text(stringResource(Res.string.common_cancel)) } },
    )
}

/** A saved map's picture at the top of a backup or restore dialog. */
@Composable
private fun DialogMapPicture(map: ParsedMap) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .clip(MaterialTheme.shapes.medium)
            .background(MaterialTheme.colorScheme.surfaceContainerHigh),
    ) {
        SavedMapImage(map, Modifier.fillMaxSize().padding(8.dp))
    }
}

@Composable
private fun DeleteMapDialog(floor: FloorMap, onConfirm: () -> Unit, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        icon = { Icon(Icons.Default.Delete, null, tint = MaterialTheme.colorScheme.error) },
        title = { Text(stringResource(Res.string.robot_delete_title, floor.name)) },
        text = {
            Text(
                stringResource(Res.string.robot_delete_text),
            )
        },
        confirmButton = {
            TextButton(
                onClick = onConfirm,
                colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error),
            ) { Text(stringResource(Res.string.robot_delete_map)) }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text(stringResource(Res.string.common_cancel)) } },
    )
}


@Composable
private fun batteryColor(battery: Int?): Color = when {
    battery == null -> MaterialTheme.colorScheme.onSurfaceVariant
    battery <= 15 -> MaterialTheme.colorScheme.error
    battery <= 40 -> AppColors.Warn
    else -> AppColors.Good
}

@Composable
private fun stateLabel(state: Int?): String =
    if (state == null) "—" else VacuumStateCodes.text(state).text()

@Composable
private fun dryingLabel(remainingDrySec: Long?): String {
    val left = remainingDrySec?.takeIf { it > 0 }?.let { s ->
        val m = s / 60
        if (m >= 60) stringResource(Res.string.robot_left_hm, m / 60, m % 60) else stringResource(Res.string.robot_left_m, m)
    }
    return if (left != null) stringResource(Res.string.robot_drying_mop_left, left) else stringResource(Res.string.robot_drying_mop)
}


private fun isBusyState(state: Int?): Boolean = when (state) {
    null,
    VacuumStateCodes.IDLE,
    VacuumStateCodes.NOT_CHARGING,
    VacuumStateCodes.CHARGING,
    VacuumStateCodes.CHARGER_DISCONNECTED,
    VacuumStateCodes.SHUTTING_DOWN -> false
    else -> true
}

@Composable
private fun PulsingDot(color: Color, modifier: Modifier = Modifier) {
    val transition = rememberInfiniteTransition(label = "pulse")
    val alpha by transition.animateFloat(
        initialValue = 1f,
        targetValue = 0.3f,
        animationSpec = infiniteRepeatable(tween(700, easing = FastOutSlowInEasing), RepeatMode.Reverse),
        label = "alpha",
    )
    Box(
        modifier = modifier
            .size(8.dp)
            .background(color.copy(alpha = alpha), CircleShape),
    )
}

@Composable
private fun stateTint(state: Int?): Color = when (state) {
    VacuumStateCodes.WASHING_MOP, VacuumStateCodes.WASHING_MOP_2, VacuumStateCodes.GOING_TO_WASH_MOP,
    VacuumStateCodes.DRYING_MOP,
    VacuumStateCodes.RETURNING_TO_DOCK_FOR_DRYING -> MaterialTheme.colorScheme.tertiary
    VacuumStateCodes.ERROR, VacuumStateCodes.CHARGING_ERROR -> MaterialTheme.colorScheme.error
    VacuumStateCodes.CLEANING, VacuumStateCodes.SEGMENT_CLEANING,
    VacuumStateCodes.ZONED_CLEANING, VacuumStateCodes.SPOT_CLEANING,
    VacuumStateCodes.STARTING, VacuumStateCodes.MANUAL_MODE,
    VacuumStateCodes.REMOTE_CONTROL_ACTIVE, VacuumStateCodes.PAUSED,
    VacuumStateCodes.RETURNING_HOME, VacuumStateCodes.DOCKING,
    VacuumStateCodes.GOING_TO_TARGET, VacuumStateCodes.MAPPING -> MaterialTheme.colorScheme.primary
    else -> MaterialTheme.colorScheme.onSurfaceVariant
}
