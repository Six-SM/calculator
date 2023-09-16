package org.ssm.plugins

import org.ssm.db.*
import org.ssm.models.*
import org.ssm.service.*
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.encodeToString

fun Application.configureRouting() {
    routing {
        // GET /api/history
        // Returns the calculation history in the format [{expr1, id1, time1, result1}, {expr2, id2, time2, result2}, ...]
        get("/api/history" ) {
            val response = json.encodeToString(getHistory())
            call.respond(response)
        }

        // POST /api/calculate
        // Returns the result of the calculation as a string
        post("/api/calculate") {
            val calculation = call.receive<Calculation>()
            save(calculation)
            call.respond(HttpStatusCode.OK, calculation.result)
        }
    }
}
