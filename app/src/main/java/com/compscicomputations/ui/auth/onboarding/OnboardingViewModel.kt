package com.compscicomputations.ui.auth.onboarding

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compscicomputations.client.publik.data.source.OnboardingRepository
import com.compscicomputations.ui.utils.ProgressState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val onboardingRepository: OnboardingRepository
) : ViewModel() {
    companion object {
        private const val TAG = "OnboardingViewModel"
    }
    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()

    private var job: Job? = null
    fun cancel() {
        job?.cancel()
        job = null
        _uiState.value = _uiState.value.copy(progressState = ProgressState.Idle)
    }
    init {
        job = viewModelScope.launch(Dispatchers.IO) {
            onboardingRepository.onboardingItemsFlow
                .catch {
                    Log.e(TAG, "Error fetching onboarding items", it)
                    _uiState.value = _uiState.value.copy(progressState = ProgressState.Idle)
                }.collect { items ->
                    _uiState.value = _uiState.value.copy(items = items, progressState = ProgressState.Idle)
                }
        }
    }
}