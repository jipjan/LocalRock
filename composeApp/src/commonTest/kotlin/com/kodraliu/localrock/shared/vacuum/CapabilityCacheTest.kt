package com.kodraliu.localrock.shared.vacuum

import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlin.test.Test
import kotlin.test.assertEquals

class CapabilityCacheTest {
    private val a296 = "0000020008199518CED77EFDA8E9BEDDDBFF8F7F7EFEFFFF"
    private val defs = supportedBetaFeatures(a296)
    private fun def(id: String) = defs.first { it.id == id }

    @Test
    fun optionsComeBackForTheSameDefinitions() {
        val states = listOf(
            BetaFeatureState(def("clean_direction"), 2, "status"),
            BetaFeatureState(def("dirty_replenish"), 1, null),
        )
        val restored = CapabilityCache(features = states.toCache()).featureStates(defs)!!
        assertEquals(states.map { it.def.id to it.value }, restored.map { it.def.id to it.value })
        assertEquals("status", restored[0].key)
    }

    @Test
    fun optionsThisRobotIsNotOfferedAreDropped() {
        val cache = CapabilityCache(features = listOf(CachedFeature("no_such_option", 1), CachedFeature("clean_direction", 9)))
        // An unknown id, and a value the picker can't show, are both left out.
        assertEquals(emptyList(), cache.featureStates(defs))
    }

    @Test
    fun dockSettingsSurviveTheRoundTrip() {
        val dock = DockSettings(
            washMode = 1, smartWash = SmartWashSettings(0, 900), autoEmptyMode = 2, mopDrying = true,
            dryTimeSeconds = 10800, dryerAnswer = JsonObject(mapOf("status" to JsonPrimitive(1))),
            dryStart = 2, dryScheduleHour = 17, dryScheduleMinute = 0, washWaterTemperature = 1,
        )
        assertEquals(dock, dock.toCache().toSettings())
    }

    @Test
    fun theWholeCacheIsPlainJson() {
        val cache = CapabilityCache(a296, listOf(CachedFeature("clean_direction", 2, "status")), DockSettings(washMode = 1).toCache())
        val json = Json { ignoreUnknownKeys = true; encodeDefaults = false }
        val text = json.encodeToString(CapabilityCache.serializer(), cache)
        assertEquals(cache, json.decodeFromString(CapabilityCache.serializer(), text))
    }
}
