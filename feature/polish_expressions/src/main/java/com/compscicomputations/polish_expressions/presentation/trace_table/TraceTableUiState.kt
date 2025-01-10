package com.compscicomputations.polish_expressions.presentation.trace_table

import com.compscicomputations.polish_expressions.data.model.ConvertTo
import com.compscicomputations.polish_expressions.domain.RowData
import com.compscicomputations.polish_expressions.domain.Token

data class TraceTableUiState(
    val infix: String = "",
    val postfixData: List<RowData> = listOf(),
    val prefixData: List<RowData> = listOf(),

    val error: String? = null,
    val convertTo: ConvertTo = ConvertTo.PostfixPrefix
)