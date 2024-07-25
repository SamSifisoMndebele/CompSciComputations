package com.compscicomputations.ui.utils.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.compscicomputations.theme.comicNeueFamily


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
    tint: Color? = null,
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
                    contentScale = ContentScale.FillHeight,
                    colorFilter = tint?.let { ColorFilter.tint(color = it) }
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