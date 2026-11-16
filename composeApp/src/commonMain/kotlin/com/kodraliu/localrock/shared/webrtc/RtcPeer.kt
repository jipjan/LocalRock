package com.kodraliu.localrock.shared.webrtc

import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

data class RtcIceServer(
    val url: String,
    val username: String = "",
    val password: String = "",
)

data class RtcIceCandidate(
    val sdpMid: String?,
    val sdpMLineIndex: Int,
    val candidate: String,
)

enum class RtcConnectionState { NEW, CONNECTING, CONNECTED, DISCONNECTED, FAILED, CLOSED }


interface RtcPeer {
    val localCandidates: SharedFlow<RtcIceCandidate>
    val connectionState: StateFlow<RtcConnectionState>

    suspend fun createOffer(): String
    suspend fun setRemoteAnswer(sdp: String)
    fun addRemoteCandidate(candidate: RtcIceCandidate)
    fun close()

    /**
     * Sends the microphone over the offer's two-way audio (talk-back), or stops sending it. Returns
     * false where talk-back is not built.
     */
    fun setMicrophone(on: Boolean): Boolean = false
}

expect val liveViewSupported: Boolean

expect fun createRtcPeer(iceServers: List<RtcIceServer>): RtcPeer
