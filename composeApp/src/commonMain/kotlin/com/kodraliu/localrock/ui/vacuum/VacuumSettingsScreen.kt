package com.kodraliu.localrock.ui.vacuum

import com.kodraliu.localrock.resources.timeline_pref_coverage_less_collision
import com.kodraliu.localrock.resources.timeline_pref_coverage_high
import com.kodraliu.localrock.resources.timeline_pref_path_switch
import com.kodraliu.localrock.resources.timeline_pref_path_floor
import com.kodraliu.localrock.resources.timeline_pref_path_ai
import com.kodraliu.localrock.resources.timeline_pref_order_default
import com.kodraliu.localrock.resources.timeline_pref_order_custom
import com.kodraliu.localrock.resources.timeline_pref_order_sequential
import com.kodraliu.localrock.resources.timeline_pref_home_perception
import org.jetbrains.compose.resources.StringResource
import com.kodraliu.localrock.shared.vacuum.CleanPreference
import com.kodraliu.localrock.resources.Res
import com.kodraliu.localrock.resources.settings_after_cleaning
import com.kodraliu.localrock.resources.settings_also_advertised
import com.kodraliu.localrock.resources.settings_at_set_time
import com.kodraliu.localrock.resources.settings_auto_empties_since_reset
import com.kodraliu.localrock.resources.settings_auto_empty_mode
import com.kodraliu.localrock.resources.settings_back
import com.kodraliu.localrock.resources.settings_beeping
import com.kodraliu.localrock.resources.settings_beta_checking
import com.kodraliu.localrock.resources.settings_beta_features
import com.kodraliu.localrock.resources.settings_beta_intro
import com.kodraliu.localrock.resources.settings_beta_none
import com.kodraliu.localrock.resources.settings_beta_sub
import com.kodraliu.localrock.resources.settings_bin_emptied
import com.kodraliu.localrock.resources.settings_bit
import com.kodraliu.localrock.resources.settings_cancel
import com.kodraliu.localrock.resources.settings_care_how
import com.kodraliu.localrock.resources.settings_carpet_cleaning
import com.kodraliu.localrock.resources.settings_carpet_mode
import com.kodraliu.localrock.resources.settings_carpet_sub
import com.kodraliu.localrock.resources.settings_child_lock
import com.kodraliu.localrock.resources.settings_child_lock_off
import com.kodraliu.localrock.resources.settings_child_lock_on
import com.kodraliu.localrock.resources.settings_clean
import com.kodraliu.localrock.resources.settings_cleaning_history
import com.kodraliu.localrock.resources.settings_cleans_count
import com.kodraliu.localrock.resources.settings_close
import com.kodraliu.localrock.resources.settings_daily_at
import com.kodraliu.localrock.resources.settings_dnd
import com.kodraliu.localrock.resources.settings_dnd_add_period
import com.kodraliu.localrock.resources.settings_dnd_anytime
import com.kodraliu.localrock.resources.settings_dnd_delete_period
import com.kodraliu.localrock.resources.settings_dnd_enable
import com.kodraliu.localrock.resources.settings_dnd_multi_intro
import com.kodraliu.localrock.resources.settings_dnd_note
import com.kodraliu.localrock.resources.settings_dnd_sub
import com.kodraliu.localrock.resources.settings_dnd_tap_change
import com.kodraliu.localrock.resources.settings_dnd_tap_close
import com.kodraliu.localrock.resources.settings_dock_settings
import com.kodraliu.localrock.resources.settings_dock_settings_title
import com.kodraliu.localrock.resources.settings_dock_sub
import com.kodraliu.localrock.resources.settings_dry_mops_at
import com.kodraliu.localrock.resources.settings_drying_time
import com.kodraliu.localrock.resources.settings_dust_bag
import com.kodraliu.localrock.resources.settings_empty
import com.kodraliu.localrock.resources.settings_empty_tank_text
import com.kodraliu.localrock.resources.settings_empty_tank_title
import com.kodraliu.localrock.resources.settings_emptying_tank
import com.kodraliu.localrock.resources.settings_end
import com.kodraliu.localrock.resources.settings_end_time
import com.kodraliu.localrock.resources.settings_feature
import com.kodraliu.localrock.resources.settings_filter
import com.kodraliu.localrock.resources.settings_find_robot
import com.kodraliu.localrock.resources.settings_find_robot_sub
import com.kodraliu.localrock.resources.settings_firmware_version
import com.kodraliu.localrock.resources.settings_floor_cleaning
import com.kodraliu.localrock.resources.settings_floor_sub
import com.kodraliu.localrock.resources.settings_fw_available
import com.kodraliu.localrock.resources.settings_fw_check
import com.kodraliu.localrock.resources.settings_fw_check_sub
import com.kodraliu.localrock.resources.settings_fw_checking
import com.kodraliu.localrock.resources.settings_fw_installed
import com.kodraliu.localrock.resources.settings_fw_required
import com.kodraliu.localrock.resources.settings_fw_unavailable
import com.kodraliu.localrock.resources.settings_fw_up_to_date
import com.kodraliu.localrock.resources.settings_fw_updates
import com.kodraliu.localrock.resources.settings_go
import com.kodraliu.localrock.resources.settings_guide_dust_bag
import com.kodraliu.localrock.resources.settings_guide_filter
import com.kodraliu.localrock.resources.settings_guide_main_brush
import com.kodraliu.localrock.resources.settings_guide_maintenance_brush
import com.kodraliu.localrock.resources.settings_guide_sensor
import com.kodraliu.localrock.resources.settings_guide_side_brush
import com.kodraliu.localrock.resources.settings_guide_strainer
import com.kodraliu.localrock.resources.settings_hide
import com.kodraliu.localrock.resources.settings_history
import com.kodraliu.localrock.resources.settings_hour
import com.kodraliu.localrock.resources.settings_install
import com.kodraliu.localrock.resources.settings_internal_tank
import com.kodraliu.localrock.resources.settings_internal_tank_sub
import com.kodraliu.localrock.resources.settings_last_clean_covered
import com.kodraliu.localrock.resources.settings_last_clean_took
import com.kodraliu.localrock.resources.settings_led_off
import com.kodraliu.localrock.resources.settings_led_on
import com.kodraliu.localrock.resources.settings_left
import com.kodraliu.localrock.resources.settings_lifetime_stats
import com.kodraliu.localrock.resources.settings_live_view
import com.kodraliu.localrock.resources.settings_live_view_sub
import com.kodraliu.localrock.resources.settings_load_history
import com.kodraliu.localrock.resources.settings_main_brush
import com.kodraliu.localrock.resources.settings_maint_load_failed
import com.kodraliu.localrock.resources.settings_maintenance
import com.kodraliu.localrock.resources.settings_maintenance_brush
import com.kodraliu.localrock.resources.settings_maintenance_sub
import com.kodraliu.localrock.resources.settings_map_not_loaded
import com.kodraliu.localrock.resources.settings_map_not_loaded_text
import com.kodraliu.localrock.resources.settings_maps
import com.kodraliu.localrock.resources.settings_maps_sub
import com.kodraliu.localrock.resources.settings_minute
import com.kodraliu.localrock.resources.settings_model
import com.kodraliu.localrock.resources.settings_mop_drying
import com.kodraliu.localrock.resources.settings_mop_drying_off
import com.kodraliu.localrock.resources.settings_mop_drying_on
import com.kodraliu.localrock.resources.settings_mop_wash_frequency
import com.kodraliu.localrock.resources.settings_mop_washes_since_reset
import com.kodraliu.localrock.resources.settings_more_options
import com.kodraliu.localrock.resources.settings_more_options_sub
import com.kodraliu.localrock.resources.settings_no_cleans
import com.kodraliu.localrock.resources.settings_no_cruises
import com.kodraliu.localrock.resources.settings_no_history
import com.kodraliu.localrock.resources.settings_no_history_text
import com.kodraliu.localrock.resources.settings_no_map_for_clean
import com.kodraliu.localrock.resources.settings_no_timeline
import com.kodraliu.localrock.resources.settings_nothing_found
import com.kodraliu.localrock.resources.settings_obstacle_avoidance
import com.kodraliu.localrock.resources.settings_obstacle_sub
import com.kodraliu.localrock.resources.settings_off
import com.kodraliu.localrock.resources.settings_off_peak_charging
import com.kodraliu.localrock.resources.settings_off_peak_electricity
import com.kodraliu.localrock.resources.settings_pets
import com.kodraliu.localrock.resources.settings_pets_sub
import com.kodraliu.localrock.resources.settings_pin_arrived
import com.kodraliu.localrock.resources.settings_pin_go
import com.kodraliu.localrock.resources.settings_pin_go_sub
import com.kodraliu.localrock.resources.settings_pin_sent
import com.kodraliu.localrock.resources.settings_pin_tap_map
import com.kodraliu.localrock.resources.settings_pin_target_set
import com.kodraliu.localrock.resources.settings_rc_backward
import com.kodraliu.localrock.resources.settings_rc_each_tap
import com.kodraliu.localrock.resources.settings_rc_forward
import com.kodraliu.localrock.resources.settings_rc_left
import com.kodraliu.localrock.resources.settings_rc_right
import com.kodraliu.localrock.resources.settings_rc_stop
import com.kodraliu.localrock.resources.settings_rc_tap
import com.kodraliu.localrock.resources.settings_refresh
import com.kodraliu.localrock.resources.settings_remote_control
import com.kodraliu.localrock.resources.settings_remote_control_title
import com.kodraliu.localrock.resources.settings_remote_sub
import com.kodraliu.localrock.resources.settings_replace
import com.kodraliu.localrock.resources.settings_report_github
import com.kodraliu.localrock.resources.settings_reset
import com.kodraliu.localrock.resources.settings_reset_text
import com.kodraliu.localrock.resources.settings_reset_title
import com.kodraliu.localrock.resources.settings_retry
import com.kodraliu.localrock.resources.settings_robot_reply
import com.kodraliu.localrock.resources.settings_robot_settings
import com.kodraliu.localrock.resources.settings_save
import com.kodraliu.localrock.resources.settings_schedules
import com.kodraliu.localrock.resources.settings_schedules_sub
import com.kodraliu.localrock.resources.settings_search
import com.kodraliu.localrock.resources.settings_sensor
import com.kodraliu.localrock.resources.settings_sessions
import com.kodraliu.localrock.resources.settings_show
import com.kodraliu.localrock.resources.settings_side_brush
import com.kodraliu.localrock.resources.settings_spot_here
import com.kodraliu.localrock.resources.settings_spot_once_there
import com.kodraliu.localrock.resources.settings_spot_started
import com.kodraliu.localrock.resources.settings_start
import com.kodraliu.localrock.resources.settings_start_time
import com.kodraliu.localrock.resources.settings_status_led
import com.kodraliu.localrock.resources.settings_strainer
import com.kodraliu.localrock.resources.settings_tab_cleaning
import com.kodraliu.localrock.resources.settings_tab_navigating
import com.kodraliu.localrock.resources.settings_time_range
import com.kodraliu.localrock.resources.settings_timeline
import com.kodraliu.localrock.resources.settings_timeline_reading
import com.kodraliu.localrock.resources.settings_title
import com.kodraliu.localrock.resources.settings_total
import com.kodraliu.localrock.resources.settings_total_area
import com.kodraliu.localrock.resources.settings_total_line
import com.kodraliu.localrock.resources.settings_total_time
import com.kodraliu.localrock.resources.settings_unknown
import com.kodraliu.localrock.resources.settings_unknown_value
import com.kodraliu.localrock.resources.settings_unlisted_count
import com.kodraliu.localrock.resources.settings_unlisted_text
import com.kodraliu.localrock.resources.settings_unnamed
import com.kodraliu.localrock.resources.settings_used
import com.kodraliu.localrock.resources.settings_valley_limit
import com.kodraliu.localrock.resources.settings_voice_assistant
import com.kodraliu.localrock.resources.settings_voice_commands
import com.kodraliu.localrock.resources.settings_voice_downloading
import com.kodraliu.localrock.resources.settings_voice_load_error
import com.kodraliu.localrock.resources.settings_voice_pack
import com.kodraliu.localrock.resources.settings_voice_pack_sub
import com.kodraliu.localrock.resources.settings_voice_reading
import com.kodraliu.localrock.resources.settings_voice_sub
import com.kodraliu.localrock.resources.settings_voice_switch_text
import com.kodraliu.localrock.resources.settings_voice_switch_title
import com.kodraliu.localrock.resources.settings_voice_switching
import com.kodraliu.localrock.resources.settings_volume
import com.kodraliu.localrock.resources.settings_wash_water_temperature
import com.kodraliu.localrock.resources.settings_washing_mode
import com.kodraliu.localrock.resources.settings_when_full
import com.kodraliu.localrock.resources.settings_when_needed
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.getString
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.material3.Tab
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TextField
import androidx.compose.material.icons.filled.Gamepad
import androidx.compose.material.icons.filled.Place
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.Search
import com.kodraliu.localrock.shared.vacuum.summary
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.material.icons.filled.Route
import androidx.compose.material.icons.filled.Bedtime
import com.kodraliu.localrock.shared.vacuum.CleanRunSettings
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.background
import androidx.compose.ui.draw.clip
import androidx.compose.material.icons.filled.Check
import androidx.compose.foundation.border
import androidx.compose.ui.draw.drawBehind
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.IntrinsicSize
import com.kodraliu.localrock.shared.vacuum.describeCleanEvents
import com.kodraliu.localrock.shared.vacuum.CleanTimelineEntry
import com.kodraliu.localrock.shared.vacuum.CleanEvent
import com.kodraliu.localrock.shared.vacuum.VoicePack
import androidx.compose.foundation.layout.statusBarsPadding
import com.kodraliu.localrock.shared.vacuum.MAX_DND_PERIODS
import com.kodraliu.localrock.shared.vacuum.DndPeriod
import androidx.compose.foundation.clickable
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Autorenew
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Cached
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CleaningServices
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Memory
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Router
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Sensors
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material.icons.filled.SystemUpdate
import androidx.compose.material.icons.filled.Thermostat
import androidx.compose.material.icons.automirrored.filled.VolumeMute
import androidx.compose.material.icons.automirrored.filled.VolumeOff
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kodraliu.localrock.shared.vacuum.AdvertisedFeature
import com.kodraliu.localrock.shared.vacuum.BetaFeatureState
import com.kodraliu.localrock.shared.vacuum.CleanRecord
import com.kodraliu.localrock.shared.vacuum.CleanSummary
import com.kodraliu.localrock.shared.vacuum.ConsumableStatus
import com.kodraliu.localrock.shared.vacuum.VacuumStateCodes
import com.kodraliu.localrock.shared.vacuum.DndSettings
import com.kodraliu.localrock.shared.vacuum.DockCapabilities
import com.kodraliu.localrock.shared.vacuum.FeatureSection
import com.kodraliu.localrock.shared.vacuum.inSection
import com.kodraliu.localrock.shared.vacuum.DockOptions
import com.kodraliu.localrock.shared.vacuum.DRY_START_AFTER_CLEANING
import com.kodraliu.localrock.shared.vacuum.DRY_START_OFF
import com.kodraliu.localrock.shared.vacuum.DRY_START_SCHEDULED
import com.kodraliu.localrock.shared.vacuum.DockSettings
import com.kodraliu.localrock.shared.vacuum.ValleySettings
import com.kodraliu.localrock.shared.vacuum.dryTimeLabel
import com.kodraliu.localrock.shared.vacuum.labelFor
import com.kodraliu.localrock.shared.vacuum.RobotSettings
import com.kodraliu.localrock.shared.vacuum.VolumeRange
import com.kodraliu.localrock.shared.vacuum.volumeFromSlider
import com.kodraliu.localrock.shared.vacuum.volumeLabel
import com.kodraliu.localrock.shared.vacuum.washFrequencyLabel
import com.kodraliu.localrock.shared.vacuum.map.ParsedMap
import com.kodraliu.localrock.ui.AppColors
import com.kodraliu.localrock.ui.adaptive.CenteredContent
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

