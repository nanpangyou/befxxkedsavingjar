package com.z.money.ui.settings.sections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.z.money.data.ThemeMode
import com.z.money.ui.settings.EarningSettings
import com.z.money.ui.settings.components.SettingsSection

@Composable
@OptIn(ExperimentalLayoutApi::class)
fun AppearanceSection(
    draft: EarningSettings,
    onDraftChange: (EarningSettings) -> Unit,
) {
    SettingsSection(title = "\u5916\u89c2") {
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            ThemeMode.entries.forEach { mode ->
                FilterChip(
                    selected = draft.themeMode == mode,
                    onClick = { onDraftChange(draft.copy(themeMode = mode)) },
                    label = { Text(text = mode.label) },
                )
            }
        }
    }
}
