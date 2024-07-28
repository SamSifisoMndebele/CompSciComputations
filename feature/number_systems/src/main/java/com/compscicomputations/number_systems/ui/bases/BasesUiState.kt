package com.compscicomputations.number_systems.ui.bases

import com.compscicomputations.ui.utils.ProgressState

data class BasesUiState(
    val decimal: String = "",
    val binary: String = "",
    val octal: String = "",
    val hexadecimal: String = "",
    val ascii: String = "",
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
    ASCII("ASCII Character(s)");

    val isDecimal: Boolean
        get() = this == Decimal
    val isBinary: Boolean
        get() = this == Binary
    val isOctal: Boolean
        get() = this == Octal
    val isHexadecimal: Boolean
        get() = this == Hexadecimal
    val isASCII: Boolean
        get() = this == ASCII
}