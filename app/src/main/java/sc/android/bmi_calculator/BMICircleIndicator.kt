package sc.android.bmi_calculator

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import sc.android.bmi_calculator.ui.theme.BorderColor
import sc.android.bmi_calculator.ui.theme.LabelText
import sc.android.bmi_calculator.ui.theme.TitleText


@Composable
fun BMICircle(bmi: Float, modifier: Modifier) {

    // Convert BMI → angle
    val targetAngle =
        (bmi.coerceIn(0f, 50f) / 50f) * 360f

    // Animate from previous → new angle
    val animatedSweep by animateFloatAsState(
        targetValue = targetAngle,
        animationSpec = tween(durationMillis = 900),
        label = "BmiRing"
    )

    val bmiStatus = when {
        bmi < 18.5 -> "Underweight"
        bmi < 25f -> "Healthy"
        bmi < 30f -> "Overweight"
        else -> "Obese"
    }

    Box(modifier = modifier, contentAlignment = Alignment.Center) {

        Canvas(modifier = Modifier.fillMaxSize()) {

            // Background ring
            drawArc(
                color = BorderColor,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(16f, cap = StrokeCap.Round)
            )

            // Moving ring
            drawArc(
                color = bmiColor(bmi),
                startAngle = -90f,
                sweepAngle = animatedSweep,
                useCenter = false,
                style = Stroke(24f, cap = StrokeCap.Round)
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(String.format("%.1f", bmi), style = TitleText)
            Text(bmiStatus, style = LabelText)
        }
    }
}


fun bmiColor(bmi: Float): Color =
    when {
        bmi < 18.5 -> Color(0xFFFFC857)
        bmi < 25f -> Color(0xFF4CAF50)
        bmi < 30f -> Color(0xFFFF9800)
        else -> Color(0xFFF53F3B)
    }