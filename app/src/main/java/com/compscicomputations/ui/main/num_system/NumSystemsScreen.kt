package com.compscicomputations.ui.main.num_system

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Help
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.CrossFade
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.compscicomputations.R
import com.compscicomputations.ui.main.AppBar
import com.compscicomputations.ui.main.dashboard.DashboardOption

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun NumSystemsScreen(
    padding: PaddingValues = PaddingValues(start = 8.dp, end = 8.dp, top = 2.dp, bottom = 8.dp),
    navigateUp: () -> Unit,
) {
    Column(
        modifier = Modifier.padding(padding),
    ) {
        AppBar(title = "Number Systems", navigateUp = navigateUp) {
            //TODO Menu
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            item {
                Card(
                    modifier = Modifier.padding(vertical = 8.dp),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Card(
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.25f)
                        )
                    ) {
                        Row (
                            modifier = Modifier.padding(end = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            GlideImage(
                                modifier = Modifier
                                    .size(128.dp)
                                    .padding(10.dp)
                                    .clip(CircleShape),
                                model = "uiState.imageUri",
                                contentScale = ContentScale.FillBounds,
                                loading = placeholder(R.drawable.img_profile),
                                failure = placeholder(R.drawable.img_profile),
                                transition = CrossFade,
                                contentDescription = "Profile"
                            )
                            Column(
                                Modifier
                                    .padding(end = 4.dp, start = 6.dp)
                                    .weight(1f)) {
                            }
                        }
                    }
                    val optionPadding = PaddingValues(start = 8.dp, end = 8.dp, bottom = 8.dp)
                    DashboardOption(
                        padding = optionPadding,
                        painter = painterResource(id = R.drawable.ic_number_64),
                        text = "Number Systems",
                        onClick = {  }
                    )
                    DashboardOption(
                        padding = optionPadding,
                        painter = painterResource(id = R.drawable.ic_abc),
                        text = "Polish Expressions",
                        onClick = {  }
                    )
                    DashboardOption(
                        padding = optionPadding,
                        painter = painterResource(id = R.drawable.ic_grid),
                        text = "Karnaugh Maps",
                        onClick = {  }
                    )

                }
            }
            item { Spacer(modifier = Modifier.height(8.dp)) }
            item {
                DashboardOption(
                    iconVector = Icons.AutoMirrored.Outlined.Help,
                    text = "Help",
                    onClick = {  }
                )
            }
        }
    }
}