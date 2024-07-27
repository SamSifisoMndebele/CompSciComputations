package com.compscicomputations.client.publik.data.model

import kotlinx.serialization.Serializable

@Serializable
data class DynamicFeature(
    val title: String,
    val module: String,
    val clazz: String = "MainActivity",
    val iconUrl: String? = null,
) {
    val className: String
        get() = "com.compscicomputations.$module.$clazz"


    @Deprecated("Use className instead",
        ReplaceWith("className")
    )
    val className2: String
        get() = "com.compscicomputations.feature.$module.$clazz"
}
