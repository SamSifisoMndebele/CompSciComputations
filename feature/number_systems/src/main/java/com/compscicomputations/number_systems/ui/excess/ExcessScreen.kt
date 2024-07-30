package com.compscicomputations.number_systems.ui.excess

import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.Hyphens
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.compscicomputations.theme.comicNeueFamily
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExcessScreen(
    viewModel: ExcessViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
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
                ConvertFrom.entries.forEach {
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
            value = uiState.excessBits.toString(),
            onValueChange = {
                if (it.isBlank()) viewModel.setExcessBits(it)
                else if (!it.contains('.') && it.toInt() <= 32) {
                    viewModel.setExcessBits(it)
                    if (uiState.decimal.isNotBlank())
                        viewModel.fromDecimal(uiState.decimal)
                }
            },
            label = {
                Text(text = "Bits Length < 32 bits")
            },
            singleLine = true,
            shape = RoundedCornerShape(18.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
//                isError = progressState.errors contain FieldType.NAMES,
//                supportingText = progressState.errors getMessage FieldType.NAMES
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            value = uiState.decimal,
            onValueChange = {
                if (it.contains('.')) return@OutlinedTextField
                viewModel.fromDecimal(it)
            },
            label = {
                Text(text = "Signed Decimal")
            },
            singleLine = true,
            shape = RoundedCornerShape(18.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
//                isError = progressState.errors contain FieldType.NAMES,
//                supportingText = progressState.errors getMessage FieldType.NAMES
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            value = uiState.excess,
            onValueChange = {
                if (it.contains('.')) return@OutlinedTextField
                if (it.matches(Regex("[0*1*]*[1*0*]*"))) {
                    viewModel.fromExcess(it)
                }
            },
            label = {
                Text(text = "Excess Notation")
            },
            singleLine = true,
            shape = RoundedCornerShape(18.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
//                isError = progressState.errors contain FieldType.NAMES,
//                supportingText = progressState.errors getMessage FieldType.NAMES
        )
    }
}