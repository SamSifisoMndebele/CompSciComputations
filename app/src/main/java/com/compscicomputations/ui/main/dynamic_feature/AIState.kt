package com.compscicomputations.ui.main.dynamic_feature

import androidx.compose.runtime.Stable
import com.compscicomputations.data.source.local.AiResponse

@Stable
sealed interface AIState {
    data object Idle : AIState
    data class Loading(val message: String = "Loading...") : AIState
    data class Success(val response: AiResponse) : AIState
    data class Error(val e: Exception) : AIState
}