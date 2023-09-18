package org.ssm.app.handlers

import kotlin.math.max
import kotlin.math.min

fun cutExpression(expression: String): String {
    return expression.takeWhile { it != '=' }
}

fun handleEqualButton(calculatorState: CalculatorState): CalculatorState {
    // TODO: send request
    return CalculatorState("${cutExpression(calculatorState.expression)}=25", calculatorState.position)
}

fun handleLeftArrow(calculatorState: CalculatorState): CalculatorState {
    return CalculatorState(calculatorState.expression, max(calculatorState.position - 1, 0))
}

fun handleRightArrow(calculatorState: CalculatorState): CalculatorState {
    val equalSignPosition = calculatorState.expression.indexOf('=')
    if (equalSignPosition > -1 && calculatorState.position + 1 >= equalSignPosition)
        return calculatorState

    return CalculatorState(calculatorState.expression, min( calculatorState.position + 1, calculatorState.expression.length - 1))
}

fun handleSymbol(calculatorState: CalculatorState, symbol: Char): CalculatorState {
    val currentExpression = cutExpression(calculatorState.expression)
    val newExpression = if (calculatorState.position >= currentExpression.length - 1) {
        currentExpression + symbol
    } else {
        currentExpression.take(calculatorState.position + 1) + symbol + currentExpression.substring(calculatorState.position + 1)
    }
    return CalculatorState(newExpression, min(calculatorState.position + 1, newExpression.length - 1))
}

fun handleDeletion(calculatorState: CalculatorState): CalculatorState {
    if (calculatorState.expression.contains('=')) {
        val newExpression = cutExpression(calculatorState.expression)
        return CalculatorState(newExpression, min(calculatorState.position, newExpression.length - 1))
    }

    if (calculatorState.expression.isEmpty()) {
        return CalculatorState("", 0)
    }

    val newExpression = calculatorState.expression.take(calculatorState.position) + calculatorState.expression.substring(calculatorState.position + 1)
    return CalculatorState(newExpression, min(calculatorState.position, newExpression.length - 1))
}

fun updateRecentRequest(): List<String> {
    // TODO: send request
    return listOf(
        "1+1=2",
        "25*25=625",
        "167+78*(82+3)=6797",
        "1+=error"
    )
}