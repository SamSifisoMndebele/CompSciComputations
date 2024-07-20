package com.compscicomputations.client.publik.data.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface OnboardingItemDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vararg onboardingItems: OnboardingItem)

    @Update
    suspend fun update(vararg onboardingItems: OnboardingItem)

    @Delete
    suspend fun delete(vararg onboardingItems: OnboardingItem)

    @Query("SELECT * FROM onboardingitem WHERE id = :id")
    fun select(id: Int): Flow<OnboardingItem>

    @Query("select count(1) where not exists (select * from onboardingitem)")
    fun isEmpty(): Boolean

    @Query("SELECT id FROM onboardingitem")
    fun selectIds(): IntArray


    @Query("SELECT * FROM onboardingitem")
    fun selectAll(): Flow<List<OnboardingItem>>
}