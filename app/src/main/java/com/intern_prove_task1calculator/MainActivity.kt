package com.intern_prove_task1calculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CalculatorApp()
        }
    }
}

@Composable
fun CalculatorApp() {
    var result by remember { mutableStateOf("0") }
    val buttons = listOf(
        listOf("7", "8", "9", "/"),
        listOf("4", "5", "6", "*"),
        listOf("1", "2", "3", "-"),
        listOf("C", "0", "=", "+")
    )

    val buttonColors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE0E0E0))
    val displayColor = Color(0xFFFAFAFA)
    val borderColor = Color(0xFFB0B0B0)

    MaterialTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFDDDDDD))
                .padding(16.dp)
                .padding(top = 280.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Display area
            Text(
                text = result,
                fontSize = 48.sp,
                color = Color.Black,
                textAlign = TextAlign.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(displayColor, RoundedCornerShape(16.dp))
                    .border(2.dp, borderColor, RoundedCornerShape(16.dp))
                    .padding(24.dp)
            )

            // Buttons area
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                buttons.forEach { row ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(72.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        row.forEach { text ->
                            CalculatorButton(text, buttonColors) {
                                result = calculateResult(result, text)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CalculatorButton(text: String, colors: ButtonColors, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = colors,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .aspectRatio(1f)
            .padding(4.dp)
            .shadow(4.dp, RoundedCornerShape(16.dp))
    ) {
        Text(
            text = text,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
    }
}

fun calculateResult(currentResult: String, input: String): String {
    return when (input) {
        "C" -> "0"
        "=" -> {
            try {
                val sanitizedExpression = currentResult.replace("x", "*").replace("÷", "/")
                val result = eval(sanitizedExpression)
                if (result.endsWith(".0")) result.dropLast(2) else result
            } catch (e: Exception) {
                "Error"
            }
        }
        else -> if (currentResult == "0") input else currentResult + input
    }
}

fun eval(expression: String): String {
    return try {
        val tokens = expression.split("(?<=[-+*/])|(?=[-+*/])".toRegex()).filter { it.isNotEmpty() }
        var result = tokens[0].toDouble()
        var i = 1
        while (i < tokens.size) {
            val operator = tokens[i]
            val value = tokens[i + 1].toDouble()
            result = when (operator) {
                "+" -> result + value
                "-" -> result - value
                "*" -> result * value
                "/" -> result / value
                else -> result
            }
            i += 2
        }
        result.toString()
    } catch (e: Exception) {
        "Error"
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    CalculatorApp()
}
