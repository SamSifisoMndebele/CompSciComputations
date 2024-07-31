package com.compscicomputations.number_systems.data.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.compscicomputations.number_systems.data.model.ConvertFrom
import kotlinx.coroutines.flow.Flow

@Dao
interface AiResponseDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vararg aiResponse: AiResponse)

    @Update
    suspend fun update(vararg aiResponse: AiResponse)

    @Query("delete from airesponse where id in (:ids)")
    suspend fun delete(vararg ids: Int)

    @Query("SELECT * FROM airesponse WHERE id = :id")
    fun select(id: Int): Flow<AiResponse>

    @Query("SELECT * FROM airesponse WHERE convert_from = :convertFrom AND value like :value")
    suspend fun select(convertFrom: ConvertFrom, value: String): AiResponse?

    @Query("SELECT id FROM airesponse")
    fun selectIds(): IntArray

    @Query("SELECT * FROM airesponse")
    fun selectAll(): Flow<List<AiResponse>>
}