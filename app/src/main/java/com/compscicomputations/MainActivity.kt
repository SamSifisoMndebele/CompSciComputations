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
import com.google.android.play.core.splitinstall.SplitInstallRequest
import com.google.android.play.core.splitinstall.SplitInstallStateUpdatedListener
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    companion object {
        private const val TAG = "DynamicFeatures"
        private const val KARNAUGH_ACTIVITY_CLASS = "${BuildConfig.APPLICATION_ID}.karnaughmaps.KarnaughActivity"
    }
    /** Listener used to handle changes in state for install requests. */
    private val listener = SplitInstallStateUpdatedListener { state ->
        val multiInstall = state.moduleNames().size > 1
        val names = state.moduleNames().joinToString(" - ")
        when (state.status()) {
            SplitInstallSessionStatus.DOWNLOADING -> {
                //  In order to see this, the application has to be uploaded to the Play Store.
//                displayLoadingState(state, "Downloading $names")
                Log.d(TAG, "Downloading $names")
            }
            SplitInstallSessionStatus.REQUIRES_USER_CONFIRMATION -> {
                /*
                  This may occur when attempting to download a sufficiently large module.

                  In order to see this, the application has to be uploaded to the Play Store.
                  Then features can be requested until the confirmation path is triggered.
                 */
                startIntentSender(state.resolutionIntent()?.intentSender, null, 0, 0, 0)
            }
            SplitInstallSessionStatus.INSTALLED -> {
                onSuccessfulLoad(names, launch = !multiInstall)
            }

            SplitInstallSessionStatus.INSTALLING -> {
                Log.d(TAG, "Installing $names")
//                displayLoadingState(state, "Installing $names")
            }
            SplitInstallSessionStatus.FAILED -> {
                toastAndLog("Error: ${state.errorCode()} for module ${state.moduleNames()}")
            }
        }
    }


    @Inject
    lateinit var auth: FirebaseAuth
    @Inject
    lateinit var splitInstallManager: SplitInstallManager

    private var exit = false
    private lateinit var navController: NavHostController


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (auth.currentUser==null) {
            startActivity(Intent(this, AuthActivity::class.java))
            finishAffinity()
            return
        }

        setContent {
            navController = rememberNavController()
            CompSciComputationsTheme(
                dynamicColor = false
            ) {
                Surface {
                    MainHostScreen(this, navController)
                    
//                    Text(text = splitInstallManager.installedModules.joinToString("\n"))
                    
                }
            }
        }

        onBackPressedMethod()
    }

    override fun onResume() {
        splitInstallManager.registerListener(listener)
        super.onResume()
    }

    override fun onPause() {
        splitInstallManager.unregisterListener(listener)
        super.onPause()
    }
    /**
     * Load a feature by module name.
     * @param name The name of the feature module to load.
     */
    private fun loadAndLaunchModule(name: String) {
//        updateProgressMessage("Loading module $name")
        Log.d(TAG, "Loading module $name")
        // Skip loading if the module already is installed. Perform success action directly.
        if (splitInstallManager.installedModules.contains(name)) {
//            updateProgressMessage("Already installed")
            Log.d(TAG, "Already installed")
            onSuccessfulLoad(name, launch = true)
            return
        }

        // Create request to install a feature module by name.
        val request = SplitInstallRequest.newBuilder()
            .addModule(name)
            .build()

        // Load and install the requested feature module.
        splitInstallManager.startInstall(request)
        Log.d(TAG, "Starting install for $name")
//        updateProgressMessage("Starting install for $name")
    }

    /**
     * Define what to do once a feature module is loaded successfully.
     * @param moduleName The name of the successfully loaded module.
     * @param launch `true` if the feature module should be launched, else `false`.
     */
    private fun onSuccessfulLoad(moduleName: String, launch: Boolean) {
        if (launch) {
            when (moduleName) {
                "karnaugh_maps" -> launchActivity(KARNAUGH_ACTIVITY_CLASS)
//                moduleAssets -> displayAssets()
            }
        }

//        displayButtons()
    }


    /** Launch an activity by its class name. */
    private fun launchActivity(className: String) {
        Intent().setClassName(packageName, className)
            .also {
                startActivity(it)
            }
    }

    private fun MainActivity.toastAndLog(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show()
        Log.d(TAG, text)
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