package com.z.money.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.z.money.data.WorkdayResolution
import com.z.money.data.toWorkdayResolution
import com.z.money.domain.EarningSnapshot
import com.z.money.domain.IncomeCalculator
import com.z.money.domain.WorkdayStatus
import com.z.money.ui.common.CoinTrail
import com.z.money.ui.common.PiggyJarIllustration
import com.z.money.ui.common.PrimaryActionButton
import com.z.money.ui.common.SECONDS_PER_HOUR
import com.z.money.ui.common.UiSize
import com.z.money.ui.common.UiSpacing
import com.z.money.ui.common.WarmCard
import com.z.money.ui.common.WorkerIllustration
import com.z.money.ui.common.formatCurrency
import com.z.money.ui.common.fractionDigitsForCents
import com.z.money.ui.common.label
import com.z.money.ui.settings.EarningSettings
import com.z.money.ui.theme.MoneyTheme
import java.time.LocalDateTime
import kotlin.math.roundToInt

@Composable
fun EarningContent(
    snapshot: EarningSnapshot,
    workdayResolution: WorkdayResolution,
    settings: EarningSettings,
    onOpenSettings: () -> Unit,
) {
    val earningFractionDigits = fractionDigitsForCents(snapshot.centsPerSecond)
    val totalSeconds = (snapshot.maxEarnableCentsToday / snapshot.centsPerSecond)
        .takeIf { it.isFinite() && it > 0.0 }
        ?.roundToInt()
        ?: 0
    val remainingSeconds = (totalSeconds - snapshot.workedSecondsToday).coerceAtLeast(0)

    Scaffold(containerColor = MaterialTheme.colorScheme.background) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(innerPadding)
                .padding(horizontal = UiSpacing.PageHorizontal, vertical = UiSpacing.PageVertical),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = "\u4eca\u5929\u5df2\u7ecf\u8d5a\u5230",
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                )
                IconButton(onClick = onOpenSettings) {
                    Text(
                        text = "\u2699",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 20.sp,
                    )
                }
            }

            HeroEarningBlock(
                amountText = formatCurrency(
                    cents = snapshot.earnedCentsToday,
                    fractionDigits = earningFractionDigits,
                ),
                workdaySource = workdayResolution.source.label,
            )

            ProgressBlock(
                progress = snapshot.progress.toFloat().coerceIn(0f, 1f),
                elapsedSeconds = snapshot.workedSecondsToday,
                remainingSeconds = remainingSeconds,
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp),
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

            StatusCard(status = snapshot.status)

            Spacer(modifier = Modifier.weight(1f))

            Text(
                text = settings.summaryText,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )

            PrimaryActionButton(
                text = "\u8bbe\u7f6e\u85aa\u8d44",
                onClick = onOpenSettings,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun HeroEarningBlock(
    amountText: String,
    workdaySource: String,
) {
    Box(modifier = Modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = 112.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text(
                text = amountText,
                color = MaterialTheme.colorScheme.primary,
                fontSize = 40.sp,
                fontWeight = FontWeight.Black,
                lineHeight = 44.sp,
                maxLines = 1,
                softWrap = false,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = workdaySource,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
        PiggyJarIllustration(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .size(width = 116.dp, height = 94.dp),
            showJar = false,
        )
        CoinTrail(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 4.dp, end = 4.dp),
        )
    }
}

@Composable
private fun ProgressBlock(
    progress: Float,
    elapsedSeconds: Long,
    remainingSeconds: Long,
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(UiSize.ProgressHeight)
                .clip(RoundedCornerShape(50)),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.18f),
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = "\u25c9 \u5df2\u8fdb\u884c ${formatDuration(elapsedSeconds)}",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.labelSmall,
                maxLines = 1,
            )
            Text(
                text = "\u5269\u4f59 ${formatDuration(remainingSeconds)}",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.labelSmall,
                maxLines = 1,
            )
        }
    }
}

@Composable
private fun StatusCard(status: WorkdayStatus) {
    val isAfterWork = status == WorkdayStatus.AfterWork || status == WorkdayStatus.NotWorkday
    WarmCard(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Text(
                    text = "\u5f53\u524d\u72b6\u6001",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(
                        text = status.label,
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Black,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                    Surface(
                        modifier = Modifier.size(9.dp),
                        shape = RoundedCornerShape(50),
                        color = MaterialTheme.colorScheme.primary,
                    ) {}
                }
            }
            WorkerIllustration(
                resting = isAfterWork,
                modifier = Modifier.size(width = 118.dp, height = 82.dp),
            )
        }
    }
}

private fun formatDuration(totalSeconds: Long): String {
    val hours = totalSeconds / 3600
    val minutes = (totalSeconds % 3600) / 60
    return "${hours} \u5c0f\u65f6 ${minutes} \u5206"
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
