package com.z.money.data

import android.content.Context
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.z.money.domain.SalaryPeriod
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.settingsDataStore by preferencesDataStore(name = "settings")

class SettingsRepository(
    private val context: Context,
) {
    val settings: Flow<UserSettings> = context.settingsDataStore.data.map { preferences ->
        UserSettings(
            salaryPeriod = SalaryPeriod.entries.firstOrNull {
                it.name == preferences[Keys.salaryPeriod]
            } ?: UserSettings().salaryPeriod,
            salaryAmountYuan = preferences[Keys.salaryAmountYuan]
                ?: UserSettings().salaryAmountYuan,
            annualWorkDays = preferences[Keys.annualWorkDays]
                ?: UserSettings().annualWorkDays,
            workStartMinutes = preferences[Keys.workStartMinutes]
                ?: UserSettings().workStartMinutes,
            workEndMinutes = preferences[Keys.workEndMinutes]
                ?: UserSettings().workEndMinutes,
        )
    }

    suspend fun save(settings: UserSettings) {
        context.settingsDataStore.edit { preferences ->
            preferences[Keys.salaryPeriod] = settings.salaryPeriod.name
            preferences[Keys.salaryAmountYuan] = settings.salaryAmountYuan
            preferences[Keys.annualWorkDays] = settings.annualWorkDays
            preferences[Keys.workStartMinutes] = settings.workStartMinutes
            preferences[Keys.workEndMinutes] = settings.workEndMinutes
        }
    }

    private object Keys {
        val salaryPeriod = stringPreferencesKey("salary_period")
        val salaryAmountYuan = doublePreferencesKey("salary_amount_yuan")
        val annualWorkDays = intPreferencesKey("annual_work_days")
        val workStartMinutes = intPreferencesKey("work_start_minutes")
        val workEndMinutes = intPreferencesKey("work_end_minutes")
    }
}
