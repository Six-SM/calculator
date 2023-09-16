package org.ssm.models

import org.ssm.service.calculate
import java.util.UUID

@kotlinx.serialization.Serializable
data class Calculation(val expression: String) {
    val result: String = calculate(expression)
    val id: String = UUID.randomUUID().toString()
    val timestamp: String = System.currentTimeMillis().toString()
}