package com.kodraliu.localrock.shared.vacuum.map

/**
 * The official app's room colouring, ported from the a296 plugin (`getNeighboursInfo` and
 * `getColorData`), so every room gets the same one of four colours as in the official app.
 *
 * Returns a colour index per room id: 1–4, or 0 for none. Index 1 always goes to the largest room.
 *
 * - Two rooms are neighbours when a cell of one touches a cell of the other directly (left or above),
 *   which happens through a doorway; a wall between them keeps them apart.
 * - Rooms are coloured greedily, most neighbours first (ties by room id), each taking the lowest
 *   index none of its coloured neighbours has.
 * - Empty colours are then filled by moving rooms out of a fuller colour. Rooms in one colour never
 *   touch each other, so the result stays valid.
 *
 * [raw] is the robot's map image, one byte per cell: the low three bits are non-zero on a room cell
 * and the room id is the value shifted right by three.
 */
fun officialRoomColorIndex(raw: ByteArray, width: Int, height: Int): Map<Int, Int> {
    val n = ROOM_SLOTS
    val adjacent = IntArray(n * n)
    val cells = IntArray(n)
    val ids = IntArray(width * height)
    for (y in 0 until height) {
        for (x in 0 until width) {
            val p = raw[y * width + x].toInt() and 0xff
            if (p and 7 == 0) continue
            val id = (p ushr 3) and (n - 1)
            ids[y * width + x] = id
            if (id == 0) continue
            adjacent[id * n + id] = 1
            if (y > 0) link(adjacent, n, id, ids[(y - 1) * width + x])
            if (x > 0) link(adjacent, n, id, ids[y * width + x - 1])
            cells[id]++
        }
    }
    var largest = 0
    for (id in 0 until n) if (cells[id] > cells[largest]) largest = id
    if (largest == 0) return emptyMap()

    // Most neighbours first; a stable sort keeps room-id order among equals, as the plugin's does.
    val order = (1 until n).map { id -> id to (1 until n).count { it != id && adjacent[id * n + it] == 1 } }
        .sortedByDescending { it.second }
        .map { it.first }

    val color = IntArray(n)
    val groups = List(4) { mutableListOf<Int>() }
    color[largest] = 1
    groups[0] += largest
    for (id in order) {
        if (id == largest || adjacent[id * n + id] == 0) continue
        val used = BooleanArray(5)
        for (other in order) {
            if (adjacent[other * n + id] != 0 && color[other] != 0) used[color[other]] = true
        }
        val c = (1..4).firstOrNull { !used[it] } ?: continue
        adjacent[id * n + id] = c
        color[id] = c
        groups[c - 1] += id
    }

    // An empty colour takes the second half of the colour before it (colour 2 and 3 from 1, 4 from 2).
    for (i in 1 until 4) {
        if (groups[i].isNotEmpty()) continue
        val from = groups[(i + 1) / 2 - 1]
        if (from.size <= 1) continue
        val half = from.size / 2.0
        var k = from.size - 1
        var moved = 0
        while (k >= half) {
            groups[i] += from[k]
            k--
            moved++
        }
        repeat(moved) { from.removeAt(from.size - 1) }
    }
    // Then the fullest colour hands rooms to the last empty one until it has no more than it.
    var fullest = 0
    var empty = -1
    for (i in 0 until 4) {
        if (groups[i].size > groups[fullest].size) fullest = i
        if (groups[i].isEmpty()) empty = i
    }
    if (groups[fullest].size >= 2 && empty != -1) {
        while (groups[fullest].size > groups[empty].size) {
            groups[empty] += groups[fullest].removeAt(groups[fullest].size - 1)
        }
    }

    val out = HashMap<Int, Int>()
    groups.forEachIndexed { i, ids -> ids.forEach { out[it] = i + 1 } }
    return out
}

private fun link(adjacent: IntArray, n: Int, a: Int, b: Int) {
    if (b == a) return
    adjacent[a * n + b] = 1
    adjacent[b * n + a] = 1
}

/** The plugin's matrix is 32 × 32: room ids 1–31. */
private const val ROOM_SLOTS = 32
