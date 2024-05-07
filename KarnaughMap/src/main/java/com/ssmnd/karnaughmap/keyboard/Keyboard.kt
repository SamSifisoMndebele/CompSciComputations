package com.ssmnd.karnaughmap.keyboard

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun Keyboard(
    modifier: Modifier = Modifier,
    textFieldState: MutableState<TextFieldValue>?,
    //onAction: ((key: Key) -> Unit)? = null,
    onKeyPress: (key: Key) -> Unit
) {
    Card(
        modifier = modifier.padding(horizontal = 4.dp),
        shape = RoundedCornerShape(18.dp)
    ) {
        LazyVerticalGrid(columns = GridCells.Fixed(4)) {
            items(listOf(Key.A, Key.B, Key.Or)) { key ->
                KeyButton(
                    key = key,
                ) {
                    onKeyPress(it)
                    processKeys(it, textFieldState)
                }
            }
            item {
                Row {
                    KeyButton(
                        modifier = Modifier.weight(1f),
                        key = Key.Left,
                    ) {
                        onKeyPress(it)
                        processKeys(it, textFieldState)
                    }
                    KeyButton(
                        modifier = Modifier.weight(1f),
                        key = Key.Right,
                    ) {
                        onKeyPress(it)
                        processKeys(it, textFieldState)
                    }
                }
            }
            items(listOf(Key.C, Key.D, Key.Not, Key.Backspace)) { key ->
                KeyButton(
                    key = key,
                ) {
                    onKeyPress(it)
                    processKeys(it, textFieldState)
                }
            }
        }
    }
}

fun processKeys(key: Key, state: MutableState<TextFieldValue>?) {

    when (key) {
        Key.A, Key.B, Key.C, Key.D, Key.Or, Key.Not -> state?.append(key.value)
        Key.Backspace -> state?.updateAndRemoveLastChar()
        Key.Left -> {
            //TODO()
        }
        Key.Right -> {
            //TODO()
        }
    }
}

@Preview
@Composable
fun KeyboardViewPreview() {
    Keyboard(textFieldState = null) {}
}