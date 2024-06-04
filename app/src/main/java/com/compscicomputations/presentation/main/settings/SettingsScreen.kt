package com.compscicomputations.presentation.main.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.compscicomputations.presentation.main.AppBar


@Composable
fun SettingsScreen(
    padding: PaddingValues = PaddingValues(start = 8.dp, end = 8.dp, top = 2.dp),
    navigateUp: () -> Unit,
) {
    Column(
        modifier = Modifier.padding(padding)
            .fillMaxSize(),
    ) {
        AppBar(title = "Settings", navigateUp = navigateUp) {
            //TODO Menu
        }
        Text(text = "Settings screen.")

    }
}