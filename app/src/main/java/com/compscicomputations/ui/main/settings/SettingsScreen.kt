package com.compscicomputations.ui.main.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.compscicomputations.theme.comicNeueFamily
import com.compscicomputations.ui.main.settings.SettingsPreferences.Themes
import com.compscicomputations.ui.main.settings.SettingsPreferences.getDefaultInstance
import com.compscicomputations.ui.main.settings.SettingsSerializer.text
import com.compscicomputations.ui.utils.ui.CompSciScaffold

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel = hiltViewModel(),
    navigateUp: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle(getDefaultInstance())

    CompSciScaffold(
        title = "Settings",
        navigateUp = navigateUp
    ) { contentPadding ->

        Column(
            modifier = Modifier.padding(contentPadding)
        ) {
            Text(
                modifier = Modifier.padding(bottom = 8.dp, top = 24.dp, start = 24.dp, end = 24.dp),
                text = "Appearance",
                fontFamily = comicNeueFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp,
                color = MaterialTheme.colorScheme.primary,
            )
            HorizontalDivider()

            SettingsTheme(
                theme = uiState.theme,
                onThemeChange = viewModel.onThemeChange
            )

            SettingsSwitch(
                text = "Dynamic Colors",
                checked = uiState.dynamicColor,
                onCheckedChange = viewModel.onDynamicColorChange
            )
        }
    }
}

@Composable
private fun SettingsSwitch(
    text: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .height(72.dp)
                .padding(vertical = 8.dp)
                .clickable {
                    onCheckedChange(!checked)
                }
        ) {
            Text(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp),
                text = text,
                fontFamily = comicNeueFamily,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic,
                fontSize = 20.sp,
            )
            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange
            )
        }
        HorizontalDivider()
    }
}

@Composable
private fun SettingsTheme(
    theme: Themes,
    onThemeChange: (Themes) -> Unit
) {
    var openDialog by rememberSaveable { mutableStateOf(false) }
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .height(72.dp)
                .padding(vertical = 8.dp)
                .clickable {
                    openDialog = true
                }
        ) {
            Text(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp),
                text = "Theme",
                fontFamily = comicNeueFamily,
                fontWeight = FontWeight.Bold,
                fontStyle = FontStyle.Italic,
                fontSize = 20.sp,
            )
            Text(
                text = theme.text,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
        HorizontalDivider()
    }
    if (openDialog) {
        Dialog(
            onDismissRequest = { openDialog = false }
        ) {
            Card(
                modifier = Modifier
                    .padding(16.dp),
                shape = RoundedCornerShape(16.dp),
            ) {
                Column(Modifier.selectableGroup()) {
                    Themes.entries.forEach { option ->
                        if (option == Themes.UNRECOGNIZED) return@forEach
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .height(56.dp)
                                .selectable(
                                    selected = (option == theme),
                                    onClick = { onThemeChange(option); openDialog = false },
                                    role = Role.RadioButton
                                )
                                .padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = (option == theme),
                                onClick = null
                            )
                            Text(
                                text = option.text,
                                style = MaterialTheme.typography.bodyLarge,
                                modifier = Modifier.padding(start = 16.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}