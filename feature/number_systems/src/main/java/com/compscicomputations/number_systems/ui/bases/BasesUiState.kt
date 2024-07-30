package com.compscicomputations.number_systems.ui.bases

import com.compscicomputations.number_systems.utils.ProgressState

data class BasesUiState(
    val decimal: String = "",
    val binary: String = "",
    val octal: String = "",
    val hexadecimal: String = "",
    val unicode: String = "",
    val convertFrom: ConvertFrom = ConvertFrom.Decimal,

    val error: String? = null,

    val progressState: ProgressState = ProgressState.Idle,

    val stepsContent: String = "",
)

enum class ConvertFrom(val text: String) {
    Decimal("Decimal"),
    Binary("Binary"),
    Octal("Octal"),
    Hexadecimal("Hexadecimal"),
    Unicode("Unicode Character(s)");

    val decimal: Boolean
        get() = this == Decimal
    val binary: Boolean
        get() = this == Binary
    val octal: Boolean
        get() = this == Octal
    val hexadecimal: Boolean
        get() = this == Hexadecimal
    val unicode: Boolean
        get() = this == Unicode
}