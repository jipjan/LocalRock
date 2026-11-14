package com.kodraliu.localrock.shared.vacuum

import kotlin.test.Test
import kotlin.test.assertEquals

class VolumeLabelTest {
    @Test
    fun only_zero_is_off() {
        // A Qrevo Curv 2 Flow reports [10] and an S8 Pro Ultra [20]; both still speak.
        assertEquals("Off", volumeLabel(0))
        assertEquals("10%", volumeLabel(10))
        assertEquals("20%", volumeLabel(20))
    }

    @Test
    fun any_value_shows_as_its_percentage() {
        assertEquals("1%", volumeLabel(1))
        assertEquals("45%", volumeLabel(45))
        assertEquals("100%", volumeLabel(100))
    }

    @Test
    fun values_outside_the_range_are_clamped() {
        assertEquals("Off", volumeLabel(-5))
        assertEquals("100%", volumeLabel(120))
    }

    @Test
    fun slider_positions_round_to_whole_volumes_in_range() {
        assertEquals(0, volumeFromSlider(0f))
        assertEquals(45, volumeFromSlider(44.6f))
        assertEquals(44, volumeFromSlider(44.4f))
        assertEquals(100, volumeFromSlider(100f))
        assertEquals(0, volumeFromSlider(-3f))
        assertEquals(100, volumeFromSlider(104f))
    }
}
