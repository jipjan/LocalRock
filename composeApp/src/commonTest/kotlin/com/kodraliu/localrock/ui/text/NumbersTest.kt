package com.kodraliu.localrock.ui.text

import kotlin.test.Test
import kotlin.test.assertEquals

class NumbersTest {
    @Test
    fun one_decimal_uses_the_languages_separator() {
        assertEquals("12.5", oneDecimal(12.46f, "."))
        assertEquals("12,5", oneDecimal(12.46f, ","))
        assertEquals("0,3", oneDecimal(0.25f, ","))
        assertEquals("-1,5", oneDecimal(-1.5f, ","))
    }

    @Test
    fun areas_are_shown_in_square_metres_with_one_decimal() {
        assertEquals("21,2", areaTenths(21_290_000, ","))
        assertEquals("3.6", areaTenths(3_650_000, "."))
        assertEquals("0,0", areaTenths(0, ","))
    }
}
