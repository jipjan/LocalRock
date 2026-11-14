package com.kodraliu.localrock.shared.vacuum

import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.booleanOrNull
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.intOrNull

/**
 * A voice the robot can speak with, from the list the official app reads. The server mirrors that list
 * (`/voice-pkg/<bucket>/info`) with every URL pointing at itself, so a robot without internet access
 * downloads the pack from the server.
 */
data class VoicePack(
    val id: Int,
    val lang: String,
    val title: String,
    val subTitle: String?,
    val version: Int,
    val packageUrl: String,
    val md5: String,
    /** The pack also understands spoken commands (the voice assistant). */
    val voiceControl: Boolean,
)

/** What the robot speaks now (`get_current_sound`) and any download in progress (`get_sound_progress`). */
data class VoicePackState(
    val inUse: Int? = null,
    val inUseVersion: Int? = null,
    /** The pack being installed, 0 or null when none. */
    val inProgress: Int? = null,
    val progress: Int? = null,
    val error: Int? = null,
)

/**
 * The folder Roborock keeps a product's voice packs in, by model. It is the product's code name in the
 * official device plugin (`bucket`, defaulting to the product): `vida` for the Saros 20 Neo (a295, a296).
 * Models not listed have no voice pack page until their folder is known.
 */
fun voicePackBucket(model: String?): String? = when (model?.substringAfterLast('.')) {
    "a295", "a296" -> "vida"
    else -> null
}

private fun JsonObject.int(key: String) = (this[key] as? JsonPrimitive)?.intOrNull
private fun JsonObject.str(key: String) = (this[key] as? JsonPrimitive)?.contentOrNull

/** The packs in the server's list, in the order Roborock gives them; entries without a package are left out. */
fun parseVoicePacks(list: JsonElement?): List<VoicePack> {
    val voices = ((list as? JsonObject)?.get("voice_list") as? JsonArray) ?: return emptyList()
    return voices.mapNotNull { item ->
        val v = item as? JsonObject ?: return@mapNotNull null
        VoicePack(
            id = v.int("voice_id") ?: return@mapNotNull null,
            lang = v.str("lang").orEmpty(),
            title = v.str("voice_title") ?: return@mapNotNull null,
            subTitle = v.str("voice_sub_title")?.takeIf { it.isNotBlank() },
            version = v.int("version") ?: 0,
            packageUrl = v.str("voice_pkg_url")?.takeIf { it.startsWith("https://") } ?: return@mapNotNull null,
            md5 = v.str("voice_pkg_md5") ?: return@mapNotNull null,
            voiceControl = (v["support_voice_control"] as? JsonPrimitive)?.booleanOrNull == true,
        )
    }
}

private fun firstObject(result: JsonElement?): JsonObject? =
    (if (result is JsonArray) result.firstOrNull() else result) as? JsonObject

/** Reads `get_current_sound`, e.g. `[{"sid_in_use":129,"sid_version":4,"sid_in_progress":0,...}]`. */
fun parseCurrentSound(result: JsonElement?): VoicePackState? = firstObject(result)?.let {
    VoicePackState(inUse = it.int("sid_in_use"), inUseVersion = it.int("sid_version"), inProgress = it.int("sid_in_progress"))
}

/** Reads `get_sound_progress`, e.g. `[{"sid_in_progress":0,"progress":100,"state":0,"error":0}]`. */
fun parseSoundProgress(result: JsonElement?): VoicePackState? = firstObject(result)?.let {
    VoicePackState(inProgress = it.int("sid_in_progress"), progress = it.int("progress"), error = it.int("error"))
}

/**
 * Parameters for `dnld_install_sound`, as the official a296 plugin builds them for robots without
 * new feature bit 149: `{"sid": voice_id, "url": ..., "md5": voice_pkg_md5, "sver": version}`.
 */
fun installSoundParams(pack: VoicePack): JsonObject = JsonObject(mapOf(
    "sid" to JsonPrimitive(pack.id),
    "url" to JsonPrimitive(pack.packageUrl),
    "md5" to JsonPrimitive(pack.md5),
    "sver" to JsonPrimitive(pack.version),
))
