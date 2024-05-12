package com.compscicomputations.logic.main.num_systems.bases

data class BaseConversion(
    val decimal: String = "",
    val binary: String = "",
    val octal: String = "",
    val hexadecimal: String = "",
    val ascii: String = "",
    var error: BaseError? = null
)
