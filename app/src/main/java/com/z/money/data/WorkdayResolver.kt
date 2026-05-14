package com.z.money.data

import java.time.LocalDate

enum class WorkdaySource {
    FixedWorkday,
    FixedOffDay,
    ChinaLegalFallbackWorkday,
    ChinaLegalFallbackOffDay,
    ChinaLegalRegularWorkday,
    ChinaLegalRegularOffDay,
    ChinaLegalAdjustedWorkday,
    ChinaLegalHoliday,
}

data class WorkdayResolution(
    val isWorkday: Boolean,
    val source: WorkdaySource,
)

fun UserSettings.resolveWorkday(date: LocalDate): WorkdayResolution {
    val weeklyWorkDays = workDays.ifEmpty { UserSettings().workDays }

    if (workdayMode == WorkdayMode.FixedWeekly) {
        val isWorkday = date.dayOfWeek in weeklyWorkDays
        return WorkdayResolution(
            isWorkday = isWorkday,
            source = if (isWorkday) WorkdaySource.FixedWorkday else WorkdaySource.FixedOffDay,
        )
    }

    val calendar = chinaLegalCalendar?.takeIf { it.isAvailableFor(date.year) }
    if (calendar == null) {
        val isWorkday = date.dayOfWeek in weeklyWorkDays
        return WorkdayResolution(
            isWorkday = isWorkday,
            source = if (isWorkday) {
                WorkdaySource.ChinaLegalFallbackWorkday
            } else {
                WorkdaySource.ChinaLegalFallbackOffDay
            },
        )
    }

    return when {
        date in calendar.extraWorkDates -> WorkdayResolution(
            isWorkday = true,
            source = WorkdaySource.ChinaLegalAdjustedWorkday,
        )

        date in calendar.offDates -> WorkdayResolution(
            isWorkday = false,
            source = WorkdaySource.ChinaLegalHoliday,
        )

        date.dayOfWeek in weeklyWorkDays -> WorkdayResolution(
            isWorkday = true,
            source = WorkdaySource.ChinaLegalRegularWorkday,
        )

        else -> WorkdayResolution(
            isWorkday = false,
            source = WorkdaySource.ChinaLegalRegularOffDay,
        )
    }
}
