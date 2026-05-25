package com.z.money.ui.main

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import com.z.money.BuildConfig
import com.z.money.data.BuiltInChinaLegalCalendars
import com.z.money.data.SettingsRepository
import com.z.money.data.ThemeMode
import com.z.money.data.UserSettings
import com.z.money.data.toSalaryInput
import com.z.money.data.toWorkSchedule
import com.z.money.data.toWorkdayResolution
import com.z.money.domain.IncomeCalculator
import com.z.money.ui.theme.MoneyTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.Year

@Composable
fun EarningScreen() {
    val context = LocalContext.current
    val repository = remember { SettingsRepository(context.applicationContext) }
    val initialSettings = remember {
        UserSettings(
            chinaLegalCalendar = BuiltInChinaLegalCalendars.forYear(Year.now().value),
        )
    }
    val persistedSettings by repository.settings.collectAsState(initial = initialSettings)
    val settings = EarningSettings.fromUserSettings(persistedSettings)
    val systemInDarkTheme = isSystemInDarkTheme()
    val darkTheme = when (persistedSettings.themeMode) {
        ThemeMode.System -> systemInDarkTheme
        ThemeMode.Light -> false
        ThemeMode.Dark -> true
    }
    val scope = rememberCoroutineScope()
    var showingSettings by remember { mutableStateOf(false) }
    var showingAbout by remember { mutableStateOf(false) }
    var legalCalendarStatus by remember { mutableStateOf("") }
    var now by remember { mutableStateOf(LocalDateTime.now()) }
    val openUrl: (String) -> Unit = remember(context) {
        { url ->
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))
        }
    }

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

    MoneyTheme(darkTheme = darkTheme) {
        if (showingAbout) {
            AboutContent(
                versionName = BuildConfig.VERSION_NAME,
                onOpenPrivacy = { openUrl(PRIVACY_URL) },
                onOpenGitHub = { openUrl(PROJECT_URL) },
                onOpenFeedback = { openUrl(FEEDBACK_URL) },
                onBack = { showingAbout = false },
            )
        } else if (showingSettings) {
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
                onOpenAbout = { showingAbout = true },
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
}
