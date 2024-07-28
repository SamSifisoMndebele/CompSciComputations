package com.compscicomputations.client.publik.data.model

import kotlinx.serialization.Serializable

@Serializable
data class DynamicFeature(
    val title: String,
    val module: String,
    val iconUrl: String,
) {
    val className: String
        get() = "com.compscicomputations.$module.MainActivity"
}
