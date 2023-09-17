package org.ssm.server.plugins

import io.ktor.server.application.*
import io.ktor.server.plugins.compression.*

fun Application.configureHTTP() {
    install(Compression) {
        gzip()
    }
}
