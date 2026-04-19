package com.shadow.apps.memoir.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = Cyan6,
    onPrimary = Color.White,
    primaryContainer = Cyan1,
    onPrimaryContainer = Cyan9,
    secondary = Terracotta6,
    onSecondary = Color.White,
    secondaryContainer = Terracotta0,
    onSecondaryContainer = Terracotta9,
    tertiary = Slate5,
    onTertiary = Color.White,
    tertiaryContainer = Slate1,
    onTertiaryContainer = Slate9,
    background = Slate0,
    onBackground = Slate9,
    surface = Color.White,
    onSurface = Slate9,
    surfaceVariant = Slate0,
    onSurfaceVariant = Slate6,
    outline = Slate3,
    outlineVariant = Slate2,
    inverseSurface = Slate8,
    inverseOnSurface = Slate1,
    inversePrimary = Cyan4,
    error = Color(0xFFB3261E),
    onError = Color.White,
    errorContainer = Color(0xFFF9DEDC),
    onErrorContainer = Color(0xFF410E0B),
)

private val DarkColorScheme = darkColorScheme(
    primary = Cyan4,
    onPrimary = Cyan9,
    primaryContainer = Cyan7,
    onPrimaryContainer = Cyan1,
    secondary = Terracotta3,
    onSecondary = Terracotta9,
    secondaryContainer = Terracotta7,
    onSecondaryContainer = Terracotta0,
    tertiary = Slate3,
    onTertiary = Slate8,
    tertiaryContainer = Slate6,
    onTertiaryContainer = Slate1,
    background = Slate9,
    onBackground = Slate1,
    surface = Slate8,
    onSurface = Slate1,
    surfaceVariant = Slate7,
    onSurfaceVariant = Slate2,
    outline = Slate4,
    outlineVariant = Slate6,
    inverseSurface = Slate1,
    inverseOnSurface = Slate8,
    inversePrimary = Cyan6,
    error = Color(0xFFF2B8B5),
    onError = Color(0xFF601410),
    errorContainer = Color(0xFF8C1D18),
    onErrorContainer = Color(0xFFF9DEDC),
)

@Composable
fun ShadowMemoirTheme(
    darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme,
        typography = Typography,
        content = content,
    )
}
