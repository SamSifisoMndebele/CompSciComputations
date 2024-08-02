package com.compscicomputations.utils.dynamicfeature

import android.content.Context
import android.util.Log
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallRequest
import com.google.android.play.core.splitinstall.SplitInstallSessionState
import com.google.android.play.core.splitinstall.SplitInstallStateUpdatedListener
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class InstallModuleUseCase @Inject constructor(
    @ApplicationContext private val context: Context,
    private val manager: SplitInstallManager
) {
    private var mySessionId = 0
    /** Request install of feature module. */
    operator fun invoke(vararg module: String, onStateUpdated: (SplitInstallSessionState) -> Unit) {
        val requestBuilder = SplitInstallRequest.newBuilder()
        module.forEach { name ->
            if (!manager.installedModules.contains(name)) {
                requestBuilder.addModule(name)
            }
        }
        val request = requestBuilder.build()
        manager.startInstall(request)
            .addOnCompleteListener {
                Log.d("InstallModuleUseCase", "Complete ${request.moduleNames}")
            }
            .addOnSuccessListener {
                Log.d("InstallModuleUseCase", "Success ${request.moduleNames}")
            }
            .addOnFailureListener {
                Log.d("InstallModuleUseCase", "Failure ${request.moduleNames}")
            }
        val listener = SplitInstallStateUpdatedListener { state ->
            if (state.sessionId() == mySessionId) {
                onStateUpdated(state)
            }
            val names = state.moduleNames().joinToString(" - ")
            when (state.status()) {
                SplitInstallSessionStatus.DOWNLOADING -> {
                    //  In order to see this, the application has to be uploaded to the Play Store.
                    Log.d("InstallModuleUseCase", "Downloading $names")
                }
                SplitInstallSessionStatus.REQUIRES_USER_CONFIRMATION -> {
                    /*
                      This may occur when attempting to download a sufficiently large module.

                      In order to see this, the application has to be uploaded to the Play Store.
                      Then features can be requested until the confirmation path is triggered.
                     */
                    context.startIntentSender(state.resolutionIntent()?.intentSender, null, 0, 0, 0)
                }
                SplitInstallSessionStatus.INSTALLED -> {
                    Log.d("InstallModuleUseCase", "SuccessfulLoad $names")
                }

                SplitInstallSessionStatus.INSTALLING -> {
                    Log.d("InstallModuleUseCase", "Installing $names")
                }
                SplitInstallSessionStatus.FAILED -> {
                    Log.d("InstallModuleUseCase", "Error: ${state.errorCode()} for module ${state.moduleNames()}")
                }
                else -> {
                    Log.d("InstallModuleUseCase", "Status: ${state.status()}")
                }
            }
        }

        manager.registerListener(listener)

        manager.startInstall(request)
            .addOnFailureListener { e -> Log.w("InstallModuleUseCase", "Exception::installModule", e) }
            .addOnSuccessListener { sessionId -> mySessionId = sessionId }
    }

//    /** Install all features deferred. */
//    private fun installAllFeaturesDeferred(modules: List<String>) {
//        manager.deferredInstall(modules)
//            .addOnCompleteListener {
//                Log.d("InstallModuleUseCase", "Complete Deferred installation of $modules\"")
//            }
//            .addOnSuccessListener {
//                Log.d("InstallModuleUseCase", "Success Deferred installation of $modules\"")
//            }
//            .addOnFailureListener {
//                Log.d("InstallModuleUseCase", "Failure Deferred installation of $modules\"")
//            }
//    }
//
//    /** Request uninstall of all features. */
//    private fun requestUninstall(modules: List<String> = manager.installedModules.toList()) {
//        Log.d("InstallModuleUseCase", "Requesting uninstall of all modules." +
//                "This will happen at some point in the future.")
//        manager.deferredUninstall(modules)
//            .addOnCompleteListener {
//                Log.d("InstallModuleUseCase", "Complete Uninstalling of $modules\"")
//            }
//            .addOnSuccessListener {
//                Log.d("InstallModuleUseCase", "Success Uninstalling of $modules")
//            }
//            .addOnFailureListener {
//                Log.d("InstallModuleUseCase", "Failure Uninstalling of $modules\"")
//            }
//    }
}