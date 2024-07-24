package com.compscicomputations.client.auth.data.model

import kotlinx.serialization.Serializable

@Serializable
data class DynamicFeature(
    val title: String,
    val moduleName: String,
    val className: String = "MainKt",
    val methodName: String? = "MainScreen",
    val iconUrl: String? = null
) {
    val module: String get() = "com.compscicomputations.feature.$moduleName"
    val clazz: String get() = "$module.$className"
}
