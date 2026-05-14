package com.z.money.data

import java.time.LocalDate

data class ChinaLegalCalendar(
    val year: Int,
    val extraWorkDates: Set<LocalDate>,
    val offDates: Set<LocalDate>,
    val source: ChinaLegalCalendarSource = ChinaLegalCalendarSource.Remote,
) {
    fun isAvailableFor(year: Int): Boolean = this.year == year
}

enum class ChinaLegalCalendarSource {
    BuiltIn,
    Remote,
}
