package com.kodraliu.localrock.ui.vacuum

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.Block
import androidx.compose.material.icons.filled.Cyclone
import androidx.compose.material.icons.filled.Tornado
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp
import com.kodraliu.localrock.shared.vacuum.MopRoute
import com.kodraliu.localrock.shared.vacuum.VacuumFanPower

/** Suction as the official app's mode sheet shows it: a moon for quiet, stronger swirls upwards. */
internal fun suctionIcon(fanPower: Int): ImageVector? = when (fanPower) {
    VacuumFanPower.OFF -> Icons.Default.Block
    VacuumFanPower.QUIET -> Icons.Default.Bedtime
    VacuumFanPower.BALANCED -> Icons.Default.Air
    VacuumFanPower.TURBO -> Icons.Default.Cyclone
    VacuumFanPower.MAX -> Icons.Default.Tornado
    else -> null
}

/** Route as the official app draws it: a back-and-forth line, denser for a more thorough route. */
internal fun routeIcon(route: Int): ImageVector? = when (route) {
    MopRoute.FAST -> serpentine(2)
    MopRoute.STANDARD -> serpentine(3)
    MopRoute.DEEP -> serpentine(4)
    MopRoute.DEEP_PLUS -> serpentine(5)
    else -> null
}

private val serpentines = HashMap<Int, ImageVector>()

/** A 24 dp line going up and down [lanes] times, like the official route icons. */
private fun serpentine(lanes: Int): ImageVector = serpentines.getOrPut(lanes) {
    ImageVector.Builder("Route$lanes", 24.dp, 24.dp, 24f, 24f).apply {
        path(
            fill = null,
            stroke = SolidColor(Color.Black),
            strokeLineWidth = 1.8f,
            strokeLineCap = StrokeCap.Round,
            strokeLineJoin = StrokeJoin.Round,
        ) {
            val left = 4f
            val right = 20f
            val step = (right - left) / (lanes - 1).coerceAtLeast(1)
            moveTo(left, 19f)
            for (i in 0 until lanes) {
                val x = left + i * step
                val up = i % 2 == 0
                lineTo(x, if (up) 7f else 17f)
                if (i < lanes - 1) {
                    // A rounded turn over to the next lane.
                    val nx = x + step
                    if (up) curveTo(x, 4f, nx, 4f, nx, 7f) else curveTo(x, 20f, nx, 20f, nx, 17f)
                }
            }
        }
    }.build()
}
