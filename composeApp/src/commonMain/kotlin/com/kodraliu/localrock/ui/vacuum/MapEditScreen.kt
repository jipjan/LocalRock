package com.kodraliu.localrock.ui.vacuum

import androidx.compose.foundation.horizontalScroll
import com.kodraliu.localrock.shared.vacuum.map.ERASER_USER
import com.kodraliu.localrock.shared.vacuum.map.LOW_SPACE_USER
import com.kodraliu.localrock.shared.vacuum.curtainStrip
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.WindowInsets
import com.kodraliu.localrock.shared.vacuum.RoomType
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.windowInsetsPadding
import com.kodraliu.localrock.ui.exceptBottom
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.HorizontalRule
import androidx.compose.material.icons.filled.MeetingRoom
import androidx.compose.material.icons.filled.Texture
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Surface
import androidx.compose.runtime.Stable
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.semantics.Role
import com.kodraliu.localrock.shared.vacuum.MAX_MAP_COORD_MM
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.calculateCentroid
import androidx.compose.foundation.gestures.calculatePan
import androidx.compose.foundation.gestures.calculateZoom
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Map
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.backhandler.BackHandler
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.toSize
import com.kodraliu.localrock.shared.vacuum.MAX_RESTRICTION_VERTICES
import com.kodraliu.localrock.shared.vacuum.restrictionsMatch
import com.kodraliu.localrock.shared.vacuum.thresholdStrip
import com.kodraliu.localrock.shared.vacuum.saveMapIndex
import com.kodraliu.localrock.shared.vacuum.zoneMatches
import com.kodraliu.localrock.shared.vacuum.restrictionVertexCount
import com.kodraliu.localrock.shared.vacuum.map.FloorMaterial
import com.kodraliu.localrock.shared.vacuum.map.MapZone
import com.kodraliu.localrock.shared.vacuum.map.ParsedMap
import com.kodraliu.localrock.shared.vacuum.map.VirtualWall
import com.kodraliu.localrock.shared.vacuum.map.ZoneKind
import com.kodraliu.localrock.shared.vacuum.map.roomAtNorm
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.hypot
import com.kodraliu.localrock.resources.Res
import com.kodraliu.localrock.resources.app_dismiss
import com.kodraliu.localrock.resources.common_cancel
import com.kodraliu.localrock.resources.common_save
import com.kodraliu.localrock.resources.mapedit_add_furniture
import com.kodraliu.localrock.resources.mapedit_add_to_map
import com.kodraliu.localrock.resources.mapedit_add_tool
import com.kodraliu.localrock.resources.mapedit_cant_change
import com.kodraliu.localrock.resources.mapedit_checking_maps
import com.kodraliu.localrock.resources.mapedit_close_editor
import com.kodraliu.localrock.resources.mapedit_count_carpets
import com.kodraliu.localrock.resources.mapedit_count_curtains
import com.kodraliu.localrock.resources.mapedit_count_erased
import com.kodraliu.localrock.resources.mapedit_count_furniture
import com.kodraliu.localrock.resources.mapedit_count_low_space
import com.kodraliu.localrock.resources.mapedit_count_no_go
import com.kodraliu.localrock.resources.mapedit_count_thresholds
import com.kodraliu.localrock.resources.mapedit_count_walls
import com.kodraliu.localrock.resources.mapedit_custom_name
import com.kodraliu.localrock.resources.mapedit_custom_name_text
import com.kodraliu.localrock.resources.mapedit_delete_kind
import com.kodraliu.localrock.resources.mapedit_detected_carpet
import com.kodraliu.localrock.resources.mapedit_detected_carpet_help
import com.kodraliu.localrock.resources.mapedit_discard
import com.kodraliu.localrock.resources.mapedit_discard_text
import com.kodraliu.localrock.resources.mapedit_discard_title
import com.kodraliu.localrock.resources.mapedit_done
import com.kodraliu.localrock.resources.mapedit_floor_current
import com.kodraliu.localrock.resources.mapedit_floor_current_unknown
import com.kodraliu.localrock.resources.mapedit_floor_default
import com.kodraliu.localrock.resources.mapedit_floor_not_reported
import com.kodraliu.localrock.resources.mapedit_floor_sent
import com.kodraliu.localrock.resources.mapedit_floor_set
import com.kodraliu.localrock.resources.mapedit_floor_tile
import com.kodraliu.localrock.resources.mapedit_floor_type
import com.kodraliu.localrock.resources.mapedit_floor_wood
import com.kodraliu.localrock.resources.mapedit_floor_wood_horizontal
import com.kodraliu.localrock.resources.mapedit_floor_wood_vertical
import com.kodraliu.localrock.resources.mapedit_furniture_armchair
import com.kodraliu.localrock.resources.mapedit_furniture_cat_tree
import com.kodraliu.localrock.resources.mapedit_furniture_closed_litter_box
import com.kodraliu.localrock.resources.mapedit_furniture_coffee_table
import com.kodraliu.localrock.resources.mapedit_furniture_dining_set
import com.kodraliu.localrock.resources.mapedit_furniture_double_bed
import com.kodraliu.localrock.resources.mapedit_furniture_floor_mirror
import com.kodraliu.localrock.resources.mapedit_furniture_litter_box
import com.kodraliu.localrock.resources.mapedit_furniture_modular_sofa
import com.kodraliu.localrock.resources.mapedit_furniture_nightstand
import com.kodraliu.localrock.resources.mapedit_furniture_pet_basket
import com.kodraliu.localrock.resources.mapedit_furniture_pet_bowls
import com.kodraliu.localrock.resources.mapedit_furniture_pet_cage
import com.kodraliu.localrock.resources.mapedit_furniture_shoe_rack
import com.kodraliu.localrock.resources.mapedit_furniture_single_bed
import com.kodraliu.localrock.resources.mapedit_furniture_sofa_multi
import com.kodraliu.localrock.resources.mapedit_furniture_sofa_two
import com.kodraliu.localrock.resources.mapedit_furniture_square_table
import com.kodraliu.localrock.resources.mapedit_furniture_tv_cabinet
import com.kodraliu.localrock.resources.mapedit_furniture_wardrobe
import com.kodraliu.localrock.resources.mapedit_help_box
import com.kodraliu.localrock.resources.mapedit_help_curtain
import com.kodraliu.localrock.resources.mapedit_help_eraser
import com.kodraliu.localrock.resources.mapedit_help_line
import com.kodraliu.localrock.resources.mapedit_help_low_space
import com.kodraliu.localrock.resources.mapedit_help_no_mop
import com.kodraliu.localrock.resources.mapedit_help_round_carpet
import com.kodraliu.localrock.resources.mapedit_keep_editing
import com.kodraliu.localrock.resources.mapedit_kind_erased
import com.kodraliu.localrock.resources.mapedit_kind_no_go
import com.kodraliu.localrock.resources.mapedit_kind_no_mop
import com.kodraliu.localrock.resources.mapedit_kind_round_carpet
import com.kodraliu.localrock.resources.mapedit_map_not_loaded
import com.kodraliu.localrock.resources.mapedit_map_not_loaded_text
import com.kodraliu.localrock.resources.mapedit_merge
import com.kodraliu.localrock.resources.mapedit_merge_done
import com.kodraliu.localrock.resources.mapedit_merge_help
import com.kodraliu.localrock.resources.mapedit_merge_room_title
import com.kodraliu.localrock.resources.mapedit_merge_text
import com.kodraliu.localrock.resources.mapedit_merge_title
import com.kodraliu.localrock.resources.mapedit_merge_with
import com.kodraliu.localrock.resources.mapedit_mode_rooms
import com.kodraliu.localrock.resources.mapedit_mode_zones
import com.kodraliu.localrock.resources.mapedit_no_saved_map
import com.kodraliu.localrock.resources.mapedit_rename
import com.kodraliu.localrock.resources.mapedit_room_number
import com.kodraliu.localrock.resources.mapedit_room_renamed
import com.kodraliu.localrock.resources.mapedit_room_type
import com.kodraliu.localrock.resources.mapedit_room_type_set
import com.kodraliu.localrock.resources.mapedit_room_type_text
import com.kodraliu.localrock.resources.mapedit_rooms_help
import com.kodraliu.localrock.resources.mapedit_save_detected_carpet
import com.kodraliu.localrock.resources.mapedit_save_text
import com.kodraliu.localrock.resources.mapedit_save_title
import com.kodraliu.localrock.resources.mapedit_saved
import com.kodraliu.localrock.resources.mapedit_something_went_wrong
import com.kodraliu.localrock.resources.mapedit_split
import com.kodraliu.localrock.resources.mapedit_split_done
import com.kodraliu.localrock.resources.mapedit_split_help
import com.kodraliu.localrock.resources.mapedit_split_room
import com.kodraliu.localrock.resources.mapedit_split_room_title
import com.kodraliu.localrock.resources.mapedit_split_text
import com.kodraliu.localrock.resources.mapedit_split_title
import com.kodraliu.localrock.resources.mapedit_this_room
import com.kodraliu.localrock.resources.mapedit_title
import com.kodraliu.localrock.resources.mapedit_tool_carpet
import com.kodraliu.localrock.resources.mapedit_tool_curtain
import com.kodraliu.localrock.resources.mapedit_tool_eraser
import com.kodraliu.localrock.resources.mapedit_tool_furniture
import com.kodraliu.localrock.resources.mapedit_tool_no_go
import com.kodraliu.localrock.resources.mapedit_tool_threshold
import com.kodraliu.localrock.resources.mapedit_tool_under_furniture
import com.kodraliu.localrock.resources.mapedit_tool_wall
import com.kodraliu.localrock.resources.mapedit_vertex_limit
import com.kodraliu.localrock.resources.mapedit_zone_points
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource
import com.kodraliu.localrock.shared.text.HasUiText
import com.kodraliu.localrock.shared.text.localizedText
import com.kodraliu.localrock.shared.vacuum.mapIndexBlockerText
import com.kodraliu.localrock.shared.vacuum.restrictionSaveBlockerText
import com.kodraliu.localrock.ui.text.text

