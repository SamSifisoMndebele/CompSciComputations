package com.compscicomputations.ui.utils


import android.graphics.Bitmap
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.automirrored.filled.FollowTheSigns
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.compscicomputations.R
import com.compscicomputations.theme.AppRed
import com.compscicomputations.theme.comicNeueFamily
import com.compscicomputations.utils.network.ConnectionState
import com.compscicomputations.utils.network.Connectivity.currentConnectivityState
import com.compscicomputations.utils.network.Connectivity.observeConnectivityAsFlow
import dev.jakhongirmadaminov.glassmorphiccomposables.fastblur
import dev.shreyaspatil.capturable.Capturable
import dev.shreyaspatil.capturable.controller.rememberCaptureController
import eu.bambooapps.material3.pullrefresh.PullRefreshIndicator
import eu.bambooapps.material3.pullrefresh.pullRefresh
import eu.bambooapps.material3.pullrefresh.rememberPullRefreshState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

@Composable
fun LoadingDialog(
    progressState: ProgressState,
    onDismiss: (() -> Unit)? = null,
    backgroundBitmap: Bitmap? = null,
) {
    AnimatedVisibility(
        modifier = Modifier.fillMaxSize(),
        visible = progressState is ProgressState.Loading,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.9f)),
        ) {
            backgroundBitmap?.asImageBitmap()?.let {
                Image(
                    bitmap = it,
                    contentDescription = "Background Image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.FillWidth
                )
            }
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (onDismiss != null) {
                    Row(horizontalArrangement = Arrangement.End,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 16.dp, top = 8.dp)) {
                        TextButton(onClick = onDismiss) {
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
                val message = if (progressState is ProgressState.Loading) progressState.message else ""
                Text(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    text = message,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary
                )
                Spacer(modifier = Modifier.weight(1.3f))
            }
        }
    }
}

