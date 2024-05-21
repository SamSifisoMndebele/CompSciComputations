package com.compscicomputations.data

import com.compscicomputations.R
import com.compscicomputations.data.model.Feature

val featuresList = listOf(
    Feature(0, "Number Systems", "number_systems", "NumberSystemsKt", "NumberSystemsScreen"),
    Feature(1, "Polish Expressions", "polish_expressions", "PolishExpressionsKt", "PolishExpressionsScreen"),
    Feature(2, "Karnaugh Maps", "karnaugh_maps", "KarnaughActivity", null),
    Feature(3, "Matrix Methods", "matrix_methods", "MatrixMethodsKt", "MatrixMethodsScreen"),
)

val featuresIcons = listOf(
    R.drawable.ic_number_64,
    R.drawable.ic_abc,
    R.drawable.ic_grid,
    R.drawable.ic_matrix
)