package com.compscicomputations.utils

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.Instant

@RequiresApi(Build.VERSION_CODES.O)
fun getExecutionTime(runnable: () -> Unit) {
    val thread = Thread {
        val start = Instant.now()
        runnable.invoke()
        val end = Instant.now()
        val executionTime = end.toEpochMilli() - start.toEpochMilli()
        println("Execution time: $executionTime milliseconds")
        println("========================================")
    }
    thread.start()
}