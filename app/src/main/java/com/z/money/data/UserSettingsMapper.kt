package com.z.money.data

import com.z.money.domain.SalaryInput
import com.z.money.domain.WorkSchedule
import java.time.LocalTime

fun UserSettings.toSalaryInput(): SalaryInput {
    return SalaryInput(
        amountCents = (salaryAmountYuan.coerceAtLeast(0.0) * 100).toLong(),
        period = salaryPeriod,
    )
}

fun UserSettings.toWorkSchedule(): WorkSchedule {
    return WorkSchedule(
        annualWorkDays = annualWorkDays.coerceAtLeast(1),
        workStart = workStartMinutes.toLocalTime(),
        workEnd = workEndMinutes.toLocalTime().takeIf {
            it > workStartMinutes.toLocalTime()
        } ?: LocalTime.of(23, 59),
    )
}

private fun Int.toLocalTime(): LocalTime {
    val minutes = coerceIn(0, MINUTES_PER_DAY - 1)
    return LocalTime.of(minutes / MINUTES_PER_HOUR, minutes % MINUTES_PER_HOUR)
}

private const val MINUTES_PER_HOUR = 60
private const val MINUTES_PER_DAY = 24 * MINUTES_PER_HOUR
