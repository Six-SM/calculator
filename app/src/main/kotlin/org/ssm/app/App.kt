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

var recent_requests = mutableStateListOf(
    "aaa",
    "bbb",
    "21479",
    "2507",
    "888",
    "kek",
    "aaa",
    "bbb",
    "21479",
    "2507",
    "888",
    "kek",
    "aaa",
    "bbb",
    "21479",
    "2507",
    "888",
    "kek"
)

val COLUMN_WIDTH = 400
val LEFT_PADDING = 50
val BUTTON_CALC_SIZE = 60


fun updateRecentRequestList(expression: String, result: String) {


    recent_requests.add(0, expression + " = $result")
    //TODO: переприсваивание recent_requests из //List<CalculationHistoryRequest>

}

fun makeRequest(expression: String): String {
    //TODO: отправляю запрос на сервер, передавая expression
    // CalculationRequest(expression) (не пон, куда передавать)
    updateRecentRequestList(expression, "25")
    return "25" //пока что так


}


@Composable
fun RunMain() {
    var current_expression by remember { mutableStateOf("") }
    var current_position by remember { mutableStateOf(0) }

    Row {
        Column(modifier = Modifier.width(COLUMN_WIDTH.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                buildAnnotatedString {
                    append(current_expression.take(current_position))

                    if (current_position < current_expression.length) {
                        withStyle(style = SpanStyle(color = Color(0xFFf59563))) {
                            append(current_expression[current_position])
                        }
                    }
                    if (current_position < current_expression.length - 1) {
                        append(current_expression.substring(current_position + 1, current_expression.length))
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
                                val result = makeRequest(current_expression)
                                if (result.all { it.isDigit() }) {
                                    current_expression += " = $result"
                                } else {
                                    current_expression = result
                                }

                            } else
                                if (symbols[it][0].isDigit() || (operation_symbols.contains(symbols[it][0]) && symbols[it].length == 1)) {
                                    if (current_position < current_expression.length - 1) {
                                        current_expression =
                                            current_expression.take(current_position + 1) + (symbols[it]) + current_expression.substring(
                                                current_position + 1,
                                                current_expression.length
                                            )
                                    } else {
                                        current_expression += symbols[it]
                                    }
                                    current_position++
                                } else {
                                    if (symbols[it] == "<-") {
                                        current_position--
                                        current_position = maxOf(current_position, 0)
                                    } else if (symbols[it] == "->") {
                                        current_position++
                                        current_position = minOf(current_expression.length - 1, current_position)


                                    } else if (symbols[it] == "⌫" && current_expression.isNotEmpty()) {
                                        var tmp = current_expression.take(current_position)
                                        if (current_position < current_expression.length - 1) {
                                            tmp += current_expression.substring(
                                                current_position + 1,
                                                current_expression.length
                                            )
                                        }
                                        current_expression = tmp
                                        current_position =
                                            minOf(current_expression.length - 1, current_position)
                                    }
                                }

                            if (current_expression.isEmpty()) {
                                current_position = 0
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
                        Text(
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
                }
            }
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(1),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier.fillMaxSize()
                .absolutePadding(left = 30.dp, right = 30.dp, top = 90.dp, bottom = BUTTON_CALC_SIZE.dp),
            content = {
                items(recent_requests.size) {
                    Button(
                        onClick = {}, //TODO
                        modifier = Modifier
                            .width(200.dp)
                            .absolutePadding(30.dp, 10.dp, 10.dp, 0.dp)
                            .background(Color(0xFFF3E8D3))
                            .height(BUTTON_CALC_SIZE.dp),
                        colors = ButtonDefaults.buttonColors(Color(0xFFF3E8D3)),
                    ) {
                        Text(
                            recent_requests[it],
                            fontSize = 25.sp,
                            color = Color(0xFF776E65)
                        )

                    }
                }

            }
        )
    }
}

@Composable
@Preview
fun App() {

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF9F2E7),
    ) {
        RunMain()
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
