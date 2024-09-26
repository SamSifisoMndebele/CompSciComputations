package com.compscicomputations.keyboard.model

sealed class AlphabetKey(
    override val value: String,
    override val span: Int = 2
) : Key {
    data object A : AlphabetKey("A")
    data object B : AlphabetKey("B")
    data object C : AlphabetKey("C")
    data object D : AlphabetKey("D")
//    object E : Alphabets(R.drawable.ic_e, "E")
//    object F : Alphabets(R.drawable.ic_f, "F")
}