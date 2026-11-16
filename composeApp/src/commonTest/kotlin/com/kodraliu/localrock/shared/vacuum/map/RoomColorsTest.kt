package com.kodraliu.localrock.shared.vacuum.map

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class RoomColorsTest {
    /** Rooms drawn as rectangles; a room cell is `(id shl 3) or 7`, a wall cell is 1. */
    private class Grid(val w: Int, val h: Int) {
        val bytes = ByteArray(w * h)
        fun room(id: Int, x0: Int, y0: Int, x1: Int, y1: Int) {
            for (y in y0..y1) for (x in x0..x1) bytes[y * w + x] = ((id shl 3) or 7).toByte()
        }
        fun wall(x0: Int, y0: Int, x1: Int, y1: Int) {
            for (y in y0..y1) for (x in x0..x1) bytes[y * w + x] = 1
        }
    }

    @Test
    fun matchesTheOfficialAppOnTheUpstairsMap() {
        // Map 2 as the official app shows it: Slaapkamer (1, largest) and Bedroom (4) each open onto
        // the Hall (3), the Shower (2) opens onto the Hall, and a wall separates Bedroom and Slaapkamer.
        val g = Grid(40, 30)
        g.room(4, 0, 0, 11, 9)      // Bedroom
        g.wall(12, 0, 12, 9)
        g.room(1, 13, 0, 39, 29)    // Slaapkamer
        g.room(3, 0, 10, 12, 15)    // Hall, touching Bedroom above and Slaapkamer to the right
        g.room(2, 0, 16, 6, 22)     // Shower, below the Hall
        g.wall(7, 16, 12, 22)
        val colors = officialRoomColorIndex(g.bytes, g.w, g.h)
        assertEquals(mapOf(1 to 1, 3 to 2, 4 to 3, 2 to 4), colors)
    }

    @Test
    fun neighboursNeverShareAColour() {
        // A row of rooms, each opening onto the next.
        val g = Grid(60, 5)
        for (i in 0 until 6) g.room(i + 1, i * 10, 0, i * 10 + 9, 4)
        val colors = officialRoomColorIndex(g.bytes, g.w, g.h)
        for (id in 1 until 6) assertTrue(colors[id] != colors[id + 1], "rooms $id and ${id + 1}")
        assertTrue(colors.values.all { it in 1..4 })
    }

    @Test
    fun noRoomsGiveNoColours() {
        assertEquals(emptyMap(), officialRoomColorIndex(ByteArray(16), 4, 4))
    }
}
