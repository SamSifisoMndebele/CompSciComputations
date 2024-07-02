package com.compscicomputations

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import android.window.OnBackInvokedDispatcher
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.compscicomputations.core.ktor_client.remote.dao.AuthDao
import com.compscicomputations.presentation.main.MainHostScreen
import com.compscicomputations.ui.theme.CompSciComputationsTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var authDao: AuthDao
    private var exit = false
    private lateinit var navController: NavHostController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!authDao.isLoggedIn()) {
            startActivity(Intent(this, AuthActivity::class.java))
            finishAffinity()
            return
        }

//        enableEdgeToEdge()
        setContent {
            navController = rememberNavController()
            CompSciComputationsTheme(
                dynamicColor = false
            ) {
                MainHostScreen(this, navController)
            }
        }
        onBackPressedMethod()
    }
    private fun onBackPressedMethod() {
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
    }
}