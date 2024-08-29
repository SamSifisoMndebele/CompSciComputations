package com.compscicomputations.number_systems.ui.floating_point

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compscicomputations.data.source.local.AiResponse
import com.compscicomputations.data.source.local.AiResponseDao
import com.compscicomputations.number_systems.data.model.ConvertFrom
import com.compscicomputations.number_systems.data.model.ConvertFrom.Binary16
import com.compscicomputations.number_systems.data.model.ConvertFrom.Binary32
import com.compscicomputations.number_systems.data.model.ConvertFrom.Binary64
import com.compscicomputations.number_systems.data.model.ConvertFrom.Decimal
import com.compscicomputations.number_systems.data.model.ConvertFrom.MiniFloat
import com.compscicomputations.number_systems.data.model.CurrentTab.FloatingPoint
import com.compscicomputations.number_systems.data.source.local.datastore.FloatingPointDataStore
import com.compscicomputations.number_systems.ui.floating_point.FloatingPointConverter.fromBinary16
import com.compscicomputations.number_systems.ui.floating_point.FloatingPointConverter.fromBinary32
import com.compscicomputations.number_systems.ui.floating_point.FloatingPointConverter.fromBinary64
import com.compscicomputations.number_systems.ui.floating_point.FloatingPointConverter.fromDecimal
import com.compscicomputations.number_systems.ui.floating_point.FloatingPointConverter.fromMiniFloat
import com.compscicomputations.number_systems.utils.BinaryArithmetic.fixBits
import com.compscicomputations.number_systems.utils.MODULE_NAME
import com.compscicomputations.ui.main.dynamic_feature.AIState
import com.compscicomputations.ui.main.dynamic_feature.GenerateContentUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlin.coroutines.cancellation.CancellationException

