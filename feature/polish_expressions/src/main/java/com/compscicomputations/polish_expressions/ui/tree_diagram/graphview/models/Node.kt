package com.compscicomputations.polish_expressions.ui.tree_diagram.graphview.models

import com.compscicomputations.polish_expressions.ui.tree_diagram.graphview.util.Size
import com.compscicomputations.polish_expressions.ui.tree_diagram.graphview.util.VectorF

data class Node(var pos: Int, var operand: String, var postfix: String, var prefix: String) {
    constructor(pos: Int,operand: String) : this(pos, operand, "", "")
    constructor(pos: Int,postfix: String, prefix: String) : this(pos, "", postfix, prefix)

    // TODO make private
    val position: VectorF = VectorF()
    val size: Size = Size()

    var height: Int
        get() = size.height
        set(value) {
            size.height = value
        }

    var width: Int
        get() = size.width
        set(value) {
            size.width = value
        }

    var x: Float
        get() = position.x
        set(value) {
            position.x = value
        }

    var y: Float
        get() = position.y
        set(value) {
            position.y = value
        }

    fun setSize(width: Int, height: Int) {
        this.width = width
        this.height = height
    }

    fun setPosition(x: Float, y: Float) {
        this.x = x
        this.y = y
    }

    fun setPosition(position: VectorF) {
        this.x = position.x
        this.y = position.y
    }
}
