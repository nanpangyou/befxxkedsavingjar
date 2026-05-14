package com.z.money.data

import java.time.LocalDate

object BuiltInChinaLegalCalendars {
    fun forYear(year: Int): ChinaLegalCalendar? {
        return when (year) {
            2026 -> ChinaLegalCalendar(
                year = 2026,
                source = ChinaLegalCalendarSource.BuiltIn,
                extraWorkDates = setOf(
                    LocalDate.of(2026, 1, 4),
                    LocalDate.of(2026, 2, 14),
                    LocalDate.of(2026, 2, 28),
                    LocalDate.of(2026, 5, 9),
                    LocalDate.of(2026, 9, 20),
                    LocalDate.of(2026, 10, 10),
                ),
                offDates = dateRange(2026, 1, 1, 3) +
                    dateRange(2026, 2, 15, 23) +
                    dateRange(2026, 4, 4, 6) +
                    dateRange(2026, 5, 1, 5) +
                    dateRange(2026, 6, 19, 21) +
                    dateRange(2026, 9, 25, 27) +
                    dateRange(2026, 10, 1, 7),
            )

            else -> null
        }
    }

    private fun dateRange(
        year: Int,
        month: Int,
        startDay: Int,
        endDay: Int,
    ): Set<LocalDate> {
        return (startDay..endDay).mapTo(mutableSetOf()) { day ->
            LocalDate.of(year, month, day)
        }
    }
}
