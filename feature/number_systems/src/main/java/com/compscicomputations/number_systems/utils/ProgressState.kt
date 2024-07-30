package com.compscicomputations.number_systems.utils

sealed interface ProgressState {
    data object Idle : ProgressState
    data class Loading(val message: String = "Loading...") : ProgressState
    data class Success(val aiOutput: String? = null) : ProgressState
    data class Error(val message: String?) : ProgressState
}

val ProgressState.isLoading: Boolean
    get() = this is ProgressState.Loading

val ProgressState.isSuccess: Boolean
    get() = this is ProgressState.Success

val ProgressState.isError: Boolean
    get() = this is ProgressState.Error