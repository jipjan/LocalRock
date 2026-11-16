package com.kodraliu.localrock.ui.vacuum

import com.kodraliu.localrock.shared.vacuum.map.officialRoomColorIndex
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.kodraliu.localrock.ui.LocalAppContainer
import androidx.compose.runtime.collectAsState
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.PaintingStyle
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.luminance
import com.kodraliu.localrock.shared.vacuum.map.FloorMaterial
import com.kodraliu.localrock.shared.vacuum.map.MapZone
import com.kodraliu.localrock.shared.vacuum.map.ParsedMap
import com.kodraliu.localrock.shared.vacuum.map.ParsedMapPoint
import com.kodraliu.localrock.shared.vacuum.map.VirtualWall

// Zone overlays are translucent, so one pair reads over both the light and dark palettes.
private val NO_GO_FILL = Color(0x33f44336)    // translucent red
private val NO_GO_STROKE = Color(0xccf44336)
private val NO_MOP_FILL = Color(0x332196f3)   // translucent blue
private val NO_MOP_STROKE = Color(0xcc2196f3)
private val WALL_STROKE = Color(0xffe53935)     // solid red, drawn as a line
private val CARPET_FILL = Color(0x40a1887f)     // translucent brown
private val CARPET_STROKE = Color(0xcca1887f)
private val THRESHOLD_FILL = Color(0x66ffb300)  // amber, small strips across doorways
private val THRESHOLD_STROKE = Color(0xffffb300)

/**
 * The map is the signature surface, so it gets a palette per theme instead of one light bitmap
 * that glows in a dark UI. Room tints stay in a narrow luminance band so no single room shouts,
 * and markers differ in shape as well as colour so they don't rely on colour alone.
 */
private class MapPalette(
    val wall: Color,
    val floor: Color,
    /** The official app's four room colours, for colour index 1–4 ([officialRoomColorIndex]). */
    val rooms: List<Color>,
    /** A room with no colour index (or a map without room data): the official app's grey. */
    val unassigned: Color,
    val path: Color,
    val charger: Color,
    val robot: Color,
    /** Drawn behind both markers so they stay readable on any room tint. */
    val markerRing: Color,
    /** Plank and tile lines: a faint shade that shows on the room tints of this palette. */
    val texture: Color,
)

// Colours from the official a296 plugin: room fills are its `mapBlockHLColor` (light `colorBubble`,
// dark `colorBubbleDark`), path and wall its map theme's `pathColor` and `wallColor`.
private val LightPalette = MapPalette(
    wall = Color(0xFF6B7174),
    floor = Color(0xFFE9EFEE),
    rooms = listOf(Color(0xFF98C9FF), Color(0xFFFFB196), Color(0xFF1FE7DB), Color(0xFFFFDD63)),
    unassigned = Color(0x88BBBBBB),
    path = Color(0xFFFFFFFF),
    charger = Color(0xFF2BBF8A),
    robot = Color(0xFFFFFFFF),
    markerRing = Color(0xFF2BBF8A),
    texture = Color(0x14000000),
)

private val DarkPalette = MapPalette(
    wall = Color(0xFF6D7476),
    floor = Color(0xFF17201F),
    rooms = listOf(Color(0xFF73AAE9), Color(0xFFD97E5F), Color(0xFF25D1C7), Color(0xFFE5BE5A)),
    unassigned = Color(0x88BBBBBB),
    path = Color(0x99FFFFFF),
    charger = Color(0xFF2BBF8A),
    robot = Color(0xFFFFFFFF),
    markerRing = Color(0xFF2BBF8A),
    texture = Color(0x1A000000),
)

/** Label colours per colour index, from the plugin's `tagTintColors` (light, dark). */
internal val RoomTagLight = listOf(Color(0xFF1F558E), Color(0xFFA0432B), Color(0xFF0B5D54), Color(0xFF7E5C00))
internal val RoomTagDark = listOf(Color(0xFF143357), Color(0xFF752C1A), Color(0xFF005558), Color(0xFF664300))

/** The official colour index (1–4) per room id of [map], or empty without room data. */
internal fun ParsedMap.roomColorIndex(): Map<Int, Int> =
    originalGrid?.let { officialRoomColorIndex(it, width, height) } ?: emptyMap()

/**
 * Follows whatever scheme is actually applied — including platform dynamic colour — rather than
 * the raw system setting, so the map never disagrees with the surface it sits on.
 */
