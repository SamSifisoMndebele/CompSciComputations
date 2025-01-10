package com.compscicomputations.polish_expressions.ui.trace_table

import com.compscicomputations.polish_expressions.data.model.ConvertTo
import com.compscicomputations.polish_expressions.ui.RowData
import com.compscicomputations.polish_expressions.ui.Token

data class TraceTableUiState(
    val infix: List<Token> = listOf(),
    val postfixData: List<RowData> = listOf(),
    val prefixData: List<RowData> = listOf(),

    val error: String? = null,
    val convertTo: ConvertTo = ConvertTo.PostfixPrefix
)