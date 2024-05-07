package com.compscicomputations.ui.auth.reset

sealed interface ResetUiEvent {
    data class OnEmailChange(val email: String): ResetUiEvent
    data object Reset: ResetUiEvent
}