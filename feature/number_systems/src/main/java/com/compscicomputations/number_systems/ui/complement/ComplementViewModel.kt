package com.compscicomputations.number_systems.ui.complement

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compscicomputations.number_systems.data.model.AIState
import com.compscicomputations.number_systems.data.source.local.AiResponse
import com.compscicomputations.number_systems.data.model.ConvertFrom
import com.compscicomputations.number_systems.data.model.ConvertFrom.Complement1
import com.compscicomputations.number_systems.data.model.ConvertFrom.Complement2
import com.compscicomputations.number_systems.data.model.ConvertFrom.Decimal
import com.compscicomputations.number_systems.data.source.local.AiResponseDao
import com.compscicomputations.number_systems.ui.complement.ComplementConverter.fromComplement1
import com.compscicomputations.number_systems.ui.complement.ComplementConverter.fromComplement2
import com.compscicomputations.number_systems.ui.complement.ComplementConverter.fromDecimal
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ComplementViewModel(
    private val generativeModel: GenerativeModel,
    private val aiResponseDao: AiResponseDao
) : ViewModel() {
    private val _uiState = MutableStateFlow(ComplementUiState())
    val uiState = _uiState.asStateFlow()

    fun clear() {
        _uiState.value = ComplementUiState()
    }

    fun setConvertFrom(convertFrom: ConvertFrom) {
        _uiState.value = _uiState.value.copy(convertFrom = convertFrom)
    }

    fun setAiState(aiState: AIState) {
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
        setAiState(AIState.Loading("Loading Steps..."))
        job = viewModelScope.launch(Dispatchers.IO) {
            try {
                val fromValue = when(_uiState.value.convertFrom) {
                    Decimal -> _uiState.value.decimal
                    Complement1 -> _uiState.value.complement1
                    Complement2 -> _uiState.value.complement2
                    else -> throw IllegalArgumentException("Invalid option.")
                }
                val aiResponse = aiResponseDao.select(_uiState.value.convertFrom, fromValue)
                if (aiResponse != null) {
                    setAiState(AIState.Success(aiResponse))
                    return@launch
                }

                val response = generativeModel.generateContent(
                    content {
                        text(_uiState.value.aiText)
                    }
                )
                response.text?.let { outputContent ->
                    Log.d("AI::response", outputContent)
                    val newAiResponse = AiResponse(
                        id = 0,
                        convertFrom = _uiState.value.convertFrom,
                        value = fromValue,
                        text = outputContent
                    )
                    setAiState(AIState.Success(newAiResponse))
                    aiResponseDao.insert(newAiResponse)
                } ?: throw Exception("Empty content generated.")
            } catch (e: Exception) {
                Log.w("AI::error", e)
                setAiState(AIState.Error(e.localizedMessage))
            }
        }
    }

    fun regenerateSteps() {

    }

    private val ComplementUiState.aiText: String
        get() = when(convertFrom) {
            Decimal -> "Show me the steps to convert the decimal: $decimal to first complement notation: $complement1, and second complement notation: $complement2"
            Complement1 -> "Show me the steps to convert the first complement notation: $complement1 to decimal: $decimal, and second complement notation: $complement2"
            Complement2 -> "Show me the steps to convert the second complement notation: $complement2 to decimal: $decimal, and first complement notation: $complement1"
            else -> throw IllegalArgumentException("Invalid option.")
        }

}