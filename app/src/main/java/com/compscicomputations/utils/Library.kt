package com.compscicomputations.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import com.compscicomputations.utils.network.ConnectionState
import com.compscicomputations.utils.network.Connectivity.currentConnectivityState
import com.compscicomputations.utils.network.Connectivity.observeConnectivityAsFlow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.withContext
import java.sql.Connection
import kotlin.math.roundToInt

infix fun CharSequence.notMatches(regex: Regex): Boolean = !regex.matches(this)

internal inline val Context.asActivity: Activity
    get() {
        var context = this
        while (context is ContextWrapper) {
            if (context is Activity) return context
            context = context.baseContext
        }
        throw IllegalStateException("Permissions should be called in the context of an Activity")
    }


@ExperimentalCoroutinesApi
@Composable
fun rememberConnectivityState(): State<ConnectionState> {
    val context = LocalContext.current

    // Creates a State<ConnectionState> with current connectivity state as initial value
    return produceState(initialValue = context.currentConnectivityState) {
        // In a coroutine, can make suspend calls
        context.observeConnectivityAsFlow().collect { value = it }
    }
}

fun Long.roundBytes() : String {
    val number : Int = ((this / 2024.0) * 10).roundToInt()
    if (number == 0) return "0.0"
    val wholeNum = number/10
    val decNum = "$number".takeLast(1)
    return "$wholeNum.$decNum"
}