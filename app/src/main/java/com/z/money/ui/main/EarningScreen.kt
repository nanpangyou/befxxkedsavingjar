package com.z.money.ui.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.z.money.data.SettingsRepository
import com.z.money.data.UserSettings
import com.z.money.data.toSalaryInput
import com.z.money.data.toWorkSchedule
import com.z.money.domain.EarningSnapshot
import com.z.money.domain.IncomeCalculator
import com.z.money.domain.SalaryPeriod
import com.z.money.domain.WorkdayStatus
import com.z.money.ui.theme.MoneyTheme
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.time.LocalDateTime
import java.util.Locale

@Composable
fun EarningScreen() {
    val context = LocalContext.current
    val repository = remember { SettingsRepository(context.applicationContext) }
    val persistedSettings by repository.settings.collectAsState(initial = UserSettings())
    val settings = EarningSettings.fromUserSettings(persistedSettings)
    val scope = rememberCoroutineScope()
    var showingSettings by remember { mutableStateOf(false) }
    var now by remember { mutableStateOf(LocalDateTime.now()) }

    LaunchedEffect(Unit) {
        while (true) {
            now = LocalDateTime.now()
            delay(1_000)
        }
    }

    if (showingSettings) {
        SettingsContent(
            settings = settings,
            onSave = { draft ->
                scope.launch {
                    repository.save(draft.toUserSettings())
                    showingSettings = false
                }
            },
            onBack = { showingSettings = false },
        )
    } else {
        EarningContent(
            snapshot = IncomeCalculator.snapshot(
                salary = settings.toSalaryInput(),
                schedule = settings.toWorkSchedule(),
                now = now,
            ),
            settings = settings,
            onOpenSettings = { showingSettings = true },
        )
    }
}

@Composable
private fun EarningContent(
    snapshot: EarningSnapshot,
    settings: EarningSettings,
    onOpenSettings: () -> Unit,
) {
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

@Composable
private fun SettingsContent(
    settings: EarningSettings,
    onSave: (EarningSettings) -> Unit,
    onBack: () -> Unit,
) {
    var draft by remember(settings) { mutableStateOf(settings) }

    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(innerPadding)
                .padding(horizontal = 24.dp, vertical = 28.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp),
        ) {
            Text(
                text = "\u85aa\u8d44\u8bbe\u7f6e",
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
            )

            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                SalaryPeriod.entries.forEach { period ->
                    FilterChip(
                        selected = draft.salaryPeriod == period,
                        onClick = { draft = draft.copy(salaryPeriod = period) },
                        label = { Text(text = period.label) },
                    )
                }
            }

            SettingsNumberField(
                label = "\u85aa\u8d44\u91d1\u989d",
                value = draft.salaryAmount,
                onValueChange = { draft = draft.copy(salaryAmount = it) },
            )

            SettingsNumberField(
                label = "\u5e74\u5de5\u4f5c\u65e5",
                value = draft.annualWorkDays,
                onValueChange = { draft = draft.copy(annualWorkDays = it) },
            )

            SettingsTimeDropdown(
                label = "\u4e0a\u73ed\u65f6\u95f4",
                minutes = draft.workStartMinutes,
                onMinutesChange = { draft = draft.copy(workStartMinutes = it) },
            )

            SettingsTimeDropdown(
                label = "\u4e0b\u73ed\u65f6\u95f4",
                minutes = draft.workEndMinutes,
                onMinutesChange = { draft = draft.copy(workEndMinutes = it) },
            )

            Spacer(modifier = Modifier.weight(1f))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                OutlinedButton(
                    onClick = onBack,
                    modifier = Modifier.weight(1f),
                ) {
                    Text(text = "\u8fd4\u56de")
                }
                Button(
                    onClick = { onSave(draft) },
                    modifier = Modifier.weight(1f),
                ) {
                    Text(text = "\u4fdd\u5b58")
                }
            }
        }
    }
}

@Composable
private fun SettingsNumberField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
) {
    OutlinedTextField(
        value = value,
        onValueChange = { onValueChange(it.filter { char -> char.isDigit() || char == '.' }) },
        modifier = Modifier.fillMaxWidth(),
        label = { Text(text = label) },
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
    )
}

