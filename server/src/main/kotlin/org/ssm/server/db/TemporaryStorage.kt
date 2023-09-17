package org.ssm.server.db

import org.ssm.api.CalculationListResponse
import org.ssm.api.CalculationRequest
import org.ssm.api.CalculationResponse

object TemporaryStorage {
    private val temporaryStorage = mutableListOf<CalculationListResponse.CalculationHistoryRequest>()

    fun save(request: CalculationRequest, response: CalculationResponse): Boolean =
        temporaryStorage.add(CalculationListResponse.CalculationHistoryRequest(
            request.expression,
            response.result,
            response.id,
            response.timestamp
        ))

    fun getHistory(): CalculationListResponse = CalculationListResponse(temporaryStorage.sortedBy { it.timestamp })
}