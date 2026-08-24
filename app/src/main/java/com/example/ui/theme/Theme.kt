package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val GamingColorScheme = darkColorScheme(
    primary = CyberCyan,
    onPrimary = GamingDarkBg,
    primaryContainer = GamingSurfaceElevated,
    onPrimaryContainer = CyberCyan,
    secondary = EmberOrange,
    onSecondary = GamingDarkBg,
    secondaryContainer = GamingSurfaceVariant,
    onSecondaryContainer = EmberOrangeLight,
    tertiary = DiamondBlue,
    onTertiary = GamingDarkBg,
    background = GamingDarkBg,
    onBackground = TextWhite,
    surface = GamingSurface,
    onSurface = TextWhite,
    surfaceVariant = GamingSurfaceVariant,
    onSurfaceVariant = TextGray,
    outline = GamingBorder,
    error = ErrorRed,
    onError = Color.White
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true, // Gaming apps are best in sleek dark theme
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = GamingColorScheme,
        typography = Typography,
        content = content
    )
}
