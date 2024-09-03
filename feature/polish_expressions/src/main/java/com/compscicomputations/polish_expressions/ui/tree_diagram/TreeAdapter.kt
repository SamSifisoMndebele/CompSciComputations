package com.compscicomputations.polish_expressions.ui.tree_diagram

import android.content.res.ColorStateList
import androidx.recyclerview.widget.RecyclerView
import android.view.ViewGroup
import android.view.LayoutInflater
import android.view.View
import com.compscicomputations.polish_expressions.R
import android.widget.TextView
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.graphics.toArgb
import com.compscicomputations.polish_expressions.ui.tree_diagram.graphview.AbstractGraphAdapter

class TreeAdapter(private val colorScheme: ColorScheme) : AbstractGraphAdapter<RecyclerView.ViewHolder>() {
    override fun getItemViewType(position: Int): Int {
        return if (getNodeOperand(position)!!.isNotEmpty()) NODE_VIEW else PARENT_VIEW
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View?
        return if (viewType == NODE_VIEW) {
            view = LayoutInflater.from(parent.context).inflate(R.layout.node, parent, false)
            view.backgroundTintList = ColorStateList.valueOf(colorScheme.primary.toArgb())
            ViewHolderNode(view)
        } else {
            view = LayoutInflater.from(parent.context).inflate(R.layout.parent_node, parent, false)
            view.backgroundTintList = ColorStateList.valueOf(colorScheme.primary.toArgb())
            ViewHolderParentNode(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == NODE_VIEW) {
            holder as ViewHolderNode

            holder.charTextView.text = getNodeOperand(position).toString()
            holder.charTextView.setTextColor(colorScheme.onPrimary.toArgb())

        } else {
            holder as ViewHolderParentNode

            holder.prefixTextView.text = getNodePrefix(position)
            holder.postfixTextView.text = getNodePostfix(position)
            holder.prefixTextView.setTextColor(colorScheme.onPrimary.toArgb())
            holder.postfixTextView.setTextColor(colorScheme.onPrimary.toArgb())
        }
    }

    //**************** NODE VIEW HOLDER 1 ******************//
    inner class ViewHolderNode internal constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var charTextView: TextView = itemView.findViewById(R.id.character)
    }
    //**************** PARENT NODE VIEW HOLDER ******************//
    inner class ViewHolderParentNode(itemView: View) : RecyclerView.ViewHolder(itemView){
        var prefixTextView: TextView = itemView.findViewById(R.id.prefix)
        var postfixTextView: TextView = itemView.findViewById(R.id.postfix)
    }

    companion object {
        private const val NODE_VIEW = 0
        private const val PARENT_VIEW = 1
    }
}