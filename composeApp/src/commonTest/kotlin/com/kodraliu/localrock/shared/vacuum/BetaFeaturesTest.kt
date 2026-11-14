package com.kodraliu.localrock.shared.vacuum

import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue
import kotlin.test.assertNull

class BetaFeaturesTest {

    @Test
    fun supported_features_follow_the_feature_bits() {
        // QX Revo Ultra 2 newFeatureSet: bits 36, 40, 43, 54, 56, 60 and 63 are on, 58 is off.
        val set = "0000000000119418CCD57EFDA869B6DDDBFF8F7F7EFEFFFF"
        val original = setOf(
            "avoid_collision", "collision_mode", "corner_mop_stretch", "dirty_object_detect", "gap_deep_clean",
            "pet_supplies_deep_clean", "right_brush_stretch", "water_leak_check",
        )
        assertEquals(
            listOf(
                "collision_mode", "corner_mop_stretch", "dirty_object_detect", "gap_deep_clean",
                "pet_supplies_deep_clean", "right_brush_stretch", "water_leak_check",
            ),
            supportedBetaFeatures(set).filter { it.id in original }.map { it.id }.sorted(),
        )
        assertEquals(emptyList(), supportedBetaFeatures("0").filter { it.bit != null })
    }

    @Test
    fun carpet_deep_clean_is_bit_3_and_cleaning_fluid_is_bit_112() {
        val carpet = "8"
        val fluid = "1" + "0".repeat(28)
        assertEquals(listOf("carpet_deep_clean"), supportedBetaFeatures(carpet).filter { it.bit != null }.map { it.id })
        assertEquals(listOf("clean_fluid_delivery"), supportedBetaFeatures(fluid).filter { it.bit != null }.map { it.id })
        assertEquals(emptyList(), supportedBetaFeatures("6" + "0".repeat(28)).filter { it.bit != null })
    }

    @Test
    fun features_without_a_known_bit_are_always_probed() {
        assertEquals(
            listOf(
                "carpet_boost", "dust_collection_switch", "fill_light", "follow_ground_material", "have_pets",
                "identify_furniture", "identify_ground_material", "offline_map", "priority_carpet", "sway_arm_door_sill",
            ),
            supportedBetaFeatures(null).map { it.id }.sorted(),
        )
    }

    @Test
    fun collision_avoidance_is_a_mode_picker_on_robots_with_bit_118() {
        // An a296 (Saros 20 Neo) has bit 118; the official app offers 1 Large coverage / 2 Less collision.
        val a296 = "0000000000199518CED77EFDA8E9BEDDDBFF8F7F7EFEFFFF"
        val ids = supportedBetaFeatures(a296).map { it.id }
        assertTrue("collision_mode" in ids)
        assertFalse("avoid_collision" in ids)
        val mode = BetaFeatureDefs.first { it.id == "collision_mode" }
        assertEquals(listOf(1, 2), mode.choices?.map { it.first })
        // Without bit 118 (only bit 36) the old on/off switch stays.
        val bit36Only = "1" + "0".repeat(9)
        assertEquals(listOf("avoid_collision"), supportedBetaFeatures(bit36Only).filter { it.bit == 36 }.map { it.id })
    }

    @Test
    fun the_new_a296_features_are_offered_on_an_a296() {
        val a296 = "0000000000199518CED77EFDA8E9BEDDDBFF8F7F7EFEFFFF"
        val ids = supportedBetaFeatures(a296).map { it.id }.toSet()
        for (id in listOf(
            "dirty_replenish", "clean_direction", "low_area_access", "lds_lifting", "remove_mop",
            "dynamic_obstacle_replenish", "omission_reminder", "cross_carpet", "long_haired_carpet",
            "carpet_wire", "identify_room", "smart_wash",
        )) assertTrue(id in ids, id)
    }

    @Test
    fun robot_settings_are_read_and_written_by_name() {
        val def = BetaFeatureDefs.first { it.id == "clean_direction" }
        assertEquals("app_get_robot_setting", def.getMethod)
        assertEquals("""{"name":"clean_direction_status"}""", featureReadParams(def).toString())
        assertEquals("status" to 2, readFeatureReply(def, Json.parseToJsonElement("""{"status":2}""")))
        // Exactly as captured from the official app.
        assertEquals(
            listOf("""{"name":"clean_direction_status","status":2}"""),
            featureSetParamCandidates(def, "status", 2).map { it.toString() },
        )
    }

    @Test
    fun status_bit_features_read_one_bit_of_the_option_bitmask() {
        val def = BetaFeatureDefs.first { it.id == "dirty_replenish" }
        assertEquals("get_offline_map_status", def.getMethod)
        // 24 = bits 3 and 4: carpets first and re-mop on.
        assertEquals("status" to 1, readFeatureReply(def, Json.parseToJsonElement("""{"common_switch_status":24}""")))
        // An a296 with carpets first on and re-mop off answered 10.
        val a296 = Json.parseToJsonElement("""{"common_switch_status":10}""")
        assertEquals("status" to 0, readFeatureReply(def, a296))
        assertEquals("status" to 1, readFeatureReply(BetaFeatureDefs.first { it.id == "priority_carpet" }, a296))
        assertEquals("status" to 0, readFeatureReply(BetaFeatureDefs.first { it.id == "cross_carpet" }, a296))
        assertEquals("""{"status":1}""", featureSetParamCandidates(def, null, 1).first().toString())
    }

