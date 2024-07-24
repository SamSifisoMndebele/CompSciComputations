package com.compscicomputations.feature.number_systems.ui.conversion

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