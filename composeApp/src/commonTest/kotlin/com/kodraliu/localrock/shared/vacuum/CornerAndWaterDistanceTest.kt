package com.kodraliu.localrock.shared.vacuum

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class CornerAndWaterDistanceTest {
    // A Saros 20 Neo (a296): water slide, no corner mopping, bit 31 of new_feature_info set.
    private val a296Hex = "0000020008199518CED77EFDA8E9BEDDDBFF8F7F7EFEFFFF"
    private val a296Decimal = "4499197267967999"

    @Test
    fun cornerMoppingFollowsBit31OfTheLowWord() {
        assertFalse(isCornerCleanSupported(a296Hex))
        assertTrue(isCornerCleanSupported("80000000"))
        assertFalse(isCornerCleanSupported(null))
    }

    @Test
    fun waterDistanceNeedsBit31AndNoSlide() {
        // The a296 has bit 31 but uses its water slide instead.
        assertFalse(isWaterDistanceSupported(a296Decimal, a296Hex))
        assertTrue(isWaterDistanceSupported((1L shl 31).toString(), "0"))
        assertFalse(isWaterDistanceSupported("1", "0"))
        assertFalse(isWaterDistanceSupported(null, null))
    }

    @Test
    fun theSliderRunsAgainstTheDistance() {
        assertEquals(146, WaterDistance.level(60))
        assertEquals(1, WaterDistance.level(205))
        assertEquals(60, WaterDistance.distanceFor(146))
        assertEquals(205, WaterDistance.distanceFor(1))
        for (d in WaterDistance.RANGE) assertEquals(d, WaterDistance.distanceFor(WaterDistance.level(d)))
    }

    @Test
    fun startsWhereThePluginStarts() {
        assertEquals(135, WaterDistance.DEFAULT)
        assertEquals(135, WaterDistance.initial(0))
        assertEquals(135, WaterDistance.initial(null))
        assertEquals(100, WaterDistance.initial(100))
    }
}