@Composable
fun rememberMapBitmap(map: ParsedMap, drawRestrictions: Boolean = true): ImageBitmap {
    val dark = MaterialTheme.colorScheme.surface.luminance() < 0.5f
    val outlines by LocalAppContainer.current.appSettings.mapRoomOutlines.collectAsState()
    // The first bitmap is drawn at once so there is something to show; later ones (a map poll every few
    // seconds while cleaning) are drawn off the main thread, and the previous one stays until it is ready.
    val first = remember { renderMap(map, if (dark) DarkPalette else LightPalette, drawRestrictions, outlines) }
    val firstKey = remember { listOf(map, dark, drawRestrictions, outlines) }
    var shown by remember { mutableStateOf(first) }
    LaunchedEffect(map, dark, drawRestrictions, outlines) {
        if (listOf(map, dark, drawRestrictions, outlines) == firstKey) {
            shown = first
            return@LaunchedEffect
        }
        shown = withContext(Dispatchers.Default) { renderMap(map, if (dark) DarkPalette else LightPalette, drawRestrictions, outlines) }
    }
    return shown
}

/**
 * Normalised bounds (0..1, in drawn-image space with Y already flipped) of the cells that actually
 * contain floor or wall. A parsed map is mostly empty grid, so a view that shows the whole bitmap
 * wastes most of its area on padding; this lets the hero frame the floor plan instead.
 */
class MapContentBounds(val left: Float, val top: Float, val right: Float, val bottom: Float) {
    val width: Float get() = right - left
    val height: Float get() = bottom - top
    val isUsable: Boolean get() = width > 0.01f && height > 0.01f
}

fun ParsedMap.contentBoundsNorm(): MapContentBounds {
    var minX = width
    var maxX = -1
    var minY = height
    var maxY = -1
    for (y in 0 until height) {
        val rowOff = y * width
        // Match renderMap's Y flip so the bounds are in the same space as the drawn bitmap.
        val drawY = height - 1 - y
        for (x in 0 until width) {
            val gv = grid[rowOff + x].toInt() and 0xff
            if (gv != 127 && gv != 128) continue
            if (x < minX) minX = x
            if (x > maxX) maxX = x
            if (drawY < minY) minY = drawY
            if (drawY > maxY) maxY = drawY
        }
    }
    if (maxX < 0 || maxY < 0) return MapContentBounds(0f, 0f, 1f, 1f)
    var left = minX.toFloat()
    var top = minY.toFloat()
    var right = (maxX + 1).toFloat()
    var bottom = (maxY + 1).toFloat()
    // Room outlines (block 55) can reach past the robot's cells; frame them too, or a room is cut off.
    for (room in floorPlanPolygons(this)) {
        left = minOf(left, room.minX)
        top = minOf(top, room.minY)
        right = maxOf(right, room.maxX)
        bottom = maxOf(bottom, room.maxY)
    }
    return MapContentBounds(
        left = (left / width).coerceIn(0f, 1f),
        top = (top / height).coerceIn(0f, 1f),
        right = (right / width).coerceIn(0f, 1f),
        bottom = (bottom / height).coerceIn(0f, 1f),
    )
}

/**
 * Pixels per map cell (a cell is 50 mm). The map used to be drawn one pixel per cell and stretched,
 * which made every edge a staircase and the 1-cell cleaning path a thick band. Drawing at several
 * pixels per cell gives room for round edges, a thin path and floor textures, as the official app
 * shows them. Capped so the bitmap stays near 2048 px on its long side.
 */
internal fun mapRenderScale(map: ParsedMap): Int =
    (MAX_MAP_BITMAP_PX / maxOf(map.width, map.height, 1)).coerceIn(1, MAX_RENDER_SCALE)

private const val MAX_RENDER_SCALE = 4
private const val MAX_MAP_BITMAP_PX = 2048