private val NO_GO_COLOR = Color(0xfff44336)
private val NO_MOP_COLOR = Color(0xff2196f3)
private val WALL_COLOR = Color(0xffe53935)
private val SPLIT_COLOR = Color(0xffff9800)
// Same brown as the map renderer's carpet stroke; ~4.9:1 on the dark surface, ~3.3:1 on white.
private val CARPET_COLOR = Color(0xffa1887f)
private val THRESHOLD_COLOR = Color(0xffffb300)
private val CURTAIN_COLOR = Color(0xff4dd0e1)
private val LOW_SPACE_COLOR = Color(0xff9575cd)
private val FURNITURE_COLOR = Color(0xffbdbdbd)
private val ERASER_COLOR = Color(0xfff06292)

/**
 * Furniture the official app offers (a296, 2026-11-14): type and subtype as `save_furnitures` takes
 * them, and the size in mm a new piece starts at, as the official app placed it.
 */
private data class FurnitureChoice(val label: StringResource, val type: Int, val subtype: Int, val widthMm: Int, val depthMm: Int)

private val FURNITURE_CHOICES = listOf(
    FurnitureChoice(Res.string.mapedit_furniture_double_bed, 45, 2, 2400, 2400),
    FurnitureChoice(Res.string.mapedit_furniture_single_bed, 45, 1, 1200, 2400),
    FurnitureChoice(Res.string.mapedit_furniture_tv_cabinet, 43, 0, 2500, 800),
    FurnitureChoice(Res.string.mapedit_furniture_sofa_multi, 46, 3, 3500, 1000),
    FurnitureChoice(Res.string.mapedit_furniture_sofa_two, 46, 2, 2000, 1000),
    FurnitureChoice(Res.string.mapedit_furniture_armchair, 46, 1, 1000, 1000),
    FurnitureChoice(Res.string.mapedit_furniture_modular_sofa, 46, 5, 4000, 1500),
    FurnitureChoice(Res.string.mapedit_furniture_dining_set, 47, 0, 2400, 1500),
    FurnitureChoice(Res.string.mapedit_furniture_square_table, 48, 2, 1500, 1500),
    FurnitureChoice(Res.string.mapedit_furniture_coffee_table, 48, 1, 1200, 900),
    FurnitureChoice(Res.string.mapedit_furniture_shoe_rack, 49, 0, 1000, 400),
    FurnitureChoice(Res.string.mapedit_furniture_wardrobe, 51, 0, 2500, 700),
    FurnitureChoice(Res.string.mapedit_furniture_nightstand, 50, 0, 600, 600),
    FurnitureChoice(Res.string.mapedit_furniture_litter_box, 52, 0, 500, 700),
    FurnitureChoice(Res.string.mapedit_furniture_closed_litter_box, 53, 0, 500, 700),
    FurnitureChoice(Res.string.mapedit_furniture_pet_cage, 54, 0, 1500, 800),
    FurnitureChoice(Res.string.mapedit_furniture_pet_basket, 55, 0, 1500, 800),
    FurnitureChoice(Res.string.mapedit_furniture_pet_bowls, 56, 0, 600, 300),
    FurnitureChoice(Res.string.mapedit_furniture_floor_mirror, 57, 0, 800, 500),
    FurnitureChoice(Res.string.mapedit_furniture_cat_tree, 58, 0, 1000, 800),
)

/** A furniture piece's name ("Sofa"), for the bar shown while it is selected. */
private fun furnitureLabel(z: MapZone?): StringResource? =
    FURNITURE_CHOICES.firstOrNull { it.type == z?.furnitureType && it.subtype == z.furnitureSubtype }?.label

/** Shapes smaller than this (either side of a zone, or a line) are treated as accidental drags. */
private const val MIN_SHAPE_MM = 200

private enum class EditMode { RESTRICTIONS, ROOMS }
private enum class Tool { NO_GO, WALL, CARPET, THRESHOLD, CURTAIN, LOW_SPACE, FURNITURE, ERASER }

private sealed interface Selection {
    data class Zone(val index: Int) : Selection
    data class Wall(val index: Int) : Selection
}

private sealed interface RoomAction {
    data object None : RoomAction
    data class Split(val segment: Int) : RoomAction
    data class Merge(val segment: Int) : RoomAction
}

private data class Notice(val text: String, val isError: Boolean)

private data class PendingSplit(val segment: Int, val a: Pair<Int, Int>, val b: Pair<Int, Int>)
private data class PendingMerge(val segmentA: Int, val segmentB: Int)

/** The floor choices offered, as (label, material, wood direction). */
private data class FloorChoice(val label: StringResource, val material: FloorMaterial, val direction: Int?)

private val FLOOR_CHOICES = listOf(
    FloorChoice(Res.string.mapedit_floor_default, FloorMaterial.DEFAULT, null),
    FloorChoice(Res.string.mapedit_floor_wood_horizontal, FloorMaterial.WOOD, 0),
    FloorChoice(Res.string.mapedit_floor_wood_vertical, FloorMaterial.WOOD, 90),
    FloorChoice(Res.string.mapedit_floor_tile, FloorMaterial.TILE, null),
)

private fun zoneColor(kind: ZoneKind) = when (kind) {
    ZoneKind.NO_GO -> NO_GO_COLOR
    ZoneKind.NO_MOP -> NO_MOP_COLOR
    ZoneKind.CARPET -> CARPET_COLOR
    ZoneKind.THRESHOLD -> THRESHOLD_COLOR
    ZoneKind.CURTAIN -> CURTAIN_COLOR
    ZoneKind.LOW_SPACE -> LOW_SPACE_COLOR
    ZoneKind.FURNITURE -> FURNITURE_COLOR
    ZoneKind.ERASER -> ERASER_COLOR
}

/** Kinds drawn as a strip along a line (ends drag), not as a rectangle. */
private fun ZoneKind.isStrip() = this == ZoneKind.THRESHOLD || this == ZoneKind.CURTAIN

/** Zones that count towards save_map's vertex limit (carpets and thresholds have their own commands). */
private fun List<MapZone>.restrictionZones() = filter { it.kind == ZoneKind.NO_GO || it.kind == ZoneKind.NO_MOP }

