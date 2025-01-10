package com.compscicomputations.polish_expressions.domain.graphview

import androidx.recyclerview.widget.RecyclerView
import com.compscicomputations.polish_expressions.domain.graphview.models.Graph
import com.compscicomputations.polish_expressions.domain.graphview.models.Node

abstract class AbstractGraphAdapter<VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {
    var graph: Graph? = null
    override fun getItemCount(): Int = graph?.nodeCount ?: 0

    open fun getNode(position: Int): Node? = graph?.getNodeAtPosition(position)
    open fun getNodeOperand(position: Int): String? = graph?.getNodeAtPosition(position)?.operand
    open fun getNodePostfix(position: Int): String? = graph?.getNodeAtPosition(position)?.postfix
    open fun getNodePrefix(position: Int): String? = graph?.getNodeAtPosition(position)?.prefix
    /**
     * Submits a new graph to be displayed.
     *
     *
     * If a graph is already being displayed, you need to dispatch Adapter.notifyItem.
     *
     * @param graph The new graph to be displayed.
     */
    open fun submitGraph(graph: Graph?) {
        this.graph = graph
    }
}