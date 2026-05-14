package com.z.money.data

import java.time.LocalDate

data class ChinaLegalCalendar(
    val year: Int,
    val extraWorkDates: Set<LocalDate>,
    val offDates: Set<LocalDate>,
) {
    fun isAvailableFor(year: Int): Boolean = this.year == year
}
