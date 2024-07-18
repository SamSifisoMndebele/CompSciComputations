package com.compscicomputations.ui.auth.onboarding

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compscicomputations.client.publik.OnboardingRepository
import com.compscicomputations.ui.utils.ProgressState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    private val onboardingRepository: OnboardingRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()


    companion object {
        private const val TAG = "OnboardingViewModel"
    }
    init {
        viewModelScope.launch(Dispatchers.IO) {
            val items = try {
                onboardingRepository.getOnboardingItems()
            } catch (e: Exception) {
                Log.e(TAG, "getOnboardingItems:", e)
                _uiState.value = _uiState.value.copy(items = null, progressState = ProgressState.Error(e.localizedMessage))
                return@launch
            }
            _uiState.value = _uiState.value.copy(items = items, progressState = ProgressState.Idle)
        }
    }
}