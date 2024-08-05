package com.compscicomputations.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.compscicomputations.ui.main.settings.SettingsUiState

private val AppRed = Color(0xFFEE2737)

val DarkColorScheme = darkColorScheme(
    primary = AppRed,
    secondary = AppRed,
    tertiary = AppRed,
    secondaryContainer = AppRed,
    onSecondaryContainer = Color.White,
    onPrimary = Color.White
)

val LightColorScheme = lightColorScheme(
    primary = AppRed,
    secondary = AppRed,
    tertiary = AppRed,
    secondaryContainer = AppRed,
    onSecondaryContainer = Color.White,
    onPrimary = Color.White
    /* Other default colors to override
    background = Color(0xFFFFFBFE),
    surface = Color(0xFFFFFBFE),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onTertiary = Color.White,
    onBackground = Color(0xFF1C1B1F),
    onSurface = Color(0xFF1C1B1F),
    */
)

@Composable
fun CompSciComputationsTheme(
    theme:  SettingsUiState = SettingsUiState(),
    content: @Composable () -> Unit
) {
    val darkTheme = theme.darkTheme ?: isSystemInDarkTheme()
    val context = LocalContext.current
    val colorScheme = when {
        theme.dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}