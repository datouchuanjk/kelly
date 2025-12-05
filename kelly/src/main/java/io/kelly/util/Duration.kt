package io.kelly.util

import kotlin.time.Duration


fun Duration.formattedAdvanced(
    dayUnit: String? = "d",
    hourUnit: String? = "h",
    minuteUnit: String? = "m",
    secondUnit: String? = "s",
    separator: String = " ",
): String {
    val absDuration = if (isNegative()) -this else this
    val totalSeconds = absDuration.inWholeSeconds
    if (dayUnit == null && hourUnit == null && minuteUnit == null && secondUnit == null) {
        return "${totalSeconds}s"
    }

    val parts = mutableListOf<String>()
    var remainingSeconds = totalSeconds

    if (dayUnit != null) {
        val days = remainingSeconds / (24 * 3600)
        if (days > 0) {
            parts.add("${days}$dayUnit")
            remainingSeconds %= (24 * 3600)
        }
    }

    if (hourUnit != null) {
        val hours = remainingSeconds / 3600
        if (hours > 0 || parts.isNotEmpty()) {
            parts.add("${hours}$hourUnit")
            remainingSeconds %= 3600
        }
    }

    if (minuteUnit != null) {
        val minutes = remainingSeconds / 60
        if (minutes > 0 || parts.isNotEmpty()) {
            parts.add("${minutes}$minuteUnit")
            remainingSeconds %= 60
        }
    }

    if (secondUnit != null) {
        if (remainingSeconds > 0 || parts.isEmpty()) {
            parts.add("${remainingSeconds}$secondUnit")
        }
    }

    return if (parts.isEmpty()) "" else parts.joinToString(separator)
}