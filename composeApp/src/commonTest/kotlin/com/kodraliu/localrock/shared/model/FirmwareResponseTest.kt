package com.kodraliu.localrock.shared.model

import com.kodraliu.localrock.shared.http.ProdJson
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class FirmwareResponseTest {

    @Test
    fun the_servers_generic_answer_is_not_an_up_to_date_check() {
        // local_roborock_server has no OTA route; its catchall answers /ota/firmware/{duid}/updatev2 so.
        val body = """{"success":true,"code":200,"data":{"ok":true,"route":"/ota/firmware/abc/updatev2"},
            "result":{"ok":true,"route":"/ota/firmware/abc/updatev2"}}"""
        val info = ProdJson.decodeFromString<FirmwareOtaResponse>(body).result!!

        assertFalse(info.updatable)
        assertFalse(info.namesAVersion)
    }

    @Test
    fun an_answer_that_names_versions_is_a_real_check() {
        val body = """{"code":200,"msg":"success","result":{"version":"02.29.56","currentVersion":"02.27.04",
            "updatable":true,"desc":"Fixes","forceUpdate":false}}"""
        val info = ProdJson.decodeFromString<FirmwareOtaResponse>(body).result!!

        assertTrue(info.namesAVersion)
        assertTrue(info.updatable)
        assertEquals("02.29.56", info.version)
    }

    @Test
    fun up_to_date_from_a_real_check_still_names_the_installed_version() {
        val info = FirmwareUpdateInfo(currentVersion = "02.27.04", updatable = false)
        assertTrue(info.namesAVersion)
    }
}
