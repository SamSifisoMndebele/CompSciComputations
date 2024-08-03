package com.compscicomputations.ui.main.profile

import android.net.Uri
import android.util.Log
import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compscicomputations.client.auth.data.model.User
import com.compscicomputations.client.auth.data.source.AuthRepository
import com.compscicomputations.theme.emailRegex
import com.compscicomputations.theme.namesRegex
import com.compscicomputations.theme.strongPasswordRegex
import com.compscicomputations.ui.utils.ProgressState
import com.compscicomputations.utils.notMatches
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.singleOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState = _uiState.asStateFlow()
    val snackBarHostState: SnackbarHostState = SnackbarHostState()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            authRepository.currentUserFlow
                .flowOn(Dispatchers.IO)
                .catch { e ->
                    Log.w("DashboardViewModel", e)
                    _uiState.value = _uiState.value.copy(progressState = ProgressState.Error(e.localizedMessage))
                }
                .firstOrNull()
                ?.let { user ->
                    updateProfile(user)
                } ?: let {
                _uiState.value = _uiState.value.copy(
                    isSignedIn = false,
                    progressState = ProgressState.Idle
                )
            }
        }
    }

    private fun updateProfile(user: User) {
        _uiState.value = _uiState.value.copy(
            id = user.id,
            email = user.email,
            displayName = user.displayName,
            imageBitmap = user.imageBitmap,
            phone = user.phone,
            isAdmin = user.isAdmin,
            isStudent = user.isStudent,
            isEmailVerified = user.isEmailVerified,
            isSignedIn = true,
            progressState = ProgressState.Idle,
        )
    }

//    fun onRefresh() {
//        _uiState.value = _uiState.value.copy(progressState = ProgressState.Loading())
//        viewModelScope.launch(Dispatchers.IO) {
//            authRepository.refreshUserFlow
//                .flowOn(Dispatchers.IO)
//                .catch { e ->
//                    Log.w("DashboardViewModel", e)
//                    _uiState.value = _uiState.value.copy(progressState = ProgressState.Error(e.localizedMessage))
//                }
//                .firstOrNull()
//                ?.let { user ->
//                    updateProfile(user)
//                }
//                ?: let {
//                    _uiState.value = _uiState.value.copy(
//                        isSignedIn = false,
//                        progressState = ProgressState.Idle
//                    )
//                }
//        }
//    }

    fun logout() {
        viewModelScope.launch {
            try {
                authRepository.logout()
                _uiState.value = _uiState.value.copy(isSignedIn = false)
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(progressState = ProgressState.Error(e.localizedMessage))
            }
        }
    }

    fun setPhotoUri(photoUri: Uri?) {
        _uiState.value = _uiState.value.copy(imageUri = photoUri)
    }
    fun onDisplayNameChange(displayName: String) {
        _uiState.value = _uiState.value.copy(displayName = displayName, displayNameError = null)
    }
    fun onEmailChange(email: String) {
        _uiState.value = _uiState.value.copy(email = email, emailError = null)
    }
    fun onPhoneChange(phone: String) {
        _uiState.value = _uiState.value.copy(phone = phone.takeIf { it.isNotBlank() }, phoneError = null)
    }
    fun onUniversityChange(university: String) {
        _uiState.value = _uiState.value.copy(university = university, universityError = null)
    }
    fun onSchoolChange(school: String) {
        _uiState.value = _uiState.value.copy(school = school, schoolError = null)
    }
    fun onCourseChange(course: String) {
        _uiState.value = _uiState.value.copy(course = course, courseError = null)
    }
    fun onAdminPinChange(adminPin: String) {
        _uiState.value = _uiState.value.copy(adminPin = adminPin.takeIf { it.isNotBlank() }, adminPinError = null)
    }

    fun setProgressState(progressState: ProgressState) {
        _uiState.value = _uiState.value.copy(progressState = progressState)
    }
    fun setIsStudent(isStudent: Boolean) {
        _uiState.value = _uiState.value.copy(isStudent = isStudent)
    }
    fun setIsAdmin(isAdmin: Boolean) {
        _uiState.value = _uiState.value.copy(isAdmin = isAdmin)
    }
    fun setIsStudentOrAdmin(isStudent: Boolean, isAdmin: Boolean) {
        _uiState.value = _uiState.value.copy(isStudent = isStudent, isAdmin = isAdmin)
    }

    fun save() {
        _uiState.value = _uiState.value.copy(progressState = ProgressState.Loading("Saving changes..."))
        viewModelScope.launch(Dispatchers.IO) {
            delay(4000)
            // TODO: Save changes
            _uiState.value = _uiState.value.copy(progressState = ProgressState.Idle)
        }
    }

    private var job: Job? = null
    @OptIn(InternalCoroutinesApi::class)
    fun cancelJob(handler: () -> Unit = {}) {
        job?.cancel()
        job?.invokeOnCompletion(true) {
            job = null
            handler()
            setProgressState(ProgressState.Idle)
        }
    }

    private fun fieldsAreValid(): Boolean {
        var valid = true
        if (_uiState.value.displayName.isBlank()) {
            _uiState.value = _uiState.value.copy(displayNameError = "Enter your display name.")
            valid = false
        } else if (_uiState.value.displayName.notMatches(namesRegex)) {
            _uiState.value = _uiState.value.copy(displayNameError = "Enter valid display name.")
            valid = false
        }
        if (_uiState.value.email.isBlank()) {
            _uiState.value = _uiState.value.copy(emailError = "Enter your email.")
            valid = false
        } else if (_uiState.value.email.notMatches(emailRegex)) {
            _uiState.value = _uiState.value.copy(emailError = "Enter a valid email.")
            valid = false
        }
        return valid
    }

    companion object {
        private const val TAG = "ProfileViewModel"
    }
}