package com.z.money.ui.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.z.money.data.WorkdayMode
import com.z.money.ui.common.SecondaryActionButton
import com.z.money.ui.settings.sections.AppearanceSection
import com.z.money.ui.settings.sections.LegalCalendarSyncSection
import com.z.money.ui.settings.sections.SalarySection
import com.z.money.ui.settings.sections.WorkTimeSection
import com.z.money.ui.settings.sections.WorkdayRuleSection

@Composable
fun SettingsContent(
    settings: EarningSettings,
    legalCalendarStatus: String,
    onRefreshLegalCalendar: () -> Unit,
    onSettingsChange: (EarningSettings) -> Unit,
    onOpenAbout: () -> Unit,
) {
    var draft by remember { mutableStateOf(settings) }

    fun updateDraft(updated: EarningSettings) {
        draft = updated
        onSettingsChange(updated)
    }

    Scaffold(containerColor = MaterialTheme.colorScheme.background) { innerPadding ->
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(innerPadding),
        ) {
            val metrics = settingsLayoutMetrics(maxWidth, maxHeight)

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .navigationBarsPadding()
                    .padding(
                        horizontal = metrics.horizontalPadding,
                        vertical = metrics.verticalPadding,
                    ),
                verticalArrangement = Arrangement.spacedBy(metrics.sectionSpacing),
            ) {
                Text(
                    text = "\u85aa\u8d44\u8bbe\u7f6e",
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Black,
                )

                SalarySection(
                    draft = draft,
                    onDraftChange = ::updateDraft,
                )

                WorkTimeSection(
                    draft = draft,
                    onDraftChange = ::updateDraft,
                )

                WorkdayRuleSection(
                    draft = draft,
                    onDraftChange = ::updateDraft,
                )

                AppearanceSection(
                    draft = draft,
                    onDraftChange = ::updateDraft,
                )

                if (legalCalendarStatus.isNotBlank() && draft.workdayMode == WorkdayMode.ChinaLegal) {
                    LegalCalendarSyncSection(
                        legalCalendarStatus = legalCalendarStatus,
                        onRefreshLegalCalendar = onRefreshLegalCalendar,
                    )
                }

                SecondaryActionButton(
                    text = "\u5173\u4e8e\u7a9d\u56ca\u8d39\u8ba1\u7b97\u5668",
                    onClick = onOpenAbout,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}
