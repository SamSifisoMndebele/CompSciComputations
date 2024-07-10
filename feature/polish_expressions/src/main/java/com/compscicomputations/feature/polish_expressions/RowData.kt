package com.compscicomputations.feature.polish_expressions


data class RowData(
    val char: Char,
    val stack: String,
    val polish : String
) {
//    val toJson: JsonElement
//        get() = Json.encodeToJsonElement(this)
//    companion object {
//        val List<RowData>.toJson: JsonElement
//            get() = Json.encodeToJsonElement(this)
//        val JsonElement.toRowData: RowData
//            get() = Json.decodeFromJsonElement<RowData>(this)
//        val JsonElement.toRowDataList: List<RowData>
//            get() = Json.decodeFromJsonElement<List<RowData>>(this)
//    }
}
