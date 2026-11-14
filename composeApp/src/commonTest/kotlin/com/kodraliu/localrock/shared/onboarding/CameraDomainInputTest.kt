package com.kodraliu.localrock.shared.onboarding

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class CameraDomainInputTest {
    @Test
    fun aShortHostnameGetsTheTrailingSlash() {
        assertEquals("rr.stomp.cloud/", cameraDomainInput("rr.stomp.cloud"))
        assertEquals("rr.stomp.cloud/", cameraDomainInput(" rr.stomp.cloud/ "))
        assertNull(cameraDomainInput(""))
    }

    @Test
    fun whatTheFirmwareWouldCutIsRefused() {
        assertFailsWith<IllegalArgumentException> { cameraDomainInput("api-roborock.stomp.cloud") }
        assertFailsWith<IllegalArgumentException> { cameraDomainInput("https://rr.example.com") }
        assertFailsWith<IllegalArgumentException> { cameraDomainInput("rr.x.cc:555") }
        assertFailsWith<IllegalArgumentException> { cameraDomainInput("nodot") }
    }
}
