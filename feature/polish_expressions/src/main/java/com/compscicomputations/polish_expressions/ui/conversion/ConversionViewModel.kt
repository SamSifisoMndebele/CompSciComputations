package com.compscicomputations.polish_expressions.ui.conversion

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compscicomputations.polish_expressions.ui.conversion.ExpressionConvert.infixToPostfix
import com.compscicomputations.polish_expressions.ui.conversion.ExpressionConvert.infixToPrefix
import com.compscicomputations.polish_expressions.ui.conversion.ExpressionConvert.postfixToInfix
import com.compscicomputations.polish_expressions.ui.conversion.ExpressionConvert.postfixToPrefix
import com.compscicomputations.polish_expressions.ui.conversion.ExpressionConvert.prefixToInfix
import com.compscicomputations.polish_expressions.ui.conversion.ExpressionConvert.prefixToPostfix
import com.compscicomputations.polish_expressions.data.model.ConvertFrom
import com.compscicomputations.polish_expressions.data.model.ConvertFrom.Infix
import com.compscicomputations.polish_expressions.data.model.ConvertFrom.Postfix
import com.compscicomputations.polish_expressions.data.model.ConvertFrom.Prefix
import com.compscicomputations.polish_expressions.data.source.local.datastore.PolishDataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class ConversionViewModel(
    private val context: Context,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ConversionUiState())
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            PolishDataStore.lastState(context).first()
                ?.let {
                    when (it.convertFrom) {
                        Infix -> _uiState.value = _uiState.value.copy(
                            infix = it.fromValue,
                            postfix = it.fromValue.infixToPostfix(),
                            prefix = it.fromValue.infixToPrefix(),
                        )
                        Postfix -> _uiState.value = _uiState.value.copy(
                            infix = it.fromValue.postfixToInfix(),
                            postfix = it.fromValue,
                            prefix = it.fromValue.postfixToPrefix(),
                        )
                        Prefix -> _uiState.value = _uiState.value.copy(
                            infix = it.fromValue.prefixToInfix(),
                            postfix = it.fromValue.prefixToPostfix(),
                            prefix = it.fromValue,
                        )
                    }
                }
        }
    }

    fun clear() {
        _uiState.value = ConversionUiState(convertFrom = _uiState.value.convertFrom)
        viewModelScope.launch(Dispatchers.IO) {
            PolishDataStore.setLastState(context, _uiState.value.convertFrom, "")
        }
    }

    fun setConvertFrom(convertFrom: ConvertFrom) {
        _uiState.value = _uiState.value.copy(convertFrom = convertFrom)
        viewModelScope.launch(Dispatchers.IO) {
            PolishDataStore.setLastState(context, convertFrom, _uiState.value.fromValue)
        }
    }

    fun onInfixChange(infix: String) {
        _uiState.value = _uiState.value.copy(infix = infix)
        viewModelScope.launch(Dispatchers.IO) {
            PolishDataStore.setLastState(context, Infix, infix)
        }
    }

    fun onPostfixChange(postfix: String) {
        _uiState.value = _uiState.value.copy(postfix = postfix)
        viewModelScope.launch(Dispatchers.IO) {
            PolishDataStore.setLastState(context, Postfix, postfix)
        }
    }

    fun onPrefixChange(prefix: String) {
        _uiState.value = _uiState.value.copy(prefix = prefix)
        viewModelScope.launch(Dispatchers.IO) {
            PolishDataStore.setLastState(context, Prefix, prefix)
        }
    }

    fun onConvert() {
        viewModelScope.launch(Dispatchers.IO) {
            when (_uiState.value.convertFrom) {
                Infix -> _uiState.value = _uiState.value.copy(
                    infix = _uiState.value.infix,
                    postfix = _uiState.value.infix.infixToPostfix(),
                    prefix = _uiState.value.infix.infixToPrefix(),
                )
                Postfix -> _uiState.value = _uiState.value.copy(
                    infix = _uiState.value.postfix.postfixToInfix(),
                    postfix = _uiState.value.postfix,
                    prefix = _uiState.value.postfix.postfixToPrefix(),
                )
                Prefix -> _uiState.value = _uiState.value.copy(
                    infix = _uiState.value.prefix.prefixToInfix(),
                    postfix = _uiState.value.prefix.prefixToPostfix(),
                    prefix = _uiState.value.prefix,
                )
            }
        }
    }

    companion object {
        val ConversionUiState.fromValue: String
            get() = when(convertFrom) {
                Infix -> infix.trim()
                Postfix -> postfix.trim()
                Prefix -> prefix.trim()
                else -> throw IllegalArgumentException("Invalid option.")
            }

    }
}