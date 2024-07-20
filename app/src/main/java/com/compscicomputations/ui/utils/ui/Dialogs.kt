package com.compscicomputations.ui.utils.ui

import android.graphics.Bitmap
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.compscicomputations.R
import com.compscicomputations.ui.utils.ProgressState
import kotlinx.coroutines.delay

@Composable
fun LoadingDialog(
    progressState: ProgressState,
    backgroundBitmap: Bitmap? = null,
    onDismiss: (() -> Unit)? = null,
) {
    AnimatedVisibility(
        modifier = Modifier.fillMaxSize(),
        visible = progressState is ProgressState.Loading,
        enter = fadeIn(),
        exit = fadeOut()
    ) {
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background.copy(alpha = 0.9f)),
        ) {
            Box {
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
}

@Composable
fun ExceptionDialog(
    progressState: ProgressState,
    delayMillis: Long = 6000,
    backgroundBitmap: Bitmap? = null,
    onDismiss: (() -> Unit)?,
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
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.9f)),
        ) {
            Box {
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
}