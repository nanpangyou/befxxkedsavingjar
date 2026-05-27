package com.z.money.ui.settings.sections

import androidx.compose.runtime.Composable
import com.z.money.data.ThemeMode
import com.z.money.ui.settings.EarningSettings
import com.z.money.ui.settings.components.SegmentedSelector
import com.z.money.ui.settings.components.SettingsSection

@Composable
fun AppearanceSection(
    draft: EarningSettings,
    onDraftChange: (EarningSettings) -> Unit,
) {
    SettingsSection(title = "\u5916\u89c2", icon = "\ud83c\udfa8") {
        SegmentedSelector(
            options = ThemeMode.entries,
            selected = draft.themeMode,
            label = { it.label },
            onSelected = { onDraftChange(draft.copy(themeMode = it)) },
        )
    }
}
