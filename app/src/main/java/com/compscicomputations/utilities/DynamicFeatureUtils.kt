package com.compscicomputations.utilities

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.currentComposer

object DynamicFeatureUtils {

    @Composable
    fun dfFirstScreen(paddingValues: PaddingValues): Boolean {
        return loadDF(
            paddingValues = paddingValues,
            className = DynamicFeaturePackageFactory.DFFirst.DF_FIRST_SCREEN,
            methodName = DynamicFeaturePackageFactory.DFFirst.COMPOSE_METHOD_NAME
        )
    }

    @Composable
    fun dfSecondScreen(paddingValues: PaddingValues): Boolean {
        return loadDF(
            paddingValues = paddingValues,
            className = DynamicFeaturePackageFactory.DFSecond.DF_SECOND_SCREEN,
            methodName = DynamicFeaturePackageFactory.DFSecond.COMPOSE_METHOD_NAME
        )
    }

    @Composable
    fun dfThirdScreen(paddingValues: PaddingValues): Boolean {
        return loadDF(
            paddingValues = paddingValues,
            className = DynamicFeaturePackageFactory.DFThird.DF_THIRD_SCREEN,
            methodName = DynamicFeaturePackageFactory.DFThird.COMPOSE_METHOD_NAME
        )
    }

    @Composable
    private fun loadDF(
        paddingValues: PaddingValues,
        className: String,
        methodName: String,
        objectInstance: Any = Any()
    ): Boolean {
        val dfClass = loadClassByReflection(className)
        if (dfClass != null) {
            val composer = currentComposer
            val method = findMethodByReflection(
                dfClass,
                methodName
            )
            if (method != null) {
                val isMethodInvoked =
                    invokeMethod(method, objectInstance, paddingValues, composer, 0)
                if (!isMethodInvoked) {
                    DFNotFoundScreen(paddingValues = paddingValues)
                    return false
                }
                return true
            } else {
                DFNotFoundScreen(paddingValues = paddingValues)
                return false
            }
        } else {
            DFNotFoundScreen(paddingValues = paddingValues)
            return false
        }
    }


}