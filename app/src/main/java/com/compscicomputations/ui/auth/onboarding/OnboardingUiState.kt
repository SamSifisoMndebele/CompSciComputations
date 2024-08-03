package com.compscicomputations.ui.auth.onboarding

import com.compscicomputations.client.publik.data.model.local.OnboardingItem
import com.compscicomputations.ui.utils.ProgressState

data class OnboardingUiState(
    val items: List<OnboardingItem> = emptyList(),

    val progressState: ProgressState = ProgressState.Loading("Onboarding Loading,\nplease wait...")
)