class FloatingPointViewModel(
    private val context: Context,
    private val generateContent: GenerateContentUseCase,
    private val aiResponseDao: AiResponseDao,
) : ViewModel() {
    init {
        viewModelScope.launch {
            FloatingPointDataStore.lastState(context).first()
                ?.let {
                    Log.d("BasesViewModel", "init: $it")
                    when(it.convertFrom) {
                        Decimal -> _uiState.value = _uiState.value.fromDecimal(it.fromValue)
                            .copy(convertFrom = Decimal)
                        MiniFloat -> _uiState.value = _uiState.value.fromMiniFloat(it.fromValue)
                            .copy(convertFrom = MiniFloat)
                        Binary16 -> _uiState.value = _uiState.value.fromBinary16(it.fromValue)
                            .copy(convertFrom = Binary16)
                        Binary32 -> _uiState.value = _uiState.value.fromBinary32(it.fromValue)
                            .copy(convertFrom = Binary32)
                        Binary64 -> _uiState.value = _uiState.value.fromBinary64(it.fromValue)
                            .copy(convertFrom = Binary64)
                        else -> {}
                    }
                }
        }
    }
    private val _uiState = MutableStateFlow(FloatingPointUiState())
    val uiState = _uiState.asStateFlow()

    fun setAiState(aiState: AIState) {
        _uiState.value = _uiState.value.copy(aiState = aiState)
    }

    fun clear() {
        _uiState.value = FloatingPointUiState(convertFrom = _uiState.value.convertFrom)
        viewModelScope.launch(Dispatchers.IO) {
            FloatingPointDataStore.setLastState(context, _uiState.value.convertFrom, "")
        }
    }

    fun setConvertFrom(convertFrom: ConvertFrom) {
        _uiState.value = _uiState.value.copy(convertFrom = convertFrom)
        viewModelScope.launch(Dispatchers.IO) {
            FloatingPointDataStore.setLastState(context, convertFrom, _uiState.value.fromValue)
        }
    }

    fun onDecimalChange(decimalStr: String) {
        _uiState.value = _uiState.value.fromDecimal(decimalStr)
            .copy(convertFrom = Decimal)
        viewModelScope.launch(Dispatchers.IO) {
            FloatingPointDataStore.setLastState(context, Decimal, decimalStr)
        }
    }

    fun onMiniFloatChange(miniFloat: String) {
        _uiState.value = _uiState.value.fromMiniFloat(miniFloat)
            .copy(convertFrom = MiniFloat)
        viewModelScope.launch(Dispatchers.IO) {
            FloatingPointDataStore.setLastState(context, Decimal, miniFloat)
        }
    }

    fun onBinary16Change(binary16: String) {
        _uiState.value = _uiState.value.fromBinary16(binary16)
            .copy(convertFrom = Binary16)
        viewModelScope.launch(Dispatchers.IO) {
            FloatingPointDataStore.setLastState(context, Binary16, binary16)
        }
    }

    fun onBinary32Change(binary32: String) {
        _uiState.value = _uiState.value.fromBinary32(binary32)
            .copy(convertFrom = Binary32)
        viewModelScope.launch(Dispatchers.IO) {
            FloatingPointDataStore.setLastState(context, Binary32, binary32)
        }
    }

    fun onBinary64Change(binary64: String) {
        _uiState.value = _uiState.value.fromBinary64(binary64)
            .copy(convertFrom = Binary64)
        viewModelScope.launch(Dispatchers.IO) {
            FloatingPointDataStore.setLastState(context, Binary64, binary64)
        }
    }

    val responsesFlow: Flow<List<AiResponse>?>
        get() = aiResponseDao.selectAll(MODULE_NAME, FloatingPoint.name).map {
            if (it.isNullOrEmpty()) null
            else it
        }

    fun deleteResponse(response: AiResponse) {
        viewModelScope.launch {
            aiResponseDao.delete(response)
        }
    }

    fun setFields(response: AiResponse) {
        when(ConvertFrom.valueOf(response.convertFrom)) {
            Decimal -> _uiState.value = _uiState.value.fromDecimal(response.value)
                .copy(convertFrom = Decimal)
            MiniFloat -> _uiState.value = _uiState.value.fromMiniFloat(response.value)
                .copy(convertFrom = MiniFloat)
            Binary16 -> _uiState.value = _uiState.value.fromBinary16(response.value)
                .copy(convertFrom = Binary16)
            Binary32 -> _uiState.value = _uiState.value.fromBinary32(response.value)
                .copy(convertFrom = Binary32)
            Binary64 -> _uiState.value = _uiState.value.fromBinary64(response.value)
                .copy(convertFrom = Binary64)
            else -> {}
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

    private suspend fun generateContent(): AiResponse = generateContent(
        MODULE_NAME,
        FloatingPoint.name,
        _uiState.value.convertFrom.name,
        _uiState.value.fromValue,
        _uiState.value.aiText
    )

    fun generateSteps() {
        setAiState(AIState.Loading("Loading Steps..."))
        job = viewModelScope.launch(Dispatchers.IO) {
            try {
                val aiResponse = aiResponseDao.select(MODULE_NAME, FloatingPoint.name, _uiState.value.convertFrom.name, _uiState.value.fromValue)
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
                    e is GenerateContentUseCase.AiServerException -> setAiState(AIState.Error(Exception("An internal error has occurred. Please retry again.")))
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
                    e is GenerateContentUseCase.AiServerException -> setAiState(AIState.Error(Exception("An internal error has occurred. Please retry again.")))
                    else -> setAiState(AIState.Error(e))
                }
                delay(5000)
                setAiState(AIState.Success(currentResponse))
            }
        }
    }

    companion object {
        val FloatingPointUiState.fromValue: String
            get() = when(convertFrom) {
                Decimal -> decimal
                MiniFloat -> miniFloat
                Binary16 -> binary16
                Binary32 -> binary32
                Binary64 -> binary64
                else -> throw IllegalArgumentException("Invalid option.")
            }
        private val FloatingPointUiState.aiText: String
            get() = when(convertFrom) {
                Decimal -> "Show me steps how to convert the decimal: " +
                        "$decimal to half, single and double precision IEEE754."
                MiniFloat -> TODO()
                Binary16 -> "Show me steps how to convert the half precision IEEE754: " +
                        "${binary16.fixBits(16)} to decimal, and single and double precision IEEE754."
                Binary32 -> "Show me steps how to convert the single precision IEEE754: " +
                        "${binary32.fixBits(32)} to decimal, and half and double precision IEEE754."
                Binary64 -> "Show me steps how to convert the double precision IEEE754: " +
                        "${binary64.fixBits(64)} to decimal, and half and single precision IEEE754."

                else -> throw IllegalArgumentException("Invalid option.")
            }
    }

}