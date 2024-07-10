package com.compscicomputations

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.compscicomputations.theme.CompSciComputationsTheme
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

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private var exit = false
    private lateinit var navController: NavHostController
    @Inject
    lateinit var remoteConfig: FirebaseRemoteConfig

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()


        setContent {
            navController = rememberNavController()
            CompSciComputationsTheme(
                dynamicColor = false
            ) {
                NavHost(
                    modifier = Modifier.fillMaxSize(),
                    navController = navController,
                    startDestination = Main
                ) {
                    authNavigation(this@MainActivity, navController)
                    mainNavigation(this@MainActivity, navController)
                }
            }
        }
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 3600
        }
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
    }

    /*private fun onBackPressedMethod() {
        if (Build.VERSION.SDK_INT >= 33) {
            onBackInvokedDispatcher.registerOnBackInvokedCallback(OnBackInvokedDispatcher.PRIORITY_DEFAULT) {
                if (!navController.navigateUp()){
                    if (exit) {
                        finish()
                    } else {
                        exit = true
                        Toast.makeText(this, "Tab again to exit", Toast.LENGTH_SHORT).show()
                        Handler(Looper.getMainLooper()).postDelayed({
                            exit = false
                        }, 5000)
                    }
                }
            }
        } else {
            onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true){
                override fun handleOnBackPressed() {
                    if (!navController.navigateUp()){
                        if (exit) {
                            finish()
                        } else {
                            exit = true
                            Toast.makeText(this@MainActivity, "Tab again to exit", Toast.LENGTH_SHORT).show()
                            Handler(Looper.getMainLooper()).postDelayed({
                                exit = false
                            }, 5000)
                        }
                    }
                }
            })
        }
    }*/
}