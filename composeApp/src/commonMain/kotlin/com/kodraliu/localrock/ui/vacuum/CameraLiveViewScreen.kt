package com.kodraliu.localrock.ui.vacuum

import com.kodraliu.localrock.resources.Res
import com.kodraliu.localrock.resources.camera_back_to_map
import com.kodraliu.localrock.resources.camera_change_pin
import com.kodraliu.localrock.resources.camera_clear
import com.kodraliu.localrock.resources.camera_cruise
import com.kodraliu.localrock.resources.camera_cruise_started
import com.kodraliu.localrock.resources.camera_cruise_text
import com.kodraliu.localrock.resources.camera_dock
import com.kodraliu.localrock.resources.camera_draw_instead
import com.kodraliu.localrock.resources.camera_draw_pattern_hint
import com.kodraliu.localrock.resources.camera_drive_forward
import com.kodraliu.localrock.resources.camera_end
import com.kodraliu.localrock.resources.camera_find_pet
import com.kodraliu.localrock.resources.camera_go_back
import com.kodraliu.localrock.resources.camera_live
import com.kodraliu.localrock.resources.camera_looking_for_pet
import com.kodraliu.localrock.resources.camera_pattern_captured
import com.kodraliu.localrock.resources.camera_pause
import com.kodraliu.localrock.resources.camera_pin
import com.kodraliu.localrock.resources.camera_pin_go
import com.kodraliu.localrock.resources.camera_pin_intro_pattern
import com.kodraliu.localrock.resources.camera_pin_intro_typed
import com.kodraliu.localrock.resources.camera_retry
import com.kodraliu.localrock.resources.camera_returning_to_dock
import com.kodraliu.localrock.resources.camera_set_pin
import com.kodraliu.localrock.resources.camera_start
import com.kodraliu.localrock.resources.camera_start_cruise
import com.kodraliu.localrock.resources.camera_stop_talking
import com.kodraliu.localrock.resources.camera_talk
import com.kodraliu.localrock.resources.camera_talk_needs_mic
import com.kodraliu.localrock.resources.camera_talk_unavailable
import com.kodraliu.localrock.resources.camera_turn_left
import com.kodraliu.localrock.resources.camera_turn_right
import com.kodraliu.localrock.resources.camera_type_instead
import com.kodraliu.localrock.resources.camera_unsupported
import com.kodraliu.localrock.resources.common_cancel
import org.jetbrains.compose.resources.stringResource
import kotlin.time.TimeSource
import com.kodraliu.localrock.ui.rememberMicrophonePermissionRequest
import com.kodraliu.localrock.shared.vacuum.map.ParsedMap
import com.kodraliu.localrock.shared.vacuum.VacuumStateCodes
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.toSize
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.Offset
import androidx.compose.material3.AlertDialog
import androidx.compose.material.icons.filled.Pets
import androidx.compose.material.icons.filled.CallEnd
import androidx.compose.material.icons.filled.Call
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.automirrored.filled.RotateLeft
import androidx.compose.material.icons.automirrored.filled.RotateRight
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.VideocamOff
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kodraliu.localrock.shared.vacuum.camera.CameraLiveSession
import com.kodraliu.localrock.shared.webrtc.RtcVideoView
import com.kodraliu.localrock.shared.webrtc.liveViewSupported
import com.kodraliu.localrock.ui.LocalAppContainer
import com.kodraliu.localrock.shared.text.localizedText
import com.kodraliu.localrock.ui.text.text


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CameraLiveViewScreen(
    viewModel: VacuumViewModel,
    onBack: () -> Unit,
    onPinGo: () -> Unit = {},
) {
    val container = LocalAppContainer.current
    val duid = viewModel.repository.duid
    val liveSession = remember { CameraLiveSession(viewModel.repository.session) }
    val state by liveSession.state.collectAsState()
    val peer by liveSession.peer.collectAsState()

    var pin by remember { mutableStateOf(container.appSettings.cameraPin(duid) ?: "") }
    var autoStarted by remember { mutableStateOf(false) }

    val drive = remember { LiveDrive(viewModel.repository) }
    DisposableEffect(Unit) {
        onDispose {
            liveSession.dispose()
            drive.dispose()
        }
    }

    // If a PIN is already saved, connect immediately.
    LaunchedEffect(Unit) {
        if (liveViewSupported && pin.isNotBlank() && !autoStarted) {
            autoStarted = true
            liveSession.start(pin)
        }
    }

    Scaffold(
        containerColor = Color.Black,
        topBar = {
            TopAppBar(
                title = { Text(viewModel.deviceName, color = Color.White) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(Res.string.camera_go_back), tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Black.copy(alpha = 0.6f),
                ),
            )
        },
    ) { paddingValues ->
        Box(
            // Black runs under the navigation bar too; only the video area is inset.
            modifier = Modifier.fillMaxSize().background(Color.Black).padding(paddingValues),
            contentAlignment = Alignment.Center,
        ) {
            if (!liveViewSupported) {
                CameraMessage(
                    icon = { Icon(Icons.Default.VideocamOff, null, Modifier.size(48.dp), tint = Color.White) },
                    text = stringResource(Res.string.camera_unsupported),
                )
                return@Box
            }

            when (val s = state) {
                is CameraLiveSession.State.Idle -> {
                    PinEntry(
                        pin = pin,
                        onPinChange = { pin = it },
                        onConnect = {
                            container.appSettings.setCameraPin(duid, pin)
                            liveSession.start(pin)
                        },
                        onSetPinOnRobot = {
                            container.appSettings.setCameraPin(duid, pin)
                            liveSession.setPinAndStart(pin)
                        },
                    )
                }

                is CameraLiveSession.State.Connecting -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        CircularProgressIndicator(color = Color.White)
                        Spacer(Modifier.height(16.dp))
                        Text(s.stage.text(), color = Color.White, style = MaterialTheme.typography.bodyMedium)
                    }
                }

                is CameraLiveSession.State.Streaming -> {
                    peer?.let { RtcVideoView(it, Modifier.fillMaxSize()) }
                    Text(
                        stringResource(Res.string.camera_live),
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .padding(top = 12.dp, end = 16.dp)
                            .background(Color.Red.copy(alpha = 0.8f), MaterialTheme.shapes.small)
                            .padding(horizontal = 8.dp, vertical = 2.dp),
                        color = Color.White,
                        style = MaterialTheme.typography.labelSmall,
                    )
                    val robotStatus by viewModel.status.collectAsState()
                    val liveMap by viewModel.repository.parsedMap.collectAsState()
                    val talkScope = rememberCoroutineScope()
                    var talking by remember { mutableStateOf(false) }
                    var talkStarted by remember { mutableStateOf<TimeSource.Monotonic.ValueTimeMark?>(null) }
                    var talkError by remember { mutableStateOf<String?>(null) }
                    var cruisePicking by remember { mutableStateOf(false) }
                    var patrolling by remember { mutableStateOf(false) }
                    DisposableEffect(Unit) {
                        onDispose { if (talking) talkScope.launch(NonCancellable) { runCatching { liveSession.setTalking(false) } } }
                    }
                    val needsMic = stringResource(Res.string.camera_talk_needs_mic)
                    val talkUnavailable = stringResource(Res.string.camera_talk_unavailable)
                    val askMic = rememberMicrophonePermissionRequest { granted ->
                        if (!granted) {
                            talkError = needsMic
                        } else {
                            talkScope.launch {
                                runCatching { liveSession.setTalking(true) }
                                    .onSuccess { ok ->
                                        talking = ok
                                        talkStarted = TimeSource.Monotonic.markNow()
                                        if (!ok) talkError = talkUnavailable
                                    }
                                    .onFailure { talkError = it.localizedText() }
                            }
                        }
                    }
                    // A cruise or pet search shows Pause / End / Dock instead of the drive buttons.
                    val state = robotStatus.state
                    if (state == VacuumStateCodes.PATROL) patrolling = true
                    if (patrolling && state != VacuumStateCodes.PATROL && state != VacuumStateCodes.PAUSED) patrolling = false
                    LiveViewControls(
                        drive = drive,
                        onPinGo = onPinGo,
                        patrolling = patrolling,
                        patrolPaused = patrolling && state == VacuumStateCodes.PAUSED,
                        onCruise = { cruisePicking = true },
                        onFindPet = { viewModel.run(Res.string.camera_looking_for_pet) { it.startPetSearch() } },
                        onPatrolPause = { viewModel.run { it.pause() } },
                        onPatrolResume = { viewModel.run { it.resumeCleaning() } },
                        onPatrolEnd = { viewModel.run { it.stopTask() } },
                        onPatrolDock = { viewModel.run(Res.string.camera_returning_to_dock) { it.dock() } },
                    )
                    TalkButton(
                        talking = talking,
                        startedAt = talkStarted,
                        modifier = Modifier.align(Alignment.CenterStart).padding(start = 12.dp),
                        onClick = {
                            talkError = null
                            if (talking) {
                                talking = false
                                talkScope.launch { runCatching { liveSession.setTalking(false) } }
                            } else askMic()
                        },
                    )
                    talkError?.let { msg ->
                        Text(
                            msg,
                            color = Color.White,
                            style = MaterialTheme.typography.labelMedium,
                            modifier = Modifier.align(Alignment.TopCenter).padding(top = 12.dp)
                                .background(Color.Black.copy(alpha = 0.6f), MaterialTheme.shapes.small).padding(horizontal = 10.dp, vertical = 4.dp),
                        )
                    }
                    val cruiseMap = liveMap
                    if (cruisePicking && cruiseMap != null) {
                        CruisePointsDialog(
                            map = cruiseMap,
                            onDismiss = { cruisePicking = false },
                            onStart = { points ->
                                cruisePicking = false
                                viewModel.run(Res.string.camera_cruise_started) { it.startCruise(points) }
                            },
                        )
                    }
                    // The official live view keeps a small map in the corner, with the robot on it.
                    val miniMap by viewModel.repository.parsedMap.collectAsState()
                    miniMap?.let { map ->
                        SavedMapImage(
                            map = map,
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .padding(top = 40.dp, end = 12.dp)
                                .size(width = 150.dp, height = 112.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.Black.copy(alpha = 0.55f))
                                .border(1.dp, Color.White.copy(alpha = 0.25f), RoundedCornerShape(12.dp))
                                .clickable(onClickLabel = stringResource(Res.string.camera_back_to_map), onClick = onBack)
                                .padding(6.dp),
                        )
                    }
                }

                is CameraLiveSession.State.Failed -> {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(24.dp),
                    ) {
                        Icon(Icons.Default.VideocamOff, null, Modifier.size(48.dp), tint = Color.White)
                        Spacer(Modifier.height(12.dp))
                        Text(
                            s.text.text(),
                            color = Color.White,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                        Spacer(Modifier.height(16.dp))
                        Button(onClick = { liveSession.start(pin) }) { Text(stringResource(Res.string.camera_retry)) }
                        TextButton(onClick = {
                            container.appSettings.setCameraPin(duid, null)
                            pin = ""
                            liveSession.stop()
                        }) { Text(stringResource(Res.string.camera_change_pin), color = Color.White) }
                    }
                }
            }
        }
    }
}

