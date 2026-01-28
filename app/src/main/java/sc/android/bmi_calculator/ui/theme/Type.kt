//package sc.android.bmi_calculator.ui.theme
//
//import androidx.compose.material3.Typography
//import androidx.compose.ui.text.TextStyle
//import androidx.compose.ui.text.font.FontFamily
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.sp
//
//// Set of Material typography styles to start with
//val Typography = Typography(
//    bodyLarge = TextStyle(
//        fontFamily = FontFamily.Default,
//        fontWeight = FontWeight.Normal,
//        fontSize = 16.sp,
//        lineHeight = 24.sp,
//        letterSpacing = 0.5.sp
//    )
//    /* Other default text styles to override
//    titleLarge = TextStyle(
//        fontFamily = FontFamily.Default,
//        fontWeight = FontWeight.Normal,
//        fontSize = 22.sp,
//        lineHeight = 28.sp,
//        letterSpacing = 0.sp
//    ),
//    labelSmall = TextStyle(
//        fontFamily = FontFamily.Default,
//        fontWeight = FontWeight.Medium,
//        fontSize = 11.sp,
//        lineHeight = 16.sp,
//        letterSpacing = 0.5.sp
//    )
//    */
//)

package sc.android.bmi_calculator.ui.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.compose.material3.Typography

val TitleText = TextStyle(
    fontSize = 34.sp,
    fontWeight = FontWeight.Bold,
    fontFamily = FontFamily.Default,
    color = PrimaryText
)

val LabelText = TextStyle(
    fontSize = 18.sp,
    fontWeight = FontWeight.Medium,
    fontFamily = FontFamily.Default,
    color = SecondaryText
)

val ButtonText = TextStyle(
    fontSize = 20.sp,
    fontWeight = FontWeight.SemiBold,
    fontFamily = FontFamily.Default,
    color = ButtonTextColor
)

val AppTypography = Typography(
    titleLarge = TitleText,
    bodyLarge = LabelText,
    labelLarge = ButtonText
)