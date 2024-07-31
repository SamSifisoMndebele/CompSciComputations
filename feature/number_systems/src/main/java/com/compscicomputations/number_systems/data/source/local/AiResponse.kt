package com.compscicomputations.number_systems.data.source.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.compscicomputations.number_systems.data.model.ConvertFrom

@Entity
data class AiResponse(
    @PrimaryKey(true) val id: Int,
    @ColumnInfo("convert_from")
    val convertFrom: ConvertFrom,
    val value: String,
    val text: String,
)
