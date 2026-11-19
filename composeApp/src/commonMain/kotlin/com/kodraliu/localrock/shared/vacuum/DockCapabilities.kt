package com.kodraliu.localrock.shared.vacuum

import com.kodraliu.localrock.resources.*
import com.kodraliu.localrock.shared.text.UiText
import com.kodraliu.localrock.shared.text.rawText
import com.kodraliu.localrock.shared.text.uiText

data class WashFrequencyOption(val label: String, val mode: Int, val intervalMinutes: Int? = null) {
    /** [label] for the UI, in the app's language. */
    val text: UiText
        get() = when {
            mode == 0 && intervalMinutes != null -> uiText(Res.string.duration_minutes, label, intervalMinutes)
            mode == 1 -> uiText(Res.string.dock_wash_by_room, label)
            mode == 2 -> uiText(Res.string.mode_smart, label)
            else -> rawText(label)
        }
}

private val smart get() = uiText(Res.string.mode_smart, "Smart")
private val light get() = uiText(Res.string.mode_light, "Light")
private val balanced get() = uiText(Res.string.mode_balanced, "Balanced")
private val deep get() = uiText(Res.string.mode_deep, "Deep")
private val strong get() = uiText(Res.string.mode_strong, "Strong")
private val max get() = uiText(Res.string.mode_max, "Max")
private val unavailable get() = uiText(Res.string.mode_unavailable, "Unavailable")
private fun unknown(mode: Int) = uiText(Res.string.mode_unknown, "Unknown ($mode)", mode)

// Official plugin: FM.Dock.isOldWashFrequency, DockConfigs and getWashModes.
private val legacyWashModels = setOf(
    "a26", "a27", "a29", "a30", "a46", "a47", "a62", "a64", "a65", "a66",
    "a68", "a69", "a70", "a76",
)
private val simpleWashDockTypes = setOf(41, 42, 43, 46)
// a187 plugin DockConfigs, lines 281000–281788; charging and collection-only types excluded.
private val knownWashDockTypes = setOf(2, 3) + (6..11) + (13..24) + (26..35) + (37..46)

class DockCapabilities(model: String?, featureSet: String?, newFeatureSet: String?) {
    private val smartWash = newFeatureBit(newFeatureSet, 34) || newFeatureBit(newFeatureSet, 150)
    private val superDeepWash = newFeatureBit(newFeatureSet, 15)
    private val soakWash = newFeatureBit(newFeatureSet, 107)
    private val aiWash = newFeatureBit(newFeatureSet, 144)

    /** Bit 83 (DryIntervalTimer): drying can start at a set time each day, not only after cleaning. */
    val dryerSchedule = newFeatureBit(newFeatureSet, 83)

    val washIntervals: List<Int> = if (model?.substringAfterLast('.') in legacyWashModels) {
        (10..50 step 5).toList()
    } else {
        listOf(10, 15, 20, 25)
    }

    val washFrequencyOptions: List<WashFrequencyOption> = buildList {
        washIntervals.forEach { add(WashFrequencyOption("$it min", 0, it)) }
        add(WashFrequencyOption("By room", 1))
        if (newFeatureBit(newFeatureSet, 109)) add(WashFrequencyOption("Smart", 2))
    }

    // Dust mode support is bit 25 of decimal featureSet, not the hex newFeatureSet.
    val autoEmptyModes: List<Pair<Int, UiText>> = if (
        (featureSet?.toULongOrNull() ?: 0uL) and (1uL shl 25) != 0uL
    ) {
        listOf(0 to smart, 1 to light, 2 to balanced, 4 to max)
    } else {
        emptyList()
    }

    private val superDeepText get() = if (soakWash) uiText(Res.string.mode_soak, "Soak") else uiText(Res.string.mode_super_deep, "Super Deep")

    fun washModes(dockType: Int?, dss: Int?): List<Pair<Int, UiText>> {
        if (dockType !in knownWashDockTypes) return listOf(0 to light)
        val simple = dockType in simpleWashDockTypes
        return buildList {
            // With AI washing (bit 144) the official app sets "smart" through smart_wash_status, not wash_mode 10
            // (captured on an a296); LocalRock offers that as its own switch.
            if (smartWash && !aiWash) add(10 to smart)
            add(0 to light)
            if (!simple || aiWash) add(1 to balanced)
            if (!simple) add(2 to deep)
            // dss low bits report the plumbing module; a dock type alone cannot establish it.
            if (superDeepWash && (soakWash || (dss?.and(3) == 2 && !smartWash))) {
                add(8 to superDeepText)
            }
        }
    }

    fun washModeCode(raw: Int?, dockType: Int?): Int? = when {
        dockType !in simpleWashDockTypes -> raw
        raw == 2 -> 8
        raw == 1 && !aiWash -> 10
        else -> raw
    }

    fun washModeLabel(raw: Int?, dockType: Int?): String = washModeText(raw, dockType).english

    /** [washModeLabel] for the UI, in the app's language. */
    fun washModeText(raw: Int?, dockType: Int?): UiText = when (val mode = washModeCode(raw, dockType)) {
        null -> unavailable
        0 -> light
        1 -> balanced
        2 -> deep
        8 -> superDeepText
        10 -> smart
        else -> unknown(mode)
    }

    fun autoEmptyModeLabel(mode: Int?): String = autoEmptyModeText(mode).english

    /** [autoEmptyModeLabel] for the UI, in the app's language. */
    fun autoEmptyModeText(mode: Int?): UiText = when (mode) {
        null -> unavailable
        0 -> smart
        1 -> light
        2 -> balanced
        3 -> strong
        4 -> max
        else -> unknown(mode)
    }
}
