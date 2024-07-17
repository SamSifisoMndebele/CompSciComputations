package com.compscicomputations.ui.auth.onboarding

import com.compscicomputations.core.database.publik.models.OnboardingItem
import com.compscicomputations.ui.utils.ProgressState

data class OnboardingUiState(
    val items: List<OnboardingItem> = emptyList(),

    val progressState: ProgressState = ProgressState.Loading()
)