package com.z.money.ui.settings.sections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.z.money.data.WorkdayMode
import com.z.money.ui.common.WORKDAY_MODE_OPTIONS
import com.z.money.ui.common.label
import com.z.money.ui.settings.EarningSettings
import com.z.money.ui.settings.components.SettingsSection
import com.z.money.ui.settings.components.WorkDaysSelector

@Composable
@OptIn(ExperimentalLayoutApi::class)
fun WorkdayRuleSection(
    draft: EarningSettings,
    onDraftChange: (EarningSettings) -> Unit,
) {
    SettingsSection(title = "\u5de5\u4f5c\u65e5\u89c4\u5219", icon = "\ud83d\udcc5") {
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            WORKDAY_MODE_OPTIONS.forEach { mode ->
                FilterChip(
                    selected = draft.workdayMode == mode,
                    onClick = { onDraftChange(draft.copy(workdayMode = mode)) },
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = MaterialTheme.colorScheme.primary,
                        selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                    ),
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
