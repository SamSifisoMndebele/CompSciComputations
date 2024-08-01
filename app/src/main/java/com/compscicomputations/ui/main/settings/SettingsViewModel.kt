package com.compscicomputations.ui.main.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.dataStore
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
) : ViewModel() {
    companion object {
        val Context.settingsDataStore: DataStore<SettingsPreferences> by dataStore(
            fileName = "settings.pb",
            serializer = SettingsSerializer
        )
    }

    val uiState = context.settingsDataStore.data


    val onThemeChange: (SettingsPreferences.Themes) -> Unit = { theme ->
        viewModelScope.launch(Dispatchers.IO) {
            context.settingsDataStore.updateData {
                it.toBuilder()
                    .setTheme(theme)
                    .build()
            }
        }
    }

    val onDynamicColorChange: (Boolean) -> Unit = { dynamicColor ->
        viewModelScope.launch(Dispatchers.IO) {
            context.settingsDataStore.updateData {
                it.toBuilder()
                    .setNotDynamicColor(!dynamicColor)
                    .build()
            }
        }
    }
}