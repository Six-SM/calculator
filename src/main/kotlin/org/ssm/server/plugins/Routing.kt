package org.ssm.server.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.encodeToString
import org.ssm.api.models.Calculation
import org.ssm.server.db.TemporaryStorage

fun Application.configureRouting() {
    routing {
        // Returns the calculation history in the format [{expr1, result1, id1, time1}, {expr2, result2, id2, time2}, ...]
        get("/api/history" ) {
            val response = json.encodeToString(TemporaryStorage.getHistory())
            call.respond(response)
        }

        // Returns the result of the calculation as a string
        post("/api/calculate") {
            val calculation = call.receive<Calculation>()
            TemporaryStorage.save(calculation)
            call.respond(HttpStatusCode.OK, calculation.result)
        }
    }
}
