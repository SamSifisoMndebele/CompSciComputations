package com.compscicomputations.ui.auth.onboarding

import com.compscicomputations.client.publik.data.source.local.OnboardingItem
import com.compscicomputations.ui.utils.ProgressState

data class OnboardingUiState(
    val items: List<OnboardingItem> = emptyList(),

    val progressState: ProgressState = ProgressState.Loading("Onboarding Loading,\nplease wait...")
)