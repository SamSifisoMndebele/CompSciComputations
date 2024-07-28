package com.compscicomputations.number_systems.ui.bases

import com.compscicomputations.ui.utils.ProgressState

data class BasesUiState(
    val decimal: String = "",
    val binary: String = "",
    val octal: String = "",
    val hexadecimal: String = "",
    val ascii: String = "",
    val convertFrom: ConvertFrom = ConvertFrom.Decimal,

    val error: BaseError? = null,

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

enum class BaseError(val message: String) {
    SIZE_ERROR("Size error"),
    INVALID_DECIMAL("Invalid decimal number"),
    INVALID_BINARY("Invalid binary number"),
    INVALID_OCT("Invalid octal number"),
    INVALID_HEX("Invalid hexadecimal number"),
    INVALID_NUM("Invalid number");

    companion object {
        infix fun errorOf(base: Int) : BaseError {
            return when(base) {
                0 -> SIZE_ERROR
                2 -> INVALID_BINARY
                8 -> INVALID_OCT
                10 -> INVALID_DECIMAL
                16 -> INVALID_HEX
                else -> INVALID_NUM
            }
        }
    }
}