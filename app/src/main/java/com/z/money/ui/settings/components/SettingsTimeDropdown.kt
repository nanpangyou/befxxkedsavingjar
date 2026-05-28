package com.z.money.ui.settings.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.z.money.ui.common.UiRadius
import com.z.money.ui.common.UiSize
import com.z.money.ui.common.MINUTES_PER_DAY
import com.z.money.ui.common.MINUTES_PER_HOUR
import com.z.money.ui.common.MINUTE_OPTIONS
import java.util.Locale

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
            Text(
                text = ":",
                modifier = Modifier
                    .width(10.dp)
                    .align(androidx.compose.ui.Alignment.CenterVertically),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
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
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = UiSize.SmallButtonHeight),
            shape = androidx.compose.foundation.shape.RoundedCornerShape(UiRadius.Button),
            colors = ButtonDefaults.outlinedButtonColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface,
            ),
        ) {
            Text(
                text = "%02d %s".format(Locale.US, value, suffix),
                fontWeight = FontWeight.SemiBold,
            )
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