@Composable
private fun PinEntry(
    pin: String,
    onPinChange: (String) -> Unit,
    onConnect: () -> Unit,
    onSetPinOnRobot: () -> Unit,
) {
    // Newer models draw the camera PIN as a pattern; older ones type it. Default to drawing and
    // keep the keypad as a fallback so the S8-era flow still works.
    var typed by remember { mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.padding(32.dp),
    ) {
        Text(
            if (typed) {
                stringResource(Res.string.camera_pin_intro_typed)
            } else {
                stringResource(Res.string.camera_pin_intro_pattern)
            },
            color = Color.White,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyMedium,
        )
        if (typed) {
            OutlinedTextField(
                value = pin,
                onValueChange = onPinChange,
                label = { Text(stringResource(Res.string.camera_pin)) },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                modifier = Modifier.fillMaxWidth(),
            )
        } else {
            PatternPinInput(onPattern = onPinChange)
            Text(
                if (pin.isBlank()) stringResource(Res.string.camera_draw_pattern_hint) else stringResource(Res.string.camera_pattern_captured),
                color = Color.White.copy(alpha = 0.7f),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodySmall,
            )
        }
        Button(onClick = onConnect, enabled = pin.isNotBlank()) { Text(stringResource(Res.string.camera_start)) }
        TextButton(onClick = onSetPinOnRobot, enabled = pin.isNotBlank()) {
            Text(stringResource(Res.string.camera_set_pin), color = Color.White)
        }
        TextButton(
            onClick = {
                typed = !typed
                onPinChange("")
            },
        ) {
            Text(
                if (typed) stringResource(Res.string.camera_draw_instead) else stringResource(Res.string.camera_type_instead),
                color = Color.White.copy(alpha = 0.7f),
            )
        }
    }
}

