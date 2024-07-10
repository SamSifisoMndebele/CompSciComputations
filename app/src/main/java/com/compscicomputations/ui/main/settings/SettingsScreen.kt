package com.compscicomputations.ui.main.settings

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.compscicomputations.ui.utils.CompSciScaffold

@Composable
fun SettingsScreen(
    navigateUp: () -> Unit,
) {
    CompSciScaffold(
        title = "Settings",
        navigateUp = navigateUp
    ) {
        Text(text = "Settings screen.")
    }
}