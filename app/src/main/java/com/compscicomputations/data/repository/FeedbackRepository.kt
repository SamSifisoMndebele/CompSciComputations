package com.compscicomputations.data.repository

import com.compscicomputations.data.dto.FeedbackDto
import com.compscicomputations.data.model.Feedback

interface FeedbackRepository {
    suspend fun createFeedback(feedback: Feedback): Boolean
    suspend fun getFeedbacks(): List<FeedbackDto>?
    suspend fun getFeedback(id: String): FeedbackDto
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