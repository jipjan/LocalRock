package com.kodraliu.localrock.shared.vacuum

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class FeatureSectionsTest {
    private fun section(id: String) = BetaFeatureDefs.first { it.id == id }.section

    @Test
    fun features_sit_on_the_page_the_official_app_puts_them() {
        assertEquals(FeatureSection.CARPET, section("carpet_deep_clean"))
        assertEquals(FeatureSection.FLOOR, section("corner_mop_stretch"))
        assertEquals(FeatureSection.PETS, section("pet_supplies_deep_clean"))
        assertEquals(FeatureSection.PETS, section("have_pets"))
        assertEquals(FeatureSection.PETS, section("pet_snaps"))
        assertEquals(FeatureSection.OBSTACLE, section("avoid_collision"))
        assertEquals(FeatureSection.DOCK, section("dust_collection_switch"))
        assertEquals(FeatureSection.ROBOT, section("voice_control_led"))
    }

    @Test
    fun only_what_fits_nowhere_else_stays_under_beta() {
        // Furniture recognition moved to obstacles, where the official app has it (AI recognition).
        val beta = BetaFeatureDefs.filter { it.section == FeatureSection.BETA }.map { it.id }
        assertEquals(emptyList(), beta)
        assertEquals(FeatureSection.OBSTACLE, section("identify_furniture"))
    }

    @Test
    fun a_page_lists_only_its_own_features() {
        val states = BetaFeatureDefs.map { BetaFeatureState(it, value = 1) }
        val floor = states.inSection(FeatureSection.FLOOR)
        assertTrue(floor.isNotEmpty())
        assertTrue(floor.all { it.def.section == FeatureSection.FLOOR })
    }
}
