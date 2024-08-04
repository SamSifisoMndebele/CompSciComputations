package com.compscicomputations.ui.main.settings

import android.content.Context
import android.content.SharedPreferences
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val preferences: AppPreferences,
) : ViewModel() {
    companion object {
        val Context.settingsDataStore: DataStore<SettingsPreferences> by dataStore(
            fileName = "settings.pb",
            serializer = SettingsSerializer
        )
    }

    private val _uiState = MutableStateFlow(AppPreferences.Companion.UiSettings(
        preferences.getTheme(),
        preferences.getDynamicColor()
    ))
    val uiState = context.settingsDataStore.data



    val onThemeChange: (SettingsPreferences.Themes) -> Unit = { theme ->
        viewModelScope.launch(Dispatchers.IO) {
            preferences.setTheme(when(theme) {
                SettingsPreferences.Themes.DARK -> Themes2.DARK
                SettingsPreferences.Themes.LIGHT -> Themes2.LIGHT
                SettingsPreferences.Themes.SYSTEM -> Themes2.SYSTEM
                else -> Themes2.SYSTEM
            })
            context.settingsDataStore.updateData {
                it.toBuilder()
                    .setTheme(theme)
                    .build()
            }
        }
    }

    val onDynamicColorChange: (Boolean) -> Unit = { dynamicColor ->
        viewModelScope.launch(Dispatchers.IO) {
            preferences.setDynamicColor(dynamicColor)
            context.settingsDataStore.updateData {
                it.toBuilder()
                    .setNotDynamicColor(!dynamicColor)
                    .build()
            }
        }
    }
}