package com.kodraliu.localrock.ui.devices

import com.kodraliu.localrock.shared.vacuum.taskRunning
import com.kodraliu.localrock.shared.vacuum.taskPausedAtDock
import kotlinx.coroutines.delay
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.LinearProgressIndicator
import com.kodraliu.localrock.ui.vacuum.ACTIVE_CLEANING_STATES
import com.kodraliu.localrock.shared.AppContainer
import com.kodraliu.localrock.shared.vacuum.VacuumStateCodes
import com.kodraliu.localrock.shared.vacuum.VacuumStatus
import com.kodraliu.localrock.ui.exceptBottom
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kodraliu.localrock.shared.device.Reachability
import com.kodraliu.localrock.shared.model.Device
import com.kodraliu.localrock.shared.onboarding.vacuumPairingSupported
import com.kodraliu.localrock.ui.AppColors
import com.kodraliu.localrock.ui.icons.RobotVacuum

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeviceListScreen(
    viewModel: DeviceListViewModel,
    onSettings: () -> Unit,
    onDeviceClick: (Device) -> Unit,
    onAddVacuum: () -> Unit,
) {
    val devices by viewModel.devices.collectAsState()
    val products by viewModel.productsByDuid.collectAsState()
    val reachability by viewModel.reachability.collectAsState()
    val statuses by viewModel.statuses.collectAsState()
    val actionError by viewModel.actionError.collectAsState()
    // Keep the cards current while the list is on screen, as the official app's home page does.
    LaunchedEffect(Unit) {
        while (true) {
            delay(15_000)
            viewModel.refreshStatuses()
        }
    }
    val refreshing by viewModel.refreshing.collectAsState()
    val error by viewModel.error.collectAsState()
    var showLogoutDialog by remember { mutableStateOf(false) }
    var showOverflow by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My vacuums") },
                actions = {
                    IconButton(onClick = onSettings) {
                        Icon(Icons.Default.Settings, contentDescription = "Settings")
                    }
                    // Refresh moved to pull-to-refresh, so the bar keeps only two actions.
                    Box {
                        IconButton(onClick = { showOverflow = true }) {
                            Icon(Icons.Default.MoreVert, contentDescription = "More")
                        }
                        DropdownMenu(expanded = showOverflow, onDismissRequest = { showOverflow = false }) {
                            DropdownMenuItem(
                                text = { Text("Sign out") },
                                leadingIcon = { Icon(Icons.AutoMirrored.Filled.Logout, contentDescription = null) },
                                onClick = {
                                    showOverflow = false
                                    showLogoutDialog = true
                                },
                            )
                        }
                    }
                },
            )
        },
        floatingActionButton = {
            if (vacuumPairingSupported && devices.isNotEmpty()) {
                ExtendedFloatingActionButton(
                    onClick = onAddVacuum,
                    icon = { Icon(Icons.Default.Add, contentDescription = null) },
                    text = { Text("Add vacuum") },
                )
            }
        },
    ) { padding ->
        PullToRefreshBox(
            isRefreshing = refreshing,
            onRefresh = viewModel::refresh,
            modifier = Modifier.fillMaxSize().padding(padding.exceptBottom()),
        ) {
            when {
                refreshing && devices.isEmpty() && error == null -> {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator()
                    }
                }

                devices.isEmpty() -> {
                    EmptyState(
                        error = error,
                        canPair = vacuumPairingSupported,
                        onAddVacuum = onAddVacuum,
                        onRetry = viewModel::refresh,
                    )
                }

                else -> {
                    LazyVerticalGrid(
                        columns = GridCells.Adaptive(minSize = 320.dp),
                        modifier = Modifier.fillMaxSize(),
                        // Edge to edge: the grid scrolls behind the navigation bar and ends above it.
                        contentPadding = PaddingValues(
                            start = 16.dp,
                            top = 16.dp,
                            end = 16.dp,
                            bottom = 16.dp + padding.calculateBottomPadding(),
                        ),
                        horizontalArrangement = Arrangement.spacedBy(10.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                    ) {
                        actionError?.let { msg ->
                            item(span = { GridItemSpan(maxLineSpan) }) { ErrorCard(message = msg, onRetry = viewModel::refresh) }
                        }
                        error?.let { msg ->
                            item(span = { GridItemSpan(maxLineSpan) }) {
                                ErrorCard(message = msg, onRetry = viewModel::refresh)
                            }
                        }
                        items(devices, key = { it.duid }) { device ->
                            DeviceCard(
                                device = device,
                                model = products[device.duid]?.model.orEmpty(),
                                reachability = reachability[device.duid],
                                status = statuses[device.duid],
                                onAction = { viewModel.quickAction(device, it) },
                                onClick = { onDeviceClick(device) },
                            )
                        }
                        item(span = { GridItemSpan(maxLineSpan) }) { Spacer(Modifier.height(80.dp)) }
                    }
                }
            }
        }
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Sign out?") },
            text = { Text("You'll need to log in again with an email code to control your vacuums.") },
            confirmButton = {
                TextButton(onClick = { showLogoutDialog = false; viewModel.logout() }) {
                    Text("Sign out", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) { Text("Cancel") }
            },
        )
    }
}

