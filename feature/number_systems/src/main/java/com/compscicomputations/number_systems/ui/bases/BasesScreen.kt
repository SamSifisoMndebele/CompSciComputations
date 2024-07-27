package com.compscicomputations.number_systems.ui.bases

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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.compscicomputations.number_systems.ui.bases.ConvertFrom.Companion.isASCII
import com.compscicomputations.number_systems.ui.bases.ConvertFrom.Companion.isBinary
import com.compscicomputations.number_systems.ui.bases.ConvertFrom.Companion.isDecimal
import com.compscicomputations.number_systems.ui.bases.ConvertFrom.Companion.isHexadecimal
import com.compscicomputations.number_systems.ui.bases.ConvertFrom.Companion.isOctal
import com.compscicomputations.number_systems.utils.binaryNumbersRegex
import com.compscicomputations.number_systems.utils.decimalFieldRegex
import com.compscicomputations.number_systems.utils.errorTextIf
import com.compscicomputations.number_systems.utils.hexNumbersRegex
import com.compscicomputations.number_systems.utils.octalNumbersRegex
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BasesScreen(
    padding: PaddingValues,
    viewModel: BasesViewModel = koinViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
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
                ConvertFrom.list.forEach {
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
            enabled = uiState.convertFrom.isDecimal,
            value = viewModel.decimal.value,
            onValueChange = { if (it.matches(decimalFieldRegex)) viewModel.fromDecimal(it) },
            textStyle = TextStyle(
                lineBreak = LineBreak.Simple,
                hyphens = Hyphens.Auto,
                fontSize = 18.sp
            ),
            label = { Text(text = "Decimal") },
            shape = RoundedCornerShape(18.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            isError = uiState.convertFrom.isDecimal && viewModel.error.value != null,
            supportingText = viewModel.error.value errorTextIf uiState.convertFrom.isDecimal
        )

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            enabled = uiState.convertFrom.isBinary,
            value = viewModel.binary.value,
            onValueChange = { if (it.matches(binaryNumbersRegex)) viewModel.fromBinary(it) },
            textStyle = TextStyle(
                lineBreak = LineBreak.Simple,
                hyphens = Hyphens.Auto,
                fontSize = 18.sp
            ),
            label = { Text(text = "Binary") },
            shape = RoundedCornerShape(18.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isError = uiState.convertFrom.isBinary && viewModel.error.value != null,
            supportingText = viewModel.error.value errorTextIf uiState.convertFrom.isBinary
        )

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            enabled = uiState.convertFrom.isOctal,
            value = viewModel.octal.value,
            onValueChange = { if (it.matches(octalNumbersRegex)) viewModel.fromOctal(it) },
            label = { Text(text = "Octal") },
            shape = RoundedCornerShape(18.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isError = uiState.convertFrom.isOctal && viewModel.error.value != null,
            supportingText = viewModel.error.value errorTextIf uiState.convertFrom.isOctal
        )

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            enabled = uiState.convertFrom.isHexadecimal,
            value = viewModel.hexadecimal.value,
            onValueChange = { if (it.matches(hexNumbersRegex)) viewModel.fromHex(it) },
            textStyle = TextStyle(
                lineBreak = LineBreak.Simple,
                hyphens = Hyphens.Auto,
                fontSize = 18.sp
            ),
            label = { Text(text = "Hexadecimal") },
            shape = RoundedCornerShape(18.dp),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Characters,
                autoCorrectEnabled = false,
                keyboardType = KeyboardType.Text
            ),
            isError = uiState.convertFrom.isHexadecimal && viewModel.error.value != null,
            supportingText = viewModel.error.value errorTextIf uiState.convertFrom.isHexadecimal
        )

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            enabled = uiState.convertFrom.isASCII,
            value = viewModel.ascii.value,
            onValueChange = { viewModel.fromAscii(it) },
            textStyle = TextStyle(
                lineBreak = LineBreak.Simple,
                hyphens = Hyphens.Auto,
                fontSize = 18.sp
            ),
            label = { Text(text = "ASCII Characters") },
            shape = RoundedCornerShape(18.dp),
            keyboardOptions = KeyboardOptions(
                autoCorrectEnabled = false,
                keyboardType = KeyboardType.Text
            ),
            isError = uiState.convertFrom.isASCII && viewModel.error.value != null,
            supportingText = viewModel.error.value errorTextIf uiState.convertFrom.isASCII
        )
    }
}