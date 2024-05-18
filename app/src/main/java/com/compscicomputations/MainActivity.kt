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
import androidx.compose.material3.Surface
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.compscicomputations.ui.main.MainHostScreen
import com.compscicomputations.ui.theme.CompSciComputationsTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private var exit = false
    private lateinit var navController: NavHostController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Firebase.auth.currentUser==null) {
            startActivity(Intent(this, AuthActivity::class.java))
            finishAffinity()
            return
        }
        //val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

        setContent {
            navController = rememberNavController()
            CompSciComputationsTheme(
                dynamicColor = false
            ) {
                Surface {
                    MainHostScreen(this, navController)
                    
//                    Text(text = text)
                    
                }
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