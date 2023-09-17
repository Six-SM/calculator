package org.ssm.api.models

import org.ssm.server.service.CalculationService
import java.util.*

@kotlinx.serialization.Serializable
data class Calculation(val expression: String) {
    val result: String = CalculationService.calculate(expression)
    val id: String = UUID.randomUUID().toString()
    val timestamp: String = System.currentTimeMillis().toString()
}