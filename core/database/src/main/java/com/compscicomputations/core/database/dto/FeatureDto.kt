package com.compscicomputations.core.database.dto

import com.compscicomputations.core.database.model.Feature
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class FeatureDto(
    @SerialName("id") val id: Int,
    @SerialName("title") val title: String,
    @SerialName("module_name") val moduleName: String,
    @SerialName("class_name") val className: String,
    @SerialName("method_name") val methodName: String?,
) {
    val asFeature get() = Feature(id, title, moduleName, className, methodName)
}