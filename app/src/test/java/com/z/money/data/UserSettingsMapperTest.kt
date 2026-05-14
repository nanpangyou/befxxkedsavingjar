package com.z.money.data

import com.z.money.domain.SalaryPeriod
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import java.time.DayOfWeek
import java.time.LocalDate

class UserSettingsMapperTest {
    @Test
    fun convertsSalaryAmountToCents() {
        val settings = UserSettings(
            salaryPeriod = SalaryPeriod.Annual,
            salaryAmountYuan = 123_456.78,
        )

        val salary = settings.toSalaryInput()

        assertEquals(SalaryPeriod.Annual, salary.period)
        assertEquals(12_345_678L, salary.amountCents)
    }

    @Test
    fun fixedWeeklyModeIgnoresManualAnnualWorkDays() {
        val settings = UserSettings(
            workdayMode = WorkdayMode.FixedWeekly,
            annualWorkDays = -10,
        )
        val expectedWorkDays = UserSettings().workDays.sumOf { dayOfWeek ->
            countWeekdaysInYear(
                year = LocalDate.now().year,
                dayOfWeek = dayOfWeek,
            )
        }

        val schedule = settings.toWorkSchedule()

        assertEquals(expectedWorkDays, schedule.annualWorkDays)
    }

    @Test
    fun mapsWorkStartAndEndTimes() {
        val settings = UserSettings(
            workStartMinutes = 8 * 60 + 30,
            workEndMinutes = 22 * 60 + 15,
        )

        val schedule = settings.toWorkSchedule()

        assertEquals(8, schedule.workStart.hour)
        assertEquals(30, schedule.workStart.minute)
        assertEquals(22, schedule.workEnd.hour)
        assertEquals(15, schedule.workEnd.minute)
    }

    @Test
    fun invalidEndTimeFallsBackToLastMinuteOfDay() {
        val settings = UserSettings(
            workStartMinutes = 18 * 60,
            workEndMinutes = 9 * 60,
        )

        val schedule = settings.toWorkSchedule()

        assertEquals(23, schedule.workEnd.hour)
        assertEquals(59, schedule.workEnd.minute)
    }

    @Test
    fun mapsEndOfDayAsLastRepresentableTime() {
        val settings = UserSettings(
            workStartMinutes = 9 * 60,
            workEndMinutes = 24 * 60,
        )

        val schedule = settings.toWorkSchedule()

        assertEquals(23, schedule.workEnd.hour)
        assertEquals(59, schedule.workEnd.minute)
        assertEquals(59, schedule.workEnd.second)
    }

    @Test
    fun mapsCustomWorkDays() {
        val settings = UserSettings(
            workdayMode = WorkdayMode.FixedWeekly,
            workDays = setOf(DayOfWeek.WEDNESDAY),
        )

        val schedule = settings.toWorkSchedule()

        assertEquals(setOf(DayOfWeek.WEDNESDAY), schedule.workDays)
    }

    @Test
    fun fixedWeeklyModeCalculatesAnnualWorkDaysFromSelectedWeekdays() {
        val settings = UserSettings(
            workdayMode = WorkdayMode.FixedWeekly,
            workDays = setOf(DayOfWeek.WEDNESDAY),
        )
        val expectedWorkDays = countWeekdaysInYear(
            year = LocalDate.now().year,
            dayOfWeek = DayOfWeek.WEDNESDAY,
        )

        val schedule = settings.toWorkSchedule()

        assertEquals(expectedWorkDays, schedule.annualWorkDays)
    }

    @Test
    fun chinaLegalCalendarOverridesDefaultWorkdays() {
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

        val schedule = settings.toWorkSchedule()

        assertFalse(schedule.isWorkday(holiday))
        assertTrue(schedule.isWorkday(adjustedWorkday))
    }

    @Test
    fun defaultSettingsUseChinaLegalMode() {
        assertEquals(WorkdayMode.ChinaLegal, UserSettings().workdayMode)
    }

    private fun countWeekdaysInYear(
        year: Int,
        dayOfWeek: DayOfWeek,
    ): Int {
        val start = LocalDate.of(year, 1, 1)
        val endExclusive = start.plusYears(1)
        return generateSequence(start) { date ->
            date.plusDays(1).takeIf { it < endExclusive }
        }.count { it.dayOfWeek == dayOfWeek }
    }
}
