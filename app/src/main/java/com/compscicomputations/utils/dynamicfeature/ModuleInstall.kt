package com.compscicomputations.utils.dynamicfeature

import android.content.Context
import android.util.Log
import com.compscicomputations.client.publik.data.model.DynamicFeature
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallRequest
import com.google.android.play.core.splitinstall.SplitInstallStateUpdatedListener
import com.google.android.play.core.splitinstall.model.SplitInstallErrorCode
import com.google.android.play.core.splitinstall.model.SplitInstallErrorCode.*
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class ModuleInstall @Inject constructor(
    @ApplicationContext private val context: Context,
    private val manager: SplitInstallManager
) {
    companion object {
        const val INSTALLING = 2f
    }
    private var listener: SplitInstallStateUpdatedListener? = null
    /** Request install of feature module. */
    suspend operator fun invoke(
        vararg feature: DynamicFeature,
        onProgress: (progress: Float) -> Unit,
        onFailure: (message: String) -> Unit,
        onInstalled: () -> Unit,
    ) {
        onProgress(0f)
        val requestBuilder = SplitInstallRequest.newBuilder()
        feature.forEach {
            if (!manager.installedModules.contains(it.module)) {
                requestBuilder.addModule(it.module)
            }
        }
        val request = requestBuilder.build()
        val features = feature.joinToString()
        listener = SplitInstallStateUpdatedListener { state ->
            val names = state.moduleNames().joinToString(" - ")
            when (state.status()) {
                SplitInstallSessionStatus.DOWNLOADING -> {
                    val downloadProgress = try { state.bytesDownloaded().toFloat() / state.totalBytesToDownload() } catch (_: Exception) { 1f }
                    if (downloadProgress in 0f..1f) onProgress(downloadProgress)
                    else onProgress(1f)
                    Log.d("ModuleInstall", "Downloading $names: ${state.bytesDownloaded()}/${state.totalBytesToDownload()}")
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
                    onInstalled()
                    Log.d("ModuleInstall", "SuccessfulLoad $names")
                    listener?.let { manager.unregisterListener(it) }
                }
                SplitInstallSessionStatus.INSTALLING -> {
                    onProgress(INSTALLING)
                    Log.d("ModuleInstall", "Installing $names")
                }
                SplitInstallSessionStatus.FAILED -> {
                    val errorMessage = when(state.errorCode()) {
                        INSUFFICIENT_STORAGE -> "$features install failed due to insufficient storage."
                        NETWORK_ERROR -> "$features install failed due to network connection."
                        MODULE_UNAVAILABLE -> "$features is not available to this device, or app version."
                        PLAY_STORE_NOT_FOUND -> "The Play Store app is either not installed or not the official version."
                        INTERNAL_ERROR -> "Unknown error processing split install."
                        else -> "Failed to install $features"
                    }
                    onFailure(errorMessage)
                    Log.w("ModuleInstall", "Error: ${state.errorCode()} for module ${state.moduleNames()}, $errorMessage")
                    listener?.let { manager.unregisterListener(it) }
                }
                else -> {
                    Log.d("ModuleInstall", "Status: ${state.status()}")
                }
            }
        }
        listener?.let { manager.registerListener(it) }
        manager.startInstall(request).await()
    }

//    /** Install all features deferred. */
//    private fun installAllFeaturesDeferred(modules: List<String>) {
//        manager.deferredInstall(modules)
//            .addOnCompleteListener {
//                Log.d("ModuleInstall", "Complete Deferred installation of $modules\"")
//            }
//            .addOnSuccessListener {
//                Log.d("ModuleInstall", "Success Deferred installation of $modules\"")
//            }
//            .addOnFailureListener {
//                Log.d("ModuleInstall", "Failure Deferred installation of $modules\"")
//            }
//    }
//
//    /** Request uninstall of all features. */
//    private fun requestUninstall(modules: List<String> = manager.installedModules.toList()) {
//        Log.d("ModuleInstall", "Requesting uninstall of all modules." +
//                "This will happen at some point in the future.")
//        manager.deferredUninstall(modules)
//            .addOnCompleteListener {
//                Log.d("ModuleInstall", "Complete Uninstalling of $modules\"")
//            }
//            .addOnSuccessListener {
//                Log.d("ModuleInstall", "Success Uninstalling of $modules")
//            }
//            .addOnFailureListener {
//                Log.d("ModuleInstall", "Failure Uninstalling of $modules\"")
//            }
//    }
}