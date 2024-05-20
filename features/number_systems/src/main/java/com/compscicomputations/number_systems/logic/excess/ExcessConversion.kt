package com.compscicomputations.number_systems.logic.excess

data class ExcessConversion(
    val decimal: String = "",
    val excess: String = "",
    val excessIdentifier: String = "",
    val excessBits: Int = 8,
    val error: ExcessError? = null
)
