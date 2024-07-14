package com.compscicomputations.ui.auth.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compscicomputations.R
import com.compscicomputations.ui.utils.ProgressState
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.serialization.json.Json
import javax.inject.Inject

@HiltViewModel
class OnboardingViewModel @Inject constructor(
    remoteConfig: FirebaseRemoteConfig
) : ViewModel() {
    private val _uiState = MutableStateFlow(OnboardingUiState())
    val uiState: StateFlow<OnboardingUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults).await()
            val defaultOnboardingItems = remoteConfig.getString("OnboardingItems")
            val items = Json.decodeFromString<List<OnboardingItem>>(defaultOnboardingItems)
            _uiState.value = _uiState.value.copy(items = items, progressState = ProgressState.Idle)
        }
    }
}