package org.ssm.api

import kotlinx.serialization.Serializable

@Serializable
data class CalculationListResponse(val history: List<CalculationHistoryRequest>) {

    // TODO: replace type for timestamp
    @Serializable
    data class CalculationHistoryRequest(
        val expression: String,
        val result: String,
        val id: String,
        val timestamp: String
    )
}