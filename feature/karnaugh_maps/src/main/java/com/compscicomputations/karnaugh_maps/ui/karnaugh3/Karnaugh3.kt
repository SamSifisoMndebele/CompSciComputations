package com.compscicomputations.karnaugh_maps.ui.karnaugh3

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults.TrailingIcon
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.Hyphens
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidViewBinding
import com.compscicomputations.karnaugh_maps.data.model.ConvertFrom
import com.compscicomputations.karnaugh_maps.data.source.local.datastore.ThreeVarsDataStore
import com.compscicomputations.karnaugh_maps.databinding.Karnaugh3Binding
import com.compscicomputations.karnaugh_maps.kMapView.KMapVariablesImageView
import com.compscicomputations.theme.comicNeueFamily
import com.compscicomputations.ui.auth.isError
import com.compscicomputations.ui.auth.showMessage
import com.compscicomputations.ui.utils.ui.DisableSoftKeyboard
import com.compscicomputations.utils.onVisible
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("ClickableViewAccessibility")
@Composable
fun Karnaugh3Screen(
    modifier: Modifier = Modifier,
    viewModel: Karnaugh3ViewModel,
    uiState: Karnaugh3UiState,
    textFieldState: MutableState<TextFieldValue>,
    textFieldFocus: MutableState<Boolean>,
) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
//    LaunchedEffect(Unit){
//        textFieldStateFlow.value = TextFieldValue(uiState.expression, TextRange(uiState.expression.length))
//    }
    Column(modifier = modifier) {
        var expanded by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
        ) {
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
                modifier = Modifier.clickable {  expanded = !expanded  }
            ) {
                ConvertFrom.entries.forEach {
                    DropdownMenuItem(
                        text = { Text(text = it.text) },
                        onClick = {
                            coroutineScope.launch {
                                ThreeVarsDataStore.setConvertFrom(context, it, when(it) {
                                    ConvertFrom.Expression -> textFieldState.value.text
                                    ConvertFrom.Map -> uiState.minTerms.joinToString(";")
                                })
                            }
                            viewModel.setConvertFrom(it)
                            expanded = false
                        }
                    )
                }
            }
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(MenuAnchorType.PrimaryEditable, true)
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
        }
        HorizontalDivider()

        if (uiState.convertFrom.isExpression) {
            val focusRequester = remember { FocusRequester() }
            val expressionError = when {
                textFieldState.value.text.contains("++") -> "Expression cannot contain ++"
                textFieldState.value.text.contains("+'") -> "Expression cannot contain +'"
                textFieldState.value.text.startsWith("+") -> "Expression cannot start with + character"
                textFieldState.value.text.startsWith("'") -> "Expression cannot start with ' character"
                else -> null
            }

            LaunchedEffect(textFieldState.value) {
                if (expressionError == null) viewModel.onExpressionChange(textFieldState.value) else viewModel.onExpressionChange(TextFieldValue())
                coroutineScope.launch(Dispatchers.IO) {
                    ThreeVarsDataStore.setValue(context, textFieldState.value.text)
                }
            }
            DisableSoftKeyboard {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .focusRequester(focusRequester)
                        .onFocusChanged { textFieldFocus.value = it.isFocused }
                        .onVisible { focusRequester.requestFocus() },
                    interactionSource = null,
                    value = textFieldState.value,
                    onValueChange = {},
                    textStyle = TextStyle(
                        lineBreak = LineBreak.Simple,
                        hyphens = Hyphens.Auto,
                        fontSize = 20.sp,
                        fontFamily = comicNeueFamily,
                        color = MaterialTheme.colorScheme.onBackground
                    ),
                    label = { Text(text = "Expression") },
                    shape = RoundedCornerShape(18.dp),
                    isError = expressionError.isError,
                    supportingText = expressionError.showMessage()
                )
            }
        }
        AndroidViewBinding(Karnaugh3Binding::inflate, Modifier.fillMaxWidth()) {
            answer.setText(uiState.answers[uiState.selected].toString())

            kMap.setMinTerms(uiState.minTerms)
            kMap.setDrawGroups(uiState.answers[uiState.selected])
            kMap.setOnKmapAnimationListener(object : KMapVariablesImageView.OnKmapAnimationListener {
                override fun onAnimate() {
                    answer.setAnimation(true)
                    answer.setAnimationPart(0)
                    answer.setText(uiState.answers[uiState.selected].toString())
                }

                override fun stopAnimate() {
                    answer.setAnimation(false)
                    answer.setText(uiState.answers[uiState.selected].toString())
                }

                override fun onTick(i: Int) {
                    answer.setAnimationPart(i)
                    answer.setText(uiState.answers[uiState.selected].toString())
                }
            })
            if (uiState.convertFrom.isMap) {
                kMap.setOnTouchListener { map, motionEvent ->
                    if (motionEvent.action == 0) true
                    else if (motionEvent.action != 1) false
                    else {
                        val x = (motionEvent.x / map.width.toFloat()).toDouble()
                        val y = (motionEvent.y / map.height.toFloat()).toDouble()
                        kMap.checkInversionBtn(x, y)
                        val findClosestMinTerm = kMap.findClosestMinterm(x, y)
                        if (findClosestMinTerm > -1) {
                            kMap.setMinterms(findClosestMinTerm)
                            viewModel.onMinTermsChange(kMap.minterms)
                            coroutineScope.launch(Dispatchers.IO) {
                                ThreeVarsDataStore.setValue(context, kMap.minterms.joinToString(";"))
                            }
                        }
                        false
                    }
                }
            }
            else kMap.setOnTouchListener(null)
        }
    }
}