// ── Navigation ────────────────────────────────────────────────────────────────

private sealed interface SettingsRoute {
    data object Root : SettingsRoute
    data object Maintenance : SettingsRoute
    data object DockSettings : SettingsRoute
    data object RobotSettings : SettingsRoute
    data object DndMode : SettingsRoute
    data object Carpet : SettingsRoute
    data object Floor : SettingsRoute
    data object Obstacle : SettingsRoute
    data object Pets : SettingsRoute
    data object Valley : SettingsRoute
    data object BetaFeatures : SettingsRoute
    data object History : SettingsRoute
    data object VoicePack : SettingsRoute
}

// ── Entry point ───────────────────────────────────────────────────────────────

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun VacuumSettingsScreen(
    viewModel: VacuumViewModel,
    onBack: () -> Unit,
    onSchedule: () -> Unit = {},
    onMaps: () -> Unit = {},
    onLiveView: (() -> Unit)? = null,
    onPinGo: () -> Unit = {},
    onRemote: () -> Unit = {},
) {
    val repository = viewModel.repository
    var route by remember { mutableStateOf<SettingsRoute>(SettingsRoute.Root) }
    // Sub-pages are local state, not nav destinations, so system back would leave Settings (#52).
    BackHandler(enabled = route != SettingsRoute.Root) {
        route = if (route == SettingsRoute.Valley) SettingsRoute.DockSettings else SettingsRoute.Root
    }
    val scope = rememberCoroutineScope()
    val consumables by repository.consumableStatus.collectAsState()
    val robotSettings by repository.robotSettings.collectAsState()
    val dockSettings by repository.dockSettings.collectAsState()
    val status by viewModel.status.collectAsState()
    val busy by viewModel.busy.collectAsState()
    val dndSettings by repository.dndSettings.collectAsState()
    val dndPeriods by repository.dndPeriods.collectAsState()
    val valleySettings by repository.valleySettings.collectAsState()
    val firmwareUpdate by viewModel.firmwareUpdate.collectAsState()
    val betaFeatures by repository.betaFeatures.collectAsState()
    val betaFeaturesLoading by repository.betaFeaturesLoading.collectAsState()

    val reading by repository.reading.collectAsState()

    // The options come from the cache first (what the robot said last time) and are read again each
    // time settings open; the pages keep showing the cached values until the robot has answered.
    LaunchedEffect(Unit) {
        runCatching { repository.fetchRobotFeatureSet() }
        runCatching { repository.fetchBetaFeatures(viewModel.betaFeatureDefs) }
    }

    val cleanHistory by repository.cleanHistory.collectAsState()
    val cleanSummary by repository.cleanSummary.collectAsState()
    LaunchedEffect(Unit) {
        // For the cards at the top of Settings, as the official app opens it.
        runCatching { repository.fetchCleanSummary() }
        runCatching { repository.fetchConsumableStatus() }
        runCatching { repository.fetchCleanHistory() }
    }

    val emptyingTankMessage = stringResource(Res.string.settings_emptying_tank)
    Box(Modifier.fillMaxSize()) {
    when (route) {
        SettingsRoute.Root -> SettingsRootScreen(
            onPets = if (betaFeatures.inSection(FeatureSection.PETS).isEmpty()) null else ({ route = SettingsRoute.Pets }),
            onMaps = onMaps,
            onVoice = {
                scope.launch { runCatching { repository.fetchRobotSettings() } }
                route = SettingsRoute.RobotSettings
            },
            onLiveView = onLiveView,
            onPinGo = onPinGo,
            onRemote = onRemote,
            searchEntries = settingsSearchIndex(
                features = betaFeatures,
                openSection = { section ->
                    route = when (section) {
                        FeatureSection.CARPET -> SettingsRoute.Carpet
                        FeatureSection.FLOOR -> SettingsRoute.Floor
                        FeatureSection.OBSTACLE -> SettingsRoute.Obstacle
                        FeatureSection.PETS -> SettingsRoute.Pets
                        FeatureSection.DOCK -> SettingsRoute.DockSettings
                        FeatureSection.ROBOT -> SettingsRoute.RobotSettings
                        FeatureSection.BETA -> SettingsRoute.BetaFeatures
                    }
                },
                pages = listOf(
                    stringResource(Res.string.settings_cleaning_history) to { route = SettingsRoute.History },
                    stringResource(Res.string.settings_schedules) to onSchedule,
                    stringResource(Res.string.settings_maintenance) to { route = SettingsRoute.Maintenance },
                    stringResource(Res.string.settings_carpet_cleaning) to { route = SettingsRoute.Carpet },
                    stringResource(Res.string.settings_floor_cleaning) to { route = SettingsRoute.Floor },
                    stringResource(Res.string.settings_dock_settings) to { route = SettingsRoute.DockSettings },
                    stringResource(Res.string.settings_obstacle_avoidance) to { route = SettingsRoute.Obstacle },
                    stringResource(Res.string.settings_pets) to { route = SettingsRoute.Pets },
                    stringResource(Res.string.settings_maps) to onMaps,
                    stringResource(Res.string.settings_voice_assistant) to { route = SettingsRoute.RobotSettings },
                    stringResource(Res.string.settings_voice_pack) to { route = SettingsRoute.VoicePack },
                    stringResource(Res.string.settings_more_options) to { route = SettingsRoute.RobotSettings },
                    stringResource(Res.string.settings_dnd) to { route = SettingsRoute.DndMode },
                    stringResource(Res.string.settings_off_peak_charging) to { route = SettingsRoute.Valley },
                    stringResource(Res.string.settings_beta_features) to { route = SettingsRoute.BetaFeatures },
                    stringResource(Res.string.settings_pin_go) to onPinGo,
                    stringResource(Res.string.settings_remote_control) to onRemote,
                ) + listOfNotNull(onLiveView?.let { liveView -> stringResource(Res.string.settings_live_view) to liveView }),
            ),
            lastClean = cleanHistory.filter { !it.isPatrol }.maxByOrNull { it.beginEpoch },
            summary = cleanSummary,
            maintenanceSummary = consumables?.summary(),
            onSchedule = onSchedule,
            modelName = viewModel.modelName,
            firmwareVersion = viewModel.firmwareVersion,
            firmwareUpdate = firmwareUpdate,
            onCheckUpdate = { viewModel.checkFirmwareUpdate() },
            onBack = onBack,
            onHistory = { route = SettingsRoute.History },
            onMaintenance = {
                scope.launch { runCatching { repository.fetchConsumableStatus() } }
                route = SettingsRoute.Maintenance
            },
            onCarpet = {
                scope.launch { runCatching { repository.fetchRobotSettings() } }
                route = SettingsRoute.Carpet
            },
            onFloor = if (betaFeatures.inSection(FeatureSection.FLOOR).isEmpty()) null else ({ route = SettingsRoute.Floor }),
            onObstacle = if (betaFeatures.inSection(FeatureSection.OBSTACLE).isEmpty()) null else ({ route = SettingsRoute.Obstacle }),
            onDockSettings = {
                scope.launch {
                    runCatching { repository.fetchDockSettings() }
                    runCatching { repository.fetchValleySettings() }
                }
                route = SettingsRoute.DockSettings
            },
            onRobotSettings = {
                scope.launch { runCatching { repository.fetchRobotSettings() } }
                route = SettingsRoute.RobotSettings
            },
            onDndMode = {
                scope.launch { runCatching { repository.fetchDndSettings() } }
                scope.launch { runCatching { repository.fetchDndPeriods() } }
                route = SettingsRoute.DndMode
            },
            onBetaFeatures = if (!viewModel.showsBetaFeatures) null else ({
                route = SettingsRoute.BetaFeatures
            }),
        )
        SettingsRoute.Carpet -> CarpetScreen(
            carpetMode = robotSettings.carpetMode,
            modes = viewModel.carpetModes,
            features = betaFeatures.inSection(FeatureSection.CARPET),
            onCarpetModeChange = { mode -> viewModel.report { it.setCarpetMode(mode) } },
            onToggle = { id, on -> viewModel.report { it.setBetaFeature(id, on) } },
            onBack = { route = SettingsRoute.Root },
        )
        SettingsRoute.Floor -> FeatureSectionScreen(
            title = stringResource(Res.string.settings_floor_cleaning),
            features = betaFeatures.inSection(FeatureSection.FLOOR),
            onToggle = { id, on -> viewModel.report { it.setBetaFeature(id, on) } },
            onBack = { route = SettingsRoute.Root },
        )
        SettingsRoute.Pets -> FeatureSectionScreen(
            title = stringResource(Res.string.settings_pets),
            features = betaFeatures.inSection(FeatureSection.PETS),
            onToggle = { id, on -> viewModel.report { it.setBetaFeature(id, on) } },
            onBack = { route = SettingsRoute.Root },
        )
        SettingsRoute.Obstacle -> FeatureSectionScreen(
            title = stringResource(Res.string.settings_obstacle_avoidance),
            features = betaFeatures.inSection(FeatureSection.OBSTACLE),
            onToggle = { id, on -> viewModel.report { it.setBetaFeature(id, on) } },
            onBack = { route = SettingsRoute.Root },
        )
        SettingsRoute.BetaFeatures -> BetaFeaturesScreen(
            features = betaFeatures.inSection(FeatureSection.BETA),
            loading = betaFeaturesLoading,
            unlisted = viewModel.unlistedFeatures,
            onToggle = { id, on -> viewModel.report { it.setBetaFeature(id, on) } },
            onBack = { route = SettingsRoute.Root },
        )
        SettingsRoute.Maintenance -> MaintenanceScreen(
            consumables = consumables,
            onRefresh = { scope.launch { runCatching { repository.fetchConsumableStatus() } } },
            onReset = { field -> scope.launch { runCatching { repository.resetConsumable(field) } } },
            onBack = { route = SettingsRoute.Root },
        )
        SettingsRoute.DockSettings -> DockSettingsScreen(
            dockSettings = dockSettings,
            dockCapabilities = viewModel.dockCapabilities,
            dockType = status.dockType,
            dockStatus = status.dockStatus,
            busy = busy,
            onWashModeChange = { mode -> viewModel.run { it.setWashMode(mode) } },
            onWashFrequencyChange = { mode, minutes -> viewModel.run { it.setWashFrequency(mode, minutes) } },
            onAutoEmptyModeChange = { mode -> viewModel.run { it.setAutoEmptyMode(mode) } },
            onEmptyWaterTank = { viewModel.run(emptyingTankMessage) { it.emptyInbuiltWaterTank() } },
            features = betaFeatures.inSection(FeatureSection.DOCK),
            onFeatureToggle = { id, on -> viewModel.report { it.setBetaFeature(id, on) } },
            valleySettings = valleySettings,
            onMopDryingChange = { on -> viewModel.run { it.setMopDrying(on) } },
            onDryStartChange = { start, hour, minute -> viewModel.run { it.setDryStart(start, hour, minute) } },
            onDryTimeChange = { seconds -> viewModel.run { it.setDryTime(seconds) } },
            onWashWaterTemperatureChange = { value -> viewModel.run { it.setWashWaterTemperature(value) } },
            onOpenValley = { route = SettingsRoute.Valley },
            onBack = { route = SettingsRoute.Root },
        )
        SettingsRoute.Valley -> ValleyScreen(
            valleySettings = valleySettings,
            onEnable = { sh, sm, eh, em -> viewModel.run { it.setValley(sh, sm, eh, em) } },
            onDisable = { viewModel.run { it.disableValley() } },
            onBack = { route = SettingsRoute.DockSettings },
        )
        SettingsRoute.RobotSettings -> RobotSettingsScreen(
            robotSettings = robotSettings,
            onFindRobot = { scope.launch { runCatching { repository.findMe() } } },
            onVolumeChange = { volume -> viewModel.report { it.setVolume(volume) } },
            onChildLockChange = { locked -> viewModel.report { it.setChildLock(locked) } },
            onLedChange = { on -> viewModel.report { it.setLed(on) } },
            features = betaFeatures.inSection(FeatureSection.ROBOT),
            onFeatureToggle = { id, on -> viewModel.report { it.setBetaFeature(id, on) } },
            onVoicePack = if (viewModel.voicePackBucket == null) null else ({ route = SettingsRoute.VoicePack }),
            onBack = { route = SettingsRoute.Root },
        )
        SettingsRoute.VoicePack -> VoicePackScreen(viewModel, onBack = { route = SettingsRoute.RobotSettings })
        SettingsRoute.DndMode -> if (dndPeriods != null) MultiDndScreen(
            robotPeriods = dndPeriods.orEmpty(),
            onChange = { periods -> viewModel.report { r -> runCatching { r.setDndPeriods(periods) }.onFailure { r.fetchDndPeriods(); throw it } } },
            onBack = { route = SettingsRoute.Root },
        ) else DndScreen(
            dndSettings = dndSettings,
            // A refusal is shown and the robot's setting read back, so the switch never shows a state it lacks.
            onEnable = { sh, sm, eh, em -> viewModel.report { r -> runCatching { r.setDnd(sh, sm, eh, em) }.onFailure { r.fetchDndSettings(); throw it } } },
            onDisable = { viewModel.report { r -> runCatching { r.disableDnd() }.onFailure { r.fetchDndSettings(); throw it } } },
            onBack = { route = SettingsRoute.Root },
        )
        SettingsRoute.History -> VacuumHistoryScreen(viewModel, onBack = { route = SettingsRoute.Root })
    }
    // A thin bar under the top bar while the robot is being asked; the values below are the last known.
    if (reading) {
        LinearProgressIndicator(
            Modifier.fillMaxWidth().align(Alignment.TopCenter).statusBarsPadding().padding(top = 64.dp),
        )
    }
    }
}


