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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.bumptech.glide.integration.compose.CrossFade
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.compscicomputations.BuildConfig
import com.compscicomputations.R
import com.compscicomputations.data.featuresIcons
import com.compscicomputations.data.featuresList
import com.compscicomputations.ui.main.OptionButton
import com.compscicomputations.ui.theme.comicNeueFamily

@OptIn(ExperimentalGlideComposeApi::class, ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    padding: PaddingValues = PaddingValues(start = 8.dp, end = 8.dp, top = 2.dp, bottom = 8.dp),
    viewModel: DashboardViewModel = hiltViewModel(),
    navigateAuth: () -> Unit,
    navigateProfile: () -> Unit,
    navigateHelp: () -> Unit,
    navigateFeedback: () -> Unit,
    navigateSettings: () -> Unit,
    navigateDynamicFeature: (packageName: String, className: String, composeMethodName: String?) -> Unit
) {
//    val firebaseUser by viewModel.firebaseUser.collectAsState()
    val userType by viewModel.userType.collectAsState()
    val displayName by viewModel.displayName.collectAsState()
    val email by viewModel.email.collectAsState()
    val photoUrl by viewModel.photoUrl.collectAsState()

    val userSignedOut by viewModel.userSignedOut.collectAsState()
    if (userSignedOut) {
        navigateAuth()
        return
    }

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
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
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
                                Text(text = userType.name, fontSize = 18.sp,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Bold, fontFamily = comicNeueFamily,)
                                Text(text = displayName, fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold, fontFamily = comicNeueFamily,)
                                Text(text = email, fontSize = 13.sp)
                            }
                        }
                    }
//                    Text(text = viewModel.splitInstallManager.installedModules.joinToString("\n"))
                    val optionPadding = PaddingValues(start = 8.dp, end = 8.dp, bottom = 8.dp)
                    val installedFeatures = featuresList.toMutableSet()
                    val installedModules = viewModel.splitInstallManager.installedModules
                    installedFeatures.retainAll { installedModules.contains(it.module) }
                    installedFeatures.forEach {
                        OptionButton(
                            padding = optionPadding,
                            painter = painterResource(id = featuresIcons[it.id]),
                            text = it.title,
                            onClick = {
                                val packageName = BuildConfig.APPLICATION_ID
                                val className = "$packageName.${it.module}.${it.className}"
                                navigateDynamicFeature(packageName, className, it.methodName)
                            }
                        )
                    }

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
                }
            }
            item { Spacer(modifier = Modifier.height(8.dp)) }
            item {
                OptionButton(
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
