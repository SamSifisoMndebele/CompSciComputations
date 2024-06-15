package com.compscicomputations.presentation.main.dashboard

import android.content.Context
import android.util.Log
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compscicomputations.core.database.room.LocalDatabase
import com.compscicomputations.core.database.remote.repo.UserRepo
import com.compscicomputations.core.database.model.Feature
import com.compscicomputations.core.database.model.Usertype
import com.compscicomputations.utils.featuresList
import com.google.firebase.auth.FirebaseAuth
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import io.ktor.client.plugins.HttpRequestTimeoutException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val userRepo: UserRepo,
//    @ApplicationContext
//    private val context: Context,
    private val database: LocalDatabase,
    private val auth: FirebaseAuth
) : ViewModel() {
    private val _usertype = MutableStateFlow(Usertype.STUDENT)
    private val _displayName = MutableStateFlow<String?>(null)
    private val _email = MutableStateFlow<String?>(null)
    private val _photoUrl = MutableStateFlow<String?>(null)
    private val _installedFeatures = MutableStateFlow<Set<Feature>?>(null)
    private val _isLoading = MutableStateFlow(true)
    val userType = _usertype.asStateFlow()
    val displayName = _displayName.asStateFlow()
    val email = _email.asStateFlow()
    val photoUrl = _photoUrl.asStateFlow()
    val installedFeatures = _installedFeatures.asStateFlow()
    val isLoading = _isLoading.asStateFlow()

    val snackBarHostState = SnackbarHostState()
    var gotoProfile by mutableStateOf(false)

    fun setIsLoading(isLoading: Boolean = true) {
        _isLoading.value = isLoading
    }

    private fun updateUserInfo() {
        val localUser = database.userDao().getUser(auth.currentUser!!.uid)
        if (localUser != null) {
            _usertype.value = localUser.getUsertype
            _displayName.value = localUser.metadata.displayName
            _email.value = localUser.email
            _photoUrl.value = localUser.metadata.photoUrl
            _isLoading.value = false
        } else {
            updateUser()
        }
    }
    fun updateUser(retry: Int = 2) {
        viewModelScope.launch {
            try {
                val user = userRepo.getUser() ?: return@launch
                _usertype.value = user.getUsertype
                _displayName.value = user.metadata.displayName
                _email.value = user.email
                _photoUrl.value = user.metadata.photoUrl
                _isLoading.value = false

                database.userDao().upsert(user)

            } catch (e: HttpRequestTimeoutException) {
                Log.e("DashboardViewModel", "updateUserInfo:Exception", e)
                if (retry > 0) updateUser(retry-1)
                else _isLoading.value = false
            } catch (e: Exception) {
                val error = e.message?.split('=', limit = 2)
                when(error?.get(0)) {
                    "NoSuchUserException" -> {
                        Log.e("DashboardViewModel", "updateUserInfo:NoSuchUserException", e)
                        _isLoading.value = false
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
                        _isLoading.value = false
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
        if (auth.currentUser?.uid != null) {
            updateInstalledFeatures()
            updateUserInfo()
        }
        /*splitInstallManager.deferredUninstall(listOf("polish_expressions", "matrix_methods"))
            .addOnSuccessListener {
                Toast.makeText(context, "Done remove polish_expressions", Toast.LENGTH_SHORT).show()
            }*/

//        DB.test()
    }
}