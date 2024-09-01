package com.compscicomputations.number_systems.ui.bases

import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults.TrailingIcon
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.Hyphens
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.compscicomputations.number_systems.data.model.ConvertFrom
import com.compscicomputations.number_systems.utils.binaryFieldRegex
import com.compscicomputations.number_systems.utils.errorTextIf
import com.compscicomputations.number_systems.utils.hexadecimalFieldRegex
import com.compscicomputations.number_systems.utils.numberFieldRegex
import com.compscicomputations.number_systems.utils.octalFieldRegex
import com.compscicomputations.theme.comicNeueFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasesScreen(
    viewModel: BasesViewModel,
    uiState: BasesUiState
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        var expanded by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
                    .clickable { expanded = !expanded }
                    .focusable(false)
                    .padding(vertical = 4.dp),
                value = uiState.convertFrom.name,
                onValueChange = {},
                textStyle = TextStyle(
                    lineBreak = LineBreak.Simple,
                    hyphens = Hyphens.Auto,
                    fontSize = 20.sp,
                    fontFamily = comicNeueFamily,
                    color = MaterialTheme.colorScheme.onBackground
                ),
                label = { Text(text = "Convert from_") },
                readOnly = true,
                singleLine = true,
                shape = RoundedCornerShape(18.dp),
                trailingIcon = { TrailingIcon(expanded = expanded) },
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                ConvertFrom.baseN.forEach {
                    DropdownMenuItem(
                        text = { Text(text = it.name) },
                        onClick = {
                            viewModel.setConvertFrom(it)
                            expanded = false
                        }
                    )
                }
            }
        }
        HorizontalDivider()

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            enabled = uiState.convertFrom.decimal,
            value = uiState.decimal,
            onValueChange = { if (it.matches(numberFieldRegex)) viewModel.onDecimalChange(it) },
            textStyle = TextStyle(
                lineBreak = LineBreak.Simple,
                hyphens = Hyphens.Auto,
                fontSize = 20.sp,
                fontFamily = comicNeueFamily,
                color = MaterialTheme.colorScheme.onBackground
            ),
            label = { Text(text = "Decimal") },
            shape = RoundedCornerShape(18.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal, imeAction = ImeAction.Done),
            isError = uiState.convertFrom.decimal && uiState.error != null,
            supportingText = uiState.error errorTextIf uiState.convertFrom.decimal
        )

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            enabled = uiState.convertFrom.binary,
            value = uiState.binary,
            onValueChange = { if (it.matches(binaryFieldRegex)) viewModel.onBinaryChange(it) },
            textStyle = TextStyle(
                lineBreak = LineBreak.Simple,
                hyphens = Hyphens.Auto,
                fontSize = 20.sp,
                fontFamily = comicNeueFamily,
                color = MaterialTheme.colorScheme.onBackground
            ),
            label = { Text(text = "Binary") },
            shape = RoundedCornerShape(18.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
            isError = uiState.convertFrom.binary && uiState.error != null,
            supportingText = uiState.error errorTextIf uiState.convertFrom.binary
        )

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            enabled = uiState.convertFrom.octal,
            value = uiState.octal,
            onValueChange = { if (it.matches(octalFieldRegex)) viewModel.onOctalChange(it) },
            textStyle = TextStyle(
                lineBreak = LineBreak.Simple,
                hyphens = Hyphens.Auto,
                fontSize = 20.sp,
                fontFamily = comicNeueFamily,
                color = MaterialTheme.colorScheme.onBackground
            ),
            label = { Text(text = "Octal") },
            shape = RoundedCornerShape(18.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
            isError = uiState.convertFrom.octal && uiState.error != null,
            supportingText = uiState.error errorTextIf uiState.convertFrom.octal
        )

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            enabled = uiState.convertFrom.hexadecimal,
            value = uiState.hexadecimal,
            onValueChange = { if (it.matches(hexadecimalFieldRegex)) viewModel.onHexadecimalChange(it) },
            textStyle = TextStyle(
                lineBreak = LineBreak.Simple,
                hyphens = Hyphens.Auto,
                fontSize = 20.sp,
                fontFamily = comicNeueFamily,
                color = MaterialTheme.colorScheme.onBackground
            ),
            label = { Text(text = "Hexadecimal") },
            shape = RoundedCornerShape(18.dp),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Characters,
                autoCorrectEnabled = false,
                keyboardType = KeyboardType.Text,
                imeAction = ImeAction.Done
            ),
            isError = uiState.convertFrom.hexadecimal && uiState.error != null,
            supportingText = uiState.error errorTextIf uiState.convertFrom.hexadecimal
        )

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            enabled = uiState.convertFrom.unicode,
            value = uiState.unicode,
            onValueChange = { viewModel.onUnicodeChange(it) },
            textStyle = TextStyle(
                lineBreak = LineBreak.Simple,
                hyphens = Hyphens.Auto,
                fontSize = 20.sp,
                fontFamily = comicNeueFamily,
                color = MaterialTheme.colorScheme.onBackground
            ),
            label = { Text(text = "Unicode Characters") },
            shape = RoundedCornerShape(18.dp),
            keyboardOptions = KeyboardOptions(
                autoCorrectEnabled = false,
                keyboardType = KeyboardType.Text
            ),
            isError = uiState.convertFrom.unicode && uiState.error != null,
            supportingText = uiState.error errorTextIf uiState.convertFrom.unicode
        )
    }
}