/**
 * Map editor with two modes.
 *
 * Zones & walls: no-go zones, virtual walls, carpet areas and thresholds are edited locally and
 * saved together. Each kind is written as a complete set (save_map, set_carpet_area,
 * app_set_smart_door_sill), so what the user removes is removed from the robot. Existing shapes
 * keep their original corners, so anything the user does not touch is sent back unchanged.
 *
 * Rooms: rename, floor type, split and merge. Each one is sent immediately and only reported as
 * done once the robot's next map (or room table) shows it.
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun MapEditScreen(
    viewModel: VacuumViewModel,
    onBack: () -> Unit,
) {
    val repository = viewModel.repository
    val parsedMap by repository.parsedMap.collectAsState()
    val status by repository.status.collectAsState()
    val floorMaps by repository.floorMaps.collectAsState()
    val floorMapsLoaded by repository.floorMapsLoaded.collectAsState()
    // The list request can be lost (e.g. sent while MQTT is reconnecting); keep asking until the
    // robot answers, since saving needs it.
    LaunchedEffect(Unit) {
        while (!repository.floorMapsLoaded.value) {
            runCatching { repository.fetchFloorMaps() }
            if (!repository.floorMapsLoaded.value) delay(MAP_LIST_RETRY_MS)
        }
    }
    val scope = rememberCoroutineScope()

    var mode by remember { mutableStateOf(EditMode.RESTRICTIONS) }
    val viewport = remember(parsedMap?.width, parsedMap?.height) { MapViewport() }

    var zones by remember { mutableStateOf<List<MapZone>>(emptyList()) }
    var walls by remember { mutableStateOf<List<VirtualWall>>(emptyList()) }
    var dirty by remember { mutableStateOf(false) }
    var selection by remember { mutableStateOf<Selection?>(null) }

    var selectedRoom by remember { mutableStateOf<Int?>(null) }
    var showRoomSheet by remember { mutableStateOf(false) }
    var roomAction by remember { mutableStateOf<RoomAction>(RoomAction.None) }
    var pendingSplit by remember { mutableStateOf<PendingSplit?>(null) }
    var pendingMerge by remember { mutableStateOf<PendingMerge?>(null) }

    var working by remember { mutableStateOf(false) }
    var notice by remember { mutableStateOf<Notice?>(null) }
    var showSaveConfirm by remember { mutableStateOf(false) }
    var showDiscardConfirm by remember { mutableStateOf(false) }
    var showFurniturePicker by remember { mutableStateOf(false) }

    // Follow the robot's map until the user starts editing; after that local edits own the state
    // so the periodic map poll can't wipe work in progress.
    // The map (floor) the shapes below were read from; a save is refused if the robot has another one loaded.
    var editingFloor by remember { mutableStateOf<Int?>(null) }
    LaunchedEffect(parsedMap, dirty) {
        val pm = parsedMap
        if (!dirty && pm != null) {
            editingFloor = repository.parsedMapFloorFlag
            zones = pm.noGoZones + pm.noMopZones + pm.carpetAreas + pm.thresholds +
                pm.curtains.orEmpty() + pm.furniture.orEmpty() + pm.eraserZones.orEmpty() +
                // Only the zones the user drew; what the robot found itself, and erase marks, stay as they are.
                pm.lowSpaceZones.orEmpty().filter { it.lowSpaceKind == LOW_SPACE_USER }
            walls = pm.virtualWalls
            selection = null
        }
    }

    val pm = parsedMap
    val blockedReason = repository.editBlockedReason(pm, status.state)?.text()
    val mapIndex = saveMapIndex(floorMaps, status.loadedMapFlag)
    // Carpets and thresholds only need the map index; no-go zones and walls also need save_map to
    // be safe on this map.
    // Until the robot's map list arrives nothing is blocked for drawing; only Save waits for it.
    val indexBlocker = when {
        !floorMapsLoaded -> null
        floorMaps.isEmpty() -> stringResource(Res.string.mapedit_no_saved_map)
        else -> mapIndexBlockerText(mapIndex)?.text()
    }
    val saveBlocker = (if (floorMapsLoaded) pm?.let { restrictionSaveBlockerText(it, mapIndex) } else null)?.text()
    val restrictionsChanged = pm != null &&
        !restrictionsMatch(pm, zones.filter { it.kind == ZoneKind.NO_GO }, pm.noMopZones, walls)
    val vertices = restrictionVertexCount(walls.size, zones.restrictionZones().size)

    LaunchedEffect(notice) {
        if (notice?.isError == false) {
            delay(NOTICE_VISIBLE_MS)
            notice = null
        }
    }

    fun runEdit(successText: StringResource, block: suspend () -> String?) {
        scope.launch {
            working = true
            notice = null
            try {
                notice = Notice(block() ?: getString(successText), isError = false)
            } catch (e: Throwable) {
                notice = Notice(e.localizedText() ?: getString(Res.string.mapedit_something_went_wrong), isError = true)
            } finally {
                working = false
            }
        }
    }

    val requestBack = {
        if (dirty) showDiscardConfirm = true else onBack()
    }
    // System back and the back gesture must not silently drop unsaved zone and wall edits.
    BackHandler(enabled = dirty && !showDiscardConfirm) { showDiscardConfirm = true }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Res.string.mapedit_title)) },
                navigationIcon = {
                    IconButton(onClick = requestBack) { Icon(Icons.Default.Close, stringResource(Res.string.mapedit_close_editor)) }
                },
                actions = {
                    if (working) {
                        CircularProgressIndicator(Modifier.padding(end = 16.dp).size(24.dp), strokeWidth = 2.dp)
                    } else if (mode == EditMode.RESTRICTIONS && dirty) {
                        // The one prominent action, on the trailing edge.
                        Button(
                            onClick = { showSaveConfirm = true },
                            enabled = blockedReason == null && floorMapsLoaded && indexBlocker == null &&
                                (saveBlocker == null || !restrictionsChanged),
                            modifier = Modifier.padding(end = 8.dp),
                        ) { Text(stringResource(Res.string.common_save)) }
                    }
                },
            )
        },
    ) { padding ->
        // Edge to edge: the dock runs to the bottom of the screen and pads itself for the
        // navigation bar, so no strip of background shows under it.
        Column(Modifier.fillMaxSize().padding(padding.exceptBottom())) {
            if (pm == null) {
                MapNotLoaded()
                return@Column
            }

            val editable = blockedReason == null && !working
            fun toolEnabled(t: Tool) = editable && when (t) {
                Tool.NO_GO, Tool.WALL -> saveBlocker == null
                Tool.CARPET, Tool.THRESHOLD, Tool.CURTAIN, Tool.LOW_SPACE, Tool.FURNITURE, Tool.ERASER -> indexBlocker == null
            }
            // Only the tools whose block the robot's map carries (curtains 59, under furniture 52, furniture 25).
            val availableTools = Tool.entries.filter { t ->
                when (t) {
                    Tool.CURTAIN -> pm.curtains != null
                    Tool.LOW_SPACE -> pm.lowSpaceZones != null
                    Tool.FURNITURE -> pm.furniture != null
                    Tool.ERASER -> pm.eraserZones != null
                    else -> true
                }
            }
            val deleteEnabled = editable && when (val sel = selection) {
                is Selection.Wall -> saveBlocker == null
                is Selection.Zone -> when (zones.getOrNull(sel.index)?.kind) {
                    ZoneKind.NO_GO -> saveBlocker == null
                    ZoneKind.CARPET, ZoneKind.THRESHOLD, ZoneKind.CURTAIN, ZoneKind.LOW_SPACE, ZoneKind.FURNITURE, ZoneKind.ERASER -> indexBlocker == null
                    ZoneKind.NO_MOP, null -> false
                }
                null -> false
            }

            val vertexLimitText = stringResource(Res.string.mapedit_vertex_limit, MAX_RESTRICTION_VERTICES)
            fun addShape(t: Tool) {
                val (cx, cy) = viewport.centerMm(pm) ?: return
                // Sized to what is on screen so the handles never crowd together when zoomed out.
                val side = viewport.newShapeSizeMm(pm)
                val half = side / 2
                val restrictionLimitHit = when (t) {
                    Tool.NO_GO -> restrictionVertexCount(walls.size, zones.restrictionZones().size + 1) > MAX_RESTRICTION_VERTICES
                    Tool.WALL -> restrictionVertexCount(walls.size + 1, zones.restrictionZones().size) > MAX_RESTRICTION_VERTICES
                    else -> false
                }
                if (restrictionLimitHit) {
                    notice = Notice(vertexLimitText, true)
                    return
                }
                // New shapes start in the middle of what is on screen, already selected, so the
                // next touch adjusts them (the official app works the same way).
                when (t) {
                    Tool.NO_GO -> {
                        zones = zones + MapZone.rect(cx - half, cy - half, cx + half, cy + half, ZoneKind.NO_GO)
                        selection = Selection.Zone(zones.lastIndex)
                    }
                    Tool.CARPET -> {
                        zones = zones + MapZone.rect(cx - half * 8 / 5, cy - half, cx + half * 8 / 5, cy + half, ZoneKind.CARPET)
                        selection = Selection.Zone(zones.lastIndex)
                    }
                    Tool.THRESHOLD -> {
                        // A doorway is roughly 0.9 m whatever the zoom.
                        zones = zones + thresholdStrip(cx - 450 to cy, cx + 450 to cy)
                        selection = Selection.Zone(zones.lastIndex)
                    }
                    Tool.WALL -> {
                        walls = walls + VirtualWall(cx - half * 6 / 5, cy, cx + half * 6 / 5, cy)
                        selection = Selection.Wall(walls.lastIndex)
                    }
                    Tool.CURTAIN -> {
                        // The official app starts a curtain at 2.3 m.
                        zones = zones + curtainStrip(cx - 1150 to cy, cx + 1150 to cy)
                        selection = Selection.Zone(zones.lastIndex)
                    }
                    Tool.LOW_SPACE -> {
                        zones = zones + MapZone.rect(cx - half, cy - half, cx + half, cy + half, ZoneKind.LOW_SPACE)
                            .copy(extra = listOf(LOW_SPACE_USER))
                        selection = Selection.Zone(zones.lastIndex)
                    }
                    Tool.ERASER -> {
                        zones = zones + MapZone.rect(cx - half, cy - half, cx + half, cy + half, ZoneKind.ERASER)
                            .copy(extra = listOf(ERASER_USER))
                        selection = Selection.Zone(zones.lastIndex)
                    }
                    Tool.FURNITURE -> {
                        // Which piece first; it is added once chosen.
                        showFurniturePicker = true
                        return
                    }
                }
                dirty = true
                notice = null
            }

            Box(Modifier.fillMaxWidth().weight(1f)) {
                EditorCanvas(
                    map = pm,
                    mode = mode,
                    zones = zones,
                    walls = walls,
                    selection = selection,
                    selectedRoom = selectedRoom,
                    roomAction = roomAction,
                    viewport = viewport,
                    // Only a room split is drawn by dragging; shapes are added from the dock.
                    drawEnabled = mode == EditMode.ROOMS && editable && roomAction is RoomAction.Split,
                    onLineDrawn = { a, b ->
                        val action = roomAction
                        if (action is RoomAction.Split) pendingSplit = PendingSplit(action.segment, a, b)
                    },
                    onSelectShape = { selection = it },
                    canModifySelection = deleteEnabled,
                    onZoneChanged = { i, z ->
                        if (z.fitsMap()) {
                            zones = zones.mapIndexed { j, old -> if (j == i) z else old }
                            dirty = true
                            notice = null
                        }
                    },
                    onWallChanged = { i, wl ->
                        if (wl.fitsMap()) {
                            walls = walls.mapIndexed { j, old -> if (j == i) wl else old }
                            dirty = true
                            notice = null
                        }
                    },
                    onRoomTap = { segment ->
                        val action = roomAction
                        when {
                            segment == null -> Unit
                            action is RoomAction.Merge && segment != action.segment ->
                                pendingMerge = PendingMerge(action.segment, segment)
                            action is RoomAction.None -> {
                                selectedRoom = segment
                                showRoomSheet = true
                            }
                            else -> Unit
                        }
                    },
                    modifier = Modifier.fillMaxSize(),
                )

                // Floating control layer over the map: mode switch and any warning at the top,
                // results at the bottom.
                Column(
                    Modifier.align(Alignment.TopCenter).fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    ModeSwitch(
                        mode = mode,
                        roomsEnabled = !working && !dirty,
                        enabled = !working,
                        onMode = { m ->
                            mode = m
                            selection = null
                            roomAction = RoomAction.None
                        },
                    )
                    val warning = blockedReason ?: if (mode == EditMode.RESTRICTIONS) (indexBlocker ?: saveBlocker) else null
                    if (warning != null) {
                        WarningCard(warning)
                    } else if (!floorMapsLoaded && mode == EditMode.RESTRICTIONS) {
                        WarningCard(stringResource(Res.string.mapedit_checking_maps), isError = false)
                    }
                }
                notice?.let { n ->
                    NoticePill(
                        notice = n,
                        onDismiss = { notice = null },
                        modifier = Modifier.align(Alignment.BottomCenter).padding(12.dp),
                    )
                }
            }

            EditorDock {
                when (mode) {
                    EditMode.RESTRICTIONS -> {
                        val sel = selection
                        if (sel == null) {
                            ToolDock(tools = availableTools, toolEnabled = ::toolEnabled, onAdd = ::addShape, vertices = vertices)
                        } else {
                            val selectedZone = (sel as? Selection.Zone)?.let { zones.getOrNull(it.index) }
                            val kind = selectedZone?.kind
                            ShapeBar(
                                kind = kind,
                                round = selectedZone?.isRoundCarpet == true,
                                title = furnitureLabel(selectedZone)?.let { stringResource(it) }
                                    ?: stringResource(Res.string.mapedit_detected_carpet).takeIf { selectedZone?.isDetectedCarpet == true },
                                help = stringResource(Res.string.mapedit_detected_carpet_help).takeIf { selectedZone?.isDetectedCarpet == true && pm?.ignoredCarpetZones != null },
                                canEdit = deleteEnabled,
                                onDelete = {
                                    when (sel) {
                                        is Selection.Zone -> zones = zones.filterIndexed { i, _ -> i != sel.index }
                                        is Selection.Wall -> walls = walls.filterIndexed { i, _ -> i != sel.index }
                                    }
                                    selection = null
                                    dirty = true
                                },
                                onDone = { selection = null },
                            )
                        }
                    }
                    EditMode.ROOMS -> RoomDock(
                        action = roomAction,
                        roomName = { id -> pm.rooms.find { it.id == id }?.name },
                        onCancel = { roomAction = RoomAction.None },
                    )
                }
            }
        }
    }

    if (showFurniturePicker) {
        AlertDialog(
            onDismissRequest = { showFurniturePicker = false },
            title = { Text(stringResource(Res.string.mapedit_add_furniture)) },
            text = {
                Column(Modifier.heightIn(max = 420.dp).verticalScroll(rememberScrollState())) {
                    FURNITURE_CHOICES.forEach { choice ->
                        Text(
                            stringResource(choice.label),
                            style = MaterialTheme.typography.bodyLarge,
                            modifier = Modifier.fillMaxWidth().clickable {
                                showFurniturePicker = false
                                val map = parsedMap ?: return@clickable
                                val (cx, cy) = viewport.centerMm(map) ?: return@clickable
                                val hw = choice.widthMm / 2
                                val hd = choice.depthMm / 2
                                zones = zones + MapZone.rect(cx - hw, cy - hd, cx + hw, cy + hd, ZoneKind.FURNITURE)
                                    .copy(extra = listOf(choice.type, choice.subtype))
                                selection = Selection.Zone(zones.lastIndex)
                                dirty = true
                                notice = null
                            }.padding(vertical = 12.dp),
                        )
                    }
                }
            },
            confirmButton = { TextButton(onClick = { showFurniturePicker = false }) { Text(stringResource(Res.string.common_cancel)) } },
        )
    }

    if (showSaveConfirm) {
        val noGo = zones.count { it.kind == ZoneKind.NO_GO }
        val carpets = zones.count { it.kind == ZoneKind.CARPET }
        val thresholds = zones.count { it.kind == ZoneKind.THRESHOLD }
        val deletesDetectedCarpet = pm?.ignoredCarpetZones != null && pm.carpetAreas.any { old ->
            old.isDetectedCarpet && zones.none { it.kind == ZoneKind.CARPET && zoneMatches(it, old) }
        }
        val extras = listOfNotNull(
            zones.count { it.kind == ZoneKind.CURTAIN }.takeIf { pm?.curtains != null }?.let { pluralStringResource(Res.plurals.mapedit_count_curtains, it, it) },
            zones.count { it.kind == ZoneKind.LOW_SPACE }.takeIf { pm?.lowSpaceZones != null }?.let { pluralStringResource(Res.plurals.mapedit_count_low_space, it, it) },
            zones.count { it.kind == ZoneKind.FURNITURE }.takeIf { pm?.furniture != null }?.let { pluralStringResource(Res.plurals.mapedit_count_furniture, it, it) },
            zones.count { it.kind == ZoneKind.ERASER }.takeIf { pm?.eraserZones != null }?.let { pluralStringResource(Res.plurals.mapedit_count_erased, it, it) },
        )
        val kept = (
            listOf(
                pluralStringResource(Res.plurals.mapedit_count_no_go, noGo, noGo),
                pluralStringResource(Res.plurals.mapedit_count_walls, walls.size, walls.size),
                pluralStringResource(Res.plurals.mapedit_count_carpets, carpets, carpets),
                pluralStringResource(Res.plurals.mapedit_count_thresholds, thresholds, thresholds),
            ) + extras
            ).joinToString(", ")
        val saveText = stringResource(Res.string.mapedit_save_text, kept) +
            (if (deletesDetectedCarpet) " " + stringResource(Res.string.mapedit_save_detected_carpet) else "")
        AlertDialog(
            onDismissRequest = { showSaveConfirm = false },
            title = { Text(stringResource(Res.string.mapedit_save_title)) },
            text = {
                Text(saveText)
            },
            confirmButton = {
                TextButton(onClick = {
                    showSaveConfirm = false
                    val noGoZones = zones.filter { it.kind == ZoneKind.NO_GO }
                    val carpetAreas = zones.filter { it.kind == ZoneKind.CARPET }
                    val thresholdStrips = zones.filter { it.kind == ZoneKind.THRESHOLD }
                    val savedWalls = walls
                    val m = parsedMap
                    val curtains = zones.filter { it.kind == ZoneKind.CURTAIN }.takeIf { m?.curtains != null }
                    val lowSpace = zones.filter { it.kind == ZoneKind.LOW_SPACE }.takeIf { m?.lowSpaceZones != null }
                    val furniture = zones.filter { it.kind == ZoneKind.FURNITURE }.takeIf { m?.furniture != null }
                    val eraser = zones.filter { it.kind == ZoneKind.ERASER }.takeIf { m?.eraserZones != null }
                    runEdit(Res.string.mapedit_saved) {
                        repository.saveMapEdits(noGoZones, savedWalls, carpetAreas, thresholdStrips, curtains, lowSpace, furniture, eraser, floorFlag = editingFloor)
                        dirty = false
                        selection = null
                        null
                    }
                }) { Text(stringResource(Res.string.common_save)) }
            },
            dismissButton = { TextButton(onClick = { showSaveConfirm = false }) { Text(stringResource(Res.string.common_cancel)) } },
        )
    }

    if (showDiscardConfirm) {
        AlertDialog(
            onDismissRequest = { showDiscardConfirm = false },
            title = { Text(stringResource(Res.string.mapedit_discard_title)) },
            text = { Text(stringResource(Res.string.mapedit_discard_text)) },
            confirmButton = {
                TextButton(onClick = { showDiscardConfirm = false; onBack() }) { Text(stringResource(Res.string.mapedit_discard)) }
            },
            dismissButton = { TextButton(onClick = { showDiscardConfirm = false }) { Text(stringResource(Res.string.mapedit_keep_editing)) } },
        )
    }

    val room = selectedRoom
    if (showRoomSheet && room != null && pm != null) {
        ModalBottomSheet(onDismissRequest = { showRoomSheet = false }) {
            RoomSheet(
                map = pm,
                segment = room,
                working = working,
                enabled = blockedReason == null,
                onRename = { name ->
                    runEdit(Res.string.mapedit_room_renamed) {
                        viewModel.renameRoom(room, name)
                        null
                    }
                },
                onRoomType = { type ->
                    runEdit(Res.string.mapedit_room_type_set) {
                        viewModel.setRoomType(room, type)
                        null
                    }
                },
                onFloor = { choice ->
                    runEdit(Res.string.mapedit_floor_set) {
                        val confirmed = repository.setFloorMaterial(room, choice.material, choice.direction)
                        if (confirmed) null
                        else getString(Res.string.mapedit_floor_sent)
                    }
                },
                onSplit = {
                    showRoomSheet = false
                    roomAction = RoomAction.Split(room)
                    notice = null
                },
                onMerge = {
                    showRoomSheet = false
                    roomAction = RoomAction.Merge(room)
                    notice = null
                },
            )
        }
    }

    pendingSplit?.let { split ->
        val name = pm?.rooms?.find { it.id == split.segment }?.name ?: stringResource(Res.string.mapedit_this_room)
        AlertDialog(
            onDismissRequest = { pendingSplit = null },
            title = { Text(stringResource(Res.string.mapedit_split_title, name)) },
            text = {
                Text(stringResource(Res.string.mapedit_split_text))
            },
            confirmButton = {
                TextButton(onClick = {
                    pendingSplit = null
                    roomAction = RoomAction.None
                    runEdit(Res.string.mapedit_split_done) {
                        repository.splitRoom(split.segment, split.a.first, split.a.second, split.b.first, split.b.second)
                        null
                    }
                }) { Text(stringResource(Res.string.mapedit_split)) }
            },
            dismissButton = { TextButton(onClick = { pendingSplit = null }) { Text(stringResource(Res.string.common_cancel)) } },
        )
    }

    pendingMerge?.let { merge ->
        val nameA = pm?.rooms?.find { it.id == merge.segmentA }?.name ?: stringResource(Res.string.mapedit_room_number, merge.segmentA)
        val nameB = pm?.rooms?.find { it.id == merge.segmentB }?.name ?: stringResource(Res.string.mapedit_room_number, merge.segmentB)
        AlertDialog(
            onDismissRequest = { pendingMerge = null },
            title = { Text(stringResource(Res.string.mapedit_merge_title)) },
            text = {
                Text(stringResource(Res.string.mapedit_merge_text, nameA, nameB))
            },
            confirmButton = {
                TextButton(onClick = {
                    pendingMerge = null
                    roomAction = RoomAction.None
                    selectedRoom = null
                    runEdit(Res.string.mapedit_merge_done) {
                        repository.mergeRooms(merge.segmentA, merge.segmentB)
                        null
                    }
                }) { Text(stringResource(Res.string.mapedit_merge)) }
            },
            dismissButton = { TextButton(onClick = { pendingMerge = null }) { Text(stringResource(Res.string.common_cancel)) } },
        )
    }
}

@Composable
private fun EditorCanvas(
    map: ParsedMap,
    mode: EditMode,
    zones: List<MapZone>,
    walls: List<VirtualWall>,
    selection: Selection?,
    selectedRoom: Int?,
    roomAction: RoomAction,
    viewport: MapViewport,
    drawEnabled: Boolean,
    onLineDrawn: (Pair<Int, Int>, Pair<Int, Int>) -> Unit,
    onSelectShape: (Selection?) -> Unit,
    onRoomTap: (Int?) -> Unit,
    canModifySelection: Boolean,
    onZoneChanged: (Int, MapZone) -> Unit,
    onWallChanged: (Int, VirtualWall) -> Unit,
    modifier: Modifier = Modifier,
) {
    val bitmap = rememberMapBitmap(map, drawRestrictions = false)
    val textMeasurer = rememberTextMeasurer()
    val labelStyle = TextStyle(fontSize = 11.sp, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onSurface)
    val selectedLabelStyle = labelStyle.copy(color = MaterialTheme.colorScheme.onPrimary)
    val labelBg = MaterialTheme.colorScheme.surface.copy(alpha = 0.88f)
    val selectedBg = MaterialTheme.colorScheme.primary

    var scale by viewport::scale
    var offset by viewport::offset

    // Open framed on the floor plan rather than the mostly empty grid, like the main map does.
    // Done once per map size and view size so later map polls don't undo the user's own zoom,
    // while a rotated phone gets the floor plan fitted again.
    val bounds = remember(map.width, map.height) { map.contentBoundsNorm() }
    // Kept here, not in the viewport: a new map size brings a new viewport but no new layout pass.
    var viewSize by remember { mutableStateOf(Size.Zero) }
    LaunchedEffect(viewport, bounds, viewSize) {
        viewport.viewSize = viewSize
        if (viewport.framedFor == viewSize || viewSize.width == 0f || viewSize.height == 0f) return@LaunchedEffect
        val (fitScale, fitOffset) = frameMap(bounds, map.fittedSize(viewSize), viewSize)
        scale = fitScale
        offset = fitOffset
        viewport.framedFor = viewSize
    }
    var dragStart by remember { mutableStateOf<Offset?>(null) }
    var dragEnd by remember { mutableStateOf<Offset?>(null) }

    // Gesture lambdas outlive recompositions; read the latest values through these.
    val currentZones by rememberUpdatedState(zones)
    val currentWalls by rememberUpdatedState(walls)
    val currentSelection by rememberUpdatedState(selection)
    val currentOnLineDrawn by rememberUpdatedState(onLineDrawn)
    val currentOnSelect by rememberUpdatedState(onSelectShape)
    val currentOnRoomTap by rememberUpdatedState(onRoomTap)
    val currentDrawEnabled by rememberUpdatedState(drawEnabled)
    val currentCanModify by rememberUpdatedState(canModifySelection)
    val currentOnZoneChanged by rememberUpdatedState(onZoneChanged)
    val currentOnWallChanged by rememberUpdatedState(onWallChanged)
    // White dots with a coloured ring read on every room tint in both themes.
    val handleColor = Color.White


    Box(modifier.clipToBounds(), contentAlignment = Alignment.Center) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .onSizeChanged { viewSize = it.toSize() }
                .pointerInput(map, mode) {
                    detectTapGestures { tap ->
                        val fitted = map.fittedSize(size.toSize())
                        val w = fitted.width
                        val h = fitted.height
                        val content = (tap - offset) / scale
                        if (mode == EditMode.ROOMS) {
                            val normX = content.x / w
                            val normY = content.y / h
                            // The view is larger than the map; a tap beside it is not on a room.
                            val onMap = normX in 0f..1f && normY in 0f..1f
                            currentOnRoomTap(if (onMap) map.roomAtNorm(normX, normY)?.id else null)
                            return@detectTapGestures
                        }
                        val (xMm, yMm) = map.canvasToMm(content, w, h)
                        // 24dp of finger slop, converted to mm at the current zoom.
                        val slopMm = 24.dp.toPx() / scale * (map.width / w) * MM_PER_CELL
                        val wallHit = currentWalls.indexOfLast {
                            distanceToSegmentMm(xMm, yMm, it.x0, it.y0, it.x1, it.y1) <= slopMm
                        }
                        val hit: Selection? = if (wallHit >= 0) {
                            Selection.Wall(wallHit)
                        } else {
                            currentZones.indexOfLast { z ->
                                z.contains(xMm, yMm) ||
                                    // Threshold strips are ~2 map pixels wide; accept taps near the centre line.
                                    (z.kind.isStrip() && distanceToSegmentMm(
                                        xMm, yMm,
                                        (z.x0 + z.x3) / 2, (z.y0 + z.y3) / 2,
                                        (z.x1 + z.x2) / 2, (z.y1 + z.y2) / 2,
                                    ) <= slopMm)
                            }.takeIf { it >= 0 }?.let { Selection.Zone(it) }
                        }
                        currentOnSelect(if (hit == currentSelection) null else hit)
                    }
                }
                .pointerInput(map, mode, drawEnabled, viewSize) {
                    val view = size.toSize()
                    val fitted = map.fittedSize(view)
                    val w = fitted.width
                    val h = fitted.height
                    fun clampToMap(p: Offset) = Offset(p.x.coerceIn(0f, w), p.y.coerceIn(0f, h))
                    fun contentOf(p: Offset) = (p - offset) / scale
                    val handleRadius = HANDLE_TOUCH_RADIUS.toPx()
                    val rotateDistance = ROTATE_HANDLE_DISTANCE.toPx()

                    awaitEachGesture {
                        val down = awaitFirstDown(requireUnconsumed = false)
                        val sel = currentSelection
                        // What this touch does is decided where it lands: on the selected shape's
                        // handles or body it edits that shape, otherwise it draws or pans.
                        val target: DragTarget? =
                            if (mode == EditMode.RESTRICTIONS && sel != null && currentCanModify) {
                                hitDragTarget(
                                    sel, currentZones, currentWalls, map, w, h, scale, offset,
                                    down.position, handleRadius, rotateDistance,
                                )
                            } else null
                        var gesture = when {
                            target != null -> Gesture.EDIT
                            currentDrawEnabled -> Gesture.DRAW
                            else -> Gesture.PAN
                        }
                        val startZone = (sel as? Selection.Zone)?.let { currentZones.getOrNull(it.index) }
                        val startWall = (sel as? Selection.Wall)?.let { currentWalls.getOrNull(it.index) }
                        val startMm = map.canvasToMm(clampToMap(contentOf(down.position)), w, h)
                        var pastSlop = false
                        var last = down.position

                        while (true) {
                            val event = awaitPointerEvent()
                            val pressed = event.changes.filter { it.pressed }
                            if (pressed.isEmpty()) {
                                if (gesture == Gesture.DRAW && pastSlop) {
                                    val sd = dragStart
                                    val ed = dragEnd
                                    if (sd != null && ed != null) {
                                        val a = map.canvasToMm(sd, w, h)
                                        val b = map.canvasToMm(ed, w, h)
                                        val dx = abs(a.first - b.first)
                                        val dy = abs(a.second - b.second)
                                        if (hypot(dx.toFloat(), dy.toFloat()) >= MIN_SHAPE_MM) currentOnLineDrawn(a, b)
                                    }
                                }
                                dragStart = null
                                dragEnd = null
                                break
                            }
                            if (pressed.size >= 2 || gesture == Gesture.ZOOM) {
                                // A second finger always means zoom/pan; an unfinished drawing is dropped.
                                gesture = Gesture.ZOOM
                                dragStart = null
                                dragEnd = null
                                val zoom = event.calculateZoom()
                                val pan = event.calculatePan()
                                val centroid = event.calculateCentroid(useCurrent = true)
                                val (newScale, newOffset) = zoomMap(scale, offset, centroid, pan, zoom, fitted, view)
                                scale = newScale
                                offset = newOffset
                                event.changes.forEach { it.consume() }
                                continue
                            }
                            val change = pressed.first()
                            val pos = change.position
                            if (!pastSlop) {
                                if ((pos - down.position).getDistance() < viewConfiguration.touchSlop) continue
                                pastSlop = true
                                if (gesture == Gesture.DRAW) dragStart = clampToMap(contentOf(down.position))
                            }
                            change.consume()
                            when (gesture) {
                                Gesture.PAN -> offset = clampMapOffset(offset + (pos - last), scale, fitted, view)
                                Gesture.DRAW -> dragEnd = clampToMap(contentOf(pos))
                                Gesture.EDIT -> {
                                    val nowMm = map.canvasToMm(clampToMap(contentOf(pos)), w, h)
                                    applyEdit(
                                        target!!, sel!!, startZone, startWall, startMm, nowMm,
                                        onZone = { i, z -> currentOnZoneChanged(i, z) },
                                        onWall = { i, wl -> currentOnWallChanged(i, wl) },
                                    )
                                }
                                Gesture.ZOOM -> Unit
                            }
                            last = pos
                        }
                    }
                },
        ) {
            val fitted = map.fittedSize(size)
            val w = fitted.width
            val h = fitted.height
            withTransform({
                translate(left = offset.x, top = offset.y)
                scale(scale, scale, pivot = Offset.Zero)
            }) {
                drawImage(
                    image = bitmap,
                    srcOffset = IntOffset.Zero,
                    srcSize = IntSize(bitmap.width, bitmap.height),
                    dstOffset = IntOffset.Zero,
                    dstSize = IntSize(w.toInt(), h.toInt()),
                    filterQuality = FilterQuality.Low,
                )
                val stroke = (2.dp.toPx() / scale).coerceAtLeast(1f)
                val dimmed = mode == EditMode.ROOMS

                zones.forEachIndexed { i, zone ->
                    val path = Path().apply {
                        if (zone.isRoundCarpet) {
                            val a = map.mmToCanvas(zone.minXmm, zone.minYmm, w, h)
                            val b = map.mmToCanvas(zone.maxXmm, zone.maxYmm, w, h)
                            addOval(Rect(minOf(a.x, b.x), minOf(a.y, b.y), maxOf(a.x, b.x), maxOf(a.y, b.y)))
                        } else {
                            val c0 = map.mmToCanvas(zone.x0, zone.y0, w, h); moveTo(c0.x, c0.y)
                            val c1 = map.mmToCanvas(zone.x1, zone.y1, w, h); lineTo(c1.x, c1.y)
                            val c2 = map.mmToCanvas(zone.x2, zone.y2, w, h); lineTo(c2.x, c2.y)
                            val c3 = map.mmToCanvas(zone.x3, zone.y3, w, h); lineTo(c3.x, c3.y)
                            close()
                        }
                    }
                    val c = zoneColor(zone.kind)
                    val selected = selection == Selection.Zone(i)
                    drawPath(path, c.copy(alpha = if (dimmed) 0.12f else 0.25f))
                    drawPath(path, c.copy(alpha = if (dimmed) 0.4f else 0.9f), style = Stroke(if (selected) stroke * 2.5f else stroke))
                }
                walls.forEachIndexed { i, wall ->
                    val selected = selection == Selection.Wall(i)
                    drawLine(
                        color = WALL_COLOR.copy(alpha = if (dimmed) 0.4f else 1f),
                        start = map.mmToCanvas(wall.x0, wall.y0, w, h),
                        end = map.mmToCanvas(wall.x1, wall.y1, w, h),
                        strokeWidth = if (selected) stroke * 3f else stroke * 1.5f,
                        cap = StrokeCap.Round,
                    )
                }

                val s = dragStart
                val e = dragEnd
                if (s != null && e != null) {
                    val dash = PathEffect.dashPathEffect(floatArrayOf(12f / scale, 6f / scale))
                    drawLine(
                        color = SPLIT_COLOR,
                        start = s, end = e, strokeWidth = stroke * 1.5f, cap = StrokeCap.Round, pathEffect = dash,
                    )
                }
            }

            val sel = selection
            if (mode == EditMode.RESTRICTIONS && sel != null && canModifySelection) {
                val handles = dragHandles(
                    sel, zones, walls, map, w, h, scale, offset, ROTATE_HANDLE_DISTANCE.toPx(),
                )
                val ring = when (sel) {
                    is Selection.Wall -> WALL_COLOR
                    is Selection.Zone -> zones.getOrNull(sel.index)?.let { zoneColor(it.kind) } ?: WALL_COLOR
                }
                handles.forEach { (target, at) ->
                    val r = HANDLE_DRAW_RADIUS.toPx()
                    if (target is DragTarget.ZoneRotate) {
                        drawCircle(ring, radius = r, center = at)
                        drawCircle(handleColor, radius = r * 0.45f, center = at)
                    } else {
                        drawCircle(handleColor, radius = r, center = at)
                        drawCircle(ring, radius = r, center = at, style = Stroke(2.dp.toPx()))
                    }
                }
            }

            if (mode == EditMode.ROOMS) {
                val focus = when (roomAction) {
                    is RoomAction.Split -> roomAction.segment
                    is RoomAction.Merge -> roomAction.segment
                    RoomAction.None -> selectedRoom
                }
                map.rooms.forEach { room ->
                    val nx = room.labelNormX ?: return@forEach
                    val ny = room.labelNormY ?: return@forEach
                    val center = Offset(nx * w * scale + offset.x, ny * h * scale + offset.y)
                    val highlighted = room.id == focus
                    val layout = textMeasurer.measure(room.name, if (highlighted) selectedLabelStyle else labelStyle)
                    val pad = 6.dp.toPx()
                    val boxW = layout.size.width + pad * 2
                    val boxH = layout.size.height + pad
                    val topLeft = Offset(center.x - boxW / 2, center.y - boxH / 2)
                    drawRoundRect(
                        color = if (highlighted) selectedBg else labelBg,
                        topLeft = topLeft,
                        size = Size(boxW, boxH),
                        cornerRadius = CornerRadius(boxH / 2),
                    )
                    drawText(layout, topLeft = Offset(topLeft.x + pad, topLeft.y + pad / 2))
                }
            }
        }
    }
}

/**
 * Zoom and pan of the editor map, held by the screen so it can place new shapes in the middle of
 * what is visible.
 */
