package com.compscicomputations.ui.auth.onboarding

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowBackIos
import androidx.compose.material.icons.automirrored.outlined.ArrowForwardIos
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.compscicomputations.client.publik.models.SourceType
import com.compscicomputations.theme.comicNeueFamily
import com.compscicomputations.ui.utils.isLoading
import com.compscicomputations.ui.utils.rememberShimmerBrushState
import com.compscicomputations.ui.utils.shimmerBrush
import kotlinx.coroutines.launch

@Composable
fun OnboardingScreen(
    uiState: OnboardingUiState,
    navigateLogin: () -> Unit,
    navigateRegister: () -> Unit,
) {
    val items = uiState.items ?: return
    val scope = rememberCoroutineScope()
    val pageState = rememberPagerState { items.size }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        // Top section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp, end = 8.dp)
        ) {
            // Back button
            if (pageState.canScrollBackward) {
                IconButton(
                    onClick = {
                        scope.launch {
                            pageState.scrollToPage(pageState.currentPage - 1)
                        }
                    },
                    modifier = Modifier.align(Alignment.CenterStart)) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.ArrowBackIos,
                        contentDescription = "Back Button Icon",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
                }
            }

            // Skip Button
            if (pageState.canScrollForward) {
                TextButton(
                    onClick = {
                        scope.launch {
                            pageState.scrollToPage(items.size - 1)
                        }
                    },
                    modifier = Modifier.align(Alignment.CenterEnd),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(text = "Skip", color = MaterialTheme.colorScheme.onBackground)
                }
            }
        }

        // Boarding section
        HorizontalPager(
            state = pageState,
            modifier = Modifier.fillMaxHeight(0.9f)
                .fillMaxWidth()
        ) { page ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                val showImage = rememberShimmerBrushState()
                when(items[page].type) {
                    SourceType.IMAGE -> {
                        AsyncImage(
                            modifier = Modifier.fillMaxWidth()
                                .padding(8.dp)
                                .background(shimmerBrush(showShimmer = showImage.value))
                                .height(280.dp)
                                .clip(RoundedCornerShape(18.dp)),
                            contentScale = ContentScale.FillWidth,
                            model = items[page].sourceUrl,
                            contentDescription = "On boarding image.",
                            onSuccess = { showImage.value = false },
                        )
                    }
                    SourceType.LOTTIE -> {
                        val preloaderLottieComposition by rememberLottieComposition(
                            LottieCompositionSpec.Url(url = items[page].sourceUrl),
                            onRetry = { failCount, previousException ->
                                false
                            }
                        )
                        val preloaderProgress by animateLottieCompositionAsState(
                            preloaderLottieComposition,
                            iterations = LottieConstants.IterateForever,
                            isPlaying = true
                        )
                        LottieAnimation(
                            composition = preloaderLottieComposition,
                            progress = preloaderProgress,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .background(shimmerBrush(showShimmer = uiState.progressState.isLoading))
                                .height(280.dp)
                                .clip(RoundedCornerShape(18.dp)),
                            contentScale = ContentScale.FillWidth
                        )
                    }
                }

                Spacer(modifier = Modifier.height(25.dp))
                Text(
                    modifier = Modifier.widthIn(64.dp)
                        .background(shimmerBrush(showShimmer = uiState.progressState.isLoading)),
                    text = items[page].title,
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    letterSpacing = 1.sp,
                )
                Spacer(modifier = Modifier.height(8.dp))
                items[page].description?.let {
                    Text(
                        modifier = Modifier.fillMaxWidth().padding(10.dp)
                            .background(shimmerBrush(showShimmer = uiState.progressState.isLoading)),
                        text = it,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontWeight = FontWeight.Light,
                        textAlign = TextAlign.Center,
                        letterSpacing = 1.sp,
                    )
                }
            }
        }

        // Bottom section
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            // Indicators
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier
                    .padding(start = 8.dp)
                    .align(Alignment.CenterStart)
            ) {
                repeat(items.size) {
                    Indicator(isSelected = it == pageState.currentPage)
                }
            }

            // FAB Next
            if (pageState.canScrollForward) {
                FloatingActionButton(
                    onClick = {
                        scope.launch {
                            pageState.animateScrollToPage(pageState.currentPage + 1)
                        }
                    },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                        .clip(RoundedCornerShape(18.dp))
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.ArrowForwardIos,
                        tint = Color.White,
                        contentDescription = "Forward Icon"
                    )
                }
            }
            else {
                // Done
                Row(
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    TextButton(onClick = navigateRegister) {
                        Text(
                            text = "Register",
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp,
                            fontFamily = comicNeueFamily
                        )
                    }

                    ExtendedFloatingActionButton(
                        onClick = navigateLogin,
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.clip(RoundedCornerShape(18.dp))
                    ) {
                        Text(
                            text = "Login",
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp,
                            fontFamily = comicNeueFamily
                        )
                        Icon(
                            imageVector = Icons.AutoMirrored.Outlined.ArrowForwardIos,
                            tint = Color.White,
                            contentDescription = "Forward Icon"
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun Indicator(isSelected: Boolean) {
    val width = animateDpAsState(
        targetValue = if (isSelected) 25.dp else 10.dp,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy), label = ""
    )
    Box(
        modifier = Modifier
            .height(10.dp)
            .width(width.value)
            .clip(CircleShape)
            .background(
                color = if (isSelected) MaterialTheme.colorScheme.primary else Color(0XFFF8E2E7)
            )
    )
}