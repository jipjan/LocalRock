package com.kodraliu.localrock.ui.settings

import com.kodraliu.localrock.ui.exceptBottom
import com.kodraliu.localrock.ui.adaptive.CenteredContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.semantics.Role
import androidx.compose.foundation.selection.toggleable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.kodraliu.localrock.shared.notify.backgroundAlertsSupported
import com.kodraliu.localrock.shared.settings.ThemeMode
import com.kodraliu.localrock.ui.LocalAppContainer
import com.kodraliu.localrock.ui.rememberNotificationPermissionRequest
import com.kodraliu.localrock.ui.whatsnew.WhatsNewSheet
import com.kodraliu.localrock.ui.whatsnew.whatsNewFor
import com.kodraliu.localrock.resources.Res
import com.kodraliu.localrock.resources.appsettings_about
import com.kodraliu.localrock.resources.appsettings_about_text
import com.kodraliu.localrock.resources.appsettings_ack_intro
import com.kodraliu.localrock.resources.appsettings_ack_outro
import com.kodraliu.localrock.resources.appsettings_alerts_note_android
import com.kodraliu.localrock.resources.appsettings_alerts_note_ios
import com.kodraliu.localrock.resources.appsettings_appearance
import com.kodraliu.localrock.resources.appsettings_built_for
import com.kodraliu.localrock.resources.appsettings_connection
import com.kodraliu.localrock.resources.appsettings_notifications
import com.kodraliu.localrock.resources.appsettings_notifications_denied
import com.kodraliu.localrock.resources.appsettings_notify_background
import com.kodraliu.localrock.resources.appsettings_notify_background_text
import com.kodraliu.localrock.resources.appsettings_notify_dock
import com.kodraliu.localrock.resources.appsettings_notify_dock_text
import com.kodraliu.localrock.resources.appsettings_notify_errors
import com.kodraliu.localrock.resources.appsettings_notify_finished
import com.kodraliu.localrock.resources.appsettings_notify_progress
import com.kodraliu.localrock.resources.appsettings_notify_progress_text
import com.kodraliu.localrock.resources.appsettings_privacy
import com.kodraliu.localrock.resources.appsettings_robot_alerts
import com.kodraliu.localrock.resources.appsettings_robot_alerts_text
import com.kodraliu.localrock.resources.appsettings_room_outlines
import com.kodraliu.localrock.resources.appsettings_room_outlines_text
import com.kodraliu.localrock.resources.appsettings_server_url
import com.kodraliu.localrock.resources.appsettings_server_url_text
import com.kodraliu.localrock.resources.appsettings_thanks
import com.kodraliu.localrock.resources.appsettings_theme_dark
import com.kodraliu.localrock.resources.appsettings_theme_light
import com.kodraliu.localrock.resources.appsettings_theme_system
import com.kodraliu.localrock.resources.appsettings_title
import com.kodraliu.localrock.resources.appsettings_whats_new
import com.kodraliu.localrock.resources.common_cancel
import com.kodraliu.localrock.resources.common_save
import com.kodraliu.localrock.resources.login_demo
import com.kodraliu.localrock.resources.onboarding_back
import org.jetbrains.compose.resources.stringResource

const val APP_VERSION: String = "1.4.0"


