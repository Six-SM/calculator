package org.ssm.app

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.ssm.api.CalculationListResponse
import org.ssm.api.CalculationRequest

class CalculatorClient(private val serverAddress: String) {
    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json()
        }
    }

    suspend fun calculate(expression: String): String? {
        val result = client.post("http://$serverAddress/api/calculate") {
            contentType(ContentType.Application.Json)
            setBody(CalculationRequest(expression))
        }
        if (result.status != HttpStatusCode.OK) {
            return null
        }
        return result.bodyAsText()
    }

    suspend fun getHistory(): CalculationListResponse? {
        val result = client.get("http://$serverAddress/api/history")
        if (result.status != HttpStatusCode.OK) {
            return null
        }
        return Json.decodeFromString<CalculationListResponse>(result.bodyAsText())
    }
}