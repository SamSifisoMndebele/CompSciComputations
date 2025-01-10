package com.compscicomputations.polish_expressions.presentation.tree_diagram

import com.compscicomputations.polish_expressions.domain.Token
import com.compscicomputations.polish_expressions.domain.graphview.models.Graph

data class TreeDiagramUiState(
    val infix: String = "",
    val postfix: String = "",
    val prefix: String = "",

    val graph: Graph? = null,

    val error: String? = null
)