@Composable
fun ExceptionDialog(
    progressState: ProgressState,
    delayMillis: Long = 6000,
    onDismiss: (() -> Unit)?,
    backgroundBitmap: Bitmap? = null,
) {
    LaunchedEffect(progressState) {
        if (progressState is ProgressState.Error) {
            delay(delayMillis)
            onDismiss?.invoke()
        }
    }
    AnimatedVisibility(
        modifier = Modifier.fillMaxSize(),
        visible = progressState is ProgressState.Error,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.9f)),
        ) {
            backgroundBitmap?.asImageBitmap()?.let {
                Image(
                    bitmap = it,
                    contentDescription = "Background Image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.FillWidth
                )
            }
            Column(
                modifier = Modifier.padding(bottom = 64.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (onDismiss != null) {
                    Row(horizontalArrangement = Arrangement.End,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 16.dp, top = 8.dp)) {
                        TextButton(onClick = onDismiss) {
                            Text(text = "Cancel", fontSize = 18.sp)
                        }
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
                val message = if (progressState is ProgressState.Error) progressState.message else ""
                Text(
                    modifier = Modifier.padding(horizontal = 16.dp),
                    text = message ?:"An unexpected error occurred.",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary
                )
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@Composable
fun OptionButton(
    padding: PaddingValues = PaddingValues(bottom = 8.dp),
    iconVector: ImageVector? = null,
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
            }
            Text(
                text = text, Modifier.padding(start = 24.dp, end = 8.dp), color = MaterialTheme.colorScheme.primary,
                fontSize = 22.sp, fontWeight = FontWeight.Bold, fontFamily = comicNeueFamily,
                maxLines = 1
            )
        }
    }
}
@Composable
fun OptionButton(
    padding: PaddingValues = PaddingValues(bottom = 8.dp),
    iconUrl: String?,
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
            if (iconUrl != null) {
                AsyncImage(
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .size(48.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    model = iconUrl,
                    contentDescription = "$text Icon",
                    contentScale = ContentScale.FillHeight
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
    title: String,
    navigateUp: (() -> Unit)? = null,
    menuActions: @Composable (RowScope.() -> Unit) = {},
    bottomBar: @Composable (cornerRadius: Dp) -> Unit = {},
    snackBarHost: @Composable () -> Unit = {},
    floatingActionButton: @Composable () -> Unit = {},
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    containerColor: Color = MaterialTheme.colorScheme.background,
    contentColor: Color = contentColorFor(containerColor),
    isRefreshing: Boolean? = null,
    onRefresh: () -> Unit = {},
    topBarCornerRadius: Dp = 24.dp,
    bottomBarCornerRadius: Dp = 24.dp,
    content: @Composable ColumnScope.(PaddingValues) -> Unit
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            Card(Modifier.padding(horizontal = 8.dp), shape = RoundedCornerShape(topBarCornerRadius)) {
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
                                modifier = Modifier
                                    .height(32.dp)
                                    .padding(start = 8.dp),
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
        bottomBar = { bottomBar(bottomBarCornerRadius) },
        snackbarHost = snackBarHost,
        floatingActionButton = floatingActionButton,
        floatingActionButtonPosition = floatingActionButtonPosition,
        containerColor = containerColor,
        contentColor = contentColor
    ) { p ->
        val topPadding = p.calculateTopPadding() - topBarCornerRadius
        val bottomPadding = p.calculateBottomPadding() - bottomBarCornerRadius

        Column(
            Modifier.padding(
                top = if (topPadding <= 0.dp) 0.dp else topPadding,
                bottom = if (bottomPadding <= 0.dp) 0.dp else bottomPadding
            )
        ) {
            if (isRefreshing != null) {
                val pullRefreshState = rememberPullRefreshState(refreshing = isRefreshing, onRefresh = onRefresh)
                Box(modifier = Modifier.pullRefresh(pullRefreshState)) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
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

                    PullRefreshIndicator(
                        refreshing = isRefreshing,
                        state = pullRefreshState,
                        modifier = Modifier
                            .align(Alignment.TopCenter)
                            .padding(top = 32.dp),
                        scale = true
                    )
                }
            } else {
                content(
                    PaddingValues(
                        start = 8.dp,
                        end = 8.dp,
                        top = (if (topPadding <= 0.dp) 0.dp else topBarCornerRadius) + 8.dp,
                        bottom = (if (bottomPadding <= 0.dp) 0.dp else bottomBarCornerRadius) + 8.dp,
                    )
                )
            }
        }
    }
}

@Composable
fun CompSciAuthScaffold(
    title: String,
    description: String,
    navigateUp: (() -> Unit)?,
    navigateOnboarding: (() -> Unit)?,
    progressState: ProgressState,
    onLoadingDismiss: () -> Unit,
    onExceptionDismiss: () -> Unit,
    dialogsBlurRadius: Int = 32,
    content: @Composable ColumnScope.() -> Unit
) {
    val captureController = rememberCaptureController()
    var capturedBitmap by remember { mutableStateOf<Bitmap?>(null) }
    Capturable(
        controller = captureController,
        onCaptured = { bitmap, _ ->
            // This is captured bitmap of a content inside Capturable Composable.
            bitmap?.let {
                fastblur(it.asAndroidBitmap(), 1f, dialogsBlurRadius)?.let { fastBlurred ->
                    // Bitmap is captured successfully. Do something with it!
                    capturedBitmap = fastBlurred
                }
            }
        }

    ) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            topBar = {
                Box(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (navigateUp != null) {
                        IconButton(
                            onClick = navigateUp,
                            Modifier.padding(vertical = 8.dp, horizontal = 6.dp)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
                                contentDescription = "Back Button"
                            )
                        }
                    }
                    if (navigateOnboarding != null) {
                        IconButton(
                            onClick = navigateOnboarding,
                            Modifier.padding(vertical = 8.dp, horizontal = 6.dp).align(Alignment.CenterEnd)
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.FollowTheSigns,
                                contentDescription = "Onboarding Button"
                            )
                        }
                    }
                }
            },
        ) { padding ->
            val lottieComposition by rememberLottieComposition(
                LottieCompositionSpec.RawRes(
                    R.raw.login_anim
                )
            )
            val lottieProgress by animateLottieCompositionAsState(
                lottieComposition,
                iterations = LottieConstants.IterateForever,
                isPlaying = true
            )
            Box(
                Modifier.padding(
                    PaddingValues(
                        start = 8.dp,
                        end = 8.dp,
                        top = 0.dp,
                        bottom = 0.dp
                    )
                )
            ) {
                LazyColumn(
                    contentPadding = PaddingValues(top = padding.calculateTopPadding()+64.dp)
                ) {
                    item {
                        LottieAnimation(
                            composition = lottieComposition,
                            progress = lottieProgress,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(360.dp),
                            contentScale = ContentScale.FillWidth
                        )
                    }
                    item {
                        Text(
                            text = description,
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp),
                        )
                    }
                    item {
                        Column {
                            content()
                        }
                    }
                }
                Text(
                    text = title,
                    fontFamily = comicNeueFamily,
                    color = AppRed,
                    fontWeight = FontWeight.Bold,
                    fontStyle = FontStyle.Italic,
                    fontSize = 52.sp,
                    modifier = Modifier.padding(top = padding.calculateTopPadding()),
                    style = TextStyle(
                        shadow = Shadow(color = MaterialTheme.colorScheme.background, blurRadius = 8f)
                    )
                )
            }
        }
    }
    LaunchedEffect(progressState) {
        if (progressState is ProgressState.Loading && dialogsBlurRadius != 0)
            withContext(Dispatchers.Main) { captureController.capture() }
    }
    LoadingDialog(
        progressState = progressState,
        onDismiss = onLoadingDismiss,
        backgroundBitmap = capturedBitmap
    )
    ExceptionDialog(
        progressState = progressState,
        onDismiss = onExceptionDismiss,
        backgroundBitmap = capturedBitmap
    )
}

@Composable
fun shimmerBrush(showShimmer: Boolean = true,targetValue:Float = 1500f): Brush {
    return if (showShimmer) {
        val shimmerColors = listOf(
            Color.LightGray.copy(alpha = 0.4f),
            Color.LightGray.copy(alpha = 0.1f),
            Color.LightGray.copy(alpha = 0.4f),
        )

        val transition = rememberInfiniteTransition(label = "Shimmer Infinite Transition")
        val translateAnimation = transition.animateFloat(
            label = "Shimmer Animation State",
            initialValue = 0f,
            targetValue = targetValue,
            animationSpec = infiniteRepeatable(
                animation = tween(1000), repeatMode = RepeatMode.Restart
            ),
        )
        Brush.linearGradient(
            colors = shimmerColors,
            start = Offset.Zero,
            end = Offset(x = translateAnimation.value, y = translateAnimation.value)
        )
    } else {
        Brush.linearGradient(
            colors = listOf(Color.Transparent, Color.Transparent,),
            start = Offset.Zero,
            end = Offset.Zero
        )
    }
}
@Composable
fun rememberShimmerBrushState(show: Boolean = true): MutableState<Boolean> {
    return remember { mutableStateOf(show) }
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
