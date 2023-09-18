package calculation

import com.github.keelar.exprk.ExpressionException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.ssm.server.service.CalculationService
import java.math.BigDecimal

class CalculationTest {
    @Test
    fun testCalculateSmoke() {
        assertEquals(
            Result.success(BigDecimal(0)),
            CalculationService.calculateResult("0"),
        )

        assertThrows(ExpressionException::class.java) {
            CalculationService.calculateResult("0+").getOrThrow()
        }


        assertThrows(ArithmeticException::class.java) {
            CalculationService.calculateResult("1 / 0").getOrThrow()
        }
    }

    @Test
    fun testCalculate() {
        assertEquals(
            Result.success(BigDecimal(123)),
            CalculationService.calculateResult("63 + 6*10"),
        )

        assertEquals(
            Result.success(BigDecimal(690)),
            CalculationService.calculateResult("(63 + 6)*10"),
        )

        assertEquals(
            Result.success(BigDecimal(-900)),
            CalculationService.calculateResult("-1000 + 100"),
        )

        assertEquals(
            Result.success(BigDecimal(0.25)),
            CalculationService.calculateResult("1 / 4"),
        )

        assertEquals(
            Result.success(BigDecimal("1000000000000000000000000000000")),
            CalculationService.calculateResult(
                "1000000 * 1000000 * 1000000 * 1000000 * 1000000"
            ),
        )
    }

    @Test
    fun testRounding() {
        val oneThird = CalculationService.calculateResult("1 / 3").getOrThrow()

        assertTrue(
            CalculationService.formatResponse(oneThird).startsWith("0.3333")
        )

        val million = CalculationService.calculateResult("1000000").getOrThrow()

        assertEquals(
            "1000000",
            CalculationService.formatResponse(million)
        )

        val millionPlusEps = CalculationService.calculateResult(
            "1000000 + 1 / (1000 * 1000 * 1000 * 1000)"
        ).getOrThrow()

        assertEquals(
            "1000000.0",
            CalculationService.formatResponse(millionPlusEps)
        )

        val oneTrillionth = CalculationService.calculateResult(
            "1 / (1000 * 1000 * 1000 * 1000)"
        ).getOrThrow()

        assertEquals(
            "1E-12",
            CalculationService.formatResponse(oneTrillionth)
        )

        val trillion = CalculationService.calculateResult(
            "1000 * 1000 * 1000 * 1000"
        ).getOrThrow()

        assertEquals(
            "1.0000000E+12",
            CalculationService.formatResponse(trillion)
        )
    }
}