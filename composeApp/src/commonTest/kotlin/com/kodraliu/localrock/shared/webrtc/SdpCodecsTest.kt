package com.kodraliu.localrock.shared.webrtc

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SdpCodecsTest {
    private val offer = listOf(
        "v=0", "o=- 1 2 IN IP4 127.0.0.1", "s=-", "t=0 0", "a=group:BUNDLE 0 1",
        "m=audio 9 UDP/TLS/RTP/SAVPF 111 63 9 0",
        "a=mid:0", "a=recvonly",
        "a=rtpmap:111 opus/48000/2", "a=rtcp-fb:111 transport-cc", "a=fmtp:111 minptime=10;useinbandfec=1",
        "a=rtpmap:63 red/48000/2", "a=fmtp:63 111/111", "a=rtpmap:9 G722/8000", "a=rtpmap:0 PCMU/8000",
        "m=video 9 UDP/TLS/RTP/SAVPF 96 97 127 103 104 105",
        "a=mid:1", "a=recvonly",
        "a=rtpmap:96 VP8/90000", "a=rtcp-fb:96 nack", "a=rtpmap:97 rtx/90000", "a=fmtp:97 apt=96",
        "a=rtpmap:127 H264/90000", "a=rtcp-fb:127 nack pli",
        "a=fmtp:127 level-asymmetry-allowed=1;packetization-mode=1;profile-level-id=42e01f",
        "a=rtpmap:103 rtx/90000", "a=fmtp:103 apt=127",
        "a=rtpmap:104 H265/90000", "a=rtpmap:105 rtx/90000", "a=fmtp:105 apt=104",
        "",
    ).joinToString("\r\n")

    @Test
    fun keepsOnlyH264AndOpus() {
        val out = keepRobotCodecs(offer).split("\r\n")
        assertTrue("m=audio 9 UDP/TLS/RTP/SAVPF 111" in out)
        assertTrue("m=video 9 UDP/TLS/RTP/SAVPF 127 103" in out)
        assertTrue("a=fmtp:103 apt=127" in out)
        assertTrue("a=fmtp:111 minptime=10;useinbandfec=1" in out)
        assertFalse(out.any { "VP8" in it || "H265" in it || "G722" in it || "apt=96" in it })
        assertTrue("a=mid:1" in out && "a=group:BUNDLE 0 1" in out)
        assertEquals("", out.last())
    }

    @Test
    fun leavesASectionWithoutTheCodecAlone() {
        val vp8Only = "v=0\r\nm=video 9 UDP/TLS/RTP/SAVPF 96\r\na=rtpmap:96 VP8/90000\r\n"
        assertEquals(vp8Only, keepRobotCodecs(vp8Only))
    }
}
