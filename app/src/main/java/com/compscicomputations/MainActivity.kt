package com.compscicomputations

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.compscicomputations.core.database.auth.AuthRepository.Companion.isUserSignedIn
import com.compscicomputations.theme.CompSciComputationsTheme
import com.compscicomputations.ui.navigation.Auth
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
                    startDestination = if (isUserSignedIn) Main else Auth
                ) {
                    authNavigation(navController)
                    mainNavigation(navController)
                }
            }
        }
    }
}