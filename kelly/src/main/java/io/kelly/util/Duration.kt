package  io.kelly.util
import kotlin.time.Duration

fun Duration.formattedAdvanced(
    dayUnit: String? = "d",
    hourUnit: String? = "h",
    minuteUnit: String? = "m",
    secondUnit: String? = "s",
    separator: String = " ",
): String {
    val absDuration = this.absoluteValue
    return absDuration.toComponents { days, hours, minutes, seconds, _ ->
        val parts = buildList {
            if (dayUnit != null && days > 0) {
                add("$days$dayUnit")
            }

            if (hourUnit != null && (hours > 0 || isNotEmpty())) {
                add("$hours$hourUnit")
            }

            if (minuteUnit != null && (minutes > 0 || isNotEmpty())) {
                add("$minutes$minuteUnit")
            }

            if (secondUnit != null) {
                if (seconds > 0 || isNotEmpty()) {
                    add("$seconds$secondUnit")
                } else if (isEmpty()) {
                    add("0$secondUnit")
                }
            }
        }

        // 4. 拼接
        parts.joinToString(separator)
    }
}