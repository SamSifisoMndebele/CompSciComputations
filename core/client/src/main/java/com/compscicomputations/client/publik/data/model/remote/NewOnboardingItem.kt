package com.compscicomputations.client.publik.data.model.remote

import com.compscicomputations.client.utils.Image
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class NewOnboardingItem(
    val title: String,
    val description: String?,
    val image: Image?,
)