package com.z.money.ui.settings.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import com.z.money.ui.common.UiRadius
import com.z.money.ui.common.UiSize

@Composable
fun <T> SegmentedSelector(
    options: List<T>,
    selected: T,
    label: (T) -> String,
    onSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(UiSize.SmallButtonHeight),
        shape = RoundedCornerShape(UiRadius.Button),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(Dp.Hairline, MaterialTheme.colorScheme.outline.copy(alpha = 0.55f)),
    ) {
        Row(modifier = Modifier.fillMaxWidth()) {
            options.forEach { option ->
                Segment(
                    text = label(option),
                    selected = option == selected,
                    onClick = { onSelected(option) },
                )
            }
        }
    }
}

@Composable
private fun RowScope.Segment(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Surface(
        modifier = Modifier
            .weight(1f)
            .height(UiSize.SmallButtonHeight)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(UiRadius.Button),
        color = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
        contentColor = if (selected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurfaceVariant,
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = text,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelLarge,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
            )
        }
    }
}
