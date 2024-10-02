package com.compscicomputations.ui.main.feedback

import android.net.Uri
import com.compscicomputations.ui.utils.ProgressState

enum class Subject {
    Comment,
    Suggestion,
    Complaint,
    BugReport {
        override fun toString(): String = "Bug Report"
    },
    FeatureRequest {
        override fun toString(): String = "Feature Request"
    },
    Other
}

data class FeedbackUiState(
    val subject: Subject = Subject.Comment,
    val otherSubject: String? = null,
    val message: String = "",
    val suggestion: String = "",
    val imageUri: Uri? = null,
    val email: String? = null,
    val asAnonymous: Boolean = false,

    val subjectError: String? = null,
    val messageError: String? = null,

    val progressState: ProgressState = ProgressState.Idle
) {
    val isValid: Boolean
        get() = (subject != Subject.Other || !otherSubject.isNullOrBlank()) && message.isNotBlank()
}