@Composable
fun VacuumRemoteScreen(viewModel: VacuumViewModel, onBack: () -> Unit) {
    val repository = viewModel.repository
    val scope = rememberCoroutineScope()
    LaunchedEffect(Unit) { runCatching { repository.startRemoteControl() } }
    RemoteControlScreen(
        onMove = { v, o -> scope.launch { runCatching { repository.moveRemote(v, o, 1500) } } },
        onStop = { scope.launch { runCatching { repository.stopRemoteControl() } } },
        onBack = {
            scope.launch { runCatching { repository.stopRemoteControl() } }
            onBack()
        },
    )
}

@Composable
fun VacuumPinGoScreen(viewModel: VacuumViewModel, onBack: () -> Unit) {
    val parsedMap by viewModel.repository.parsedMap.collectAsState()
    val busy by viewModel.busy.collectAsState()
    val status by viewModel.status.collectAsState()
    val spotStartedMessage = stringResource(Res.string.settings_spot_started)
    PinGoScreen(
        parsedMap = parsedMap,
        busy = busy,
        travelling = status.state == VacuumStateCodes.GOING_TO_TARGET,
        onSpot = { viewModel.run(spotStartedMessage) { it.spotClean() } },
        // A refusal is shown by run(); onSent only fires once the robot has accepted the target.
        onGoto = { x, y, onSent ->
            viewModel.run {
                it.gotoTarget(x, y)
                onSent()
            }
        },
        onBack = onBack,
    )
}

@Composable
fun VacuumHistoryScreen(viewModel: VacuumViewModel, onBack: () -> Unit) {
    val repository = viewModel.repository
    val scope = rememberCoroutineScope()
    val cleanHistory by repository.cleanHistory.collectAsState()
    val cleanSummary by repository.cleanSummary.collectAsState()
    LaunchedEffect(Unit) {
        runCatching { repository.fetchCleanHistory() }
        runCatching { repository.fetchCleanSummary() }
    }
    HistoryScreen(
        loadMap = { repository.fetchCleanRecordMap(it) },
        loadEvents = { repository.fetchCleanEvents(it) },
        roomName = { id -> repository.rooms.value.firstOrNull { it.id == id }?.name ?: repository.parsedMap.value?.rooms?.firstOrNull { it.id == id }?.name },
        records = cleanHistory,
        summary = cleanSummary,
        onRefresh = {
            scope.launch {
                runCatching { repository.fetchCleanHistory() }
                runCatching { repository.fetchCleanSummary() }
            }
        },
        onBack = onBack,
    )
}


/** A Settings entry or option the search can find: where it is and how to get there. */
internal data class SettingsSearchEntry(val title: String, val where: String, val open: () -> Unit)

/** Every page and every option the robot reports, for the Settings search. */
@Composable
private fun settingsSearchIndex(
    features: List<BetaFeatureState>,
    openSection: (FeatureSection) -> Unit,
    pages: List<Pair<String, () -> Unit>>,
): List<SettingsSearchEntry> {
    val sectionName = mapOf(
        FeatureSection.CARPET to stringResource(Res.string.settings_carpet_cleaning), FeatureSection.FLOOR to stringResource(Res.string.settings_floor_cleaning),
        FeatureSection.OBSTACLE to stringResource(Res.string.settings_obstacle_avoidance), FeatureSection.PETS to stringResource(Res.string.settings_pets),
        FeatureSection.DOCK to stringResource(Res.string.settings_dock_settings), FeatureSection.ROBOT to stringResource(Res.string.settings_more_options),
        FeatureSection.BETA to stringResource(Res.string.settings_beta_features),
    )
    val settings = stringResource(Res.string.settings_title)
    return pages.map { (title, open) -> SettingsSearchEntry(title, settings, open) } +
        features.filter { it.answered }.map { f ->
            val section = f.def.section
            SettingsSearchEntry(f.def.title, "${sectionName[section] ?: settings} · ${f.def.description}") { openSection(section) }
        }
}

/** One row of a group card. */
private class GroupRow(val icon: ImageVector, val tint: Color, val title: String, val subtitle: String?, val onClick: () -> Unit)

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
private fun SettingsRootScreen(
    lastClean: CleanRecord?,
    summary: CleanSummary?,
    maintenanceSummary: String?,
    onSchedule: () -> Unit,
    modelName: String?,
    firmwareVersion: String?,
    firmwareUpdate: FirmwareUpdateState,
    onCheckUpdate: () -> Unit,
    onBack: () -> Unit,
    onHistory: () -> Unit,
    onMaintenance: () -> Unit,
    onCarpet: () -> Unit,
    onFloor: (() -> Unit)?,
    onObstacle: (() -> Unit)?,
    onPets: (() -> Unit)?,
    onDockSettings: () -> Unit,
    onMaps: () -> Unit,
    onVoice: () -> Unit,
    onLiveView: (() -> Unit)?,
    onRobotSettings: () -> Unit,
    onDndMode: () -> Unit,
    onBetaFeatures: (() -> Unit)?,
    onPinGo: () -> Unit,
    onRemote: () -> Unit,
    searchEntries: List<SettingsSearchEntry>,
) {
    var searching by remember { mutableStateOf(false) }
    var query by remember { mutableStateOf("") }
    BackHandler(enabled = searching) { searching = false; query = "" }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    if (searching) {
                        val focus = remember { FocusRequester() }
                        LaunchedEffect(Unit) { focus.requestFocus() }
                        TextField(
                            value = query,
                            onValueChange = { query = it },
                            placeholder = { Text(stringResource(Res.string.settings_search)) },
                            singleLine = true,
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                            ),
                            modifier = Modifier.fillMaxWidth().focusRequester(focus),
                        )
                    } else {
                        Text(stringResource(Res.string.settings_title))
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { if (searching) { searching = false; query = "" } else onBack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(Res.string.settings_back))
                    }
                },
                actions = {
                    // As the official app: search at the top right.
                    if (!searching) {
                        IconButton(onClick = { searching = true }) {
                            Icon(Icons.Default.Search, contentDescription = stringResource(Res.string.settings_search))
                        }
                    }
                },
            )
        },
    ) { padding ->
        CenteredContent {
            if (searching) {
                val terms = query.trim().lowercase().split(' ').filter { it.isNotEmpty() }
                // Each typed word has to start a word ("pet" finds Pets, not carpet).
                val hits = if (terms.isEmpty()) emptyList() else searchEntries.filter { e ->
                    val words = (e.title + " " + e.where).lowercase().split(Regex("[^\\p{L}\\p{N}&+]+"))
                    terms.all { term -> words.any { it.startsWith(term) } }
                }
                LazyColumn(contentPadding = padding) {
                    if (terms.isNotEmpty() && hits.isEmpty()) {
                        item { Text(stringResource(Res.string.settings_nothing_found, query.trim()), Modifier.padding(16.dp), color = MaterialTheme.colorScheme.onSurfaceVariant) }
                    }
                    items(hits.size) { i ->
                        val hit = hits[i]
                        Surface(onClick = { searching = false; query = ""; hit.open() }, color = Color.Transparent) {
                            Column(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp)) {
                                Text(hit.title, style = MaterialTheme.typography.bodyLarge)
                                Text(hit.where, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }
                return@CenteredContent
            }
            LazyColumn(contentPadding = padding) {
                // As the official app: the last clean and the totals first, then schedules and maintenance.
                item { HistoryOverviewCard(lastClean = lastClean, summary = summary, onClick = onHistory) }
                item {
                    SettingsGroup(listOf(
                        GroupRow(Icons.Default.Schedule, AppColors.Water, stringResource(Res.string.settings_schedules), stringResource(Res.string.settings_schedules_sub), onSchedule),
                        GroupRow(Icons.Default.Build, AppColors.Good, stringResource(Res.string.settings_maintenance), maintenanceSummary ?: stringResource(Res.string.settings_maintenance_sub), onMaintenance),
                    ))
                }
                item {
                    SettingsGroup(listOfNotNull(
                        GroupRow(Icons.Default.GridView, AppColors.Good, stringResource(Res.string.settings_carpet_cleaning), stringResource(Res.string.settings_carpet_sub), onCarpet),
                        onFloor?.let { GroupRow(Icons.Default.CleaningServices, AppColors.AccentCyan, stringResource(Res.string.settings_floor_cleaning), stringResource(Res.string.settings_floor_sub), it) },
                        GroupRow(Icons.Default.Home, AppColors.Water, stringResource(Res.string.settings_dock_settings), stringResource(Res.string.settings_dock_sub), onDockSettings),
                        onObstacle?.let { GroupRow(Icons.Default.Sensors, AppColors.AccentYellow, stringResource(Res.string.settings_obstacle_avoidance), stringResource(Res.string.settings_obstacle_sub), it) },
                        onPets?.let { GroupRow(Icons.Default.Pets, AppColors.AccentPurple, stringResource(Res.string.settings_pets), stringResource(Res.string.settings_pets_sub), it) },
                    ))
                }
                item {
                    SettingsGroup(listOfNotNull(
                        GroupRow(Icons.Default.Map, AppColors.Warn, stringResource(Res.string.settings_maps), stringResource(Res.string.settings_maps_sub), onMaps),
                        GroupRow(Icons.Default.RecordVoiceOver, AppColors.AccentIndigo, stringResource(Res.string.settings_voice_assistant), stringResource(Res.string.settings_voice_sub), onVoice),
                        onLiveView?.let { GroupRow(Icons.Default.Videocam, AppColors.AccentRed, stringResource(Res.string.settings_live_view), stringResource(Res.string.settings_live_view_sub), it) },
                        GroupRow(Icons.Default.Settings, AppColors.AccentPurple, stringResource(Res.string.settings_more_options), stringResource(Res.string.settings_more_options_sub), onRobotSettings),
                        GroupRow(Icons.Default.Bedtime, AppColors.AccentIndigo, stringResource(Res.string.settings_dnd), stringResource(Res.string.settings_dnd_sub), onDndMode),
                        onBetaFeatures?.let { GroupRow(Icons.Default.Science, AppColors.AccentCyan, stringResource(Res.string.settings_beta_features), stringResource(Res.string.settings_beta_sub), it) },
                    ))
                }
                item {
                    SettingsGroup(listOf(
                        GroupRow(Icons.Default.Place, AppColors.AccentPurple, stringResource(Res.string.settings_pin_go), stringResource(Res.string.settings_pin_go_sub), onPinGo),
                        GroupRow(Icons.Default.Gamepad, AppColors.AccentCyan, stringResource(Res.string.settings_remote_control), stringResource(Res.string.settings_remote_sub), onRemote),
                    ))
                }
                item {
                    Surface(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                        shape = MaterialTheme.shapes.large,
                        color = MaterialTheme.colorScheme.surfaceContainer,
                    ) {
                        Column {
                            SettingRowInfo(icon = Icons.Default.Router, iconTint = AppColors.AccentIndigo, title = stringResource(Res.string.settings_model), value = modelName ?: stringResource(Res.string.settings_unknown))
                            HorizontalDivider(Modifier.padding(start = 72.dp), color = MaterialTheme.colorScheme.outlineVariant)
                            SettingRowInfo(icon = Icons.Default.Memory, iconTint = AppColors.AccentCyan, title = stringResource(Res.string.settings_firmware_version), value = firmwareVersion ?: stringResource(Res.string.settings_unknown))
                            // The update check is hidden for now; FirmwareUpdateRow and the view model stay in place.
                            if (SHOW_FIRMWARE_UPDATE_CHECK) {
                                HorizontalDivider(Modifier.padding(start = 72.dp), color = MaterialTheme.colorScheme.outlineVariant)
                                FirmwareUpdateRow(state = firmwareUpdate, onCheck = onCheckUpdate)
                            }
                        }
                    }
                }
                item { Spacer(Modifier.height(16.dp)) }
            }
        }
    }
}

/** Rows in one rounded card, as the official app groups its settings. */
@Composable
private fun SettingsGroup(rows: List<GroupRow>) {
    if (rows.isEmpty()) return
    Surface(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surfaceContainer,
    ) {
        Column {
            rows.forEachIndexed { i, row ->
                if (i > 0) HorizontalDivider(Modifier.padding(start = 72.dp), color = MaterialTheme.colorScheme.outlineVariant)
                SettingsCategoryRow(icon = row.icon, iconTint = row.tint, title = row.title, subtitle = row.subtitle ?: "", onClick = row.onClick)
            }
        }
    }
}

private const val SHOW_FIRMWARE_UPDATE_CHECK = false

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DndScreen(
    dndSettings: DndSettings?,
    onEnable: (startHour: Int, startMinute: Int, endHour: Int, endMinute: Int) -> Unit,
    onDisable: () -> Unit,
    onBack: () -> Unit,
) {
    val settings = dndSettings ?: DndSettings()
    var enabled by remember(dndSettings?.enabled) { mutableStateOf(settings.enabled) }
    var startHour by remember(dndSettings) { mutableStateOf(settings.startHour) }
    var startMinute by remember(dndSettings) { mutableStateOf(settings.startMinute) }
    var endHour by remember(dndSettings) { mutableStateOf(settings.endHour) }
    var endMinute by remember(dndSettings) { mutableStateOf(settings.endMinute) }

    fun formatTime(h: Int, m: Int) =
        "${h.toString().padStart(2, '0')}:${m.toString().padStart(2, '0')}"
    fun commit() = if (enabled) onEnable(startHour, startMinute, endHour, endMinute) else onDisable()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Res.string.settings_dnd)) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, stringResource(Res.string.settings_back)) } },
            )
        },
    ) { padding ->
        CenteredContent {
            Column(
                modifier = Modifier.fillMaxWidth().padding(padding).padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    Column(Modifier.weight(1f)) {
                        Text(stringResource(Res.string.settings_dnd_enable), style = MaterialTheme.typography.bodyLarge)
                        Text(
                            if (enabled) "${formatTime(startHour, startMinute)} – ${formatTime(endHour, endMinute)}"
                            else stringResource(Res.string.settings_dnd_anytime),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    Switch(
                        checked = enabled,
                        onCheckedChange = { enabled = it; commit() },
                    )
                }

                if (enabled) {
                    HorizontalDivider()
                    Text(stringResource(Res.string.settings_start_time), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        NumberStepper2(label = stringResource(Res.string.settings_hour), value = startHour, range = 0..23, onValueChange = { startHour = it; commit() })
                        Text(":", style = MaterialTheme.typography.headlineMedium)
                        NumberStepper2(label = stringResource(Res.string.settings_minute), value = startMinute, range = 0..59, step = 15, onValueChange = { startMinute = it; commit() })
                    }

                    Text(stringResource(Res.string.settings_end_time), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        NumberStepper2(label = stringResource(Res.string.settings_hour), value = endHour, range = 0..23, onValueChange = { endHour = it; commit() })
                        Text(":", style = MaterialTheme.typography.headlineMedium)
                        NumberStepper2(label = stringResource(Res.string.settings_minute), value = endMinute, range = 0..59, step = 15, onValueChange = { endMinute = it; commit() })
                    }

                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        shape = MaterialTheme.shapes.medium,
                    ) {
                        Text(
                            stringResource(Res.string.settings_dnd_note, formatTime(startHour, startMinute), formatTime(endHour, endMinute)),
                            modifier = Modifier.padding(14.dp),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                        )
                    }
                }
            }
        }
    }
}

