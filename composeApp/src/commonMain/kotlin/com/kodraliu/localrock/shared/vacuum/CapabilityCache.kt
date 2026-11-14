package com.kodraliu.localrock.shared.vacuum

import com.russhwolf.settings.Settings
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject

/**
 * What a vacuum last reported about itself, kept on the phone so the settings pages can show at once
 * instead of filling in while the robot is asked, the way the official app keeps a status cache
 * (`isSupportStatusCache`, `DockStatusCache`). Every page still reads the robot again when it opens;
 * the cache only fills the gap until it has answered.
 */
@Serializable
data class CapabilityCache(
    /** The robot's own `new_feature_info_str`. */
    val featureSet: String? = null,
    /** Every option the robot answered with a value the pages can show. */
    val features: List<CachedFeature>? = null,
    val dock: CachedDock? = null,
)

@Serializable
data class CachedFeature(val id: String, val value: Int, val key: String? = null)

@Serializable
data class CachedDock(
    val washMode: Int? = null,
    val smartWashMode: Int? = null,
    val smartWashIntervalSeconds: Int? = null,
    val autoEmptyMode: Int? = null,
    val mopDrying: Boolean? = null,
    val dryTimeSeconds: Int? = null,
    /** No longer written: the dryer setters send this body back, so only a fresh read may supply it. */
    val dryerAnswer: JsonObject? = null,
    val dryStart: Int? = null,
    val dryScheduleHour: Int? = null,
    val dryScheduleMinute: Int? = null,
    val washWaterTemperature: Int? = null,
)

internal fun DockSettings.toCache() = CachedDock(
    washMode, smartWash?.mode, smartWash?.intervalSeconds, autoEmptyMode, mopDrying, dryTimeSeconds,
    null, dryStart, dryScheduleHour, dryScheduleMinute, washWaterTemperature,
)

internal fun CachedDock.toSettings() = DockSettings(
    washMode = washMode,
    smartWash = if (smartWashMode != null && smartWashIntervalSeconds != null) SmartWashSettings(smartWashMode, smartWashIntervalSeconds) else null,
    autoEmptyMode = autoEmptyMode,
    mopDrying = mopDrying,
    dryTimeSeconds = dryTimeSeconds,
    dryStart = dryStart,
    dryScheduleHour = dryScheduleHour,
    dryScheduleMinute = dryScheduleMinute,
    washWaterTemperature = washWaterTemperature,
)

/** The cached options as page states, for the definitions this robot is offered; unknown ids are dropped. */
internal fun CapabilityCache.featureStates(defs: List<BetaFeatureDef>): List<BetaFeatureState>? {
    val byId = defs.associateBy { it.id }
    return features?.mapNotNull { f -> byId[f.id]?.let { BetaFeatureState(it, f.value, f.key) } }
        ?.filter { it.toggleable }
}

internal fun List<BetaFeatureState>.toCache(): List<CachedFeature> =
    mapNotNull { s -> s.value?.let { CachedFeature(s.def.id, it, s.key) } }

/** One [CapabilityCache] per vacuum, as JSON in the app's settings. A cache that no longer parses is ignored. */
class CapabilityCacheStore(private val settings: Settings) {
    private val json = Json { ignoreUnknownKeys = true; encodeDefaults = false }

    fun load(duid: String): CapabilityCache? =
        settings.getStringOrNull(key(duid))?.let { runCatching { json.decodeFromString(CapabilityCache.serializer(), it) }.getOrNull() }

    fun save(duid: String, cache: CapabilityCache) =
        settings.putString(key(duid), json.encodeToString(CapabilityCache.serializer(), cache))

    private fun key(duid: String) = "capability_cache_$duid"
}
