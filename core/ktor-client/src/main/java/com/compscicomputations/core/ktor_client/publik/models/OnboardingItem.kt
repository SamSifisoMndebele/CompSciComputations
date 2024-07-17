package com.compscicomputations.core.ktor_client.publik.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OnboardingItem(
    val id: Int,
    @SerialName("source_url")
    val sourceUrl: String,
    val title: String,
    val description: String?,
    val type: SourceType,
)