/**
 * The voices the robot can speak with, from the server's copy of Roborock's list. Choosing one tells the
 * robot to download it from the server and install it, which takes a minute or two.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun VoicePackScreen(viewModel: VacuumViewModel, onBack: () -> Unit) {
    val repository = viewModel.repository
    val state by repository.voicePack.collectAsState()
    var packs by remember { mutableStateOf<List<VoicePack>?>(null) }
    var loadError by remember { mutableStateOf<String?>(null) }
    var confirm by remember { mutableStateOf<VoicePack?>(null) }
    var launched by remember { mutableStateOf<VoicePack?>(null) }
    // An install keeps running in the view model after this page closes; the repository's state brings
    // it back when the page is opened again.
    val installing = launched ?: packs?.firstOrNull { pack -> state.inProgress == pack.id }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        runCatching { repository.fetchVoicePack() }
        runCatching { viewModel.loadVoicePacks() }
            .onSuccess { packs = it }
            .onFailure { loadError = getString(Res.string.settings_voice_load_error, "${it.message}") }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Res.string.settings_voice_pack)) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, stringResource(Res.string.settings_back)) } },
            )
        },
    ) { padding ->
        CenteredContent {
          Column(Modifier.fillMaxSize().padding(padding)) {
            // Stays in view while the list scrolls, so a running install is never out of sight.
            installing?.let { pack ->
                val percent = state.progress ?: 0
                Surface(color = MaterialTheme.colorScheme.secondaryContainer) {
                    Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Text(
                            if (percent in 1..99) stringResource(Res.string.settings_voice_downloading, pack.title, percent)
                            else stringResource(Res.string.settings_voice_switching, pack.title),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                        )
                        if (percent in 1..99) {
                            LinearProgressIndicator(progress = { percent / 100f }, modifier = Modifier.fillMaxWidth())
                        } else {
                            LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                        }
                    }
                }
            }
            LazyColumn(Modifier.fillMaxSize()) {
                loadError?.let { item { Text(it, Modifier.padding(16.dp), color = MaterialTheme.colorScheme.error) } }
                if (packs == null && loadError == null) {
                    item { Text(stringResource(Res.string.settings_voice_reading), Modifier.padding(16.dp)) }
                }
                packs?.let { list ->
                    items(list.size) { i ->
                        val pack = list[i]
                        val inUse = state.inUse == pack.id
                        val thisOne = installing?.id == pack.id
                        Surface(
                            onClick = { if (!inUse && installing == null) confirm = pack },
                            enabled = installing == null,
                            color = Color.Transparent,
                        ) {
                            Row(
                                Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Column(Modifier.weight(1f)) {
                                    Text(pack.title, style = MaterialTheme.typography.bodyLarge)
                                    val detail = listOfNotNull(pack.subTitle?.takeIf { it != pack.title }, if (pack.voiceControl) stringResource(Res.string.settings_voice_commands) else null)
                                        .joinToString(" · ")
                                    if (detail.isNotEmpty()) {
                                        Text(detail, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                }
                                if (thisOne) {
                                    Text("${state.progress ?: 0}%", style = MaterialTheme.typography.labelMedium, modifier = Modifier.padding(end = 8.dp))
                                    CircularProgressIndicator(Modifier.size(24.dp), strokeWidth = 2.dp)
                                } else {
                                    RadioButton(selected = inUse, onClick = null, enabled = installing == null || inUse)
                                }
                            }
                        }
                    }
                }
            }
          }
        }
    }

    confirm?.let { pack ->
        AlertDialog(
            onDismissRequest = { confirm = null },
            title = { Text(stringResource(Res.string.settings_voice_switch_title, pack.title)) },
            text = { Text(stringResource(Res.string.settings_voice_switch_text)) },
            confirmButton = {
                TextButton(onClick = {
                    confirm = null
                    launched = pack
                    scope.launch {
                        viewModel.report { it.installVoicePack(pack) }.join()
                        launched = null
                    }
                }) { Text(stringResource(Res.string.settings_install)) }
            },
            dismissButton = { TextButton(onClick = { confirm = null }) { Text(stringResource(Res.string.settings_cancel)) } },
        )
    }
}

private fun formatClock(h: Int, m: Int) = "${h.toString().padStart(2, '0')}:${m.toString().padStart(2, '0')}"

/** Quiet hours on robots with several periods (bit 140): each its own switch, times and delete, as in the official app. */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MultiDndScreen(robotPeriods: List<DndPeriod>, onChange: (List<DndPeriod>) -> Unit, onBack: () -> Unit) {
    // Sorted by start time, as the robot keeps them; the open period is known by its times, not its place.
    val periods = robotPeriods.sortedBy { it.startHour * 60 + it.startMinute }
    var editingTimes by remember { mutableStateOf<List<Int>?>(null) }
    // Times are changed on a draft and sent once, on Save, so stepping through them is not one write per tap.
    var draft by remember { mutableStateOf<DndPeriod?>(null) }
    val editing = periods.indexOfFirst { it.times == editingTimes }.takeIf { it >= 0 }
    fun open(period: DndPeriod?) {
        editingTimes = period?.times
        draft = period
    }
    fun replace(index: Int, period: DndPeriod) = onChange(periods.toMutableList().also { it[index] = period })

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Res.string.settings_dnd)) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, stringResource(Res.string.settings_back)) } },
            )
        },
    ) { padding ->
        CenteredContent {
            Column(
                modifier = Modifier.fillMaxWidth().padding(padding).padding(16.dp).verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Text(
                    stringResource(Res.string.settings_dnd_multi_intro, MAX_DND_PERIODS),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                periods.forEachIndexed { index, period ->
                    Surface(shape = MaterialTheme.shapes.medium, color = MaterialTheme.colorScheme.surfaceContainer) {
                        Column(Modifier.fillMaxWidth().padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Column(Modifier.weight(1f).clickable { open(if (editing == index) null else period) }) {
                                    Text(
                                        "${formatClock(period.startHour, period.startMinute)} – ${formatClock(period.endHour, period.endMinute)}",
                                        style = MaterialTheme.typography.titleMedium,
                                    )
                                    Text(
                                        if (editing == index) stringResource(Res.string.settings_dnd_tap_close) else stringResource(Res.string.settings_dnd_tap_change),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    )
                                }
                                IconButton(onClick = { open(null); onChange(periods.filterIndexed { i, _ -> i != index }) }) {
                                    Icon(Icons.Filled.Delete, stringResource(Res.string.settings_dnd_delete_period))
                                }
                                Switch(checked = period.enabled, onCheckedChange = { replace(index, period.copy(enabled = it)) })
                            }
                            val d = draft
                            if (editing == index && d != null) {
                                DndTimeRow(stringResource(Res.string.settings_start), d.startHour, d.startMinute) { h, m -> draft = d.copy(startHour = h, startMinute = m) }
                                DndTimeRow(stringResource(Res.string.settings_end), d.endHour, d.endMinute) { h, m -> draft = d.copy(endHour = h, endMinute = m) }
                                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.End)) {
                                    TextButton(onClick = { open(null) }) { Text(stringResource(Res.string.settings_cancel)) }
                                    Button(
                                        onClick = {
                                            replace(index, d.copy(enabled = period.enabled))
                                            open(null)
                                        },
                                        enabled = d.times != period.times && periods.none { it !== period && it.times == d.times },
                                    ) { Text(stringResource(Res.string.settings_save)) }
                                }
                            }
                        }
                    }
                }
                if (periods.size < MAX_DND_PERIODS) {
                    OutlinedButton(
                        onClick = {
                            // A free one-hour slot from 12:00, as two periods may not share their times.
                            val taken = periods.map { it.times }.toSet()
                            val hour = (12..35).map { it % 24 }.first { listOf(it, 0, (it + 1) % 24, 0) !in taken }
                            val added = DndPeriod(hour, 0, (hour + 1) % 24, 0, true, periods.firstOrNull()?.actions)
                            open(added)
                            onChange(periods + added)
                        },
                        modifier = Modifier.fillMaxWidth(),
                    ) { Text(stringResource(Res.string.settings_dnd_add_period)) }
                }
            }
        }
    }
}

@Composable
private fun DndTimeRow(label: String, hour: Int, minute: Int, onChange: (Int, Int) -> Unit) {
    Text(label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
    Row(horizontalArrangement = Arrangement.spacedBy(16.dp), verticalAlignment = Alignment.CenterVertically) {
        NumberStepper2(stringResource(Res.string.settings_hour), hour, 0..23) { onChange(it, minute) }
        Text(":", style = MaterialTheme.typography.headlineMedium)
        NumberStepper2(stringResource(Res.string.settings_minute), minute, 0..59, step = 15) { onChange(hour, it) }
    }
}

@Composable
private fun NumberStepper2(label: String, value: Int, range: IntRange, step: Int = 1, onValueChange: (Int) -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Surface(shape = MaterialTheme.shapes.medium, color = MaterialTheme.colorScheme.surfaceContainerHigh) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(
                    onClick = { val next = value - step; onValueChange(if (next < range.first) range.last - (range.last % step) else next) },
                    modifier = Modifier.size(40.dp),
                ) { Icon(Icons.AutoMirrored.Filled.KeyboardArrowLeft, null, Modifier.size(20.dp)) }
                Text(value.toString().padStart(2, '0'), style = MaterialTheme.typography.titleLarge, modifier = Modifier.width(42.dp), textAlign = TextAlign.Center)
                IconButton(
                    onClick = { val next = value + step; onValueChange(if (next > range.last) range.first else next) },
                    modifier = Modifier.size(40.dp),
                ) { Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null, Modifier.size(20.dp)) }
            }
        }
    }
}

@Composable
private fun HistoryOverviewCard(lastClean: CleanRecord?, summary: CleanSummary?, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
        shape = MaterialTheme.shapes.large,
        color = MaterialTheme.colorScheme.surfaceContainer,
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.History, null, tint = AppColors.AccentCyan)
                Spacer(Modifier.width(12.dp))
                Text(stringResource(Res.string.settings_cleaning_history), style = MaterialTheme.typography.titleMedium, modifier = Modifier.weight(1f))
                Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null, tint = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                HistoryTile(
                    value = lastClean?.let { "${it.durationSeconds / 60}" } ?: "–",
                    unit = "min",
                    label = stringResource(Res.string.settings_last_clean_took),
                    modifier = Modifier.weight(1f),
                )
                HistoryTile(
                    value = lastClean?.let { oneDecimal(it.areaMm2 / 1_000_000f) } ?: "–",
                    unit = "m²",
                    label = stringResource(Res.string.settings_last_clean_covered),
                    modifier = Modifier.weight(1f),
                )
            }
            summary?.let { s ->
                Row(
                    Modifier.fillMaxWidth().background(MaterialTheme.colorScheme.surfaceContainerHigh, MaterialTheme.shapes.medium)
                        .padding(horizontal = 14.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(stringResource(Res.string.settings_total), style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f))
                    Text(
                        stringResource(Res.string.settings_total_line, "${s.totalTimeSec / 60}", "${(s.totalAreaMm2 / 1_000_000f).roundToInt()}", pluralStringResource(Res.plurals.settings_cleans_count, s.totalCount, s.totalCount)),
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                    )
                }
            }
        }
    }
}

