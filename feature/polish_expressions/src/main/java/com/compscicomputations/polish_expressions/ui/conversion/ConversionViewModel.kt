package com.compscicomputations.polish_expressions.ui.conversion

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compscicomputations.polish_expressions.data.model.ConvertFrom
import com.compscicomputations.polish_expressions.data.model.ConvertFrom.Infix
import com.compscicomputations.polish_expressions.data.model.ConvertFrom.Postfix
import com.compscicomputations.polish_expressions.data.model.ConvertFrom.Prefix
import com.compscicomputations.polish_expressions.data.source.local.datastore.PolishDataStore
import com.compscicomputations.polish_expressions.ui.ExpressionException
import com.compscicomputations.polish_expressions.ui.Token
import com.compscicomputations.polish_expressions.ui.infixToPostfix
import com.compscicomputations.polish_expressions.ui.infixToPrefix
import com.compscicomputations.polish_expressions.ui.postfixToInfix
import com.compscicomputations.polish_expressions.ui.postfixToPrefix
import com.compscicomputations.polish_expressions.ui.prefixToInfix
import com.compscicomputations.polish_expressions.ui.prefixToPostfix
import com.compscicomputations.polish_expressions.ui.tokenize
import kotlinx.coroutines.Dispatchers
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
                            postfix = it.fromValue.infixToPostfix().postfix,
                            prefix = it.fromValue.infixToPrefix().prefix,
                        )
                        Postfix -> _uiState.value = _uiState.value.copy(
                            infix = it.fromValue.postfixToInfix(),
                            postfix = it.fromValue,
                            prefix = it.fromValue.postfixToPrefix().prefix,
                        )
                        Prefix -> _uiState.value = _uiState.value.copy(
                            infix = it.fromValue.prefixToInfix(),
                            postfix = it.fromValue.prefixToPostfix().postfix,
                            prefix = it.fromValue,
                        )
                    }
                }
        }
    }

    fun clear() {
        _uiState.value = ConversionUiState(convertFrom = _uiState.value.convertFrom)
        viewModelScope.launch(Dispatchers.IO) {
            PolishDataStore.setLastState(context, _uiState.value.convertFrom, listOf())
        }
    }

    fun setConvertFrom(convertFrom: ConvertFrom) {
        _uiState.value = _uiState.value.copy(convertFrom = convertFrom)
        viewModelScope.launch(Dispatchers.IO) {
            PolishDataStore.setLastState(context, convertFrom, _uiState.value.fromValue)
        }
    }

    fun onInfixChange(infix: String) {
        try {
            val tokens = tokenize(infix)
            _uiState.value = _uiState.value.copy(infix = tokens)
            viewModelScope.launch(Dispatchers.IO) {
                PolishDataStore.setLastState(context, Infix, tokens)
            }
        } catch (e: Exception) {
            _uiState.value = _uiState.value.copy(postfix = listOf(), prefix = listOf(), error = e.message)

        }

    }

    fun onPostfixChange(postfix: String) {
        try {
            val tokens = tokenize(postfix)
            _uiState.value = _uiState.value.copy(postfix = tokens)
            viewModelScope.launch(Dispatchers.IO) {
                PolishDataStore.setLastState(context, Postfix, tokens)
            }
        } catch (e: Exception) {
            _uiState.value = _uiState.value.copy(infix = listOf(), prefix = listOf(), error = e.message)
        }

    }

    fun onPrefixChange(prefix: String) {
        try {
            val tokens = tokenize(prefix)
            _uiState.value = _uiState.value.copy(prefix = tokens)
            viewModelScope.launch(Dispatchers.IO) {
                PolishDataStore.setLastState(context, Prefix, tokens)
            }
        } catch (e: Exception) {
            _uiState.value = _uiState.value.copy(infix = listOf(), postfix = listOf(), error = e.message)
        }

    }

    fun onConvert() {
        viewModelScope.launch(Dispatchers.IO) {
            when (_uiState.value.convertFrom) {
                Infix -> _uiState.value = _uiState.value.copy(
                    infix = _uiState.value.infix,
                    postfix = _uiState.value.infix.infixToPostfix().postfix,
                    prefix = _uiState.value.infix.infixToPrefix().prefix,
                )
                Postfix -> _uiState.value = _uiState.value.copy(
                    infix = _uiState.value.postfix.postfixToInfix(),
                    postfix = _uiState.value.postfix,
                    prefix = _uiState.value.postfix.postfixToPrefix().prefix,
                )
                Prefix -> _uiState.value = _uiState.value.copy(
                    infix = _uiState.value.prefix.prefixToInfix(),
                    postfix = _uiState.value.prefix.prefixToPostfix().postfix,
                    prefix = _uiState.value.prefix,
                )
            }
        }
    }

    companion object {
        val ConversionUiState.fromValue: List<Token>
            get() = when(convertFrom) {
                Infix -> infix
                Postfix -> postfix
                Prefix -> prefix
            }

    }
}