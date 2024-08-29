package com.compscicomputations.client.publik.data.model.remote

import com.compscicomputations.client.publik.data.model.local.OnboardingItem
import com.compscicomputations.client.utils.Image
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RemoteOnboardingItem(
    val id: Int,
    val title: String,
    val description: String?,
    val image: Image?,
) {
    companion object {
        val List<RemoteOnboardingItem>.asOnboardingItems
            get() = map { it.asOnboardingItem }.toTypedArray()
    }
    val asOnboardingItem: OnboardingItem
        get() = OnboardingItem(
            id = id,
            title = title,
            description = description,
            imageBytes = image?.bytes,
        )
}