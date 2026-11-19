package com.kodraliu.localrock.ui.vacuum

import com.kodraliu.localrock.resources.Res
import com.kodraliu.localrock.resources.schedule_add
import com.kodraliu.localrock.resources.schedule_day_fri
import com.kodraliu.localrock.resources.schedule_day_mon
import com.kodraliu.localrock.resources.schedule_day_sat
import com.kodraliu.localrock.resources.schedule_day_short_fri
import com.kodraliu.localrock.resources.schedule_day_short_mon
import com.kodraliu.localrock.resources.schedule_day_short_sat
import com.kodraliu.localrock.resources.schedule_day_short_sun
import com.kodraliu.localrock.resources.schedule_day_short_thu
import com.kodraliu.localrock.resources.schedule_day_short_tue
import com.kodraliu.localrock.resources.schedule_day_short_wed
import com.kodraliu.localrock.resources.schedule_day_sun
import com.kodraliu.localrock.resources.schedule_day_thu
import com.kodraliu.localrock.resources.schedule_day_tue
import com.kodraliu.localrock.resources.schedule_day_wed
import com.kodraliu.localrock.resources.schedule_days
import com.kodraliu.localrock.resources.schedule_delete
import com.kodraliu.localrock.resources.schedule_empty
import com.kodraliu.localrock.resources.schedule_empty_text
import com.kodraliu.localrock.resources.schedule_every_day
import com.kodraliu.localrock.resources.schedule_new
import com.kodraliu.localrock.resources.schedule_time
import com.kodraliu.localrock.resources.schedule_title
import com.kodraliu.localrock.resources.schedule_weekdays
import com.kodraliu.localrock.resources.schedule_weekends
import com.kodraliu.localrock.resources.settings_back
import com.kodraliu.localrock.resources.settings_cancel
import com.kodraliu.localrock.resources.settings_save
import org.jetbrains.compose.resources.stringResource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import com.kodraliu.localrock.ui.rememberUse24HourClock
import com.kodraliu.localrock.ui.adaptive.CenteredContent
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kodraliu.localrock.shared.vacuum.CleanTimer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleScreen(
    viewModel: VacuumViewModel,
    onBack: () -> Unit,
) {
    val repository = viewModel.repository
    val timers by repository.timers.collectAsState()
    var showAddDialog by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { runCatching { repository.fetchTimers() } }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Res.string.schedule_title)) },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, stringResource(Res.string.settings_back)) }
                },
            )
        },
    ) { padding ->
        CenteredContent {
            if (timers.isEmpty()) {
                Box(Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Icon(Icons.Default.Schedule, null, Modifier.size(48.dp), MaterialTheme.colorScheme.onSurfaceVariant)
                        Text(stringResource(Res.string.schedule_empty), style = MaterialTheme.typography.titleMedium)
                        Text(
                            stringResource(Res.string.schedule_empty_text),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            } else {
                LazyColumn(contentPadding = padding) {
                    items(timers, key = { it.id }) { timer ->
                        TimerRow(
                            timer = timer,
                            onToggle = { viewModel.run { it.toggleTimer(timer) } },
                            onDelete = { viewModel.run { it.deleteTimer(timer) } },
                        )
                        HorizontalDivider(Modifier.padding(start = 72.dp))
                    }
                }
            }

            // On wide windows the content column is centred, so keep the FAB with it instead of
            // floating it at the far edge of the window.
            FloatingActionButton(
                onClick = { showAddDialog = true },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 16.dp, bottom = 16.dp + padding.calculateBottomPadding()),
            ) {
                Icon(Icons.Default.Add, stringResource(Res.string.schedule_add))
            }
        }
    }

    if (showAddDialog) {
        AddTimerDialog(
            onAdd = { hour, minute, weekdays ->
                showAddDialog = false
                viewModel.run { it.createTimer(hour, minute, weekdays) }
            },
            onDismiss = { showAddDialog = false },
        )
    }
}

