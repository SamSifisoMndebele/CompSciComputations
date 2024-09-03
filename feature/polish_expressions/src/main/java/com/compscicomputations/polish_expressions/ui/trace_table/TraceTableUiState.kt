package com.compscicomputations.polish_expressions.ui.trace_table

import com.compscicomputations.polish_expressions.data.model.ConvertTo

data class TraceTableUiState(
    val infix: String = "",
    val postfixData: List<RowData> = listOf(),
    val prefixData: List<RowData> = listOf(),

    val error: String? = null,
    val convertTo: ConvertTo = ConvertTo.PostfixPrefix
)