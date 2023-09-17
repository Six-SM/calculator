package org.ssm.api

import kotlinx.serialization.Serializable
import java.util.*

// TODO: replace type for timestamp
@Serializable
data class CalculationResponse(val result: String) {
    val id: String = UUID.randomUUID().toString()
    val timestamp: String = System.currentTimeMillis().toString()
}