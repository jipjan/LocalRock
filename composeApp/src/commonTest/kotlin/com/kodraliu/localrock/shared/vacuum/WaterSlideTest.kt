package com.kodraliu.localrock.shared.vacuum

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class WaterSlideTest {
    @Test
    fun levelsRunFromOneToThirty() {
        assertEquals(1, WaterBoxMode.slideLevel(221))
        assertEquals(15, WaterBoxMode.slideLevel(235))
        assertEquals(30, WaterBoxMode.slideLevel(250))
    }

    @Test
    fun codesOutsideTheSlideHaveNoLevel() {
        assertNull(WaterBoxMode.slideLevel(null))
        assertNull(WaterBoxMode.slideLevel(WaterBoxMode.LOW))
        assertNull(WaterBoxMode.slideLevel(251))
        assertNull(WaterBoxMode.slideLabel(WaterBoxMode.OFF))
    }

    @Test
    fun theOfficialMarksGetTheirNames() {
        assertEquals("5 · Low", WaterBoxMode.slideLabel(WaterBoxMode.SLIDE_LOW))
        assertEquals("15 · Medium", WaterBoxMode.slideLabel(WaterBoxMode.SLIDE_MEDIUM))
        assertEquals("25 · High", WaterBoxMode.slideLabel(WaterBoxMode.SLIDE_HIGH))
        assertEquals("28 · Extra high", WaterBoxMode.slideLabel(248))
        assertEquals("1 · Low", WaterBoxMode.slideLabel(221))
    }

    @Test
    fun bandsChangeHalfwayBetweenMarks() {
        assertEquals("9 · Low", WaterBoxMode.slideLabel(229))
        assertEquals("10 · Medium", WaterBoxMode.slideLabel(230))
        assertEquals("19 · Medium", WaterBoxMode.slideLabel(239))
        assertEquals("20 · High", WaterBoxMode.slideLabel(240))
    }
}
