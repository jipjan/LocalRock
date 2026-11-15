package com.kodraliu.localrock.shared.webrtc

/**
 * Cuts an offer down to the codecs the robots stream: H264 video (with its RTX entries) and Opus audio.
 *
 * The official app's offer to a Saros 20 (a296) carries only these; LocalRock's full offer (VP8, VP9,
 * AV1, H265, FEC, …) is accepted with `send_sdp_to_robot` → ok, but the robot never produces an
 * answer. A section with none of the kept codecs is left unchanged.
 */
fun keepRobotCodecs(sdp: String): String {
    val eol = if ("\r\n" in sdp) "\r\n" else "\n"
    val lines = sdp.split(eol)
    val out = ArrayList<String>(lines.size)
    var i = 0
    // Session-level lines up to the first m= line.
    while (i < lines.size && !lines[i].startsWith("m=")) out += lines[i++]
    while (i < lines.size) {
        val section = mutableListOf(lines[i++])
        while (i < lines.size && !lines[i].startsWith("m=")) section += lines[i++]
        out += trimSection(section)
    }
    return out.joinToString(eol)
}

private val RtpMap = Regex("^a=rtpmap:(\\d+) ([^/]+)/")
private val Apt = Regex("^a=fmtp:(\\d+) apt=(\\d+)")
private val PayloadLine = Regex("^a=(rtpmap|fmtp|rtcp-fb):(\\d+)[ ].*")

private fun trimSection(section: List<String>): List<String> {
    val mLine = section.first()
    val kind = mLine.removePrefix("m=").substringBefore(' ')
    val wanted = when (kind) {
        "video" -> "H264"
        "audio" -> "opus"
        else -> return section
    }
    val codecOf = section.mapNotNull { RtpMap.find(it) }.associate { it.groupValues[1] to it.groupValues[2] }
    val primary = codecOf.filterValues { it.equals(wanted, ignoreCase = true) }.keys
    if (primary.isEmpty()) return section
    val rtx = section.mapNotNull { Apt.find(it) }
        .filter { it.groupValues[2] in primary && codecOf[it.groupValues[1]].equals("rtx", ignoreCase = true) }
        .map { it.groupValues[1] }
    val keep = primary + rtx

    val parts = mLine.split(' ')
    val newM = (parts.take(3) + parts.drop(3).filter { it in keep }).joinToString(" ")
    return listOf(newM) + section.drop(1).filter { line ->
        val pt = PayloadLine.find(line)?.groupValues?.get(2)
        pt == null || pt in keep
    }
}
