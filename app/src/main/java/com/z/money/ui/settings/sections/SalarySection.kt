package com.z.money.ui.settings.sections

import androidx.compose.runtime.Composable
import com.z.money.R
import com.z.money.domain.SalaryPeriod
import com.z.money.ui.common.label
import com.z.money.ui.settings.EarningSettings
import com.z.money.ui.settings.components.SegmentedSelector
import com.z.money.ui.settings.components.SettingsNumberField
import com.z.money.ui.settings.components.SettingsSection

@Composable
fun SalarySection(
    draft: EarningSettings,
    onDraftChange: (EarningSettings) -> Unit,
) {
    SettingsSection(title = "\u85aa\u8d44", iconRes = R.drawable.asset_wallet) {
        SegmentedSelector(
            options = SalaryPeriod.entries,
            selected = draft.salaryPeriod,
            label = { it.label },
            onSelected = { onDraftChange(draft.copy(salaryPeriod = it)) },
        )

        SettingsNumberField(
            label = "\u85aa\u8d44\u91d1\u989d",
            value = draft.salaryAmount,
            onValueChange = { onDraftChange(draft.copy(salaryAmount = it)) },
        )
    }
}
