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
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.z.money.domain.EarningSnapshot
import com.z.money.domain.IncomeCalculator
import com.z.money.domain.SalaryInput
import com.z.money.domain.SalaryPeriod
import com.z.money.domain.WorkSchedule
import com.z.money.domain.WorkdayStatus
import com.z.money.ui.theme.MoneyTheme
import kotlinx.coroutines.delay
import java.text.NumberFormat
import java.time.LocalDateTime
import java.util.Locale

@Composable
fun EarningScreen() {
    var now by remember { mutableStateOf(LocalDateTime.now()) }

    LaunchedEffect(Unit) {
        while (true) {
            now = LocalDateTime.now()
            delay(1_000)
        }
    }

    EarningContent(
        snapshot = IncomeCalculator.snapshot(
            salary = demoSalary,
            schedule = demoSchedule,
            now = now,
        ),
    )
}

@Composable
private fun EarningContent(snapshot: EarningSnapshot) {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(innerPadding)
                .padding(horizontal = 24.dp, vertical = 28.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "\u4eca\u5929\u5df2\u7ecf\u8d5a\u5230",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(
                    text = formatCurrency(snapshot.earnedCentsToday),
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 44.sp,
                    fontWeight = FontWeight.Bold,
                    lineHeight = 52.sp,
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
                    value = formatCurrency(snapshot.centsPerSecond),
                    modifier = Modifier.weight(1f),
                )
                MetricTile(
                    title = "\u4eca\u65e5\u4e0a\u9650",
                    value = formatCurrency(snapshot.maxEarnableCentsToday),
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
                text = "\u5f53\u524d\u4f7f\u7528\u6f14\u793a\u85aa\u8d44\uff1a\u6708\u85aa 10000 \u5143\uff0c250 \u4e2a\u5de5\u4f5c\u65e5\uff0c\u6bcf\u5929 8 \u5c0f\u65f6\u3002",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@Composable
private fun MetricTile(
    title: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surfaceContainer,
        tonalElevation = 1.dp,
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.Start,
        ) {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.labelLarge,
            )
            Text(
                text = value,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.SemiBold,
            )
        }
    }
}

private val WorkdayStatus.label: String
    get() = when (this) {
        WorkdayStatus.NotWorkday -> "\u4eca\u5929\u4e0d\u662f\u5de5\u4f5c\u65e5"
        WorkdayStatus.BeforeWork -> "\u8fd8\u6ca1\u5f00\u59cb\u4e0a\u73ed"
        WorkdayStatus.Working -> "\u6b63\u5728\u6323\u94b1"
        WorkdayStatus.AfterWork -> "\u4eca\u5929\u5df2\u7ecf\u4e0b\u73ed"
    }

private fun formatCurrency(cents: Double): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale.CHINA)
    return formatter.format(cents / 100.0)
}

private val demoSalary = SalaryInput(
    amountCents = 10_000_00,
    period = SalaryPeriod.Monthly,
)

private val demoSchedule = WorkSchedule()

@Preview(showBackground = true)
@Composable
private fun EarningContentPreview() {
    MoneyTheme {
        EarningContent(
            snapshot = IncomeCalculator.snapshot(
                salary = demoSalary,
                schedule = demoSchedule,
                now = LocalDateTime.of(2026, 5, 13, 10, 30),
            ),
        )
    }
}
