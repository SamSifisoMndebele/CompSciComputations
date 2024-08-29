package com.compscicomputations.data.source.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index

@Entity(primaryKeys = ["module", "current_tab", "convert_from"], indices = [Index("value", unique = true)])
data class AiResponse(
    val module: String,
    @ColumnInfo("current_tab")
    val currentTab: String,
    @ColumnInfo("convert_from")
    val convertFrom: String,
    val value: String,
    val text: String,
    @ColumnInfo(name = "created_at") val createdAt: Long = System.currentTimeMillis(),
)
