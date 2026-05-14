package com.z.money.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.LocalDate

class BuiltInChinaLegalCalendarsTest {
    @Test
    fun provides2026LegalCalendar() {
        val calendar = BuiltInChinaLegalCalendars.forYear(2026)

        requireNotNull(calendar)
        assertEquals(ChinaLegalCalendarSource.BuiltIn, calendar.source)
        assertTrue(LocalDate.of(2026, 1, 1) in calendar.offDates)
        assertTrue(LocalDate.of(2026, 2, 23) in calendar.offDates)
        assertTrue(LocalDate.of(2026, 10, 7) in calendar.offDates)
        assertTrue(LocalDate.of(2026, 1, 4) in calendar.extraWorkDates)
        assertTrue(LocalDate.of(2026, 10, 10) in calendar.extraWorkDates)
    }

    @Test
    fun unsupportedYearReturnsNull() {
        assertEquals(null, BuiltInChinaLegalCalendars.forYear(2027))
    }
}