/** [drawRestrictions] is false for the editor, which draws the shapes it is editing itself. */
private fun renderMap(map: ParsedMap, palette: MapPalette, drawRestrictions: Boolean, roomOutlines: Boolean = true): ImageBitmap {
    val s = mapRenderScale(map)
    val bitmap = ImageBitmap(map.width * s, map.height * s)
    val canvas = Canvas(bitmap)
    // Everything below is drawn in cell units; the scale turns them into pixels.
    canvas.scale(s.toFloat(), s.toFloat())
    val w = map.width
    val h = map.height
    val grid = map.grid
    val raw = map.originalGrid

    fun kind(x: Int, y: Int): Int = if (x in 0 until w && y in 0 until h) grid[y * w + x].toInt() and 0xff else 0
    fun room(x: Int, y: Int): Int = if (raw != null) (raw[y * w + x].toInt() ushr 3) and 0x1f else -1
    val colorIndex = map.roomColorIndex()
    fun roomFill(id: Int): Color = colorIndex[id]?.let { palette.rooms[it - 1] } ?: palette.unassigned
    fun floorColor(x: Int, y: Int): Color =
        if (raw != null) roomFill(room(x, y)) else palette.floor

    val fill = Paint().apply { isAntiAlias = true }
    val boundaryPaint = Paint()
    val roomWeights = FloatArray(ROOM_ID_COUNT)
    // Rooms with a floor-plan outline (block 55) are drawn as that polygon, the way the official app
    // draws them: straight walls and sharp corners instead of the robot's pixel edges.
    val plan = if (raw != null && roomOutlines) floorPlanPolygons(map) else emptyList()
    val planRooms = BooleanArray(ROOM_ID_COUNT).also { has -> plan.forEach { has[it.roomId and (ROOM_ID_COUNT - 1)] = true } }
    val inPlan = planCells(plan, w, h)
    fun inside(x: Int, y: Int) = x in 0 until w && y in 0 until h && inPlan[y * w + x] >= 0
    fun nearPlan(x: Int, y: Int) = (-2..2).any { dy -> (-2..2).any { dx -> inside(x + dx, y + dy) } }
    for (room in plan) {
        fill.color = roomFill(room.roomId)
        canvas.drawPath(room.path, fill)
    }
    // Floor: a cell surrounded by the same room is a plain square; an edge cell is drawn as a round
    // dot a little larger than the cell, so outlines come out rounded rather than stepped.
    for (y in 0 until h) {
        val drawY = (h - 1 - y).toFloat()
        for (x in 0 until w) {
            if (plan.isNotEmpty()) {
                if (inside(x, y)) {
                    // The outline is the room. Floor inside it that the robot gave no room is grey, as in
                    // the official app; walls and unmapped cells inside are not drawn.
                    if (kind(x, y) == 128 && room(x, y) == 0) {
                        fill.color = PLAN_UNASSIGNED
                        canvas.drawRect(Rect(x.toFloat(), drawY, x + 1f, drawY + 1f), fill)
                    }
                    continue
                }
                // The robot's pixel fringe along an outline; floor further out (newly explored) still shows.
                if (kind(x, y) == 128 && planRooms[room(x, y) and (ROOM_ID_COUNT - 1)] && nearPlan(x, y)) continue
            }
            if (kind(x, y) != 128) continue
            fill.color = floorColor(x, y)
            val r = room(x, y)
            val inner = (kind(x - 1, y) == 128 && room(x - 1, y) == r) && (kind(x + 1, y) == 128 && room(x + 1, y) == r) &&
                (kind(x, y - 1) == 128 && room(x, y - 1) == r) && (kind(x, y + 1) == 128 && room(x, y + 1) == r)
            val borderRoom = raw != null && (-1..1).any { dy -> (-1..1).any { dx ->
                kind(x + dx, y + dy) == 128 && room(x + dx, y + dy) != r
            } }
            if (borderRoom) {
                // Where two rooms meet, colour each sub-pixel by the room with the most weight among
                // the cells around it, so the boundary curves instead of stepping cell by cell.
                drawRoomBoundaryCell(canvas, x, y, drawY, s, ::kind, ::room, boundaryPaint, roomWeights) { roomFill(it) }
            } else if (inner) {
                canvas.drawRect(Rect(x.toFloat(), drawY, x + 1f, drawY + 1f), fill)
            } else {
                canvas.drawRect(Rect(x + 0.15f, drawY + 0.15f, x + 0.85f, drawY + 0.85f), fill)
                canvas.drawCircle(Offset(x + 0.5f, drawY + 0.5f), 0.62f, fill)
            }
        }
    }
    drawFloorTextures(canvas, map, palette)
    // Walls as one stroke through neighbouring wall cells (round caps and joins), so a wall reads as a
    // smooth line rather than a row of blocks or beads.
    val wallStroke = Paint().apply {
        color = palette.wall
        style = PaintingStyle.Stroke
        strokeWidth = 0.9f
        strokeCap = StrokeCap.Round
        isAntiAlias = true
    }
    fill.color = palette.wall
    for (y in 0 until h) {
        val drawY = (h - 1 - y).toFloat()
        for (x in 0 until w) {
            if (kind(x, y) != 127) continue
            // The outline is the wall: none inside a floor-plan room, nor along its edge.
            if (plan.isNotEmpty() && nearPlan(x, y)) continue
            val c = Offset(x + 0.5f, drawY + 0.5f)
            var joined = false
            for (n in 0 until 4) {
                val dx = WALL_DX[n]
                val dy = WALL_DY[n]
                if (kind(x + dx, y + dy) != 127) continue
                // A diagonal only where no straight neighbour already makes the corner.
                if (dx != 0 && dy != 0 && (kind(x + dx, y) == 127 || kind(x, y + dy) == 127)) continue
                canvas.drawLine(c, Offset(c.x + dx, c.y - dy), wallStroke)
                joined = true
            }
            if (!joined) canvas.drawCircle(c, 0.45f, fill)
        }
    }
    if (plan.isNotEmpty()) {
        val outline = Paint().apply {
            color = palette.wall
            style = PaintingStyle.Stroke
            strokeWidth = 1.0f
            strokeJoin = StrokeJoin.Round
            isAntiAlias = true
        }
        plan.forEach { canvas.drawPath(it.path, outline) }
    }
    if (map.pathMm.size >= 2) drawPath(canvas, map, palette)
    if (drawRestrictions) {
        map.carpetAreas.forEach { drawZone(canvas, map, it, CARPET_FILL, CARPET_STROKE) }
        map.thresholds.forEach { drawZone(canvas, map, it, THRESHOLD_FILL, THRESHOLD_STROKE) }
        map.noMopZones.forEach { drawZone(canvas, map, it, NO_MOP_FILL, NO_MOP_STROKE) }
        map.noGoZones.forEach { drawZone(canvas, map, it, NO_GO_FILL, NO_GO_STROKE) }
        map.virtualWalls.forEach { drawWall(canvas, map, it) }
    }
    // Square dock, round robot: the two markers stay distinguishable without relying on colour.
    // Green dock, white robot in a green ring: the official app's colours. Square and round still
    // tell them apart without colour.
    map.chargerMm?.let { drawMarker(canvas, map, it, palette.charger, Color.White, square = true) }
    map.robotMm?.let { drawMarker(canvas, map, it, palette.robot, palette.markerRing, square = false) }
    return bitmap
}

