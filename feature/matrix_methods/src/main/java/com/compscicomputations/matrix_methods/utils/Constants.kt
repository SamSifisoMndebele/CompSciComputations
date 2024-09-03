package com.compscicomputations.matrix_methods.utils

object Constants {
    fun xVariables(size: Int = 6): Array<String>{
        val list = mutableListOf<String>()
        for (i in 0 until size){
            list.add("x_${i+1}")
        }
        return list.toTypedArray()
    }
    fun yVariables(size: Int = 6): Array<String>{
        val list = mutableListOf<String>()
        for (i in 0 until size){
            list.add("y_${i+1}")
        }
        return list.toTypedArray()
    }
}