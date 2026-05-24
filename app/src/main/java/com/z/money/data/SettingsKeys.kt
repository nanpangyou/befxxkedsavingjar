package com.z.money.data

import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object SettingsKeys {
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
