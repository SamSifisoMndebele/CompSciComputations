package com.compscicomputations.ui.main.settings

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class AppPreferences @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private const val PREFS_NAME = "app_preferences"
        const val THEME_KEY = "theme"
        const val DYNAMIC_COLOR_KEY = "dynamic_color"

        data class UiSettings(
            val theme: Themes2,
            val dynamicColors: Boolean
        )
    }
    private val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun getTheme(): Themes2 {
        val theme = preferences.getString(THEME_KEY, Themes2.SYSTEM.name) ?: Themes2.SYSTEM.name
        return Themes2.valueOf(theme)
    }

    fun setTheme(theme: Themes2) {
        preferences.edit().putString(THEME_KEY, theme.name).apply()
    }

    fun getDynamicColor(): Boolean {
        return preferences.getBoolean(DYNAMIC_COLOR_KEY, true)
    }

    fun setDynamicColor(dynamicColors: Boolean) {
        preferences.edit().putBoolean(DYNAMIC_COLOR_KEY, dynamicColors).apply()
    }

    fun clear() {
        preferences.edit().clear().apply()
    }



}