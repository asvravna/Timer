package asravna.timer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialogDefaults.containerColor
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Surface(
                color = Color(0xFF101010),
                modifier = Modifier.fillMaxSize()
            ) {
                Box(
                    contentAlignment = Alignment.Center
                ) {

                }
                    Timer(
                        totalTime = 10L * 1000L,
                        handleColor = Color.Green,
                        inactiveBarColor = Color.DarkGray,
                        activeBarColor = Color(0xFF37B900),
                        modifier = Modifier.size(200.dp))
                }

            }
        }
    }



    @Composable
    fun Timer(
        totalTime: Long,
        handleColor: Color,
        inactiveBarColor: Color,
        activeBarColor: Color,
        modifier: Modifier = Modifier,
        initialValue: Float = 1f,
        strokeWidth: Dp = 5.dp
    ) {
        var size by remember { mutableStateOf(IntSize.Zero) }
        var value by remember { mutableStateOf(initialValue) }
        var currentTime by remember { mutableStateOf(totalTime)}
        var isTimerRunning by remember { mutableStateOf(false) }

        @Composable
        fun RestTimer() {
            var timeLeft by remember { mutableStateOf(10) }

            LaunchedEffect(key1 = timeLeft) {
                if (timeLeft > 0) {
                    delay(1000L) // Wait for 1 second
                    timeLeft -= 1
                }
            }

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Rest: $timeLeft",
                    fontSize = 35.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .padding(30.dp)
                )

            }
        }
        @Composable
        fun modes(){
            Box(  modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.BottomCenter){
                Row(){
                    Button(onClick = {
                        currentTime = currentTime * 4
                    },
                        modifier = Modifier.padding(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
                    ){
                        Text(text = "4 x 4",
                            style = MaterialTheme.typography.headlineLarge)
                    }
                }

            }

        }

        LaunchedEffect(key1 = currentTime, key2 = isTimerRunning) {
            if (currentTime > 0 && isTimerRunning) {
                delay(100L)
                currentTime -= 100L
                value = currentTime / totalTime.toFloat()
            }
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
                .onSizeChanged { size = it }

        ) {
            Canvas(modifier = modifier) {
                drawArc(
                    color = inactiveBarColor,
                    startAngle = -215f,
                    sweepAngle = 250f,
                    useCenter = false,
                    size = Size(size.width.toFloat(), size.height.toFloat()),
                    style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round)
                )
                drawArc(
                    color = activeBarColor,
                    startAngle = -215f,
                    sweepAngle = 250f * value,
                    useCenter = false,
                    size = Size(size.width.toFloat(), size.height.toFloat()),
                    style = Stroke(strokeWidth.toPx(), cap = StrokeCap.Round)
                )
                val center = Offset(size.width / 2f, size.height / 2f)
                val beta = (250f * value + 145f) * (PI / 180f).toFloat()
                val r = size.width / 2f
                val a = cos(beta) * r
                val b = sin(beta) * r
                drawPoints(
                    listOf(Offset(center.x + a, center.y + b)),
                    pointMode = PointMode.Points,
                    color = handleColor,
                    strokeWidth = (strokeWidth * 3f).toPx(),
                    cap = StrokeCap.Round
                )
            }
            Text(
                text = (currentTime / 1000L).toString() + " sec",
                fontSize = 35.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(30.dp)
            )

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
            ) {

                Button(
                    onClick = {
                        if (currentTime <= 0L) {
                            currentTime = totalTime
                            isTimerRunning = true
                        } else {
                            isTimerRunning = !isTimerRunning
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (!isTimerRunning || currentTime <= 0L) {
                            Color.Green
                        } else {
                            Color.Red
                        }
                    ),
                ) {
                    Text(
                        text = if (isTimerRunning && currentTime >= 0L) "Stop"
                        else if (!isTimerRunning && currentTime >= 0L) "Start"
                        else "Restart"
                    )
                }

                Button(
                    onClick = {
                        currentTime = totalTime
                        isTimerRunning = false
                        value = initialValue
                    },
                    modifier = Modifier.padding(top = 8.dp) // Add some space between buttons
                ) {
                    Text("Reset")
                }

            }

        }
        Spacer(modifier = Modifier.padding(32.dp))
        modes()
        if(currentTime == 0L){
            RestTimer()


        }
    }


}