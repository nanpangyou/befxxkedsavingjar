package com.z.money.ui.about

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.z.money.ui.theme.MoneyTheme

@Composable
fun AboutContent(
    versionName: String,
    onOpenPrivacy: () -> Unit,
    onOpenGitHub: () -> Unit,
    onOpenFeedback: () -> Unit,
    onBack: () -> Unit,
) {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(innerPadding)
                .padding(horizontal = 20.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "\u7a9d\u56ca\u8d39\u8ba1\u7b97\u5668",
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = "v$versionName",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    text = "\u628a\u85aa\u8d44\u6362\u7b97\u6210\u6bcf\u79d2\u6d41\u52a8\u7684\u6570\u5b57\uff0c\u770b\u770b\u4eca\u5929\u5df2\u7ecf\u8d5a\u5230\u4e86\u591a\u5c11\u7a9d\u56ca\u8d39\u3002",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedButton(
                    onClick = onOpenPrivacy,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(text = "\u9690\u79c1\u8bf4\u660e")
                }
                OutlinedButton(
                    onClick = onOpenGitHub,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(text = "GitHub")
                }
                Button(
                    onClick = onOpenFeedback,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    Text(text = "\u53cd\u9988\u95ee\u9898")
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            OutlinedButton(
                onClick = onBack,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(text = "\u8fd4\u56de")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AboutContentPreview() {
    MoneyTheme {
        AboutContent(
            versionName = "0.1.0",
            onOpenPrivacy = {},
            onOpenGitHub = {},
            onOpenFeedback = {},
            onBack = {},
        )
    }
}
