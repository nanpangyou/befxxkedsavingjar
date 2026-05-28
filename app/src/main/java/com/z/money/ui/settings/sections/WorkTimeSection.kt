package com.z.money.ui.settings.sections

import androidx.compose.runtime.Composable
import com.z.money.R
import com.z.money.ui.settings.EarningSettings
import com.z.money.ui.settings.components.SettingsSection
import com.z.money.ui.settings.components.SettingsTimeDropdown

@Composable
fun WorkTimeSection(
    draft: EarningSettings,
    onDraftChange: (EarningSettings) -> Unit,
) {
    SettingsSection(title = "\u5de5\u4f5c\u65f6\u95f4", iconRes = R.drawable.asset_clock) {
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
