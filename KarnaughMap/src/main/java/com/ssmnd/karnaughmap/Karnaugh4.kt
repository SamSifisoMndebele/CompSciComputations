package com.ssmnd.karnaughmap

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.ssmnd.karnaughmap.kMapView.KMap4VariablesImageView
import com.ssmnd.karnaughmap.keyboard.Keyboard

@Composable
fun Karnaugh4() {

    var showKeyboard by rememberSaveable { mutableStateOf(false) }
    Column(
        modifier = Modifier
            .padding(top = 8.dp)
            .fillMaxSize(),
    ) {
        val text = remember { mutableStateOf(TextFieldValue()) }
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        ) {
            item {
                Card(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    AndroidView(
                        modifier = Modifier
                            .padding(vertical = 8.dp)
                            .fillMaxWidth()
                            .wrapContentHeight(),
                        factory = {
                        KMap4VariablesImageView(it)
                    })
                    Text(text = "answers Dropdown")
                    AndroidView(factory = {
                        MinTermTextView(it)
                    })

                    /*AndroidView(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        factory = { context ->
                            AppCompatEditText(context).apply {
                                isFocusable = true
                                isFocusableInTouchMode = true
                                showSoftInputOnFocus = false
                                onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus -> 
                                    showKeyboard = hasFocus
                                }
                            }
                        },
                        update = { view ->
                            view.setText("ABC + uiState")
                            //view.setTextColor(textColor)
                            //view.setSelection(text.length)
                        }
                    )*/
                    val focusRequester = remember { FocusRequester() }
                    val keyboardController = LocalSoftwareKeyboardController.current
                    Box {
                        OutlinedTextField(
//                            readOnly = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .focusRequester(focusRequester)
                                .onFocusChanged {
                                    keyboardController?.hide()
                                }
                                /*.focusProperties {
                                    canFocus = false
                                }*/,
                            value = text.value,
                            onValueChange = {
                                text.value = it
                            },
                            label = {
                                Text(text = stringResource(id = R.string.simply_boolean_expression))
                            },
                            shape = RoundedCornerShape(22.dp),
                            keyboardOptions = KeyboardOptions(
                                capitalization = KeyboardCapitalization.Characters,
                                keyboardType = KeyboardType.Text,
                                autoCorrect = false
                            ),
                            keyboardActions = KeyboardActions(

                            )
                            //isError = uiState.errors contain FieldType.EMAIL,
                            //supportingText = uiState.errors getMessage FieldType.EMAIL
                        )
                        //Box(modifier = Modifier.fillMaxWidth().fillParentMaxWidth())
                    }

                    /**/

                    Card(
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.25f)
                        )
                    ) {
                        /*AndroidView(
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentHeight(),
                            factory = { context ->
                                Karnaugh4Fragment().rootView
                            }
                        )*/
                        Button(onClick = { showKeyboard = true; focusRequester.requestFocus() }) {
                            Text(text = "Show Keyboard")
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.weight(1f))
        AnimatedVisibility(visible = showKeyboard,
            Modifier
                .fillMaxWidth()
                .wrapContentHeight()) {
            Column(
                Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()) {
                Keyboard(textFieldState = text) {

                }
                IconButton(onClick = { showKeyboard = false }) {
                    Icon(imageVector = Icons.Default.KeyboardArrowDown, contentDescription = "Close keyboard",
                        tint = MaterialTheme.colorScheme.primary)
                }
            }
        }
    }
}