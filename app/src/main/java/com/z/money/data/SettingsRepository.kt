package com.z.money.data

import android.content.Context
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.z.money.domain.SalaryPeriod
import java.time.DayOfWeek
import java.time.LocalDate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.settingsDataStore by preferencesDataStore(name = "settings")

private object SettingsKeys {
    val schemaVersion = intPreferencesKey("schema_version")
    val salaryPeriod = stringPreferencesKey("salary_period")
    val salaryAmountYuan = doublePreferencesKey("salary_amount_yuan")
    val annualWorkDays = intPreferencesKey("annual_work_days")
    val workStartMinutes = intPreferencesKey("work_start_minutes")
    val workEndMinutes = intPreferencesKey("work_end_minutes")
    val workdayMode = stringPreferencesKey("workday_mode")
    val workDays = stringPreferencesKey("work_days")
    val chinaLegalYear = intPreferencesKey("china_legal_year")
    val chinaLegalExtraWorkDates = stringPreferencesKey("china_legal_extra_work_dates")
    val chinaLegalOffDates = stringPreferencesKey("china_legal_off_dates")
}

class SettingsRepository(
    private val context: Context,
    private val holidayService: ChinaHolidayService = ChinaHolidayService(),
) {
    val settings: Flow<UserSettings> = context.settingsDataStore.data.map { preferences ->
        val schemaVersion = preferences[SettingsKeys.schemaVersion] ?: 0
        UserSettings(
            salaryPeriod = SalaryPeriod.entries.firstOrNull {
                it.name == preferences[SettingsKeys.salaryPeriod]
            } ?: UserSettings().salaryPeriod,
            salaryAmountYuan = preferences[SettingsKeys.salaryAmountYuan]
                ?: UserSettings().salaryAmountYuan,
            annualWorkDays = preferences[SettingsKeys.annualWorkDays]
                ?: UserSettings().annualWorkDays,
            workStartMinutes = preferences[SettingsKeys.workStartMinutes]
                ?: UserSettings().workStartMinutes,
            workEndMinutes = preferences[SettingsKeys.workEndMinutes]
                ?: UserSettings().workEndMinutes,
            workdayMode = preferences.toWorkdayMode(schemaVersion),
            workDays = preferences[SettingsKeys.workDays]?.toWorkDays()
                ?: UserSettings().workDays,
            chinaLegalCalendar = preferences.toChinaLegalCalendar(),
        )
    }

    suspend fun save(settings: UserSettings) {
        context.settingsDataStore.edit { preferences ->
            preferences[SettingsKeys.schemaVersion] = CURRENT_SCHEMA_VERSION
            preferences[SettingsKeys.salaryPeriod] = settings.salaryPeriod.name
            preferences[SettingsKeys.salaryAmountYuan] = settings.salaryAmountYuan
            preferences[SettingsKeys.annualWorkDays] = settings.annualWorkDays
            preferences[SettingsKeys.workStartMinutes] = settings.workStartMinutes
            preferences[SettingsKeys.workEndMinutes] = settings.workEndMinutes
            preferences[SettingsKeys.workdayMode] = settings.workdayMode.name
            preferences[SettingsKeys.workDays] = settings.workDays.toPreferenceValue()
            settings.chinaLegalCalendar?.let { calendar ->
                preferences[SettingsKeys.chinaLegalYear] = calendar.year
                preferences[SettingsKeys.chinaLegalExtraWorkDates] = calendar.extraWorkDates.toDateListValue()
                preferences[SettingsKeys.chinaLegalOffDates] = calendar.offDates.toDateListValue()
            }
        }
    }

    suspend fun refreshChinaLegalCalendar(year: Int) {
        val calendar = holidayService.fetchCalendar(year)
        context.settingsDataStore.edit { preferences ->
            preferences[SettingsKeys.chinaLegalYear] = calendar.year
            preferences[SettingsKeys.chinaLegalExtraWorkDates] = calendar.extraWorkDates.toDateListValue()
            preferences[SettingsKeys.chinaLegalOffDates] = calendar.offDates.toDateListValue()
        }
    }
}

private fun Set<DayOfWeek>.toPreferenceValue(): String {
    return sortedBy { it.value }.joinToString(",") { it.name }
}

private fun String.toWorkDays(): Set<DayOfWeek> {
    val days = split(",")
        .mapNotNull { value ->
            DayOfWeek.entries.firstOrNull { it.name == value }
        }
        .toSet()

    return days.ifEmpty { UserSettings().workDays }
}

private fun androidx.datastore.preferences.core.Preferences.toWorkdayMode(
    schemaVersion: Int,
): WorkdayMode {
    if (schemaVersion < CHINA_LEGAL_DEFAULT_SCHEMA_VERSION) {
        return WorkdayMode.ChinaLegal
    }

    return WorkdayMode.entries.firstOrNull {
        it.name == this[SettingsKeys.workdayMode]
    } ?: UserSettings().workdayMode
}

private fun androidx.datastore.preferences.core.Preferences.toChinaLegalCalendar(): ChinaLegalCalendar? {
    val year = this[SettingsKeys.chinaLegalYear] ?: return null
    return ChinaLegalCalendar(
        year = year,
        extraWorkDates = this[SettingsKeys.chinaLegalExtraWorkDates].toDateSet(),
        offDates = this[SettingsKeys.chinaLegalOffDates].toDateSet(),
    )
}

private fun Set<LocalDate>.toDateListValue(): String {
    return sorted().joinToString(",") { it.toString() }
}

private fun String?.toDateSet(): Set<LocalDate> {
    return this
        ?.split(",")
        ?.mapNotNull { value -> value.takeIf { it.isNotBlank() }?.let(LocalDate::parse) }
        ?.toSet()
        ?: emptySet()
}

private const val CURRENT_SCHEMA_VERSION = 2
private const val CHINA_LEGAL_DEFAULT_SCHEMA_VERSION = 2