    @Test
    fun carpet_boost_reads_enable_and_writes_it_wrapped() {
        val def = BetaFeatureDefs.first { it.id == "carpet_boost" }
        val reply = Json.parseToJsonElement("""[{"enable":1,"current_integral":1000,"current_high":500,"current_low":1000,"stall_time":10}]""")
        assertEquals("enable" to 1, readFeatureReply(def, reply))
        assertEquals(listOf("""[{"enable":0}]"""), featureSetParamCandidates(def, "enable", 0).map { it.toString() })
    }

    @Test
    fun pickers_accept_only_their_own_values() {
        val fluid = BetaFeatureDefs.first { it.id == "clean_fluid_delivery" }
        assertTrue(BetaFeatureState(fluid, value = 3).toggleable)
        assertEquals("High volume", BetaFeatureState(fluid, value = 3).choiceLabel)
        assertFalse(BetaFeatureState(fluid, value = 2).toggleable)
        val led = BetaFeatureDefs.first { it.id == "voice_control_led" }
        assertTrue(BetaFeatureState(led, value = 1).toggleable)
        assertFalse(BetaFeatureState(led, value = 3).toggleable)
    }

    @Test
    fun reply_shapes_parse_to_key_and_value() {
        assertEquals("status" to 1, parseBetaReply(Json.parseToJsonElement("""[{"status":1}]""")))
        assertEquals("status" to 0, parseBetaReply(Json.parseToJsonElement("""{"status":0}""")))
        assertEquals(null to 1, parseBetaReply(Json.parseToJsonElement("[1]")))
        assertEquals(null to 0, parseBetaReply(Json.parseToJsonElement("0")))
    }

    @Test
    fun unexpected_replies_do_not_parse() {
        assertNull(parseBetaReply(null))
        assertNull(parseBetaReply(Json.parseToJsonElement("""["ok"]""")))
        assertNull(parseBetaReply(Json.parseToJsonElement("""{"a":1,"b":2}""")))
        assertNull(parseBetaReply(Json.parseToJsonElement("[]")))
    }

    @Test
    fun set_params_try_the_bare_object_first_like_the_official_app() {
        // Captured from the official app on an a296: set_gap_deep_clean_status {"status":1}.
        assertEquals(
            listOf("""{"status":1}""", """[{"status":1}]""", "[1]"),
            betaSetParamCandidates("status", true).map { it.toString() },
        )
        assertEquals(listOf("[0]"), betaSetParamCandidates(null, false).map { it.toString() })
    }

    @Test
    fun set_tries_the_accepted_shape_first() {
        assertEquals(listOf(0, 1, 2), betaSetTryOrder(3, null))
        assertEquals(listOf(1, 0, 2), betaSetTryOrder(3, 1))
        assertEquals(listOf(2, 0, 1), betaSetTryOrder(3, 2))
        assertEquals(listOf(0), betaSetTryOrder(1, 2))
    }

    @Test
    fun both_param_error_codes_move_on_to_the_next_shape() {
        assertTrue(-10007 in BetaShapeRefusalCodes)
        assertTrue(-10005 in BetaShapeRefusalCodes)
        assertFalse(-10004 in BetaShapeRefusalCodes)
    }

    @Test
    fun advertised_features_without_a_switch_are_listed_by_name() {
        val set = "0000000000119418CCD57EFDA869B6DDDBFF8F7F7EFEFFFF"
        val listed = unlistedAdvertisedFeatures(set).map { it.name }
        assertTrue("Map eraser" in listed)
        // Curtains (bit 138) have the map editor's Curtain tool now.
        assertFalse("Curtains on the map" in listed)
        // Bits with nothing to switch (Matter needs Roborock's cloud) are not listed as missing.
        assertFalse("Matter" in listed)
        // Bits handled on another page (vacuum then mop) are not listed either.
        assertFalse("Clean then mop mode" in listed)
        // Bit 80 now has a switch (Detach mops automatically), so it is no longer listed as missing.
        assertFalse("Auto tear down mop" in listed)
        assertFalse("Gap deep clean" in listed)
        assertFalse("UVC sterilize" in listed)
        assertFalse("Mechanical arm mode" in listed)
    }

    @Test
    fun covered_and_handled_bits_are_not_listed() {
        // Bit 54 (FlexiArm side brush, has a switch) and bit 55 (SmartPlan, handled elsewhere).
        assertEquals(emptyList(), unlistedAdvertisedFeatures("C" + "0".repeat(13)))
    }

    @Test
    fun unnamed_bits_have_no_name_and_missing_sets_are_empty() {
        assertEquals(listOf(AdvertisedFeature(44, null)), unlistedAdvertisedFeatures("100000000000"))
        assertEquals(listOf(AdvertisedFeature(89, "Mechanical arm mode")), unlistedAdvertisedFeatures("2" + "0".repeat(22)))
        assertEquals(emptyList(), unlistedAdvertisedFeatures(null))
        assertEquals(emptyList(), unlistedAdvertisedFeatures(""))
    }

    @Test
    fun a_camera_option_changes_only_its_own_bit() {
        val pets = supportedBetaFeatures(null).first { it.id == "have_pets" }
        // Captured: 387 with "I have pets" off, 419 with it on.
        assertEquals(419, bitmaskWithBit(387, pets, 1))
        assertEquals(387, bitmaskWithBit(419, pets, 0))
        val light = supportedBetaFeatures(null).first { it.id == "fill_light" }
        assertEquals(385, bitmaskWithBit(387, light, 0))
    }

    @Test
    fun a_camera_option_is_read_from_the_bitmask() {
        val json = kotlinx.serialization.json.Json
        val pets = supportedBetaFeatures(null).first { it.id == "have_pets" }
        assertEquals(1, readFeatureReply(pets, json.parseToJsonElement("[419]"))?.second)
        assertEquals(0, readFeatureReply(pets, json.parseToJsonElement("[387]"))?.second)
    }
}
