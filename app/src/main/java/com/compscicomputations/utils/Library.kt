package com.compscicomputations.utils

import android.view.View
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import com.compscicomputations.utils.network.ConnectionState
import com.compscicomputations.utils.network.Connectivity.currentConnectivityState
import com.compscicomputations.utils.network.Connectivity.observeConnectivityAsFlow
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOn


//internal inline val Context.asActivity: Activity
//    get() {
//        var context = this
//        while (context is ContextWrapper) {
//            if (context is Activity) return context
//            context = context.baseContext
//        }
//        throw IllegalStateException("Permissions should be called in the context of an Activity")
//    }

@ExperimentalCoroutinesApi
@Composable
fun rememberConnectivityState(): State<ConnectionState> {
    val context = LocalContext.current

    // Creates a State<ConnectionState> with current connectivity state as initial value
    return produceState(initialValue = context.currentConnectivityState) {
        // In a coroutine, can make suspend calls
        context.observeConnectivityAsFlow()
            .flowOn(Dispatchers.IO)
            .collect { value = it }
    }
}

fun Modifier.onVisible(onVisible: () -> Unit): Modifier = composed {
    val view = LocalView.current
    var isVisible by remember { mutableStateOf(false) }

    if (isVisible) {
        LaunchedEffect(Unit) {
            onVisible()
        }
    }

    onGloballyPositioned { coordinates ->
        isVisible = coordinates.isCompletelyVisible(view)
    }
}

fun LayoutCoordinates.isCompletelyVisible(view: View): Boolean {
    if (!isAttached) return false
    // Window relative bounds of our compose root view that are visible on the screen
    val globalRootRect = android.graphics.Rect()
    if (!view.getGlobalVisibleRect(globalRootRect)) {
        // we aren't visible at all.
        return false
    }
    val bounds = boundsInWindow()
    // Make sure we are completely in bounds.
    return bounds.top >= globalRootRect.top &&
            bounds.left >= globalRootRect.left &&
            bounds.right <= globalRootRect.right &&
            bounds.bottom <= globalRootRect.bottom
}