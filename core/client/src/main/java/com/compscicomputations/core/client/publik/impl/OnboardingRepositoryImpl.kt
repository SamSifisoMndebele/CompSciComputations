package com.compscicomputations.core.client.publik.impl

import com.compscicomputations.core.client.publik.OnboardingRepository
import com.compscicomputations.core.client.publik.models.NewOnboardingItem
import com.compscicomputations.core.client.publik.models.Onboarding
import com.compscicomputations.core.client.publik.models.OnboardingItem
import com.compscicomputations.core.client.publik.models.UpdateOnboardingItem
import com.compscicomputations.core.client.utils.ktorRequest
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.resources.delete
import io.ktor.client.plugins.resources.get
import io.ktor.client.plugins.resources.post
import io.ktor.client.request.accept
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class OnboardingRepositoryImpl(
    private val client: HttpClient,
) : OnboardingRepository {
    override suspend fun createOnboardingItem(item: NewOnboardingItem) = ktorRequest {
        val response = client.post(Onboarding.Items()) {
            contentType(ContentType.Application.Json)
            setBody(item)
        }
        if (response.status != HttpStatusCode.Created) throw Exception(response.bodyAsText())
    }

    override suspend fun updateOnboardingItem(id: Int, item: UpdateOnboardingItem) = ktorRequest {
        val response = client.post(Onboarding.Items.Id(id = id)) {
            contentType(ContentType.Application.Json)
            setBody(item)
        }
        if (response.status != HttpStatusCode.OK) throw Exception(response.bodyAsText())
    }

    override suspend fun getOnboardingItem(id: Int): OnboardingItem? = ktorRequest {
        val response = client.get(Onboarding.Items.Id(id = id)) {
            accept(ContentType.Application.Json)
        }
        when {
            response.status != HttpStatusCode.OK -> throw Exception(response.bodyAsText())
            else -> response.body<OnboardingItem?>()
        }
    }

    override suspend fun getOnboardingItems(): List<OnboardingItem> = ktorRequest {
        val response = client.get(Onboarding.Items()) {
            accept(ContentType.Application.Json)
        }
        when {
            response.status != HttpStatusCode.OK -> throw Exception(response.bodyAsText())
            else -> response.body<List<OnboardingItem>>()
        }
    }

    override suspend fun deleteOnboardingItems(id: Int) = ktorRequest {
        val response = client.delete(Onboarding.Items.Id(id = id))
        when {
            response.status != HttpStatusCode.OK -> throw Exception(response.bodyAsText())
            else -> {}
        }
    }
}