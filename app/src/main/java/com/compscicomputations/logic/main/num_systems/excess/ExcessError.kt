package com.compscicomputations.logic.main.num_systems.excess

enum class ExcessError(val message: String) {
    SIZE_ERROR("Size error"),
    INVALID_DECIMAL("Invalid decimal number"),
    INVALID_EXCESS_BITS("Invalid number of bits"),
    INVALID_EXCESS("Invalid excess binary"),
    INVALID_NUM("Invalid number");

    companion object {
        infix fun errorOf(base: Int) : ExcessError {
            return when(base) {
                0 -> SIZE_ERROR
                2 -> INVALID_EXCESS
                10 -> INVALID_DECIMAL
                else -> INVALID_NUM
            }
        }
    }
}