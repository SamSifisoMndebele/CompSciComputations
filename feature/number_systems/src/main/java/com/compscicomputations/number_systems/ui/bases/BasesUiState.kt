package com.compscicomputations.number_systems.ui.bases

import com.compscicomputations.ui.utils.ProgressState

data class BasesUiState(
    val decimal: String = "",
    val binary: String = "",
    val octal: String = "",
    val hexadecimal: String = "",
    val ascii: String = "",

    val error: BaseError? = null,

    val convertFrom: ConvertFrom = ConvertFrom.Decimal,
    val progressState: ProgressState = ProgressState.Idle,
)

sealed interface ConvertFrom {
    val name: String
    data object Decimal : ConvertFrom {
        override val name = "Decimal"
    }
    data object Binary : ConvertFrom {
        override val name = "Binary"
    }
    data object Octal : ConvertFrom {
        override val name = "Octal"
    }
    data object Hexadecimal : ConvertFrom {
        override val name = "Hexadecimal"
    }
    data object ASCII : ConvertFrom {
        override val name = "ASCII Character(s)"
    }

    companion object {
        val list = listOf(
            Decimal, Binary, Octal, Hexadecimal, ASCII
        )
        val ConvertFrom.isDecimal: Boolean
            get() = this is ConvertFrom.Decimal
        val ConvertFrom.isBinary: Boolean
            get() = this is ConvertFrom.Binary
        val ConvertFrom.isOctal: Boolean
            get() = this is ConvertFrom.Octal
        val ConvertFrom.isHexadecimal: Boolean
            get() = this is ConvertFrom.Hexadecimal
        val ConvertFrom.isASCII: Boolean
            get() = this is ConvertFrom.ASCII
    }
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