package com.compscicomputations.polish_expressions.ui.trace_table

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compscicomputations.polish_expressions.data.model.ConvertFrom.Infix
import com.compscicomputations.polish_expressions.data.model.ConvertFrom.Postfix
import com.compscicomputations.polish_expressions.data.model.ConvertFrom.Prefix
import com.compscicomputations.polish_expressions.data.model.ConvertTo
import com.compscicomputations.polish_expressions.data.source.local.datastore.PolishDataStore
import com.compscicomputations.polish_expressions.ui.conversion.ExpressionConvert.infixToPostfix
import com.compscicomputations.polish_expressions.ui.conversion.ExpressionConvert.infixToPrefix
import com.compscicomputations.polish_expressions.ui.conversion.ExpressionConvert.postfixToInfix
import com.compscicomputations.polish_expressions.ui.conversion.ExpressionConvert.postfixToPrefix
import com.compscicomputations.polish_expressions.ui.conversion.ExpressionConvert.prefixToInfix
import com.compscicomputations.polish_expressions.ui.conversion.ExpressionConvert.prefixToPostfix
import com.compscicomputations.polish_expressions.ui.trace_table.TraceTableConverter.postfixPrefixTables
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class TraceTableViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(TraceTableUiState())
    val uiState = _uiState.asStateFlow()

    fun setConvertTo(convertTo: ConvertTo) {
        _uiState.value = _uiState.value.copy(convertTo = convertTo)
    }

    fun onChange(infix: String) {
        _uiState.value = _uiState.value.postfixPrefixTables(infix)
    }
}