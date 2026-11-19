package com.kodraliu.localrock.ui.intro

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Dns
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.filled.VpnKey
import androidx.compose.material.icons.filled.Wifi
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.kodraliu.localrock.ui.LocalAppContainer
import com.kodraliu.localrock.ui.adaptive.CenteredContent
import com.kodraliu.localrock.ui.settings.APP_VERSION
import com.kodraliu.localrock.resources.Res
import com.kodraliu.localrock.resources.intro_credentials_body
import com.kodraliu.localrock.resources.intro_credentials_title
import com.kodraliu.localrock.resources.intro_disclaimer
import com.kodraliu.localrock.resources.intro_get_started
import com.kodraliu.localrock.resources.intro_headline
import com.kodraliu.localrock.resources.intro_lead
import com.kodraliu.localrock.resources.intro_network_body
import com.kodraliu.localrock.resources.intro_network_title
import com.kodraliu.localrock.resources.intro_server_body
import com.kodraliu.localrock.resources.intro_server_title
import com.kodraliu.localrock.resources.intro_setup_guide
import org.jetbrains.compose.resources.stringResource

private const val PROJECT_URL = "https://github.com/Python-roborock/local_roborock_server"


@Composable
fun WelcomeScreen() {
    val container = LocalAppContainer.current
    val uriHandler = LocalUriHandler.current

    CenteredContent {
        Column(modifier = Modifier.fillMaxSize().safeDrawingPadding()) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Spacer(Modifier.height(24.dp))
                Text(
                    stringResource(Res.string.intro_headline),
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    stringResource(Res.string.intro_lead),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )

                Spacer(Modifier.height(4.dp))
                Point(
                    Icons.Default.Dns,
                    stringResource(Res.string.intro_server_title),
                    stringResource(Res.string.intro_server_body),
                )
                Point(
                    Icons.Default.VpnKey,
                    stringResource(Res.string.intro_credentials_title),
                    stringResource(Res.string.intro_credentials_body),
                )
                Point(
                    Icons.Default.Wifi,
                    stringResource(Res.string.intro_network_title),
                    stringResource(Res.string.intro_network_body),
                )

                TextButton(
                    onClick = { uriHandler.openUri(PROJECT_URL) },
                    modifier = Modifier.padding(start = 4.dp),
                ) {
                    Text(stringResource(Res.string.intro_setup_guide))
                    Spacer(Modifier.width(6.dp))
                    Icon(Icons.AutoMirrored.Filled.OpenInNew, contentDescription = null, modifier = Modifier.size(16.dp))
                }

                Text(
                    stringResource(Res.string.intro_disclaimer),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Spacer(Modifier.height(8.dp))
            }

            // The way forward stays put at the bottom instead of drifting with the scroll.
            Button(
                onClick = {
                    // A new install starts on the current version, so it gets no What's new.
                    container.appSettings.setLastSeenVersion(APP_VERSION)
                    container.appSettings.setIntroSeen(true)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
                    .padding(top = 8.dp, bottom = 24.dp)
                    .height(52.dp),
            ) {
                Text(stringResource(Res.string.intro_get_started))
            }
        }
    }
}

@Composable
private fun Point(icon: ImageVector, title: String, body: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.Top,
    ) {
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primaryContainer,
            modifier = Modifier.size(36.dp),
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    icon,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                )
            }
        }
        Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
            Text(title, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.SemiBold)
            Text(
                body,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
        }
    }
}