@Composable
private fun CameraMessage(icon: @Composable () -> Unit, text: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(24.dp),
    ) {
        icon()
        Spacer(Modifier.height(12.dp))
        Text(text, color = Color.White, textAlign = TextAlign.Center)
    }
}


/**
 * Drives the robot from the live view, as the official app's video screen does: hold an arrow to keep
 * moving. Remote control starts on the first press (`app_rc_start`) and ends when the screen closes.
 */
private class LiveDrive(private val repository: com.kodraliu.localrock.shared.vacuum.VacuumRepository) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private var started = false
    private var holding: Job? = null

    fun hold(velocity: Double, omega: Double) {
        holding?.cancel()
        holding = scope.launch {
            if (!started) {
                runCatching { repository.startRemoteControl() }
                started = true
            }
            while (isActive) {
                runCatching { repository.moveRemote(velocity, omega, MOVE_MS) }
                delay(REPEAT_MS)
            }
        }
    }

    fun release() {
        holding?.cancel()
        holding = null
    }

    fun dispose() {
        release()
        if (started) scope.launch(NonCancellable) { runCatching { repository.stopRemoteControl() } }
            .invokeOnCompletion { scope.cancel() }
        else scope.cancel()
    }

    private companion object {
        const val MOVE_MS = 600
        const val REPEAT_MS = 400L
    }
}

