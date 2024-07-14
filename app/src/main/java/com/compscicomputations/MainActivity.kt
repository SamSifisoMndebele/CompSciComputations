package com.compscicomputations

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.compscicomputations.core.ktor_client.auth.AuthRepository.Companion.isUserSignedIn
import com.compscicomputations.theme.CompSciComputationsTheme
import com.compscicomputations.ui.navigation.Auth
import com.compscicomputations.ui.navigation.Main
import com.compscicomputations.ui.navigation.authNavigation
import com.compscicomputations.ui.navigation.mainNavigation
import com.google.firebase.remoteconfig.ConfigUpdate
import com.google.firebase.remoteconfig.ConfigUpdateListener
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.FirebaseRemoteConfigException
import com.google.firebase.remoteconfig.remoteConfigSettings
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import androidx.compose.runtime.rememberCoroutineScope
import com.google.firebase.Firebase
import com.google.firebase.remoteconfig.remoteConfig

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 3600
        }
        val remoteConfig = Firebase.remoteConfig
        remoteConfig.setConfigSettingsAsync(configSettings)
        remoteConfig.addOnConfigUpdateListener(object : ConfigUpdateListener {
            override fun onUpdate(configUpdate : ConfigUpdate) {
                Log.d("MainActivity", "Updated keys: " + configUpdate.updatedKeys)
                remoteConfig.fetchAndActivate()
                    .addOnCompleteListener {
                        Log.d("MainActivity", "Activate succeeded: "+it.result)
                    }
            }
            override fun onError(error : FirebaseRemoteConfigException) {
                Log.w("MainActivity", "Config update error with code: " + error.code, error)
            }
        })

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