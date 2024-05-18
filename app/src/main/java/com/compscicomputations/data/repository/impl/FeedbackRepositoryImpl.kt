package com.compscicomputations.data.repository.impl

import com.compscicomputations.data.dto.FeedbackDto
import com.compscicomputations.data.model.Feedback
import com.compscicomputations.data.repository.FeedbackRepository

class FeedbackRepositoryImpl : FeedbackRepository {
    override suspend fun createFeedback(feedback: Feedback): Boolean {
        TODO("Not yet implemented")
        //UUID.randomUUID().toString()
    }

    override suspend fun getFeedbacks(): List<FeedbackDto>? {
        TODO("Not yet implemented")
    }

    override suspend fun getFeedback(id: String): FeedbackDto {
        TODO("Not yet implemented")
    }

    override suspend fun deleteFeedback(id: String) {
        TODO("Not yet implemented")
    }

    override suspend fun updateFeedback(
        id: Int,
        title: String,
        content: String,
        imageUrl: String?,
        uid: String
    ) {
        TODO("Not yet implemented")
    }
}