package com.z.money.data

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import java.time.Year
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.settingsDataStore by preferencesDataStore(name = "settings")

class SettingsRepository(
    private val context: Context,
    private val holidayService: ChinaHolidayService = ChinaHolidayService(),
) {
    val settings: Flow<UserSettings> = context.settingsDataStore.data.map { preferences ->
        preferences.toUserSettings(
            fallbackCalendar = BuiltInChinaLegalCalendars.forYear(Year.now().value),
        )
    }

    suspend fun save(settings: UserSettings) {
        context.settingsDataStore.edit { preferences ->
            preferences.writeUserSettings(settings)
        }
    }

    suspend fun refreshChinaLegalCalendar(year: Int) {
        val calendar = holidayService.fetchCalendar(year)
        context.settingsDataStore.edit { preferences ->
            preferences.writeChinaLegalCalendar(calendar)
        }
    }
}
