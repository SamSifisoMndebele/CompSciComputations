package com.compscicomputations.ui.main.num_systems

import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults.TrailingIcon
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.Hyphens
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.compscicomputations.ui.main.errorTextIf
import com.compscicomputations.ui.main.num_systems.model.BasesViewModel
import com.compscicomputations.ui.theme.binaryNumbersRegex
import com.compscicomputations.ui.theme.decimalFieldRegex
import com.compscicomputations.ui.theme.hexNumbersRegex
import com.compscicomputations.ui.theme.octalNumbersRegex

enum class ConversionOption(val string: String) {
    DECIMAL("Decimal"), BINARY("Binary"),
    OCTAL("Octal"), HEXADECIMAL("Hexadecimal"),
    ASCII("ASCII Character(s)")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConversionScreen(
    padding: PaddingValues,
) {
    val model: BasesViewModel = viewModel()

    var selectedOption by rememberSaveable { mutableStateOf(ConversionOption.DECIMAL) }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        item {
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
                    value = selectedOption.string,
                    onValueChange = {},
                    textStyle = TextStyle(
                        lineBreak = LineBreak.Simple,
                        hyphens = Hyphens.Auto,
                        fontSize = 18.sp
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
                    ConversionOption.entries.forEach {
                        DropdownMenuItem(
                            text = { Text(text = it.name) },
                            onClick = {
                                selectedOption = it
                                expanded = false
                            }
                        )
                    }
                }
            }
            HorizontalDivider()
        }
        item {
            val isSelected = selectedOption == ConversionOption.DECIMAL
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                enabled = isSelected,
                value = model.decimal.value,
                onValueChange = { if (it.matches(decimalFieldRegex)) model.fromDecimal(it) },
                textStyle = TextStyle(
                    lineBreak = LineBreak.Simple,
                    hyphens = Hyphens.Auto,
                    fontSize = 18.sp
                ),
                label = { Text(text = "Decimal") },
                shape = RoundedCornerShape(18.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                isError = isSelected && model.error.value != null,
                supportingText = model.error.value errorTextIf isSelected
            )
        }
        item {
            val isSelected = selectedOption == ConversionOption.BINARY
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
//                    .focusProperties { canFocus = isSelected },
                enabled = isSelected,
                value = model.binary.value,
                onValueChange = { if (it.matches(binaryNumbersRegex)) model.fromBinary(it) },
                textStyle = TextStyle(
                    lineBreak = LineBreak.Simple,
                    hyphens = Hyphens.Auto,
                    fontSize = 18.sp
                ),
                label = { Text(text = "Binary") },
                shape = RoundedCornerShape(18.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = isSelected && model.error.value != null,
                supportingText = model.error.value errorTextIf isSelected
            )
        }
        item {
            val isSelected = selectedOption == ConversionOption.OCTAL
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                enabled = isSelected,
                value = model.octal.value,
                onValueChange = { if (it.matches(octalNumbersRegex)) model.fromOctal(it) },
                label = { Text(text = "Octal") },
                shape = RoundedCornerShape(18.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = isSelected && model.error.value != null,
                supportingText = model.error.value errorTextIf isSelected
            )
        }
        item {
            val isSelected = selectedOption == ConversionOption.HEXADECIMAL
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                enabled = isSelected,
                value = model.hexadecimal.value,
                onValueChange = { if (it.matches(hexNumbersRegex)) model.fromHex(it) },
                textStyle = TextStyle(
                    lineBreak = LineBreak.Simple,
                    hyphens = Hyphens.Auto,
                    fontSize = 18.sp
                ),
                label = { Text(text = "Hexadecimal") },
                shape = RoundedCornerShape(18.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text,
                    autoCorrect = false, capitalization = KeyboardCapitalization.Characters),
                isError = isSelected && model.error.value != null,
                supportingText = model.error.value errorTextIf isSelected
            )
        }
        item {
            val isSelected = selectedOption == ConversionOption.ASCII
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                enabled = isSelected,
                value = model.ascii.value,
                onValueChange = { model.fromAscii(it) },
                textStyle = TextStyle(
                    lineBreak = LineBreak.Simple,
                    hyphens = Hyphens.Auto,
                    fontSize = 18.sp
                ),
                label = { Text(text = "ASCII Characters") },
                shape = RoundedCornerShape(18.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, autoCorrect = false),
                isError = isSelected && model.error.value != null,
                supportingText = model.error.value errorTextIf isSelected
            )
        }
    }

}