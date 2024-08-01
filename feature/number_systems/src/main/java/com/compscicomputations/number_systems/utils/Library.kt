package com.compscicomputations.number_systems.utils

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable


val decimalFieldRegex = Regex("[0-9-+Ee., ]*")
val numberFieldRegex = Regex("[\\d +-]*")
val binaryFieldRegex = Regex("[01 ]*")
val octalFieldRegex = Regex("[0-7 ]*")
val hexadecimalFieldRegex = Regex("[0-9A-Fa-f ]*")

val decimalRegex = Regex("[+-]?(\\d+([.]\\d*)?([eE][+-]?\\d+)?|[.]\\d+([eE][+-]?\\d+)?)")
val numberRegex = Regex("[+-]?\\d+")
val binaryRegex = Regex("[01]+")
val octalRegex = Regex("[0-7]+")
val hexadecimalRegex = Regex("[0-9A-Fa-f]+")


infix fun String?.errorTextIf(isSelected: Boolean): @Composable (() -> Unit)? {
    if (!isSelected || this == null) return null
    return {
        Text(text = this, color = MaterialTheme.colorScheme.errorContainer)
    }
}

val bitLength = arrayOf(4, 8, 16, 32, 64)