@Composable
private fun HistoryTile(value: String, unit: String, label: String, modifier: Modifier = Modifier) {
    Column(
        modifier.background(MaterialTheme.colorScheme.surfaceContainerHigh, MaterialTheme.shapes.medium).padding(14.dp),
    ) {
        Text(
            buildAnnotatedString {
                withStyle(SpanStyle(fontWeight = FontWeight.SemiBold, fontSize = 26.sp)) { append(value) }
                append(" $unit")
            },
            style = MaterialTheme.typography.bodyMedium,
        )
        Text(label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@Composable
private fun SettingsCategoryRow(
    icon: ImageVector,
    iconTint: Color,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
) {
    Surface(onClick = onClick, color = Color.Transparent) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = iconTint.copy(alpha = 0.12f),
                modifier = Modifier.size(44.dp),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(icon, null, Modifier.size(22.dp), iconTint)
                }
            }
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(title, style = MaterialTheme.typography.bodyLarge)
                Text(
                    subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            Icon(
                Icons.AutoMirrored.Filled.KeyboardArrowRight,
                null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MaintenanceScreen(
    consumables: ConsumableStatus?,
    onRefresh: () -> Unit,
    onReset: (String) -> Unit,
    onBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Res.string.settings_maintenance)) },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, stringResource(Res.string.settings_back)) }
                },
                actions = {
                    IconButton(onClick = onRefresh) { Icon(Icons.Default.Refresh, stringResource(Res.string.settings_refresh)) }
                },
            )
        },
    ) { padding ->
        CenteredContent {
            if (consumables == null) {
                Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        Icon(
                            Icons.Default.Build,
                            null,
                            Modifier.size(48.dp),
                            MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Text(stringResource(Res.string.settings_maint_load_failed), style = MaterialTheme.typography.titleMedium)
                        OutlinedButton(onClick = onRefresh) { Text(stringResource(Res.string.settings_retry)) }
                    }
                }
            } else {
                LazyColumn(contentPadding = padding) {
                    item {
                        ConsumableItem(
                            icon = Icons.Default.Cached,
                            iconTint = AppColors.Good,
                            title = stringResource(Res.string.settings_main_brush),
                            careGuide = stringResource(Res.string.settings_guide_main_brush),
                            usedSeconds = consumables.mainBrushWorkTime ?: 0L,
                            maxSeconds = ConsumableStatus.MAIN_BRUSH_LIFETIME_S,
                            onReset = { onReset("main_brush_work_time") },
                        )
                    }
                    item {
                        ConsumableItem(
                            icon = Icons.Default.Autorenew,
                            iconTint = AppColors.Water,
                            title = stringResource(Res.string.settings_side_brush),
                            careGuide = stringResource(Res.string.settings_guide_side_brush),
                            usedSeconds = consumables.sideBrushWorkTime ?: 0L,
                            maxSeconds = ConsumableStatus.SIDE_BRUSH_LIFETIME_S,
                            onReset = { onReset("side_brush_work_time") },
                        )
                    }
                    item {
                        ConsumableItem(
                            icon = Icons.Default.Air,
                            iconTint = AppColors.AccentPurple,
                            title = stringResource(Res.string.settings_filter),
                            careGuide = stringResource(Res.string.settings_guide_filter),
                            usedSeconds = consumables.filterWorkTime ?: 0L,
                            maxSeconds = ConsumableStatus.FILTER_LIFETIME_S,
                            onReset = { onReset("filter_work_time") },
                        )
                    }
                    item {
                        ConsumableItem(
                            icon = Icons.Default.Sensors,
                            iconTint = AppColors.Warn,
                            title = stringResource(Res.string.settings_sensor),
                            careGuide = stringResource(Res.string.settings_guide_sensor),
                            usedSeconds = consumables.sensorDirtyTime ?: 0L,
                            maxSeconds = ConsumableStatus.SENSOR_LIFETIME_S,
                            onReset = { onReset("sensor_dirty_time") },
                        )
                    }
                    consumables.cleaningBrushWorkTime?.let { used ->
                            item {
                            ConsumableItem(
                                icon = Icons.Default.CleaningServices,
                                iconTint = AppColors.AccentCyan,
                                title = stringResource(Res.string.settings_maintenance_brush),
                                careGuide = stringResource(Res.string.settings_guide_maintenance_brush),
                                usedSeconds = used,
                                maxSeconds = ConsumableStatus.CLEANING_BRUSH_LIFETIME_S,
                                onReset = { onReset("cleaning_brush_work_times") },
                            )
                        }
                    }
                    // Counted, not timed: shown as the official app does, with the count since the last reset.
                    consumables.strainerWorkTime?.let { count ->
                            item {
                            CountedConsumableItem(
                                icon = Icons.Default.FilterAlt,
                                iconTint = AppColors.AccentIndigo,
                                title = stringResource(Res.string.settings_strainer),
                                careGuide = stringResource(Res.string.settings_guide_strainer),
                                count = count,
                                countLabel = stringResource(Res.string.settings_mop_washes_since_reset),
                                advice = stringResource(Res.string.settings_clean) to stringResource(Res.string.settings_when_needed),
                                onReset = { onReset("strainer_work_times") },
                            )
                        }
                    }
                    consumables.dustCollectionWorkTimes?.let { count ->
                            item {
                            CountedConsumableItem(
                                icon = Icons.Default.Delete,
                                iconTint = AppColors.Good,
                                title = stringResource(Res.string.settings_dust_bag),
                                careGuide = stringResource(Res.string.settings_guide_dust_bag),
                                count = count,
                                countLabel = stringResource(Res.string.settings_auto_empties_since_reset),
                                advice = stringResource(Res.string.settings_replace) to stringResource(Res.string.settings_when_full),
                                onReset = { onReset("dust_collection_work_times") },
                            )
                        }
                    }
                }
            }
        }
    }
}

/** One part as the official app's maintenance card: picture, two figures, a reset and the care guide. */
@Composable
private fun MaintenanceCard(
    icon: ImageVector,
    iconTint: Color,
    title: String,
    /** How to look after the part, shown from the card's arrow, as the official app's part pages. */
    careGuide: String,
    firstValue: String,
    firstLabel: String,
    secondValue: String,
    secondLabel: String,
    secondColor: Color? = null,
    onReset: () -> Unit,
) {
    var confirm by remember { mutableStateOf(false) }
    var guide by remember { mutableStateOf(false) }
    Surface(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 6.dp),
        shape = MaterialTheme.shapes.extraLarge,
        color = MaterialTheme.colorScheme.surfaceContainer,
    ) {
        Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Surface(shape = RoundedCornerShape(20.dp), color = iconTint.copy(alpha = 0.12f), modifier = Modifier.size(84.dp)) {
                    Box(contentAlignment = Alignment.Center) { Icon(icon, null, Modifier.size(44.dp), iconTint) }
                }
                Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Medium)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        MaintenanceFigure(firstValue, firstLabel, Modifier.weight(1f))
                        Box(Modifier.width(1.dp).height(32.dp).background(MaterialTheme.colorScheme.outlineVariant))
                        MaintenanceFigure(secondValue, secondLabel, Modifier.weight(1f).padding(start = 12.dp), secondColor)
                    }
                }
                IconButton(onClick = { guide = true }) {
                    Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, contentDescription = stringResource(Res.string.settings_care_how, title.lowercase(), title))
                }
            }
            // A light button, as the official card's: resetting is occasional and asks first.
            FilledTonalButton(
                onClick = { confirm = true },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                ),
            ) { Text(stringResource(Res.string.settings_reset)) }
        }
    }
    if (confirm) {
        AlertDialog(
            onDismissRequest = { confirm = false },
            title = { Text(stringResource(Res.string.settings_reset_title, title.lowercase(), title)) },
            text = { Text(stringResource(Res.string.settings_reset_text)) },
            confirmButton = { TextButton(onClick = { confirm = false; onReset() }) { Text(stringResource(Res.string.settings_reset)) } },
            dismissButton = { TextButton(onClick = { confirm = false }) { Text(stringResource(Res.string.settings_cancel)) } },
        )
    }
    if (guide) {
        AlertDialog(
            onDismissRequest = { guide = false },
            icon = { Icon(icon, null, tint = iconTint) },
            title = { Text(title) },
            text = { Text(careGuide) },
            confirmButton = { TextButton(onClick = { guide = false }) { Text(stringResource(Res.string.settings_close)) } },
        )
    }
}

