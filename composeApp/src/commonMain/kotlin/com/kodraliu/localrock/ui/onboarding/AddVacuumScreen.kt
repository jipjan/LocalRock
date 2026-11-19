package com.kodraliu.localrock.ui.onboarding

import com.kodraliu.localrock.ui.exceptBottom
import com.kodraliu.localrock.ui.adaptive.CenteredContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Switch
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.kodraliu.localrock.shared.AppContainer
import com.kodraliu.localrock.shared.onboarding.DEFAULT_IANA_TZ
import com.kodraliu.localrock.shared.onboarding.OnboardingDevice
import com.kodraliu.localrock.shared.onboarding.OnboardingInput
import com.kodraliu.localrock.shared.onboarding.OnboardingSession
import com.kodraliu.localrock.shared.onboarding.VacuumOnboarder
import com.kodraliu.localrock.shared.onboarding.countryDomainFromIana
import com.kodraliu.localrock.shared.onboarding.cameraDomainInput
import com.kodraliu.localrock.shared.onboarding.posixTzFromIana
import com.kodraliu.localrock.shared.onboarding.supportedIanaTimezones
import com.kodraliu.localrock.ui.LocalAppContainer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.kodraliu.localrock.resources.Res
import com.kodraliu.localrock.resources.onboarding_admin_password
import com.kodraliu.localrock.resources.onboarding_after_join
import com.kodraliu.localrock.resources.onboarding_back
import com.kodraliu.localrock.resources.onboarding_camera_as_server
import com.kodraliu.localrock.resources.onboarding_camera_as_server_hint
import com.kodraliu.localrock.resources.onboarding_camera_domain
import com.kodraliu.localrock.resources.onboarding_camera_domain_hint
import com.kodraliu.localrock.resources.onboarding_complete
import com.kodraliu.localrock.resources.onboarding_conflict
import com.kodraliu.localrock.resources.onboarding_device_connected
import com.kodraliu.localrock.resources.onboarding_device_key_ready
import com.kodraliu.localrock.resources.onboarding_device_not_connected
import com.kodraliu.localrock.resources.onboarding_device_unsupported
import com.kodraliu.localrock.resources.onboarding_err_no_server_url
import com.kodraliu.localrock.resources.onboarding_err_no_session
import com.kodraliu.localrock.resources.onboarding_err_select_first
import com.kodraliu.localrock.resources.onboarding_home_wifi
import com.kodraliu.localrock.resources.onboarding_home_wifi_hint
import com.kodraliu.localrock.resources.onboarding_intro
import com.kodraliu.localrock.resources.onboarding_load_vacuums
import com.kodraliu.localrock.resources.onboarding_loading_vacuums
import com.kodraliu.localrock.resources.onboarding_log_enter_details
import com.kodraliu.localrock.resources.onboarding_log_ready
import com.kodraliu.localrock.resources.onboarding_network_details
import com.kodraliu.localrock.resources.onboarding_no_devices
import com.kodraliu.localrock.resources.onboarding_no_key
import com.kodraliu.localrock.resources.onboarding_pairing_failed
import com.kodraliu.localrock.resources.onboarding_pairing_robot
import com.kodraliu.localrock.resources.onboarding_pairing_status
import com.kodraliu.localrock.resources.onboarding_robot_connected
import com.kodraliu.localrock.resources.onboarding_select_vacuum
import com.kodraliu.localrock.resources.onboarding_server_status
import com.kodraliu.localrock.resources.onboarding_server_vacuum
import com.kodraliu.localrock.resources.onboarding_server_vacuum_text
import com.kodraliu.localrock.resources.onboarding_setup_logs
import com.kodraliu.localrock.resources.onboarding_start_pairing
import com.kodraliu.localrock.resources.onboarding_step_awaiting_ack
import com.kodraliu.localrock.resources.onboarding_step_awaiting_hello
import com.kodraliu.localrock.resources.onboarding_step_done
import com.kodraliu.localrock.resources.onboarding_step_idle
import com.kodraliu.localrock.resources.onboarding_step_joining_wifi
import com.kodraliu.localrock.resources.onboarding_step_sending_hello
import com.kodraliu.localrock.resources.onboarding_step_sending_wifi
import com.kodraliu.localrock.resources.onboarding_timeout_has_key
import com.kodraliu.localrock.resources.onboarding_timeout_none
import com.kodraliu.localrock.resources.onboarding_timeout_reached
import com.kodraliu.localrock.resources.onboarding_timezone
import com.kodraliu.localrock.resources.onboarding_timezone_info
import com.kodraliu.localrock.resources.onboarding_title
import com.kodraliu.localrock.resources.onboarding_unknown_error
import com.kodraliu.localrock.resources.onboarding_vacuum_to_onboard
import com.kodraliu.localrock.resources.onboarding_wifi_password
import com.kodraliu.localrock.resources.onboarding_wifi_sent
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import com.kodraliu.localrock.resources.onboarding_hide_password
import com.kodraliu.localrock.resources.onboarding_show_password

