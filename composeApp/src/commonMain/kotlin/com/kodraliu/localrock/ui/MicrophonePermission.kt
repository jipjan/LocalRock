package com.kodraliu.localrock.ui

import androidx.compose.runtime.Composable

/**
 * Returns a function that asks for the microphone (for talk-back in the live view) and reports whether
 * it may be used. Asks only when it is not granted yet.
 */
@Composable
expect fun rememberMicrophonePermissionRequest(onResult: (Boolean) -> Unit): () -> Unit
