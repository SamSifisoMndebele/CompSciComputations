package com.compscicomputations.number_systems.ui.floating_point

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
import com.compscicomputations.number_systems.utils.decimalFieldRegex
import com.compscicomputations.number_systems.utils.errorTextIf
import com.compscicomputations.theme.comicNeueFamily
import com.compscicomputations.utils.notMatches

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FloatingPointScreen(
    viewModel: FloatingPointViewModel,
    uiState: FloatingPointUiState
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
                value = uiState.convertFrom.text,
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
                ConvertFrom.floatingPointNotation.forEach {
                    DropdownMenuItem(
                        text = { Text(text = it.text) },
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
            onValueChange = { if (it.matches(decimalFieldRegex)) viewModel.onDecimalChange(it) },
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

//        OutlinedTextField(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(vertical = 4.dp),
//            enabled = uiState.convertFrom.miniFloat,
//            value = uiState.miniFloat,
//            onValueChange = { if (it.matches(binaryNumbersRegex)) viewModel.onMiniFloatChange(it) },
//            textStyle = TextStyle(
//                lineBreak = LineBreak.Simple,
//                hyphens = Hyphens.Auto,
//                fontSize = 20.sp,
//                fontFamily = comicNeueFamily,
//                color = MaterialTheme.colorScheme.onBackground
//            ),
//            label = { Text(text = "Mini float (8 bits)") },
//            shape = RoundedCornerShape(18.dp),
//            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
//            isError = uiState.convertFrom.miniFloat && uiState.error != null,
//            supportingText = uiState.error errorTextIf uiState.convertFrom.miniFloat
//        )

        var binary16Selection by remember { mutableStateOf(TextRange.Zero) }
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            enabled = uiState.convertFrom.binary16,
            value = TextFieldValue(uiState.binary16.dividedBits(1, 5, 10), binary16Selection),
            onValueChange = { value ->
                if (value.text.length > 18 || value.text.notMatches(binaryFieldRegex)) return@OutlinedTextField
                binary16Selection = when (value.text.length) {
                    11, 17 -> TextRange(value.selection.end + 1)
                    else -> value.selection
                }
                viewModel.onBinary16Change(value.text.removedSpaces)
            },
            textStyle = TextStyle(
                lineBreak = LineBreak.Simple,
                hyphens = Hyphens.Auto,
                fontSize = 20.sp,
                fontFamily = comicNeueFamily,
                color = MaterialTheme.colorScheme.onBackground
            ),
            label = { Text(text = "Half precision (16 bits)") },
            shape = RoundedCornerShape(18.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
            isError = uiState.convertFrom.binary16 && uiState.error != null,
            supportingText = uiState.error errorTextIf uiState.convertFrom.binary16
        )


        var binary32Selection by remember { mutableStateOf(TextRange.Zero) }
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            enabled = uiState.convertFrom.binary32,
            value = TextFieldValue(uiState.binary32.dividedBits(1, 8, 23), binary32Selection),
            onValueChange = { value ->
                if (value.text.length > 34 || value.text.notMatches(binaryFieldRegex)) return@OutlinedTextField
                binary32Selection = when (value.text.length) {
                    24, 33 -> TextRange(value.selection.end + 1)
                    else -> value.selection
                }
                viewModel.onBinary32Change(value.text.removedSpaces)
            },
            textStyle = TextStyle(
                lineBreak = LineBreak.Simple,
                hyphens = Hyphens.Auto,
                fontSize = 20.sp,
                fontFamily = comicNeueFamily,
                color = MaterialTheme.colorScheme.onBackground
            ),
            label = { Text(text = "Single precision (32 bits)") },
            shape = RoundedCornerShape(18.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
            isError = uiState.convertFrom.binary32 && uiState.error != null,
            supportingText = uiState.error errorTextIf uiState.convertFrom.binary32
        )

        var binary64Selection by remember { mutableStateOf(TextRange.Zero) }
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            enabled = uiState.convertFrom.binary64,
            value = TextFieldValue(uiState.binary64.dividedBits(1, 11, 52), binary64Selection),
            onValueChange = { value ->
                if (value.text.length > 66 || value.text.notMatches(binaryFieldRegex)) return@OutlinedTextField
                binary64Selection = when (value.text.length) {
                    53, 65 -> TextRange(value.selection.end + 1)
                    else -> value.selection
                }
                viewModel.onBinary64Change(value.text.removedSpaces)
            },
            textStyle = TextStyle(
                lineBreak = LineBreak.Simple,
                hyphens = Hyphens.Auto,
                fontSize = 20.sp,
                fontFamily = comicNeueFamily,
                color = MaterialTheme.colorScheme.onBackground
            ),
            label = { Text(text = "Double precision (64 bits)") },
            shape = RoundedCornerShape(18.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
            isError = uiState.convertFrom.binary64 && uiState.error != null,
            supportingText = uiState.error errorTextIf uiState.convertFrom.binary64
        )
    }
}