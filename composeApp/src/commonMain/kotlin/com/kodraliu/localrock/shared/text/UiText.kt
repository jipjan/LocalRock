package com.kodraliu.localrock.shared.text

import org.jetbrains.compose.resources.PluralStringResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getPluralString
import org.jetbrains.compose.resources.getString

/**
 * A user-visible text made in the model layer (`shared/`), in a form the UI can translate.
 *
 * The model layer keeps producing English: every [UiText] carries [english], the exact text the
 * shared functions have always returned, so code that only needs English (unit tests, which run on
 * the JVM where Compose resources cannot be read, logs, notifications built in tests) reads that.
 * Next to it sits the string resource (from `composeResources/values*\/strings_model.xml`) with its
 * arguments, which the UI reads in the app's language:
 * - in composables with `UiText.text()` (ui/text/ModelText.kt), backed by `stringResource`;
 * - in suspend code with [resolve], backed by `getString`.
 *
 * Arguments may be numbers, strings (names, which stay as they are) or other [UiText]s, which are
 * resolved first. Shared functions that return a String keep doing so (as [english]) and have a
 * `...Text` counterpart that returns the [UiText]; exceptions with a user-facing message implement
 * [HasUiText]. `ModelTextTest` (androidUnitTest) checks every key against the three XML files and
 * that the English template with its arguments gives [english].
 */
sealed interface UiText {
    /** The text in English, as the model layer has always produced it. */
    val english: String

    /** A string resource and its arguments. */
    data class Res(val res: StringResource, val args: List<Any>, override val english: String) : UiText

    /** A plural string resource for [quantity], and its arguments. */
    data class Plural(
        val res: PluralStringResource,
        val quantity: Int,
        val args: List<Any>,
        override val english: String,
    ) : UiText

    /** Text that is the same in every language: a name, a number, something the robot said. */
    data class Raw(override val english: String) : UiText

    /** [parts] in a row with [separator] between them, such as "Vacuum and mop · ×2". */
    data class Joined(val parts: List<UiText>, val separator: String = " · ") : UiText {
        override val english: String get() = parts.joinToString(separator) { it.english }
    }
}

/** A string resource whose English reads [english]; [args] fill its placeholders in order. */
fun uiText(res: StringResource, english: String, vararg args: Any): UiText = UiText.Res(res, args.toList(), english)

/** A plural resource for [quantity] whose English reads [english]; [args] fill its placeholders. */
fun uiPlural(res: PluralStringResource, quantity: Int, english: String, vararg args: Any): UiText =
    UiText.Plural(res, quantity, args.toList(), english)

/** Text that needs no translation. */
fun rawText(text: String): UiText = UiText.Raw(text)

/** The non-null [parts] joined with " · ", or null when there are none. */
fun joinedText(parts: List<UiText?>, separator: String = " · "): UiText? =
    parts.filterNotNull().takeIf { it.isNotEmpty() }?.let { UiText.Joined(it, separator) }

/** The text in the app's language, for code outside composition (view models, notifications). */
suspend fun UiText.resolve(): String = when (this) {
    is UiText.Res -> getString(res, *resolveArgs(args))
    is UiText.Plural -> getPluralString(res, quantity, *resolveArgs(args))
    is UiText.Raw -> english
    is UiText.Joined -> parts.map { it.resolve() }.joinToString(separator)
}

private suspend fun resolveArgs(args: List<Any>): Array<Any> =
    args.map { if (it is UiText) it.resolve() else it }.toTypedArray()

/** An exception whose message is meant for the user, and can be shown in the app's language. */
interface HasUiText {
    val text: UiText
}

/** A failure with a user-facing message; its [message] is the English text. */
open class UiTextException(override val text: UiText, cause: Throwable? = null) :
    IllegalStateException(text.english, cause), HasUiText

/** Invalid input with a user-facing message; its [message] is the English text. */
class UiTextArgumentException(override val text: UiText) : IllegalArgumentException(text.english), HasUiText

/** The message of this exception in the app's language when it has one, else its plain message. */
suspend fun Throwable.localizedText(): String? = (this as? HasUiText)?.text?.resolve() ?: message