private const val PRIVACY_URL = "https://donsidro.github.io/LocalRock/"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(onDone: () -> Unit, allowCancel: Boolean) {
    val container = LocalAppContainer.current
    val uriHandler = LocalUriHandler.current
    val savedUrl by container.appSettings.serverBaseUrl.collectAsState()
    val themeMode by container.appSettings.themeMode.collectAsState()
    val userData by container.authRepository.userData.collectAsState()

    var url by remember { mutableStateOf(savedUrl ?: "") }
    var showWhatsNew by remember { mutableStateOf(false) }
    val whatsNew = whatsNewFor(APP_VERSION)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(Res.string.appsettings_title)) },
                navigationIcon = {
                    if (allowCancel) {
                        IconButton(onClick = onDone) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(Res.string.onboarding_back))
                        }
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
                    .padding(horizontal = 24.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                SectionLabel(stringResource(Res.string.appsettings_appearance))
                val themeModes = listOf(
                    ThemeMode.SYSTEM to stringResource(Res.string.appsettings_theme_system),
                    ThemeMode.LIGHT to stringResource(Res.string.appsettings_theme_light),
                    ThemeMode.DARK to stringResource(Res.string.appsettings_theme_dark),
                )
                SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                    themeModes.forEachIndexed { index, (mode, label) ->
                        SegmentedButton(
                            selected = themeMode == mode,
                            onClick = { container.appSettings.setThemeMode(mode) },
                            shape = SegmentedButtonDefaults.itemShape(index = index, count = themeModes.size),
                        ) {
                            Text(label)
                        }
                    }
                }
                val roomOutlines by container.appSettings.mapRoomOutlines.collectAsState()
                SwitchRow(
                    stringResource(Res.string.appsettings_room_outlines),
                    stringResource(Res.string.appsettings_room_outlines_text),
                    roomOutlines,
                    container.appSettings::setMapRoomOutlines,
                )

                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

                SectionLabel(stringResource(Res.string.appsettings_connection))
                OutlinedTextField(
                    value = url,
                    onValueChange = { url = it },
                    label = { Text(stringResource(Res.string.appsettings_server_url)) },
                    placeholder = { Text("https://api-test.example.com") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
                Text(
                    stringResource(Res.string.appsettings_server_url_text),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )

                Spacer(Modifier.height(4.dp))
                Button(
                    enabled = url.isNotBlank(),
                    onClick = {
                        container.appSettings.setServerBaseUrl(url.trim())
                        onDone()
                    },
                    modifier = Modifier.fillMaxWidth(),
                ) { Text(stringResource(Res.string.common_save)) }
                if (allowCancel) {
                    TextButton(onClick = onDone, modifier = Modifier.fillMaxWidth()) { Text(stringResource(Res.string.common_cancel)) }
                }
                if (userData == null) {
                    TextButton(
                        onClick = { container.authRepository.enterDemo() },
                        modifier = Modifier.fillMaxWidth(),
                    ) { Text(stringResource(Res.string.login_demo)) }
                }

                // Alerts are about robots, and there are none to watch before signing in.
                if (userData != null) {
                    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))
                    NotificationSettings()
                }

                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

                SectionLabel(stringResource(Res.string.appsettings_about))
                Text(
                    "LocalRock $APP_VERSION",
                    style = MaterialTheme.typography.bodyMedium,
                )
                Text(
                    stringResource(Res.string.appsettings_about_text),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                if (whatsNew != null) {
                    OutlinedButton(
                        onClick = { showWhatsNew = true },
                        modifier = Modifier.fillMaxWidth(),
                    ) { Text(stringResource(Res.string.appsettings_whats_new, APP_VERSION)) }
                }
                OutlinedButton(
                    onClick = { uriHandler.openUri(PRIVACY_URL) },
                    modifier = Modifier.fillMaxWidth(),
                ) { Text(stringResource(Res.string.appsettings_privacy)) }

                Spacer(Modifier.height(4.dp))
                Text(
                    stringResource(Res.string.appsettings_thanks),
                    style = MaterialTheme.typography.labelLarge,
                )
                Text(
                    stringResource(Res.string.appsettings_ack_intro) + "\n\n" + ACKNOWLEDGED_PROJECTS + "\n\n" +
                        stringResource(Res.string.appsettings_ack_outro),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    stringResource(Res.string.appsettings_built_for),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(Modifier.height(16.dp))
            }
        }
    }

    if (showWhatsNew && whatsNew != null) {
        WhatsNewSheet(release = whatsNew, onDismiss = { showWhatsNew = false })
    }
}

