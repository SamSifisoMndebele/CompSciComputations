package com.compscicomputations.client.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.UUID

internal suspend inline fun <T> ktorRequest(
    ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    crossinline block: suspend () -> T
): T = withContext(ioDispatcher) {
    block()
}

// This method might be computationally expensive
internal fun createUUID() : String {
    return UUID.randomUUID().toString()
}