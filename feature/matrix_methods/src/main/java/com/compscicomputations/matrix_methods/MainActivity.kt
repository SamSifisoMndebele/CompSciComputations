package com.compscicomputations.matrix_methods

import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.compscicomputations.CSCActivity
import com.compscicomputations.matrix_methods.classes.Matrix.toExpressionMatrix
import com.compscicomputations.matrix_methods.classes.Methods.salveByGaussJordanElimination
import com.compscicomputations.matrix_methods.ui.MatrixMethods
import com.compscicomputations.theme.CompSciComputationsTheme

class MainActivity : CSCActivity() {
    private lateinit var view : LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContent {
//            val themeState by themeState.collectAsStateWithLifecycle()
//            CompSciComputationsTheme(themeState) {
//                MatrixMethods(
//                    navigateUp = {
//                        finish()
//                    }
//                )
//            }
//        }

        view = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
        }
        setContentView(view)
        view.orientation = LinearLayout.VERTICAL

        /*val a = arrayOf(
            doubleArrayOf(2.0, -3.0, 1.0),
            doubleArrayOf(-1.0, 2.0, -1.0),
            doubleArrayOf(-1.0, 3.0, -2.0)
        ).toExpressionMatrix()
        val b = arrayOf(
            doubleArrayOf(5.0),
            doubleArrayOf(0.0),
            doubleArrayOf(4.0)
        ).toExpressionMatrix()*/
        val a = arrayOf(
            doubleArrayOf(1.0, 1.0, 0.0),
            doubleArrayOf(2.0, 1.0, 1.0),
            doubleArrayOf(4.0, 3.0, 1.0)
        ).toExpressionMatrix()
        val b = arrayOf(
            doubleArrayOf(1.0),
            doubleArrayOf(10.0),
            doubleArrayOf(21.0)
        ).toExpressionMatrix()

        view.salveByGaussJordanElimination(a,b)
    }
}