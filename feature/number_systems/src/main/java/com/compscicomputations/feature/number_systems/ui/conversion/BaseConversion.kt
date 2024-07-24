package com.compscicomputations.feature.number_systems.ui.conversion

data class BaseConversion(
    val decimal: String = "",
    val binary: String = "",
    val octal: String = "",
    val hexadecimal: String = "",
    val ascii: String = "",
    var error: BaseError? = null
)
