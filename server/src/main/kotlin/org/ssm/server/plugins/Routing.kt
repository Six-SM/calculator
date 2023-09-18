package org.ssm.server.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.encodeToString
import org.ssm.api.CalculationRequest
import org.ssm.server.db.TemporaryStorage
import org.ssm.server.service.CalculationService

fun Application.configureRouting(url: String, username: String, password: String) {
    routing {
        // Returns the calculation history in the format [{expr1, result1, id1, time1}, {expr2, result2, id2, time2}, ...]
        get("/api/history") {
            val response = json.encodeToString(TemporaryStorage(url, username, password).getHistory())
            call.respond(response)
        }

        // Returns the result of the calculation as a string
        post("/api/calculate") {
            val calculationRequest = call.receive<CalculationRequest>()
            val calculationResponse = CalculationService.calculate(calculationRequest.expression)
            TemporaryStorage(url, username, password).save(calculationRequest, calculationResponse)
            call.respond(HttpStatusCode.OK, calculationResponse.result)
        }
    }
}