/** Floor inside a floor-plan room that the robot gave no room: the light grey the official app uses. */
private val PLAN_UNASSIGNED = Color(0xffb0b0b0)

internal class PlanPolygon(val roomId: Int, val corners: List<Offset>) {
    val minX = corners.minOf { it.x }
    val maxX = corners.maxOf { it.x }
    val minY = corners.minOf { it.y }
    val maxY = corners.maxOf { it.y }

    val path: Path by lazy {
        Path().apply {
            moveTo(corners[0].x, corners[0].y)
            corners.drop(1).forEach { lineTo(it.x, it.y) }
            close()
        }
    }
}

/** The floor-plan outlines in cell space (y down, like the bitmap). */
internal fun floorPlanPolygons(map: ParsedMap): List<PlanPolygon> = map.floorPlan.map { room ->
    PlanPolygon(room.roomId, room.pointsMm.map { (x, y) -> map.mmToCell(x, y) })
}

/** Per cell, the room id of the outline its centre lies in, or -1. */
internal fun planCells(plan: List<PlanPolygon>, w: Int, h: Int): IntArray {
    val out = IntArray(w * h) { -1 }
    if (plan.isEmpty()) return out
    for (y in 0 until h) {
        val cy = (h - 1 - y) + 0.5f
        for (x in 0 until w) {
            val cx = x + 0.5f
            for (room in plan) {
                if (cx < room.minX || cx > room.maxX || cy < room.minY || cy > room.maxY) continue
                if (pointInPolygon(cx, cy, room.corners)) { out[y * w + x] = room.roomId; break }
            }
        }
    }
    return out
}

