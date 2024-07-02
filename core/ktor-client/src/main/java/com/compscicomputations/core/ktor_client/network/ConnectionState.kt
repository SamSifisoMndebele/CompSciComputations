package com.compscicomputations.core.ktor_client.network

sealed class ConnectionState {
    data object Available : ConnectionState()
    data object Unavailable : ConnectionState()
}