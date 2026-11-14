package com.kodraliu.localrock.shared.vacuum

import com.kodraliu.localrock.shared.vacuum.map.LOW_SPACE_DETECTED
import com.kodraliu.localrock.shared.vacuum.map.LOW_SPACE_ERASED
import com.kodraliu.localrock.shared.vacuum.map.LOW_SPACE_USER
import com.kodraliu.localrock.shared.vacuum.map.MapZone
import com.kodraliu.localrock.shared.vacuum.map.ZoneKind
import com.kodraliu.localrock.shared.vacuum.map.parseCurtains
import com.kodraliu.localrock.shared.vacuum.map.parseFurniture
import com.kodraliu.localrock.shared.vacuum.map.parseLowSpaceZones
import kotlin.test.Test
import kotlin.test.assertEquals

class MapFurnitureCurtainTest {
    private fun hex(s: String) = ByteArray(s.length / 2) { s.substring(it * 2, it * 2 + 2).toInt(16).toByte() }

    /** A block as the map carries it: type, 12-byte header with the entry count, then [body]. */
    private fun block(type: Int, count: Int, body: String): ByteArray {
        val b = hex(body)
        val head = byteArrayOf(type.toByte(), 0, 12, 0, b.size.toByte(), (b.size shr 8).toByte(), 0, 0, count.toByte(), 0, 0, 0)
        return head + b
    }

    // Read from an a296's map (2026-11-14) after drawing one of each in the official app.
    private val curtainBody = "c860f172c269f172c2698d72c8608d7200000000"
    private val furnitureBody = "5d61876d2d69876d2d699f695d619f6910272e02010101" + "6360236d276a236d276a036a6360036a10272b00010201"
    private val lowSpaceBody = "00000000c8603c77c2693c77c269436ec860436e" + "01000000dd6d49753b6f49753b6fa177dd6da177" +
        "02000000c7603d77c3693d77c369796dc760796d"

    @Test
    fun readsCurtains() {
        val b = block(59, 1, curtainBody)
        val c = parseCurtains(b, 0, 12, b.size - 12).single()
        assertEquals(listOf(24776, 29425, 27074, 29425), listOf(c.x0, c.y0, c.x1, c.y1))
        assertEquals(listOf(0, 0), c.extra)
    }

    @Test
    fun readsFurnitureWithTypeAndId() {
        val b = block(25, 2, furnitureBody)
        val f = parseFurniture(b, 0, 12, b.size - 12)
        assertEquals(listOf(46 to 2, 43 to 0), f.map { it.furnitureType to it.furnitureSubtype })
        assertEquals(listOf(1, 2), f.map { it.furnitureId })
        assertEquals(24925, f[0].x0)
    }

    @Test
    fun readsUnderFurnitureZonesOfEachKind() {
        val b = block(52, 3, lowSpaceBody)
        val z = parseLowSpaceZones(b, 0, 12, b.size - 12)
        assertEquals(listOf(LOW_SPACE_USER, LOW_SPACE_DETECTED, LOW_SPACE_ERASED), z.map { it.lowSpaceKind })
        assertEquals(24776, z[0].x0)
    }

    @Test
    fun curtainsAreSentAsTheOfficialAppSentThem() {
        val b = block(59, 1, curtainBody)
        val c = parseCurtains(b, 0, 12, b.size - 12)
        assertEquals(
            """{"map_index":0,"zones":[[24776,29425,27074,29425,27074,29325,24776,29325,0,0]]}""",
            encodeCurtainParams(c, 0).toString(),
        )
        assertEquals("""{"map_index":0,"zones":[]}""", encodeCurtainParams(emptyList(), 0).toString())
    }

    @Test
    fun underFurnitureLeavesWhatTheRobotFoundAlone() {
        val b = block(52, 3, lowSpaceBody)
        val z = parseLowSpaceZones(b, 0, 12, b.size - 12)
        assertEquals(
            """{"map_index":0,"zone_data":[[0,24776,30524,27074,30524,27074,28227,24776,28227],""" +
                """[2,24775,30525,27075,30525,27075,28025,24775,28025]]}""",
            encodeLowSpaceParams(z, 0).toString(),
        )
    }

    @Test
    fun furnitureIsSentAsChanges() {
        val sofa = MapZone(24925, 28039, 26925, 28039, 26925, 27039, 24925, 27039, ZoneKind.FURNITURE, extra = listOf(46, 2))
        // Captured: [[1,-1,24925,28039,26925,28039,26925,27039,24925,27039,46,2,1]] to add, [[0,1]] to delete.
        assertEquals(
            """{"data":[[1,-1,24925,28039,26925,28039,26925,27039,24925,27039,46,2,1],[0,7]],"map_flag":0}""",
            encodeFurnitureChanges(listOf(sofa), listOf(7), 0).toString(),
        )
    }

    @Test
    fun aMovedPieceIsRemovedAndAddedAgain() {
        val b = block(25, 2, furnitureBody)
        val before = parseFurniture(b, 0, 12, b.size - 12)
        val after = listOf(before[0], before[1].copy(x0 = before[1].x0 + 1000, x1 = before[1].x1 + 1000, x2 = before[1].x2 + 1000, x3 = before[1].x3 + 1000))
        val (added, removed) = furnitureChanges(before, after)
        assertEquals(listOf(2), removed)
        assertEquals(1, added.size)
        assertEquals(43, added[0].furnitureType)
        // Nothing changed: nothing to send.
        assertEquals(emptyList<MapZone>() to emptyList(), furnitureChanges(before, before))
    }
}

class A296CarpetBlockTest {
    @Test
    fun carpetsOnlyInBlock39AreRead() {
        // A minimal legacy map: header, a 1x1 image, an empty block 22 and block 39 with one carpet (a296 layout).
        fun le16(v: Int) = byteArrayOf(v.toByte(), (v shr 8).toByte())
        fun le32(v: Int) = byteArrayOf(v.toByte(), (v shr 8).toByte(), (v shr 16).toByte(), (v shr 24).toByte())
        val header = byteArrayOf('r'.code.toByte(), 'r'.code.toByte()) + le16(20) + ByteArray(16)
        val image = le16(2) + le16(28) + le32(1) + le32(0) + le32(0) + le32(0) + le32(1) + le32(1) + byteArrayOf(7)
        val block22 = le16(22) + le16(12) + le32(0) + le32(0)
        val quad = listOf(25525, 34275, 26275, 34275, 26275, 35225, 25525, 35225).fold(ByteArray(0)) { a, v -> a + le16(v) }
        val block39 = le16(39) + le16(12) + le32(20) + le32(1) + quad + le32(0x331c7e)
        val map = com.kodraliu.localrock.shared.vacuum.map.parseLegacyMap(header + image + block22 + block39)
        assertEquals(1, map.carpetAreas.size)
        assertEquals(0x331c7eL, map.carpetAreas[0].flags)
        assertEquals(true, map.carpetFlagsPresent)
    }
}
