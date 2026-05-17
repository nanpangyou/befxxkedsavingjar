package com.z.money.ui.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import java.time.DayOfWeek
import java.util.Locale

@Composable
fun MetricTile(
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
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.Start,
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = title,
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.labelLarge,
                )
                Text(
                    text = value,
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        }
    }
}

@Composable
fun SettingsSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(
            text = title,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            content = content,
        )
    }
}

@Composable
@OptIn(ExperimentalLayoutApi::class)
fun WorkDaysSelector(
    selectedDays: Set<DayOfWeek>,
    onSelectedDaysChange: (Set<DayOfWeek>) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "\u5de5\u4f5c\u65e5",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.labelLarge,
        )
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            DayOfWeek.entries.forEach { day ->
                FilterChip(
                    selected = day in selectedDays,
                    onClick = {
                        val updatedDays = if (day in selectedDays) {
                            selectedDays - day
                        } else {
                            selectedDays + day
                        }
                        onSelectedDaysChange(updatedDays.ifEmpty { selectedDays })
                    },
                    label = { Text(text = day.shortLabel) },
                )
            }
        }
    }
}

@Composable
fun SettingsNumberField(
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
fun SettingsTimeDropdown(
    label: String,
    minutes: Int,
    onMinutesChange: (Int) -> Unit,
) {
    val maxMinutes = if (label == "\u4e0b\u73ed\u65f6\u95f4") {
        MINUTES_PER_DAY
    } else {
        MINUTES_PER_DAY - 1
    }
    val maxHour = maxMinutes / MINUTES_PER_HOUR
    val normalizedMinutes = minutes.coerceIn(0, maxMinutes)
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
                options = (0..maxHour).toList(),
                suffix = "\u65f6",
                onValueChange = { selectedHour ->
                    val selectedMinute = if (selectedHour == 24) 0 else minute
                    onMinutesChange(selectedHour * MINUTES_PER_HOUR + selectedMinute)
                },
                modifier = Modifier.weight(1f),
            )
            TimePartDropdown(
                value = minute,
                options = if (hour == 24) listOf(0) else MINUTE_OPTIONS,
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
