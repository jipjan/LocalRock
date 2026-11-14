package com.kodraliu.localrock.shared.auth

import com.kodraliu.localrock.shared.http.ProdJson
import kotlinx.serialization.json.JsonElement
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class LoginResponseTest {

    private fun parse(body: String) =
        parseLoginResponse(ProdJson.decodeFromString<LoginEnvelope>(body))

    @Test
    fun new_connections_switched_off_is_named_not_a_serialization_error() {
        // What local_roborock_server answers (403) while the admin has new connections switched off.
        // Read as a session this used to fail with "Fields [uid, token, rruid, rriot] are required".
        val e = assertFailsWith<LoginException> {
            parse("""{"code":40301,"msg":"new_connections_disabled","data":{"reason":"new_connections_disabled","flow":"protocol_login"}}""")
        }
        assertEquals(40301, e.responseCode)
        assertEquals(LoginFailure.NEW_CONNECTIONS_DISABLED, e.failure)
    }

    @Test
    fun a_wrong_email_or_code_is_invalid_credentials() {
        // The server's 401 for a wrong email (invalid_login_email) or login code (invalid_login_pin).
        val e = assertFailsWith<LoginException> {
            parse("""{"code":2010,"msg":"invalid_credentials","data":{"reason":"invalid_login_pin","auth":"code"}}""")
        }
        assertEquals(LoginFailure.INVALID_CREDENTIALS, e.failure)
    }

    @Test
    fun other_refusals_keep_the_server_message() {
        val e = assertFailsWith<LoginException> {
            parse("""{"code":40023,"msg":"local_session_issue_failed","data":{"error":"boom"}}""")
        }
        assertEquals(LoginFailure.OTHER, e.failure)
        assertEquals("local_session_issue_failed", e.message)
    }

    @Test
    fun a_refusal_without_data_still_names_the_reason() {
        val e = assertFailsWith<LoginException> { parse("""{"code":2010,"msg":"invalid_credentials"}""") }
        assertEquals(LoginFailure.INVALID_CREDENTIALS, e.failure)
    }

    @Test
    fun an_ok_without_a_session_is_refused() {
        assertFailsWith<LoginException> { parse("""{"code":200,"msg":"success","data":null}""") }
    }

    @Test
    fun an_ok_answer_is_read_as_the_session() {
        val user = parse(
            """{"success":true,"code":200,"msg":"success","data":{"uid":42,"token":"t","rruid":"rr1",
            "rriot":{"u":"u","s":"s","h":"h","k":"k","r":{"r":"EU","a":"https://a","m":"ssl://m:8883","l":"https://l"}},
            "nickname":"me"}}"""
        )
        assertEquals(42L, user.uid)
        assertEquals("rr1", user.rruid)
        assertEquals("ssl://m:8883", user.rriot.r.m)
    }
}
