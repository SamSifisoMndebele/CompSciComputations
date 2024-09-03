package com.compscicomputations.polish_expressions.ui.tree_diagram

import com.compscicomputations.polish_expressions.ui.tree_diagram.graphview.models.Graph

data class TreeDiagramUiState(
    val infix: String = "",
    val postfix: String = "",
    val prefix: String = "",

    val graph: Graph? = null,

    val error: String? = null
)