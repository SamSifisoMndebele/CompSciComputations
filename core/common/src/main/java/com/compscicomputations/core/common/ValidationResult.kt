package com.compscicomputations.core.common

data class ValidationResult(
    val successful: Boolean = false,
    val errorMessage: String? = null
)
