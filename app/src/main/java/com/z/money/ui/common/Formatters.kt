package com.z.money.ui.common

import java.text.NumberFormat
import java.util.Locale

fun formatCurrency(cents: Double): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale.CHINA)
    return formatter.format(cents / 100.0)
}

fun formatCurrency(
    cents: Double,
    fractionDigits: Int,
): String {
    return NumberFormat.getCurrencyInstance(Locale.CHINA).apply {
        minimumFractionDigits = fractionDigits
        maximumFractionDigits = fractionDigits
    }.format(cents / 100.0)
}

fun fractionDigitsForCents(cents: Double): Int {
    val yuan = cents / 100.0
    return when {
        yuan >= 0.01 -> 2
        yuan >= 0.0001 -> 4
        else -> 6
    }
}

fun Int.toTimeText(): String {
    val minutes = coerceIn(0, MINUTES_PER_DAY)
    return "%02d:%02d".format(Locale.US, minutes / MINUTES_PER_HOUR, minutes % MINUTES_PER_HOUR)
}
