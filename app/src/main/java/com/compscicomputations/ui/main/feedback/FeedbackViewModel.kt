package com.compscicomputations.ui.main.feedback

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.compscicomputations.client.auth.data.model.User
import com.compscicomputations.client.auth.data.source.AuthRepository
import com.compscicomputations.client.publik.data.source.PublicRepository
import com.compscicomputations.client.utils.ByteArrayUseCase
import com.compscicomputations.di.IoDispatcher
import com.compscicomputations.ui.utils.ProgressState
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
import javax.inject.Inject
import kotlin.math.roundToInt

@HiltViewModel
class FeedbackViewModel @Inject constructor(
    private val publicRepository: PublicRepository,
    private val authRepository: AuthRepository,
    private val byteArrayUseCase: ByteArrayUseCase,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {
    private val _uiState = MutableStateFlow(FeedbackUiState())
    val uiState: StateFlow<FeedbackUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            authRepository.currentUserFlow
                .flowOn(ioDispatcher)
                .catch { Log.w("FeedbackViewModel", it) }
                .firstOrNull()
                ?.let { user ->
                    _uiState.value = _uiState.value.copy(email = user.email)
                }
        }
    }

    fun setPhotoUri(photoUri: Uri?) {
        _uiState.value = _uiState.value.copy(imageUri = photoUri)
    }
    fun onSubjectChange(subject: Subject) {
        _uiState.value = _uiState.value.copy(subject = subject, subjectError = null)
    }
    fun onOtherSubjectChange(otherSubject: String?) {
        _uiState.value = _uiState.value.copy(otherSubject = otherSubject, subjectError = null)
    }
    fun onMessageChange(message: String) {
        _uiState.value = _uiState.value.copy(message = message, messageError = null)
    }
    fun onSuggestionChange(suggestion: String) {
        _uiState.value = _uiState.value.copy(suggestion = suggestion)
    }
    fun setAsAnonymous(asAnonymous: Boolean) {
        _uiState.value = _uiState.value.copy(asAnonymous = asAnonymous)
    }

    fun onProgressStateChange(progressState: ProgressState) {
        _uiState.value = _uiState.value.copy(progressState = progressState)
    }

    private var job: Job? = null
    fun onSendFeedback() {
        if (!fieldsAreValid()) return
        _uiState.value = _uiState.value.copy(progressState = ProgressState.Loading("Sending feedback..."))
        job = viewModelScope.launch {
            try {
                publicRepository.sendFeedback(
                    if (_uiState.value.subject != Subject.Other) _uiState.value.subject.name else _uiState.value.otherSubject!!,
                    _uiState.value.message,
                    _uiState.value.suggestion,
                    _uiState.value.imageUri?.let { byteArrayUseCase(it) },
                    if (uiState.value.asAnonymous) null else _uiState.value.email
                ) { bytesSent, totalBytes ->
                    if (bytesSent != totalBytes) {
                        _uiState.value = _uiState.value.copy(progressState = ProgressState.Loading("Uploading image...\n" +
                                "${(bytesSent/1024.0).roundToInt()}kB/${(totalBytes/1024.0).roundToInt()}kB"))
                    } else {
                        _uiState.value = _uiState.value.copy(progressState = ProgressState.Loading("Sending feedback...\n" +
                                "Please wait."))
                    }

                }
                _uiState.value = _uiState.value.copy(
                    progressState = ProgressState.Success,
                    subject = Subject.Comment,
                    otherSubject = null,
                    message = "",
                    suggestion = "",
                    imageUri = null,
                    asAnonymous = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(progressState = ProgressState.Error(e.localizedMessage))
                Log.e(TAG, "onSendFeedback", e)
            }
        }
    }

    @OptIn(InternalCoroutinesApi::class)
    fun cancelSendFeedback(handler: () -> Unit = {}) {
        job?.cancel()
        job?.invokeOnCompletion(true) {
            job = null
            handler()
            onProgressStateChange(ProgressState.Idle)
        }
    }

    private fun fieldsAreValid(): Boolean {
        var valid = true
        if (_uiState.value.subject == Subject.Other && _uiState.value.otherSubject.isNullOrBlank()) {
            _uiState.value = _uiState.value.copy(subjectError = "Subject required.")
            valid = false
        } else if (_uiState.value.message.isBlank()) {
            _uiState.value = _uiState.value.copy(messageError = "Message required")
            valid = false
        }
        return valid
    }

    companion object {
        private const val TAG = "FeedbackViewModel"
    }
}