package com.compscicomputations.keyboard.model

sealed interface Key {
    val value: String
    val span: Int
}