@Stable
private class MapViewport {
    var scale by mutableStateOf(1f)
    var offset by mutableStateOf(Offset.Zero)
    /** Size of the whole map view on screen. */
    var viewSize by mutableStateOf(Size.Zero)
    /** The view size the map was last framed for; framed again when it changes. */
    var framedFor by mutableStateOf<Size?>(null)

    /** Robot mm under the centre of the view, kept away from the map's edges. */
    fun centerMm(map: ParsedMap): Pair<Int, Int>? {
        val fitted = map.fittedSize(viewSize)
        if (fitted.width <= 0f || fitted.height <= 0f) return null
        val content = (viewSize.center - offset) / scale
        val (x, y) = map.canvasToMm(content, fitted.width, fitted.height)
        return x.coerceIn(NEW_SHAPE_MARGIN_MM, MAX_MAP_COORD_MM - NEW_SHAPE_MARGIN_MM) to
            y.coerceIn(NEW_SHAPE_MARGIN_MM, MAX_MAP_COORD_MM - NEW_SHAPE_MARGIN_MM)
    }

    /** Side of a new shape: about a fifth of the visible map width, 0.6-3 m. */
    fun newShapeSizeMm(map: ParsedMap): Int {
        val visibleWidthMm = map.width * MM_PER_CELL / scale
        return (visibleWidthMm * NEW_SHAPE_FRACTION).toInt().coerceIn(600, 3_000)
    }
}

