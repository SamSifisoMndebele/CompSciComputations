package com.compscicomputations

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
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
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallManagerFactory
import com.google.android.play.core.splitinstall.SplitInstallRequest
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private var exit = false
    private lateinit var navController: NavHostController
    private lateinit var splitInstallManager: SplitInstallManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Firebase.auth.currentUser==null) {
            startActivity(Intent(this, AuthActivity::class.java))
            finishAffinity()
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

        installKarnaughMapsModule()


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


    private fun installKarnaughMapsModule() {
        splitInstallManager = SplitInstallManagerFactory.create(applicationContext)
        val request = SplitInstallRequest.newBuilder()
            .addModule("karnaughmaps")
            .build()

        splitInstallManager.startInstall(request)
            .addOnSuccessListener {
                Log.d("FEATURE", it.toString())
                if (splitInstallManager.installedModules.contains("karnaughmaps")) {
                    val intent = Intent()
                    intent.setClassName(BuildConfig.APPLICATION_ID, "com.ssmnd.karnaughmaps.KarnaughActivity")
                    intent.putExtra("ExtraInt", 3)
                    startActivity(intent)
                } else {
                    Log.e("FEATURE", "Karnaugh Maps is not installed")
                }
            }
            .addOnFailureListener {
                Log.e("FEATURE", it.message, it)
            }
    }
}