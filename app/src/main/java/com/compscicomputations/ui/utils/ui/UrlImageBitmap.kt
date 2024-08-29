package com.compscicomputations.ui.utils.ui

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.ImageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import com.compscicomputations.R
import com.compscicomputations.theme.comicNeueFamily
import com.compscicomputations.utils.rememberConnectivityState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch

/**
 * A composable function that gives an image bitmap from a URL, and displays an error message
 * with error image if the image loading fails.
 */
@OptIn(ExperimentalCoroutinesApi::class)
@Composable
fun UrlAsImageBitmap(
    modifier: Modifier = Modifier,
    imageUrl: String,
    content: @Composable (imageBitmap: ImageBitmap, isLoading: Boolean) -> Unit
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(true) }
    var imageBitmap by remember {
        mutableStateOf(BitmapFactory.decodeResource(context.resources, R.drawable.img_logo_name).asImageBitmap())
    }
    var error by remember { mutableStateOf<Throwable?>(null) }
    val connectivityState by rememberConnectivityState()
    LaunchedEffect(imageUrl, connectivityState) {var temp: ImageBitmap? = null
        if (isLoading) {
            val loadBitmap = scope.launch(Dispatchers.IO) {
                val loader = ImageLoader(context)
                val request = ImageRequest.Builder(context)
                    .data(imageUrl)
                    .allowHardware(false)
                    .build()
                val result = loader.execute(request)
                if (result is SuccessResult) {
                    temp = (result.drawable as BitmapDrawable).bitmap?.asImageBitmap()
                }
            }
            loadBitmap.invokeOnCompletion { throwable ->
                error = throwable
                if (temp != null) {
                    imageBitmap = temp!!
                    isLoading = false
                } else if (throwable != null) {
                    imageBitmap = BitmapFactory.decodeResource(context.resources, R.drawable.img_error).asImageBitmap()
                    isLoading = false
                }
            }
        }
    }
    Box(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .background(
                    MaterialTheme.colorScheme.errorContainer.copy(alpha = .75f),
                    CircleShape
                )
        ) {
            if (error != null) {
                Log.e("UrlAsImageBitmap", "Error loading image", error)
                Text(
                    text = error!!.localizedMessage ?: "Error loading image",
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    fontSize = 12.sp,
                    fontFamily = comicNeueFamily,
                    modifier = Modifier.padding(8.dp)
                )
            }
            Log.d("ConnectivityState", "connectivityState: $connectivityState")
            if (connectivityState.isUnavailable && isLoading) {
                Text(
                    text = "Connectivity unavailable, please check your internet connection.",
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                    fontSize = 12.sp,
                    fontFamily = comicNeueFamily,
                    modifier = Modifier.padding(8.dp)
                )
            }
        }
        content(imageBitmap, isLoading)
    }
}


fun urlToImageBitmap(
    imageUrl: String,
    scope: CoroutineScope,
    context: Context,
    onError: (error: Throwable) -> Unit,
    onSuccess: (imageBitmap: ImageBitmap) -> Unit,
) {
    var imageBitmap: ImageBitmap? = null
    val loadBitmap = scope.launch(Dispatchers.IO) {
        val loader = ImageLoader(context)
        val request = ImageRequest.Builder(context)
            .data(imageUrl)
            .allowHardware(false)
            .build()
        val result = loader.execute(request)
        if (result is SuccessResult) {
            imageBitmap = (result.drawable as BitmapDrawable).bitmap?.asImageBitmap()
        }
    }
    loadBitmap.invokeOnCompletion { throwable ->
        imageBitmap?.let {
            onSuccess(it)
        } ?: throwable?.let {
            onError(it)
        } ?: onError(Throwable("Undefined Error"))
    }
}