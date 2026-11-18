package com.kodraliu.localrock.ui.vacuum

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kodraliu.localrock.shared.vacuum.MopRoute
import com.kodraliu.localrock.shared.vacuum.RoutineStep
import com.kodraliu.localrock.shared.vacuum.RoutineStepKind
import com.kodraliu.localrock.shared.vacuum.VacuumFanPower
import com.kodraliu.localrock.shared.vacuum.WaterBoxMode
import com.kodraliu.localrock.shared.vacuum.stepTitle

/**
 * Make or change a routine, as the official app's routine editor: a name and steps that run one after
 * another, each a full clean, some rooms or some zones with its own suction, water and mop route.
 * Saving only writes to the server; nothing is sent to the robot until the routine is run.
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun RoutineEditorScreen(
    viewModel: VacuumViewModel,
    onBack: () -> Unit,
    onPickZones: (stepIndex: Int) -> Unit,
) {
    val edit by viewModel.routineEdit.collectAsState()
    val saving by viewModel.routineSaving.collectAsState()
    val rooms by viewModel.repository.rooms.collectAsState()
    val waterSlide by viewModel.supportsWaterSlide.collectAsState()
    val current = edit
    val roomName: (Int) -> String? = { id -> rooms.find { it.id == id }?.name?.takeIf { n -> n.isNotBlank() } }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (current?.sceneId == null) "New routine" else "Edit routine") },
                navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") } },
                actions = {
                    if (saving) {
                        CircularProgressIndicator(Modifier.size(24.dp).padding(end = 8.dp), strokeWidth = 2.dp)
                    } else if (current != null) {
                        TextButton(onClick = { viewModel.saveRoutine(onSaved = onBack) }) { Text("Save") }
                    }
                },
            )
        },
    ) { padding ->
        if (current == null) {
            Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) { Text("No routine open") }
            return@Scaffold
        }
        val draft = current.draft
        Column(
            Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp).navigationBarsPadding().padding(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            OutlinedTextField(
                value = draft.name,
                onValueChange = { name -> viewModel.updateRoutine { it.copy(name = name.take(40)) } },
                label = { Text("Name") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth(),
            )
            Text(
                "The steps run one after another; each starts once the robot has finished the one before.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            draft.steps.forEachIndexed { index, step ->
                RoutineStepCard(
                    index = index,
                    count = draft.steps.size,
                    step = step,
                    rooms = rooms.map { it.id to (it.name.ifBlank { "Room ${it.id}" }) },
                    roomName = roomName,
                    waterSlide = waterSlide,
                    otherMap = step.kind != RoutineStepKind.FULL && step.mapFlag != viewModel.currentMapFlag(),
                    onChange = { change -> viewModel.updateRoutineStep(index, change) },
                    onMove = { delta ->
                        viewModel.updateRoutine { d ->
                            val to = (index + delta).coerceIn(0, d.steps.lastIndex)
                            val list = d.steps.toMutableList()
                            list.add(to, list.removeAt(index))
                            d.copy(steps = list)
                        }
                    },
                    onRemove = { viewModel.updateRoutine { d -> d.copy(steps = d.steps.filterIndexed { i, _ -> i != index }) } },
                    onPickZones = { onPickZones(index) },
                )
            }
            OutlinedButton(
                onClick = {
                    viewModel.updateRoutine { d ->
                        d.copy(steps = d.steps + RoutineStep(RoutineStepKind.ROOMS, mapFlag = viewModel.currentMapFlag()))
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = draft.steps.size < MAX_STEPS,
            ) {
                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(Modifier.width(6.dp))
                Text("Add step")
            }
        }
    }
}

private const val MAX_STEPS = 10

private val STEP_KINDS = listOf(RoutineStepKind.FULL to "Full", RoutineStepKind.ROOMS to "Rooms", RoutineStepKind.ZONES to "Zones")

private val STEP_FAN = listOf(
    VacuumFanPower.OFF to "Off",
    VacuumFanPower.QUIET to "Quiet",
    VacuumFanPower.BALANCED to "Standard",
    VacuumFanPower.TURBO to "Strong",
    VacuumFanPower.MAX to "MAX",
)

private val STEP_ROUTES = listOf(
    MopRoute.STANDARD to "Standard",
    MopRoute.DEEP to "Deep",
    MopRoute.DEEP_PLUS to "Deep+",
    MopRoute.FAST to "Fast",
)

/** Water flows: the slide's marks on robots with the water slide (a296), the three levels otherwise. */
private fun stepWater(slide: Boolean) = listOf(WaterBoxMode.OFF to "Off") + if (slide) {
    listOf(WaterBoxMode.SLIDE_LOW to "Low", WaterBoxMode.SLIDE_MEDIUM to "Medium", WaterBoxMode.SLIDE_HIGH to "High")
} else {
    listOf(WaterBoxMode.LOW to "Low", WaterBoxMode.MEDIUM to "Medium", WaterBoxMode.HIGH to "High")
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun RoutineStepCard(
    index: Int,
    count: Int,
    step: RoutineStep,
    rooms: List<Pair<Int, String>>,
    roomName: (Int) -> String?,
    waterSlide: Boolean,
    /** The step's rooms or zones belong to a map other than the one loaded now. */
    otherMap: Boolean,
    onChange: ((RoutineStep) -> RoutineStep) -> Unit,
    onMove: (Int) -> Unit,
    onRemove: () -> Unit,
    onPickZones: () -> Unit,
) {
    Surface(shape = MaterialTheme.shapes.large, color = MaterialTheme.colorScheme.surfaceContainerHigh) {
        Column(Modifier.fillMaxWidth().padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text("Step ${index + 1}", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.primary)
                    Text(stepTitle(step, roomName), style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Medium)
                }
                IconButton(onClick = { onMove(-1) }, enabled = index > 0) { Icon(Icons.Default.KeyboardArrowUp, "Move up") }
                IconButton(onClick = { onMove(1) }, enabled = index < count - 1) { Icon(Icons.Default.KeyboardArrowDown, "Move down") }
                IconButton(onClick = onRemove, enabled = count > 1) { Icon(Icons.Default.Delete, "Remove step") }
            }
            ChipRow("Clean", STEP_KINDS, step.kind) { kind -> onChange { it.copy(kind = kind) } }
            if (otherMap) {
                Text(
                    "Made on another map: load that map to change these rooms or zones. The robot needs that map loaded to run this step.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                )
            }
            when (step.kind) {
                RoutineStepKind.ROOMS -> if (rooms.isEmpty()) {
                    Text("The map has no rooms yet.", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.error)
                } else {
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        rooms.forEach { (id, name) ->
                            val picked = id in step.rooms
                            FilterChip(
                                selected = picked,
                                onClick = { onChange { s -> s.copy(rooms = if (picked) s.rooms - id else s.rooms + id) } },
                                label = { Text(if (picked) "${step.rooms.indexOf(id) + 1}. $name" else name) },
                            )
                        }
                    }
                }
                RoutineStepKind.ZONES -> OutlinedButton(onClick = onPickZones, modifier = Modifier.fillMaxWidth()) {
                    Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(Modifier.width(6.dp))
                    Text(if (step.zones.isEmpty()) "Draw zones on the map" else "Redraw zones (${step.zones.size})")
                }
                RoutineStepKind.FULL -> {}
            }
            ChipRow("Suction", STEP_FAN, step.fanPower) { fan ->
                // Mop only needs water; without either the robot would do nothing.
                onChange { s ->
                    val water = if (fan == VacuumFanPower.OFF && s.waterBoxMode == WaterBoxMode.OFF) stepWater(waterSlide)[1].first else s.waterBoxMode
                    s.copy(fanPower = fan, waterBoxMode = water)
                }
            }
            val water = stepWater(waterSlide)
            val shownWater = water.firstOrNull { it.first == step.waterBoxMode }?.first
                ?: water.minByOrNull { kotlin.math.abs(it.first - step.waterBoxMode) }!!.first
            ChipRow("Water", water, shownWater) { w ->
                onChange { s ->
                    val fan = if (w == WaterBoxMode.OFF && s.fanPower == VacuumFanPower.OFF) VacuumFanPower.BALANCED else s.fanPower
                    s.copy(waterBoxMode = w, fanPower = fan)
                }
            }
            if (step.waterBoxMode != WaterBoxMode.OFF) {
                ChipRow("Mop route", STEP_ROUTES, step.mopMode) { route -> onChange { it.copy(mopMode = route) } }
            }
            ChipRow("Passes", listOf(1 to "1×", 2 to "2×", 3 to "3×"), step.repeat) { r -> onChange { it.copy(repeat = r) } }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun <T> ChipRow(label: String, options: List<Pair<T, String>>, selected: T, onSelect: (T) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(label, style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        FlowRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            options.forEach { (value, text) ->
                FilterChip(selected = value == selected, onClick = { onSelect(value) }, label = { Text(text) })
            }
        }
    }
}
