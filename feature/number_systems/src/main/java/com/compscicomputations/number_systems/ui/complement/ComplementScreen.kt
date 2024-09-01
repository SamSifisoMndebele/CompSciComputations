package com.compscicomputations.number_systems.ui.complement

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
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.Hyphens
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.compscicomputations.number_systems.data.model.ConvertFrom
import com.compscicomputations.number_systems.utils.BinaryArithmetic.dividedBits
import com.compscicomputations.number_systems.utils.BinaryArithmetic.removedSpaces
import com.compscicomputations.number_systems.utils.binaryFieldRegex
import com.compscicomputations.number_systems.utils.errorTextIf
import com.compscicomputations.number_systems.utils.numberFieldRegex
import com.compscicomputations.theme.comicNeueFamily
import com.compscicomputations.utils.notMatches

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComplementScreen(
    viewModel: ComplementViewModel,
    uiState: ComplementUiState
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
                    color = MaterialTheme.colorScheme.onBackground,
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
                ConvertFrom.complementNotation.forEach {
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

        var complement1Selection by remember { mutableStateOf(TextRange.Zero) }
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            enabled = uiState.convertFrom.complement1,
            value = TextFieldValue(uiState.complement1.dividedBits(8), complement1Selection),
            onValueChange = { value ->
                if (value.text.length > 71 || value.text.notMatches(binaryFieldRegex)) return@OutlinedTextField
                complement1Selection = when (value.text.length) {
                    9, 18, 27, 36, 45, 54, 63 -> TextRange(value.selection.end + 1)
                    else -> value.selection
                }
                viewModel.onComplement1Change(value.text.removedSpaces)
            },
            textStyle = TextStyle(
                lineBreak = LineBreak.Simple,
                hyphens = Hyphens.Auto,
                fontSize = 20.sp,
                fontFamily = comicNeueFamily,
                color = MaterialTheme.colorScheme.onBackground
            ),
            label = { Text(text = "Complement 1") },
            shape = RoundedCornerShape(18.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
            isError = uiState.convertFrom.complement1 && uiState.error != null,
            supportingText = uiState.error errorTextIf uiState.convertFrom.complement1
        )

        var complement2Selection by remember { mutableStateOf(TextRange.Zero) }
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            enabled = uiState.convertFrom.complement2,
            value = TextFieldValue(uiState.complement2.dividedBits(8), complement2Selection),
            onValueChange = { value ->
                if (value.text.length > 71 || value.text.notMatches(binaryFieldRegex)) return@OutlinedTextField
                complement2Selection = when (value.text.length) {
                    9, 18, 27, 36, 45, 54, 63 -> TextRange(value.selection.end + 1)
                    else -> value.selection
                }
                viewModel.onComplement2Change(value.text.removedSpaces)
            },
            textStyle = TextStyle(
                lineBreak = LineBreak.Simple,
                hyphens = Hyphens.Auto,
                fontSize = 20.sp,
                fontFamily = comicNeueFamily,
                color = MaterialTheme.colorScheme.onBackground
            ),
            label = { Text(text = "Complement 2") },
            shape = RoundedCornerShape(18.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
            isError = uiState.convertFrom.complement2 && uiState.error != null,
            supportingText = uiState.error errorTextIf uiState.convertFrom.complement2
        )
    }
}