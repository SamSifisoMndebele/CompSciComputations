package com.compscicomputations.client.publik.data.model

import kotlinx.serialization.Serializable

@Serializable
data class DynamicFeature(
    val title: String,
    val module: String,
    val iconUrl: String? = null,
    val clazz: String = "MainKt",
    val methodName: String? = if (clazz == "MainKt") "MainScreen" else null,
) {
    val className: String
        get() = "com.compscicomputations.feature.$module.$clazz"
}
