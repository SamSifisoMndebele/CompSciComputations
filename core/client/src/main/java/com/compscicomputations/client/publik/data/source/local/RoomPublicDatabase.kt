package com.compscicomputations.client.publik.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [OnboardingItem::class], version = 1, exportSchema = false)
abstract class RoomPublicDatabase : RoomDatabase() {
    abstract fun onboardingItemDao(): OnboardingItemDao
}