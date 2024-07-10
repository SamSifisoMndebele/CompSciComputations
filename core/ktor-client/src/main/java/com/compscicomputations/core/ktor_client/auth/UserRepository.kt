package com.compscicomputations.core.ktor_client.auth

import com.compscicomputations.core.ktor_client.auth.models.AuthUser
import com.compscicomputations.core.ktor_client.auth.models.DynamicFeature
import com.compscicomputations.core.ktor_client.auth.models.User

interface UserRepository {
    /**
     * @return [AuthUser] the currently signed-in FirebaseUser or null if there is none.
     */
    fun getAuthUser(): AuthUser?

    /**
     * @return [User] the currently signed-in user information or null if there is none.
     */
    suspend fun getUser(): User?

    /**
     * @return a set of [DynamicFeature] the currently signed-in user information or null if there is none.
     */
    suspend fun getDynamicFeatures(): Set<DynamicFeature> {
        return featuresList
    }

    companion object {
        private val featuresList = setOf(
            DynamicFeature(
                "Number Systems",
                "number_systems",
                "NumberSystemsKt",
                "NumberSystemsScreen",
//                "ic_number_64"
            ),
            DynamicFeature(
                "Polish Expressions",
                "feature.polish_expressions",
                "PolishExpressionsKt",
                "PolishExpressionsScreen",
//                "ic_abc"
            ),
            DynamicFeature(
                "Karnaugh Maps",
                "feature.karnaugh_maps",
                "KarnaughActivity",
                null,
//                "ic_grid"
            ),
            DynamicFeature(
                "Matrix Methods",
                "feature.matrix_methods",
                "MatrixMethodsKt",
                "MatrixMethodsScreen",
//                "ic_matrix"
            ),

//    Feature(4, "Lexical & Syntax Analyzer", "lex_analyzer", "Activity", null, R.drawable.ic_software,),
//    Feature(4, "Semantic Analyzer", "sem_analyzer", "Activity", null, R.drawable.ic_software,),
//    Feature(4, "Code Generator", "code_analyzer", "Activity", null, R.drawable.ic_software,),
        ).sortedBy { it.title }.toSet()
    }
}