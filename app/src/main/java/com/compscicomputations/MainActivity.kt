package com.compscicomputations

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.compscicomputations.theme.CompSciComputationsTheme
import com.compscicomputations.ui.auth.onboarding.SplashScreen
import com.compscicomputations.ui.navigation.Splash
import com.compscicomputations.ui.navigation.navigationAuth
import com.compscicomputations.ui.navigation.navigationMain
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : CSCActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val themeState by themeState.collectAsStateWithLifecycle()
            CompSciComputationsTheme(themeState) {
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
                    navigationAuth(this@MainActivity, navController, gotoOnboarding)
                    navigationMain(navController)
                }
            }
        }
    }
}