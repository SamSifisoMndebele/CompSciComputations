package com.compscicomputations.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.compscicomputations.R
import kotlinx.coroutines.delay


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
    val delayMillis: Long = 4000,
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
