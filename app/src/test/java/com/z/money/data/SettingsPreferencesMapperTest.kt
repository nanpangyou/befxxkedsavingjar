package com.z.money.data

import androidx.datastore.preferences.core.mutablePreferencesOf
import androidx.datastore.preferences.core.preferencesOf
import com.z.money.domain.SalaryPeriod
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.DayOfWeek
import java.time.LocalDate

class SettingsPreferencesMapperTest {
    @Test
    fun oldSchemaMigratesWorkdayModeToChinaLegal() {
        val preferences = preferencesOf(
            SettingsKeys.schemaVersion to 1,
            SettingsKeys.workdayMode to WorkdayMode.FixedWeekly.name,
        )

        val settings = preferences.toUserSettings(fallbackCalendar = null)

        assertEquals(WorkdayMode.ChinaLegal, settings.workdayMode)
    }

    @Test
    fun currentSchemaPreservesSavedWorkdayMode() {
        val preferences = preferencesOf(
            SettingsKeys.schemaVersion to CURRENT_SCHEMA_VERSION,
            SettingsKeys.workdayMode to WorkdayMode.FixedWeekly.name,
        )

        val settings = preferences.toUserSettings(fallbackCalendar = null)

        assertEquals(WorkdayMode.FixedWeekly, settings.workdayMode)
    }

    @Test
    fun workDaysRoundTripThroughPreferences() {
        val original = UserSettings(
            workDays = setOf(DayOfWeek.MONDAY, DayOfWeek.SUNDAY),
        )
        val preferences = mutablePreferencesOf()

        preferences.writeUserSettings(original)
        val restored = preferences.toUserSettings(fallbackCalendar = null)

        assertEquals(setOf(DayOfWeek.MONDAY, DayOfWeek.SUNDAY), restored.workDays)
    }

    @Test
    fun legalCalendarRoundTripsThroughPreferences() {
        val calendar = ChinaLegalCalendar(
            year = 2026,
            source = ChinaLegalCalendarSource.Remote,
            extraWorkDates = setOf(LocalDate.of(2026, 1, 4)),
            offDates = setOf(LocalDate.of(2026, 1, 1)),
        )
        val preferences = mutablePreferencesOf()

        preferences.writeChinaLegalCalendar(calendar)
        val settings = preferences.toUserSettings(fallbackCalendar = null)

        assertEquals(calendar, settings.chinaLegalCalendar)
    }

    @Test
    fun savedSettingsRoundTripThroughPreferences() {
        val original = UserSettings(
            salaryPeriod = SalaryPeriod.Annual,
            salaryAmountYuan = 88_888.0,
            workStartMinutes = 9 * 60 + 15,
            workEndMinutes = 19 * 60 + 45,
            workdayMode = WorkdayMode.FixedWeekly,
            workDays = setOf(DayOfWeek.TUESDAY),
        )
        val preferences = mutablePreferencesOf()

        preferences.writeUserSettings(original)
        val restored = preferences.toUserSettings(fallbackCalendar = null)

        assertEquals(original.salaryPeriod, restored.salaryPeriod)
        assertEquals(original.salaryAmountYuan, restored.salaryAmountYuan, 0.0)
        assertEquals(original.workStartMinutes, restored.workStartMinutes)
        assertEquals(original.workEndMinutes, restored.workEndMinutes)
        assertEquals(original.workdayMode, restored.workdayMode)
        assertEquals(original.workDays, restored.workDays)
    }
}
