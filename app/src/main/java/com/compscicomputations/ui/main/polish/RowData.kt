package com.compscicomputations.ui.main.polish

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.encodeToJsonElement

@Serializable
data class RowData(
    val char: Char,
    val stack: String,
    val polish : String
) {
    val toJson: JsonElement
        get() = Json.encodeToJsonElement(this)
    companion object {
        val List<RowData>.toJson: JsonElement
            get() = Json.encodeToJsonElement(this)
        val JsonElement.toRowData: RowData
            get() = Json.decodeFromJsonElement<RowData>(this)
        val JsonElement.toRowDataList: List<RowData>
            get() = Json.decodeFromJsonElement<List<RowData>>(this)
    }
}
