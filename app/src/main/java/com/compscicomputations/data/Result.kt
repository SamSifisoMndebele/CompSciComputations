package com.compscicomputations.data

class Result<out T> {
    private val value: T?
    private val exception: Exception?
    constructor(value: T) {
        this.value = value
        this.exception = null
    }
    constructor(exception: Exception) {
        this.value = null
        this.exception = exception
    }

    fun onFailure(action: (exception: Exception) -> Unit): Result<T> {
        if (value == null && exception != null) action(exception)
        return this
    }
    fun onSuccess(action: (value: T) -> Unit): Result<T> {
        if (value != null && exception == null) action(value)
        return this
    }
    fun onComplete(action: (value: T?, exception: Exception?) -> Unit): Result<T> {
        action(value, exception)
        return this
    }
}

