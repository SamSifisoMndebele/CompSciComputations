package com.compscicomputations

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.compscicomputations.client.auth.data.source.local.UserDataStore.isUserSignedInFlow
import com.compscicomputations.theme.CompSciComputationsTheme
import com.compscicomputations.ui.navigation.Auth
import com.compscicomputations.ui.navigation.Loading
import com.compscicomputations.ui.navigation.Main
import com.compscicomputations.ui.navigation.authNavigation
import com.compscicomputations.ui.navigation.mainNavigation
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            CompSciComputationsTheme(
                dynamicColor = false
            ) {
                val navController = rememberNavController()
                NavHost(
                    modifier = Modifier.fillMaxSize(),
                    navController = navController,
                    startDestination = Loading
                ) {
                    composable<Loading> (
                        enterTransition = { fadeIn(animationSpec = tween(durationMillis = 250)) },
                        exitTransition = { fadeOut() }
                    ) {
                        val context = LocalContext.current
                        val isUserSignedIn by context.isUserSignedInFlow.collectAsState(initial = null)
                        LaunchedEffect(isUserSignedIn) {
                            when(isUserSignedIn) {
                                true -> navController.navigate(route = Main) {
                                    popUpTo<Loading> { inclusive = true }
                                    launchSingleTop = true
                                }
                                false -> navController.navigate(route = Auth) {
                                    popUpTo<Loading> { inclusive = true }
                                    launchSingleTop = true
                                }
                                null -> {}
                            }
                        }
                        Surface(
                            modifier = Modifier.fillMaxSize()
                                .background(MaterialTheme.colorScheme.background)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                Image(
                                    modifier = Modifier.fillMaxWidth(),
                                    painter = painterResource(id = R.drawable.img_logo_name),
                                    contentDescription = "App Logo",
                                    contentScale = ContentScale.FillWidth
                                )
                                CircularProgressIndicator(
                                    strokeWidth = 6.dp,
                                    modifier = Modifier
                                        .size(80.dp)
                                        .padding(bottom = 8.dp),
                                    color = MaterialTheme.colorScheme.secondary,
                                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                                )
                            }
                        }
                    }
                    authNavigation(navController)
                    mainNavigation(navController)
                }
            }
        }
    }
}