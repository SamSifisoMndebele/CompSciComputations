package com.compscicomputations.client.publik.data.source

import android.content.Context
import android.util.Log
import com.compscicomputations.client.publik.data.source.local.OnboardingItem
import com.compscicomputations.client.publik.data.source.local.OnboardingItemDao
import com.compscicomputations.client.publik.data.source.remote.NewOnboardingItem
import com.compscicomputations.client.publik.data.source.remote.OnboardingDataSource
import com.compscicomputations.client.publik.data.source.remote.RemoteOnboardingItem
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

    fun onboardingItemsFlow(): Flow<List<OnboardingItem>> = flow {
        if (onboardingItemDao.isEmpty()) {
            Log.d(TAG, "Fetching onboarding items from remote data source.")
            remoteDataSource.getOnboardingItems().let { remoteOnboardingItems ->
                val onboardingItems = remoteOnboardingItems.map { it.asOnboardingItem }.toTypedArray()
                onboardingItemDao.insert(*onboardingItems)
            }
        } else {
            try {
                val itemsIds = onboardingItemDao.selectIds()
                Log.d(TAG, "Fetching except (${itemsIds.joinToString()}) onboarding items from remote data source.")
                //TODO:
            } catch (e:Exception) {
                emitAll(onboardingItemDao.selectAll())
                throw e
            }
        }
//        onboardingItemDao.delete(*onboardingItemDao.selectAll().first().toTypedArray())

        Log.d(TAG, "Fetching onboarding items from local database.")
        emitAll(onboardingItemDao.selectAll())

    }.retry(2) {
        Log.w(TAG, "Error fetching onboarding items, retrying.", it)
        true
    }


}