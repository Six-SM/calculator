package api

import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.server.testing.*
import io.ktor.util.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.ssm.api.CalculationListResponse
import org.ssm.server.plugins.configureHTTP
import org.ssm.server.plugins.configureRouting
import org.ssm.server.plugins.configureSerialization
import org.ssm.server.plugins.json
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ApiTest {
    private fun decodeHistory(body: String): CalculationListResponse {
        return json.decodeFromString<CalculationListResponse>(body)
    }

    private val postgres: PostgreSQLContainer<*> = PostgreSQLContainer(DockerImageName.parse("postgres")).apply {
        withDatabaseName("x")
        withUsername("y")
        withPassword("z")
    }

    @BeforeAll
    fun setUp() {
        postgres.start();
    }

    @OptIn(InternalAPI::class)
    @Test
    fun testGeneral() = testApplication {
        application {
            configureHTTP()
            configureRouting(postgres.getJdbcUrl(), postgres.getUsername(), postgres.getPassword())
            configureSerialization()
        }

        client.get("/api/history").apply {
            assertEquals(HttpStatusCode.OK, status)
        }.apply {
            val decoded = decodeHistory(bodyAsText())
            assertEquals(0, decoded.history.size)
            assertEquals("{\"history\":[]}", bodyAsText())
        }

        client.post("/api/calculate") {
            contentType(ContentType.Application.Json)
            body = """{"expression":"1+1"}"""
        }.apply {
            assertEquals(HttpStatusCode.OK, status)
            assertEquals("2", bodyAsText())
        }

        client.get("/api/history").apply {
            assertEquals(HttpStatusCode.OK, status)
        }.apply {
            val decoded = decodeHistory(bodyAsText())
            assertEquals(1, decoded.history.size)
            assertEquals("1+1", decoded.history[0].expression)
            assertEquals("2", decoded.history[0].result)
        }

        client.post("/api/calculate") {
            contentType(ContentType.Application.Json)
            body = """{"expression":"1+2"}"""
        }.apply {
            assertEquals(HttpStatusCode.OK, status)


            assertEquals("3", bodyAsText())
        }

        client.get("/api/history").apply {
            assertEquals(HttpStatusCode.OK, status)
        }.apply {
            val decoded = decodeHistory(bodyAsText())
            assertEquals(2, decoded.history.size)
            assertEquals("1+1", decoded.history[1].expression)
            assertEquals("2", decoded.history[1].result)
            assertEquals("1+2", decoded.history[0].expression)
            assertEquals("3", decoded.history[0].result)
        }
    }
}
