package com.z.money.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = MintGreen,
    onPrimary = ForestBackground,
    secondary = DarkCoinGold,
    tertiary = DarkBrickCoral,
    background = ForestBackground,
    onBackground = IvoryText,
    surface = ForestSurface,
    surfaceVariant = ForestContainer,
    surfaceContainer = ForestContainer,
    onSurface = IvoryText,
    onSurfaceVariant = DarkMutedText,
    outline = DarkMutedText.copy(alpha = 0.42f),
    primaryContainer = MintGreen.copy(alpha = 0.22f),
    onPrimaryContainer = IvoryText,
    secondaryContainer = DarkCoinGold.copy(alpha = 0.2f),
    onSecondaryContainer = IvoryText,
)

private val LightColorScheme = lightColorScheme(
    primary = MossGreen,
    onPrimary = Color.White,
    secondary = CoinGold,
    tertiary = BrickCoral,
    background = WarmBackground,
    onBackground = InkText,
    surface = WarmSurface,
    surfaceVariant = WarmContainer,
    surfaceContainer = WarmContainer,
    onSurface = InkText,
    onSurfaceVariant = MutedText,
    outline = MutedText.copy(alpha = 0.32f),
    primaryContainer = MossGreen.copy(alpha = 0.14f),
    onPrimaryContainer = InkText,
    secondaryContainer = CoinGold.copy(alpha = 0.18f),
    onSecondaryContainer = InkText,
)

@Composable
fun MoneyTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
