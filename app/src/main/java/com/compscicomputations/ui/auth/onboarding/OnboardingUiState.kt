package com.compscicomputations.ui.auth.onboarding

import com.compscicomputations.core.client.publik.models.OnboardingItem
import com.compscicomputations.ui.utils.ProgressState

data class OnboardingUiState(
    val items: List<OnboardingItem> = emptyList(),

    val progressState: ProgressState = ProgressState.Loading()
)