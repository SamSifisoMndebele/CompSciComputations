package com.compscicomputations.polish_expressions.ui.conversion

import com.compscicomputations.polish_expressions.data.model.ConvertFrom
import com.compscicomputations.polish_expressions.ui.Token

data class ConversionUiState(
    val infix: List<Token> = listOf(),
    val postfix: List<Token> = listOf(),
    val prefix: List<Token> = listOf(),

    val error: String? = null,
    val convertFrom: ConvertFrom = ConvertFrom.Infix,
//    val aiState: AIState = AIState.Idle
)