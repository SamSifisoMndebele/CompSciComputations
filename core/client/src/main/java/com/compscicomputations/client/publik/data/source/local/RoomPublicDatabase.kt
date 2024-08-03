package com.compscicomputations.client.publik.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.compscicomputations.client.publik.data.model.local.OnboardingItem

@Database(entities = [OnboardingItem::class], version = 2, exportSchema = false)
abstract class RoomPublicDatabase : RoomDatabase() {
    abstract fun onboardingItemDao(): OnboardingItemDao
}