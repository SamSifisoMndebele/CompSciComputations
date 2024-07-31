package com.compscicomputations.number_systems.ui.floating_point

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compscicomputations.number_systems.data.source.local.AiResponse
import com.compscicomputations.number_systems.data.model.ConvertFrom
import com.compscicomputations.number_systems.data.model.AIState
import com.compscicomputations.number_systems.data.model.ConvertFrom.*
import com.compscicomputations.number_systems.ui.floating_point.FloatingPointConverter.fromDecimal
import com.compscicomputations.number_systems.ui.floating_point.FloatingPointConverter.fromDoubleIEEE754
import com.compscicomputations.number_systems.ui.floating_point.FloatingPointConverter.fromIEEE754
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FloatingPointViewModel(
    private val generativeModel: GenerativeModel
) : ViewModel() {
    private val _uiState = MutableStateFlow(FloatingPointUiState())
    val uiState = _uiState.asStateFlow()

    fun clear() {
        _uiState.value = FloatingPointUiState()
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

    fun onFloat32Change(float32: String) {
        _uiState.value = _uiState.value.fromIEEE754(float32)
            .copy(convertFrom = Binary32)
    }

    fun onFloat64Change(float64: String) {
        _uiState.value = _uiState.value.fromDoubleIEEE754(float64)
            .copy(convertFrom = Binary64)
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
//                                MiniFloat -> _uiState.value.miniFloat
                                Binary16 -> _uiState.value.binary16
                                Binary32 -> _uiState.value.binary32
                                Binary64 -> _uiState.value.binary64
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

    private val FloatingPointUiState.aiText: String
        get() = when(convertFrom) {
            Decimal -> "Show me steps how to convert the decimal number: $decimal to single precision IEEE754: $binary32 and double precision IEEE754: $binary64"
//            MiniFloat -> TODO()
            Binary16 -> TODO()
            Binary32 -> TODO()
            Binary64 -> TODO()
            else -> throw IllegalArgumentException("Invalid option.")
        }

}