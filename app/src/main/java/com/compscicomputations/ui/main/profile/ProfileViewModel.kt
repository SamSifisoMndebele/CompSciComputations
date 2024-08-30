package com.compscicomputations.ui.main.profile

import android.net.Uri
import android.util.Log
import androidx.compose.material3.SnackbarHostState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compscicomputations.client.auth.data.model.Student
import com.compscicomputations.client.auth.data.model.User
import com.compscicomputations.client.auth.data.model.remote.UpdateUser
import com.compscicomputations.client.auth.data.source.AuthRepository
import com.compscicomputations.di.IoDispatcher
import com.compscicomputations.theme.namesRegex
import com.compscicomputations.ui.utils.ProgressState
import com.compscicomputations.utils.isBlankText
import com.compscicomputations.utils.isPhoneValid
import com.compscicomputations.utils.notMatches
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext
import kotlin.math.roundToInt

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ProfileUiState())
    private val _userState = MutableStateFlow<User?>(null)
    private val _studentState = MutableStateFlow<Student?>(null)
    private val _progressState = MutableStateFlow<ProgressState>(ProgressState.Idle)
    val uiState = _uiState.asStateFlow()
    val userState = _userState.asStateFlow()
    val progressState: StateFlow<ProgressState> = _progressState.asStateFlow()
    val snackBarHostState: SnackbarHostState = SnackbarHostState()

    var isNotChanged = false
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
                    _progressState.value = ProgressState.Error(e.localizedMessage)
                }
                .firstOrNull()
                ?.let { user ->
                    _userState.value = user
                    updateProfile(user)

                    if (user.isStudent) {
                        val student = Student(
                            id = user.id,
                            university = "university",
                            course = "course",
                            school = "school",
                        )
                        _studentState.value = student
                        _uiState.value = _uiState.value.copy(
                            university = student.university,
                            course = student.course,
                            school = student.school,
                        )
                    }
                    isNotChanged = true
                }
                ?: let {
                    _uiState.value = _uiState.value.copy(isSignedIn = false)
                    _progressState.value = ProgressState.Idle
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

            isSignedIn = true,
        )
        _progressState.value = ProgressState.Idle
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
                _progressState.value = ProgressState.Error(e.localizedMessage)
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
        _progressState.value = progressState
    }
    fun setIsStudent(isStudent: Boolean) {
        _uiState.value = _uiState.value.copy(isStudent = isStudent)
    }

    fun save(
        actionContext: CoroutineContext = Dispatchers.Main,
        action: (suspend () -> Unit)? = null
    ) {
        if (!fieldsAreValid()) return
        _progressState.value = ProgressState.Loading("Saving...")
        viewModelScope.launch(ioDispatcher) {
            authRepository.updateUser(
                id = _uiState.value.id,
                UpdateUser(
                    names = _uiState.value.names,
                    lastname = _uiState.value.lastname,
                    phone = _uiState.value.phone,
                    isStudent = _uiState.value.isStudent,
                    university = _uiState.value.university,
                    school = _uiState.value.school,
                    course = _uiState.value.course,
                )
            ) { bytesSent, totalBytes ->
                if (bytesSent == totalBytes) {
                    _progressState.value = ProgressState.Loading("Saving...")
                } else {
                    _progressState.value = ProgressState.Loading("Uploading image...\n" +
                            "${(bytesSent/1024.0).roundToInt()}kB/${(totalBytes/1024.0).roundToInt()}kB")
                }
            }
            _progressState.value = ProgressState.Idle
            if (action != null) { withContext(actionContext) { action() } }
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
            if (_uiState.value.university.isBlankText()) {
                _uiState.value = _uiState.value.copy(universityError = "Enter valid university name.")
                valid = false
            }
            if (_uiState.value.school.isBlankText()) {
                _uiState.value = _uiState.value.copy(schoolError = "Enter valid school name.")
                valid = false
            }
            if (_uiState.value.course.isBlankText()) {
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