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
import com.compscicomputations.ui.main.settings.AppPreferences
import com.compscicomputations.ui.main.settings.Themes2

private val AppRed = Color(0xFFEE2737)

private val DarkColorScheme = darkColorScheme(
    primary = AppRed,
    secondary = AppRed,
    tertiary = AppRed,
    secondaryContainer = AppRed,
    onSecondaryContainer = Color.White,
    onPrimary = Color.White
)

private val LightColorScheme = lightColorScheme(
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
    main: Boolean = false,
    content: @Composable () -> Unit
) {
    val context = LocalContext.current
    val preferences = AppPreferences(context)
//    val settings = if (main) {
//        val preferences by context.settingsDataStore.data
//            .collectAsStateWithLifecycle(initialValue = SettingsPreferences.getDefaultInstance())
//        preferences
//    } else {
//        runBlocking(Dispatchers.IO) {
//            context.settingsDataStore.data.first()
//        }
//    }

//    val settings by context.settingsDataStore.data
//        .collectAsStateWithLifecycle(initialValue = SettingsPreferences.getDefaultInstance())


    val darkTheme = when(preferences.getTheme()) {
        Themes2.DARK -> true
        Themes2.LIGHT -> false
//        SettingsPreferences.Themes.DARK -> true
//        SettingsPreferences.Themes.LIGHT -> false
        else -> isSystemInDarkTheme()
    }

    val colorScheme = when {
        preferences.getDynamicColor() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
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