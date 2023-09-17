package org.ssm.server.service

import com.github.keelar.exprk.Expressions
import org.ssm.api.CalculationResponse
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

object CalculationService {
    private val calculator: Expressions = Expressions()
        .setPrecision(256) // chosen arbitrary, should be enough
        .setRoundingMode(RoundingMode.HALF_EVEN)

    private val roundingMathContext: MathContext =
        MathContext(8, RoundingMode.HALF_EVEN)

    /**
     * Evaluates the expression and represents result as [Result]. Exists
     * separately from [calculator] for testing purposes.
     *
     * @param expression the expression to evaluate
     *
     * @return [Result], the value is success if the expression was evaluated,
     *      or failure with thrown error otherwise.
     */
    internal fun calculateResult(expression: String): Result<BigDecimal> {
        return try {
            Result.success(calculator.eval(expression))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Formats [BigDecimal] number, rounded to an appropriate precision to be
     * returned by the API.
     *
     * @param value the value to round and format
     * @return rounded number, formatted to a string
     */
    internal fun formatResponse(value: BigDecimal): String {
        return value.round(roundingMathContext).toString()
    }

    /**
     * Evaluates the expression and represents result as [CalculationResponse],
     * ready to be returned by the API.
     *
     * @param expression the expression to evaluate
     *
     * @return the response of the request
     */
    fun calculate(expression: String): CalculationResponse {
        val result = calculateResult(expression)

        return if (result.isSuccess) {
            CalculationResponse(formatResponse(result.getOrThrow()))
        } else {
            CalculationResponse("error")
        }
    }
}