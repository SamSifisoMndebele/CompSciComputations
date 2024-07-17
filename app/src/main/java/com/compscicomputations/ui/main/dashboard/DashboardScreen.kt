package com.compscicomputations.ui.main.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.automirrored.outlined.Help
import androidx.compose.material.icons.outlined.Feedback
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.compscicomputations.core.client.auth.models.Usertype
import com.compscicomputations.core.client.auth.models.DynamicFeature
import com.compscicomputations.theme.comicNeueFamily
import com.compscicomputations.ui.utils.CompSciScaffold
import com.compscicomputations.ui.utils.OptionButton
import com.compscicomputations.ui.utils.isLoading
import com.compscicomputations.ui.utils.shimmerBrush

@Composable
fun DashboardScreen(
    viewModel: DashboardViewModel,
    uiState: DashboardUiState,
    navigateProfile: () -> Unit,
    navigateHelp: () -> Unit,
    navigateFeedback: () -> Unit,
    navigateSettings: () -> Unit,
    navigateDynamicFeature: (feature: DynamicFeature) -> Unit
) {

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
        onRefresh = { viewModel.onRefresh() },
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
                        Box(Modifier.fillMaxWidth()){
                            Row (
                                modifier = Modifier.padding(end = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
//                                val showShimmer = rememberShimmerBrushState()
                                AsyncImage(
                                    modifier = Modifier
                                        .size(128.dp)
                                        .padding(8.dp)
                                        .background(
                                            shimmerBrush(showShimmer = uiState.progressState.isLoading),
                                            CircleShape
                                        )
                                        .clip(CircleShape),
                                    model = uiState.photoUrl,
                                    contentScale = ContentScale.FillBounds,
//                                    onSuccess = { showShimmer.value = false },
                                    contentDescription = "Profile",
                                )
                                Column(
                                    Modifier.weight(1f)
                                ) {
                                    Text(
                                        modifier = Modifier
                                            .widthIn(min = 80.dp)
                                            .background(
                                                shimmerBrush(showShimmer = uiState.progressState.isLoading),
                                                CircleShape
                                            ),
                                        text = when {
                                            !uiState.isCompleteProfile -> "Complete Profile"
                                            uiState.usertype == Usertype.OTHER -> ""
                                            else -> uiState.usertype.name
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
                                            .background(
                                                shimmerBrush(showShimmer = uiState.progressState.isLoading),
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
                                            .background(
                                                shimmerBrush(showShimmer = uiState.progressState.isLoading),
                                                CircleShape
                                            ),
                                        text = uiState.email,
                                        fontSize = 14.sp,
                                    )
                                }
                            }
                        }
                    }

                    if (uiState.installedFeatures.isNullOrEmpty()) {
                        Text(text = "No features installed")
                        TextButton(onClick = { /*TODO*/ }) {
                            Text(text = "Go to Install")
                        }
                    } else {
                        for (feature in uiState.installedFeatures) {
                            OptionButton(
                                padding = PaddingValues(start = 8.dp, end = 8.dp, bottom = 8.dp),
                                iconUrl = feature.iconUrl?.ifBlank { null },
                                text = feature.title,
                                onClick = {
                                    navigateDynamicFeature(feature)
                                }
                            )
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
