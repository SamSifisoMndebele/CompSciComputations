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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.compscicomputations.R
import com.compscicomputations.ui.utils.ProgressState
import com.compscicomputations.ui.utils.isError
import com.compscicomputations.ui.utils.isLoading
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

@Composable
fun LoadingDialog(
    message: String,
    visible: Boolean,
    backgroundBitmap: Bitmap? = null,
    onCancel: () -> Unit = {},
    onDismiss: () -> Unit,
) {
    if (visible) {
        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(
                dismissOnClickOutside = false,
                dismissOnBackPress = false,
                usePlatformDefaultWidth = false
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background.copy(alpha = 0.75f)),
            ) {
                backgroundBitmap?.asImageBitmap()?.let {
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        bitmap = it,
                        contentDescription = "Background Image",
                        contentScale = ContentScale.FillWidth
                    )
                }
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(
                        strokeWidth = 6.dp,
                        modifier = Modifier
                            .size(80.dp)
                            .padding(bottom = 8.dp),
                        color = MaterialTheme.colorScheme.secondary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
                    )
                    Text(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        text = message,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
                TextButton(
                    onClick = onCancel,
                    modifier = Modifier.align(Alignment.TopEnd)
                        .padding(end = 16.dp, top = 8.dp)
                ) {
                    Text(text = "Cancel", fontSize = 18.sp)
                }
            }
        }
    }
}

@Composable
fun ExceptionDialog(
    message: String,
    visible: Boolean,
    backgroundBitmap: Bitmap? = null,
    delayMillis: Long = 6000,
    onDismiss: () -> Unit,
) {
    LaunchedEffect(visible) {
        withContext(Dispatchers.IO) {
            if (visible) {
                delay(delayMillis)
                onDismiss.invoke()
            }
        }
    }
    if (visible) {
        Dialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(
                dismissOnClickOutside = false,
                dismissOnBackPress = false,
                usePlatformDefaultWidth = false
            )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.75f)),
            ) {
                backgroundBitmap?.asImageBitmap()?.let {
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        bitmap = it,
                        contentDescription = "Background Image",
                        contentScale = ContentScale.FillWidth
                    )
                }
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Image(
                        modifier = Modifier
                            .size(160.dp)
                            .padding(bottom = 8.dp)
                            .clip(RoundedCornerShape(36.dp)),
                        painter = painterResource(id = R.drawable.img_error),
                        contentDescription = "Error image",
                    )
                    Text(
                        modifier = Modifier.padding(horizontal = 16.dp),
                        text = message,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
                TextButton(
                    onClick = onDismiss,
                    modifier = Modifier.align(Alignment.TopEnd)
                        .padding(end = 16.dp, top = 8.dp)
                ) {
                    Text(text = "Dismiss", fontSize = 18.sp)
                }
            }
        }
    }
}