private fun pointInPolygon(px: Float, py: Float, pts: List<Offset>): Boolean {
    var inside = false
    var j = pts.lastIndex
    for (i in pts.indices) {
        val a = pts[i]
        val b = pts[j]
        if ((a.y > py) != (b.y > py) && px < (b.x - a.x) * (py - a.y) / (b.y - a.y) + a.x) inside = !inside
        j = i
    }
    return inside
}

/** Room ids are the pixel's top five bits. */
private const val ROOM_ID_COUNT = 32

/** Right, up and both diagonals, so every pair of touching wall cells is joined once. */
private val WALL_DX = intArrayOf(1, 0, 1, 1)
private val WALL_DY = intArrayOf(0, 1, 1, -1)

/** Cells within this distance (in cells) of a sub-pixel vote on its room, weighted by closeness. */
private const val ROOM_SMOOTH_RADIUS = 1.6f

/**
 * Draws one floor cell on a room boundary as [s]×[s] sub-pixels. Each takes the room with the most
 * weight among floor cells whose centres lie within [ROOM_SMOOTH_RADIUS], weight falling off linearly
 * with distance. A straight boundary stays straight; a staircase becomes a smooth diagonal or curve.
 */
private inline fun drawRoomBoundaryCell(
    canvas: Canvas,
    x: Int,
    y: Int,
    drawY: Float,
    s: Int,
    kind: (Int, Int) -> Int,
    room: (Int, Int) -> Int,
    paint: Paint,
    weights: FloatArray,
    color: (Int) -> Color,
) {
    val step = 1f / s
    for (sy in 0 until s) {
        for (sx in 0 until s) {
            // Sub-pixel centre in map cell space (y grows upwards in the map).
            val px = x + (sx + 0.5f) * step
            val py = y + 1f - (sy + 0.5f) * step
            weights.fill(0f)
            for (dy in -2..2) for (dx in -2..2) {
                val cx = x + dx
                val cy = y + dy
                if (kind(cx, cy) != 128) continue
                val ddx = px - (cx + 0.5f)
                val ddy = py - (cy + 0.5f)
                val d = kotlin.math.sqrt(ddx * ddx + ddy * ddy)
                if (d >= ROOM_SMOOTH_RADIUS) continue
                val id = room(cx, cy) and (ROOM_ID_COUNT - 1)
                weights[id] += ROOM_SMOOTH_RADIUS - d
            }
            var best = room(x, y) and (ROOM_ID_COUNT - 1)
            for (id in weights.indices) if (weights[id] > weights[best]) best = id
            paint.color = color(best)
            canvas.drawRect(Rect(x + sx * step, drawY + sy * step, x + (sx + 1) * step, drawY + (sy + 1) * step), paint)
        }
    }
}

/**
 * Faint plank lines on wood floors, in the direction the robot stores (block 32: 0 runs along x, 90
 * along y), and a grid on tiles, as the official app draws them. Only cells of that room are touched.
 */
private fun drawFloorTextures(canvas: Canvas, map: ParsedMap, palette: MapPalette) {
    val raw = map.originalGrid ?: return
    if (map.segmentMaterials.isEmpty()) return
    val w = map.width
    val h = map.height
    val line = Paint().apply {
        color = palette.texture
        style = PaintingStyle.Stroke
        strokeWidth = 0.12f
        isAntiAlias = true
    }
    val plankCells = 4   // 20 cm boards
    val boardLengthCells = 24
    for (y in 0 until h) {
        val drawY = (h - 1 - y).toFloat()
        for (x in 0 until w) {
            val i = y * w + x
            if ((map.grid[i].toInt() and 0xff) != 128) continue
            val room = (raw[i].toInt() ushr 3) and 0x1f
            val material = map.segmentMaterials[room] ?: continue
            val alongY = map.segmentMaterialDirections[room] == 90
            when (material) {
                FloorMaterial.WOOD.id -> {
                    // Long seams between boards, and staggered end joints.
                    val across = if (alongY) x else y
                    val along = if (alongY) y else x
                    if (across % plankCells == 0) {
                        if (alongY) canvas.drawLine(Offset(x + 0f, drawY), Offset(x + 0f, drawY + 1f), line)
                        else canvas.drawLine(Offset(x + 0f, drawY + 1f), Offset(x + 1f, drawY + 1f), line)
                    }
                    if ((along + (across / plankCells) * 9) % boardLengthCells == 0) {
                        if (alongY) canvas.drawLine(Offset(x + 0f, drawY + 1f), Offset(x + 1f, drawY + 1f), line)
                        else canvas.drawLine(Offset(x + 0f, drawY), Offset(x + 0f, drawY + 1f), line)
                    }
                }
                FloorMaterial.TILE.id -> {
                    if (x % 12 == 0) canvas.drawLine(Offset(x + 0f, drawY), Offset(x + 0f, drawY + 1f), line)
                    if (y % 12 == 0) canvas.drawLine(Offset(x + 0f, drawY + 1f), Offset(x + 1f, drawY + 1f), line)
                }
            }
        }
    }
}

