package com.compscicomputations.polish_expressions.data.model

import androidx.compose.runtime.Stable

@Stable
enum class ConvertFrom(val text: String) {
    Infix("Infix"),
    Postfix("Postfix"),
    Prefix("Prefix");

    val infix: Boolean
        get() = this == Infix
    val postfix: Boolean
        get() = this == Postfix
    val prefix: Boolean
        get() = this == Prefix
}