package com.compscicomputations.number_systems.data.model

import androidx.compose.runtime.Stable
import com.compscicomputations.number_systems.data.source.local.AiResponse

@Stable
sealed interface AIState {
    data object Idle : AIState
    data class Loading(val message: String = "Loading...") : AIState
    data class Success(val response: AiResponse) : AIState
    data class Error(val message: String? = "") : AIState
}