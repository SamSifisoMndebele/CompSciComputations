package com.compscicomputations.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.Intent.getIntent
import android.os.Build
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat.recreate
import androidx.core.content.ContextCompat.startActivity
import com.compscicomputations.utils.network.ConnectionState
import com.compscicomputations.utils.network.Connectivity.currentConnectivityState
import com.compscicomputations.utils.network.Connectivity.observeConnectivityAsFlow
import kotlinx.coroutines.ExperimentalCoroutinesApi


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

internal fun Context.restartActivity() {
    asActivity.recreate()
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