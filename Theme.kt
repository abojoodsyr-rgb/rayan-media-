package com.rayan.rayantube.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val RayantubeLightColors = lightColorScheme(
    primary = RayanRed,
    onPrimary = RayanCream,
    secondary = RayanKohli,
    onSecondary = RayanCream,
    background = LightBackground,
    onBackground = RayanKohli,
    surface = LightSurface,
    onSurface = RayanKohli,
    surfaceVariant = RayanCream,
    onSurfaceVariant = RayanGray
)

private val RayantubeDarkColors = darkColorScheme(
    primary = RayanRed,
    onPrimary = RayanCream,
    secondary = RayanCream,
    onSecondary = RayanKohli,
    background = DarkBackground,
    onBackground = RayanCream,
    surface = DarkSurface,
    onSurface = RayanCream,
    surfaceVariant = RayanKohliLight,
    onSurfaceVariant = RayanGray
)

@Composable
fun RayantubeTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) RayantubeDarkColors else RayantubeLightColors
    MaterialTheme(
        colorScheme = colors,
        typography = RayantubeTypography,
        content = content
    )
}
