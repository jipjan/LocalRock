package com.kodraliu.localrock.shared.vacuum

import kotlin.test.Test
import kotlin.test.assertEquals

class CarpetCleanModeParamsTest {
    @Test
    fun isABareObjectAsTheOfficialAppSendsIt() {
        // Captured from the official app on an a296; the wrapped form got -10005 "param is not an object".
        assertEquals("""{"carpet_clean_mode":3}""", carpetCleanModeParams(3).toString())
    }
}
