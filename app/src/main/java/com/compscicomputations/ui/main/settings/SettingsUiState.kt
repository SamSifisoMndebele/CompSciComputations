package com.compscicomputations.ui.main.settings

enum class PhoneThemes {
    System,
    Dark,
    Light;
}

data class SettingsUiState(
    val darkTheme: Boolean? = null,
    val dynamicColor: Boolean = true,
)