@Composable
private fun TimerRow(timer: CleanTimer, onToggle: () -> Unit, onDelete: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = if (timer.enabled) 1f else 0.4f),
            modifier = Modifier.size(44.dp),
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(Icons.Default.Schedule, null, Modifier.size(22.dp), MaterialTheme.colorScheme.onPrimaryContainer)
            }
        }
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(
                formatClock(timer.hour, timer.minute, rememberUse24HourClock()),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                color = if (timer.enabled) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                formatWeekdays(timer.weekdays),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
        IconButton(onClick = onDelete) {
            Icon(Icons.Default.Delete, stringResource(Res.string.schedule_delete), tint = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Switch(checked = timer.enabled, onCheckedChange = { onToggle() })
    }
}

@Composable
private fun AddTimerDialog(onAdd: (hour: Int, minute: Int, weekdays: Set<Int>) -> Unit, onDismiss: () -> Unit) {
    var hour by remember { mutableIntStateOf(8) }
    var minute by remember { mutableIntStateOf(0) }
    var weekdays by remember { mutableStateOf(setOf(1, 2, 3, 4, 5)) } // Mon–Fri default

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(Res.string.schedule_new)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                Text(stringResource(Res.string.schedule_time), style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                ClockStepper(hour, minute, onHourChange = { hour = it }, onMinuteChange = { minute = it })
                Text(stringResource(Res.string.schedule_days), style = MaterialTheme.typography.labelLarge, color = MaterialTheme.colorScheme.onSurfaceVariant)
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    val dayLabels = listOf(
                        stringResource(Res.string.schedule_day_short_sun), stringResource(Res.string.schedule_day_short_mon), stringResource(Res.string.schedule_day_short_tue), stringResource(Res.string.schedule_day_short_wed), stringResource(Res.string.schedule_day_short_thu), stringResource(Res.string.schedule_day_short_fri), stringResource(Res.string.schedule_day_short_sat),
                    )
                    dayLabels.forEachIndexed { index, label ->
                        val selected = index in weekdays
                        Surface(
                            onClick = { weekdays = if (selected) weekdays - index else weekdays + index },
                            modifier = Modifier.weight(1f).aspectRatio(1f),
                            shape = CircleShape,
                            color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant,
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    label,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                                    textAlign = TextAlign.Center,
                                )
                            }
                        }
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = { onAdd(hour, minute, weekdays) }) { Text(stringResource(Res.string.settings_save)) }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text(stringResource(Res.string.settings_cancel)) }
        },
    )
}

@Composable
internal fun NumberStepper(label: String, value: Int, min: Int, max: Int, step: Int = 1, onChange: (Int) -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(label, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        StepButton("+") { onChange(steppedWithWrap(value, step, min, max)) }
        Text(value.toString().padStart(2, '0'), style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        StepButton("−") { onChange(steppedWithWrap(value, -step, min, max)) }
    }
}

/**
 * [value] moved by [delta] within [min]..[max], wrapping around the ends of the range like a clock.
 * Minutes in steps of 15 go 45, 30, 15, 0 and back to 45, not 0 then 59, 44, 29, 14.
 */
internal fun steppedWithWrap(value: Int, delta: Int, min: Int, max: Int): Int {
    val size = max - min + 1
    return ((value - min + delta) % size + size) % size + min
}

@Composable
private fun formatWeekdays(days: Set<Int>): String {
    if (days.isEmpty()) return stringResource(Res.string.schedule_every_day)
    val names = listOf(
        stringResource(Res.string.schedule_day_sun), stringResource(Res.string.schedule_day_mon), stringResource(Res.string.schedule_day_tue), stringResource(Res.string.schedule_day_wed), stringResource(Res.string.schedule_day_thu), stringResource(Res.string.schedule_day_fri), stringResource(Res.string.schedule_day_sat),
    )
    if (days == setOf(1, 2, 3, 4, 5)) return stringResource(Res.string.schedule_weekdays)
    if (days == setOf(0, 6)) return stringResource(Res.string.schedule_weekends)
    if (days.size == 7) return stringResource(Res.string.schedule_every_day)
    return days.sorted().joinToString(", ") { names.getOrElse(it) { "$it" } }
}
