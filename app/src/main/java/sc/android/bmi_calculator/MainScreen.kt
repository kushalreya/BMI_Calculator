package sc.android.bmi_calculator

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import sc.android.bmi_calculator.ui.theme.AccentBlue
import sc.android.bmi_calculator.ui.theme.BMICalculatorTheme
import sc.android.bmi_calculator.ui.theme.BorderColor
import sc.android.bmi_calculator.ui.theme.ButtonText
import sc.android.bmi_calculator.ui.theme.CardBackground
import sc.android.bmi_calculator.ui.theme.DarkBackground
import sc.android.bmi_calculator.ui.theme.ErrorBorder
import sc.android.bmi_calculator.ui.theme.ErrorText
import sc.android.bmi_calculator.ui.theme.HintText
import sc.android.bmi_calculator.ui.theme.LabelText
import sc.android.bmi_calculator.ui.theme.PrimaryText
import sc.android.bmi_calculator.ui.theme.SecondaryText
import sc.android.bmi_calculator.ui.theme.TitleText
import sc.android.bmi_calculator.ui.theme.UnitTextColor

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge(
            statusBarStyle = SystemBarStyle.dark(
                scrim = Color.Transparent.toArgb()
            ),
            navigationBarStyle = SystemBarStyle.dark(
                scrim = Color.Transparent.toArgb()
            )
        )

        setContent {
            BMICalculatorTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = DarkBackground
                ) { padding ->
                    BMICalculator(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding)
                    )
                }
            }
        }
    }
}

