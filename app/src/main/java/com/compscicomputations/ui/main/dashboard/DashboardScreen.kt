package com.compscicomputations.ui.main.dashboard

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.CrossFade
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.compscicomputations.R
import com.compscicomputations.core.database.model.Feature
import com.compscicomputations.data.featuresIcons
import com.compscicomputations.ui.CompSciScaffold
import com.compscicomputations.ui.OptionButton
import com.compscicomputations.ui.theme.comicNeueFamily

@OptIn(ExperimentalGlideComposeApi::class)
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

    CompSciScaffold(
        title = "Dashboard",
        snackBarHost = {
            SnackbarHost(hostState = viewModel.snackBarHostState)
        }
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
                                GlideImage(
                                    modifier = Modifier
                                        .size(128.dp)
                                        .padding(10.dp)
                                        .clip(CircleShape),
                                    model = photoUrl,
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
                                    Text(text = userType?.name?:"", fontSize = 14.sp,
                                        color = MaterialTheme.colorScheme.primary,
                                        fontWeight = FontWeight.Bold, fontFamily = comicNeueFamily,)
                                    Text(text = displayName, fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold, fontFamily = comicNeueFamily,)
                                    Text(text = email, fontSize = 13.sp)
                                }
                            }
                            val showProgress by viewModel.showProgress.collectAsState()
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
