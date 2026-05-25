package com.z.money.ui.settings.sections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.z.money.domain.SalaryPeriod
import com.z.money.ui.common.label
import com.z.money.ui.settings.EarningSettings
import com.z.money.ui.settings.components.SettingsNumberField
import com.z.money.ui.settings.components.SettingsSection

@Composable
@OptIn(ExperimentalLayoutApi::class)
fun SalarySection(
    draft: EarningSettings,
    onDraftChange: (EarningSettings) -> Unit,
) {
    SettingsSection(title = "\u85aa\u8d44") {
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
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
