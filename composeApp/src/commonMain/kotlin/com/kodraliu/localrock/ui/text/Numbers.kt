package com.kodraliu.localrock.ui.text

import androidx.compose.runtime.Composable
import com.kodraliu.localrock.resources.Res
import com.kodraliu.localrock.resources.number_decimal_separator
import org.jetbrains.compose.resources.stringResource
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

/** The app language's decimal separator: "." in English, "," in German and Dutch. */
@Composable
fun decimalSeparator(): String = stringResource(Res.string.number_decimal_separator)

/** [value] with one decimal, e.g. "12.5" or "12,5" with [separator]. */
fun oneDecimal(value: Float, separator: String): String {
    val scaled = (value * 10f).roundToInt()
    val sign = if (scaled < 0) "-" else ""
    val abs = scaled.absoluteValue
    return "$sign${abs / 10}$separator${abs % 10}"
}

/** An area in mm² as m² with one decimal (rounded down, as the robot's own app does), e.g. "21,2". */
fun areaTenths(mm2: Long, separator: String): String {
    val tenths = mm2 / 100_000
    return "${tenths / 10}$separator${tenths % 10}"
}