private fun drawZone(canvas: Canvas, map: ParsedMap, zone: MapZone, fill: Color, stroke: Color) {
    val path = Path()
    if (zone.isRoundCarpet) {
        val a = map.mmToCell(zone.minXmm, zone.minYmm)
        val b = map.mmToCell(zone.maxXmm, zone.maxYmm)
        path.addOval(Rect(minOf(a.x, b.x), minOf(a.y, b.y), maxOf(a.x, b.x), maxOf(a.y, b.y)))
    } else {
        val c0 = map.mmToCell(zone.x0, zone.y0)
        path.moveTo(c0.x, c0.y)
        val c1 = map.mmToCell(zone.x1, zone.y1); path.lineTo(c1.x, c1.y)
        val c2 = map.mmToCell(zone.x2, zone.y2); path.lineTo(c2.x, c2.y)
        val c3 = map.mmToCell(zone.x3, zone.y3); path.lineTo(c3.x, c3.y)
        path.close()
    }
    canvas.drawPath(path, Paint().apply { color = fill })
    canvas.drawPath(path, Paint().apply {
        color = stroke
        style = PaintingStyle.Stroke
        strokeWidth = 0.4f
    })
}

private fun drawWall(canvas: Canvas, map: ParsedMap, wall: VirtualWall) {
    val a = map.mmToCell(wall.x0, wall.y0)
    val b = map.mmToCell(wall.x1, wall.y1)
    canvas.drawLine(a, b, Paint().apply {
        color = WALL_STROKE
        strokeWidth = 0.7f
        strokeCap = StrokeCap.Round
    })
}

private fun drawPath(canvas: Canvas, map: ParsedMap, palette: MapPalette) {
    val gfxPath = Path()
    var started = false
    for (point in map.pathMm) {
        val (cellX, cellY) = map.mmToCell(point.x, point.y).let { it.x to it.y }
        if (cellX !in 0f..map.width.toFloat() || cellY !in 0f..map.height.toFloat()) {
            started = false
            continue
        }
        if (!started) {
            gfxPath.moveTo(cellX, cellY)
            started = true
        } else {
            gfxPath.lineTo(cellX, cellY)
        }
    }
    // About 15 mm wide, white and (in dark mode) see-through, as in the official app.
    val pathPaint = Paint().apply {
        color = palette.path
        style = PaintingStyle.Stroke
        strokeWidth = 0.3f
        strokeCap = StrokeCap.Round
        strokeJoin = androidx.compose.ui.graphics.StrokeJoin.Round
        isAntiAlias = true
    }
    canvas.drawPath(gfxPath, pathPaint)
}

private fun drawMarker(
    canvas: Canvas,
    map: ParsedMap,
    point: ParsedMapPoint,
    color: Color,
    ring: Color,
    square: Boolean,
    radius: Float = 4f,
) {

    val (cellX, cellY) = map.mmToCell(point.x, point.y).let { it.x to it.y }
    if (cellX !in 0f..map.width.toFloat() || cellY !in 0f..map.height.toFloat()) return
    val ringPaint = Paint().apply { this.color = ring }
    val fillPaint = Paint().apply { this.color = color }
    if (square) {
        canvas.drawRect(
            Rect(cellX - radius - 1f, cellY - radius - 1f, cellX + radius + 1f, cellY + radius + 1f),
            ringPaint,
        )
        canvas.drawRect(Rect(cellX - radius, cellY - radius, cellX + radius, cellY + radius), fillPaint)
    } else {
        canvas.drawCircle(Offset(cellX, cellY), radius + 1.5f, ringPaint)
        canvas.drawCircle(Offset(cellX, cellY), radius, fillPaint)
    }
}
