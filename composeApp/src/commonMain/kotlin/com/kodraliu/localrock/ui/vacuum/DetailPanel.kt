package com.kodraliu.localrock.ui.vacuum

import androidx.compose.runtime.Composable
import com.kodraliu.localrock.resources.Res
import com.kodraliu.localrock.resources.settings_panel_cleaning_mode
import com.kodraliu.localrock.resources.settings_panel_dock
import com.kodraliu.localrock.resources.settings_panel_maps
import com.kodraliu.localrock.resources.settings_panel_more
import com.kodraliu.localrock.resources.settings_panel_order
import com.kodraliu.localrock.resources.settings_panel_rooms
import com.kodraliu.localrock.resources.settings_panel_routines
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

/** The sheet (and, on wide windows, side pane) currently shown on the robot detail screen. */
internal enum class DetailPanel { More, Routines, Rooms, Order, Dock, CleaningMode, Maps }

internal fun DetailPanel.titleRes(): StringResource = when (this) {
    DetailPanel.More -> Res.string.settings_panel_more
    DetailPanel.Routines -> Res.string.settings_panel_routines
    DetailPanel.Rooms -> Res.string.settings_panel_rooms
    DetailPanel.Order -> Res.string.settings_panel_order
    DetailPanel.Dock -> Res.string.settings_panel_dock
    DetailPanel.CleaningMode -> Res.string.settings_panel_cleaning_mode
    DetailPanel.Maps -> Res.string.settings_panel_maps
}

@Composable
internal fun DetailPanel.title(): String = stringResource(titleRes())

/** True when a panel is shown as a headed view in the wide-window side pane. `More` is the pane's home content, not a headed panel. */
internal fun sidePaneShowsPanel(panel: DetailPanel?): Boolean = panel != null && panel != DetailPanel.More
