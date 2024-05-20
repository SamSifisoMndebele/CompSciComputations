package com.compscicomputations.ui.main

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBackIos
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.compscicomputations.ui.theme.comicNeueFamily

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    modifier: Modifier = Modifier,
    title: String,
    navigateUp: () -> Unit,
    actions: @Composable (RowScope.() -> Unit) = {}
) {
    Card(modifier, shape = RoundedCornerShape(24.dp)) {
        CenterAlignedTopAppBar(
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer,
                titleContentColor = MaterialTheme.colorScheme.secondary,
            ),
            navigationIcon = {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBackIos,
                        contentDescription = "Back Button",
                        tint = Color.White,
                        modifier = Modifier
                    )
                }
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
            actions = actions
        )
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