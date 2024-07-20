package com.compscicomputations.client.publik.data.source.remote

import com.compscicomputations.client.publik.models.Onboarding
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
    internal suspend fun getOnboardingItems(): List<RemoteOnboardingItem> = ktorRequest {
        val response = client.get(Onboarding.Items())
        when (response.status) {
            HttpStatusCode.ExpectationFailed -> throw ExpectationFailedException(response.bodyAsText())
            HttpStatusCode.OK -> response.body<List<RemoteOnboardingItem>>()
            else -> throw Exception("Unexpected response.")
        }
    }
    /**
     * @param id the onboarding item unique identifier
     * @return [RemoteOnboardingItem] from the database.
     * @throws NotFoundException if the is no corresponding onboarding item.
     * @throws ExpectationFailedException if the was server side error.
     */
    internal suspend fun getOnboardingItem(id: Int): RemoteOnboardingItem = ktorRequest {
        val response = client.get(Onboarding.Items.Id(id = id))
        when (response.status) {
            HttpStatusCode.NotFound -> throw NotFoundException(response.body<String?>())
            HttpStatusCode.ExpectationFailed -> throw ExpectationFailedException(response.bodyAsText())
            HttpStatusCode.OK -> response.body<RemoteOnboardingItem>()
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








    suspend fun updateOnboardingItem(id: Int, item: UpdateOnboardingItem) = ktorRequest {
        val response = client.post(Onboarding.Items.Id(id = id)) {
            contentType(ContentType.Application.Json)
            setBody(item)
        }
        if (response.status != HttpStatusCode.OK) throw Exception(response.bodyAsText())
    }
    suspend fun deleteOnboardingItems(id: Int) = ktorRequest {
        val response = client.delete(Onboarding.Items.Id(id = id))
        when {
            response.status != HttpStatusCode.OK -> throw Exception(response.bodyAsText())
            else -> {}
        }
    }
}