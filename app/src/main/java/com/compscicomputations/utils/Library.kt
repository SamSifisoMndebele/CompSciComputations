package com.compscicomputations.utils

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import kotlin.math.roundToInt

infix fun CharSequence.notMatches(regex: Regex): Boolean = !regex.matches(this)

fun NavController.navigate(
    route: String,
    args: Bundle,
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null
) {
    val nodeId = graph.findNode(route = route)?.id
    if (nodeId != null) {
        navigate(nodeId, args, navOptions, navigatorExtras)
    } else {
        navigate(route, navOptions, navigatorExtras)
    }
}

fun cardXXGen(cardNumber: String):String{
    return "**** **** **** ${cardNumber.takeLast(4)}"
}

fun Double.toRandString() : String {
    val number : Int = (this * 100).roundToInt()
    if (number == 0) return "R0.00"
    val wholeNum = number/100
    val decNum = "0$number".takeLast(2)
    return "R$wholeNum.$decNum"
}

internal inline val Context.asActivity: Activity
    get() {
        var context = this
        while (context is ContextWrapper) {
            if (context is Activity) return context
            context = context.baseContext
        }
        throw IllegalStateException("Permissions should be called in the context of an Activity")
    }