@Composable
fun BMICircle(bmi: Float, modifier: Modifier) {

    val sweepAngle = (bmi.coerceIn(0f, 50f) / 50f) * 360f

    val bmiStatus = when {
        bmi < 18.5 -> "Underweight"
        bmi < 25f -> "Healthy"
        bmi < 30f -> "Overweight"
        else -> "Obese"
    }

    Box(modifier = modifier, contentAlignment = Alignment.Center) {

        Canvas(modifier = Modifier.fillMaxSize()) {
            drawArc(
                color = BorderColor,
                startAngle = -90f,
                sweepAngle = 360f,
                useCenter = false,
                style = Stroke(16f, cap = StrokeCap.Round)
            )

            val ringColor = bmiColor(bmi)

            drawArc(
                color = ringColor,
                startAngle = -90f,
                sweepAngle = sweepAngle,
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


@Composable
fun BMICalculator(modifier: Modifier) {

    var isMetric by remember { mutableStateOf(true) }
    var isFemale by remember { mutableStateOf(false) }

    var height by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }

    var showError by remember { mutableStateOf(false) }
    var bmiCalculated by remember { mutableStateOf<Float?>(null) }

    var lastBmi by remember { mutableStateOf<Float?>(null) }

    val context = LocalContext.current

    val heightValue = height.toFloatOrNull()
    val weightValue = weight.toFloatOrNull()

    val heightError = showError && (heightValue == null || heightValue <= 0f)
    val weightError = showError && (weightValue == null || weightValue <= 0f)

    val normalWeightText = healthyWeightRange(isMetric, height, isFemale)


    Column(
        modifier = modifier.padding(top = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text("BMI Calculator", style = TitleText)

        Spacer(Modifier.height(24.dp))

        // ---------------- Toggles ----------------
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Imperial", style = LabelText)
                Spacer(Modifier.width(8.dp))
                Switch(
                    checked = isMetric,
                    onCheckedChange = { toMetric ->

                        if (height.isNotBlank()) {
                            height = height.toFloatOrNull()?.let {
                                if (toMetric) {
                                    // ft → m
                                    (it / 3.28084f)
                                } else {
                                    // m → ft
                                    (it * 3.28084f)
                                }
                            }?.let { String.format("%.2f", it) } ?: height
                        }

                        if (weight.isNotBlank()) {
                            weight = weight.toFloatOrNull()?.let {
                                if (toMetric) {
                                    // lb → kg
                                    (it / 2.20462f)
                                } else {
                                    // kg → lb
                                    (it * 2.20462f)
                                }
                            }?.let { String.format("%.2f", it) } ?: weight
                        }

                        isMetric = toMetric
                        bmiCalculated = null
                        showError = false
                    },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color(0xFFEE8867),
                        uncheckedThumbColor = Color(0xFF82BB83),
                        checkedTrackColor = Color.White.copy(alpha = 0.15f),
                        uncheckedTrackColor = Color.White.copy(alpha = 0.10f)
                    )
                )
                Spacer(Modifier.width(8.dp))
                Text("Metric", style = LabelText)
            }

            Spacer(Modifier.weight(1f))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Male", style = LabelText)
                Spacer(Modifier.width(8.dp))
                Switch(
                    checked = isFemale,
                    onCheckedChange = { isFemale = it },
                    colors = SwitchDefaults.colors(
                        checkedThumbColor = Color(0xFFF878B8),
                        uncheckedThumbColor = Color(0xFF7092F8),
                        checkedTrackColor = Color.White.copy(alpha = 0.15f),
                        uncheckedTrackColor = Color.White.copy(alpha = 0.10f)
                    )
                )
                Spacer(Modifier.width(8.dp))
                Text("Female", style = LabelText)
            }
        }

        Spacer(Modifier.height(24.dp))

        // ---------------- Input Card ----------------
        Column(
            modifier = Modifier
                .width(370.dp)
                .background(CardBackground, RoundedCornerShape(24.dp))
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                Text("Height", style = LabelText)
            }

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = height,
                onValueChange = {
                    if (it.isEmpty() || it.matches(Regex("^\\d*(\\.\\d*)?$"))) {
                        height = it
                        showError = false
                        bmiCalculated = null
                    }
                },
                modifier = Modifier.width(300.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal
                ),
                placeholder = {
                    Text("Enter Height", color = if (heightError) ErrorText else HintText)
                },
                trailingIcon = {
                    Text(
                        text = if (isMetric) "m" else "ft",
                        color = if (heightError) ErrorText else UnitTextColor,
                        style = LabelText
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = if (heightError) ErrorBorder else AccentBlue,
                    unfocusedBorderColor = if (heightError) ErrorBorder else BorderColor,
                    focusedTextColor = PrimaryText,
                    unfocusedTextColor = SecondaryText,
                    cursorColor = if (heightError) ErrorBorder else AccentBlue
                )
            )

            Spacer(Modifier.height(32.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                Text("Weight", style = LabelText)
            }

            Spacer(Modifier.height(16.dp))

            OutlinedTextField(
                value = weight,
                onValueChange = {
                    if (it.isEmpty() || it.matches(Regex("^\\d*(\\.\\d*)?$"))) {
                        weight = it
                        showError = false
                        bmiCalculated = null
                    }
                },
                modifier = Modifier.width(300.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Decimal
                ),
                placeholder = {
                    Text("Enter Weight", color = if (weightError) ErrorText else HintText)
                },
                trailingIcon = {
                    Text(
                        text = if (isMetric) "kg" else "lb",
                        color = if (weightError) ErrorText else UnitTextColor,
                        style = LabelText
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = if (weightError) ErrorBorder else AccentBlue,
                    unfocusedBorderColor = if (weightError) ErrorBorder else BorderColor,
                    focusedTextColor = PrimaryText,
                    unfocusedTextColor = SecondaryText,
                    cursorColor = if (weightError) ErrorBorder else AccentBlue
                )
            )

            Spacer(Modifier.height(32.dp))

            Button(
                onClick = {
                    val errorMessage = when {
                        heightValue == null || weightValue == null ->
                            "Please enter both height and weight"

                        heightValue <= 0f || weightValue <= 0f ->
                            "Height and weight must be greater than 0"

                        heightValue < 0.5f -> {
                            "Height seems too small"
                        }

                        weightValue < 5f -> {
                            "Weight seems too low"
                        }

                        else -> null
                    }

                    if (errorMessage != null) {
                        showError = true
                        Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                        return@Button
                    }

                    // ✅ ACTUAL BMI CALCULATION
                    val bmi = bmiLogic(heightValue!!, weightValue!!, isMetric)

                    // ✅ UPDATE BOTH STATES (THIS IS CRITICAL)
                    bmiCalculated = bmi
                    lastBmi = bmi
                },
                colors = ButtonDefaults.buttonColors(containerColor = AccentBlue)
            ) {
                Text("Calculate", style = ButtonText)
            }
        }

        Spacer(Modifier.height(16.dp))

        AnimatedVisibility(
            visible = bmiCalculated != null,
            enter =
                fadeIn(animationSpec = tween(450)) +
                        scaleIn(
                            initialScale = 0.75f,
                            animationSpec = tween(450)
                        ),
            exit =
                fadeOut(animationSpec = tween(180)) +
                        scaleOut(
                            targetScale = 0.85f,
                            animationSpec = tween(180)
                        )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
                    .padding(vertical = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                val bmi = lastBmi ?: return@AnimatedVisibility

                BMICircle(
                    bmi = bmi,
                    modifier = Modifier.size(180.dp)
                )

                Spacer(Modifier.height(24.dp))

                Text(
                    text = normalWeightText,
                    style = ButtonText,
                    color = bmiColor(bmi).copy(alpha = 0.85f),
                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            }
        }
    }
}