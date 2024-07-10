package com.compscicomputations.ui.auth.onboarding

import kotlinx.serialization.Serializable

@Serializable
data class OnboardingItem(
    val source: String,
    val title: String,
    val description: String,
    val type: ItemType,
)

enum class ItemType {
    IMAGE_URL,
    ANIM_URL,
}