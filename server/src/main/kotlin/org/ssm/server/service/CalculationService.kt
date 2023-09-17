package org.ssm.server.service

import org.ssm.api.CalculationResponse

object CalculationService {
    fun calculate(expression: String): CalculationResponse = CalculationResponse("error")
}