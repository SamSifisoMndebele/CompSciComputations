package com.compscicomputations.client.publik.data.source.remote

import com.compscicomputations.client.utils.Onboarding
import com.compscicomputations.client.utils.ktorRequest
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.resources.delete
import io.ktor.client.plugins.resources.get
import io.ktor.client.plugins.resources.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import javax.inject.Inject

class OnboardingDataSource @Inject constructor(
    private val client: HttpClient
) {
    companion object {
        const val TAG = "AuthDataSource"

        class NotFoundException(message: String? = null): Exception(message)
        class ExpectationFailedException(message: String? = null): Exception(message)
    }

    /**
     * @return [RemoteOnboardingItem] list from the database.
     * @throws ExpectationFailedException if the was server side error.
     */
    internal suspend fun getOnboardingItems(except: IntArray? = null): List<RemoteOnboardingItem> = ktorRequest {
        val response = client.get(Onboarding.Items()) {
            contentType(ContentType.Application.Json)
            setBody(except)
        }
        when (response.status) {
            HttpStatusCode.ExpectationFailed -> throw ExpectationFailedException(response.bodyAsText())
            HttpStatusCode.OK -> response.body<List<RemoteOnboardingItem>>()
            else -> throw Exception("Unexpected response.")
        }
    }

    /**
     * @param onboardingItem [NewOnboardingItem] the new onboarding item information.
     * @return [RemoteOnboardingItem] the database onboarding item record.
     * @throws ExpectationFailedException if the was server side error.
     */
    internal suspend fun createOnboardingItem(onboardingItem: NewOnboardingItem): RemoteOnboardingItem = ktorRequest {
        val response = client.post(Onboarding.Items()) {
            contentType(ContentType.Application.Json)
            setBody(onboardingItem)
        }
        when (response.status) {
            HttpStatusCode.ExpectationFailed -> throw ExpectationFailedException(response.bodyAsText())
            HttpStatusCode.Created -> response.body<RemoteOnboardingItem>()
            else -> throw Exception("Unexpected response.")
        }
    }

}