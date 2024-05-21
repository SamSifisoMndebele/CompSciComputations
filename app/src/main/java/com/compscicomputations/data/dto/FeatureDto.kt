package com.compscicomputations.data.dto

import com.compscicomputations.data.model.Feature
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
    val feature get() = Feature(id, title, moduleName, className, methodName)
}