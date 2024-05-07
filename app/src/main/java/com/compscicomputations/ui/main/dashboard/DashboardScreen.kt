package com.compscicomputations.ui.main.dashboard

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
import androidx.compose.material.icons.outlined.Feedback
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.CrossFade
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.compscicomputations.R
import com.compscicomputations.ui.theme.comicNeueFamily

@OptIn(ExperimentalGlideComposeApi::class, ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    padding: PaddingValues = PaddingValues(start = 8.dp, end = 8.dp, top = 2.dp, bottom = 8.dp),
    navigateProfile: () -> Unit,
    navigateNumStystems: () -> Unit,
    navigatePolish: () -> Unit,
    navigateKarnaugh: () -> Unit,
    navigateMatrix: () -> Unit,
    navigateHelp: () -> Unit,
    navigateFeedback: () -> Unit,
    navigateSettings: () -> Unit,
    uiState: DashboardUiState,
    onEvent: (DashboardUiEvent) -> Unit,
) {
    Column(
        modifier = Modifier.padding(padding),
    ) {
        Card(shape = RoundedCornerShape(24.dp)) {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.secondary,
                ),
                navigationIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.img_logo_name),
                        contentDescription = stringResource(id = R.string.app_name),
                        modifier = Modifier
                            .height(32.dp)
                            .padding(start = 8.dp),
                        tint = Color.White
                    )
                },
                title = {
                    Text(
                        "Dashboard",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 26.sp,
                        fontFamily = comicNeueFamily
                    )
                },
            )
        }
        LazyColumn(
            modifier = Modifier.fillMaxWidth().fillMaxHeight()
        ) {
            item {
                Card(
                    modifier = Modifier.padding(vertical = 8.dp),
                    shape = RoundedCornerShape(24.dp)
                ) {
                    Card(onClick = navigateProfile,
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
                                model = uiState.imageUri,
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
                                Text(text = uiState.names +" "+uiState.lastname, fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold, fontFamily = comicNeueFamily,)
                                Text(text = uiState.email, fontSize = 13.sp)
                            }
                        }
                    }
                    val optionPadding = PaddingValues(start = 8.dp, end = 8.dp, bottom = 8.dp)
                    DashboardOption(
                        padding = optionPadding,
                        painter = painterResource(id = R.drawable.ic_number_64),
                        text = "Number Systems",
                        onClick = navigateNumStystems
                    )
                    DashboardOption(
                        padding = optionPadding,
                        painter = painterResource(id = R.drawable.ic_abc),
                        text = "Polish Expressions",
                        onClick = navigatePolish
                    )
                    DashboardOption(
                        padding = optionPadding,
                        painter = painterResource(id = R.drawable.ic_grid),
                        text = "Karnaugh Maps",
                        onClick = navigateKarnaugh
                    )
                    /*DashboardOption(
                        padding = optionPadding,
                        painter = painterResource(id = R.drawable.ic_software),
                        text = "Lexical & Syntax Analyzer"
                    ) {

                    }
                    DashboardOption(
                        padding = optionPadding,
                        painter = painterResource(id = R.drawable.ic_software),
                        text = "Semantic Analyzer"
                    ) {

                    }
                    DashboardOption(
                        padding = optionPadding,
                        painter = painterResource(id = R.drawable.ic_software),
                        text = "Code Generator"
                    ) {

                    }*/
                    DashboardOption(
                        padding = optionPadding,
                        painter = painterResource(id = R.drawable.ic_matrix),
                        text = "Matrix Methods",
                        onClick = navigateMatrix
                    )

                }
            }
            item { Spacer(modifier = Modifier.height(8.dp)) }
            item {
                DashboardOption(
                    iconVector = Icons.AutoMirrored.Outlined.Help,
                    text = "Help",
                    onClick = navigateHelp
                )
            }
            item {
                DashboardOption(
                    iconVector = Icons.Outlined.Feedback,
                    text = "Feedback",
                    onClick = navigateFeedback
                )
            }
            item {
                DashboardOption(
                    iconVector = Icons.Outlined.Settings,
                    text = "Settings",
                    onClick = navigateSettings
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