private class AddVacuumModel(
    private val container: AppContainer,
    private val scope: CoroutineScope,
) {
    val onboarder = VacuumOnboarder()
    val step: StateFlow<VacuumOnboarder.Step> = onboarder.step
    private val adminApi = container.onboardingAdminApi

    private val _running = MutableStateFlow(false)
    val running: StateFlow<Boolean> = _running.asStateFlow()

    private val _loadingDevices = MutableStateFlow(false)
    val loadingDevices: StateFlow<Boolean> = _loadingDevices.asStateFlow()

    private val _devices = MutableStateFlow<List<OnboardingDevice>>(emptyList())
    val devices: StateFlow<List<OnboardingDevice>> = _devices.asStateFlow()

    private val _selectedDuid = MutableStateFlow<String?>(null)
    val selectedDuid: StateFlow<String?> = _selectedDuid.asStateFlow()

    private val _serverStatus = MutableStateFlow<String?>(null)
    val serverStatus: StateFlow<String?> = _serverStatus.asStateFlow()

    private val _result = MutableStateFlow<String?>(null)
    val result: StateFlow<String?> = _result.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun selectDuid(duid: String) { _selectedDuid.value = duid }

    fun loadDevices(adminPassword: String) {
        if (_loadingDevices.value || _running.value) return
        scope.launch {
            _loadingDevices.value = true
            _error.value = null
            try {
                container.appSettings.setAdminPassword(adminPassword)
                adminApi.login(adminPassword)
                val list = adminApi.listDevices()
                _devices.value = list
                if (_selectedDuid.value == null || _selectedDuid.value !in list.map { it.duid }) {
                    _selectedDuid.value = list.firstOrNull()?.duid
                }
                if (list.isEmpty()) {
                    _error.value = getString(Res.string.onboarding_no_devices)
                }
            } catch (e: Throwable) {
                _error.value = describe(e)
            } finally {
                _loadingDevices.value = false
            }
        }
    }

    fun start(
        adminPassword: String,
        ssid: String,
        password: String,
        timezone: String,
        cameraDomain: String = "",
        cameraDomainAsServer: Boolean = false,
    ) {
        if (_running.value) return
        val duid = _selectedDuid.value
        scope.launch {
            _running.value = true
            _error.value = null
            _result.value = null
            _serverStatus.value = null
            try {
                val baseUrl = container.appSettings.serverBaseUrl.value
                    ?: error(getString(Res.string.onboarding_err_no_server_url))
                if (duid == null) error(getString(Res.string.onboarding_err_select_first))

                container.appSettings.setAdminPassword(adminPassword)
                // 1. Register a server-side onboarding session so the server recovers the
                //    vacuum's public key while we provision it.
                adminApi.login(adminPassword)
                val session = adminApi.startSession(duid)
                val sessionId = session.sessionId
                    ?: error(getString(Res.string.onboarding_err_no_session))

                // 2. Push Wi-Fi credentials + server URL to the vacuum over the local UDP handshake.
                onboarder.run(
                    OnboardingInput(
                        serverBaseUrl = baseUrl,
                        timezone = timezone,
                        homeSsid = ssid,
                        homePassword = password,
                        cameraDomain = cameraDomain,
                        cameraDomainAsServer = cameraDomainAsServer,
                    )
                )

                // 3. Poll the session until the vacuum reports in.
                _serverStatus.value = getString(Res.string.onboarding_wifi_sent)
                when (val outcome = pollSession(sessionId)) {
                    PairOutcome.Complete -> _result.value =
                        getString(Res.string.onboarding_complete)
                    is PairOutcome.ConnectedNoKey -> _error.value =
                        getString(Res.string.onboarding_no_key) + outcome.guidance.asSuffix()
                    is PairOutcome.Conflict -> _error.value =
                        getString(Res.string.onboarding_conflict) +
                            outcome.guidance.asSuffix()
                    is PairOutcome.TimedOut -> _result.value = timedOutText(outcome)
                }
            } catch (e: Throwable) {
                _error.value = describe(e)
            } finally {
                _running.value = false
            }
        }
    }

    private suspend fun pollSession(sessionId: String): PairOutcome {
        var last: OnboardingSession? = null
        repeat(POLL_ATTEMPTS) {
            val snap = runCatching { adminApi.getSession(sessionId) }.getOrNull()
            if (snap != null) {
                last = snap
                _serverStatus.value = buildString {
                    append("samples=${snap.querySamples}")
                    append(" • public key ${if (snap.hasPublicKey) "recovered" else "pending"}")
                    snap.publicKeyState?.takeIf { it.isNotBlank() }?.let { append(" ($it)") }
                    append(" • ${if (snap.connected) "connected" else "waiting"}")
                    snap.status?.takeIf { it.isNotBlank() }?.let { append(" • $it") }
                }
                if (snap.status == STATUS_CONFLICT) return PairOutcome.Conflict(snap.guidance)
                // The server defines success as connected AND the public key recovered. Without the
                // key the MQTT bridge cannot work, which surfaces later as "paired but no data".
                if (snap.complete || (snap.connected && snap.hasPublicKey)) return PairOutcome.Complete
            }
            delay(POLL_INTERVAL_MS)
        }
        return if (last?.connected == true) {
            PairOutcome.ConnectedNoKey(last?.guidance)
        } else {
            PairOutcome.TimedOut(last?.querySamples ?: 0, last?.hasPublicKey == true, last?.guidance)
        }
    }

    /**
     * What to do next depends on how far the vacuum got. Reports from several models agree that
     * pairing takes two or three rounds: one or two until the server has the public key, then one
     * more for the vacuum to connect.
     */
    private suspend fun timedOutText(outcome: PairOutcome.TimedOut): String = getString(
        when {
            outcome.hasPublicKey -> Res.string.onboarding_timeout_has_key
            outcome.samples > 0 -> Res.string.onboarding_timeout_reached
            else -> Res.string.onboarding_timeout_none
        },
    ) + outcome.guidance.asSuffix()

    private companion object {
        const val POLL_INTERVAL_MS = 5_000L
        const val POLL_ATTEMPTS = 60 // ~5 minutes
        const val STATUS_CONFLICT = "conflict"
    }
}

