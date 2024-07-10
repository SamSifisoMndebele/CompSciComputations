package com.compscicomputations.core.ktor_client.auth.models

data class DynamicFeature(
    val title: String,
    val moduleName: String,
    val className: String,
    val methodName: String? = null,
    val iconUrl: String? = null
) {
    val module: String get() = "com.compscicomputations.feature.$moduleName"
}
