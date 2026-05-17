package com.z.money.ui.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.z.money.data.ChinaLegalCalendarSource
import com.z.money.data.SettingsRepository
import com.z.money.data.UserSettings
import com.z.money.data.WorkdayMode
import com.z.money.data.toSalaryInput
import com.z.money.data.toWorkSchedule
import com.z.money.data.toWorkdayResolution
import com.z.money.domain.IncomeCalculator
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDateTime

@Composable
fun EarningScreen() {
    val context = LocalContext.current
    val repository = remember { SettingsRepository(context.applicationContext) }
    val persistedSettings by repository.settings.collectAsState(initial = UserSettings())
    val settings = EarningSettings.fromUserSettings(persistedSettings)
    val scope = rememberCoroutineScope()
    var showingSettings by remember { mutableStateOf(false) }
    var legalCalendarStatus by remember { mutableStateOf("") }
    var now by remember { mutableStateOf(LocalDateTime.now()) }

    LaunchedEffect(Unit) {
        while (true) {
            now = LocalDateTime.now()
            delay(1_000)
        }
    }

    LaunchedEffect(persistedSettings.workdayMode, persistedSettings.chinaLegalCalendar, now.year) {
        legalCalendarStatus = resolveLegalCalendarStatus(
            settings = persistedSettings,
            year = now.year,
            refreshCalendar = { repository.refreshChinaLegalCalendar(now.year) },
        )
    }

    if (showingSettings) {
        SettingsContent(
            settings = settings,
            legalCalendarStatus = legalCalendarStatus,
            onRefreshLegalCalendar = {
                scope.launch {
                    legalCalendarStatus = syncStatus(now.year)
                    legalCalendarStatus = runCatching {
                        repository.refreshChinaLegalCalendar(now.year)
                    }.fold(
                        onSuccess = { syncedStatus(now.year) },
                        onFailure = { syncFailedStatus },
                    )
                }
            },
            onSave = { draft ->
                scope.launch {
                    repository.save(draft.toUserSettings())
                    showingSettings = false
                }
            },
            onBack = { showingSettings = false },
        )
    } else {
        EarningContent(
            snapshot = IncomeCalculator.snapshot(
                salary = settings.toSalaryInput(),
                schedule = settings.toWorkSchedule(),
                now = now,
            ),
            workdayResolution = persistedSettings.toWorkdayResolution(now.toLocalDate()),
            settings = settings,
            onOpenSettings = { showingSettings = true },
        )
    }
}

private suspend fun resolveLegalCalendarStatus(
    settings: UserSettings,
    year: Int,
    refreshCalendar: suspend () -> Unit,
): String {
    if (settings.workdayMode != WorkdayMode.ChinaLegal) return ""

    if (settings.chinaLegalCalendar?.isAvailableFor(year) != true) {
        return runCatching {
            refreshCalendar()
        }.fold(
            onSuccess = { syncedStatus(year) },
            onFailure = { missingCalendarStatus(year) },
        )
    }

    return when (settings.chinaLegalCalendar.source) {
        ChinaLegalCalendarSource.BuiltIn -> builtInStatus(year)
        ChinaLegalCalendarSource.Remote -> syncedStatus(year)
    }
}
