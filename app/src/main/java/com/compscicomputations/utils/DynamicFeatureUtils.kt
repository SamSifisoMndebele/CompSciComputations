package com.compscicomputations.utils

import android.text.TextUtils
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.currentComposer
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.lang.reflect.Method
import java.lang.reflect.Modifier

@Composable
fun LoadDynamicFeature(
    className: String,
    methodName: String,
    objectInstance: Any = Any(),
    navigateUp: () -> Unit,
): Boolean {
    val dfClass = loadClassByReflection(className)
    if (dfClass != null) {
        val method = findMethodByReflection(dfClass, methodName)
        if (method != null) {
            val isMethodInvoked = invokeMethod(method, objectInstance, navigateUp, currentComposer, 0)
            if (!isMethodInvoked) DFNotFoundScreen()
            return isMethodInvoked
        } else {
            DFNotFoundScreen()
            return false
        }
    } else {
        DFNotFoundScreen()
        return false
    }
}

private fun loadClassByReflection(className: String): Class<*>? {
    return try {
        val classLoader = ::loadClassByReflection.javaClass.classLoader
        classLoader?.loadClass(className)
    } catch (e: Throwable) {
        null
    }
}
private fun findMethodByReflection(classMethod: Class<*>?, methodName: String): Method? {
    return try {
        if (!TextUtils.isEmpty(methodName)) {
            classMethod?.let { clazz ->
                clazz.methods.find { it.name.equals(methodName) && Modifier.isStatic(it.modifiers) }
            } ?: run {
                null
            }
        } else {
            null
        }
    }catch (e:Throwable){
        null
    }
}
private fun invokeMethod(method: Method, obj: Any, vararg args: Any): Boolean {
    return try {
        method.invoke(obj,*(args))
        true
    } catch (e: Throwable) {
        false
    }
}

@Composable
private fun DFNotFoundScreen() {
    Column(
        modifier = androidx.compose.ui.Modifier
            .background(Color.Red)
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Dynamic feature not found.",
            color = Color.White,
            fontSize = 20.sp,
            modifier = androidx.compose.ui.Modifier.padding(all = 20.dp)
        )
    }
}