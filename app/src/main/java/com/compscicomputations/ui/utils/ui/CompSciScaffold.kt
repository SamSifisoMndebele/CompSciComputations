package com.compscicomputations.ui.utils.ui

import android.graphics.Bitmap
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material.icons.automirrored.filled.FollowTheSigns
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.compscicomputations.R
import com.compscicomputations.theme.comicNeueFamily
import com.compscicomputations.ui.utils.ProgressState
import com.compscicomputations.ui.utils.isError
import com.compscicomputations.ui.utils.isLoading
import dev.jakhongirmadaminov.glassmorphiccomposables.fastblur
import dev.shreyaspatil.capturable.Capturable
import dev.shreyaspatil.capturable.controller.rememberCaptureController
import eu.bambooapps.material3.pullrefresh.PullRefreshIndicator
import eu.bambooapps.material3.pullrefresh.pullRefresh
import eu.bambooapps.material3.pullrefresh.rememberPullRefreshState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompSciScaffold(
    modifier: Modifier = Modifier,
    title: String,
    navigateUp: (() -> Unit)? = null,
    menuActions: @Composable (RowScope.() -> Unit) = {},
    tabsBar: @Composable() (() -> Unit)? = null,
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
                        titleContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    ),
                    navigationIcon = {
                        if (navigateUp != null)
                            IconButton(onClick = navigateUp, Modifier.fillMaxHeight()) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
                                    contentDescription = "Back Button",
                                    tint = MaterialTheme.colorScheme.onSecondaryContainer,
                                )
                            }
                        else
                            Icon(
                                painter = painterResource(id = R.drawable.img_logo_name),
                                contentDescription = stringResource(id = R.string.app_name),
                                modifier = Modifier
                                    .height(32.dp)
                                    .padding(start = 8.dp),
                                tint = MaterialTheme.colorScheme.onSecondaryContainer,
                            )
                    },
                    title = {
                        Text(
                            title,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            fontWeight = FontWeight.Bold,
                            fontSize = 26.sp,
                            fontFamily = comicNeueFamily
                        )
                    },
                    actions = menuActions
                )
                if (tabsBar != null) {
                    HorizontalDivider()
                    tabsBar()
                }
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
                            Modifier
                                .padding(vertical = 8.dp, horizontal = 6.dp)
                                .align(Alignment.CenterEnd)
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
                    color = MaterialTheme.colorScheme.primary,
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
        message = if (progressState is ProgressState.Loading) progressState.message else "",
        visible = progressState.isLoading,
        onDismiss = onLoadingDismiss,
        backgroundBitmap = capturedBitmap
    )
    ExceptionDialog(
        message = (if (progressState is ProgressState.Error) progressState.message else "")
            ?:"An unexpected error occurred.",
        visible = progressState.isError,
        onDismiss = onExceptionDismiss,
        backgroundBitmap = capturedBitmap
    )
}