/** One empty screen that also carries the error case, so the list is never blank without a reason. */
@Composable
private fun EmptyState(
    error: String?,
    canPair: Boolean,
    onAddVacuum: () -> Unit,
    onRetry: () -> Unit,
) {
    Box(Modifier.fillMaxSize().padding(32.dp), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Icon(
                if (error != null) Icons.Default.ErrorOutline else RobotVacuum,
                contentDescription = null,
                modifier = Modifier.size(56.dp),
                tint = if (error != null) MaterialTheme.colorScheme.error
                else MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(
                if (error != null) "Can't reach your server" else "No robots yet",
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                error ?: "Add your first vacuum, or pull down to check the server again.",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
            )
            Spacer(Modifier.height(4.dp))
            if (error != null) {
                Button(onClick = onRetry) {
                    Icon(Icons.Default.Refresh, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Try again")
                }
            } else if (canPair) {
                Button(onClick = onAddVacuum) {
                    Icon(Icons.Default.Add, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text("Add vacuum")
                }
            }
        }
    }
}

@Composable
private fun ErrorCard(message: String, onRetry: () -> Unit) {
    Surface(
        color = MaterialTheme.colorScheme.errorContainer,
        shape = MaterialTheme.shapes.large,
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier.padding(start = 14.dp, top = 8.dp, end = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                message,
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colorScheme.onErrorContainer,
                style = MaterialTheme.typography.bodySmall,
            )
            TextButton(onClick = onRetry) { Text("Retry") }
        }
    }
}

@Composable
private fun DeviceCard(
    device: Device,
    model: String,
    reachability: Reachability?,
    status: VacuumStatus?,
    onAction: (AppContainer.QuickAction) -> Unit,
    onClick: () -> Unit,
) {
    // Without a local key there is nothing to talk to, so say that instead of silently ignoring taps.
    val notPaired = device.localKey == null
    // The robot's own answer wins; the server's flag is only used when the robot could not be asked.
    val online = when (reachability) {
        Reachability.ONLINE -> true
        Reachability.OFFLINE, Reachability.CHECKING -> false
        null -> device.online
    }
    val statusColor = when {
        notPaired -> MaterialTheme.colorScheme.error
        online -> AppColors.Good
        else -> MaterialTheme.colorScheme.outline
    }
    val statusText = when {
        notPaired -> "Setup incomplete"
        reachability == Reachability.CHECKING -> "Checking…"
        online -> "Online"
        else -> "Offline"
    }

    Surface(
        onClick = onClick,
        enabled = !notPaired,
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.extraLarge,
        color = MaterialTheme.colorScheme.surfaceContainerHigh,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
    ) {
      Column {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            // A per-robot state badge is more useful here than repeating the app icon on every row.
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = if (online && !notPaired) MaterialTheme.colorScheme.primaryContainer
                else MaterialTheme.colorScheme.surfaceContainerHighest,
                modifier = Modifier.size(48.dp),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        RobotVacuum,
                        contentDescription = null,
                        modifier = Modifier.size(26.dp),
                        tint = if (online && !notPaired) MaterialTheme.colorScheme.onPrimaryContainer
                        else MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
            Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    device.name,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold,
                )
                if (model.isNotEmpty()) {
                    Text(
                        model,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(5.dp),
                    modifier = Modifier.padding(top = 3.dp),
                ) {
                    Surface(shape = CircleShape, color = statusColor, modifier = Modifier.size(7.dp)) {}
                    Text(
                        statusText,
                        style = MaterialTheme.typography.labelSmall,
                        color = statusColor,
                    )
                }
                if (notPaired) {
                    Text(
                        "This robot has no local key yet. Re-run pairing on your server to control it.",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
            if (!notPaired) {
                Icon(
                    Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        if (online && !notPaired && status?.state != null) {
            LiveRobotPanel(status, onAction)
        }
      }
    }
}

/**
 * What the robot is doing, as the official app's home card shows it: the task, how long it has been
 * running, the battery and progress, with start or pause and back to the dock at hand.
 */
@Composable
private fun LiveRobotPanel(status: VacuumStatus, onAction: (AppContainer.QuickAction) -> Unit) {
    val state = status.state ?: return
    val cleaning = state in ACTIVE_CLEANING_STATES || status.taskRunning
    val paused = state == VacuumStateCodes.PAUSED || status.taskPausedAtDock
    val minutes = status.cleanTime?.takeIf { (cleaning || paused) && it > 0 }?.let { it / 60 }
    Column(Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(
                when {
                    status.taskPausedAtDock -> "Paused at the dock"
                    status.dryStatus == 1 && !cleaning -> "Drying mop"
                    else -> VacuumStateCodes.describe(state)
                },
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(1f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            minutes?.let { Text("$it min", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant) }
            status.battery?.let { Text("$it%", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant) }
        }
        val percent = status.cleanPercent?.takeIf { (cleaning || paused) && it in 0..100 }
        if (percent != null) {
            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                LinearProgressIndicator(progress = { percent / 100f }, modifier = Modifier.weight(1f))
                Text("$percent%", style = MaterialTheme.typography.labelLarge)
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            FilledTonalButton(
                onClick = { onAction(if (cleaning) AppContainer.QuickAction.PAUSE else AppContainer.QuickAction.START_OR_RESUME) },
                modifier = Modifier.weight(1f),
            ) {
                Icon(if (cleaning) Icons.Default.Pause else Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(20.dp))
                Spacer(Modifier.width(6.dp))
                Text(if (cleaning) "Pause" else if (paused) "Resume" else "Clean")
            }
            FilledTonalIconButton(onClick = { onAction(AppContainer.QuickAction.DOCK) }) {
                Icon(Icons.Default.Home, contentDescription = "Return to dock")
            }
        }
    }
}
