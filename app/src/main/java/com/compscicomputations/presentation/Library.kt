package com.compscicomputations.presentation

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.compscicomputations.R
import com.compscicomputations.ui.theme.comicNeueFamily
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.callbackFlow

@Composable
fun LoadingDialog(
    show: Boolean,
    message: String = "Loading...",
    onCancel: (() -> Unit)? = null
) {
    AnimatedVisibility(visible = show) {
        Surface(modifier = Modifier
            .fillMaxSize()
            .padding(start = 7.dp, end = 7.dp, top = 64.dp, bottom = 32.dp),
            shape = RoundedCornerShape(44.dp),
            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f)){
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                if (onCancel != null) {
                    Row(horizontalArrangement = Arrangement.End,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 16.dp, top = 8.dp)) {
                        TextButton(onClick = onCancel) {
                            Text(text = "Cancel", fontSize = 18.sp)
                        }
                    }
                }
                Spacer(modifier = Modifier.weight(1f))
                CircularProgressIndicator(
                    strokeWidth = 6.dp,
                    modifier = Modifier
                        .size(80.dp)
                        .padding(bottom = 8.dp),
                    color = MaterialTheme.colorScheme.secondary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
                Text(text = message, fontSize = 18.sp, fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary)
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

data class ExceptionData(
    var message: String?,
    val delayMillis: Long = 5000,
    val onCancel: (() -> Unit)? = null,
)
@Composable
fun ExceptionDialog(
    exceptionState: MutableState<ExceptionData?>
) {
    var state by exceptionState
    LaunchedEffect(key1 = state) {
        if (state != null) {
            delay(state!!.delayMillis)
            state = null
        }
    }
    AnimatedVisibility(visible = state != null) {
        Surface(modifier = Modifier
            .fillMaxSize()
            .padding(start = 7.dp, end = 7.dp, top = 64.dp, bottom = 32.dp),
            shape = RoundedCornerShape(44.dp),
            color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.9f)){
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Row(horizontalArrangement = Arrangement.End,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 16.dp, top = 8.dp)) {
                    TextButton(onClick = {
                        if (state?.onCancel != null) {
                            state?.onCancel
                            state = null
                        } else {
                            state = null
                        }
                    }) {
                        Text(text = "Cancel", fontSize = 18.sp)
                    }
                }

                Spacer(modifier = Modifier.weight(1f))
                Image(
                    modifier = Modifier
                        .size(180.dp)
                        .padding(bottom = 8.dp)
                        .clip(RoundedCornerShape(36.dp)),
                    painter = painterResource(id = R.drawable.img_error),
                    contentDescription = "Error image",
                )
                Spacer(modifier = Modifier.weight(.2f))
                Text(text = state?.message?:"An unexpected error occurred.", fontSize = 18.sp, fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary)
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun OptionButton(
    padding: PaddingValues = PaddingValues(bottom = 8.dp),
    iconVector: ImageVector? = null,
    painter: Painter? = null,
    text: String,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .padding(padding)
            .fillMaxWidth()
            .height(64.dp),
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
        )
    ) {
        Row(
            Modifier
                .padding(vertical = 6.dp, horizontal = 18.dp)
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (iconVector != null) {
                Icon(
                    imageVector = iconVector,
                    contentDescription = "$text Icon",
                    tint = MaterialTheme.colorScheme.primary
                )
            } else if (painter != null){
                Icon(
                    modifier = Modifier.padding(vertical = 10.dp),
                    painter = painter,
                    contentDescription = "$text Icon",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Text(
                text = text, Modifier.padding(start = 24.dp, end = 8.dp), color = MaterialTheme.colorScheme.primary,
                fontSize = 22.sp, fontWeight = FontWeight.Bold, fontFamily = comicNeueFamily,
                maxLines = 1
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompSciScaffold(
    modifier: Modifier = Modifier,
    navigateUp: (() -> Unit)? = null,
    title: String,
    menuActions: @Composable (RowScope.() -> Unit) = {},
    bottomBar: @Composable () -> Unit = {},
    snackBarHost: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    containerColor: Color = MaterialTheme.colorScheme.background,
    contentColor: Color = contentColorFor(containerColor),
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            Card(Modifier.padding(horizontal = 8.dp), shape = RoundedCornerShape(24.dp)) {
                CenterAlignedTopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.secondaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.secondary,
                    ),
                    navigationIcon = {
                        if (navigateUp != null)
                            IconButton(onClick = navigateUp, Modifier.fillMaxHeight()) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
                                    contentDescription = "Back Button",
                                    tint = Color.White,
                                )
                            }
                        else
                            Icon(
                                painter = painterResource(id = R.drawable.img_logo_name),
                                contentDescription = stringResource(id = R.string.app_name),
                                modifier = Modifier.height(32.dp).padding(start = 8.dp),
                                tint = Color.White,
                            )
                    },
                    title = {
                        Text(
                            title,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 26.sp,
                            fontFamily = comicNeueFamily
                        )
                    },
                    actions = menuActions
                )
            }
        },
        bottomBar = bottomBar,
        snackbarHost = snackBarHost,
        floatingActionButton = floatingActionButton,
        floatingActionButtonPosition = floatingActionButtonPosition,
        containerColor = containerColor,
        contentColor = contentColor
    ) { p ->
        val topPadding = p.calculateTopPadding() - 24.dp
        val bottomPadding = p.calculateBottomPadding() - 24.dp
        Column(
            Modifier.padding(
                top = if (topPadding <= 0.dp) 0.dp else topPadding,
                bottom = if (bottomPadding <= 0.dp) 0.dp else bottomPadding
            )
        ) {
            content(
                PaddingValues(
                    start = 8.dp,
                    end = 8.dp,
                    top = if (topPadding <= 0.dp) 0.dp else 32.dp,
                    bottom = if (bottomPadding <= 0.dp) 0.dp else 32.dp
                )
            )
        }
    }
}

sealed class ConnectionState {
    data object Available : ConnectionState()
    data object Unavailable : ConnectionState()
}

private fun getCurrentConnectivityState(
    connectivityManager: ConnectivityManager
): ConnectionState {
    val connected = connectivityManager.allNetworks.any { network ->
        connectivityManager.getNetworkCapabilities(network)
            ?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            ?: false
    }

    return if (connected) ConnectionState.Available else ConnectionState.Unavailable
}

private val Context.currentConnectivityState: ConnectionState
    get() {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return getCurrentConnectivityState(connectivityManager)
    }

@ExperimentalCoroutinesApi
private fun Context.observeConnectivityAsFlow() = callbackFlow {
    val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    val callback = NetworkCallback { connectionState -> trySend(connectionState) }

    val networkRequest = NetworkRequest.Builder()
        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        .build()

    connectivityManager.registerNetworkCallback(networkRequest, callback)

    // Set current state
    val currentState = getCurrentConnectivityState(connectivityManager)
    trySend(currentState)

    // Remove callback when not used
    awaitClose {
        // Remove listeners
        connectivityManager.unregisterNetworkCallback(callback)
    }
}

private fun NetworkCallback(callback: (ConnectionState) -> Unit): ConnectivityManager.NetworkCallback {
    return object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            callback(ConnectionState.Available)
        }

        override fun onLost(network: Network) {
            callback(ConnectionState.Unavailable)
        }
    }
}

@ExperimentalCoroutinesApi
@Composable
fun connectivityState(): State<ConnectionState> {
    val context = LocalContext.current

    // Creates a State<ConnectionState> with current connectivity state as initial value
    return produceState(initialValue = context.currentConnectivityState) {
        // In a coroutine, can make suspend calls
        context.observeConnectivityAsFlow().collect { value = it }
    }
}