package com.z.money.ui.settings.sections

import androidx.compose.runtime.Composable
import com.z.money.data.WorkdayMode
import com.z.money.ui.common.WORKDAY_MODE_OPTIONS
import com.z.money.ui.common.label
import com.z.money.ui.settings.EarningSettings
import com.z.money.ui.settings.components.SegmentedSelector
import com.z.money.ui.settings.components.SettingsSection
import com.z.money.ui.settings.components.WorkDaysSelector

@Composable
fun WorkdayRuleSection(
    draft: EarningSettings,
    onDraftChange: (EarningSettings) -> Unit,
) {
    SettingsSection(title = "\u5de5\u4f5c\u65e5\u89c4\u5219", icon = "\ud83d\udcc5") {
        SegmentedSelector(
            options = WORKDAY_MODE_OPTIONS,
            selected = draft.workdayMode,
            label = { it.label },
            onSelected = { onDraftChange(draft.copy(workdayMode = it)) },
        )

        if (draft.workdayMode == WorkdayMode.FixedWeekly) {
            WorkDaysSelector(
                selectedDays = draft.workDays,
                onSelectedDaysChange = { onDraftChange(draft.copy(workDays = it)) },
            )
        }
    }
}
