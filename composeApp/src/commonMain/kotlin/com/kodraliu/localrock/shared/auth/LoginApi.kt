package com.kodraliu.localrock.shared.auth

import com.kodraliu.localrock.shared.http.ProdJson
import com.kodraliu.localrock.shared.model.UserData
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonNull

@Serializable
private data class CodeLoginBody(val email: String, val code: String)

/** The login answer's envelope; unlike [com.kodraliu.localrock.shared.model.ApiResponse], a refusal may leave out `data` or `msg`. */
@Serializable
internal data class LoginEnvelope(val code: Int = 0, val msg: String = "", val data: JsonElement = JsonNull)

class LoginApi(private val client: HttpClient) {

    suspend fun login(email: String, code: String): UserData {
        // Read the envelope before the session: a refusal carries {reason, ...} in `data`, not a
        // session, and reading it as one hid the server's answer behind a serialization error.
        val response: LoginEnvelope = client.post("/api/v5/auth/email/login/code") {
            contentType(ContentType.Application.Json)
            setBody(CodeLoginBody(email, code))
        }.body()
        return parseLoginResponse(response)
    }
}

internal fun parseLoginResponse(response: LoginEnvelope): UserData {
    if (response.code != 200) throw LoginException(response.code, response.msg)
    if (response.data is JsonNull) throw LoginException(response.code, "The server sent no session")
    return ProdJson.decodeFromJsonElement(UserData.serializer(), response.data)
}

/** Why the server refused a sign-in, from the code and msg local_roborock_server answers with. */
enum class LoginFailure {
    /** The admin has switched new connections off (403, code 40301), so no new session is issued. */
    NEW_CONNECTIONS_DISABLED,

    /** The email or the login code does not match the server's (401, code 2010). */
    INVALID_CREDENTIALS,

    OTHER,
}

class LoginException(val responseCode: Int, message: String) : RuntimeException(message) {
    val failure: LoginFailure get() = loginFailureFor(responseCode, message.orEmpty())
}

internal fun loginFailureFor(code: Int, msg: String): LoginFailure = when {
    code == 40301 || msg == "new_connections_disabled" -> LoginFailure.NEW_CONNECTIONS_DISABLED
    code == 2010 || msg == "invalid_credentials" -> LoginFailure.INVALID_CREDENTIALS
    else -> LoginFailure.OTHER
}