@Composable
private fun SettingsTimeDropdown(
    label: String,
    minutes: Int,
    onMinutesChange: (Int) -> Unit,
) {
    val normalizedMinutes = minutes.coerceIn(0, MINUTES_PER_DAY - 1)
    val hour = normalizedMinutes / MINUTES_PER_HOUR
    val minute = normalizedMinutes % MINUTES_PER_HOUR

    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = label,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.labelLarge,
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            TimePartDropdown(
                value = hour,
                options = (0..23).toList(),
                suffix = "\u65f6",
                onValueChange = { onMinutesChange(it * MINUTES_PER_HOUR + minute) },
                modifier = Modifier.weight(1f),
            )
            TimePartDropdown(
                value = minute,
                options = MINUTE_OPTIONS,
                suffix = "\u5206",
                onValueChange = { onMinutesChange(hour * MINUTES_PER_HOUR + it) },
                modifier = Modifier.weight(1f),
            )
        }
    }
}

@Composable
private fun TimePartDropdown(
    value: Int,
    options: List<Int>,
    suffix: String,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        OutlinedButton(
            onClick = { expanded = true },
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(text = "%02d %s".format(Locale.US, value, suffix))
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.heightIn(max = 280.dp),
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(text = "%02d %s".format(Locale.US, option, suffix)) },
                    onClick = {
                        expanded = false
                        onValueChange(option)
                    },
                )
            }
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

private data class EarningSettings(
    val salaryPeriod: SalaryPeriod = SalaryPeriod.Monthly,
    val salaryAmount: String = "10000",
    val annualWorkDays: String = "250",
    val workStartMinutes: Int = 9 * MINUTES_PER_HOUR,
    val workEndMinutes: Int = 17 * MINUTES_PER_HOUR,
) {
    val summaryText: String
        get() = "\u5f53\u524d\u4f7f\u7528\uff1a${salaryPeriod.label} ${salaryAmount.ifBlank { "0" }} \u5143\uff0c${annualWorkDays.ifBlank { "0" }} \u4e2a\u5de5\u4f5c\u65e5\uff0c${workStartMinutes.toTimeText()}-${workEndMinutes.toTimeText()}\u3002"

    fun toSalaryInput() = toUserSettings().toSalaryInput()

    fun toWorkSchedule() = toUserSettings().toWorkSchedule()

    fun toUserSettings(): UserSettings {
        return UserSettings(
            salaryPeriod = salaryPeriod,
            salaryAmountYuan = salaryAmount.toDoubleOrNull()?.coerceAtLeast(0.0) ?: 0.0,
            annualWorkDays = annualWorkDays.toIntOrNull()?.coerceAtLeast(1) ?: 1,
            workStartMinutes = workStartMinutes,
            workEndMinutes = workEndMinutes,
        )
    }

    companion object {
        fun fromUserSettings(settings: UserSettings): EarningSettings {
            return EarningSettings(
                salaryPeriod = settings.salaryPeriod,
                salaryAmount = settings.salaryAmountYuan.toDisplayString(),
                annualWorkDays = settings.annualWorkDays.toString(),
                workStartMinutes = settings.workStartMinutes,
                workEndMinutes = settings.workEndMinutes,
            )
        }
    }
}

private fun Double.toDisplayString(): String {
    return if (this % 1.0 == 0.0) {
        toLong().toString()
    } else {
        toString()
    }
}

private fun Int.toTimeText(): String {
    val minutes = coerceIn(0, MINUTES_PER_DAY - 1)
    return "%02d:%02d".format(Locale.US, minutes / MINUTES_PER_HOUR, minutes % MINUTES_PER_HOUR)
}

private val MINUTE_OPTIONS = (0..55 step 5).toList()
private const val MINUTES_PER_HOUR = 60
private const val MINUTES_PER_DAY = 24 * MINUTES_PER_HOUR

private val SalaryPeriod.label: String
    get() = when (this) {
        SalaryPeriod.Monthly -> "\u6708\u85aa"
        SalaryPeriod.Annual -> "\u5e74\u85aa"
    }

@Preview(showBackground = true)
@Composable
private fun EarningContentPreview() {
    val settings = EarningSettings()
    MoneyTheme {
        EarningContent(
            snapshot = IncomeCalculator.snapshot(
                salary = settings.toSalaryInput(),
                schedule = settings.toWorkSchedule(),
                now = LocalDateTime.of(2026, 5, 13, 10, 30),
            ),
            settings = settings,
            onOpenSettings = {},
        )
    }
}