private const val NEW_SHAPE_MARGIN_MM = 1_000
private const val NEW_SHAPE_FRACTION = 0.18f
private const val NOTICE_VISIBLE_MS = 4_000L
private val DOCK_MIN_HEIGHT = 136.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ModeSwitch(mode: EditMode, enabled: Boolean, roomsEnabled: Boolean, onMode: (EditMode) -> Unit) {
    Surface(
        shape = RoundedCornerShape(50),
        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.94f),
        tonalElevation = 3.dp,
        shadowElevation = 2.dp,
    ) {
        SingleChoiceSegmentedButtonRow(Modifier.width(280.dp).padding(4.dp)) {
            SegmentedButton(
                selected = mode == EditMode.RESTRICTIONS,
                onClick = { onMode(EditMode.RESTRICTIONS) },
                shape = SegmentedButtonDefaults.itemShape(0, 2),
                enabled = enabled,
            ) { Text(stringResource(Res.string.mapedit_mode_zones)) }
            SegmentedButton(
                selected = mode == EditMode.ROOMS,
                onClick = { onMode(EditMode.ROOMS) },
                shape = SegmentedButtonDefaults.itemShape(1, 2),
                // Room edits reshape the map under unsaved zone edits, so save or discard first.
                enabled = roomsEnabled,
            ) { Text(stringResource(Res.string.mapedit_mode_rooms)) }
        }
    }
}

