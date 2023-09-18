package org.ssm.app

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import org.ssm.app.handlers.*
import java.awt.Dimension
import kotlin.math.max

val symbols = listOf(
    "1", "2", "3", "⌫", "=",
    "4", "5", "6", "+", "-",
    "7", "8", "9", "*", "/",
    "0", "(", ")", "<-", "->"
)

val operationSymbols = listOf('+', '-', '/', '*', ')', '(', '=')

var recentRequests = mutableStateListOf(
    "MADE BY SIX SCRUM-MASTERS"
)

@Composable
fun drawTextOnButtons(it: Int) {
    return Text(
        text = symbols[it],
        color = Color.Black,
        style = if (!(symbols[it].all { char -> char.isDigit() })) {
            TextStyle(fontSize = 23.sp)
        } else {
            TextStyle(fontSize = 20.sp)
        },
        textAlign = if (symbols[it] == "⌫") {
            TextAlign.Start
        } else {
            TextAlign.Center
        }
    )
}


@Composable
fun runMain(client: CalculatorClient) {
    var currentExpression by remember { mutableStateOf("") }
    var currentPosition by remember { mutableStateOf(0) }

    Row {
        Column(modifier = Modifier.width(COLUMN_WIDTH.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                buildAnnotatedString {
                    append(currentExpression.take(currentPosition))

                    if (currentPosition < currentExpression.length) {
                        withStyle(style = SpanStyle(color = Color(0xFFf59563))) {
                            append(currentExpression[currentPosition])
                        }
                    }
                    if (currentPosition < currentExpression.length - 1) {
                        append(currentExpression.substring(currentPosition + 1, currentExpression.length))
                    }
                },
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .width(COLUMN_WIDTH.dp)
                    .absolutePadding(LEFT_PADDING.dp, 100.dp, 10.dp, 0.dp)
                    .background(Color(0xFFF3E8D3))
                    .padding(LEFT_PADDING.dp).height(200.dp),
                fontSize = 25.sp,
                fontWeight = FontWeight.Bold
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(5),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .absolutePadding(LEFT_PADDING.dp, 70.dp, 10.dp, 30.dp)
            ) {
                items(symbols.size) {
                    Button(
                        onClick = {
                            val newCalculatorState = if (symbols[it] == "=") {
                                    val newCalculatorState = handleEqualButton(CalculatorState(currentExpression, currentPosition), client)
                                    recentRequests.add(0, newCalculatorState.expression)
                                    newCalculatorState
                                } else if (symbols[it][0].isDigit() || (operationSymbols.contains(symbols[it][0]) && symbols[it].length == 1)) {
                                    handleSymbol(CalculatorState(currentExpression, currentPosition), symbols[it][0])
                                } else if (symbols[it] == "<-") {
                                    handleLeftArrow(CalculatorState(currentExpression, currentPosition))
                                } else if (symbols[it] == "->") {
                                    handleRightArrow(CalculatorState(currentExpression, currentPosition))
                                } else if (symbols[it] == "⌫") {
                                    handleDeletion(CalculatorState(currentExpression, currentPosition))
                                } else {
                                    // Should not happen
                                    CalculatorState(currentExpression, currentPosition)
                                }
                            currentExpression = newCalculatorState.expression
                            currentPosition = newCalculatorState.position

                            if (currentExpression.isEmpty()) {
                                currentPosition = 0
                            }
                        },


                        modifier = Modifier.size(BUTTON_CALC_SIZE.dp),
                        colors = if (it % 5 < 3) {
                            ButtonDefaults.buttonColors(Color(0xFFEBD7B2))
                        } else {
                            ButtonDefaults.buttonColors(Color(0xFFF2B179))
                        },
                        shape = RoundedCornerShape(CornerSize(10))

                    )
                    {
                        drawTextOnButtons(it)
                    }
                }
            }
        }

        Column(modifier = Modifier.width(500.dp), horizontalAlignment = Alignment.CenterHorizontally) {

            LazyVerticalGrid(
                columns = GridCells.Fixed(1),
                horizontalArrangement = Arrangement.spacedBy(20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
                modifier = Modifier.width(500.dp).height(610.dp).absolutePadding(left = 30.dp, right = 30.dp, top = 90.dp, bottom = 0.dp),
                content = {
                    items(recentRequests.size) {
                        Button(
                            onClick = {
                                currentExpression = recentRequests[it]
                                currentPosition = max(currentExpression.indexOf('=') - 1, 0)
                            },
                            modifier = Modifier
                                .width(200.dp)
                                .absolutePadding(30.dp, 10.dp, 10.dp, 0.dp)
                                .background(Color(0xFFF3E8D3))
                                .height(BUTTON_CALC_SIZE.dp),
                            colors = ButtonDefaults.buttonColors(Color(0xFFF3E8D3)),
                        ) {
                            Text(
                                recentRequests[it],
                                fontSize = 23.sp,
                                color = Color(0xFF776E65)
                            )
                        }
                    }

                }
            )

            Button(
                onClick = {
                    val newRecentRequests = updateRecentRequest(client)
                    recentRequests.clear()
                    recentRequests.addAll(newRecentRequests)
                },
                modifier = Modifier
                    .width(460.dp)
                     .absolutePadding(40.dp, 30.dp, 20.dp, 13.dp)
                    .background(Color(0xFFF3E8D3))
                    .height(85.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFFF2B179)),
            ) {
                Text(
                    "UPDATE RECENT REQUESTS",
                    fontSize = 23.sp,
                    color = Color(0xFF776E65)
                )
            }
        }
    }
}

@Composable
@Preview
fun app(client: CalculatorClient) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF9F2E7),
    ) {
        runMain(client)
    }
}

fun main(args: Array<String>) = application {
    val windowState = rememberWindowState(height = WINDOW_SIZE.first.dp, width = WINDOW_SIZE.second.dp)
    val serverAddress = args.getOrNull(0) ?: error("Expected 1 argument: Address of host")
    val client = CalculatorClient(serverAddress)
    Window(onCloseRequest = ::exitApplication, state = windowState, title = "SIX-SM Calculator") {
        window.minimumSize = Dimension(WINDOW_SIZE.second, WINDOW_SIZE.first)
        app(client)
    }
}
