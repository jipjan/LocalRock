package com.kodraliu.localrock.shared.text

import com.kodraliu.localrock.shared.notify.RobotEvent
import com.kodraliu.localrock.shared.notify.notificationBody
import com.kodraliu.localrock.shared.vacuum.BetaFeatureDefs
import com.kodraliu.localrock.shared.vacuum.CleanEvent
import com.kodraliu.localrock.shared.vacuum.CleanRecord
import com.kodraliu.localrock.shared.vacuum.CleanRunSettings
import com.kodraliu.localrock.shared.vacuum.ConsumableStatus
import com.kodraliu.localrock.shared.vacuum.DockCapabilities
import com.kodraliu.localrock.shared.vacuum.DockErrorCodes
import com.kodraliu.localrock.shared.vacuum.DockOptions
import com.kodraliu.localrock.shared.vacuum.RoomType
import com.kodraliu.localrock.shared.vacuum.RoutineStep
import com.kodraliu.localrock.shared.vacuum.RoutineStepKind
import com.kodraliu.localrock.shared.vacuum.SmartWashSettings
import com.kodraliu.localrock.shared.vacuum.VacuumErrorCodes
import com.kodraliu.localrock.shared.vacuum.VacuumStateCodes
import com.kodraliu.localrock.shared.vacuum.WaterBoxMode
import com.kodraliu.localrock.shared.vacuum.ZoneRect
import com.kodraliu.localrock.shared.vacuum.carpetModeOptions
import com.kodraliu.localrock.shared.vacuum.describeCleanEvents
import com.kodraliu.localrock.shared.vacuum.dryTimeText
import com.kodraliu.localrock.shared.vacuum.map.MapObstacle
import com.kodraliu.localrock.shared.vacuum.mapIndexBlockerText
import com.kodraliu.localrock.shared.vacuum.mapNameBlockerText
import com.kodraliu.localrock.shared.vacuum.stepTitleText
import com.kodraliu.localrock.shared.vacuum.summaryText
import com.kodraliu.localrock.shared.vacuum.textFor
import com.kodraliu.localrock.shared.vacuum.volumeText
import com.kodraliu.localrock.shared.vacuum.washFrequencyText
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

/**
 * The model layer's [UiText]s against `strings_model.xml`, read as files (Compose resources cannot be
 * read on the JVM): every key exists in English, German and Dutch with the same placeholders, and the
 * English template filled with a text's arguments gives exactly the English the shared code returns.
 */
class ModelTextTest {
    private val resources = File("src/commonMain/composeResources")

    /** key → (quantity → text); plain strings have the single quantity "". */
    private fun read(dir: String, file: String = "strings_model.xml"): Map<String, Map<String, String>> {
        val doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(File(resources, "$dir/$file"))
        val out = linkedMapOf<String, Map<String, String>>()
        val strings = doc.getElementsByTagName("string")
        for (i in 0 until strings.length) {
            val e = strings.item(i) as org.w3c.dom.Element
            out[e.getAttribute("name")] = mapOf("" to e.textContent)
        }
        val plurals = doc.getElementsByTagName("plurals")
        for (i in 0 until plurals.length) {
            val e = plurals.item(i) as org.w3c.dom.Element
            val items = e.getElementsByTagName("item")
            out[e.getAttribute("name")] = (0 until items.length).associate {
                val item = items.item(it) as org.w3c.dom.Element
                item.getAttribute("quantity") to item.textContent
            }
        }
        return out
    }

    private val english by lazy { read("values") }
    private val placeholder = Regex("""%(\d+)\$[ds]""")

    private fun fill(template: String, args: List<Any>): String =
        placeholder.replace(template) { m ->
            val arg = args[m.groupValues[1].toInt() - 1]
            if (arg is UiText) arg.english else arg.toString()
        }

    /** Checks [text] and the texts in its arguments against the English file. */
    private fun check(text: UiText?) {
        when (text) {
            null, is UiText.Raw -> Unit
            is UiText.Joined -> text.parts.forEach(::check)
            is UiText.Res -> {
                val template = english[text.res.key]?.get("") ?: fail("No English string ${text.res.key}")
                assertEquals(text.english, fill(template, text.args), "English of ${text.res.key}")
                text.args.filterIsInstance<UiText>().forEach(::check)
            }
            is UiText.Plural -> {
                val forms = english[text.res.key] ?: fail("No English plurals ${text.res.key}")
                val template = forms[if (text.quantity == 1) "one" else "other"] ?: fail("No form for ${text.quantity} in ${text.res.key}")
                assertEquals(text.english, fill(template, text.args), "English of ${text.res.key}")
            }
        }
    }