/** The docked panel under the map that holds the current mode's controls. */
@Composable
private fun EditorDock(content: @Composable () -> Unit) {
    Surface(
        shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
        tonalElevation = 3.dp,
        shadowElevation = 8.dp,
        modifier = Modifier.fillMaxWidth(),
    ) {
        // A fixed minimum height keeps the map from jumping when the dock switches between the
        // tools and a selected shape's bar.
        Box(
            Modifier
                .windowInsetsPadding(WindowInsets.navigationBars.only(WindowInsetsSides.Bottom))
                .heightIn(min = DOCK_MIN_HEIGHT)
                .padding(horizontal = 16.dp, vertical = 14.dp),
            contentAlignment = Alignment.CenterStart,
        ) { content() }
    }
}

private data class ToolSpec(val tool: Tool, val label: StringResource, val icon: ImageVector, val color: Color)

private val TOOL_SPECS = listOf(
    ToolSpec(Tool.NO_GO, Res.string.mapedit_tool_no_go, Icons.Default.Block, NO_GO_COLOR),
    ToolSpec(Tool.WALL, Res.string.mapedit_tool_wall, Icons.Default.HorizontalRule, WALL_COLOR),
    ToolSpec(Tool.CARPET, Res.string.mapedit_tool_carpet, Icons.Default.Texture, CARPET_COLOR),
    ToolSpec(Tool.THRESHOLD, Res.string.mapedit_tool_threshold, Icons.Default.MeetingRoom, THRESHOLD_COLOR),
    ToolSpec(Tool.CURTAIN, Res.string.mapedit_tool_curtain, Icons.Default.Texture, CURTAIN_COLOR),
    ToolSpec(Tool.LOW_SPACE, Res.string.mapedit_tool_under_furniture, Icons.Default.Block, LOW_SPACE_COLOR),
    ToolSpec(Tool.FURNITURE, Res.string.mapedit_tool_furniture, Icons.Default.MeetingRoom, FURNITURE_COLOR),
    ToolSpec(Tool.ERASER, Res.string.mapedit_tool_eraser, Icons.Default.Block, ERASER_COLOR),
)

