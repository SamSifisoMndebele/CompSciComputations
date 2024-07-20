package com.compscicomputations.ui.main.settings

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.compscicomputations.ui.utils.ui.CompSciScaffold

@Composable
fun SettingsScreen(
    navigateUp: () -> Unit,
) {
    CompSciScaffold(
        title = "Settings",
        navigateUp = navigateUp
    ) { contentPadding ->
        Text(text = "Settings screen.")
    }
}