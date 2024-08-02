package com.compscicomputations.ui.utils.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Downloading
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.compscicomputations.R
import com.compscicomputations.theme.comicNeueFamily

@Composable
fun DownloadFeatureButton(
    modifier: Modifier = Modifier,
    featureName: String,
    enabled: Boolean,
    downloading: Boolean,
    installing: Boolean,
    downloadProgress: Float,
    onDownload: () -> Unit,
) {
    OutlinedButton(
        modifier = modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        enabled = enabled,
        colors = if (downloading) ButtonDefaults.buttonColors() else ButtonDefaults.outlinedButtonColors(),
        onClick = onDownload,
        shape = RoundedCornerShape(32.dp),

    ) {
        Column {
            Row(
                Modifier
                    .padding(vertical = 4.dp, horizontal = 8.dp)
                    .fillMaxWidth()
                    .height(36.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = featureName + if (installing) ": Installing..." else "",
                    modifier = Modifier
                        .padding(start = 8.dp, end = 8.dp)
                        .weight(1f),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = comicNeueFamily,
                    maxLines = 1
                )
                Icon(
                    imageVector = if (downloading) Icons.Default.Downloading else Icons.Default.Download,
                    contentDescription = "$featureName download",
                )
            }
            if (installing) {
                LinearProgressIndicator(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .padding(bottom = 3.dp, start = 4.dp, end = 4.dp),
                    strokeCap = StrokeCap.Round
                )
            }
            if (downloading) {
                LinearProgressIndicator(
                    progress = { downloadProgress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp)
                        .padding(bottom = 3.dp, start = 4.dp, end = 4.dp),
                    strokeCap = StrokeCap.Round
                )
            }
        }
    }
}

@Composable
fun OptionButton(
    modifier: Modifier = Modifier,
    iconVector: ImageVector? = null,
    text: String,
    containerColor: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = MaterialTheme.colorScheme.primary,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor.copy(alpha = 0.15f),
            contentColor = contentColor,
        ),
        onClick = onClick,
        shape = RoundedCornerShape(32.dp)
    ) {
        if (iconVector != null) {
            Icon(
                imageVector = iconVector,
                contentDescription = "$text Icon",
                tint = contentColor
            )
        }
        Text(
            text = text,
            modifier = Modifier
                .padding(start = 32.dp, end = 8.dp, top = 12.dp, bottom = 12.dp)
                .weight(1f),
            color = contentColor,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = comicNeueFamily,
            maxLines = 1
        )
    }
}

@Composable
fun OptionButton(
    modifier: Modifier = Modifier,
    assetName: String,
    text: String,
    shape: Shape = RoundedCornerShape(32.dp),
    containerColor: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = MaterialTheme.colorScheme.primary,
    tint: Color? = null,
    onClick: () -> Unit
) {
    OptionButton(
        modifier = modifier,
        url = "file:///android_asset/icons/$assetName",
        text = text,
        shape = shape,
        containerColor = containerColor,
        contentColor = contentColor,
        tint = tint,
        onClick = onClick
    )
}

@Composable
fun OptionButton(
    modifier: Modifier = Modifier,
    url: String? = null,
    text: String,
    enabled: Boolean = true,
    shape: Shape = RoundedCornerShape(32.dp),
    containerColor: Color = MaterialTheme.colorScheme.primary,
    contentColor: Color = MaterialTheme.colorScheme.primary,
    tint: Color? = null,
    onClick: () -> Unit
) {
    Button(
        modifier = modifier.fillMaxWidth(),
        enabled = enabled,
        shape = shape,
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor.copy(alpha = 0.15f),
            contentColor = contentColor,
        ),
        onClick = onClick
    ) {
        if (url != null) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(url)
                    .crossfade(true)
                    .build(),
                placeholder = painterResource(R.drawable.ic_launcher_foreground),
                error = painterResource(R.drawable.ic_launcher_foreground),
                contentDescription = "$text Icon",
                contentScale = ContentScale.FillHeight,
                colorFilter = tint?.let { ColorFilter.tint(it) },
                modifier = Modifier
                    .size(46.dp)
                    .padding(vertical = 8.dp)
                    .clip(RoundedCornerShape(16.dp))
            )
        }
        Text(
            text = text,
            modifier = Modifier
                .padding(start = 32.dp, end = 8.dp, top = 12.dp, bottom = 12.dp)
                .weight(1f),
            color = contentColor,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = comicNeueFamily,
            maxLines = 1
        )
    }
}