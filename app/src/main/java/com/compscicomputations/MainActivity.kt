package com.compscicomputations

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.compscicomputations.theme.CompSciComputationsTheme
import com.compscicomputations.ui.auth.onboarding.SplashScreen
import com.compscicomputations.ui.navigation.Splash
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
                val gotoOnboarding = remember { mutableStateOf(true) }
                val navController = rememberNavController()
                NavHost(
                    modifier = Modifier.fillMaxSize(),
                    navController = navController,
                    startDestination = Splash
                ) {
                    composable<Splash> (
                        enterTransition = { fadeIn(animationSpec = tween(durationMillis = 250)) },
                        exitTransition = { fadeOut() }
                    ) {
                        SplashScreen(navController)
                    }
                    authNavigation(this@MainActivity, navController, gotoOnboarding)
                    mainNavigation(navController)
                }
            }
        }
    }
}