@Composable
private fun NotificationSettings() {
    val container = LocalAppContainer.current
    val settings = container.appSettings
    val enabled by settings.notifyEnabled.collectAsState()
    val errors by settings.notifyErrors.collectAsState()
    val finished by settings.notifyFinished.collectAsState()
    val dock by settings.notifyDock.collectAsState()
    val background by settings.notifyBackground.collectAsState()
    val progress by settings.notifyProgress.collectAsState()
    val deniedText = stringResource(Res.string.appsettings_notifications_denied)
    val requestPermission = rememberNotificationPermissionRequest { granted ->
        if (granted) {
            settings.setNotifyEnabled(true)
        } else {
            container.messages.warn(deniedText)
        }
    }

    SectionLabel(stringResource(Res.string.appsettings_notifications))
    SwitchRow(
        title = stringResource(Res.string.appsettings_robot_alerts),
        description = stringResource(Res.string.appsettings_robot_alerts_text),
        checked = enabled,
        onCheckedChange = { on -> if (on) requestPermission() else settings.setNotifyEnabled(false) },
    )
    if (!enabled) return
    SwitchRow(stringResource(Res.string.appsettings_notify_errors), null, errors, settings::setNotifyErrors)
    SwitchRow(stringResource(Res.string.appsettings_notify_finished), null, finished, settings::setNotifyFinished)
    SwitchRow(
        title = stringResource(Res.string.appsettings_notify_dock),
        description = stringResource(Res.string.appsettings_notify_dock_text),
        checked = dock,
        onCheckedChange = settings::setNotifyDock,
    )
    if (backgroundAlertsSupported) {
        SwitchRow(
            title = stringResource(Res.string.appsettings_notify_background),
            description = stringResource(Res.string.appsettings_notify_background_text),
            checked = background,
            onCheckedChange = settings::setNotifyBackground,
        )
        // Progress needs the background watch: without it nothing keeps the notification moving.
        if (background) {
            SwitchRow(
                title = stringResource(Res.string.appsettings_notify_progress),
                description = stringResource(Res.string.appsettings_notify_progress_text),
                checked = progress,
                onCheckedChange = settings::setNotifyProgress,
            )
        }
    }
    Text(
        stringResource(
            if (backgroundAlertsSupported) Res.string.appsettings_alerts_note_android
            else Res.string.appsettings_alerts_note_ios,
        ),
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant,
    )
}

@Composable
private fun SwitchRow(title: String, description: String?, checked: Boolean, onCheckedChange: (Boolean) -> Unit) {
    // The whole row toggles, so the text is a target too, and it reads as one switch to TalkBack.
    Row(
        modifier = Modifier.fillMaxWidth().toggleable(value = checked, role = Role.Switch, onValueChange = onCheckedChange),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
            Text(title, style = MaterialTheme.typography.bodyLarge)
            if (description != null) {
                Text(
                    description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        Switch(checked = checked, onCheckedChange = null)
    }
}

@Composable
private fun SectionLabel(text: String) {
    Text(
        text,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.primary,
    )
}

/** Project names and licences stay as their authors wrote them; the text around them is translated. */
private const val ACKNOWLEDGED_PROJECTS =
    "• Kotlin Multiplatform & Compose Multiplatform (Apache-2.0) — JetBrains\n" +
        "• Ktor client (Apache-2.0) — JetBrains\n" +
        "• kotlinx.serialization / coroutines (Apache-2.0) — JetBrains\n" +
        "• KMQTT (MIT) — Davide Pianca\n" +
        "• CocoaMQTT (MIT) — the emqx team\n" +
        "• stream-webrtc-android (Apache-2.0) — GetStream / the WebRTC project\n" +
        "• multiplatform-settings (Apache-2.0) — Russell Wolf\n" +
        "• KotlinCrypto hash & macs (Apache-2.0)\n" +
        "• Okio (Apache-2.0) — Square"
