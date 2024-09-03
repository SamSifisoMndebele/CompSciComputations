package com.compscicomputations.polish_expressions.data.model

import androidx.compose.runtime.Stable

@Stable
enum class ConvertTo(val text: String) {
    Postfix("Postfix"),
    Prefix("Prefix"),
    PostfixPrefix("Postfix and Prefix");

    val postfix: Boolean
        get() = this == Postfix
    val prefix: Boolean
        get() = this == Prefix
    val postfixPrefix: Boolean
        get() = this == PostfixPrefix
}