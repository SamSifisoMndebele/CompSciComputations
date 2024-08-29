package com.compscicomputations.ui.main.dashboard

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Feedback
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.compscicomputations.R
import com.compscicomputations.client.publik.data.model.DynamicFeature
import com.compscicomputations.theme.comicNeueFamily
import com.compscicomputations.ui.utils.isLoading
import com.compscicomputations.ui.utils.ui.CompSciScaffold
import com.compscicomputations.ui.utils.ui.DownloadFeatureButton
import com.compscicomputations.ui.utils.ui.OptionButton
import com.compscicomputations.ui.utils.ui.shimmerBackground

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel = hiltViewModel(),
    navigateProfile: () -> Unit,
    navigateFeedback: () -> Unit,
    navigateSettings: () -> Unit,
    navigateDynamicFeature: (feature: DynamicFeature) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    CompSciScaffold(
        title = "Dashboard",
        snackBarHost = {
            SnackbarHost(hostState = viewModel.snackBarHostState)
        },
        menuActions = {
//            TextButton(onClick = navigateProfile) {
//                Text(text = "Refresh")
//            }
        },
        isRefreshing = uiState.progressState.isLoading,
//        onRefresh = { viewModel.onRefresh() },
    ) { contentPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            contentPadding = contentPadding
        ) {
            item {
                Card(shape = RoundedCornerShape(24.dp)) {
                    Card(
                        onClick = navigateProfile,
                        shape = RoundedCornerShape(20.dp),
                        modifier = Modifier
                            .padding(8.dp)
                            .fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.25f)
                        )
                    ) {
                        Row (
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            AsyncImage(
                                model = ImageRequest.Builder(LocalContext.current)
                                    .data(uiState.imageBitmap)
                                    .crossfade(true)
                                    .build(),
                                placeholder = painterResource(R.drawable.img_profile),
                                error = painterResource(R.drawable.img_profile),
                                contentDescription = "Profile",
                                contentScale = ContentScale.FillBounds,
                                modifier = Modifier
                                    .shimmerBackground(showShimmer = uiState.progressState.isLoading)
                                    .size(128.dp)
                                    .padding(8.dp)
                                    .clip(CircleShape),
                                onSuccess = {},
                                onError = {},
                                onLoading = {}
                            )
                            Column(
                                Modifier.weight(1f)
                            ) {
                                Text(
                                    modifier = Modifier
                                        .shimmerBackground(showShimmer = uiState.progressState.isLoading)
                                        .widthIn(min = 80.dp),
                                    text = when {
                                        uiState.isAdmin && uiState.isStudent -> "ADMIN | STUDENT"
                                        uiState.isAdmin -> "ADMIN"
                                        uiState.isStudent -> "STUDENT"
                                        else -> ""
                                    },
                                    fontSize = 18.sp,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = comicNeueFamily
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    modifier = Modifier
                                        .widthIn(min = 128.dp)
                                        .shimmerBackground(
                                            uiState.progressState.isLoading,
                                            CircleShape
                                        ),
                                    text = uiState.displayName,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = comicNeueFamily
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    modifier = Modifier
                                        .widthIn(min = 180.dp)
                                        .shimmerBackground(
                                            uiState.progressState.isLoading,
                                            CircleShape
                                        ),
                                    text = uiState.email,
                                    fontSize = 14.sp,
                                )
                            }
                        }
                    }

                    uiState.installedFeatures?.forEach { feature ->
                        OptionButton(
                            modifier = Modifier.padding(start = 8.dp, end = 8.dp, bottom = 8.dp),
                            assetName = feature.icon,
                            text = feature.name,
                            tint = MaterialTheme.colorScheme.primary,
                        ) {
                            navigateDynamicFeature(feature)
                        }
                    }

                    var addModules by remember { mutableStateOf(false) }
                    if (uiState.installedFeatures.isNullOrEmpty()) addModules = true
                    if (!uiState.notInstalledFeatures.isNullOrEmpty()) {
                        if (addModules || uiState.downloadingModule != null) {
                            Spacer(modifier = Modifier.height(8.dp))
                            HorizontalDivider()
                            Spacer(modifier = Modifier.height(4.dp))
                            uiState.notInstalledFeatures?.forEach { feature ->
                                DownloadFeatureButton(
                                    modifier = Modifier.padding(start = 8.dp, end = 8.dp, bottom = 8.dp),
                                    featureName = feature.name,
                                    enabled = uiState.downloadingModule.isNullOrBlank(),
                                    downloading = uiState.downloadingModule == feature.module,
                                    progress = uiState.downloadProgress,
                                ) {
                                    viewModel.onInstallFeature(feature)
                                }
                            }
                        } else {
                            TextButton(
                                onClick = { addModules = !addModules},
                                modifier = Modifier
                                    .padding(8.dp)
                                    .align(Alignment.End)
                            ) {
                                Icon(imageVector = Icons.Default.Add, contentDescription = "Add feature")
                                Text(text = "Add Features")
                            }
                        }
                    }
                }
            }
            item {
                OptionButton(
                    modifier = Modifier.padding(vertical = 8.dp),
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
