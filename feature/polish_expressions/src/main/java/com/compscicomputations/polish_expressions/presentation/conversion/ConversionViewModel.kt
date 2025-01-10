package com.compscicomputations.polish_expressions.presentation.conversion

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compscicomputations.polish_expressions.data.model.ConvertFrom
import com.compscicomputations.polish_expressions.data.model.ConvertFrom.Infix
import com.compscicomputations.polish_expressions.data.model.ConvertFrom.Postfix
import com.compscicomputations.polish_expressions.data.model.ConvertFrom.Prefix
import com.compscicomputations.polish_expressions.data.source.local.datastore.PolishDataStore
import com.compscicomputations.polish_expressions.domain.asString
import com.compscicomputations.polish_expressions.domain.infixToPostfix
import com.compscicomputations.polish_expressions.domain.infixToPrefix
import com.compscicomputations.polish_expressions.domain.postfixToInfix
import com.compscicomputations.polish_expressions.domain.postfixToPrefix
import com.compscicomputations.polish_expressions.domain.prefixToInfix
import com.compscicomputations.polish_expressions.domain.prefixToPostfix
import com.compscicomputations.polish_expressions.domain.tokenizeInfix
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
                    try {
                        val tokens = tokenizeInfix(it.fromValue)
                        when (it.convertFrom) {
                            Infix -> _uiState.value = _uiState.value.copy(
                                infix = it.fromValue,
                                postfix = tokens.infixToPostfix().postfix.asString(),
                                prefix = tokens.infixToPrefix().prefix.asString(),
                                convertFrom = Infix
                            )
                            Postfix -> _uiState.value = _uiState.value.copy(
                                infix = tokens.postfixToInfix().asString(),
                                postfix = it.fromValue,
                                prefix = tokens.postfixToPrefix().prefix.asString(),
                                convertFrom = Postfix
                            )
                            Prefix -> _uiState.value = _uiState.value.copy(
                                infix = tokens.prefixToInfix().asString(),
                                postfix = tokens.prefixToPostfix().postfix.asString(),
                                prefix = it.fromValue,
                                convertFrom = Postfix
                            )
                        }
                    } catch (e: Exception) {
                        when (it.convertFrom) {
                            Infix -> _uiState.value = _uiState.value.copy(
                                infix = it.fromValue,
                                error = e.message,
                                convertFrom = Infix
                            )
                            Postfix -> _uiState.value = _uiState.value.copy(
                                postfix = it.fromValue,
                                error = e.message,
                                convertFrom = Postfix
                            )
                            Prefix -> _uiState.value = _uiState.value.copy(
                                prefix = it.fromValue,
                                error = e.message,
                                convertFrom = Prefix
                            )
                        }
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
            try {
                val tokens = tokenizeInfix(infix)
                _uiState.value = _uiState.value.copy(
                    postfix = tokens.infixToPostfix().postfix.asString(),
                    prefix = tokens.infixToPrefix().prefix.asString(),
                    error = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(postfix = "", prefix = "", error = e.message)
            }
            PolishDataStore.setLastState(context, Infix, infix)
        }
    }

    fun onPostfixChange(postfix: String) {
        _uiState.value = _uiState.value.copy(postfix = postfix)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val tokens = tokenizeInfix(postfix)
                _uiState.value = _uiState.value.copy(
                    infix = tokens.postfixToInfix().asString(filterAsterisk = true, spaces = false),
                    prefix = tokens.postfixToPrefix().prefix.asString(),
                    error = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(infix = "", prefix = "", error = e.message)
            }
            PolishDataStore.setLastState(context, Postfix, postfix)
        }

    }

    fun onPrefixChange(prefix: String) {
        _uiState.value = _uiState.value.copy(prefix = prefix)
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val tokens = tokenizeInfix(prefix)
                _uiState.value = _uiState.value.copy(
                    infix = tokens.prefixToInfix().asString(filterAsterisk = true, spaces = false),
                    postfix = tokens.prefixToPostfix().postfix.asString(),
                    error = null
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(infix = "", postfix = "", error = e.message)
            }
            PolishDataStore.setLastState(context, Prefix, prefix)
        }
    }

    companion object {
        val ConversionUiState.fromValue: String
            get() = when(convertFrom) {
                Infix -> infix
                Postfix -> postfix
                Prefix -> prefix
            }

    }
}