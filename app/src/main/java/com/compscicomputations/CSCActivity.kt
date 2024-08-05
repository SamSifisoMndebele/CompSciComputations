package com.compscicomputations

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import com.compscicomputations.ui.main.settings.PhoneThemes
import com.compscicomputations.ui.main.settings.SettingsUiState
import com.compscicomputations.ui.main.settings.SettingsViewModel.Companion.DYNAMIC_COLOR_KEY
import com.compscicomputations.ui.main.settings.SettingsViewModel.Companion.PHONE_THEME_KEY
import com.compscicomputations.ui.main.settings.SettingsViewModel.Companion.PREFS_NAME
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

abstract class CSCActivity: ComponentActivity() {
    private val _themeState = MutableStateFlow(SettingsUiState())
    protected val themeState = _themeState.asStateFlow()
    private val listener = SharedPreferences.OnSharedPreferenceChangeListener { preferences, _ ->
        _themeState.value = SettingsUiState(
            when(PhoneThemes.entries[preferences.getInt(PHONE_THEME_KEY, PhoneThemes.System.ordinal)]) {
                PhoneThemes.Dark -> true
                PhoneThemes.Light -> false
                else -> null
            },
            preferences.getBoolean(DYNAMIC_COLOR_KEY, true)
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        _themeState.value = SettingsUiState(
            when(PhoneThemes.entries[sharedPreferences.getInt(PHONE_THEME_KEY, PhoneThemes.System.ordinal)]) {
                PhoneThemes.Dark -> true
                PhoneThemes.Light -> false
                else -> null
            },
            sharedPreferences.getBoolean(DYNAMIC_COLOR_KEY, true)
        )
        sharedPreferences.registerOnSharedPreferenceChangeListener(listener)
    }
}