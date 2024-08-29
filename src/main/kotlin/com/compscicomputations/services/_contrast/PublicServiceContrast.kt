package com.compscicomputations.services._contrast

import com.compscicomputations.services.publik.models.requests.NewOnboardingItem
import com.compscicomputations.services.publik.models.response.OnboardingItem

internal interface PublicServiceContrast {

    suspend fun createOnboardingItem(item: NewOnboardingItem)

    suspend fun getOnboardingItems(except: IntArray?): List<OnboardingItem>

    suspend fun deleteOnboardingItem(id: Int)
}