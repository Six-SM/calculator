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
import java.awt.Dimension

val symbols = listOf(
    "1",
    "2",
    "3",
    "⌫",
    "=",
    "4",
    "5",
    "6",
    "+",
    "-",
    "7",
    "8",
    "9",
    "*",
    "/",
    "0",
    "(",
    ")",
    "<-",
    "->"
)

val operation_symbols = listOf('+', '-', '/', '*', ')', '(', '=')

var recent_requests = mutableStateListOf( //TODO: потом сделать mutableStateFlow для подгрузки
    "MADE BY SIX SM-MASTERS"
)

const val COLUMN_WIDTH = 400
const val LEFT_PADDING = 50
const val BUTTON_CALC_SIZE = 60


fun updateRecentRequestList(expression: String, result: String) {
    recent_requests.add(0, "$expression = $result")
    //TODO: переприсваивание org.ssm.app.getRecent_requests из //List<CalculationHistoryRequest>
}

fun makeRequest(expression: String): String {
    //TODO: отправляю запрос на сервер, передавая expression
    // CalculationRequest(expression) (не пон, куда передавать)
    var result = "25"
    updateRecentRequestList(expression, result)
    return result //пока что так
}

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
fun runMain() {
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
                            if (symbols[it] == "=") {
                                val result = makeRequest(currentExpression)
                                if (result.all { it.isDigit() }) {
                                    currentExpression += " = $result"
                                } else {
                                    currentExpression = result
                                }
                              //  currentPosition = currentExpression.length - 1

                            } else
                                if (symbols[it][0].isDigit() || (operation_symbols.contains(symbols[it][0]) && symbols[it].length == 1)) {
                                    if (currentPosition < currentExpression.length - 1) {
                                        currentExpression =
                                            currentExpression.take(currentPosition + 1) + (symbols[it]) + currentExpression.substring(
                                                currentPosition + 1,
                                                currentExpression.length
                                            )
                                    } else {
                                        currentExpression += symbols[it]
                                    }
                                    currentPosition++
                                } else {
                                    if (symbols[it] == "<-") {
                                        currentPosition--
                                        currentPosition = maxOf(currentPosition, 0)
                                    } else if (symbols[it] == "->") {
                                        currentPosition++
                                        currentPosition = minOf(currentExpression.length - 1, currentPosition)


                                    } else if (symbols[it] == "⌫" && currentExpression.isNotEmpty()) {
                                        var tmp = currentExpression.take(currentPosition)
                                        if (currentPosition < currentExpression.length - 1) {
                                            tmp += currentExpression.substring(
                                                currentPosition + 1,
                                                currentExpression.length
                                            )
                                        }
                                        currentExpression = tmp
                                        currentPosition =
                                            minOf(currentExpression.length - 1, currentPosition)
                                    }
                                }

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
                    items(recent_requests.size) {
                        Button(
                            onClick = {
                                currentExpression = recent_requests[it]
                                currentPosition = currentExpression.length - 1
                            }, //TODO: должен общаться с предыдущими запросами
                            modifier = Modifier
                                .width(200.dp)
                                .absolutePadding(30.dp, 10.dp, 10.dp, 0.dp)
                                .background(Color(0xFFF3E8D3))
                                .height(BUTTON_CALC_SIZE.dp),
                            colors = ButtonDefaults.buttonColors(Color(0xFFF3E8D3)),
                        ) {
                            Text(
                                recent_requests[it],
                                fontSize = 23.sp,
                                color = Color(0xFF776E65)
                            )

                        }
                    }

                }
            )

            Button(
                onClick = {
                    //TODO: update истории
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
fun App() {

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF9F2E7),
    ) {
        runMain()
    }
}


val WINDOW_SIZE = Pair(800, 900)

fun main() = application {
    val windowState = rememberWindowState(height = WINDOW_SIZE.first.dp, width = WINDOW_SIZE.second.dp)
    Window(onCloseRequest = ::exitApplication, state = windowState, title = "SIX-SM Calculator") {
        window.minimumSize = Dimension(WINDOW_SIZE.second, WINDOW_SIZE.first)
        App()
    }
}
