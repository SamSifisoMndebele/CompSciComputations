package com.compscicomputations.client.publik.data.source.remote

import com.compscicomputations.client.publik.data.source.local.OnboardingItem
import com.compscicomputations.client.publik.models.SourceType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RemoteOnboardingItem(
    val id: Int,
    @SerialName("source_url")
    val sourceUrl: String,
    val title: String,
    val description: String?,
    @SerialName("source_type")
    val sourceType: SourceType,
) {
    val asOnboardingItem: OnboardingItem
        get() = OnboardingItem(
            id = id,
            sourceUrl = sourceUrl,
            title = title,
            description = description,
            sourceType = sourceType
        )
}