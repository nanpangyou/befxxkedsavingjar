package com.z.money.ui.about

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.z.money.R
import com.z.money.ui.common.AssetImage
import com.z.money.ui.common.PiggyJarIllustration
import com.z.money.ui.common.SecondaryActionButton
import com.z.money.ui.common.UiRadius
import com.z.money.ui.common.UiSpacing
import com.z.money.ui.common.WarmCard
import com.z.money.ui.common.WorkerIllustration
import com.z.money.ui.theme.MoneyTheme

@Composable
fun AboutContent(
    versionName: String,
    onOpenPrivacy: () -> Unit,
    onOpenGitHub: () -> Unit,
    onOpenFeedback: () -> Unit,
    onBack: () -> Unit,
) {
    Scaffold(containerColor = MaterialTheme.colorScheme.background) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(innerPadding)
                .padding(horizontal = UiSpacing.PageHorizontal, vertical = UiSpacing.PageVertical),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = "\u5173\u4e8e",
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Black,
            )

            AboutScene()

            WarmCard(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Surface(
                        shape = RoundedCornerShape(UiRadius.Card),
                        color = MaterialTheme.colorScheme.primaryContainer,
                    ) {
                        Image(
                            painter = painterResource(R.drawable.ic_launcher_foreground),
                            contentDescription = null,
                            modifier = Modifier
                                .size(78.dp)
                                .clip(RoundedCornerShape(UiRadius.Card)),
                            contentScale = ContentScale.Fit,
                        )
                    }
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "\u7a9d\u56ca\u8d39\u8ba1\u7b97\u5668",
                            color = MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Black,
                            maxLines = 1,
                        )
                        Text(
                            text = "v$versionName",
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
                Text(
                    text = "\u628a\u85aa\u8d44\u6362\u7b97\u6210\u6bcf\u79d2\u6d41\u52a8\u7684\u6570\u5b57\uff0c\u770b\u770b\u4eca\u5929\u5df2\u7ecf\u8d5a\u5230\u4e86\u591a\u5c11\u7a9d\u56ca\u8d39\u3002",
                    modifier = Modifier.padding(top = 14.dp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium,
                    lineHeight = 22.sp,
                )
            }

            WarmCard(modifier = Modifier.fillMaxWidth()) {
                AboutRow(iconRes = R.drawable.asset_shield, title = "\u9690\u79c1\u8bf4\u660e", onClick = onOpenPrivacy)
                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.24f))
                AboutRow(iconRes = R.drawable.asset_code, title = "GitHub", onClick = onOpenGitHub)
                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.24f))
                AboutRow(iconRes = R.drawable.asset_feedback, title = "\u53cd\u9988\u95ee\u9898", onClick = onOpenFeedback)
            }

            Spacer(modifier = Modifier.weight(1f))

            SecondaryActionButton(
                text = "\u8fd4\u56de",
                onClick = onBack,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

@Composable
private fun AboutScene() {
    WarmCard(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
        ) {
            WorkerIllustration(
                resting = true,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .size(width = 150.dp, height = 96.dp),
            )
            PiggyJarIllustration(
                showJar = true,
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .size(width = 150.dp, height = 112.dp),
            )
            AssetImage(
                resId = R.drawable.asset_pig,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .size(width = 112.dp, height = 86.dp),
            )
        }
    }
}

@Composable
private fun AboutRow(
    iconRes: Int,
    title: String,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 13.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AssetImage(
                resId = iconRes,
                modifier = Modifier.size(28.dp),
            )
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
            )
        }
        Text(
            text = "\u203a",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AboutContentPreview() {
    MoneyTheme {
        AboutContent(
            versionName = "0.3.0",
            onOpenPrivacy = {},
            onOpenGitHub = {},
            onOpenFeedback = {},
            onBack = {},
        )
    }
}
