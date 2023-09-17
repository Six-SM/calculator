package org.ssm.api

import kotlinx.serialization.Serializable

@Serializable
data class CalculationRequest(val expression: String) { }