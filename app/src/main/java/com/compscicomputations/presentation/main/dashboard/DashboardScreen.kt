package com.compscicomputations.presentation.main.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Help
import androidx.compose.material.icons.outlined.Feedback
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.compscicomputations.core.ktor_client.model.Feature
import com.compscicomputations.presentation.CompSciScaffold
import com.compscicomputations.presentation.OptionButton
import com.compscicomputations.ui.theme.comicNeueFamily
import com.compscicomputations.utils.featuresIcons

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = hiltViewModel(),
    navigateProfile: () -> Unit,
    navigateHelp: () -> Unit,
    navigateFeedback: () -> Unit,
    navigateSettings: () -> Unit,
    navigateDynamicFeature: (feature: Feature) -> Unit
) {
//    val firebaseUser by viewModel.firebaseUser.collectAsState()
    val userType by viewModel.userType.collectAsState()
    val displayName by viewModel.displayName.collectAsState()
    val email by viewModel.email.collectAsState()
    val photoUrl by viewModel.photoUrl.collectAsState()
    LaunchedEffect(viewModel.gotoProfile) {
        if (viewModel.gotoProfile) {
            viewModel.gotoProfile = false
            navigateProfile()
        }
    }
    val coroutineScope = rememberCoroutineScope()
    val isLoading by viewModel.isLoading.collectAsState()
    CompSciScaffold(
        title = "Dashboard",
        snackBarHost = {
            SnackbarHost(hostState = viewModel.snackBarHostState)
        },
        menuActions = {
            TextButton(onClick = navigateProfile) {
                Text(text = "Refresh")
            }
        },
        isRefreshing = isLoading,
        onRefresh = {
            viewModel.setIsLoading()
            viewModel.updateUser()
        },
    ) { contentPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            contentPadding = contentPadding
        ) {
            item {
                Card(shape = RoundedCornerShape(24.dp)) {
                    Card(onClick = navigateProfile,
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.25f)
                        )
                    ) {
                        Box(Modifier.fillMaxWidth()){
                            Row (
                                modifier = Modifier.padding(end = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                AsyncImage(
                                    modifier = Modifier
                                        .size(128.dp)
                                        .padding(10.dp)
                                        .clip(CircleShape),
                                    model = photoUrl,
                                    contentScale = ContentScale.FillBounds,
                                    contentDescription = "Profile",)
                                Column(
                                    Modifier
                                        .padding(end = 4.dp, start = 6.dp)
                                        .weight(1f)) {
                                    Text(text = userType.sqlName, fontSize = 14.sp,
                                        color = MaterialTheme.colorScheme.primary,
                                        fontWeight = FontWeight.Bold, fontFamily = comicNeueFamily,)
                                    Text(text = displayName ?: "", fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold, fontFamily = comicNeueFamily,)
                                    Text(text = email ?: "", fontSize = 13.sp)
                                }
                            }
                            val showProgress by viewModel.isLoading.collectAsState()
                            if (showProgress) {
                                Row (
                                    modifier = Modifier
                                        .padding(end = 8.dp)
                                        .matchParentSize(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    CircularProgressIndicator(
                                        strokeWidth = 10.dp,
                                        modifier = Modifier
                                            .size(128.dp)
                                            .padding(10.dp),
                                        color = MaterialTheme.colorScheme.secondary,
                                        trackColor = MaterialTheme.colorScheme.surfaceVariant,
                                    )
                                    Column(
                                        Modifier
                                            .padding(end = 4.dp, start = 6.dp)
                                            .weight(1f)) {
                                        Box(modifier = Modifier
                                            .height(24.dp)
                                            .width(128.dp)
                                            .background(MaterialTheme.colorScheme.primary))
                                        Box(modifier = Modifier
                                            .height(23.dp)
                                            .fillMaxWidth()
                                            .padding(top = 8.dp, end = 16.dp)
                                            .background(MaterialTheme.colorScheme.onPrimary))
                                        Box(modifier = Modifier
                                            .height(21.dp)
                                            .fillMaxWidth()
                                            .padding(top = 8.dp)
                                            .background(MaterialTheme.colorScheme.onPrimary))
                                    }
                                }
                            }
                        }

                    }


                    val optionPadding = PaddingValues(start = 8.dp, end = 8.dp, bottom = 8.dp)

                    val installedFeatures by viewModel.installedFeatures.collectAsState()
                    if (installedFeatures != null) {
                        for (feature in installedFeatures!!) {
                            OptionButton(
                                padding = optionPadding,
                                painter = painterResource(id = featuresIcons[feature.id]),
                                text = feature.title,
                                onClick = {
                                    navigateDynamicFeature(feature)
                                }
                            )
                        }
                    } else {
                        Text(text = "No features installed")
                        TextButton(onClick = { /*TODO*/ }) {
                            Text(text = "Go to Install")
                        }
                    }
                }
            }
            item {
                OptionButton(
                    padding = PaddingValues(vertical = 8.dp),
                    iconVector = Icons.AutoMirrored.Outlined.Help,
                    text = "Help",
                    onClick = navigateHelp
                )
            }
            item {
                OptionButton(
                    iconVector = Icons.Outlined.Feedback,
                    text = "Feedback",
                    onClick = navigateFeedback
                )
            }
            item {
                OptionButton(
                    iconVector = Icons.Outlined.Settings,
                    text = "Settings",
                    onClick = navigateSettings
                )
            }
        }
    }
}
