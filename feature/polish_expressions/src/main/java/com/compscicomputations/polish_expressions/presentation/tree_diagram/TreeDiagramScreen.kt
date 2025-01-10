package com.compscicomputations.polish_expressions.presentation.tree_diagram

import android.graphics.Color
import android.graphics.CornerPathEffect
import android.graphics.Paint
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidViewBinding
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.compscicomputations.polish_expressions.databinding.FragmentTreeDiagramBinding
import com.compscicomputations.polish_expressions.domain.graphview.TreeAdapter
import com.compscicomputations.polish_expressions.domain.graphview.layouts.StraightEdgeDecoration
import com.compscicomputations.polish_expressions.domain.graphview.layouts.TreeConfiguration
import com.compscicomputations.polish_expressions.domain.graphview.layouts.TreeLayoutManager

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
