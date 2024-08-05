package com.compscicomputations.ui.main.settings

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compscicomputations.di.IoDispatcher
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {
    companion object {
        const val PREFS_NAME = "app_preferences"
        const val PHONE_THEME_KEY = "phone_theme"
        const val DYNAMIC_COLOR_KEY = "dynamic_color"
    }

    private val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        _uiState.value = SettingsUiState(
            when(PhoneThemes.entries[sharedPreferences.getInt(PHONE_THEME_KEY, PhoneThemes.System.ordinal)]) {
                PhoneThemes.Dark -> true
                PhoneThemes.Light -> false
                else -> null
            },
            sharedPreferences.getBoolean(DYNAMIC_COLOR_KEY, true)
        )
    }

    val onThemeChange: (PhoneThemes) -> Unit = { theme ->
        viewModelScope.launch(ioDispatcher) {
            sharedPreferences.edit().putInt(PHONE_THEME_KEY, theme.ordinal).apply()
            _uiState.value = _uiState.value.copy(darkTheme = when(theme) {
                PhoneThemes.Dark -> true
                PhoneThemes.Light -> false
                else -> null
            })
        }
    }

    val onDynamicColorChange: (Boolean) -> Unit = { dynamicColor ->
        viewModelScope.launch(ioDispatcher) {
            sharedPreferences.edit().putBoolean(DYNAMIC_COLOR_KEY, dynamicColor).apply()
            _uiState.value = _uiState.value.copy(dynamicColor = dynamicColor)
        }
    }
}