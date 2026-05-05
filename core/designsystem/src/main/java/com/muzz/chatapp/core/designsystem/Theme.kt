package com.muzz.chatapp.core.designsystem

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val MuzzLightColorScheme = lightColorScheme(
    primary = MuzzColors.Pink,
    onPrimary = MuzzColors.OnBubbleSent,
    primaryContainer = MuzzColors.PinkSoft,
    onPrimaryContainer = MuzzColors.PinkDark,
    background = MuzzColors.Background,
    onBackground = MuzzColors.OnSurface,
    surface = MuzzColors.Surface,
    onSurface = MuzzColors.OnSurface,
    surfaceVariant = MuzzColors.BubbleReceived,
    onSurfaceVariant = MuzzColors.OnBubbleReceived,
    outline = MuzzColors.Divider,
)

@Composable
fun MuzzTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = MuzzLightColorScheme,
        typography = MuzzTypography,
        content = content,
    )
}
