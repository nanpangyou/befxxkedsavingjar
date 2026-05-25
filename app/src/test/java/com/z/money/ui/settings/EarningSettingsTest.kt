package com.z.money.ui.settings

import com.z.money.data.WorkdayMode
import com.z.money.data.ThemeMode
import com.z.money.domain.SalaryPeriod
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.DayOfWeek

class EarningSettingsTest {
    @Test
    fun convertsUiSettingsToUserSettings() {
        val settings = EarningSettings(
            salaryPeriod = SalaryPeriod.Annual,
            salaryAmount = "120000.50",
            workStartMinutes = 8 * 60 + 30,
            workEndMinutes = 18 * 60,
            workdayMode = WorkdayMode.FixedWeekly,
            workDays = setOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY),
            themeMode = ThemeMode.Dark,
        )

        val userSettings = settings.toUserSettings()

        assertEquals(SalaryPeriod.Annual, userSettings.salaryPeriod)
        assertEquals(120000.50, userSettings.salaryAmountYuan, 0.0)
        assertEquals(8 * 60 + 30, userSettings.workStartMinutes)
        assertEquals(18 * 60, userSettings.workEndMinutes)
        assertEquals(WorkdayMode.FixedWeekly, userSettings.workdayMode)
        assertEquals(setOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY), userSettings.workDays)
        assertEquals(ThemeMode.Dark, userSettings.themeMode)
    }

    @Test
    fun invalidSalaryFallsBackToZero() {
        val settings = EarningSettings(salaryAmount = "abc")

        val userSettings = settings.toUserSettings()

        assertEquals(0.0, userSettings.salaryAmountYuan, 0.0)
    }

    @Test
    fun negativeSalaryIsClampedToZero() {
        val settings = EarningSettings(salaryAmount = "-100")

        val userSettings = settings.toUserSettings()

        assertEquals(0.0, userSettings.salaryAmountYuan, 0.0)
    }
}