/**
 * Add buttons, one per shape kind. Each tile's colour is the colour that kind has on the map, so
 * the dock doubles as the map's key.
 */
@Composable
private fun ToolDock(tools: List<Tool>, toolEnabled: (Tool) -> Boolean, onAdd: (Tool) -> Unit, vertices: Int) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(stringResource(Res.string.mapedit_add_to_map), style = MaterialTheme.typography.titleSmall, modifier = Modifier.weight(1f))
            Text(
                stringResource(Res.string.mapedit_zone_points, vertices, MAX_RESTRICTION_VERTICES),
                style = MaterialTheme.typography.labelMedium,
                color = if (vertices >= MAX_RESTRICTION_VERTICES) MaterialTheme.colorScheme.error
                else MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        val specs = TOOL_SPECS.filter { it.tool in tools }
        // Four tools fit across; more scroll sideways.
        Row(
            Modifier.fillMaxWidth().then(if (specs.size > 4) Modifier.horizontalScroll(rememberScrollState()) else Modifier),
            horizontalArrangement = if (specs.size > 4) Arrangement.spacedBy(4.dp) else Arrangement.SpaceBetween,
        ) {
            specs.forEach { spec ->
                ToolTile(spec, enabled = toolEnabled(spec.tool), onClick = { onAdd(spec.tool) })
            }
        }
    }
}

@Composable
private fun ToolTile(spec: ToolSpec, enabled: Boolean, onClick: () -> Unit) {
    val alpha = if (enabled) 1f else 0.38f
    val label = stringResource(spec.label)
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable(enabled = enabled, role = Role.Button, onClickLabel = stringResource(Res.string.mapedit_add_tool, label.lowercase()), onClick = onClick)
            .widthIn(min = 72.dp)
            .padding(horizontal = 6.dp, vertical = 4.dp),
    ) {
        Box(
            Modifier.size(52.dp).clip(CircleShape).background(spec.color.copy(alpha = 0.18f * alpha)),
            contentAlignment = Alignment.Center,
        ) {
            Icon(spec.icon, contentDescription = null, tint = spec.color.copy(alpha = alpha), modifier = Modifier.size(26.dp))
        }
        Text(
            label,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = alpha),
        )
    }
}

@Composable
private fun kindTitle(kind: ZoneKind?, round: Boolean = false) = stringResource(
    when (kind) {
        null -> Res.string.mapedit_tool_wall
        ZoneKind.NO_GO -> Res.string.mapedit_kind_no_go
        ZoneKind.NO_MOP -> Res.string.mapedit_kind_no_mop
        ZoneKind.CARPET -> if (round) Res.string.mapedit_kind_round_carpet else Res.string.mapedit_tool_carpet
        ZoneKind.THRESHOLD -> Res.string.mapedit_tool_threshold
        ZoneKind.CURTAIN -> Res.string.mapedit_tool_curtain
        ZoneKind.LOW_SPACE -> Res.string.mapedit_tool_under_furniture
        ZoneKind.FURNITURE -> Res.string.mapedit_tool_furniture
        ZoneKind.ERASER -> Res.string.mapedit_kind_erased
    },
)

// A carpet the robot found can only be deleted, as in the official app; deleting it ignores carpet
// there (help text: mapedit_detected_carpet_help).

@Composable
private fun kindHelp(kind: ZoneKind?, round: Boolean = false) = when {
    round -> stringResource(Res.string.mapedit_help_round_carpet)
    else -> kindHelpFor(kind)
}

@Composable
private fun kindHelpFor(kind: ZoneKind?) = stringResource(
    when (kind) {
        null, ZoneKind.THRESHOLD -> Res.string.mapedit_help_line
        ZoneKind.CURTAIN -> Res.string.mapedit_help_curtain
        ZoneKind.LOW_SPACE -> Res.string.mapedit_help_low_space
        ZoneKind.FURNITURE -> Res.string.mapedit_help_box
        ZoneKind.ERASER -> Res.string.mapedit_help_eraser
        ZoneKind.NO_GO, ZoneKind.CARPET -> Res.string.mapedit_help_box
        ZoneKind.NO_MOP -> Res.string.mapedit_help_no_mop
    },
)

/** Replaces the tool dock while a shape is selected: what it is, and what can be done to it. */
@Composable
private fun ShapeBar(kind: ZoneKind?, round: Boolean, title: String? = null, help: String? = null, canEdit: Boolean, onDelete: () -> Unit, onDone: () -> Unit) {
    val color = kind?.let { zoneColor(it) } ?: WALL_COLOR
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Box(Modifier.size(14.dp).clip(CircleShape).background(color))
        Column(Modifier.weight(1f)) {
            Text(title ?: kindTitle(kind, round), style = MaterialTheme.typography.titleSmall)
            Text(
                if (canEdit || kind == ZoneKind.NO_MOP) help ?: kindHelp(kind, round) else stringResource(Res.string.mapedit_cant_change),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        if (canEdit) {
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = stringResource(Res.string.mapedit_delete_kind, kindTitle(kind).lowercase()), tint = MaterialTheme.colorScheme.error)
            }
        }
        FilledTonalButton(onClick = onDone) {
            Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(6.dp))
            Text(stringResource(Res.string.mapedit_done))
        }
    }
}

@Composable
private fun RoomDock(action: RoomAction, roomName: (Int) -> String?, onCancel: () -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        Column(Modifier.weight(1f)) {
            val (title, help) = when (action) {
                RoomAction.None ->
                    stringResource(Res.string.mapedit_mode_rooms) to stringResource(Res.string.mapedit_rooms_help)
                is RoomAction.Split ->
                    stringResource(Res.string.mapedit_split_room_title, roomName(action.segment) ?: stringResource(Res.string.mapedit_room_number, action.segment)) to
                        stringResource(Res.string.mapedit_split_help)
                is RoomAction.Merge ->
                    stringResource(Res.string.mapedit_merge_room_title, roomName(action.segment) ?: stringResource(Res.string.mapedit_room_number, action.segment)) to
                        stringResource(Res.string.mapedit_merge_help)
            }
            Text(title, style = MaterialTheme.typography.titleSmall)
            Text(help, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        if (action != RoomAction.None) {
            OutlinedButton(onClick = onCancel) { Text(stringResource(Res.string.common_cancel)) }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun RoomSheet(
    map: ParsedMap,
    segment: Int,
    working: Boolean,
    enabled: Boolean,
    onRoomType: (RoomType) -> Unit,
    onRename: (String) -> Unit,
    onFloor: (FloorChoice) -> Unit,
    onSplit: () -> Unit,
    onMerge: () -> Unit,
) {
    val room = map.rooms.find { it.id == segment }
    val currentName = room?.name ?: stringResource(Res.string.mapedit_room_number, segment)
    var nameInput by remember(segment, currentName) { mutableStateOf(currentName) }
    val nameError = runCatching { validateRoomName(nameInput) }.exceptionOrNull()
        ?.let { e -> (e as? HasUiText)?.text?.text() ?: e.message }
    val canAct = enabled && !working

    Column(
        Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .navigationBarsPadding()
            .imePadding()
            .padding(horizontal = 16.dp)
            .padding(bottom = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(currentName, style = MaterialTheme.typography.titleLarge, modifier = Modifier.weight(1f))
            if (working) CircularProgressIndicator(Modifier.size(20.dp), strokeWidth = 2.dp)
        }

        Text(stringResource(Res.string.mapedit_room_type), style = MaterialTheme.typography.titleSmall)
        Text(
            stringResource(Res.string.mapedit_room_type_text),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            RoomType.entries.forEach { type ->
                val label = type.text.text()
                FilterChip(
                    // A room typed before the app spoke this language keeps the English name.
                    selected = label.equals(currentName, ignoreCase = true) || type.label.equals(currentName, ignoreCase = true),
                    onClick = { onRoomType(type) },
                    label = { Text(label) },
                    enabled = canAct,
                )
            }
        }

        Text(stringResource(Res.string.mapedit_custom_name), style = MaterialTheme.typography.titleSmall)
        Text(
            stringResource(Res.string.mapedit_custom_name_text),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedTextField(
                value = nameInput,
                onValueChange = { nameInput = it },
                singleLine = true,
                isError = nameError != null,
                supportingText = nameError?.let { { Text(it) } },
                modifier = Modifier.weight(1f),
                enabled = canAct,
            )
            Button(
                onClick = { onRename(nameInput) },
                enabled = canAct && nameError == null && nameInput.trim() != currentName,
            ) { Text(stringResource(Res.string.mapedit_rename)) }
        }

        HorizontalDivider()

        Text(stringResource(Res.string.mapedit_floor_type), style = MaterialTheme.typography.titleSmall)
        val materialId = map.segmentMaterials[segment]
        val direction = map.segmentMaterialDirections[segment]
        val current = FLOOR_CHOICES.firstOrNull { choice ->
            materialId == choice.material.id &&
                (choice.direction == null || direction == choice.direction)
        }
        Text(
            when {
                materialId == null -> stringResource(Res.string.mapedit_floor_not_reported)
                current != null -> stringResource(Res.string.mapedit_floor_current, stringResource(current.label))
                FloorMaterial.fromId(materialId) == FloorMaterial.WOOD ->
                    stringResource(Res.string.mapedit_floor_current, stringResource(Res.string.mapedit_floor_wood))
                FloorMaterial.fromId(materialId) == FloorMaterial.CARPET ->
                    stringResource(Res.string.mapedit_floor_current, stringResource(Res.string.mapedit_tool_carpet))
                else -> stringResource(Res.string.mapedit_floor_current_unknown, materialId)
            },
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FLOOR_CHOICES.take(2).forEach { choice ->
                FilterChip(
                    selected = choice == current,
                    onClick = { if (choice != current) onFloor(choice) },
                    label = { Text(stringResource(choice.label)) },
                    enabled = canAct,
                )
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FLOOR_CHOICES.drop(2).forEach { choice ->
                FilterChip(
                    selected = choice == current,
                    onClick = { if (choice != current) onFloor(choice) },
                    label = { Text(stringResource(choice.label)) },
                    enabled = canAct,
                )
            }
        }

        HorizontalDivider()

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            OutlinedButton(onClick = onSplit, enabled = canAct, modifier = Modifier.weight(1f)) { Text(stringResource(Res.string.mapedit_split_room)) }
            OutlinedButton(onClick = onMerge, enabled = canAct && map.rooms.size > 1, modifier = Modifier.weight(1f)) {
                Text(stringResource(Res.string.mapedit_merge_with))
            }
        }
    }
}

@Composable
private fun WarningCard(text: String, isError: Boolean = true) {
    Card(
        colors = if (isError) {
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer,
            )
        } else {
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        },
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp),
    ) {
        Text(text, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(12.dp))
    }
}

/** Result of the last save or room edit, floating over the bottom of the map. */
@Composable
private fun NoticePill(notice: Notice, onDismiss: () -> Unit, modifier: Modifier = Modifier) {
    val scheme = MaterialTheme.colorScheme
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = if (notice.isError) scheme.errorContainer else scheme.inverseSurface,
        contentColor = if (notice.isError) scheme.onErrorContainer else scheme.inverseOnSurface,
        shadowElevation = 4.dp,
        modifier = modifier.widthIn(max = 520.dp),
    ) {
        Row(
            Modifier.padding(start = 16.dp, top = 4.dp, bottom = 4.dp, end = 4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(notice.text, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f, fill = false))
            IconButton(onClick = onDismiss) { Icon(Icons.Default.Close, contentDescription = stringResource(Res.string.app_dismiss)) }
        }
    }
}

@Composable
private fun MapNotLoaded() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Default.Map, null, Modifier.size(48.dp), MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(Modifier.height(12.dp))
            Text(stringResource(Res.string.mapedit_map_not_loaded), style = MaterialTheme.typography.titleMedium)
            Text(
                stringResource(Res.string.mapedit_map_not_loaded_text),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp),
            )
        }
    }
}

