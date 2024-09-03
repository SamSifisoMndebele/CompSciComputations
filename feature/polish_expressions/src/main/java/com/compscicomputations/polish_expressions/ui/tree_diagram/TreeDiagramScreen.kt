package com.compscicomputations.polish_expressions.ui.tree_diagram

import android.graphics.Color
import android.graphics.CornerPathEffect
import android.graphics.Paint
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.compscicomputations.polish_expressions.databinding.FragmentTreeDiagramBinding
import com.compscicomputations.polish_expressions.ui.trace_table.TraceTableUiState
import com.compscicomputations.polish_expressions.ui.trace_table.TraceTableViewModel
import com.compscicomputations.polish_expressions.ui.tree_diagram.graphview.layouts.StraightEdgeDecoration
import com.compscicomputations.polish_expressions.ui.tree_diagram.graphview.layouts.TreeConfiguration
import com.compscicomputations.polish_expressions.ui.tree_diagram.graphview.layouts.TreeLayoutManager
import com.compscicomputations.polish_expressions.ui.tree_diagram.graphview.models.Edge
import com.compscicomputations.polish_expressions.ui.tree_diagram.graphview.models.Graph
import com.compscicomputations.polish_expressions.ui.tree_diagram.graphview.models.Node
import java.util.Stack

@Composable
fun TreeDiagramScreen(
    viewModel: TreeDiagramViewModel,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    lateinit var edgeDecoration : StraightEdgeDecoration
    val context = LocalContext.current
    val colorScheme = MaterialTheme.colorScheme

    AndroidViewBinding(FragmentTreeDiagramBinding::inflate) {
        val configuration = TreeConfiguration.Builder()
            .setSiblingSeparation(100)
            .setLevelSeparation(100)
            .setSubtreeSeparation(100)
            .setOrientation(TreeConfiguration.ORIENTATION_TOP_BOTTOM)
            .build()

        treeRecyclerView.layoutManager = TreeLayoutManager(context, configuration)

        edgeDecoration = StraightEdgeDecoration(Paint(Paint.ANTI_ALIAS_FLAG).apply {
            strokeWidth = 8f
            color = Color.GRAY
            style = Paint.Style.STROKE
            strokeJoin = Paint.Join.ROUND
            pathEffect = CornerPathEffect(10f)
        })

        treeRecyclerView.addItemDecoration(edgeDecoration)
        TreeAdapter(colorScheme).apply {
            submitGraph(uiState.graph)
            treeRecyclerView.adapter = this
        }
    }
}
