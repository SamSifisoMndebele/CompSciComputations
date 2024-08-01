package com.compscicomputations.number_systems.data.source.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.compscicomputations.number_systems.data.model.ConvertFrom
import com.compscicomputations.number_systems.data.model.CurrentTab
import kotlinx.coroutines.flow.Flow

@Dao
interface AiResponseDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(vararg aiResponse: AiResponse)

    @Update
    suspend fun update(vararg aiResponse: AiResponse)

    @Query("delete from airesponse where id in (:ids)")
    suspend fun delete(vararg ids: Int)

    @Query("SELECT * FROM airesponse " +
            "WHERE tab = :tab " +
            "AND convert_from = :convertFrom " +
            "AND value like :value " +
            "ORDER BY id DESC")
    suspend fun select(tab: CurrentTab, convertFrom: ConvertFrom, value: String): AiResponse?

    @Query("SELECT * FROM airesponse WHERE tab = :tab ORDER BY id DESC")
    fun selectAll(tab: CurrentTab): Flow<List<AiResponse>>
}