private sealed interface PairOutcome {
    /** Connected and the public key was recovered — the vacuum is usable. */
    data object Complete : PairOutcome
    /** Reached the server, but no public key: it will show up with no data. */
    data class ConnectedNoKey(val guidance: String?) : PairOutcome
    /** Server reports the vacuum is already registered under other credentials. */
    data class Conflict(val guidance: String?) : PairOutcome
    /** Not connected within the wait window; [samples] and [hasPublicKey] say how far it got. */
    data class TimedOut(val samples: Int, val hasPublicKey: Boolean, val guidance: String?) : PairOutcome
}

private fun String?.asSuffix(): String = this?.takeIf { it.isNotBlank() }?.let { " $it" } ?: ""

private suspend fun describe(e: Throwable): String =
    "${e::class.simpleName}: ${e.message ?: getString(Res.string.onboarding_unknown_error)}"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddVacuumScreen(onBack: () -> Unit) {
    val container = LocalAppContainer.current
    val scope = rememberCoroutineScope()
    val model = remember { AddVacuumModel(container, scope) }

    val step by model.step.collectAsState()
    val running by model.running.collectAsState()
    val loadingDevices by model.loadingDevices.collectAsState()
    val devices by model.devices.collectAsState()
    val selectedDuid by model.selectedDuid.collectAsState()
    val serverStatus by model.serverStatus.collectAsState()
    val result by model.result.collectAsState()
    val error by model.error.collectAsState()

    var adminPassword by remember { mutableStateOf(container.appSettings.adminPassword.value.orEmpty()) }
    var ssid by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var timezone by remember { mutableStateOf(DEFAULT_IANA_TZ) }
    var cameraDomain by remember { mutableStateOf("") }
    var cameraDomainAsServer by remember { mutableStateOf(false) }
    val cameraDomainError = runCatching { cameraDomainInput(cameraDomain) }.exceptionOrNull()?.message

    val canStartPairing = !running && !loadingDevices &&
        selectedDuid != null && ssid.isNotBlank() && password.isNotBlank() && timezone.isNotBlank() &&
        cameraDomainError == null

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Res.string.onboarding_title)) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(Res.string.onboarding_back))
                    }
                },
            )
        },
    ) { padding ->
        CenteredContent {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding.exceptBottom())
                    .verticalScroll(rememberScrollState())
                    // Edge to edge: the content scrolls behind the navigation bar and ends above it.
                    .padding(bottom = padding.calculateBottomPadding())
                    .padding(horizontal = 16.dp, vertical = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Text(
                    stringResource(Res.string.onboarding_intro),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )

                ServerVacuumCard(
                    adminPassword = adminPassword,
                    onAdminPasswordChange = { adminPassword = it },
                    devices = devices,
                    selectedDuid = selectedDuid,
                    loadingDevices = loadingDevices,
                    enabled = !running,
                    onLoadDevices = { model.loadDevices(adminPassword.trim()) },
                    onSelectDevice = { model.selectDuid(it) },
                )

                NetworkDetailsCard(
                    ssid = ssid,
                    password = password,
                    timezone = timezone,
                    cameraDomain = cameraDomain,
                    cameraDomainError = cameraDomainError,
                    onCameraDomainChange = { cameraDomain = it },
                    cameraDomainAsServer = cameraDomainAsServer,
                    onCameraDomainAsServerChange = { cameraDomainAsServer = it },
                    running = running,
                    canStartPairing = canStartPairing,
                    onSsidChange = { ssid = it },
                    onPasswordChange = { password = it },
                    onTimezoneChange = { timezone = it },
                    onStartPairing = {
                        model.start(
                            adminPassword = adminPassword.trim(),
                            ssid = ssid.trim(),
                            password = password,
                            timezone = timezone.trim(),
                            cameraDomain = cameraDomain.trim(),
                            cameraDomainAsServer = cameraDomainAsServer,
                        )
                    },
                )

                SetupProgressCard(step = step, running = running, serverStatus = serverStatus)

                error?.let { message ->
                    SetupMessageCard(
                        title = stringResource(Res.string.onboarding_pairing_failed),
                        message = message,
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                        contentColor = MaterialTheme.colorScheme.onErrorContainer,
                    )
                }

                result?.let { message ->
                    SetupMessageCard(
                        title = stringResource(Res.string.onboarding_robot_connected),
                        message = message,
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                    )
                }

                SetupLogsCard(step = step, running = running, error = error, result = result)

                Text(
                    stringResource(Res.string.onboarding_after_join),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 4.dp),
                )

                Spacer(Modifier.height(8.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun NetworkDetailsCard(
    ssid: String,
    password: String,
    timezone: String,
    cameraDomain: String,
    cameraDomainError: String?,
    onCameraDomainChange: (String) -> Unit,
    cameraDomainAsServer: Boolean,
    onCameraDomainAsServerChange: (Boolean) -> Unit,
    running: Boolean,
    canStartPairing: Boolean,
    onSsidChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onTimezoneChange: (String) -> Unit,
    onStartPairing: () -> Unit,
) {
    var passwordVisible by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(stringResource(Res.string.onboarding_network_details), style = MaterialTheme.typography.titleLarge)
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)

            OutlinedTextField(
                value = ssid,
                onValueChange = onSsidChange,
                modifier = Modifier.fillMaxWidth(),
                enabled = !running,
                singleLine = true,
                label = { Text(stringResource(Res.string.onboarding_home_wifi)) },
                placeholder = { Text("HomeNetwork_5G") },
                supportingText = { Text(stringResource(Res.string.onboarding_home_wifi_hint)) },
            )

            OutlinedTextField(
                value = password,
                onValueChange = onPasswordChange,
                modifier = Modifier.fillMaxWidth(),
                enabled = !running,
                singleLine = true,
                label = { Text(stringResource(Res.string.onboarding_wifi_password)) },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    FilledTonalIconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = stringResource(if (passwordVisible) Res.string.onboarding_hide_password else Res.string.onboarding_show_password),
                        )
                    }
                },
            )

            TimezonePicker(selected = timezone, onSelect = onTimezoneChange, enabled = !running)

            OutlinedTextField(
                value = cameraDomain,
                onValueChange = onCameraDomainChange,
                modifier = Modifier.fillMaxWidth(),
                enabled = !running,
                singleLine = true,
                isError = cameraDomainError != null,
                label = { Text(stringResource(Res.string.onboarding_camera_domain)) },
                placeholder = { Text("rr.example.com") },
                supportingText = {
                    Text(
                        cameraDomainError ?: stringResource(Res.string.onboarding_camera_domain_hint),
                    )
                },
            )

            if (cameraDomain.isNotBlank()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .toggleable(
                            value = cameraDomainAsServer,
                            enabled = !running,
                            role = Role.Switch,
                            onValueChange = onCameraDomainAsServerChange,
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column(Modifier.weight(1f)) {
                        Text(stringResource(Res.string.onboarding_camera_as_server), style = MaterialTheme.typography.bodyLarge)
                        Text(
                            stringResource(Res.string.onboarding_camera_as_server_hint, cameraDomain.trim().trimEnd('/')),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                    Spacer(Modifier.width(12.dp))
                    Switch(checked = cameraDomainAsServer, onCheckedChange = null, enabled = !running)
                }
            }

            Button(
                onClick = onStartPairing,
                enabled = canStartPairing,
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = MaterialTheme.shapes.extraLarge,
            ) {
                if (running) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp,
                    )
                    Spacer(Modifier.width(10.dp))
                }
                Text(
                    text = stringResource(if (running) Res.string.onboarding_pairing_robot else Res.string.onboarding_start_pairing),
                    style = MaterialTheme.typography.labelLarge,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TimezonePicker(selected: String, onSelect: (String) -> Unit, enabled: Boolean) {
    var expanded by remember { mutableStateOf(false) }
    val options = remember { supportedIanaTimezones() }

    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { if (enabled) expanded = !expanded },
        ) {
            OutlinedTextField(
                value = selected,
                onValueChange = {},
                readOnly = true,
                enabled = enabled,
                singleLine = true,
                label = { Text(stringResource(Res.string.onboarding_timezone)) },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                    .fillMaxWidth(),
            )
            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                options.forEach { tz ->
                    DropdownMenuItem(
                        text = { Text(tz) },
                        onClick = { onSelect(tz); expanded = false },
                    )
                }
            }
        }
        Text(
            stringResource(Res.string.onboarding_timezone_info, countryDomainFromIana(selected), posixTzFromIana(selected)),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
private fun SetupProgressCard(step: VacuumOnboarder.Step, running: Boolean, serverStatus: String?) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
    ) {
        Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(stringResource(Res.string.onboarding_pairing_status), style = MaterialTheme.typography.titleMedium)
            Text(
                text = stepLabel(step),
                style = MaterialTheme.typography.bodyLarge,
                color = if (running) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
            )
            serverStatus?.let {
                Text(
                    text = stringResource(Res.string.onboarding_server_status, it),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun ServerVacuumCard(
    adminPassword: String,
    onAdminPasswordChange: (String) -> Unit,
    devices: List<OnboardingDevice>,
    selectedDuid: String?,
    loadingDevices: Boolean,
    enabled: Boolean,
    onLoadDevices: () -> Unit,
    onSelectDevice: (String) -> Unit,
) {
    var passwordVisible by remember { mutableStateOf(false) }
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(stringResource(Res.string.onboarding_server_vacuum), style = MaterialTheme.typography.titleLarge)
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
            Text(
                stringResource(Res.string.onboarding_server_vacuum_text),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            OutlinedTextField(
                value = adminPassword,
                onValueChange = onAdminPasswordChange,
                modifier = Modifier.fillMaxWidth(),
                enabled = enabled && !loadingDevices,
                singleLine = true,
                label = { Text(stringResource(Res.string.onboarding_admin_password)) },
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    FilledTonalIconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = stringResource(if (passwordVisible) Res.string.onboarding_hide_password else Res.string.onboarding_show_password),
                        )
                    }
                },
            )

            Button(
                onClick = onLoadDevices,
                enabled = enabled && !loadingDevices && adminPassword.isNotBlank(),
                modifier = Modifier.fillMaxWidth().height(48.dp),
                shape = MaterialTheme.shapes.extraLarge,
            ) {
                if (loadingDevices) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp,
                    )
                    Spacer(Modifier.width(10.dp))
                }
                Text(stringResource(if (loadingDevices) Res.string.onboarding_loading_vacuums else Res.string.onboarding_load_vacuums))
            }

            if (devices.isNotEmpty()) {
                val selected = devices.firstOrNull { it.duid == selectedDuid }
                ExposedDropdownMenuBox(
                    expanded = expanded,
                    onExpandedChange = { if (enabled) expanded = !expanded },
                ) {
                    OutlinedTextField(
                        value = selected?.displayName ?: stringResource(Res.string.onboarding_select_vacuum),
                        onValueChange = {},
                        readOnly = true,
                        enabled = enabled,
                        singleLine = true,
                        label = { Text(stringResource(Res.string.onboarding_vacuum_to_onboard)) },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                        modifier = Modifier
                            .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                            .fillMaxWidth(),
                    )
                    ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        devices.forEach { device ->
                            DropdownMenuItem(
                                text = {
                                    Column {
                                        Text(device.displayName)
                                        val hint = buildString {
                                            append(stringResource(if (device.connected) Res.string.onboarding_device_connected else Res.string.onboarding_device_not_connected))
                                            if (device.onboarding?.hasPublicKey == true) append(" • " + stringResource(Res.string.onboarding_device_key_ready))
                                            if (device.onboarding?.unsupported == true) append(" • " + stringResource(Res.string.onboarding_device_unsupported))
                                        }
                                        Text(
                                            hint,
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        )
                                    }
                                },
                                onClick = { onSelectDevice(device.duid); expanded = false },
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun SetupMessageCard(title: String, message: String, containerColor: Color, contentColor: Color) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = containerColor,
        contentColor = contentColor,
        shape = MaterialTheme.shapes.extraLarge,
    ) {
        Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            Text(message, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
private fun SetupLogsCard(step: VacuumOnboarder.Step, running: Boolean, error: String?, result: String?) {
    val logs = buildList {
        add("[setup] " + stringResource(Res.string.onboarding_log_ready))
        if (running) add("[setup] ${stepLabel(step)}")
        when {
            error != null -> add("[error] $error")
            result != null -> add("[success] $result")
            !running -> add("[setup] " + stringResource(Res.string.onboarding_log_enter_details))
        }
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainerLow),
    ) {
        Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(Icons.Default.Terminal, contentDescription = null)
                Text(stringResource(Res.string.onboarding_setup_logs), style = MaterialTheme.typography.titleMedium)
            }
            Surface(
                modifier = Modifier.fillMaxWidth().height(172.dp),
                color = MaterialTheme.colorScheme.surfaceContainer,
                shape = MaterialTheme.shapes.large,
            ) {
                SelectionContainer {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                    ) {
                        logs.forEach { line ->
                            Text(
                                text = line,
                                style = MaterialTheme.typography.bodySmall.copy(fontFamily = FontFamily.Monospace),
                                color = logColor(line),
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun logColor(line: String): Color = when {
    line.startsWith("[error]") -> MaterialTheme.colorScheme.error
    line.startsWith("[success]") -> MaterialTheme.colorScheme.primary
    else -> MaterialTheme.colorScheme.onSurfaceVariant
}

@Composable
private fun stepLabel(step: VacuumOnboarder.Step): String = stringResource(
    when (step) {
        VacuumOnboarder.Step.Idle -> Res.string.onboarding_step_idle
        VacuumOnboarder.Step.JoiningWifi -> Res.string.onboarding_step_joining_wifi
        VacuumOnboarder.Step.SendingHello -> Res.string.onboarding_step_sending_hello
        VacuumOnboarder.Step.AwaitingHello -> Res.string.onboarding_step_awaiting_hello
        VacuumOnboarder.Step.SendingWifiConfig -> Res.string.onboarding_step_sending_wifi
        VacuumOnboarder.Step.AwaitingAck -> Res.string.onboarding_step_awaiting_ack
        VacuumOnboarder.Step.Done -> Res.string.onboarding_step_done
    },
)
