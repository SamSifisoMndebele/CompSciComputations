package com.compscicomputations.data.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface AiResponseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(vararg aiResponse: AiResponse)

    @Update
    suspend fun update(vararg aiResponse: AiResponse)

    @Delete
    suspend fun delete(vararg aiResponse: AiResponse)

    @Query("SELECT * FROM airesponse " +
            "WHERE module LIKE :module " +
            "AND current_tab LIKE :currentTab " +
            "AND convert_from LIKE :convertFrom " +
            "AND value like :value " +
            "ORDER BY created_at DESC")
    suspend fun select(
        module: String,
        currentTab: String,
        convertFrom: String,
        value: String
    ): AiResponse?

    @Query("SELECT * FROM airesponse " +
            "WHERE module LIKE :module " +
            "AND current_tab LIKE :currentTab " +
            "ORDER BY created_at DESC")
    fun selectAll(
        module: String,
        currentTab: String,
    ): Flow<List<AiResponse>?>

    @Query("SELECT count(1) WHERE EXISTS (SELECT * FROM airesponse " +
            "WHERE module LIKE :module " +
            "AND current_tab LIKE :currentTab " +
            "AND convert_from LIKE :convertFrom " +
            "AND value LIKE :value " +
            "ORDER BY created_at DESC)")
    fun exists(
        module: String,
        currentTab: String,
        convertFrom: String,
        value: String
    ): Flow<Boolean>

}