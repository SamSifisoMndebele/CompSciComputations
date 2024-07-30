package com.compscicomputations.number_systems.ui.complement

import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults.TrailingIcon
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.compscicomputations.number_systems.utils.StepsDialog
import com.compscicomputations.number_systems.utils.binaryNumbersRegex
import com.compscicomputations.number_systems.utils.decimalFieldRegex
import com.compscicomputations.number_systems.utils.errorTextIf
import com.compscicomputations.theme.comicNeueFamily
import com.compscicomputations.number_systems.utils.ProgressState
import com.compscicomputations.number_systems.utils.isError
import com.compscicomputations.number_systems.utils.isLoading
import com.compscicomputations.number_systems.utils.isSuccess
import com.compscicomputations.ui.utils.ui.ExceptionDialog
import com.compscicomputations.ui.utils.ui.LoadingDialog
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComplementScreen(
    viewModel: ComplementViewModel = koinViewModel()
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
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
            isError = uiState.convertFrom.decimal && uiState.error != null,
            supportingText = uiState.error errorTextIf uiState.convertFrom.decimal
        )

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            enabled = uiState.convertFrom.complement1,
            value = uiState.complement1,
            onValueChange = { if (it.matches(binaryNumbersRegex)) viewModel.onComplement1Change(it) },
            textStyle = TextStyle(
                lineBreak = LineBreak.Simple,
                hyphens = Hyphens.Auto,
                fontSize = 20.sp,
                fontFamily = comicNeueFamily,
                color = MaterialTheme.colorScheme.onBackground
            ),
            label = { Text(text = "Complement 1") },
            shape = RoundedCornerShape(18.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isError = uiState.convertFrom.complement1 && uiState.error != null,
            supportingText = uiState.error errorTextIf uiState.convertFrom.complement1
        )

        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp),
            enabled = uiState.convertFrom.complement2,
            value = uiState.complement2,
            onValueChange = { if (it.matches(binaryNumbersRegex)) viewModel.onComplement2Change(it) },
            textStyle = TextStyle(
                lineBreak = LineBreak.Simple,
                hyphens = Hyphens.Auto,
                fontSize = 20.sp,
                fontFamily = comicNeueFamily,
                color = MaterialTheme.colorScheme.onBackground
            ),
            label = { Text(text = "Complement 2") },
            shape = RoundedCornerShape(18.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            isError = uiState.convertFrom.complement2 && uiState.error != null,
            supportingText = uiState.error errorTextIf uiState.convertFrom.complement2
        )

        Spacer(modifier = Modifier.height(32.dp))
        OutlinedButton(
            onClick = { viewModel.sendPrompt() },
            enabled = uiState.decimal.removeSuffix("-").isNotBlank(),
            modifier = Modifier
                .fillMaxWidth()
                .height(68.dp)
                .padding(bottom = 8.dp),
            shape = RoundedCornerShape(24.dp),
            contentPadding = PaddingValues(vertical = 18.dp),
        ) {
            Icon(
                modifier = Modifier
                    .padding(start = 4.dp)
                    .size(64.dp),
                imageVector = Icons.Default.Info,
                contentDescription = null
            )
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 68.dp),
                text = "Show Steps",
                fontWeight = FontWeight.Bold,
                fontSize = 22.sp,
                fontFamily = comicNeueFamily,
                textAlign = TextAlign.Center,
            )
        }
    }

    val progressState = uiState.progressState
    LoadingDialog(
        message = if (progressState is ProgressState.Loading) progressState.message else "",
        visible = progressState.isLoading,
        onDismiss = { viewModel.setProgressState(ProgressState.Idle) },
    )
    ExceptionDialog(
        message = (if (progressState is ProgressState.Error) progressState.message else "")
            ?:"An unexpected error occurred.",
        visible = progressState.isError,
        onDismiss = { viewModel.setProgressState(ProgressState.Idle) },
    )
    StepsDialog(
        text = uiState.stepsContent,
        visible = progressState.isSuccess,
        onDismiss = { viewModel.setProgressState(ProgressState.Idle) }
    )
}