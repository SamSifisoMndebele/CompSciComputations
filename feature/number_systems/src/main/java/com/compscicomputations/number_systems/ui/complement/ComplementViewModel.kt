package com.compscicomputations.number_systems.ui.complement

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compscicomputations.number_systems.data.model.AIState
import com.compscicomputations.number_systems.data.model.ConvertFrom
import com.compscicomputations.number_systems.data.model.ConvertFrom.Complement1
import com.compscicomputations.number_systems.data.model.ConvertFrom.Complement2
import com.compscicomputations.number_systems.data.model.ConvertFrom.Decimal
import com.compscicomputations.number_systems.data.model.CurrentTab.Complement
import com.compscicomputations.number_systems.data.source.local.AiResponse
import com.compscicomputations.number_systems.data.source.local.AiResponseDao
import com.compscicomputations.number_systems.data.source.local.datastore.ComplementDataStore
import com.compscicomputations.number_systems.ui.complement.ComplementConverter.fromComplement1
import com.compscicomputations.number_systems.ui.complement.ComplementConverter.fromComplement2
import com.compscicomputations.number_systems.ui.complement.ComplementConverter.fromDecimal
import com.compscicomputations.number_systems.ui.excess.ExcessConverter.fromDecimal
import com.compscicomputations.number_systems.utils.BinaryArithmetic.fixBits
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.ServerException
import com.google.ai.client.generativeai.type.content
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

class ComplementViewModel(
    private val generativeModel: GenerativeModel,
    private val aiResponseDao: AiResponseDao,
    private val dataStore: ComplementDataStore,
) : ViewModel() {
    init {
        viewModelScope.launch {
            dataStore.lastState.first()
                ?.let {
                    Log.d("BasesViewModel", "init: $it")
                    when(it.convertFrom) {
                        Decimal -> _uiState.value = _uiState.value.fromDecimal(it.fromValue)
                            .copy(convertFrom = Decimal)
                        Complement1 -> _uiState.value = _uiState.value.fromComplement1(it.fromValue)
                            .copy(convertFrom = Complement1)
                        Complement2 -> _uiState.value = _uiState.value.fromComplement2(it.fromValue)
                            .copy(convertFrom = Complement2)
                        else -> {}
                    }
                }
        }
    }

    private val _uiState = MutableStateFlow(ComplementUiState())
    val uiState = _uiState.asStateFlow()

    fun setAiState(aiState: AIState) {
        _uiState.value = _uiState.value.copy(aiState = aiState)
    }

    fun clear() {
        _uiState.value = ComplementUiState(convertFrom = _uiState.value.convertFrom)
        viewModelScope.launch(Dispatchers.IO) {
            dataStore.setLastState(_uiState.value.convertFrom, "")
        }
    }

    fun setConvertFrom(convertFrom: ConvertFrom) {
        _uiState.value = _uiState.value.copy(convertFrom = convertFrom)
        viewModelScope.launch(Dispatchers.IO) {
            dataStore.setLastState(convertFrom, _uiState.value.fromValue)
        }
    }
    fun onDecimalChange(decimalStr: String) {
        _uiState.value = _uiState.value.fromDecimal(decimalStr)
            .copy(convertFrom = Decimal)
        viewModelScope.launch(Dispatchers.IO) {
            dataStore.setLastState(Decimal, decimalStr)
        }
    }

    fun onComplement1Change(complement1Str: String) {
        _uiState.value = _uiState.value.fromComplement1(complement1Str)
            .copy(convertFrom = Complement1)
        viewModelScope.launch(Dispatchers.IO) {
            dataStore.setLastState(Complement1, complement1Str)
        }
    }

    fun onComplement2Change(complement2Str: String) {
        _uiState.value = _uiState.value.fromComplement2(complement2Str)
            .copy(convertFrom = Complement2)
        viewModelScope.launch(Dispatchers.IO) {
            dataStore.setLastState(Complement2, complement2Str)
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

    private suspend fun generateContent(): AiResponse {
        val response = generativeModel.generateContent(
            content {
                text(_uiState.value.aiText)
            }
        )
        return response.text?.let { outputContent ->
            Log.d("AI::response", outputContent)
            AiResponse(
                id = 0,
                tab = Complement,
                convertFrom = _uiState.value.convertFrom,
                value = _uiState.value.fromValue,
                text = outputContent
            )
        } ?: throw Exception("Empty content generated.")
    }

    fun generateSteps() {
        setAiState(AIState.Loading("Loading Steps..."))
        job = viewModelScope.launch(Dispatchers.IO) {
            try {
                val aiResponse = aiResponseDao.select(Complement, _uiState.value.convertFrom, _uiState.value.fromValue)
                if (aiResponse != null) {
                    setAiState(AIState.Success(aiResponse))
                    return@launch
                }

                val newAiResponse = generateContent()
                setAiState(AIState.Success(newAiResponse))
                aiResponseDao.insert(newAiResponse)

            } catch (e: Exception) {
                Log.w("AI::error", e)
                when {
                    e.cause is CancellationException -> setAiState(AIState.Idle)
                    e is ServerException -> setAiState(AIState.Error(Exception("An internal error has occurred. Please retry again.")))
                    else -> setAiState(AIState.Error(e))
                }
            }
        }
    }

    fun regenerateSteps() {
        val currentResponse: AiResponse = (_uiState.value.aiState as AIState.Success).response
        setAiState(AIState.Loading("Loading Steps..."))
        job = viewModelScope.launch(Dispatchers.IO) {
            try {
                val newAiResponse = generateContent()
                setAiState(AIState.Success(newAiResponse))
                aiResponseDao.insert(newAiResponse)
            } catch (e: Exception) {
                Log.w("AI::error", e)
                when {
                    e.cause is CancellationException -> setAiState(AIState.Idle)
                    e is ServerException -> setAiState(AIState.Error(Exception("An internal error has occurred. Please retry again.")))
                    else -> setAiState(AIState.Error(e))
                }
                delay(5000)
                setAiState(AIState.Success(currentResponse))
            }
        }
    }

    companion object {
        private val ComplementUiState.fromValue: String
            get() = when(convertFrom) {
                Decimal -> decimal.trim()
                Complement1 -> complement1.trim()
                Complement2 -> complement2.trim()
                else -> throw IllegalArgumentException("Invalid option.")
            }

        private val ComplementUiState.aiText: String
            get() = when(convertFrom) {
                Decimal -> "Show me the steps to convert the decimal: $decimal to first and second complement notation."
                Complement1 -> "Show me the steps to convert the first complement notation: " +
                        "${complement1.fixBits(complement2.length, decimal.trim().toDouble() > 0)} to decimal and second complement notation."
                Complement2 -> "Show me the steps to convert the second complement notation: " +
                        "${complement2.fixBits(complement1.length, decimal.trim().toDouble() > 0)} to decimal and first complement notation."

                else -> throw IllegalArgumentException("Invalid option.")
            }

    }
}