package com.z.money.ui.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.z.money.data.WorkdayMode
import com.z.money.domain.SalaryPeriod

@Composable
fun SettingsContent(
    settings: EarningSettings,
    legalCalendarStatus: String,
    onRefreshLegalCalendar: () -> Unit,
    onSave: (EarningSettings) -> Unit,
    onBack: () -> Unit,
) {
    var draft by remember(settings) { mutableStateOf(settings) }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(innerPadding)
                .padding(horizontal = 20.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            Text(
                text = "\u85aa\u8d44\u8bbe\u7f6e",
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
            )

            SalarySection(
                draft = draft,
                onDraftChange = { draft = it },
            )

            WorkTimeSection(
                draft = draft,
                onDraftChange = { draft = it },
            )

            WorkdayRuleSection(
                draft = draft,
                onDraftChange = { draft = it },
            )

            if (legalCalendarStatus.isNotBlank() && draft.workdayMode == WorkdayMode.ChinaLegal) {
                SyncStatusRow(
                    legalCalendarStatus = legalCalendarStatus,
                    onRefreshLegalCalendar = onRefreshLegalCalendar,
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                OutlinedButton(
                    onClick = onBack,
                    modifier = Modifier.weight(1f),
                ) {
                    Text(text = "\u8fd4\u56de")
                }
                Button(
                    onClick = { onSave(draft) },
                    modifier = Modifier.weight(1f),
                ) {
                    Text(text = "\u4fdd\u5b58")
                }
            }
        }
    }
}

@Composable
private fun SalarySection(
    draft: EarningSettings,
    onDraftChange: (EarningSettings) -> Unit,
) {
    SettingsSection(title = "\u85aa\u8d44") {
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            SalaryPeriod.entries.forEach { period ->
                FilterChip(
                    selected = draft.salaryPeriod == period,
                    onClick = { onDraftChange(draft.copy(salaryPeriod = period)) },
                    label = { Text(text = period.label) },
                )
            }
        }

        SettingsNumberField(
            label = "\u85aa\u8d44\u91d1\u989d",
            value = draft.salaryAmount,
            onValueChange = { onDraftChange(draft.copy(salaryAmount = it)) },
        )
    }
}

@Composable
private fun WorkTimeSection(
    draft: EarningSettings,
    onDraftChange: (EarningSettings) -> Unit,
) {
    SettingsSection(title = "\u5de5\u4f5c\u65f6\u95f4") {
        SettingsTimeDropdown(
            label = "\u4e0a\u73ed\u65f6\u95f4",
            minutes = draft.workStartMinutes,
            onMinutesChange = { onDraftChange(draft.copy(workStartMinutes = it)) },
        )

        SettingsTimeDropdown(
            label = "\u4e0b\u73ed\u65f6\u95f4",
            minutes = draft.workEndMinutes,
            onMinutesChange = { onDraftChange(draft.copy(workEndMinutes = it)) },
        )
    }
}

@Composable
private fun WorkdayRuleSection(
    draft: EarningSettings,
    onDraftChange: (EarningSettings) -> Unit,
) {
    SettingsSection(title = "\u5de5\u4f5c\u65e5\u89c4\u5219") {
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            WORKDAY_MODE_OPTIONS.forEach { mode ->
                FilterChip(
                    selected = draft.workdayMode == mode,
                    onClick = { onDraftChange(draft.copy(workdayMode = mode)) },
                    label = { Text(text = mode.label) },
                )
            }
        }

        if (draft.workdayMode == WorkdayMode.FixedWeekly) {
            WorkDaysSelector(
                selectedDays = draft.workDays,
                onSelectedDaysChange = { onDraftChange(draft.copy(workDays = it)) },
            )
        }
    }
}

@Composable
private fun SyncStatusRow(
    legalCalendarStatus: String,
    onRefreshLegalCalendar: () -> Unit,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = legalCalendarStatus,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodySmall,
        )
        OutlinedButton(onClick = onRefreshLegalCalendar) {
            Text(text = "\u91cd\u65b0\u540c\u6b65")
        }
    }
}
