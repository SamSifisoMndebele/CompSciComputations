package com.compscicomputations.data

import com.compscicomputations.R
import com.compscicomputations.core.database.model.Feature

val featuresList = listOf(
    Feature(0, "Number Systems", "feature.number_systems", "NumberSystemsKt", "NumberSystemsScreen"),
    Feature(1, "Polish Expressions", "feature.polish_expressions", "PolishExpressionsKt", "PolishExpressionsScreen"),
    Feature(2, "Karnaugh Maps", "feature.karnaugh_maps", "KarnaughActivity", null),
    Feature(3, "Matrix Methods", "feature.matrix_methods", "MatrixMethodsKt", "MatrixMethodsScreen"),
//    Feature(2, "Karnaugh Maps", "karnaugh_maps", "KarnaughScreenKt", "KarnaughScreen"),
//    Feature(4, "Lexical & Syntax Analyzer", "lex_analyzer", "Activity", null),
//    Feature(4, "Semantic Analyzer", "sem_analyzer", "Activity", null),
//    Feature(4, "Code Generator", "code_analyzer", "Activity", null),
).sortedBy { it.title }

val featuresIcons = listOf(
    R.drawable.ic_number_64,
    R.drawable.ic_abc,
    R.drawable.ic_grid,
    R.drawable.ic_matrix,
    R.drawable.ic_software,
)