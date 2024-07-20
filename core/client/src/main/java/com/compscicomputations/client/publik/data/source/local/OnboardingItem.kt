package com.compscicomputations.client.publik.data.source.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.compscicomputations.client.publik.models.SourceType

@Entity
data class OnboardingItem(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "source_url") val sourceUrl: String,
    val title: String,
    val description: String?,
    @ColumnInfo(name = "source_type") val sourceType: SourceType,
)