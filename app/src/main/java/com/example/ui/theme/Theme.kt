package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = NeonCyan,
    onPrimary = TextDark,
    primaryContainer = Color(0x3300F0FF),
    onPrimaryContainer = NeonCyan,
    secondary = NeonEmerald,
    onSecondary = TextDark,
    secondaryContainer = Color(0x3310E797),
    onSecondaryContainer = NeonEmerald,
    tertiary = CyberViolet,
    onTertiary = TextPrimary,
    background = DeepSpace,
    onBackground = TextPrimary,
    surface = DarkNavyGlass,
    onSurface = TextPrimary,
    surfaceVariant = GlassCardBackground,
    onSurfaceVariant = TextSecondary,
    outline = GlassBorderSubtle
)

@Composable
fun MyApplicationTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
