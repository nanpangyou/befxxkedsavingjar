package com.z.money.ui.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.z.money.data.WorkdayResolution
import com.z.money.data.toWorkdayResolution
import com.z.money.domain.EarningSnapshot
import com.z.money.domain.IncomeCalculator
import com.z.money.ui.theme.MoneyTheme
import java.time.LocalDateTime

@Composable
fun EarningContent(
    snapshot: EarningSnapshot,
    workdayResolution: WorkdayResolution,
    settings: EarningSettings,
    onOpenSettings: () -> Unit,
) {
    val earningFractionDigits = fractionDigitsForCents(snapshot.centsPerSecond)

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(innerPadding)
                .padding(horizontal = 20.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                Text(
                    text = "\u4eca\u5929\u5df2\u7ecf\u8d5a\u5230",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(
                    text = formatCurrency(
                        cents = snapshot.earnedCentsToday,
                        fractionDigits = earningFractionDigits,
                    ),
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 48.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 56.sp,
                )
                Text(
                    text = workdayResolution.source.label,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }

            LinearProgressIndicator(
                progress = { snapshot.progress.toFloat().coerceIn(0f, 1f) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                MetricTile(
                    title = "\u6bcf\u79d2\u6536\u5165",
                    value = "${
                        formatCurrency(
                            cents = snapshot.centsPerSecond,
                            fractionDigits = earningFractionDigits,
                        )
                    } / \u79d2",
                    modifier = Modifier.weight(1f),
                )
                MetricTile(
                    title = "\u6bcf\u5c0f\u65f6\u6536\u5165",
                    value = formatCurrency(snapshot.centsPerSecond * SECONDS_PER_HOUR),
                    modifier = Modifier.weight(1f),
                )
            }

            MetricTile(
                title = "\u5f53\u524d\u72b6\u6001",
                value = snapshot.status.label,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = settings.summaryText,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium,
            )

            Button(
                onClick = onOpenSettings,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(text = "\u8bbe\u7f6e\u85aa\u8d44")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EarningContentPreview() {
    val settings = EarningSettings()
    val now = LocalDateTime.of(2026, 5, 13, 10, 30)

    MoneyTheme {
        EarningContent(
            snapshot = IncomeCalculator.snapshot(
                salary = settings.toSalaryInput(),
                schedule = settings.toWorkSchedule(),
                now = now,
            ),
            workdayResolution = settings.toUserSettings().toWorkdayResolution(now.toLocalDate()),
            settings = settings,
            onOpenSettings = {},
        )
    }
}
