package com.compscicomputations.services.publik.models.response

import com.compscicomputations.utils.Image
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class OnboardingItem(
    val id: Int,
    val title: String,
    val description: String?,
    val image: Image?,
)