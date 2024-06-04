package com.compscicomputations.presentation.auth

import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable


fun String?.showMessage(): @Composable (() -> Unit)? {
    if (this == null) return null
    return {
        Text(text = this, color = OutlinedTextFieldDefaults.colors().errorLabelColor)
    }
}
val String?.isError
    get() = this != null
