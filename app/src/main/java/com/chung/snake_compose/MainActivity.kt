package com.chung.snake_compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.chung.snake_compose.ui.theme.SnakecomposeTheme
import kotlinx.coroutines.delay
import kotlin.math.abs
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SnakecomposeTheme {
                // A surface container using the 'background' color from the theme
               SnakeGame()
            }
        }
    }
}


@Composable
fun SnakeGame() {
    var snakeBody by remember { mutableStateOf(listOf(Offset(100f, 100f))) }
    var foodPosition by remember { mutableStateOf(randomOffset()) }
    var snakeDirection by remember { mutableStateOf(Offset(1f, 0f)) }
    val blockSize = with(LocalDensity.current) { 20.dp.toPx() }

    LaunchedEffect(key1 = Unit) {
        while (true) {
            delay(200)
            val newHead = snakeBody.first().copy(
                x = snakeBody.first().x + blockSize * snakeDirection.x,
                y = snakeBody.first().y + blockSize * snakeDirection.y
            )
            snakeBody = listOf(newHead) + snakeBody.dropLast(1)

            if (newHead.x >= foodPosition.x && newHead.x < foodPosition.x + blockSize &&
                newHead.y >= foodPosition.y && newHead.y < foodPosition.y + blockSize) {
                snakeBody = listOf(newHead) + snakeBody
                foodPosition = randomOffset()
            }
        }
    }

    Canvas(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
        .pointerInput(Unit) {
            detectDragGestures { _, dragAmount ->
                val (dragX, dragY) = dragAmount
                snakeDirection = when {
                    abs(dragX) > abs(dragY) -> if (dragX > 0) Offset(1f, 0f) else Offset(-1f, 0f)
                    else -> if (dragY > 0) Offset(0f, 1f) else Offset(0f, -1f)
                }
            }
        }) {
        snakeBody.forEach { bodyPart ->
            drawRect(
                color = Color.Green,
                topLeft = bodyPart,
                size = Size(blockSize, blockSize)
            )
        }

        drawRect(
            color = Color.Red,
            topLeft = foodPosition,
            size = Size(blockSize, blockSize)
        )
    }
}

fun randomOffset(): Offset {
    val random = Random.Default
    return Offset(
        x = random.nextInt(from = 0, until = 300).toFloat(),
        y = random.nextInt(from = 0, until = 600).toFloat()
    )
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    SnakecomposeTheme {
        Greeting("Android")
    }
}