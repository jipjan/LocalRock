package com.kodraliu.localrock.shared.vacuum

import com.kodraliu.localrock.resources.*
import com.kodraliu.localrock.shared.text.UiText
import com.kodraliu.localrock.shared.text.uiText

/** One value a dock setting can have: the code the robot uses, and what to call it. */
data class DockChoice(val code: Int, val text: UiText) {
    val label: String get() = text.english
}

object DockOptions {
    // Only 2 = Hot water is confirmed on a robot; the other codes follow the official app's order.
    val washWaterTemperature = listOf(
        DockChoice(0, uiText(Res.string.dock_temp_room, "Room temperature")),
        DockChoice(1, uiText(Res.string.dock_temp_warm, "Warm water")),
        DockChoice(2, uiText(Res.string.dock_temp_hot, "Hot water")),
    )

    /** app_set_dryer_setting's dry_time, in seconds; a robot reporting 7200 showed "2 h". */
    val dryTime = listOf(1, 2, 3, 4).map { DockChoice(it * 3600, dryTimeText(it * 3600)) }
}

/** The label for [code], or "Other (code)" so a value this app has no name for is still shown. */
fun List<DockChoice>.labelFor(code: Int): String = textFor(code).english

/** [labelFor] for the UI, in the app's language. */
fun List<DockChoice>.textFor(code: Int): UiText =
    firstOrNull { it.code == code }?.text ?: uiText(Res.string.dock_other_code, "Other ($code)", code)

/** How long a drying run lasts, from the robot's seconds: "2 h", "90 min". */
fun dryTimeLabel(seconds: Int): String = dryTimeText(seconds).english

/** [dryTimeLabel] for the UI, in the app's language. */
fun dryTimeText(seconds: Int): UiText = when {
    seconds % 3600 == 0 -> (seconds / 3600).let { uiText(Res.string.duration_hours, "$it h", it) }
    seconds % 60 == 0 -> (seconds / 60).let { uiText(Res.string.duration_minutes, "$it min", it) }
    else -> uiText(Res.string.duration_seconds, "$seconds s", seconds)
}
