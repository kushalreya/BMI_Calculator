package sc.android.bmi_calculator

private const val MIN_BMI = 18.5
private const val MAX_BMI = 24.9

//isMetric: false => unit is in ft,lb
//isMetric: true => unit is in m,kg

//bmi calculation method
fun bmiLogic(height: Float, weight: Float, isMetric: Boolean): Float{
    var Height = height
    var Weight= weight
    var result =  0.00f
    //converting metric to imperial
    if (!isMetric){
        Height /= 3.28084f
        Weight /= 2.20462f
    }
    result=(Weight.div((Height*Height)))
    return result
}


// Metric (m, kg) → Imperial (ft, lb)
fun convertMetricToImperial(height: String, weight: String): Pair<String, String> {
    val h = height.toDoubleOrNull() ?: return Pair(height, weight)
    val w = weight.toDoubleOrNull() ?: return Pair(height, weight)

    val heightFt = h * 3.28084
    val weightLb = w * 2.20462

    return Pair(
        String.format("%.2f", heightFt),
        String.format("%.1f", weightLb)
    )
}

// Imperial (ft, lb) → Metric (m, kg)
fun convertImperialToMetric(height: String, weight: String): Pair<String, String> {
    val h = height.toDoubleOrNull() ?: return Pair(height, weight)
    val w = weight.toDoubleOrNull() ?: return Pair(height, weight)

    val heightM = h / 3.28084
    val weightKg = w / 2.20462

    return Pair(
        String.format("%.2f", heightM),
        String.format("%.1f", weightKg)
    )
}

fun healthyWeightRange(
    isMetric: Boolean,
    height: String,
    isFemale: Boolean
): String {

    val h = height.toDoubleOrNull() ?: return "Invalid height"

    val minWeight: Int
    val maxWeight: Int
    val unit: String

    if (isMetric) {
        minWeight = (MIN_BMI * h * h).toInt()
        maxWeight = (MAX_BMI * h * h).toInt()
        unit = "kg"
    } else {
        val heightInches = h * 12
        minWeight = ((MIN_BMI * heightInches * heightInches) / 703).toInt()
        maxWeight = ((MAX_BMI * heightInches * heightInches) / 703).toInt()
        unit = "lb"
    }

    val label = if (isFemale) "female" else "male"
    return "Healthy $label weight range: $minWeight–$maxWeight $unit"
}