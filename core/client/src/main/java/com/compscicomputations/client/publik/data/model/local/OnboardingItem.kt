package com.compscicomputations.client.publik.data.model.local

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class OnboardingItem(
    @PrimaryKey val id: Int,
    val title: String,
    val description: String?,
    @ColumnInfo("image_bytes")
    val imageBytes: ByteArray?,
)
{
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as OnboardingItem

        if (id != other.id) return false
        if (title != other.title) return false
        if (description != other.description) return false
        if (imageBytes != null) {
            if (other.imageBytes == null) return false
            if (!imageBytes.contentEquals(other.imageBytes)) return false
        } else if (other.imageBytes != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + title.hashCode()
        result = 31 * result + (description?.hashCode() ?: 0)
        result = 31 * result + (imageBytes?.contentHashCode() ?: 0)
        return result
    }
}