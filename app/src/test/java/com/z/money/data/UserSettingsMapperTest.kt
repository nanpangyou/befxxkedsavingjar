package com.z.money.data

import com.z.money.domain.SalaryPeriod
import org.junit.Assert.assertEquals
import org.junit.Test

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
    fun clampsScheduleValuesToValidMinimums() {
        val settings = UserSettings(
            annualWorkDays = -10,
            dailyWorkHours = -8.0,
        )

        val schedule = settings.toWorkSchedule()

        assertEquals(1, schedule.annualWorkDays)
        assertEquals(0.1, schedule.dailyWorkHours, 0.0)
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
}