    @Test
    fun the_three_languages_have_the_same_keys_and_placeholders() {
        for (lang in listOf("values-de", "values-nl")) {
            val other = read(lang)
            assertEquals(english.keys, other.keys, "keys of $lang")
            for ((key, forms) in english) {
                assertEquals(forms.keys, other.getValue(key).keys, "plural forms of $key in $lang")
                for ((quantity, text) in forms) {
                    val translated = other.getValue(key).getValue(quantity)
                    assertEquals(
                        placeholder.findAll(text).map { it.value }.toSet(),
                        placeholder.findAll(translated).map { it.value }.toSet(),
                        "placeholders of $key in $lang",
                    )
                    assertTrue(translated.isNotBlank(), "$key is empty in $lang")
                }
            }
        }
    }

    @Test
    fun keys_do_not_collide_with_the_other_string_files_and_quotes_are_plain() {
        for (dir in listOf("values", "values-de", "values-nl")) {
            val others = File(resources, dir).listFiles()!!.filter { it.name != "strings_model.xml" }.flatMap { read(dir, it.name).keys }
            val clash = english.keys.intersect(others.toSet())
            assertTrue(clash.isEmpty(), "keys in $dir/strings_model.xml also in another file: $clash")
            val raw = File(resources, "$dir/strings_model.xml").readText()
            assertTrue("\\'" !in raw && "\\\"" !in raw, "escaped quote in $dir/strings_model.xml")
        }
    }

    /** Every `uiText(Res.string.key, "literal")` in the code reads as its English string. */
    @Test
    fun literal_english_in_the_code_matches_the_resources() {
        val call = Regex("""uiText\(\s*Res\.string\.(\w+),\s*"((?:[^"\\$]|\\.)*)"\s*[,)]""")
        var seen = 0
        File("src/commonMain/kotlin").walkTopDown().filter { it.extension == "kt" }.forEach { file ->
            for (m in call.findAll(file.readText())) {
                val key = m.groupValues[1]
                val template = english[key]?.get("") ?: fail("${file.name}: no English string $key")
                if (placeholder.containsMatchIn(template)) continue
                assertEquals(template, m.groupValues[2].replace("\\\"", "\""), "${file.name}: $key")
                seen++
            }
        }
        assertTrue(seen > 200, "only $seen literal texts found")
    }

    @Test
    fun robot_states_and_errors() {
        (0..120).forEach { check(VacuumStateCodes.text(it)) }
        (0..60).forEach { check(VacuumErrorCodes.text(it)) }
        (0..60).forEach { check(DockErrorCodes.text(it)) }
        assertEquals("Washing mop", VacuumStateCodes.text(VacuumStateCodes.WASHING_MOP_2).english)
        assertEquals("State 99", VacuumStateCodes.text(99).english)
    }

    @Test
    fun every_room_type_and_feature_has_its_own_text() {
        RoomType.entries.forEach { check(it.text); assertEquals(it.label, it.text.english) }
        assertEquals(RoomType.entries.size, RoomType.entries.map { (it.text as UiText.Res).res.key }.toSet().size)
        for (def in BetaFeatureDefs) {
            check(def.title); check(def.description)
            def.choices?.forEach { check(it.second) }
            assertEquals("feature_${def.id}_title", (def.title as UiText.Res).res.key)
            assertEquals("feature_${def.id}_desc", (def.description as UiText.Res).res.key)
        }
    }

