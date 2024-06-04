package com.compscicomputations.presentation.main.dashboard

import android.net.Uri
import android.util.Log
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compscicomputations.core.database.dao.UserDao
import com.compscicomputations.core.database.model.Feature
import com.compscicomputations.core.database.model.UserType
import com.compscicomputations.data.featuresList
import dagger.hilt.android.lifecycle.HiltViewModel
import io.ktor.client.plugins.HttpRequestTimeoutException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val userDao: UserDao
) : ViewModel() {
    private val _userType = MutableStateFlow(UserType.STUDENT)
    private val _displayName = MutableStateFlow<String?>(null)
    private val _email = MutableStateFlow<String?>(null)
    private val _photoUrl = MutableStateFlow<Uri?>(null)
    private val _installedFeatures = MutableStateFlow<Set<Feature>?>(null)
    private val _showProgress = MutableStateFlow(true)
    val userType = _userType.asStateFlow()
    val displayName = _displayName.asStateFlow()
    val email = _email.asStateFlow()
    val photoUrl = _photoUrl.asStateFlow()
    val installedFeatures = _installedFeatures.asStateFlow()
    val showProgress = _showProgress.asStateFlow()

    val snackBarHostState = SnackbarHostState()
    var gotoProfile by mutableStateOf(false)

    private fun updateUserInfo(retry: Int = 2) {
        viewModelScope.launch {
            try {
                val user = userDao.getUser() ?: return@launch
                _userType.value = user.userType
                _displayName.value = user.displayName
                _email.value = user.email
                _photoUrl.value = user.photoUrl
                _showProgress.value = false
            } catch (e: HttpRequestTimeoutException) {
                Log.e("DashboardViewModel", "updateUserInfo:Exception", e)
                if (retry > 0) updateUserInfo(retry-1)
                else _showProgress.value = false
                _showProgress.value = false
            } catch (e: Exception) {
                val error = e.message?.split('=', limit = 2)
                when(error?.get(0)) {
                    "NoSuchUserException" -> {
                        Log.e("DashboardViewModel", "updateUserInfo:NoSuchUserException", e)
                        _showProgress.value = false
                        _displayName.value = "Complete your profile."
                        snackBarHostState.showSnackbar(
                            message = "User information does not exits.",
                            actionLabel = "Goto profile",
                            duration = SnackbarDuration.Long
                        ).also {
                            if (it == SnackbarResult.ActionPerformed) {
                                gotoProfile = true
                            }
                        }
                    }
                    else -> {
                        _showProgress.value = false
                        Log.e("DashboardViewModel", "updateUserInfo:Exception", e)
                    }
                }
            }
        }
    }
    private fun updateInstalledFeatures() {
        val features = featuresList.toMutableSet()
//        val installedModules = splitInstallManager.installedModules
//        features.retainAll { installedModules.contains(it.module) }
        _installedFeatures.value = features.toSet()
    }

    init {
        updateInstalledFeatures()
        updateUserInfo()
        /*splitInstallManager.deferredUninstall(listOf("polish_expressions", "matrix_methods"))
            .addOnSuccessListener {
                Toast.makeText(context, "Done remove polish_expressions", Toast.LENGTH_SHORT).show()
            }*/
    }
}