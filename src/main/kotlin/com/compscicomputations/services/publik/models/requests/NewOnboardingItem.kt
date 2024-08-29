package com.compscicomputations.services.publik.models.requests

import com.compscicomputations.utils.Image
import kotlinx.serialization.Serializable

@Serializable
data class NewOnboardingItem(
    val title: String,
    val description: String?,
    val image: Image?,
)