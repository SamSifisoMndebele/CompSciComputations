package com.compscicomputations.ui.main.profile

import android.net.Uri
import android.util.Log
import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compscicomputations.client.auth.data.model.Student
import com.compscicomputations.client.auth.data.model.User
import com.compscicomputations.client.auth.data.source.AuthRepository
import com.compscicomputations.di.IoDispatcher
import com.compscicomputations.theme.emailRegex
import com.compscicomputations.theme.namesRegex
import com.compscicomputations.theme.strongPasswordRegex
import com.compscicomputations.ui.utils.ProgressState
import com.compscicomputations.ui.utils.isSuccess
import com.compscicomputations.utils.isNotBlankText
import com.compscicomputations.utils.isPhoneValid
import com.compscicomputations.utils.isText
import com.compscicomputations.utils.notMatches
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
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
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState())
    private val _userState = MutableStateFlow<User?>(null)
    private val _studentState = MutableStateFlow<Student?>(null)
    val uiState = _uiState.asStateFlow()
    val userState = _userState.asStateFlow()
    val snackBarHostState: SnackbarHostState = SnackbarHostState()

    val isNotChanged
        get() = _uiState.value.imageUri == null &&
                _uiState.value.names == _userState.value?.names &&
                _uiState.value.lastname == _userState.value?.lastname &&
                _uiState.value.phone == _userState.value?.phone &&

                _uiState.value.isStudent == (_studentState.value != null) &&
                _uiState.value.university == _studentState.value?.university &&
                _uiState.value.school == _studentState.value?.school &&
                _uiState.value.course == _studentState.value?.course

    init {
        viewModelScope.launch(ioDispatcher) {
            authRepository.currentUserFlow
                .flowOn(ioDispatcher)
                .catch { e ->
                    Log.w("DashboardViewModel", e)
                    _uiState.value = _uiState.value.copy(progressState = ProgressState.Error(e.localizedMessage))
                }
                .firstOrNull()
                ?.let { user ->
                    updateProfile(user)
                    _userState.value = user

                    if (user.isStudent) {
                        _studentState.value = Student(
                            id = user.id,
                            university = "university",
                            course = "course",
                            school = "school",
                        )
                    }
                }
                ?: let {
                    _uiState.value = _uiState.value.copy(
                        isSignedIn = false,
                        progressState = ProgressState.Idle
                    )
                }
        }
    }

    private fun updateProfile(user: User, student: Student? = null) {
        _uiState.value = _uiState.value.copy(
            id = user.id,
            names = user.names,
            lastname = user.lastname,
            phone = user.phone,
            isStudent = user.isStudent,

            university = student?.university ?: "",
            course = student?.course ?: "",
            school = student?.school ?: "",

            progressState = ProgressState.Idle,
            isSignedIn = true,
        )
    }

//    fun onRefresh() {
//        _uiState.value = _uiState.value.copy(progressState = ProgressState.Loading())
//        viewModelScope.launch(ioDispatcher) {
//            authRepository.refreshUserFlow
//                .flowOn(ioDispatcher)
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
    fun onNamesChange(names: String) {
        _uiState.value = _uiState.value.copy(names = names, namesError = null)
    }
    fun onLastnameChange(lastname: String) {
        _uiState.value = _uiState.value.copy(lastname = lastname, lastnameError = null)
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

    fun setProgressState(progressState: ProgressState) {
        _uiState.value = _uiState.value.copy(progressState = progressState)
    }
    fun setIsStudent(isStudent: Boolean) {
        _uiState.value = _uiState.value.copy(isStudent = isStudent)
    }

    fun save() {
        if (!fieldsAreValid()) return
        _uiState.value = _uiState.value.copy(progressState = ProgressState.Loading("Saving changes..."))
        viewModelScope.launch(ioDispatcher) {
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
        if (_uiState.value.names.isBlank()) {
            _uiState.value = _uiState.value.copy(namesError = "Enter your names.")
            valid = false
        } else if (_uiState.value.names.notMatches(namesRegex)) {
            _uiState.value = _uiState.value.copy(namesError = "Enter valid names.")
            valid = false
        }
        if (_uiState.value.lastname.isBlank()) {
            _uiState.value = _uiState.value.copy(lastnameError = "Enter your lastname.")
            valid = false
        } else if (_uiState.value.lastname.notMatches(namesRegex)) {
            _uiState.value = _uiState.value.copy(lastnameError = "Enter valid lastname.")
            valid = false
        }
        if (!_uiState.value.phone.isPhoneValid()) {
            _uiState.value = _uiState.value.copy(phoneError = "Enter a valid phone.")
            valid = false
        }
        if (_uiState.value.isStudent) {
            if (_uiState.value.university.isNotBlankText()) {
                _uiState.value = _uiState.value.copy(universityError = "Enter valid university name.")
                valid = false
            }
            if (_uiState.value.school.isNotBlankText()) {
                _uiState.value = _uiState.value.copy(schoolError = "Enter valid school name.")
                valid = false
            }
            if (_uiState.value.course.isNotBlankText()) {
                _uiState.value = _uiState.value.copy(courseError = "Enter valid course name.")
                valid = false
            }
        }
        return valid
    }

    companion object {
        private const val TAG = "ProfileViewModel"
    }
}