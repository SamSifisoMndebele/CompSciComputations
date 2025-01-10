package com.compscicomputations.polish_expressions.presentation.tree_diagram

import androidx.lifecycle.ViewModel
import com.compscicomputations.polish_expressions.domain.Token
import com.compscicomputations.polish_expressions.domain.graphview.models.Edge
import com.compscicomputations.polish_expressions.domain.graphview.models.Graph
import com.compscicomputations.polish_expressions.domain.graphview.models.Node
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Stack

class TreeDiagramViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(TreeDiagramUiState())
    val uiState = _uiState.asStateFlow()

    fun onChange(infix: String, prefix: String, postfix: String) {
        _uiState.value = _uiState.value.copy(
            infix = infix,
            prefix = prefix,
            postfix = postfix,
            graph = postfix.drawTree()
        )
    }

    private fun String.drawTree(): Graph {
        val nodesStack: Stack<Node> = Stack()
        val edgeStack: Stack<Edge> = Stack()

        var i = 0
        while (i < length){
            var char = this[i]

            if (char.isLetter()) {
                nodesStack.push(Node(i, char.toString()))
                i++
                continue
            }

            val num = StringBuilder()
            while (char.isDigit() || char == '.'){
                num.append(char)
                char = this[++i]
            }
            if (num.isNotEmpty()){
                nodesStack.push(Node(i,num.toString()) )
                continue
            }
            if (char.isWhitespace()) {
                i++
                continue
            }


            val rightNode = nodesStack.pop()
            val leftNode = nodesStack.pop()

            val rightPostfix = rightNode.operand + rightNode.postfix
            val leftPostfix  = leftNode.operand + leftNode.postfix

            val rightPrefix = rightNode.operand + rightNode.prefix
            val leftPrefix = leftNode.operand + leftNode.prefix

            val parentNode = Node(i,
                "${leftPostfix.trim()} ${rightPostfix.trim()} $char",
                "$char ${leftPrefix.trim()} ${rightPrefix.trim()}")

            edgeStack.push(Edge(parentNode, rightNode ))
            edgeStack.push(Edge(parentNode, Node(i++,char.toString()) ))
            edgeStack.push(Edge(parentNode,leftNode ))

            nodesStack.push(parentNode)
            i++
        }

        val graph = Graph()
        while (edgeStack.isNotEmpty()) {
            graph.addEdge(edgeStack.pop())
        }
        return graph
    }
}