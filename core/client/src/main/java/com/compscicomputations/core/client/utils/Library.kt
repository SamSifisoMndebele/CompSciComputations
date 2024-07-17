package com.compscicomputations.core.client.utils

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend inline fun <T> ktorRequest(
    ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
    crossinline block: suspend () -> T
): T = withContext(ioDispatcher) {
    block()
}