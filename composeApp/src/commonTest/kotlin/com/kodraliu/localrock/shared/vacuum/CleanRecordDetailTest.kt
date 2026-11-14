package com.kodraliu.localrock.shared.vacuum

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class CleanRecordDetailTest {
    // An a296 record: 2277 s, 25.7 m², 47 obstacles avoided, 2 mop washes, bin emptied, one pass.
    private val a296 = CleanRecord(1794658027, 1794660746, 2277, 25_737_500, 0, 0, 47, 2, true, 1)

    @Test
    fun saysWhatTheRobotDid() {
        assertEquals("47 obstacles avoided · 2 mop washes · bin emptied", a296.detailLine)
    }

    @Test
    fun efficiencyIsSquareMetresPerMinute() {
        assertEquals(0.68f, a296.efficiencyM2PerMinute!!, 0.01f)
        assertNull(a296.copy(durationSeconds = 30).efficiencyM2PerMinute)
    }

    @Test
    fun olderRobotsShowNoDetail() {
        assertNull(CleanRecord(1, 2, 600, 1_000_000, 0, 1).detailLine)
    }
}
