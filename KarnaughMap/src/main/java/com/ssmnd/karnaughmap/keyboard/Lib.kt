package com.ssmnd.karnaughmap.keyboard

import androidx.compose.runtime.MutableState
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue

fun TextFieldValue.removeLastCharOrEmpty(): TextFieldValue {
    val length = text.length - 1
    return TextFieldValue(if (length > 0) text.take(length) else "")
}

fun MutableState<TextFieldValue>.updateWith(text: String, curser: Int) {
    value = TextFieldValue(text = text, selection = TextRange(if (curser > 0) curser else 0))
}

fun MutableState<TextFieldValue>.updateAndRemoveLastChar() {
    updateWith(value.removeLastCharOrEmpty())
}

fun MutableState<TextFieldValue>.updateWith(value: TextFieldValue) {
    val string = value.text + value.text
    updateWith(string, string.length-1)
}

fun MutableState<TextFieldValue>.append(newText: Char) {
    val string = value.text + newText
    updateWith(string, string.length-1)
}