package com.compscicomputations.utils

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

fun buildMaterialName(materialTitle: String, materialNumber: String?, materialYear: String?) : String{
    return if (materialNumber == null) {
        if (materialYear == null) materialTitle
        else "$materialTitle $materialYear"
    }
    else {
        if (materialYear == null) "$materialTitle $materialNumber"
        else "$materialTitle $materialNumber $materialYear"
    }
}

fun cardXXGen(cardNumber: String):String{
    return "**** **** **** ${cardNumber.takeLast(4)}"
}

fun Float.to2DecimalString() : String {
    val number : Int = (this * 100f).roundToInt()
    if (number == 0) return "0.00"
    val wholeNum = number/100
    val decNum = number.toString().padStart(2,'0').takeLast(2)

    return "$wholeNum.$decNum"
}