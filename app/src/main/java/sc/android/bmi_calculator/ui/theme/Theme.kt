package sc.android.bmi_calculator.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = AccentBlue,
    onPrimary = Color.White,

    background = DarkBackground,
    onBackground = PrimaryText,

    surface = DarkBackground,
    onSurface = PrimaryText,

    outline = BorderColor
)

@Composable
fun BMICalculatorTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = AppTypography,
        content = content
    )
}