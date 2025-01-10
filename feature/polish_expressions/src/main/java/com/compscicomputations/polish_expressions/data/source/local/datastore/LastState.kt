package com.compscicomputations.polish_expressions.data.source.local.datastore

import com.compscicomputations.polish_expressions.data.model.ConvertFrom
import com.compscicomputations.polish_expressions.ui.Token

data class LastState(
    val convertFrom: ConvertFrom,
    val fromValue: List<Token>
)