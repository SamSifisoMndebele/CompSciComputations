package com.compscicomputations.number_systems.data.source.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.compscicomputations.number_systems.data.model.ConvertFrom
import com.compscicomputations.number_systems.data.model.CurrentTab

@Entity(indices = [Index(value = ["tab", "convert_from", "value"], unique = true)])
data class AiResponse(
    @PrimaryKey(true) val id: Int,
    val tab: CurrentTab,
    @ColumnInfo("convert_from")
    val convertFrom: ConvertFrom,
    val value: String,
    val text: String,
)
