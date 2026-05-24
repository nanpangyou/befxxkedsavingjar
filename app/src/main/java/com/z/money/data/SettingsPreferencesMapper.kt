package com.z.money.data

import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import com.z.money.domain.SalaryPeriod
import java.time.DayOfWeek
import java.time.LocalDate

fun Preferences.toUserSettings(
    fallbackCalendar: ChinaLegalCalendar?,
): UserSettings {
    val defaultSettings = UserSettings()
    val schemaVersion = this[SettingsKeys.schemaVersion] ?: 0

    return UserSettings(
        salaryPeriod = SalaryPeriod.entries.firstOrNull {
            it.name == this[SettingsKeys.salaryPeriod]
        } ?: defaultSettings.salaryPeriod,
        salaryAmountYuan = this[SettingsKeys.salaryAmountYuan]
            ?: defaultSettings.salaryAmountYuan,
        annualWorkDays = this[SettingsKeys.annualWorkDays]
            ?: defaultSettings.annualWorkDays,
        workStartMinutes = this[SettingsKeys.workStartMinutes]
            ?: defaultSettings.workStartMinutes,
        workEndMinutes = this[SettingsKeys.workEndMinutes]
            ?: defaultSettings.workEndMinutes,
        workdayMode = toWorkdayMode(schemaVersion),
        workDays = this[SettingsKeys.workDays]?.toWorkDays()
            ?: defaultSettings.workDays,
        chinaLegalCalendar = toChinaLegalCalendar() ?: fallbackCalendar,
    )
}

fun MutablePreferences.writeUserSettings(settings: UserSettings) {
    this[SettingsKeys.schemaVersion] = CURRENT_SCHEMA_VERSION
    this[SettingsKeys.salaryPeriod] = settings.salaryPeriod.name
    this[SettingsKeys.salaryAmountYuan] = settings.salaryAmountYuan
    this[SettingsKeys.annualWorkDays] = settings.annualWorkDays
    this[SettingsKeys.workStartMinutes] = settings.workStartMinutes
    this[SettingsKeys.workEndMinutes] = settings.workEndMinutes
    this[SettingsKeys.workdayMode] = settings.workdayMode.name
    this[SettingsKeys.workDays] = settings.workDays.toPreferenceValue()

    settings.chinaLegalCalendar?.let(::writeChinaLegalCalendar)
}

fun MutablePreferences.writeChinaLegalCalendar(calendar: ChinaLegalCalendar) {
    this[SettingsKeys.chinaLegalYear] = calendar.year
    this[SettingsKeys.chinaLegalExtraWorkDates] = calendar.extraWorkDates.toDateListValue()
    this[SettingsKeys.chinaLegalOffDates] = calendar.offDates.toDateListValue()
}

private fun Preferences.toWorkdayMode(schemaVersion: Int): WorkdayMode {
    if (schemaVersion < CHINA_LEGAL_DEFAULT_SCHEMA_VERSION) {
        return WorkdayMode.ChinaLegal
    }

    return WorkdayMode.entries.firstOrNull {
        it.name == this[SettingsKeys.workdayMode]
    } ?: UserSettings().workdayMode
}

private fun Preferences.toChinaLegalCalendar(): ChinaLegalCalendar? {
    val year = this[SettingsKeys.chinaLegalYear] ?: return null
    return ChinaLegalCalendar(
        year = year,
        source = ChinaLegalCalendarSource.Remote,
        extraWorkDates = this[SettingsKeys.chinaLegalExtraWorkDates].toDateSet(),
        offDates = this[SettingsKeys.chinaLegalOffDates].toDateSet(),
    )
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
