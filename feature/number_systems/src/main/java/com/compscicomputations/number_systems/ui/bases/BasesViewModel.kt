package com.compscicomputations.number_systems.ui.bases

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compscicomputations.number_systems.data.source.local.AiResponse
import com.compscicomputations.number_systems.ui.bases.BaseConverter.fromUnicode
import com.compscicomputations.number_systems.ui.bases.BaseConverter.fromBinary
import com.compscicomputations.number_systems.ui.bases.BaseConverter.fromDecimal
import com.compscicomputations.number_systems.ui.bases.BaseConverter.fromHex
import com.compscicomputations.number_systems.ui.bases.BaseConverter.fromOctal
import com.compscicomputations.number_systems.data.model.ConvertFrom
import com.compscicomputations.number_systems.data.model.ConvertFrom.*
import com.compscicomputations.number_systems.data.model.AIState
import com.compscicomputations.number_systems.data.source.local.AiResponseDao
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BasesViewModel(
    private val generativeModel: GenerativeModel,
    private val aiResponseDao: AiResponseDao
) : ViewModel() {
    private val _uiState = MutableStateFlow(BasesUiState())
    val uiState = _uiState.asStateFlow()

    fun clear() {
        _uiState.value = BasesUiState()
    }

    fun setConvertFrom(convertFrom: ConvertFrom) {
        _uiState.value = _uiState.value.copy(convertFrom = convertFrom)
    }

    fun setAiState(aiState: AIState) {
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

    private var job: Job? = null
    @OptIn(InternalCoroutinesApi::class)
    fun cancelJob(handler: () -> Unit = {}) {
        job?.cancel()
        job?.invokeOnCompletion(true) {
            job = null
            setAiState(AIState.Idle)
            handler()
        }
    }

    private suspend fun generateContent(): AiResponse {
        val response = generativeModel.generateContent(
            content {
                text(_uiState.value.aiText + "\n\n" +
                        "Use UTF-16 for unicode characters encoding.\n" +
                        "Do Not group binary digits because you are not grouping correct.")
            }
        )
        return response.text?.let { outputContent ->
            Log.d("AI::response", outputContent)
            AiResponse(
                id = 0,
                convertFrom = _uiState.value.convertFrom,
                value = _uiState.value.fromValue,
                text = outputContent
            )
        } ?: throw Exception("Empty content generated.")
    }

    fun generateSteps() {
        job = viewModelScope.launch(Dispatchers.IO) {
            setAiState(AIState.Loading("Loading Steps..."))
            try {
                val aiResponse = aiResponseDao.select(_uiState.value.convertFrom, _uiState.value.fromValue)
                if (aiResponse != null) {
                    setAiState(AIState.Success(aiResponse))
                    return@launch
                }
                val newAiResponse = generateContent()
                setAiState(AIState.Success(newAiResponse))
                aiResponseDao.insert(newAiResponse)
            } catch (e: Exception) {
                Log.w("AI::error", e)
                setAiState(AIState.Error(e.localizedMessage))
            }
        }
    }

    fun regenerateSteps() {
        job = viewModelScope.launch {
            setAiState(AIState.Loading("Loading Steps..."))
            try {
                val newAiResponse = generateContent()
                setAiState(AIState.Success(newAiResponse))
                aiResponseDao.insert(newAiResponse)
            } catch (e: Exception) {
                Log.w("AI::error", e)
                setAiState(AIState.Error(e.localizedMessage))
            }
        }
    }

    companion object {
        private val BasesUiState.fromValue: String
            get() = when(convertFrom) {
                Decimal -> decimal.trim()
                Binary -> binary.trim()
                Octal -> octal.trim()
                Hexadecimal -> hexadecimal.trim()
                Unicode -> unicode.trim()
                else -> throw IllegalArgumentException("Invalid option.")
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
}