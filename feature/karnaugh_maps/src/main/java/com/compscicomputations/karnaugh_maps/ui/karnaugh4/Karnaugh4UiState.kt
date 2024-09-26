package com.compscicomputations.karnaugh_maps.ui.karnaugh4

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import com.compscicomputations.karnaugh_maps.data.model.ConvertFrom
import com.compscicomputations.karnaugh_maps.utils.ListOfMinTerms
import com.compscicomputations.ui.main.dynamic_feature.AIState

data class Karnaugh4UiState(
    val minTerms: IntArray = intArrayOf(),

    val answers: List<ListOfMinTerms> = listOf(ListOfMinTerms(0)),
    val selected: Int = 0,

    val error: String? = null,
    val convertFrom: ConvertFrom = ConvertFrom.Expression,
    val aiState: AIState = AIState.Idle,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Karnaugh4UiState

        if (!minTerms.contentEquals(other.minTerms)) return false
        if (answers != other.answers) return false
        if (selected != other.selected) return false
        if (error != other.error) return false
        if (convertFrom != other.convertFrom) return false
        if (aiState != other.aiState) return false

        return true
    }

    override fun hashCode(): Int {
        var result = minTerms.contentHashCode()
        result = 31 * result + answers.hashCode()
        result = 31 * result + selected
        result = 31 * result + (error?.hashCode() ?: 0)
        result = 31 * result + convertFrom.hashCode()
        result = 31 * result + aiState.hashCode()
        return result
    }


}
