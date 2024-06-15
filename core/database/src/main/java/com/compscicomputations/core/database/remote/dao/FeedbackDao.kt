package com.compscicomputations.core.database.remote.dao

import com.compscicomputations.core.database.model.Feedback

interface FeedbackDao {
    suspend fun createFeedback(feedback: Feedback): Boolean
    suspend fun getFeedbacks(): List<Feedback>?
    suspend fun getFeedback(id: String): Feedback
    suspend fun deleteFeedback(id: String)
    suspend fun updateFeedback(feedback: Feedback) =
        updateFeedback(feedback.id, feedback.title, feedback.content, feedback.imageUrl, feedback.uid)
    suspend fun updateFeedback(
        id: Int,
        title: String,
        content: String,
        imageUrl: String?,
        uid: String
    )
}