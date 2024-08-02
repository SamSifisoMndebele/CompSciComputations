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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.outlined.Feedback
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
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
import kotlinx.coroutines.flow.asFlow

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
            SnackbarHost(hostState = uiState.snackBarHostState)
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
                            padding = PaddingValues(start = 8.dp, end = 8.dp, bottom = 8.dp),
                            iconName = feature.icon,
                            text = feature.title,
                            tint = MaterialTheme.colorScheme.primary,
                        ) {
                            navigateDynamicFeature(feature)
                        }
                    }

                    if (viewModel._dynamicFeatureInstall.isNotEmpty()) {
                        Text(
                            modifier = Modifier.padding(start = 32.dp, end = 32.dp, top = 8.dp),
                            text = "Install features"
                        )
                        viewModel._dynamicFeatureInstall.sortedBy { it.moduleName }.forEach { feature ->
                            DownloadFeatureButton(
                                padding = PaddingValues(start = 8.dp, end = 8.dp, bottom = 8.dp),
                                module = feature.moduleName,
                                currentProgress = feature.currentProgress,
                                downloading = feature.downloading,
                            ) {
                                viewModel.onInstallFeature(feature.module)
                            }
                        }
                    }
                }
            }
            item {
                OptionButton(
                    padding = PaddingValues(vertical = 8.dp),
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
