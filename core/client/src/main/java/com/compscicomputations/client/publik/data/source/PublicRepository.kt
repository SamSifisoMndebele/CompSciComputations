package com.compscicomputations.client.publik.data.source

import android.content.Context
import android.util.Log
import com.compscicomputations.client.auth.data.source.local.UserDataStore
import com.compscicomputations.client.auth.data.source.remote.AuthDataSource
import com.compscicomputations.client.publik.data.model.remote.NewFeedback
import com.compscicomputations.client.publik.data.model.remote.RemoteFeedback
import com.compscicomputations.client.publik.data.source.remote.PublicDataSource
import com.compscicomputations.client.utils.Feedback
import com.compscicomputations.client.utils.Image
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.retry
import java.lang.Exception
import javax.inject.Inject

class PublicRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val remoteDataSource: PublicDataSource,
    private val userDataStore: UserDataStore,
) {
    companion object {
        private const val TAG = "OnboardingRepository"
    }
    suspend fun sendFeedback(
        subject: String,
        message: String,
        suggestion: String,
        imageBytes: ByteArray?,
        userEmail: String?,
        onUpload: (bytesSent: Long, totalBytes: Long) -> Unit = {_,_ ->}
    ) {
        remoteDataSource.sendFeedback(NewFeedback(
            subject = subject,
            message = message,
            suggestion = suggestion,
            image = imageBytes?.let { Image(it) },
            userEmail = userEmail
        ), onUpload)
    }

    val feedbacksFlow: Flow<List<RemoteFeedback>>
        get() = flow {
            emit(remoteDataSource.getFeedbacks())
        }.retry(2) {
            Log.w(TAG, "Error syncing feedbacks, retrying.", it)
            it !is PublicDataSource.Companion.NotFoundException
        }


    val myFeedbacksFlow: Flow<List<RemoteFeedback>>
        get() = flow {
            val user = userDataStore.userFlow.first() ?: throw Exception("No User")
            val feedbacks = remoteDataSource.getMyFeedbacks(user.email)
            emit(feedbacks)
        }.retry(2) {
            Log.w(TAG, "Error syncing my feedbacks, retrying.", it)
            it !is PublicDataSource.Companion.NotFoundException
        }
    val myQuestionsFlow: Flow<List<RemoteFeedback>>
        get() = flow {
            val user = userDataStore.userFlow.first() ?: throw Exception("No User")
            val feedbacks = remoteDataSource.getMyFeedbacks(user.email)
            emit(feedbacks)
        }.retry(2) {
            Log.w(TAG, "Error syncing my feedbacks, retrying.", it)
            it !is PublicDataSource.Companion.NotFoundException
        }
}