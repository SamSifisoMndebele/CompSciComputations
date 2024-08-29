package com.compscicomputations.client.publik.data.source

import android.content.Context
import android.util.Log
import com.compscicomputations.client.publik.data.model.remote.NewFeedback
import com.compscicomputations.client.publik.data.model.remote.RemoteFeedback
import com.compscicomputations.client.publik.data.source.remote.PublicDataSource
import com.compscicomputations.client.utils.Image
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.retry
import javax.inject.Inject

class PublicRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val remoteDataSource: PublicDataSource,
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
        }.retry(1) {
            Log.w(TAG, "Error syncing onboarding items, retrying.", it)
            true
        }


}