package com.kodraliu.localrock.ui

import androidx.compose.runtime.Composable

/** Talk-back is not built on iOS yet (the Swift peer offers receive-only audio). */
@Composable
actual fun rememberMicrophonePermissionRequest(onResult: (Boolean) -> Unit): () -> Unit = { onResult(false) }
