package com.compscicomputations.core.database.publik

import com.compscicomputations.core.database.publik.models.NewOnboardingItem
import com.compscicomputations.core.database.publik.models.OnboardingItem
import com.compscicomputations.core.database.publik.models.UpdateOnboardingItem

interface OnboardingRepository {
    suspend fun createOnboardingItem(item: NewOnboardingItem)

    suspend fun updateOnboardingItem(id: Int, item: UpdateOnboardingItem)

    suspend fun getOnboardingItem(id: Int): OnboardingItem?

    suspend fun getOnboardingItems(): List<OnboardingItem>

    suspend fun deleteOnboardingItems(id: Int)
}