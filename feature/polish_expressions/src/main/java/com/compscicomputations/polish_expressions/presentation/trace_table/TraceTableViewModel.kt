package com.compscicomputations.polish_expressions.presentation.trace_table

import androidx.lifecycle.ViewModel
import com.compscicomputations.polish_expressions.data.model.ConvertTo
import com.compscicomputations.polish_expressions.domain.Token
import com.compscicomputations.polish_expressions.domain.infixToPostfix
import com.compscicomputations.polish_expressions.domain.infixToPrefix
import com.compscicomputations.polish_expressions.domain.tokenize
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class TraceTableViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(TraceTableUiState())
    val uiState = _uiState.asStateFlow()

    fun setConvertTo(convertTo: ConvertTo) {
        _uiState.value = _uiState.value.copy(convertTo = convertTo)
    }

    fun onChange(infixStr: String) {
        val infix = tokenize(infixStr)
        _uiState.value = _uiState.value.copy(
            infix = infixStr,
            postfixData = infix.infixToPostfix().table,
            prefixData = infix.infixToPrefix().table
        )
    }
}