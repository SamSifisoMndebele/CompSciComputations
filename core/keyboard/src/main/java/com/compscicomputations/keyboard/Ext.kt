package com.compscicomputations.keyboard

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue

//fun TextFieldValue.removeLastCharOrEmpty(): TextFieldValue {
//    val length = text.length - 1
//    return TextFieldValue(if (length > 0) text.take(length) else "", TextRange(length))
//}

//fun MutableState<TextFieldValue>.updateWith(text: String?) {
//    value = value.copy(text = text ?: "")
//}

//fun MutableState<TextFieldValue>.updateAndRemoveLastChar() {
////    value = TextFieldValue(value.removeLastCharOrEmpty().text)
////    updateWith(value.removeLastCharOrEmpty())
//}

//fun MutableState<TextFieldValue>.updateWith(value: TextFieldValue?) {
//    updateWith(value?.text)
//}

fun MutableState<TextFieldValue>.backspace() {
    Log.d("Backspace", value.toString())
    if (value.selection.start == 0) return
    val position = if (value.selection.start > 0) value.selection.start - 1 else 0
    val text = value.text.substring(0, position) + value.text.substring(position + 1, value.text.length)
    value = TextFieldValue(text, TextRange(position))
    Log.d("Backspace", value.toString())
}
fun MutableState<TextFieldValue>.leftArrow() {
    Log.d("LeftArrow", value.toString())
    val position = if (value.selection.start > 0) value.selection.start - 1 else 0
    value = TextFieldValue(value.text, TextRange(position))
    Log.d("LeftArrow", value.toString())
}
fun MutableState<TextFieldValue>.rightArrow() {
    Log.d("RightArrow", value.toString())
    val position = if (value.selection.start < value.text.length) value.selection.start + 1 else value.text.length
    value = TextFieldValue(value.text, TextRange(position))
    Log.d("RightArrow", value.toString())
}

fun MutableState<TextFieldValue>.append(char: String) {
    Log.d("Append", value.toString())
    val position = value.selection.start
    val text = value.text.substring(0, position) + char + value.text.substring(position, value.text.length)

    value = TextFieldValue(text, TextRange(position + 1))
    Log.d("Append", value.toString())
}