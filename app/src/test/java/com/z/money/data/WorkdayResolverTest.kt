package com.z.money.data

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.DayOfWeek
import java.time.LocalDate

class WorkdayResolverTest {
    @Test
    fun fixedWeeklyModeReportsFixedSources() {
        val settings = UserSettings(
            workdayMode = WorkdayMode.FixedWeekly,
            workDays = setOf(DayOfWeek.MONDAY),
        )

        val monday = settings.resolveWorkday(LocalDate.of(2026, 5, 11))
        val tuesday = settings.resolveWorkday(LocalDate.of(2026, 5, 12))

        assertTrue(monday.isWorkday)
        assertEquals(WorkdaySource.FixedWorkday, monday.source)
        assertFalse(tuesday.isWorkday)
        assertEquals(WorkdaySource.FixedOffDay, tuesday.source)
    }

    @Test
    fun chinaLegalModeReportsFallbackWithoutCalendar() {
        val settings = UserSettings(
            workdayMode = WorkdayMode.ChinaLegal,
            workDays = setOf(DayOfWeek.MONDAY),
            chinaLegalCalendar = null,
        )

        val monday = settings.resolveWorkday(LocalDate.of(2026, 5, 11))

        assertTrue(monday.isWorkday)
        assertEquals(WorkdaySource.ChinaLegalFallbackWorkday, monday.source)
    }

    @Test
    fun chinaLegalModeReportsHolidayAndAdjustedWorkday() {
        val holiday = LocalDate.of(2026, 1, 1)
        val adjustedWorkday = LocalDate.of(2026, 1, 3)
        val settings = UserSettings(
            workdayMode = WorkdayMode.ChinaLegal,
            chinaLegalCalendar = ChinaLegalCalendar(
                year = 2026,
                offDates = setOf(holiday),
                extraWorkDates = setOf(adjustedWorkday),
            ),
        )

        val holidayResolution = settings.resolveWorkday(holiday)
        val adjustedResolution = settings.resolveWorkday(adjustedWorkday)

        assertFalse(holidayResolution.isWorkday)
        assertEquals(WorkdaySource.ChinaLegalHoliday, holidayResolution.source)
        assertTrue(adjustedResolution.isWorkday)
        assertEquals(WorkdaySource.ChinaLegalAdjustedWorkday, adjustedResolution.source)
    }
}
