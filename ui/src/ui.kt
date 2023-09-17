import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

val symbols = listOf(
    "1",
    "2",
    "3",
    "0",
    " = ",
    "4",
    "5",
    "6",
    " + ",
    " - ",
    "7",
    "8",
    "9",
    " * ",
    " / ",
    "(",
    ")",
    "<-",
    "->",
    "⌫"
)

@Composable
fun ButtonClick() {
    var current_expression by remember { mutableStateOf("") }
    var current_position by remember { mutableStateOf(0) }

    Text(
        text = current_expression,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .width(150.dp)
            .absolutePadding(10.dp, 200.dp, 10.dp, 400.dp)
            .background(Color(0xFFF3E8D3))
            .padding(30.dp),
        style = TextStyle(
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold
        )
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(5),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .fillMaxSize()
            .absolutePadding(10.dp, 400.dp, 10.dp, 20.dp)
    ) {
        items(symbols.size) {
            Button(
                onClick = {
                    if (it <= 16) {
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
                            var tmp = current_expression.take(current_position) //взяли все до него
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


                modifier = Modifier.size(60.dp),
                colors = ButtonDefaults.buttonColors(Color(0xFFEBD7B2)),
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

@Composable
@Preview
fun App() {

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF9F2E7)
    ) {
        ButtonClick()
    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}
