package com.kodraliu.localrock.shared.vacuum

import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class VoicePacksTest {
    // One entry of the server's list for an a296, as served.
    private val list = Json.parseToJsonElement(
        """{"voice_pkg_version":1,"voice_list":[
          {"voice_id":129,"lang":"nl","support_voice_control":false,"voice_title":"Nederlands","version":4,
           "voice_pkg_url":"https://api-roborock.example.com/voice-pkg/vida/package/nl.pkg",
           "voice_pkg_md5":"71737e540fe282d245a03566db404ab8"},
          {"voice_id":3,"lang":"en","support_voice_control":true,"voice_title":"English","voice_sub_title":"","version":4,
           "voice_pkg_url":"https://api-roborock.example.com/voice-pkg/vida/package/en.pkg","voice_pkg_md5":"a4bf"},
          {"voice_id":9,"voice_title":"No package"}]}""",
    )

    @Test
    fun readsThePacksWithAPackage() {
        val packs = parseVoicePacks(list)
        assertEquals(listOf(129, 3), packs.map { it.id })
        assertEquals("Nederlands", packs[0].title)
        assertEquals(false, packs[0].voiceControl)
        assertEquals(true, packs[1].voiceControl)
        assertNull(packs[1].subTitle)
    }

    @Test
    fun installsAsTheOfficialPluginDoes() {
        assertEquals(
            """{"sid":129,"url":"https://api-roborock.example.com/voice-pkg/vida/package/nl.pkg","md5":"71737e540fe282d245a03566db404ab8","sver":4}""",
            installSoundParams(parseVoicePacks(list)[0]).toString(),
        )
    }

    @Test
    fun readsTheRobotsAnswers() {
        val current = parseCurrentSound(Json.parseToJsonElement(
            """[{"sid_in_use":129,"sid_version":4,"sid_in_progress":0,"location":"de","bom":"A.03.1105","language":"en","msg_ver":2}]""",
        ))!!
        assertEquals(129, current.inUse)
        assertEquals(4, current.inUseVersion)
        val progress = parseSoundProgress(Json.parseToJsonElement("""[{"sid_in_progress":0,"progress":100,"state":0,"error":0}]"""))!!
        assertEquals(100, progress.progress)
        assertEquals(0, progress.error)
    }

    @Test
    fun onlyKnownModelsHaveABucket() {
        assertEquals("vida", voicePackBucket("roborock.vacuum.a296"))
        assertNull(voicePackBucket("roborock.vacuum.a70"))
        assertNull(voicePackBucket(null))
    }
}
