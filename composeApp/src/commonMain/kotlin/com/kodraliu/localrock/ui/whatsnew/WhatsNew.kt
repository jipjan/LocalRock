package com.kodraliu.localrock.ui.whatsnew

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PinDrop
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Reorder
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.kodraliu.localrock.resources.Res
import com.kodraliu.localrock.resources.whatsnew_120_editor_body
import com.kodraliu.localrock.resources.whatsnew_120_editor_title
import com.kodraliu.localrock.resources.whatsnew_120_fixes_body
import com.kodraliu.localrock.resources.whatsnew_120_maps_body
import com.kodraliu.localrock.resources.whatsnew_120_maps_title
import com.kodraliu.localrock.resources.whatsnew_120_multi_level_body
import com.kodraliu.localrock.resources.whatsnew_120_multi_level_title
import com.kodraliu.localrock.resources.whatsnew_130_alerts_body
import com.kodraliu.localrock.resources.whatsnew_130_alerts_title
import com.kodraliu.localrock.resources.whatsnew_130_fixes_body
import com.kodraliu.localrock.resources.whatsnew_130_order_body
import com.kodraliu.localrock.resources.whatsnew_130_order_title
import com.kodraliu.localrock.resources.whatsnew_130_per_room_body
import com.kodraliu.localrock.resources.whatsnew_130_per_room_title
import com.kodraliu.localrock.resources.whatsnew_130_smartplan_body
import com.kodraliu.localrock.resources.whatsnew_130_smartplan_title
import com.kodraliu.localrock.resources.whatsnew_140_beta_body
import com.kodraliu.localrock.resources.whatsnew_140_beta_title
import com.kodraliu.localrock.resources.whatsnew_140_fixes_body
import com.kodraliu.localrock.resources.whatsnew_140_room_types_body
import com.kodraliu.localrock.resources.whatsnew_140_room_types_title
import com.kodraliu.localrock.resources.whatsnew_140_routines_body
import com.kodraliu.localrock.resources.whatsnew_140_routines_title
import com.kodraliu.localrock.resources.whatsnew_140_schedules_body
import com.kodraliu.localrock.resources.whatsnew_140_schedules_title
import com.kodraliu.localrock.resources.whatsnew_140_zone_body
import com.kodraliu.localrock.resources.whatsnew_140_zone_title
import com.kodraliu.localrock.resources.whatsnew_fixes
import com.kodraliu.localrock.resources.whatsnew_got_it
import com.kodraliu.localrock.resources.whatsnew_title
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

/*
 * In-app release notes. They ship inside the app, so nothing is fetched to show them. Add an
 * entry for each release that has something worth telling users; a version without one simply
 * shows nothing after the update.
 */

data class WhatsNewItem(val icon: ImageVector, val title: StringResource, val body: StringResource)

data class WhatsNewRelease(val version: String, val items: List<WhatsNewItem>)

/** Newest first. */
val WHATS_NEW: List<WhatsNewRelease> = listOf(
    WhatsNewRelease(
        version = "1.4.0",
        items = listOf(
            WhatsNewItem(Icons.Default.PlayArrow, Res.string.whatsnew_140_routines_title, Res.string.whatsnew_140_routines_body),
            WhatsNewItem(Icons.Default.PinDrop, Res.string.whatsnew_140_zone_title, Res.string.whatsnew_140_zone_body),
            WhatsNewItem(Icons.Default.Home, Res.string.whatsnew_140_room_types_title, Res.string.whatsnew_140_room_types_body),
            WhatsNewItem(Icons.Default.Schedule, Res.string.whatsnew_140_schedules_title, Res.string.whatsnew_140_schedules_body),
            WhatsNewItem(Icons.Default.Science, Res.string.whatsnew_140_beta_title, Res.string.whatsnew_140_beta_body),
            WhatsNewItem(Icons.Default.Build, Res.string.whatsnew_fixes, Res.string.whatsnew_140_fixes_body),
        ),
    ),
    WhatsNewRelease(
        version = "1.3.0",
        items = listOf(
            WhatsNewItem(Icons.Default.Tune, Res.string.whatsnew_130_per_room_title, Res.string.whatsnew_130_per_room_body),
            WhatsNewItem(Icons.Default.Reorder, Res.string.whatsnew_130_order_title, Res.string.whatsnew_130_order_body),
            WhatsNewItem(Icons.Default.AutoAwesome, Res.string.whatsnew_130_smartplan_title, Res.string.whatsnew_130_smartplan_body),
            WhatsNewItem(Icons.Default.Notifications, Res.string.whatsnew_130_alerts_title, Res.string.whatsnew_130_alerts_body),
            WhatsNewItem(Icons.Default.Build, Res.string.whatsnew_fixes, Res.string.whatsnew_130_fixes_body),
        ),
    ),
    WhatsNewRelease(
        version = "1.2.0",
        items = listOf(
            WhatsNewItem(Icons.Default.Edit, Res.string.whatsnew_120_editor_title, Res.string.whatsnew_120_editor_body),
            WhatsNewItem(Icons.Default.Map, Res.string.whatsnew_120_maps_title, Res.string.whatsnew_120_maps_body),
            WhatsNewItem(Icons.Default.Layers, Res.string.whatsnew_120_multi_level_title, Res.string.whatsnew_120_multi_level_body),
            WhatsNewItem(Icons.Default.Build, Res.string.whatsnew_fixes, Res.string.whatsnew_120_fixes_body),
        ),
    ),
)

fun whatsNewFor(version: String): WhatsNewRelease? = WHATS_NEW.find { it.version == version }

/**
 * Show once after an update. Fresh installs never see it: the welcome screen marks the current
 * version as seen, so only someone who used an earlier version gets it.
 */
fun shouldShowWhatsNew(introSeen: Boolean, lastSeenVersion: String?, currentVersion: String): Boolean =
    introSeen && lastSeenVersion != currentVersion && whatsNewFor(currentVersion) != null

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WhatsNewSheet(release: WhatsNewRelease, onDismiss: () -> Unit) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .navigationBarsPadding()
                .padding(horizontal = 24.dp)
                .padding(bottom = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(stringResource(Res.string.whatsnew_title), style = MaterialTheme.typography.headlineSmall)
                Text(
                    "LocalRock ${release.version}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
            release.items.forEach { item ->
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp), verticalAlignment = Alignment.Top) {
                    Surface(shape = CircleShape, color = MaterialTheme.colorScheme.primaryContainer, modifier = Modifier.size(40.dp)) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                item.icon,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                modifier = Modifier.size(20.dp),
                            )
                        }
                    }
                    Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(2.dp)) {
                        Text(stringResource(item.title), style = MaterialTheme.typography.titleSmall)
                        Text(
                            stringResource(item.body),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
            Button(onClick = onDismiss, modifier = Modifier.fillMaxWidth()) { Text(stringResource(Res.string.whatsnew_got_it)) }
        }
    }
}
