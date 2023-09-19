package org.ssm.server

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import org.ssm.server.plugins.configureHTTP
import org.ssm.server.plugins.configureRouting
import org.ssm.server.plugins.configureSerialization

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    configureHTTP()
    configureSerialization()
    configureRouting("jdbc:postgresql://127.0.0.1:3071", "demo-user", "demo-password")
}
