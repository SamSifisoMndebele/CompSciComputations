package com.compscicomputations.logic.num_systems.excess

data class ExcessConversion(
    val decimal: String = "",
    val excess: String = "",
    val excessIdentifier: String = "",
    val excessBits: Int = 8,
    val error: ExcessError? = null
)
