package com.kodraliu.localrock.shared.vacuum

import com.kodraliu.localrock.shared.http.ProdJson
import kotlin.test.Test
import kotlin.test.assertEquals

class CleanSequenceTypeTest {

    @Test
    fun vacuum_then_mop_matches_the_official_app() {
        // Captured from the official app on an a296 (Saros 20 Neo), firmware 02.32.54.
        assertEquals(
            """{"fan_power":101,"mop_mode":300,"repeat":1,"type":1,"water_box_mode":235}""",
            cleanSequenceTypeParams(SEQ_TYPE_VACUUM_THEN_MOP, 101, 235, 300, 1).toString(),
        )
    }

    @Test
    fun together_leaves_the_repeat_out() {
        assertEquals(
            """{"fan_power":102,"mop_mode":300,"type":0,"water_box_mode":202}""",
            cleanSequenceTypeParams(SEQ_TYPE_TOGETHER, 102, 202, 300, 2).toString(),
        )
    }

    @Test
    fun status_reports_the_sequence_and_option_bits() {
        val status = ProdJson.decodeFromString<VacuumStatus>("""{"state":8,"seq_type":1,"repeat":2,"switch_status":24}""")
        assertEquals(true, status.vacuumThenMop)
        assertEquals(2, status.repeat)
        assertEquals(24, status.switchStatus)
        assertEquals(null, ProdJson.decodeFromString<VacuumStatus>("""{"state":8}""").vacuumThenMop)
    }
}

class CarpetModeOptionsTest {
    @Test
    fun a296_gets_the_official_carpet_modes() {
        // Captured on an a296: avoid 0, vacuum only 3, vacuum and mop 2.
        val a296 = "0000000000199518CED77EFDA8E9BEDDDBFF8F7F7EFEFFFF"
        assertEquals(listOf(0, 3, 2), carpetModeOptions(a296).map { it.first })
        assertEquals(listOf(0, 1, 2), carpetModeOptions(null).map { it.first })
    }
}

class DryerStartTest {

    @Test
    fun a_scheduled_start_sends_status_2_and_a_daily_cron() {
        // Captured from the official app on an a296: {"cron":"0 17 * * *","on":{"dry_time":10800},"status":2}.
        val reported = kotlinx.serialization.json.Json.parseToJsonElement("""{"status":1,"on":{"dry_time":10800},"cron":""}""")
            as kotlinx.serialization.json.JsonObject
        val body = dryerStartBody(reported, DRY_START_SCHEDULED, 17, 0, null)
        assertEquals("""{"status":2,"on":{"dry_time":10800},"cron":"0 17 * * *"}""", body.toString())
        val after = dryerStartBody(body, DRY_START_AFTER_CLEANING, null, null, null)
        assertEquals("", (after["cron"] as kotlinx.serialization.json.JsonPrimitive).content)
        assertEquals(1, (after["status"] as kotlinx.serialization.json.JsonPrimitive).content.toInt())
    }

    @Test
    fun only_a_daily_cron_is_read_as_a_time() {
        assertEquals(17 to 0, parseDailyCron("0 17 * * *"))
        assertEquals(8 to 30, parseDailyCron(" 30 8 * * * "))
        assertEquals(null, parseDailyCron(""))
        assertEquals(null, parseDailyCron("0 17 * * 1"))
        assertEquals(null, parseDailyCron("61 17 * * *"))
    }
}

class MopRouteOptionsTest {
    @Test
    fun anA296OffersStandardDeepPlusAndFastButNotDeep() {
        // The a296's flags (server inventory, 2026-11-15); it refused Deep (301) with -10005 and took 300/303/304.
        val routes = mopRouteOptions("4499197267967999", "0000000000199518CED77EFDA8E9BEDDDBFF8F7F7EFEFFFF")
        assertEquals(setOf(MopRoute.STANDARD, MopRoute.DEEP_PLUS, MopRoute.FAST), routes)
    }

    @Test
    fun unknownFlagsOfferEveryRoute() {
        assertEquals(null, mopRouteOptions(null, null))
    }
}
