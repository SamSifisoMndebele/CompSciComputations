package com.compscicomputations.polish_expressions.presentation.conversion

import com.compscicomputations.polish_expressions.data.model.ConvertFrom
import com.compscicomputations.polish_expressions.domain.Token

data class ConversionUiState(
    val infix: String = "",
    val postfix: String = "",
    val prefix: String = "",

    val error: String? = null,
    val convertFrom: ConvertFrom = ConvertFrom.Infix,
//    val aiState: AIState = AIState.Idle
)