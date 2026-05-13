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
}
