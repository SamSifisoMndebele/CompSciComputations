package com.compscicomputations.number_systems.ui.complement

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compscicomputations.number_systems.data.model.AIState
import com.compscicomputations.number_systems.data.model.AiResponse
import com.compscicomputations.number_systems.data.model.ConvertFrom
import com.compscicomputations.number_systems.data.model.ConvertFrom.Complement1
import com.compscicomputations.number_systems.data.model.ConvertFrom.Complement2
import com.compscicomputations.number_systems.data.model.ConvertFrom.Decimal
import com.compscicomputations.number_systems.ui.complement.ComplementConverter.fromComplement1
import com.compscicomputations.number_systems.ui.complement.ComplementConverter.fromComplement2
import com.compscicomputations.number_systems.ui.complement.ComplementConverter.fromDecimal
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ComplementViewModel(
    private val generativeModel: GenerativeModel
) : ViewModel() {
    private val _uiState = MutableStateFlow(ComplementUiState())
    val uiState = _uiState.asStateFlow()

    fun clear() {
        _uiState.value = ComplementUiState()
    }

    fun setConvertFrom(convertFrom: ConvertFrom) {
        _uiState.value = _uiState.value.copy(convertFrom = convertFrom)
    }

    fun setProgressState(aiState: AIState) {
        _uiState.value = _uiState.value.copy(aiState = aiState)
    }

    fun onDecimalChange(decimalStr: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = _uiState.value.fromDecimal(decimalStr)
                .copy(convertFrom = Decimal)
        }
    }

    fun onComplement1Change(complement1Str: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = _uiState.value.fromComplement1(complement1Str)
                .copy(convertFrom = Complement1)
        }
    }

    fun onComplement2Change(complement2Str: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.value = _uiState.value.fromComplement2(complement2Str)
                .copy(convertFrom = Complement2)
        }
    }

    fun showAISteps() {
        _uiState.value = _uiState.value.copy(aiState = AIState.Loading("Loading Steps..."))
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = generativeModel.generateContent(
                    content {
                        text(_uiState.value.aiText)
                    }
                )
                response.text?.let { outputContent ->
                    Log.d("AI::response", outputContent)
                    _uiState.value = _uiState.value.copy(
                        aiState = AIState.Success(
                            AiResponse(
                                convertFrom = _uiState.value.convertFrom,
                                value = when(_uiState.value.convertFrom) {
                                    Decimal -> _uiState.value.decimal
                                    Complement1 -> _uiState.value.complement1
                                    Complement2 -> _uiState.value.complement2
                                    else -> throw IllegalArgumentException("Invalid option.")
                                },
                                text = outputContent
                            )
                        )
                    )
                } ?: throw Exception("Empty content generated.")
                Log.d("AI::totalToken", response.usageMetadata?.totalTokenCount.toString())
                Log.d("AI::promptToken", response.usageMetadata?.promptTokenCount.toString())
                Log.d("AI::candidatesToken", response.usageMetadata?.candidatesTokenCount.toString())
            } catch (e: Exception) {
                Log.w("AI::error", e)
                _uiState.value = _uiState.value.copy(
                    aiState = AIState.Error(e.localizedMessage)
                )
            }
        }
    }

    private val ComplementUiState.aiText: String
        get() = when(convertFrom) {
            Decimal -> "Show me the steps to convert the decimal: $decimal to first complement notation: $complement1, and second complement notation: $complement2"
            Complement1 -> "Show me the steps to convert the first complement notation: $complement1 to decimal: $decimal, and second complement notation: $complement2"
            Complement2 -> "Show me the steps to convert the second complement notation: $complement2 to decimal: $decimal, and first complement notation: $complement1"
            else -> throw IllegalArgumentException("Invalid option.")
        }

}