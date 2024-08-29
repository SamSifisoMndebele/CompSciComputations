package com.compscicomputations.ui.utils

sealed interface ProgressState {
    data object Idle : ProgressState
    data class Loading(val message: String = "Loading...") : ProgressState
    data object Success : ProgressState
    data class Error(val message: String?) : ProgressState
}

internal val ProgressState.isLoading: Boolean
    get() = this is ProgressState.Loading

internal val ProgressState.isSuccess: Boolean
    get() = this is ProgressState.Success

internal val ProgressState.isError: Boolean
    get() = this is ProgressState.Error