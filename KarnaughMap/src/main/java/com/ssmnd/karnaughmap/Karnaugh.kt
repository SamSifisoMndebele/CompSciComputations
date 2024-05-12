package com.ssmnd.karnaughmap

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Keyboard
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalTextInputService
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ssmnd.karnaughmap.keyboard.Keyboard
import com.ssmnd.karnaughmap.utils.ListOfMinterms

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Karnaugh(
    booleanExp: MutableState<TextFieldValue>,
    numberVars: Int,
    answers: SnapshotStateList<ListOfMinterms?>,
    selectedAnswer: MutableState<ListOfMinterms?>,
    karnaughMap: @Composable ()-> Unit
) {
    var showKeyboard by rememberSaveable { mutableStateOf(false) }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        val focusRequester = remember { FocusRequester() }
        ///Content
        Column(
            Modifier.fillMaxSize().padding(bottom = 8.dp)
        ) {
            CompositionLocalProvider(
                LocalTextInputService provides null
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp)
                        .focusRequester(focusRequester)
                        .onFocusChanged {
                            showKeyboard = it.hasFocus
                        },
                    value = booleanExp.value,
                    textStyle = TextStyle(
                        fontSize = 20.sp
                    ),
                    onValueChange = {
                        //viewModel.setBooleanExp(it)
                    },
                    label = {
                        Text(text = stringResource(id = R.string.boolean_expression))
                    },
                    shape = RoundedCornerShape(22.dp),
                    //isError = uiState.errors contain FieldType.EMAIL,
                    //supportingText = uiState.errors getMessage FieldType.EMAIL
                )
            }
            AnimatedVisibility(
                modifier = Modifier
                    .wrapContentHeight()
                    .padding(top = 8.dp)
                    .fillMaxWidth(),
                visible = answers.size > 1
            ) {
                var answersDropdown by remember { mutableStateOf(false) }
                ExposedDropdownMenuBox(
                    expanded = answersDropdown,
                    onExpandedChange = { answersDropdown = !answersDropdown }
                ) {
                    OutlinedTextField(
                        readOnly = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor()
                            .clickable { answersDropdown = !answersDropdown }
                            .focusable(false),
                        value = "value",
                        onValueChange = {},
                        trailingIcon =  {
                            Icon(imageVector = if (answersDropdown) Icons.Filled.KeyboardArrowUp
                            else Icons.Filled.KeyboardArrowDown, contentDescription = null)
                        },
                        shape = RoundedCornerShape(22.dp),
                    )
                    ExposedDropdownMenu(
                        expanded = answersDropdown,
                        onDismissRequest = { answersDropdown = false }
                    ) {
                        answers.toList().forEachIndexed { i, listOfMinTerms ->
                            DropdownMenuItem(
                                text = { Text(text = "Answer ${i+1}") },
                                onClick = {
                                    selectedAnswer.value = listOfMinTerms
                                    answersDropdown = false
                                }
                            )
                        }
                    }
                }
            }
            karnaughMap()
            val string = buildAnnotatedString {
                withStyle(SpanStyle(color = Color.Red)) {
                    append("A'B'")
                }
                append(" + ")
                withStyle(SpanStyle(color = Color.Blue)) {
                    append("AB")
                }
                append(" + ")
                withStyle(SpanStyle(color = Color.Green)) {
                    append("D")
                }
            }
            OutlinedTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
                    .focusProperties {
                        canFocus = false
                    },
                value = TextFieldValue(string),
                textStyle = TextStyle(
                    fontSize = 20.sp
                ),
                onValueChange = {
                    //viewModel.setBooleanExp(it)
                },
                label = {
                    Text(text = stringResource(id = R.string.simplied_boolean_expression))
                },
                shape = RoundedCornerShape(22.dp),
                //isError = uiState.errors contain FieldType.EMAIL,
                //supportingText = uiState.errors getMessage FieldType.EMAIL
            )
        }

        ///Keyboard
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            AnimatedVisibility(visible = showKeyboard,
                Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()) {
                Column(
                    Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()) {
                    Keyboard(textFieldState = booleanExp, numberVars = numberVars) {

                    }
                }
            }
            val focusManager = LocalFocusManager.current
            IconButton(
                onClick = {
                    showKeyboard = !showKeyboard
                    if (!showKeyboard) {
                        focusManager.clearFocus()
                    } else {
                        focusRequester.requestFocus()
                    }
                }
            ) {
                Icon(
                    imageVector = if (showKeyboard) Icons.Default.KeyboardArrowDown else Icons.Default.Keyboard,
                    contentDescription = "Close keyboard",
                    tint = MaterialTheme.colorScheme.primary)
            }
        }
    }
}