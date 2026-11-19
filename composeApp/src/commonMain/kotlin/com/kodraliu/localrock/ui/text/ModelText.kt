package com.kodraliu.localrock.ui.text

import androidx.compose.runtime.Composable
import com.kodraliu.localrock.shared.text.UiText
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource

/**
 * A model-layer [UiText] in the app's language, for composables. Suspend code uses
 * [com.kodraliu.localrock.shared.text.resolve] instead.
 */
@Composable
fun UiText.text(): String = when (this) {
    is UiText.Res -> stringResource(res, *args.map { if (it is UiText) it.text() else it }.toTypedArray())
    is UiText.Plural -> pluralStringResource(res, quantity, *args.map { if (it is UiText) it.text() else it }.toTypedArray())
    is UiText.Raw -> english
    is UiText.Joined -> parts.map { it.text() }.joinToString(separator)
}
