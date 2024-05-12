package com.ssmnd.karnaughmap

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ssmnd.karnaughmap.logic.Karnaugh3Variables

@SuppressLint("ClickableViewAccessibility")
@Composable
fun Karnaugh3() {
    val viewModel: Karnaugh3ViewModel = viewModel()
    Karnaugh(
        viewModel.booleanExp, 3,
        answers = viewModel.answers,
        selectedAnswer = viewModel.selectedAnswer
    ) {
        Image(
            modifier = Modifier.fillMaxWidth().wrapContentHeight(),
            painter = painterResource(id = R.drawable.kmap_3_var_cab),
            contentDescription = "3 Var K Map",
            contentScale = ContentScale.FillWidth
        )
//        AndroidView(
//            modifier = Modifier
//                .fillMaxWidth()
//                .wrapContentHeight(),
//            factory = {
//                KMap3VariablesImageView(it).apply {
//                    layoutParams = ViewGroup.LayoutParams(
//                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
//                    adjustViewBounds = true
//                }
//            }
//        ) { kMap ->
//            val initArrayString = prefs.getString("min_terms_3var", null)
//            if (initArrayString != null){
//                val initArray = initArrayString.split(" ")
//                val list = mutableListOf<Int>()
//                for (int in initArray){
//                    try {
//                        list.add(int.toInt())
//                    }catch (_: Exception){}
//                }
//                val iArr = list.toIntArray()
//                val iArr2 = IntArray(0)
//                kMap.setMinTerms(iArr, iArr2)
//                executeKarnaugh(viewModel, iArr, iArr2)
//            }
//
//            kMap.setOnTouchListener { kMap, motionEvent ->
//                if (motionEvent.action == 0) {
//                    true
//                } else if (motionEvent.action != 1) {
//                    false
//                } else {
//                    val x = (motionEvent.x / kMap.width.toFloat()).toDouble()
//                    val y = (motionEvent.y / kMap.height.toFloat()).toDouble()
//
//                    //kMap.checkInversionBtn(x, y)
//                    *//*val findClosestMinTerm = kMap.findClosestMinterm(x, y)
//                    if (findClosestMinTerm > -1) {
//                        kMap.setMinterms(findClosestMinTerm)
//                        prefs.edit().putString("min_terms_3var", kMap.minterms.joinToString(" "))
//                            .apply()
//                        try {
//                            executeKarnaugh(viewModel, kMap.minterms, kMap.dontCares)
//                        } catch (_: Exception) {
//                        }
//                    }*//*
//                    false
//                }
//            }
//        }
    }
}
private fun executeKarnaugh(viewModel: Karnaugh3ViewModel, iArr: IntArray, iArr2: IntArray?) {
    viewModel.setAnswers(Karnaugh3Variables(iArr, iArr2).executeKarnaugh())
}