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
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun Keyboard(
    modifier: Modifier = Modifier,
    textFieldState: MutableState<TextFieldValue>?,
    numberVars: Int,
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
            item {
                KeyButton(
                    key = Key.C,
                    enabled = numberVars > 2
                ) {
                    onKeyPress(it)
                    processKeys(it, textFieldState)
                }
            }
            item {
                KeyButton(
                    key = Key.D,
                    enabled = numberVars > 3
                ) {
                    onKeyPress(it)
                    processKeys(it, textFieldState)
                }
            }
            items(listOf(Key.Not, Key.Backspace)) { key ->
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

private fun processKeys(key: Key, state: MutableState<TextFieldValue>?) {

    when (key) {
        Key.A, Key.B, Key.C, Key.D, Key.Or, Key.Not -> state?.append(key.value)
        Key.Backspace -> state?.backspace()
        Key.Left -> state?.moveLeft()
        Key.Right -> state?.moveRight()
    }
}

private fun MutableState<TextFieldValue>.updateWith(text: String, select: Int) {
    value = TextFieldValue(text = text, selection = TextRange(if (select > 0) select else 0))
}


private fun MutableState<TextFieldValue>.append(newChar: Char) {
    val start = value.selection.min
    val end = value.text.length - start
    val string = value.text.take(start) + newChar + value.text.takeLast(end)
    updateWith(string, start+1)
}

private fun MutableState<TextFieldValue>.backspace() {
    if (value.selection.min != value.selection.max) {
        val start = value.selection.min
        if (start < 0) return
        val end = value.text.length - value.selection.max

        updateWith(value.text.take(start) + value.text.takeLast(end), start)
    } else {
        val start = value.selection.min-1
        if (start < 0) return
        val end = value.text.length - value.selection.max

        updateWith(value.text.take(start) + value.text.takeLast(end), start)
    }
}

private fun MutableState<TextFieldValue>.moveLeft() {
    updateWith(value.text, value.selection.min - 1)
}

private fun MutableState<TextFieldValue>.moveRight() {
    updateWith(value.text, value.selection.min + 1)
}

@Preview
@Composable
fun KeyboardViewPreview() {
    Keyboard(textFieldState = null, numberVars = 4) {}
}