private enum class Gesture { DRAW, PAN, EDIT, ZOOM }

/** What a drag on the selected shape changes. */
private sealed interface DragTarget {
    data object Body : DragTarget
    data class ZoneCorner(val index: Int) : DragTarget
    data object ZoneRotate : DragTarget
    /** End 0 or 1 of a wall, or of a threshold's centre line. */
    data class LineEnd(val end: Int) : DragTarget
}

internal const val MAP_LIST_RETRY_MS = 4_000L

private val HANDLE_DRAW_RADIUS = 9.dp
private val HANDLE_TOUCH_RADIUS = 28.dp
private val ROTATE_HANDLE_DISTANCE = 36.dp

/**
 * Round carpets only move: the map stores them as a bounding square plus a flag, and what the robot
 * makes of a stretched or rotated one has not been captured.
 */
private fun canRotateOrResize(zone: MapZone) = !zone.isDetectedCarpet && (
    zone.kind == ZoneKind.NO_GO || zone.kind == ZoneKind.LOW_SPACE || zone.kind == ZoneKind.FURNITURE || zone.kind == ZoneKind.ERASER ||
        (zone.kind == ZoneKind.CARPET && !zone.isRoundCarpet))

/** Screen positions of the selected shape's handles. */
private fun dragHandles(
    sel: Selection,
    zones: List<MapZone>,
    walls: List<VirtualWall>,
    map: ParsedMap,
    w: Float,
    h: Float,
    scale: Float,
    offset: Offset,
    rotateDistancePx: Float,
): List<Pair<DragTarget, Offset>> {
    fun screen(x: Int, y: Int) = map.mmToCanvas(x, y, w, h) * scale + offset
    return when (sel) {
        is Selection.Wall -> walls.getOrNull(sel.index)?.let { wl ->
            listOf(DragTarget.LineEnd(0) to screen(wl.x0, wl.y0), DragTarget.LineEnd(1) to screen(wl.x1, wl.y1))
        } ?: emptyList()
        is Selection.Zone -> {
            val z = zones.getOrNull(sel.index) ?: return emptyList()
            when {
                z.kind.isStrip() -> {
                    val (a, b) = z.thresholdCenterLine()
                    listOf(DragTarget.LineEnd(0) to screen(a.first, a.second), DragTarget.LineEnd(1) to screen(b.first, b.second))
                }
                canRotateOrResize(z) -> {
                    val corners = z.corners().map { (x, y) -> screen(x, y) }
                    val (cxMm, cyMm) = z.center()
                    val c = screen(cxMm.toInt(), cyMm.toInt())
                    val mid = (corners[0] + corners[1]) / 2f
                    val dir = (mid - c).let { d -> if (d.getDistance() < 1f) Offset(0f, -1f) else d / d.getDistance() }
                    corners.mapIndexed { i, at -> DragTarget.ZoneCorner(i) as DragTarget to at } +
                        (DragTarget.ZoneRotate to mid + dir * rotateDistancePx)
                }
                else -> emptyList()
            }
        }
    }
}

/** Handle under [touch], else the selected shape's body, else null (the touch draws or pans). */
private fun hitDragTarget(
    sel: Selection,
    zones: List<MapZone>,
    walls: List<VirtualWall>,
    map: ParsedMap,
    w: Float,
    h: Float,
    scale: Float,
    offset: Offset,
    touch: Offset,
    handleRadiusPx: Float,
    rotateDistancePx: Float,
): DragTarget? {
    dragHandles(sel, zones, walls, map, w, h, scale, offset, rotateDistancePx)
        .minByOrNull { (_, at) -> (at - touch).getDistance() }
        ?.takeIf { (_, at) -> (at - touch).getDistance() <= handleRadiusPx }
        ?.let { return it.first }
    val content = (touch - offset) / scale
    val (xMm, yMm) = map.canvasToMm(content, w, h)
    val slopMm = handleRadiusPx / scale * (map.width / w) * MM_PER_CELL
    val onBody = when (sel) {
        is Selection.Wall -> walls.getOrNull(sel.index)?.let {
            distanceToSegmentMm(xMm, yMm, it.x0, it.y0, it.x1, it.y1) <= slopMm
        } ?: false
        is Selection.Zone -> zones.getOrNull(sel.index)?.let { z ->
            if (z.kind.isStrip()) {
                val (a, b) = z.thresholdCenterLine()
                distanceToSegmentMm(xMm, yMm, a.first, a.second, b.first, b.second) <= slopMm
            } else z.contains(xMm, yMm)
        } ?: false
    }
    return if (onBody) DragTarget.Body else null
}

/**
 * Apply a drag from [startMm] to [nowMm] to the shape as it was when the drag began, so rounding
 * never accumulates. Out-of-map results are dropped by the callers.
 */
private fun applyEdit(
    target: DragTarget,
    sel: Selection,
    startZone: MapZone?,
    startWall: VirtualWall?,
    startMm: Pair<Int, Int>,
    nowMm: Pair<Int, Int>,
    onZone: (Int, MapZone) -> Unit,
    onWall: (Int, VirtualWall) -> Unit,
) {
    val dx = nowMm.first - startMm.first
    val dy = nowMm.second - startMm.second
    when (sel) {
        is Selection.Wall -> {
            val wl = startWall ?: return
            val moved = when (target) {
                DragTarget.Body -> wl.translated(dx, dy)
                is DragTarget.LineEnd -> {
                    val (ex, ey) = if (target.end == 0) wl.x0 to wl.y0 else wl.x1 to wl.y1
                    val next = wl.withEnd(target.end, ex + dx, ey + dy)
                    if (hypot((next.x1 - next.x0).toFloat(), (next.y1 - next.y0).toFloat()) < MIN_SHAPE_MM) return
                    next
                }
                else -> return
            }
            onWall(sel.index, moved)
        }
        is Selection.Zone -> {
            val z = startZone ?: return
            val moved = when (target) {
                // A detected carpet stays where the robot found it: moving it would only be undone.
                DragTarget.Body -> if (z.isDetectedCarpet) return else z.translated(dx, dy)
                is DragTarget.ZoneCorner -> {
                    val (cx, cy) = z.corners()[target.index]
                    z.resizedFromCorner(target.index, cx + dx, cy + dy, MIN_SHAPE_MM)
                }
                DragTarget.ZoneRotate -> {
                    val (cx, cy) = z.center()
                    val before = kotlin.math.atan2(startMm.second - cy, startMm.first - cx)
                    val after = kotlin.math.atan2(nowMm.second - cy, nowMm.first - cx)
                    z.rotated(after - before)
                }
                is DragTarget.LineEnd -> {
                    if (!z.kind.isStrip()) return
                    val (a, b) = z.thresholdCenterLine()
                    val na = if (target.end == 0) (a.first + dx) to (a.second + dy) else a
                    val nb = if (target.end == 1) (b.first + dx) to (b.second + dy) else b
                    if (hypot((nb.first - na.first).toFloat(), (nb.second - na.second).toFloat()) < MIN_SHAPE_MM) return
                    if (z.kind == ZoneKind.CURTAIN) curtainStrip(na, nb).copy(extra = z.extra) else thresholdStrip(na, nb)
                }
            }
            onZone(sel.index, moved)
        }
    }
}

