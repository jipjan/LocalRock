package com.kodraliu.localrock.shared.vacuum

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ConsumableSummaryTest {
    private val hour = 3600L

    @Test
    fun normalUnlessAPartIsNearlyOrFullyWorn() {
        assertNull(ConsumableStatus().summary())
        assertEquals("Normal", ConsumableStatus(mainBrushWorkTime = 2 * hour, filterWorkTime = 2 * hour, sensorDirtyTime = 2 * hour).summary())
        // The sensors (30 h) are the most worn at 28 h: 7% left.
        assertEquals("Sensors: 7% left", ConsumableStatus(filterWorkTime = 2 * hour, sensorDirtyTime = 28 * hour).summary())
        assertEquals("Clean the sensors", ConsumableStatus(sensorDirtyTime = 31 * hour).summary())
        assertEquals("Replace the filter", ConsumableStatus(filterWorkTime = 150 * hour, sensorDirtyTime = 0).summary())
    }
}