@Composable
private fun BoxScope.LiveViewControls(
    drive: LiveDrive,
    onPinGo: () -> Unit,
    patrolling: Boolean = false,
    patrolPaused: Boolean = false,
    onCruise: () -> Unit = {},
    onFindPet: () -> Unit = {},
    onPatrolPause: () -> Unit = {},
    onPatrolResume: () -> Unit = {},
    onPatrolEnd: () -> Unit = {},
    onPatrolDock: () -> Unit = {},
) {
    val glass = ButtonDefaults.filledTonalButtonColors(containerColor = Color.Black.copy(alpha = 0.45f), contentColor = Color.White)
    // The official live view's actions: Pin & Go, a cruise past points (Navigate) and Find the pet;
    // during a cruise or search they become Pause / End / Dock.
    // A column, so the mini map in the other corner never covers a button in portrait.
    Column(Modifier.align(Alignment.TopStart).padding(12.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
        if (patrolling) {
            // Resuming a paused cruise is not captured yet (the official app's "Hervatten"), so a paused
            // cruise or search can only be ended or sent to the dock.
            if (!patrolPaused) {
                FilledTonalButton(onClick = onPatrolPause, colors = glass) { Text(stringResource(Res.string.camera_pause)) }
            }
            FilledTonalButton(onClick = onPatrolEnd, colors = glass) { Text(stringResource(Res.string.camera_end)) }
            FilledTonalButton(onClick = onPatrolDock, colors = glass) { Text(stringResource(Res.string.camera_dock)) }
        } else {
            FilledTonalButton(onClick = onPinGo, colors = glass) {
                Icon(Icons.Default.Place, null, Modifier.size(18.dp))
                Spacer(Modifier.width(6.dp))
                Text(stringResource(Res.string.camera_pin_go))
            }
            FilledTonalButton(onClick = onCruise, colors = glass) { Text(stringResource(Res.string.camera_cruise)) }
            FilledTonalButton(onClick = onFindPet, colors = glass) {
                Icon(Icons.Default.Pets, null, Modifier.size(18.dp))
                Spacer(Modifier.width(6.dp))
                Text(stringResource(Res.string.camera_find_pet))
            }
        }
    }
    if (patrolling) return
    HoldButton(
        icon = Icons.Default.KeyboardArrowUp,
        label = stringResource(Res.string.camera_drive_forward),
        modifier = Modifier.align(Alignment.BottomStart).padding(24.dp),
        onPress = { drive.hold(0.3, 0.0) },
        onRelease = drive::release,
    )
    Row(
        modifier = Modifier.align(Alignment.BottomEnd).padding(24.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        HoldButton(Icons.AutoMirrored.Filled.RotateLeft, stringResource(Res.string.camera_turn_left), onPress = { drive.hold(0.0, 0.5) }, onRelease = drive::release)
        HoldButton(Icons.AutoMirrored.Filled.RotateRight, stringResource(Res.string.camera_turn_right), onPress = { drive.hold(0.0, -0.5) }, onRelease = drive::release)
    }
}

@Composable
private fun HoldButton(
    icon: ImageVector,
    label: String,
    modifier: Modifier = Modifier,
    onPress: () -> Unit,
    onRelease: () -> Unit,
) {
    Box(
        modifier = modifier
            .size(64.dp)
            .background(Color.Black.copy(alpha = 0.45f), CircleShape)
            .semantics { contentDescription = label }
            .pointerInput(Unit) {
                detectTapGestures(onPress = {
                    onPress()
                    try {
                        tryAwaitRelease()
                    } finally {
                        onRelease()
                    }
                })
            },
        contentAlignment = Alignment.Center,
    ) {
        Icon(icon, null, Modifier.size(32.dp), tint = Color.White)
    }
}


/** Talk-back as the official live view: a green phone to start, a red one with the time to hang up. */
@Composable
private fun TalkButton(talking: Boolean, startedAt: TimeSource.Monotonic.ValueTimeMark?, onClick: () -> Unit, modifier: Modifier = Modifier) {
    var elapsed by remember { mutableStateOf(0L) }
    LaunchedEffect(talking, startedAt) {
        while (talking && startedAt != null) {
            elapsed = startedAt.elapsedNow().inWholeSeconds
            delay(1_000)
        }
    }
    val talkLabel = if (talking) stringResource(Res.string.camera_stop_talking) else stringResource(Res.string.camera_talk)
    Row(modifier, verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Box(
            Modifier.size(48.dp).background(if (talking) Color(0xFFE5484D) else Color(0xFF2BBF8A), CircleShape)
                .semantics { contentDescription = talkLabel }
                .pointerInput(Unit) { detectTapGestures(onTap = { onClick() }) },
            contentAlignment = Alignment.Center,
        ) {
            Icon(if (talking) Icons.Default.CallEnd else Icons.Default.Call, null, tint = Color.White)
        }
        if (talking) {
            val s = elapsed
            Text(
                "${(s / 60).toString().padStart(2, '0')}:${(s % 60).toString().padStart(2, '0')}",
                color = Color.White,
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.background(Color.Black.copy(alpha = 0.45f), MaterialTheme.shapes.small).padding(horizontal = 8.dp, vertical = 2.dp),
            )
        }
    }
}

/** Tap points on the map for a cruise, numbered in order, as the official live view's Navigate. */
@Composable
private fun CruisePointsDialog(map: ParsedMap, onDismiss: () -> Unit, onStart: (List<Pair<Int, Int>>) -> Unit) {
    val bitmap = rememberMapBitmap(map)
    var points by remember { mutableStateOf<List<Offset>>(emptyList()) }
    var canvasSize by remember { mutableStateOf(Size.Zero) }
    val numberMeasurer = rememberTextMeasurer()
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(Res.string.camera_cruise)) },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(stringResource(Res.string.camera_cruise_text), style = MaterialTheme.typography.bodySmall)
                Canvas(
                    Modifier.fillMaxWidth().height(300.dp)
                        .onSizeChanged { canvasSize = it.toSize() }
                        .pointerInput(Unit) { detectTapGestures { points = points + it } },
                ) {
                    val content = map.fittedSize(size)
                    val origin = Offset((size.width - content.width) / 2f, (size.height - content.height) / 2f)
                    drawImage(
                        bitmap,
                        dstOffset = IntOffset(origin.x.toInt(), origin.y.toInt()),
                        dstSize = IntSize(content.width.toInt(), content.height.toInt()),
                    )
                    points.forEachIndexed { i, p ->
                        drawCircle(Color(0xFF2B7FFF), radius = 11.dp.toPx(), center = p)
                        val label = numberMeasurer.measure("${i + 1}", TextStyle(color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold))
                        drawText(label, topLeft = Offset(p.x - label.size.width / 2f, p.y - label.size.height / 2f))
                    }
                }
            }
        },
        confirmButton = {
            TextButton(enabled = points.isNotEmpty(), onClick = {
                val content = map.fittedSize(canvasSize)
                val origin = Offset((canvasSize.width - content.width) / 2f, (canvasSize.height - content.height) / 2f)
                onStart(points.map { map.canvasToMm(it - origin, content.width, content.height) })
            }) { Text(stringResource(Res.string.camera_start_cruise)) }
        },
        dismissButton = {
            Row {
                if (points.isNotEmpty()) TextButton(onClick = { points = emptyList() }) { Text(stringResource(Res.string.camera_clear)) }
                TextButton(onClick = onDismiss) { Text(stringResource(Res.string.common_cancel)) }
            }
        },
    )
}
