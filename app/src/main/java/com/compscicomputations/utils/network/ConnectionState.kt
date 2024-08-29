package com.compscicomputations.utils.network

sealed class ConnectionState {
    data object Available : ConnectionState()
    data object Unavailable : ConnectionState()

    val isAvailable: Boolean
        get() = this is Available

    val isUnavailable: Boolean
        get() = this is Unavailable
}