    @Test
    fun clean_records_timeline_and_run_settings() {
        for (s in 0..6) for (w in 0..40) for (r in 0..2) {
            val run = CleanRunSettings(s, w, r, 1)
            listOf(run.modeText, run.suctionText, run.waterText, run.routeText).forEach(::check)
        }
        (200..255).forEach { check(WaterBoxMode.slideText(it)) }
        (0..7).forEach { check(CleanRecord(0, 0, 0, 0, 0, 1, cleanType = it).typeText) }
        for (n in 0..3) check(CleanRecord(0, 0, 0, 0, 0, 1, avoidCount = n, washCount = n, dustCollected = n > 0, passes = n).detailText)

        fun ev(i: Int, e: String) = CleanEvent(1000L + i, i, Json.parseToJsonElement(e) as JsonObject)
        val events = listOf(
            ev(25, """{"251":2,"252":1,"3":1,"4":1}"""), ev(26, """{}"""),
            CleanEvent(2000, 25, Json.parseToJsonElement("""{"251":1,"252":2,"3":0,"4":2}""") as JsonObject),
            ev(42, """{"2":1,"3":1,"4":1,"6":2}"""), ev(43, """{"2":7,"421":3}"""),
            CleanEvent(3000, 42, Json.parseToJsonElement("""{"2":9,"425":1}""") as JsonObject),
            ev(52, """{"521":10,"522":2}"""), ev(53, "{}"), ev(31, """{"311":38}"""), ev(22, """{"3":5}"""),
            ev(13, """{"131":14}"""), ev(54, """{"541":1}"""), ev(21, """{"211":1}"""), ev(12, "{}"),
            CleanEvent(5000, 23, Json.parseToJsonElement("""{"231":2}""") as JsonObject),
            ev(11, """{"111":5,"112":10}"""),
        )
        val lines = describeCleanEvents(events) { if (it == 1) "Kitchen" else null }
        assertTrue(lines.size >= 14, "only ${lines.size} lines")
        lines.forEach { check(it.titleText); check(it.detailText) }
    }

    @Test
    fun dock_options_and_robot_settings() {
        for (bits in listOf(null, "ffffffffffffffffffffffffffffffffffffffff")) {
            val dock = DockCapabilities("roborock.vacuum.a26", "33554432", bits)
            dock.washFrequencyOptions.forEach { check(it.text); assertEquals(it.label, it.text.english) }
            dock.autoEmptyModes.forEach { check(it.second) }
            for (type in listOf(null, 2, 17, 41)) for (dss in listOf(null, 2)) dock.washModes(type, dss).forEach { check(it.second) }
            (listOf(null) + (0..12)).forEach { check(dock.washModeText(it, 17)); check(dock.autoEmptyModeText(it)) }
            carpetModeOptions(bits).forEach { check(it.second) }
        }
        (0..3).forEach { check(DockOptions.washWaterTemperature.textFor(it)) }
        DockOptions.dryTime.forEach { check(it.text) }
        listOf(45, 5400, 7200).forEach { check(dryTimeText(it)) }
        listOf(0, 1, 45, 100, 150).forEach { check(volumeText(it)) }
        listOf(null, SmartWashSettings(0, 900), SmartWashSettings(0, 1250), SmartWashSettings(1, 0), SmartWashSettings(2, 0), SmartWashSettings(7, 0))
            .forEach { check(washFrequencyText(it)) }
        val hour = 3600L
        listOf(
            ConsumableStatus(mainBrushWorkTime = 2 * hour), ConsumableStatus(sideBrushWorkTime = 195 * hour),
            ConsumableStatus(filterWorkTime = 150 * hour), ConsumableStatus(sensorDirtyTime = 31 * hour),
            ConsumableStatus(mainBrushWorkTime = 300 * hour), ConsumableStatus(sideBrushWorkTime = 200 * hour),
        ).forEach { check(it.summaryText()) }
    }

    @Test
    fun map_obstacles_blockers_steps_and_notifications() {
        (0..60).forEach { check(MapObstacle(0, 0, it, null, null, false).typeText) }
        check(mapIndexBlockerText(null))
        listOf("", "Kælder").forEach { check(mapNameBlockerText(it)) }
        val zone = ZoneRect(0, 0, 10, 10)
        listOf(
            RoutineStep(RoutineStepKind.FULL), RoutineStep(RoutineStepKind.ROOMS), RoutineStep(RoutineStepKind.ROOMS, rooms = listOf(1, 2)),
            RoutineStep(RoutineStepKind.ZONES, zones = listOf(zone)), RoutineStep(RoutineStepKind.ZONES, zones = listOf(zone, zone)),
        ).forEach { check(stepTitleText(it) { id -> if (id == 1) "Kitchen" else null }) }
        listOf(
            RobotEvent.Error(8), RobotEvent.DockAlert(38), RobotEvent.CleanFinished(42_460_000L, 3_290L),
            RobotEvent.CleanFinished(null, 600), RobotEvent.CleanFinished(1_000_000, null), RobotEvent.CleanFinished(null, null),
        ).forEach { check(it.notificationBody()) }
    }
}
