package com.compscicomputations.services.publik

import com.compscicomputations.services.publik.models.requests.NewOnboardingItem
import com.compscicomputations.services.publik.models.requests.UpdateOnboardingItem
import com.compscicomputations.services.publik.models.response.OnboardingItem

internal interface PublicService {

    suspend fun createOnboardingItem(item: NewOnboardingItem)

    suspend fun updateOnboardingItem(id: Int, item: UpdateOnboardingItem)

    suspend fun getOnboardingItem(id: Int): OnboardingItem?

    suspend fun getOnboardingItems(): List<OnboardingItem>

    suspend fun getOnboardingItemsExcept(ids: IntArray): List<OnboardingItem>

    suspend fun deleteOnboardingItems(id: Int)
}