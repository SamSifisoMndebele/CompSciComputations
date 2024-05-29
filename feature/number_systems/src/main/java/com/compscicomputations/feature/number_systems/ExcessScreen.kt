package com.compscicomputations.feature.number_systems

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.compscicomputations.feature.number_systems.model.ExcessViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExcessScreen(
    padding: PaddingValues
) {
    val model: ExcessViewModel = viewModel()
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(padding),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        item {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                value = model.excessBits.intValue.toString(),
                onValueChange = {
                    if (it.isBlank()) model.setExcessBits(it)
                    else if (!it.contains('.') && it.toInt() <= 32) {
                        model.setExcessBits(it)
                        if (model.decimal.value.isNotBlank())
                            model.fromDecimal(model.decimal.value)
                    }
                },
                label = {
                    Text(text = "Bits Length < 32 bits")
                },
                singleLine = true,
                shape = RoundedCornerShape(18.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
//                isError = uiState.errors contain FieldType.NAMES,
//                supportingText = uiState.errors getMessage FieldType.NAMES
            )
        }
        item {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                value = model.decimal.value,
                onValueChange = {
                    if (it.contains('.')) return@OutlinedTextField
                    model.fromDecimal(it)
                                },
                label = {
                    Text(text = "Signed Decimal")
                },
                singleLine = true,
                shape = RoundedCornerShape(18.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
//                isError = uiState.errors contain FieldType.NAMES,
//                supportingText = uiState.errors getMessage FieldType.NAMES
            )
        }
        item {
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                value = model.excess.value,
                onValueChange = {
                    if (it.contains('.')) return@OutlinedTextField
                    if (it.matches(Regex("[0*1*]*[1*0*]*"))) {
                        model.fromExcess(it)
                    }
                },
                label = {
                    Text(text = "Excess Notation")
                },
                singleLine = true,
                shape = RoundedCornerShape(18.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
//                isError = uiState.errors contain FieldType.NAMES,
//                supportingText = uiState.errors getMessage FieldType.NAMES
            )
        }
    }
}