package com.compscicomputations.ui.auth.reset

import com.compscicomputations.ui.auth.FieldError

data class ResetUiState(
    val email: String = "",

    val errors: Set<FieldError> = setOf(),
)