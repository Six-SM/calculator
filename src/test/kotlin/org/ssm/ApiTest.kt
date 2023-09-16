package org.ssm

import org.ssm.models.Calculation
import org.ssm.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import io.ktor.util.*
import org.junit.FixMethodOrder
import kotlin.test.*

@FixMethodOrder(org.junit.runners.MethodSorters.NAME_ASCENDING)
class ApiTest {
    @Test
    fun test1EmptyHistory() = testApplication {
        application {
            configureHTTP()
            configureRouting()
            configureSerialization()
        }
        client.get("/api/history").apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("[]", bodyAsText())
        }
    }

    fun decodeHistory(body: String): List<Calculation> {
        return json.decodeFromString<List<Calculation>>(body)
    }

    @OptIn(InternalAPI::class)
    @Test
    fun test2Calculate() = testApplication {
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
