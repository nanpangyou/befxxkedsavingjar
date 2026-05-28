package com.z.money.ui.settings.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.z.money.ui.common.UiSpacing

@Composable
fun SettingsSection(
    title: String,
    icon: String = "",
    @DrawableRes iconRes: Int? = null,
    content: @Composable ColumnScope.() -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (iconRes != null || icon.isNotBlank()) {
                Surface(
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    shape = MaterialTheme.shapes.small,
                ) {
                    if (iconRes != null) {
                        Image(
                            painter = painterResource(iconRes),
                            contentDescription = null,
                            modifier = Modifier
                                .padding(4.dp)
                                .size(24.dp),
                        )
                    } else {
                        Text(
                            text = icon,
                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.titleSmall,
                        )
                    }
                }
            }
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
            )
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(UiSpacing.ItemGap),
            content = content,
        )
    }
}
