package com.compscicomputations.data
class Task<TResult>(
    isComplete: Boolean = false,
    isSuccessful: Boolean = false,
    exception: Exception? = null,
    result: TResult? = null
) {
    var isComplete: Boolean = isComplete
        private set
    var isSuccessful: Boolean = isSuccessful
        private set
    var exception: Exception? = exception
        private set
    var result: TResult? = result
        private set

    fun addOnCompleteListener(onComplete: (task: Task<TResult>) -> Unit): Task<TResult> {
        if (isComplete) onComplete(this)
        return this
    }
    fun addOnFailureListener(onFailure: (e: Exception) -> Unit): Task<TResult> {
        if (!isSuccessful) onFailure(exception!!)
        return this
    }
    fun addOnSuccessListener(onSuccess: (result: TResult) -> Unit): Task<TResult> {
        if (isSuccessful) onSuccess(result!!)
        return this
    }

    companion object {
        fun <TResult> successfulTask(result: TResult) : Task<TResult> {
            return Task(isComplete = true, isSuccessful = true, null, result)
        }
        fun <TResult> failedTask(e: Exception) : Task<TResult> {
            return Task(isComplete = true, isSuccessful = false, e, null)
        }
    }
}