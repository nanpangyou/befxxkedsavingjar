package com.z.money.ui.settings

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class SettingsLayoutMetrics(
    val horizontalPadding: Dp,
    val verticalPadding: Dp,
    val sectionSpacing: Dp,
)

fun settingsLayoutMetrics(maxWidth: Dp, maxHeight: Dp): SettingsLayoutMetrics {
    return SettingsLayoutMetrics(
        horizontalPadding = (maxWidth * 0.055f).coerceIn(16.dp, 28.dp),
        verticalPadding = (maxHeight * 0.026f).coerceIn(18.dp, 24.dp),
        sectionSpacing = (maxHeight * 0.025f).coerceIn(18.dp, 28.dp),
    )
}
