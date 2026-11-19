package com.kodraliu.localrock.ui

import androidx.compose.runtime.Composable
import platform.AVFAudio.AVAudioSession
import platform.darwin.dispatch_async
import platform.darwin.dispatch_get_main_queue

/**
 * Asks for the microphone for talk-back in the live view. iOS shows its own prompt the first time
 * (text from NSMicrophoneUsageDescription) and answers at once afterwards. The answer comes back on
 * the main thread, where Compose state may be changed.
 */
// Not compiled on this Linux machine (no Xcode); verify with an Xcode build.
@Composable
actual fun rememberMicrophonePermissionRequest(onResult: (Boolean) -> Unit): () -> Unit = {
    AVAudioSession.sharedInstance().requestRecordPermission { granted ->
        dispatch_async(dispatch_get_main_queue()) { onResult(granted) }
    }
}
