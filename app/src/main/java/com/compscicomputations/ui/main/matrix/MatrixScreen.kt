package com.compscicomputations.ui.main.matrix

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.GridOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.compscicomputations.ui.main.AppBar
import com.compscicomputations.ui.theme.comicNeueFamily

@Composable
fun MatrixScreen(
    padding: PaddingValues = PaddingValues(start = 8.dp, end = 8.dp, top = 2.dp, bottom = 8.dp),
    navigateUp: () -> Unit,
    navigateCramer: () -> Unit,
    navigateGauss: () -> Unit,
) {
    Column(
        modifier = Modifier.padding(padding),
    ) {
        AppBar(title = "Matrix Methods", navigateUp = navigateUp) {
            //TODO Menu
        }
        LazyColumn(
            modifier = Modifier
                .padding(vertical = 8.dp)
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            item {
                DashboardOption(
                    iconVector = Icons.Outlined.GridOn,
                    text = "Cramer's Rule",
                    onClick = navigateCramer
                )
            }
            item {
                DashboardOption(
                    iconVector = Icons.Outlined.GridOn,
                    text = "Gauss Elimination method",
                    onClick = navigateGauss
                )
            }
            item {
                DashboardOption(
                    iconVector = Icons.Outlined.GridOn,
                    text = "Method 3",
                    onClick = {  }
                )
            }
            item {
                DashboardOption(
                    iconVector = Icons.Outlined.GridOn,
                    text = "Method 4",
                    onClick = {  }
                )
            }
            item {
                DashboardOption(
                    iconVector = Icons.Outlined.GridOn,
                    text = "Method 5",
                    onClick = {  }
                )
            }
        }
    }
}

@Composable
fun DashboardOption(
    padding: PaddingValues = PaddingValues(bottom = 8.dp),
    iconVector: ImageVector? = null,
    painter: Painter? = null,
    text: String = "Option",
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