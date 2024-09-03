package com.compscicomputations.polish_expressions.ui.conversion

import com.compscicomputations.polish_expressions.data.model.ConvertFrom

data class ConversionUiState(
    val infix: String = "",
    val postfix: String = "",
    val prefix: String = "",

    val error: String? = null,
    val convertFrom: ConvertFrom = ConvertFrom.Infix,
//    val aiState: AIState = AIState.Idle
)