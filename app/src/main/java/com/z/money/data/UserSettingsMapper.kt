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
    val activeCalendar = chinaLegalCalendar?.takeIf {
        workdayMode == WorkdayMode.ChinaLegal
    }

    return WorkSchedule(
        annualWorkDays = activeCalendar?.annualWorkDays() ?: annualWorkDays.coerceAtLeast(1),
        workStart = workStartMinutes.toLocalTime(),
        workEnd = workEndMinutes.toLocalTime().takeIf {
            it > workStartMinutes.toLocalTime()
        } ?: LocalTime.of(23, 59),
        workDays = workDays.ifEmpty { UserSettings().workDays },
        extraWorkDates = activeCalendar?.extraWorkDates ?: emptySet(),
        offDates = activeCalendar?.offDates ?: emptySet(),
    )
}

private fun ChinaLegalCalendar.annualWorkDays(): Int {
    val start = java.time.LocalDate.of(year, 1, 1)
    val endExclusive = start.plusYears(1)
    return generateSequence(start) { date ->
        date.plusDays(1).takeIf { it < endExclusive }
    }.count { date ->
        when {
            date in extraWorkDates -> true
            date in offDates -> false
            else -> date.dayOfWeek in UserSettings().workDays
        }
    }
}

private fun Int.toLocalTime(): LocalTime {
    val minutes = coerceIn(0, MINUTES_PER_DAY)
    if (minutes == MINUTES_PER_DAY) return LocalTime.MAX

    return LocalTime.of(minutes / MINUTES_PER_HOUR, minutes % MINUTES_PER_HOUR)
}

private const val MINUTES_PER_HOUR = 60
private const val MINUTES_PER_DAY = 24 * MINUTES_PER_HOUR
