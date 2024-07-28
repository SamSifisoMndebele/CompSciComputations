package com.compscicomputations.number_systems.utils

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

val decimalNumberRegex = Regex("^-[0-9]+|[0-9]*$")
val decimalFieldRegex = Regex("^[0-9-+ ]*$")
val binaryNumbersRegex = Regex("^[01 ]*$")
val octalNumbersRegex = Regex("^[0-7 ]*$")
val hexNumbersRegex = Regex("^[0-9A-Fa-f ]*$")


infix fun String?.errorTextIf(isSelected: Boolean): @Composable (() -> Unit)? {
    if (!isSelected || this == null) return null
    return {
        Text(text = this, color = MaterialTheme.colorScheme.errorContainer)
    }
}