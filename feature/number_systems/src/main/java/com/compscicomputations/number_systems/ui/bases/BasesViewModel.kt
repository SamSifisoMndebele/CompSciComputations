package com.compscicomputations.number_systems.ui.bases

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compscicomputations.number_systems.data.model.AiResponse
import com.compscicomputations.number_systems.ui.bases.BaseConverter.fromUnicode
import com.compscicomputations.number_systems.ui.bases.BaseConverter.fromBinary
import com.compscicomputations.number_systems.ui.bases.BaseConverter.fromDecimal
import com.compscicomputations.number_systems.ui.bases.BaseConverter.fromHex
import com.compscicomputations.number_systems.ui.bases.BaseConverter.fromOctal
import com.compscicomputations.number_systems.data.model.ConvertFrom
import com.compscicomputations.number_systems.data.model.ConvertFrom.*
import com.compscicomputations.number_systems.data.model.AIState
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BasesViewModel(
    private val generativeModel: GenerativeModel
) : ViewModel() {
    private val _uiState = MutableStateFlow(BasesUiState())
    val uiState = _uiState.asStateFlow()

    fun clear() {
        _uiState.value = BasesUiState()
    }

    fun setConvertFrom(convertFrom: ConvertFrom) {
        _uiState.value = _uiState.value.copy(convertFrom = convertFrom)
    }

    fun setProgressState(aiState: AIState) {
        _uiState.value = _uiState.value.copy(aiState = aiState)
    }

    fun onDecimalChange(decimalStr: String) {
        _uiState.value = _uiState.value.fromDecimal(decimalStr)
            .copy(convertFrom = Decimal)
    }

    fun onBinaryChange(binaryStr: String) {
        _uiState.value = _uiState.value.fromBinary(binaryStr)
            .copy(convertFrom = Binary)
    }

    fun onOctalChange(octalStr: String) {
        _uiState.value = _uiState.value.fromOctal(octalStr)
            .copy(convertFrom = Octal)
    }

    fun onHexadecimalChange(hexadecimalStr: String) {
        _uiState.value = _uiState.value.fromHex(hexadecimalStr)
            .copy(convertFrom = Hexadecimal)
    }

    fun onUnicodeChange(unicodeStr: String) {
        _uiState.value = _uiState.value.fromUnicode(unicodeStr)
            .copy(convertFrom = Unicode)
    }

    fun showAISteps() {
        _uiState.value = _uiState.value.copy(aiState = AIState.Loading("Loading Steps..."))
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = generativeModel.generateContent(
                    content {
//                        image(bitmap)
                        text(_uiState.value.aiText + "\n\n" +
                                "Use UTF-16 for unicode characters encoding.\n" +
                                "Do Not group binary digits because you are not grouping correct.")
                    }
                )
                response.text?.let { outputContent ->
                    Log.d("AI::response", outputContent)
                    _uiState.value = _uiState.value.copy(
                        aiState = AIState.Success(AiResponse(
                            convertFrom = _uiState.value.convertFrom,
                            value = when(_uiState.value.convertFrom) {
                                Decimal -> _uiState.value.decimal
                                Binary -> _uiState.value.binary
                                Octal -> _uiState.value.octal
                                Hexadecimal -> _uiState.value.hexadecimal
                                Unicode -> _uiState.value.unicode
                                else -> throw IllegalArgumentException("Invalid option.")
                            },
                            text = outputContent
                        ))
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

    private val BasesUiState.aiText: String
        get() = when(convertFrom) {
            Decimal -> "Show me steps how to convert the decimal: $decimal to binary: $binary, octal: $octal, hexadecimal: $hexadecimal, and unicode character: $unicode"
            Binary -> "Show me steps how to convert the binary: $binary to decimal: $decimal, octal: $octal, hexadecimal: $hexadecimal, and unicode character: $unicode"
            Octal -> "Show me steps how to convert the octal: $octal to decimal: $decimal, binary: $binary, hexadecimal: $hexadecimal, and unicode character: $unicode"
            Hexadecimal -> "Show me steps how to convert the hexadecimal: $hexadecimal to decimal: $decimal, binary: $binary, octal: $octal, and unicode character: $unicode"
            Unicode -> "Show me steps how to convert the unicode character: $unicode to decimal: $decimal, binary: $binary, octal: $octal, and hexadecimal: $hexadecimal"
            else -> throw IllegalArgumentException("Invalid option.")
        }
}