@Composable
private fun MaintenanceFigure(value: String, label: String, modifier: Modifier = Modifier, color: Color? = null) {
    Column(modifier) {
        Text(value, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.SemiBold, color = color ?: MaterialTheme.colorScheme.onSurface)
        Text(label, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

/** A part the robot counts uses of rather than timing: no lifetime, just the count and a reset. */
@Composable
private fun CountedConsumableItem(icon: ImageVector, iconTint: Color, title: String, careGuide: String, count: Long, countLabel: String, advice: Pair<String, String>, onReset: () -> Unit) =
    MaintenanceCard(
        icon = icon, iconTint = iconTint, title = title, careGuide = careGuide,
        firstValue = "$count", firstLabel = countLabel,
        secondValue = advice.first, secondLabel = advice.second,
        onReset = onReset,
    )

@Composable
private fun ConsumableItem(
    icon: ImageVector,
    iconTint: Color,
    title: String,
    careGuide: String,
    usedSeconds: Long,
    maxSeconds: Long,
    onReset: () -> Unit,
) {
    val remaining = (maxSeconds - usedSeconds).coerceAtLeast(0L)
    val fraction = (remaining.toFloat() / maxSeconds).coerceIn(0f, 1f)
    val percent = (fraction * 100).toInt()
    val color = when {
        fraction > 0.5f -> AppColors.Good
        fraction > 0.2f -> AppColors.Warn
        else -> MaterialTheme.colorScheme.error
    }
    MaintenanceCard(
        icon = icon, iconTint = iconTint, title = title, careGuide = careGuide,
        firstValue = "${usedSeconds / 3600} h", firstLabel = stringResource(Res.string.settings_used),
        secondValue = "$percent %", secondLabel = stringResource(Res.string.settings_left), secondColor = color,
        onReset = onReset,
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HistoryScreen(
    records: List<CleanRecord>,
    summary: CleanSummary?,
    onRefresh: () -> Unit,
    onBack: () -> Unit,
    /** The map of a past clean (by its begin time); null when the robot no longer has it. */
    loadMap: (suspend (Long) -> ParsedMap?)? = null,
    loadEvents: (suspend (Long) -> List<CleanEvent>)? = null,
    roomName: (Int) -> String? = { null },
) {
    var opened by remember { mutableStateOf<CleanRecord?>(null) }
    opened?.let { record ->
        CleanRecordDetailScreen(record, loadMap, loadEvents, roomName, onBack = { opened = null })
        return
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Res.string.settings_history)) },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, stringResource(Res.string.settings_back)) }
                },
                actions = {
                    IconButton(onClick = onRefresh) { Icon(Icons.Default.Refresh, stringResource(Res.string.settings_refresh)) }
                },
            )
        },
    ) { padding ->
        CenteredContent {
            if (records.isEmpty()) {
                Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        Icon(Icons.Default.History, null, Modifier.size(48.dp), MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(stringResource(Res.string.settings_no_history), style = MaterialTheme.typography.titleMedium)
                        Text(
                            stringResource(Res.string.settings_no_history_text),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                        )
                        OutlinedButton(onClick = onRefresh) { Text(stringResource(Res.string.settings_load_history)) }
                    }
                }
            } else {
                // As the official app: cleans, and the camera cruises and pet searches ("Navigating").
                var navigating by remember { mutableStateOf(false) }
                val shown = records.filter { it.isPatrol == navigating }.sortedByDescending { it.beginEpoch }
                LazyColumn(contentPadding = padding) {
                    if (summary != null) {
                        item { CleanSummaryCard(summary) }
                    }
                    item {
                        SecondaryTabRow(selectedTabIndex = if (navigating) 1 else 0) {
                            Tab(selected = !navigating, onClick = { navigating = false }, text = { Text(stringResource(Res.string.settings_tab_cleaning)) })
                            Tab(selected = navigating, onClick = { navigating = true }, text = { Text(stringResource(Res.string.settings_tab_navigating)) })
                        }
                    }
                    if (shown.isEmpty()) {
                        item {
                            Text(
                                if (navigating) stringResource(Res.string.settings_no_cruises)
                                else stringResource(Res.string.settings_no_cleans),
                                modifier = Modifier.padding(24.dp),
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                    items(shown, key = { it.beginEpoch }) { record ->
                        Box(Modifier.clickable(enabled = loadMap != null || loadEvents != null) { opened = record }) {
                            CleanRecordRow(record)
                        }
                        HorizontalDivider(Modifier.padding(start = 72.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun CleanSummaryCard(summary: CleanSummary) {
    val totalHours = summary.totalTimeSec / 3600
    val totalMinutes = (summary.totalTimeSec % 3600) / 60
    val totalAreaM2 = summary.totalAreaMm2 / 1_000_000f
    Surface(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        color = MaterialTheme.colorScheme.primaryContainer,
        shape = MaterialTheme.shapes.extraLarge,
    ) {
        Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text(stringResource(Res.string.settings_lifetime_stats), style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                SummaryStatItem("${summary.totalCount}", stringResource(Res.string.settings_sessions))
                SummaryStatItem("${totalAreaM2.roundToInt()} m²", stringResource(Res.string.settings_total_area))
                SummaryStatItem(
                    if (totalHours > 0) "${totalHours}h ${totalMinutes}m" else "${totalMinutes}m",
                    stringResource(Res.string.settings_total_time),
                )
                summary.dustCollectionCount?.let { SummaryStatItem("$it", stringResource(Res.string.settings_bin_emptied)) }
            }
        }
    }
}

@Composable
private fun SummaryStatItem(value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(value, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

/**
 * One past clean: its map with the path the robot took (`get_clean_record_map`), the figures of the
 * record, and its timeline (`get_clean_detail` events: start, rooms with their settings, mop washes,
 * auto-empty, settings changed by hand, finish), laid out like the official app's.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CleanRecordDetailScreen(
    record: CleanRecord,
    loadMap: (suspend (Long) -> ParsedMap?)?,
    loadEvents: (suspend (Long) -> List<CleanEvent>)?,
    roomName: (Int) -> String?,
    onBack: () -> Unit,
) {
    var map by remember(record) { mutableStateOf<ParsedMap?>(null) }
    var mapLoaded by remember(record) { mutableStateOf(false) }
    var events by remember(record) { mutableStateOf<List<CleanEvent>?>(null) }
    LaunchedEffect(record) {
        map = loadMap?.let { runCatching { it(record.beginEpoch) }.getOrNull() }
        mapLoaded = true
    }
    LaunchedEffect(record) {
        events = loadEvents?.let { load -> runCatching { load(record.beginEpoch) }.getOrDefault(emptyList()) } ?: emptyList()
    }
    // Described again once the map arrives, whichever of the two loads first.
    val changes = remember(events, map) {
        events?.let { list ->
            // The names the user gave the rooms; a past clean's map only carries placeholders ("Room 1").
            describeCleanEvents(list) { id -> roomName(id) ?: map?.rooms?.firstOrNull { it.id == id }?.name?.takeIf { it.isNotBlank() } }
        }
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(formatCleanRecordTime(record.beginEpoch)) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, stringResource(Res.string.settings_back)) } },
            )
        },
    ) { padding ->
        CenteredContent {
            LazyColumn(contentPadding = padding) {
                item {
                    Surface(
                        Modifier.fillMaxWidth().height(320.dp).padding(16.dp),
                        shape = MaterialTheme.shapes.large,
                        color = MaterialTheme.colorScheme.surfaceContainer,
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            val m = map
                            when {
                                m != null -> SavedMapImage(m, Modifier.fillMaxSize().padding(8.dp))
                                mapLoaded -> Text(stringResource(Res.string.settings_no_map_for_clean), style = MaterialTheme.typography.bodySmall)
                                else -> CircularProgressIndicator(Modifier.size(24.dp), strokeWidth = 2.dp)
                            }
                        }
                    }
                }
                item { CleanRecordRow(record) }
                item { HorizontalDivider() }
                item { Text(stringResource(Res.string.settings_timeline), style = MaterialTheme.typography.titleSmall, modifier = Modifier.padding(16.dp)) }
                val list = changes
                when {
                    list == null -> item {
                        Text(stringResource(Res.string.settings_timeline_reading), Modifier.padding(horizontal = 16.dp), style = MaterialTheme.typography.bodySmall)
                    }
                    list.isEmpty() -> item {
                        Text(stringResource(Res.string.settings_no_timeline), Modifier.padding(horizontal = 16.dp), style = MaterialTheme.typography.bodySmall)
                    }
                    else -> items(list.size) { i ->
                        TimelineRow(list[i], first = i == 0, last = i == list.lastIndex)
                    }
                }
            }
        }
    }
}

/**
 * One line of a past clean's timeline, laid out as the official app does: the time on the left for a
 * main step, a vertical line with a filled check for main steps and a small open circle for the steps
 * in between, the main step's details in a card, and a step in between with its time under it.
 */
@Composable
internal fun TimelineRow(entry: CleanTimelineEntry, first: Boolean, last: Boolean) {
    val line = MaterialTheme.colorScheme.primary
    Row(Modifier.fillMaxWidth().height(IntrinsicSize.Min).padding(horizontal = 16.dp)) {
        Text(
            if (entry.milestone) formatClockTime(entry.epoch) else "",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.width(56.dp).padding(top = 14.dp),
        )
        Box(
            Modifier.width(32.dp).fillMaxHeight().drawBehind {
                val x = size.width / 2
                val nodeY = 24.dp.toPx()
                val stroke = 2.dp.toPx()
                if (!first) drawLine(line, androidx.compose.ui.geometry.Offset(x, 0f), androidx.compose.ui.geometry.Offset(x, nodeY), stroke)
                if (!last) drawLine(line, androidx.compose.ui.geometry.Offset(x, nodeY), androidx.compose.ui.geometry.Offset(x, size.height), stroke)
            },
            contentAlignment = Alignment.TopCenter,
        ) {
            if (entry.milestone) {
                Box(
                    Modifier.padding(top = 13.dp).size(22.dp).clip(CircleShape).background(line),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(Icons.Default.Check, null, Modifier.size(14.dp), tint = MaterialTheme.colorScheme.onPrimary)
                }
            } else {
                Box(
                    Modifier.padding(top = 18.dp).size(12.dp).clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surface)
                        .border(2.dp, line, CircleShape),
                )
            }
        }
        Column(Modifier.weight(1f).padding(start = 8.dp, top = 10.dp, bottom = 14.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                entry.title,
                style = if (entry.milestone) MaterialTheme.typography.titleMedium else MaterialTheme.typography.bodyLarge,
            )
            if (entry.milestone && entry.settings != null) {
                RoomSettingsCard(entry.settings)
            } else if (entry.milestone && entry.preferences.isNotEmpty()) {
                // The official app's start card: its cleaning preferences as bullets, the mode last.
                Surface(shape = MaterialTheme.shapes.large, color = MaterialTheme.colorScheme.surfaceContainerHigh) {
                    Column(Modifier.padding(horizontal = 16.dp, vertical = 12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                        val lines = entry.preferences.map { stringResource(cleanPreferenceText(it)) } + listOfNotNull(entry.detail)
                        lines.forEach { text ->
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                Text("•", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                Text(text, style = MaterialTheme.typography.bodyMedium)
                            }
                        }
                    }
                }
            } else if (entry.milestone) {
                entry.detail?.let {
                    Surface(shape = MaterialTheme.shapes.large, color = MaterialTheme.colorScheme.surfaceContainerHigh) {
                        Text(it, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp))
                    }
                }
            } else {
                entry.detail?.let {
                    Text(it, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                Text(formatClockTime(entry.epoch), style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

/**
 * How a room was cleaned, as the official app's card: the mode, then suction, water, route and passes
 * as small icons separated by bars (quiet suction is a moon, water a drop).
 */
@OptIn(androidx.compose.foundation.layout.ExperimentalLayoutApi::class)
@Composable
private fun RoomSettingsCard(settings: CleanRunSettings) {
    val tint = MaterialTheme.colorScheme.onSurfaceVariant
    Surface(shape = MaterialTheme.shapes.large, color = MaterialTheme.colorScheme.surfaceContainerHigh) {
        Column(Modifier.padding(horizontal = 16.dp, vertical = 12.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            settings.mode?.let { Text(it, style = MaterialTheme.typography.bodyLarge) }
            val parts = buildList<Pair<ImageVector?, String>> {
                settings.suctionLabel?.let { add((if (settings.suction == 1) Icons.Default.Bedtime else Icons.Default.Air) to it) }
                settings.waterLabel?.let { add(Icons.Default.WaterDrop to it) }
                settings.routeLabel?.let { add(Icons.Default.Route to it) }
                settings.passes?.takeIf { it > 0 }?.let { add(null to "×$it") }
            }
            if (parts.isNotEmpty()) {
                // Each icon stays with its label; a whole item moves to the next line when space runs out.
                FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                    parts.forEachIndexed { i, (icon, label) ->
                        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                            if (i > 0) Text("|", style = MaterialTheme.typography.bodySmall, color = tint.copy(alpha = 0.5f))
                            icon?.let { Icon(it, contentDescription = null, tint = tint, modifier = Modifier.size(16.dp)) }
                            Text(label, style = MaterialTheme.typography.bodySmall, color = tint, maxLines = 1, softWrap = false)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun CleanRecordRow(record: CleanRecord) {
    val areaM2 = record.areaMm2 / 1_000_000f
    val durationMin = record.durationSeconds / 60L
    val dateStr = formatCleanRecordTime(record.beginEpoch)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = if (record.complete == 1) MaterialTheme.colorScheme.primaryContainer
                    else MaterialTheme.colorScheme.errorContainer,
            modifier = Modifier.size(44.dp),
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    if (record.complete == 1) Icons.Default.CheckCircle else Icons.Default.Warning,
                    null,
                    Modifier.size(22.dp),
                    if (record.complete == 1) MaterialTheme.colorScheme.onPrimaryContainer
                    else MaterialTheme.colorScheme.onErrorContainer,
                )
            }
        }
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(
                listOfNotNull(dateStr, record.typeLabel).joinToString(" · "),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
            )
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(
                    buildAnnotatedString {
                        withStyle(SpanStyle(fontWeight = FontWeight.SemiBold)) { append(oneDecimal(areaM2)) }
                        append(" m²")
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    buildAnnotatedString {
                        withStyle(SpanStyle(fontWeight = FontWeight.SemiBold)) { append("$durationMin") }
                        append(" min")
                    },
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                record.efficiencyM2PerMinute?.let { e ->
                    Text(
                        buildAnnotatedString {
                            withStyle(SpanStyle(fontWeight = FontWeight.SemiBold)) { append(oneDecimal(e)) }
                            append(" m²/min")
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
            record.detailLine?.let {
                Text(it, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DockSettingsScreen(
    dockSettings: DockSettings,
    dockCapabilities: DockCapabilities,
    dockType: Int?,
    dockStatus: Int?,
    busy: Boolean,
    onWashModeChange: (Int) -> Unit,
    onWashFrequencyChange: (Int, Int?) -> Unit,
    onAutoEmptyModeChange: (Int) -> Unit,
    onEmptyWaterTank: () -> Unit,
    features: List<BetaFeatureState>,
    onFeatureToggle: (String, Int) -> Unit,
    valleySettings: ValleySettings?,
    onMopDryingChange: (Boolean) -> Unit,
    onDryStartChange: (start: Int, hour: Int?, minute: Int?) -> Unit = { _, _, _ -> },
    onDryTimeChange: (Int) -> Unit,
    onWashWaterTemperatureChange: (Int) -> Unit,
    onOpenValley: () -> Unit,
    onBack: () -> Unit,
) {
    var dialogTarget by remember { mutableStateOf<String?>(null) }
    var confirmEmptyTank by remember { mutableStateOf(false) }

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

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Res.string.settings_dock_settings_title)) },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, stringResource(Res.string.settings_back)) }
                },
            )
        },
    ) { padding ->
        CenteredContent {
            LazyColumn(contentPadding = padding) {
                item {
                    SettingRowClickable(
                        icon = Icons.Default.WaterDrop,
                        iconTint = AppColors.Water,
                        title = stringResource(Res.string.settings_mop_wash_frequency),
                        subtitle = washFrequencyLabel(smartWash),
                        enabled = washFrequencyEnabled,
                        onClick = { dialogTarget = "wash_freq" },
                    )
                }
                item { HorizontalDivider(Modifier.padding(start = 72.dp)) }
                item {
                    SettingRowClickable(
                        icon = Icons.Default.WaterDrop,
                        iconTint = AppColors.AccentCyan,
                        title = stringResource(Res.string.settings_washing_mode),
                        subtitle = dockCapabilities.washModeLabel(dockSettings.washMode, dockType),
                        enabled = washModeEnabled,
                        onClick = { dialogTarget = "wash_mode" },
                    )
                }
                dockSettings.washWaterTemperature?.let { temperature ->
                    item { HorizontalDivider(Modifier.padding(start = 72.dp)) }
                    item {
                        SettingRowClickable(
                            icon = Icons.Default.Thermostat,
                            iconTint = AppColors.Warn,
                            title = stringResource(Res.string.settings_wash_water_temperature),
                            subtitle = DockOptions.washWaterTemperature.labelFor(temperature),
                            enabled = !busy,
                            onClick = { dialogTarget = "wash_temp" },
                        )
                    }
                }
                dockSettings.mopDrying?.let { drying ->
                    item { HorizontalDivider(Modifier.padding(start = 72.dp)) }
                    item {
                        if (dockCapabilities.dryerSchedule && dockSettings.dryStart != null) {
                            // Off, after each clean, or daily at a set time (captured status 0 / 1 / 2 on an a296).
                            SettingRowClickable(
                                icon = Icons.Default.Air,
                                iconTint = AppColors.AccentCyan,
                                title = stringResource(Res.string.settings_mop_drying),
                                subtitle = dryStartLabel(dockSettings),
                                enabled = !busy,
                                onClick = { dialogTarget = "dry_start" },
                            )
                        } else {
                            SettingRowSwitch(
                                icon = Icons.Default.Air,
                                iconTint = AppColors.AccentCyan,
                                title = stringResource(Res.string.settings_mop_drying),
                                subtitle = if (drying) stringResource(Res.string.settings_mop_drying_on) else stringResource(Res.string.settings_mop_drying_off),
                                checked = drying,
                                enabled = !busy,
                                onCheckedChange = onMopDryingChange,
                            )
                        }
                    }
                    dockSettings.dryTimeSeconds?.let { seconds ->
                        item { HorizontalDivider(Modifier.padding(start = 72.dp)) }
                        item {
                            SettingRowClickable(
                                icon = Icons.Default.Schedule,
                                iconTint = AppColors.AccentCyan,
                                title = stringResource(Res.string.settings_drying_time),
                                subtitle = dryTimeLabel(seconds),
                                enabled = !busy,
                                onClick = { dialogTarget = "dry_time" },
                            )
                        }
                    }
                }
                item { HorizontalDivider(Modifier.padding(start = 72.dp)) }
                item {
                    SettingRowClickable(
                        icon = Icons.Default.Delete,
                        iconTint = AppColors.Good,
                        title = stringResource(Res.string.settings_auto_empty_mode),
                        subtitle = dockCapabilities.autoEmptyModeLabel(dockSettings.autoEmptyMode),
                        enabled = autoEmptyEnabled,
                        onClick = { dialogTarget = "auto_empty" },
                    )
                }
                valleySettings?.let { valley ->
                    item { HorizontalDivider(Modifier.padding(start = 72.dp)) }
                    item {
                        SettingRowClickable(
                            icon = Icons.Default.Schedule,
                            iconTint = AppColors.Good,
                            title = stringResource(Res.string.settings_off_peak_electricity),
                            subtitle = if (valley.enabled) stringResource(Res.string.settings_time_range, clockText(valley.startHour, valley.startMinute), clockText(valley.endHour, valley.endMinute)) else stringResource(Res.string.settings_off),
                            enabled = !busy,
                            onClick = onOpenValley,
                        )
                    }
                }
                item { HorizontalDivider(Modifier.padding(start = 72.dp)) }
                item {
                    SettingRowClickable(
                        icon = Icons.Default.WaterDrop,
                        iconTint = AppColors.Water,
                        title = stringResource(Res.string.settings_internal_tank),
                        subtitle = stringResource(Res.string.settings_internal_tank_sub),
                        enabled = !busy,
                        onClick = { confirmEmptyTank = true },
                    )
                }
                items(features, key = { it.def.id }) { f ->
                    HorizontalDivider(Modifier.padding(start = 72.dp))
                    FeatureToggleRow(f, onFeatureToggle)
                }
            }
        }
    }

    if (confirmEmptyTank) {
        AlertDialog(
            onDismissRequest = { confirmEmptyTank = false },
            title = { Text(stringResource(Res.string.settings_empty_tank_title)) },
            text = { Text(stringResource(Res.string.settings_empty_tank_text)) },
            confirmButton = {
                TextButton(onClick = { confirmEmptyTank = false; onEmptyWaterTank() }) { Text(stringResource(Res.string.settings_empty)) }
            },
            dismissButton = { TextButton(onClick = { confirmEmptyTank = false }) { Text(stringResource(Res.string.settings_cancel)) } },
        )
    }

    when (dialogTarget) {
        "wash_freq" -> SimplePickerDialog(
            title = stringResource(Res.string.settings_mop_wash_frequency),
            options = washFrequencyOptions.map { it.label },
            selected = washFrequencyIndex,
            onSelect = {
                val option = washFrequencyOptions[it]
                onWashFrequencyChange(option.mode, option.intervalMinutes)
                dialogTarget = null
            },
            enabled = washFrequencyEnabled,
            onDismiss = { dialogTarget = null },
        )
        "wash_mode" -> SimplePickerDialog(
            title = stringResource(Res.string.settings_washing_mode),
            options = washModes.map { it.second },
            selected = washModeIndex,
            onSelect = { onWashModeChange(washModes[it].first); dialogTarget = null },
            enabled = washModeEnabled,
            onDismiss = { dialogTarget = null },
        )
        "wash_temp" -> SimplePickerDialog(
            title = stringResource(Res.string.settings_wash_water_temperature),
            options = DockOptions.washWaterTemperature.map { it.label },
            selected = DockOptions.washWaterTemperature.indexOfFirst { it.code == dockSettings.washWaterTemperature },
            onSelect = { onWashWaterTemperatureChange(DockOptions.washWaterTemperature[it].code); dialogTarget = null },
            enabled = !busy,
            onDismiss = { dialogTarget = null },
        )
        "dry_start" -> SimplePickerDialog(
            title = stringResource(Res.string.settings_mop_drying),
            options = listOf(stringResource(Res.string.settings_off), stringResource(Res.string.settings_after_cleaning), stringResource(Res.string.settings_at_set_time)),
            selected = dockSettings.dryStart ?: -1,
            onSelect = {
                if (it == DRY_START_SCHEDULED) dialogTarget = "dry_schedule"
                else { onDryStartChange(it, null, null); dialogTarget = null }
            },
            enabled = !busy,
            onDismiss = { dialogTarget = null },
        )
        "dry_schedule" -> DryScheduleDialog(
            hour = dockSettings.dryScheduleHour ?: 17,
            minute = dockSettings.dryScheduleMinute ?: 0,
            onSave = { hour, minute -> onDryStartChange(DRY_START_SCHEDULED, hour, minute); dialogTarget = null },
            onDismiss = { dialogTarget = null },
        )
        "dry_time" -> SimplePickerDialog(
            title = stringResource(Res.string.settings_drying_time),
            options = DockOptions.dryTime.map { it.label },
            selected = DockOptions.dryTime.indexOfFirst { it.code == dockSettings.dryTimeSeconds },
            onSelect = { onDryTimeChange(DockOptions.dryTime[it].code); dialogTarget = null },
            enabled = !busy,
            onDismiss = { dialogTarget = null },
        )
        "auto_empty" -> SimplePickerDialog(
            title = stringResource(Res.string.settings_auto_empty_mode),
            options = autoEmptyModes.map { it.second },
            selected = autoEmptyModes.indexOfFirst { it.first == dockSettings.autoEmptyMode },
            onSelect = { onAutoEmptyModeChange(autoEmptyModes[it].first); dialogTarget = null },
            enabled = autoEmptyEnabled,
            onDismiss = { dialogTarget = null },
        )
    }
}

private fun clockText(hour: Int, minute: Int) = "${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ValleyScreen(
    valleySettings: ValleySettings?,
    onEnable: (startHour: Int, startMinute: Int, endHour: Int, endMinute: Int) -> Unit,
    onDisable: () -> Unit,
    onBack: () -> Unit,
) {
    val settings = valleySettings ?: ValleySettings()
    var enabled by remember(valleySettings?.enabled) { mutableStateOf(settings.enabled) }
    var startHour by remember(valleySettings) { mutableStateOf(settings.startHour) }
    var startMinute by remember(valleySettings) { mutableStateOf(settings.startMinute) }
    var endHour by remember(valleySettings) { mutableStateOf(settings.endHour) }
    var endMinute by remember(valleySettings) { mutableStateOf(settings.endMinute) }

    fun commit() = if (enabled) onEnable(startHour, startMinute, endHour, endMinute) else onDisable()

    // Each tap on a stepper changes a time, so the robot is written once they stop, not on every tap.
    var timesTouched by remember { mutableStateOf(false) }
    LaunchedEffect(startHour, startMinute, endHour, endMinute) {
        if (!timesTouched) return@LaunchedEffect
        kotlinx.coroutines.delay(800)
        commit()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Res.string.settings_off_peak_electricity)) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, stringResource(Res.string.settings_back)) } },
            )
        },
    ) { padding ->
        CenteredContent {
            Column(
                modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState()).padding(padding).padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                ) {
                    Column(Modifier.weight(1f)) {
                        Text(stringResource(Res.string.settings_valley_limit), style = MaterialTheme.typography.bodyLarge)
                        Text(
                            if (enabled) stringResource(Res.string.settings_time_range, clockText(startHour, startMinute), clockText(endHour, endMinute)) else stringResource(Res.string.settings_off),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    Switch(checked = enabled, onCheckedChange = {
                        // A robot with off-peak off reports 00:00 to 00:00 (a296); turning it on with that
                        // would send an empty window, so start from 22:00 to 06:00 instead.
                        if (it && startHour == endHour && startMinute == endMinute) {
                            startHour = 22; startMinute = 0; endHour = 6; endMinute = 0
                        }
                        enabled = it
                        commit()
                    })
                }
                if (enabled) {
                    HorizontalDivider()
                    Text(stringResource(Res.string.settings_start_time), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    ClockStepper(startHour, startMinute, onHourChange = { startHour = it; timesTouched = true }, onMinuteChange = { startMinute = it; timesTouched = true })
                    Text(stringResource(Res.string.settings_end_time), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    ClockStepper(endHour, endMinute, onHourChange = { endHour = it; timesTouched = true }, onMinuteChange = { endMinute = it; timesTouched = true })
                }
            }
        }
    }
}

@Composable
private fun dryStartLabel(dock: DockSettings): String = when (dock.dryStart) {
    DRY_START_OFF -> stringResource(Res.string.settings_off)
    DRY_START_AFTER_CLEANING -> stringResource(Res.string.settings_after_cleaning)
    DRY_START_SCHEDULED -> {
        val h = dock.dryScheduleHour; val m = dock.dryScheduleMinute
        if (h != null && m != null) stringResource(Res.string.settings_daily_at, clockText(h, m)) else stringResource(Res.string.settings_at_set_time)
    }
    else -> stringResource(Res.string.settings_unknown_value, "${dock.dryStart}")
}

/** The time the dock starts drying the mops each day. */
@Composable
private fun DryScheduleDialog(hour: Int, minute: Int, onSave: (Int, Int) -> Unit, onDismiss: () -> Unit) {
    var h by remember { mutableIntStateOf(hour) }
    var m by remember { mutableIntStateOf(minute) }
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(Res.string.settings_dry_mops_at)) },
        text = { ClockStepper(h, m, onHourChange = { h = it }, onMinuteChange = { m = it }) },
        confirmButton = { TextButton(onClick = { onSave(h, m) }) { Text(stringResource(Res.string.settings_save)) } },
        dismissButton = { TextButton(onClick = onDismiss) { Text(stringResource(Res.string.settings_cancel)) } },
    )
}

@Composable
private fun SimplePickerDialog(
    title: String,
    options: List<String>,
    selected: Int,
    onSelect: (Int) -> Unit,
    enabled: Boolean = true,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Column(Modifier.verticalScroll(rememberScrollState())) {
                options.forEachIndexed { index, option ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            // The whole row picks the option, not only the small radio button.
                            .clickable(enabled = enabled) { onSelect(index) }
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        RadioButton(selected = index == selected, enabled = enabled, onClick = { onSelect(index) })
                        Spacer(Modifier.width(8.dp))
                        Text(option, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        },
        confirmButton = {},
        dismissButton = { TextButton(onClick = onDismiss) { Text(stringResource(Res.string.settings_cancel)) } },
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RobotSettingsScreen(
    robotSettings: RobotSettings,
    onFindRobot: () -> Unit,
    onVolumeChange: (Int) -> Job,
    onChildLockChange: (Boolean) -> Unit,
    onLedChange: (Boolean) -> Unit,
    features: List<BetaFeatureState>,
    onFeatureToggle: (String, Int) -> Unit,
    onVoicePack: (() -> Unit)? = null,
    onBack: () -> Unit,
) {
    var findTriggered by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Res.string.settings_robot_settings)) },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, stringResource(Res.string.settings_back)) }
                },
            )
        },
    ) { padding ->
        CenteredContent {
            LazyColumn(contentPadding = padding) {
                item {
                    SettingRowClickable(
                        icon = Icons.Default.Sensors,
                        iconTint = MaterialTheme.colorScheme.primary,
                        title = stringResource(Res.string.settings_find_robot),
                        subtitle = if (findTriggered) stringResource(Res.string.settings_beeping) else stringResource(Res.string.settings_find_robot_sub),
                        onClick = { onFindRobot(); findTriggered = true },
                    )
                }
                item { HorizontalDivider(Modifier.padding(start = 72.dp)) }
                item {
                    VolumeSliderRow(volume = robotSettings.volume, onVolumeChange = onVolumeChange)
                }
                if (onVoicePack != null) {
                    item { HorizontalDivider(Modifier.padding(start = 72.dp)) }
                    item {
                        SettingRowClickable(
                            icon = Icons.Default.Sensors,
                            iconTint = MaterialTheme.colorScheme.primary,
                            title = stringResource(Res.string.settings_voice_pack),
                            subtitle = stringResource(Res.string.settings_voice_pack_sub),
                            onClick = onVoicePack,
                        )
                    }
                }
                item { HorizontalDivider(Modifier.padding(start = 72.dp)) }
                item {
                    SettingRowSwitch(
                        icon = Icons.Default.Lock,
                        iconTint = AppColors.Warn,
                        title = stringResource(Res.string.settings_child_lock),
                        subtitle = if (robotSettings.childLock) stringResource(Res.string.settings_child_lock_on) else stringResource(Res.string.settings_child_lock_off),
                        checked = robotSettings.childLock,
                        onCheckedChange = onChildLockChange,
                    )
                }
                item { HorizontalDivider(Modifier.padding(start = 72.dp)) }
                item {
                    SettingRowSwitch(
                        icon = Icons.Default.Lightbulb,
                        iconTint = AppColors.AccentYellow,
                        title = stringResource(Res.string.settings_status_led),
                        subtitle = if (robotSettings.ledEnabled) stringResource(Res.string.settings_led_on) else stringResource(Res.string.settings_led_off),
                        checked = robotSettings.ledEnabled,
                        onCheckedChange = onLedChange,
                    )
                }
                items(features, key = { it.def.id }) { f ->
                    HorizontalDivider(Modifier.padding(start = 72.dp))
                    FeatureToggleRow(f, onFeatureToggle)
                }
            }
        }
    }
}

/**
 * The robot's volume as a 0-100 slider, like the official app's. The value is only sent when the
 * thumb is let go, so a drag is one command rather than one per step. Until that command has been
 * answered the thumb stays where it was let go; after that it follows what the robot holds, which
 * after a refused change is the value read back from the robot.
 */
@Composable
private fun VolumeSliderRow(volume: Int, onVolumeChange: (Int) -> Job) {
    // The position while dragging, or while the let-go value is being sent; null shows the robot's.
    var heldPosition by remember { mutableStateOf<Float?>(null) }
    var dragging by remember { mutableStateOf(false) }
    // Only the last change sent may let go of the thumb, so an earlier answer cannot move it mid-drag.
    var lastSent by remember { mutableStateOf<Job?>(null) }
    val position = heldPosition ?: volume.coerceIn(VolumeRange).toFloat()
    val shown = volumeFromSlider(position)

    Column(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            Surface(shape = RoundedCornerShape(12.dp), color = AppColors.Water.copy(alpha = 0.12f), modifier = Modifier.size(44.dp)) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        if (shown == 0) Icons.AutoMirrored.Filled.VolumeOff else Icons.AutoMirrored.Filled.VolumeUp,
                        null,
                        Modifier.size(22.dp),
                        AppColors.Water,
                    )
                }
            }
            Text(stringResource(Res.string.settings_volume), style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
            Text(volumeLabel(shown), style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Row(
            // Lined up with the title, past the 44dp icon and the 16dp gap.
            modifier = Modifier.fillMaxWidth().padding(start = 60.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Icon(Icons.AutoMirrored.Filled.VolumeMute, null, Modifier.size(18.dp), MaterialTheme.colorScheme.onSurfaceVariant)
            Slider(
                value = position,
                onValueChange = { dragging = true; heldPosition = it },
                onValueChangeFinished = {
                    dragging = false
                    heldPosition?.let { held ->
                        val target = volumeFromSlider(held)
                        if (target == volume) {
                            lastSent = null
                            heldPosition = null
                        } else {
                            heldPosition = target.toFloat()
                            val job = onVolumeChange(target)
                            lastSent = job
                            job.invokeOnCompletion {
                                if (lastSent === job && !dragging) heldPosition = null
                            }
                        }
                    }
                },
                valueRange = VolumeRange.first.toFloat()..VolumeRange.last.toFloat(),
                colors = SliderDefaults.colors(thumbColor = AppColors.Water, activeTrackColor = AppColors.Water),
                modifier = Modifier.weight(1f),
            )
            Icon(Icons.AutoMirrored.Filled.VolumeUp, null, Modifier.size(18.dp), MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

/** One feature the robot reports, as a switch under the page it belongs to. */
@Composable
private fun FeatureToggleRow(f: BetaFeatureState, onToggle: (String, Int) -> Unit) {
    val icon = when (f.def.section) {
        FeatureSection.CARPET -> Icons.Default.GridView
        FeatureSection.FLOOR -> Icons.Default.CleaningServices
        FeatureSection.OBSTACLE -> Icons.Default.Sensors
        FeatureSection.PETS -> Icons.Default.Pets
        FeatureSection.DOCK -> Icons.Default.Home
        FeatureSection.ROBOT -> Icons.Default.Settings
        FeatureSection.BETA -> Icons.Default.Science
    }
    val choices = f.def.choices
    if (choices == null) {
        SettingRowSwitch(
            icon = icon,
            iconTint = AppColors.AccentCyan,
            title = f.def.title,
            subtitle = f.def.description,
            checked = f.enabled,
            onCheckedChange = { onToggle(f.def.id, if (it) 1 else 0) },
        )
        return
    }
    // A feature with more than two values, such as the collision mode or cleaning fluid dosing.
    var picking by remember { mutableStateOf(false) }
    SettingRowClickable(
        icon = icon,
        iconTint = AppColors.AccentCyan,
        title = f.def.title,
        subtitle = f.choiceLabel ?: f.def.description,
        onClick = { picking = true },
    )
    if (picking) {
        SimplePickerDialog(
            title = f.def.title,
            options = choices.map { it.second },
            selected = choices.indexOfFirst { it.first == f.value },
            onSelect = { onToggle(f.def.id, choices[it].first); picking = false },
            onDismiss = { picking = false },
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FeatureSectionScreen(
    title: String,
    features: List<BetaFeatureState>,
    onToggle: (String, Int) -> Unit,
    onBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, stringResource(Res.string.settings_back)) } },
            )
        },
    ) { padding ->
        CenteredContent {
            LazyColumn(contentPadding = padding) {
                items(features, key = { it.def.id }) { f ->
                    FeatureToggleRow(f, onToggle)
                    HorizontalDivider(Modifier.padding(start = 72.dp))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CarpetScreen(
    carpetMode: Int,
    modes: List<Pair<Int, String>>,
    features: List<BetaFeatureState>,
    onCarpetModeChange: (Int) -> Unit,
    onToggle: (String, Int) -> Unit,
    onBack: () -> Unit,
) {
    var showDialog by remember { mutableStateOf(false) }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Res.string.settings_carpet_cleaning)) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, stringResource(Res.string.settings_back)) } },
            )
        },
    ) { padding ->
        CenteredContent {
            LazyColumn(contentPadding = padding) {
                item {
                    SettingRowClickable(
                        icon = Icons.Default.GridView,
                        iconTint = AppColors.Good,
                        title = stringResource(Res.string.settings_carpet_mode),
                        subtitle = modes.firstOrNull { it.first == carpetMode }?.second ?: stringResource(Res.string.settings_unknown_value, "$carpetMode"),
                        onClick = { showDialog = true },
                    )
                }
                items(features, key = { it.def.id }) { f ->
                    HorizontalDivider(Modifier.padding(start = 72.dp))
                    FeatureToggleRow(f, onToggle)
                }
            }
        }
    }
    if (showDialog) {
        SimplePickerDialog(
            title = stringResource(Res.string.settings_carpet_mode),
            options = modes.map { it.second },
            selected = modes.indexOfFirst { it.first == carpetMode },
            onSelect = { onCarpetModeChange(modes[it].first); showDialog = false },
            onDismiss = { showDialog = false },
        )
    }
}

private const val BETA_FEATURES_ISSUE_URL = "https://github.com/DonSidro/LocalRock/issues/55"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BetaFeaturesScreen(
    features: List<BetaFeatureState>,
    loading: Boolean,
    unlisted: List<AdvertisedFeature>,
    onToggle: (String, Int) -> Unit,
    onBack: () -> Unit,
) {
    var showUnlisted by remember { mutableStateOf(false) }
    val uriHandler = LocalUriHandler.current
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Res.string.settings_beta_features)) },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, stringResource(Res.string.settings_back)) } },
            )
        },
    ) { padding ->
        CenteredContent {
            LazyColumn(contentPadding = padding) {
                item {
                    Text(
                        stringResource(Res.string.settings_beta_intro),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(16.dp),
                    )
                }
                if (loading) {
                    item {
                        Text(
                            stringResource(Res.string.settings_beta_checking),
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(16.dp),
                        )
                    }
                } else if (features.isEmpty() && unlisted.isEmpty()) {
                    item {
                        Text(
                            stringResource(Res.string.settings_beta_none),
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(16.dp),
                        )
                    }
                }
                items(features, key = { it.def.id }) { f ->
                    Column {
                        FeatureToggleRow(f, onToggle)
                        Text(
                            stringResource(Res.string.settings_robot_reply, "${f.raw}"),
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(start = 76.dp, end = 16.dp, bottom = 10.dp),
                        )
                        HorizontalDivider(Modifier.padding(start = 72.dp))
                    }
                }
                if (unlisted.isNotEmpty()) {
                    item {
                        Surface(onClick = { showUnlisted = !showUnlisted }, color = Color.Transparent) {
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Column(Modifier.weight(1f)) {
                                    Text(stringResource(Res.string.settings_also_advertised), style = MaterialTheme.typography.bodyLarge)
                                    Text(
                                        pluralStringResource(Res.plurals.settings_unlisted_count, unlisted.size, unlisted.size),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    )
                                }
                                Icon(
                                    if (showUnlisted) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                                    contentDescription = if (showUnlisted) stringResource(Res.string.settings_hide) else stringResource(Res.string.settings_show),
                                )
                            }
                        }
                        HorizontalDivider()
                    }
                    if (showUnlisted) {
                        item {
                            Column(Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                                Text(
                                    stringResource(Res.string.settings_unlisted_text),
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                )
                                TextButton(onClick = { uriHandler.openUri(BETA_FEATURES_ISSUE_URL) }) {
                                    Text(stringResource(Res.string.settings_report_github))
                                }
                                Row(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                                    Text(stringResource(Res.string.settings_feature), Modifier.weight(1f), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                                    Text(stringResource(Res.string.settings_bit), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                                }
                            }
                        }
                        items(unlisted, key = { it.bit }) { f ->
                            Column(Modifier.padding(horizontal = 16.dp)) {
                                HorizontalDivider()
                                Row(Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                                    Text(
                                        f.name ?: stringResource(Res.string.settings_unnamed),
                                        Modifier.weight(1f),
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = if (f.name == null) MaterialTheme.colorScheme.onSurfaceVariant else Color.Unspecified,
                                    )
                                    Text(f.bit.toString(), style = MaterialTheme.typography.bodyMedium)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SettingRowClickable(
    icon: ImageVector,
    iconTint: Color,
    title: String,
    subtitle: String,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    Surface(onClick = onClick, enabled = enabled, color = Color.Transparent) {
        Row(
            modifier = Modifier.fillMaxWidth().alpha(if (enabled) 1f else 0.38f).padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Surface(shape = RoundedCornerShape(12.dp), color = iconTint.copy(alpha = 0.12f), modifier = Modifier.size(44.dp)) {
                Box(contentAlignment = Alignment.Center) { Icon(icon, null, Modifier.size(22.dp), iconTint) }
            }
            Column(Modifier.weight(1f)) {
                Text(title, style = MaterialTheme.typography.bodyLarge)
                Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Icon(Icons.AutoMirrored.Filled.KeyboardArrowRight, null, tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.4f))
        }
    }
}

@Composable
private fun SettingRowInfo(
    icon: ImageVector,
    iconTint: Color,
    title: String,
    value: String,
    valueSupporting: String? = null,
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Surface(shape = RoundedCornerShape(12.dp), color = iconTint.copy(alpha = 0.12f), modifier = Modifier.size(44.dp)) {
            Box(contentAlignment = Alignment.Center) { Icon(icon, null, Modifier.size(22.dp), iconTint) }
        }
        Column(Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.bodyLarge)
            Text(value, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            if (valueSupporting != null) {
                Text(valueSupporting, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
private fun FirmwareUpdateRow(state: FirmwareUpdateState, onCheck: () -> Unit) {
    val checking = state is FirmwareUpdateState.Checking
    val value: String
    val supporting: String?
    val tint: Color
    when (state) {
        FirmwareUpdateState.Idle -> {
            value = stringResource(Res.string.settings_fw_check)
            supporting = stringResource(Res.string.settings_fw_check_sub)
            tint = MaterialTheme.colorScheme.primary
        }
        FirmwareUpdateState.Checking -> {
            value = stringResource(Res.string.settings_fw_checking)
            supporting = null
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        }
        is FirmwareUpdateState.UpToDate -> {
            value = stringResource(Res.string.settings_fw_up_to_date)
            supporting = state.current?.let { stringResource(Res.string.settings_fw_installed, it) }
            tint = AppColors.Good
        }
        is FirmwareUpdateState.Available -> {
            value = if (state.force) stringResource(Res.string.settings_fw_required) else stringResource(Res.string.settings_fw_available)
            supporting = buildString {
                append(state.current ?: "?"); append("  →  "); append(state.latest ?: "?")
                state.notes?.takeIf { it.isNotBlank() }?.let { append("\n"); append(it) }
            }
            tint = MaterialTheme.colorScheme.primary
        }
        is FirmwareUpdateState.Unavailable -> {
            value = stringResource(Res.string.settings_fw_unavailable)
            supporting = state.reason
            tint = AppColors.Warn
        }
    }
    Surface(onClick = onCheck, enabled = !checking, color = Color.Transparent) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Surface(shape = RoundedCornerShape(12.dp), color = tint.copy(alpha = 0.12f), modifier = Modifier.size(44.dp)) {
                Box(contentAlignment = Alignment.Center) { Icon(Icons.Default.SystemUpdate, null, Modifier.size(22.dp), tint) }
            }
            Column(Modifier.weight(1f)) {
                Text(stringResource(Res.string.settings_fw_updates), style = MaterialTheme.typography.bodyLarge)
                Text(value, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                if (supporting != null) {
                    Text(supporting, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }
            if (checking) {
                CircularProgressIndicator(Modifier.size(20.dp), strokeWidth = 2.dp)
            } else {
                Icon(Icons.Default.Refresh, null, tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f))
            }
        }
    }
}

@Composable
private fun SettingRowSwitch(
    icon: ImageVector,
    iconTint: Color,
    title: String,
    subtitle: String,
    checked: Boolean,
    enabled: Boolean = true,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Surface(shape = RoundedCornerShape(12.dp), color = iconTint.copy(alpha = 0.12f), modifier = Modifier.size(44.dp)) {
            Box(contentAlignment = Alignment.Center) { Icon(icon, null, Modifier.size(22.dp), iconTint) }
        }
        Column(Modifier.weight(1f)) {
            Text(title, style = MaterialTheme.typography.bodyLarge)
            Text(subtitle, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Switch(checked = checked, enabled = enabled, onCheckedChange = onCheckedChange)
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun RemoteControlScreen(
    onMove: (velocity: Double, omega: Double) -> Unit,
    onStop: () -> Unit,
    onBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Res.string.settings_remote_control_title)) },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, stringResource(Res.string.settings_back)) }
                },
            )
        },
    ) { padding ->
        CenteredContent {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    stringResource(Res.string.settings_rc_tap),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(Modifier.height(48.dp))
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    RcButton(Icons.Default.KeyboardArrowUp, stringResource(Res.string.settings_rc_forward)) { onMove(0.3, 0.0) }
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        RcButton(Icons.AutoMirrored.Filled.KeyboardArrowLeft, stringResource(Res.string.settings_rc_left)) { onMove(0.0, 0.3) }
                        FilledIconButton(
                            onClick = onStop,
                            modifier = Modifier.size(72.dp),
                            colors = IconButtonDefaults.filledIconButtonColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer,
                                contentColor = MaterialTheme.colorScheme.onErrorContainer,
                            ),
                        ) {
                            Icon(Icons.Default.Stop, stringResource(Res.string.settings_rc_stop), Modifier.size(32.dp))
                        }
                        RcButton(Icons.AutoMirrored.Filled.KeyboardArrowRight, stringResource(Res.string.settings_rc_right)) { onMove(0.0, -0.3) }
                    }
                    RcButton(Icons.Default.KeyboardArrowDown, stringResource(Res.string.settings_rc_backward)) { onMove(-0.3, 0.0) }
                }
                Spacer(Modifier.height(32.dp))
                Text(
                    stringResource(Res.string.settings_rc_each_tap),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
private fun RcButton(icon: ImageVector, contentDescription: String, onClick: () -> Unit) {
    FilledTonalIconButton(
        onClick = onClick,
        modifier = Modifier.size(72.dp),
    ) {
        Icon(icon, contentDescription, Modifier.size(32.dp))
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PinGoScreen(
    parsedMap: ParsedMap?,
    busy: Boolean,
    /** True while the robot drives to the pin; the spot clean waits until it is there. */
    travelling: Boolean = false,
    onSpot: (() -> Unit)? = null,
    onGoto: (x: Int, y: Int, onSent: () -> Unit) -> Unit,
    onBack: () -> Unit,
) {
    var tapPoint by remember { mutableStateOf<Offset?>(null) }
    var imageSize by remember { mutableStateOf(IntSize.Zero) }
    var sent by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Res.string.settings_pin_go)) },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, stringResource(Res.string.settings_back)) }
                },
                actions = {
                    if (tapPoint != null && parsedMap != null && !sent) {
                        TextButton(enabled = !busy, onClick = {
                            val tp = tapPoint ?: return@TextButton
                            val pm = parsedMap ?: return@TextButton
                            if (imageSize.width > 0 && imageSize.height > 0) {
                                // app_goto_target takes robot millimetres, not bitmap cells (#34).
                                val (xMm, yMm) = pm.canvasToMm(tp, imageSize.width.toFloat(), imageSize.height.toFloat())
                                onGoto(xMm, yMm) { if (tapPoint == tp) sent = true }
                            }
                        }) { Text(stringResource(Res.string.settings_go)) }
                    }
                },
            )
        },
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {
            if (parsedMap == null) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        Icon(Icons.Default.Map, null, Modifier.size(48.dp), MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(stringResource(Res.string.settings_map_not_loaded), style = MaterialTheme.typography.titleMedium)
                        Text(
                            stringResource(Res.string.settings_map_not_loaded_text),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 32.dp),
                        )
                    }
                }
            } else {
                val bitmap = rememberMapBitmap(parsedMap)
                val aspectRatio = parsedMap.width.toFloat() / parsedMap.height.toFloat()
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center,
                ) {
                    Canvas(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(aspectRatio)
                            .onSizeChanged { imageSize = it }
                            .pointerInput(parsedMap) {
                                detectTapGestures { offset ->
                                    tapPoint = offset
                                    sent = false
                                }
                            },
                    ) {
                        drawImage(
                            image = bitmap,
                            srcOffset = IntOffset.Zero,
                            srcSize = IntSize(bitmap.width, bitmap.height),
                            dstOffset = IntOffset.Zero,
                            dstSize = IntSize(size.width.toInt(), size.height.toInt()),
                            filterQuality = FilterQuality.Low,
                        )
                        tapPoint?.let { tp ->
                            drawCircle(color = AppColors.AccentRed.copy(alpha = 0.85f), radius = 22f, center = tp)
                            drawCircle(color = Color.White, radius = 9f, center = tp)
                        }
                    }
                }
                // As the official Pin & Go: once there, clean 1.5 m × 1.5 m around the pin.
                if (sent && onSpot != null) {
                    OutlinedButton(
                        onClick = onSpot,
                        enabled = !busy && !travelling,
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).padding(top = 12.dp),
                    ) { Text(if (travelling) stringResource(Res.string.settings_spot_once_there) else stringResource(Res.string.settings_spot_here)) }
                }
                Text(
                    text = when {
                        sent && !travelling -> stringResource(Res.string.settings_pin_arrived)
                        sent -> stringResource(Res.string.settings_pin_sent)
                        tapPoint != null -> stringResource(Res.string.settings_pin_target_set)
                        else -> stringResource(Res.string.settings_pin_tap_map)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    style = MaterialTheme.typography.bodySmall,
                    color = if (sent) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                )
            }
        }
    }
}


private fun oneDecimal(value: Float): String {
    val scaled = (value * 10f).roundToInt()
    return "${scaled / 10}.${(scaled % 10).absoluteValue}"
}

private fun cleanPreferenceText(p: CleanPreference): StringResource = when (p) {
    CleanPreference.HOME_PERCEPTION -> Res.string.timeline_pref_home_perception
    CleanPreference.ORDER_SEQUENTIAL -> Res.string.timeline_pref_order_sequential
    CleanPreference.ORDER_CUSTOM -> Res.string.timeline_pref_order_custom
    CleanPreference.ORDER_DEFAULT -> Res.string.timeline_pref_order_default
    CleanPreference.PATH_AI -> Res.string.timeline_pref_path_ai
    CleanPreference.PATH_FLOOR_DIRECTION -> Res.string.timeline_pref_path_floor
    CleanPreference.PATH_DIRECTION_SWITCH -> Res.string.timeline_pref_path_switch
    CleanPreference.COVERAGE_HIGH -> Res.string.timeline_pref_coverage_high
    CleanPreference.COVERAGE_LESS_COLLISION -> Res.string.timeline_pref_coverage_less_collision
}
