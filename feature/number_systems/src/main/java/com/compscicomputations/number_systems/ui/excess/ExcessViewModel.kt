package com.compscicomputations.number_systems.ui.excess

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compscicomputations.number_systems.data.source.local.AiResponse
import com.compscicomputations.number_systems.data.model.ConvertFrom
import com.compscicomputations.number_systems.data.model.AIState
import com.compscicomputations.number_systems.data.model.ConvertFrom.*
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ExcessViewModel(
    private val generativeModel: GenerativeModel
) : ViewModel() {
    private val _uiState = MutableStateFlow(ExcessUiState())
    val uiState = _uiState.asStateFlow()

    fun clear() {
        _uiState.value = ExcessUiState()
    }

    fun setConvertFrom(convertFrom: ConvertFrom) {
        _uiState.value = _uiState.value.copy(convertFrom = convertFrom)
    }

    fun setAiState(aiState: AIState) {
        _uiState.value = _uiState.value.copy(aiState = aiState)
    }


    fun setExcessBits(excessBits: String) {
//        try {
//            this.excessBits.intValue = excessBits.toInt()
//            error.value = null
//        } catch (e: Exception) {
//            error.value = ExcessError.INVALID_EXCESS_BITS
//        }
    }

    fun onDecimalChange(decimalStr: String) {
        _uiState.value = _uiState.value//.fromDecimal(decimalStr)
            .copy(convertFrom = Decimal)
    }

    fun onExcessChange(excess: String) {
        _uiState.value = _uiState.value//.fromDecimal(decimalStr)
            .copy(convertFrom = Excess)
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


    fun generateSteps() {
        _uiState.value = _uiState.value.copy(aiState = AIState.Loading("Loading Steps..."))
        job = viewModelScope.launch(Dispatchers.IO) {
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
                        aiState = AIState.Success(
                            AiResponse(
                                id = 0,
                                convertFrom = _uiState.value.convertFrom,
                                value = when(_uiState.value.convertFrom) {
                                    Decimal -> _uiState.value.decimal
                                    Excess -> _uiState.value.excess
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

    fun regenerateSteps() {

    }

    private val ExcessUiState.aiText: String
        get() = when(convertFrom) {
            Decimal -> TODO()
            Excess -> TODO()
            else -> throw IllegalArgumentException("Invalid option.")
        }
}