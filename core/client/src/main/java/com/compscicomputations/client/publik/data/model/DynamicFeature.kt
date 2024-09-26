package com.compscicomputations.client.publik.data.model

import kotlinx.serialization.Serializable

@Serializable
data class DynamicFeature(
    val name: String,
    val module: String,
    val icon: String,
    val clazz: String = "MainActivity",
    val isAvailable: Boolean = true,
    val isPreview: Boolean = false
) {
    val className: String
        get() = "com.compscicomputations.$module.$clazz"
}
