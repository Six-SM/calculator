package org.ssm.api

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import io.ktor.util.*
import org.ssm.api.models.Calculation
import org.ssm.server.plugins.configureHTTP
import org.ssm.server.plugins.configureRouting
import org.ssm.server.plugins.configureSerialization
import org.ssm.server.plugins.json
import kotlin.test.Test
import kotlin.test.assertEquals

class ApiTest {
    private fun decodeHistory(body: String): List<Calculation> {
        return json.decodeFromString<List<Calculation>>(body)
    }

    @OptIn(InternalAPI::class)
    @Test
    fun testGeneral() = testApplication {
        application {
            configureHTTP()
            configureRouting()
            configureSerialization()
        }

        client.get("/api/history").apply {
            assertEquals(HttpStatusCode.OK, status)
        }.apply {
            val decoded = decodeHistory(bodyAsText())
            assertEquals(0, decoded.size)
            assertEquals("[]", bodyAsText())
        }

        client.post("/api/calculate") {
            contentType(ContentType.Application.Json)
            body = """{"expression":"1+1"}"""
        }.apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("1+1", bodyAsText())
        }

        client.get("/api/history").apply {
            assertEquals(HttpStatusCode.OK, status)
        }.apply {
            val decoded = decodeHistory(bodyAsText())
            assertEquals(1, decoded.size)
            assertEquals("1+1", decoded[0].expression)
            assertEquals("1+1", decoded[0].result)
        }

        client.post("/api/calculate") {
            contentType(ContentType.Application.Json)
            body = """{"expression":"1+2"}"""
        }.apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("1+2", bodyAsText())
        }

        client.get("/api/history").apply {
            assertEquals(HttpStatusCode.OK, status)
        }.apply {
            val decoded = decodeHistory(bodyAsText())
            assertEquals(2, decoded.size)
            assertEquals("1+1", decoded[0].expression)
            assertEquals("1+1", decoded[0].result)
            assertEquals("1+2", decoded[1].expression)
            assertEquals("1+2", decoded[1].result)
        }
    }
}
