package com.compscicomputations.client.publik.data.source

import android.content.Context
import android.util.Log
import com.compscicomputations.client.publik.data.source.local.OnboardingItem
import com.compscicomputations.client.publik.data.source.local.OnboardingItemDao
import com.compscicomputations.client.publik.data.source.remote.NewOnboardingItem
import com.compscicomputations.client.publik.data.source.remote.OnboardingDataSource
import com.compscicomputations.client.publik.data.source.remote.RemoteOnboardingItem
import com.compscicomputations.client.publik.data.source.remote.RemoteOnboardingItem.Companion.asOnboardingItems
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.retry
import javax.inject.Inject

class OnboardingRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    private val remoteDataSource: OnboardingDataSource,
    private val onboardingItemDao: OnboardingItemDao
) {
    companion object {
        private const val TAG = "OnboardingRepository"
    }
    suspend fun createOnboardingItem(newOnboardingItem: NewOnboardingItem): RemoteOnboardingItem {
        val onboardingItem = remoteDataSource.createOnboardingItem(newOnboardingItem)
        onboardingItemDao.insert(onboardingItem.asOnboardingItem)
        return onboardingItem
    }

    val onboardingItemsFlow: Flow<List<OnboardingItem>>
        get() = flow {
            if (onboardingItemDao.isEmpty()) {
                Log.d(TAG, "Sync onboarding items from remote data source.")
                remoteDataSource.getOnboardingItems().let { remoteOnboardingItems ->
                    onboardingItemDao.insert(*remoteOnboardingItems.asOnboardingItems)
                }
            } else {
                try {
                    val itemsIds = onboardingItemDao.selectIds()
                    Log.d(TAG, "Sync onboarding items from remote data source, except (${itemsIds.joinToString()}).")
                    remoteDataSource.getOnboardingItems(itemsIds).let { remoteOnboardingItems ->
                        onboardingItemDao.delete(*itemsIds)
                        onboardingItemDao.insert(*remoteOnboardingItems.asOnboardingItems)
                    }
                } catch (e: Exception) {
                    emitAll(onboardingItemDao.selectAll())
                    throw e
                }
            }
            Log.d(TAG, "Fetching onboarding items from local database.")
            emitAll(onboardingItemDao.selectAll())
        }.retry(1) {
            Log.w(TAG, "Error syncing onboarding items, retrying.", it)
            true
        }


}