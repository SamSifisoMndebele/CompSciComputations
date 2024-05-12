package com.ssmnd.karnaughmap

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ssmnd.karnaughmap.logic.Karnaugh4Variables
import com.ssmnd.karnaughmap.utils.binaryToDecimal

private fun executeKarnaugh(viewModel: Karnaugh4ViewModel, iArr: IntArray, iArr2: IntArray?) {
    viewModel.setAnswers(Karnaugh4Variables(iArr, iArr2).executeKarnaugh())
}
private fun Char.position(): Int{
    return if (this=='A') 0
    else if (this=='B') 1
    else if (this=='C') 2
    else if (this=='D') 3
    else -1
}

private fun simplifyExpression(exp : String): IntArray {
    val list = mutableListOf<String>()

    val split = exp.split("+")
    for (each in split){
        val product = each.trim()

        val charArray = charArrayOf('-','-','-','-')
        for (i in product.indices){
            if(product[i] == '\''){
                charArray[product[i-1].position()] = '0'
            } else {
                charArray[product[i].position()] = '1'
            }
        }

        var arr = arrayOf("0000", "0001", "0010", "0011", "0100", "0101", "0110", "0111", "1000", "1001", "1010", "1011", "1100", "1101", "1110", "1111")

        val bin = charArray.joinToString("")
        for (j in 0..3){
            if (bin[j].isDigit()){
                val temp = mutableListOf<String>()
                for (elem in arr){
                    if (bin[j] == elem[j]){
                        temp.add(elem)
                    }
                }
                arr = temp.toTypedArray()
            }
        }

        for (elem in arr.distinct()){
            list.add(elem)
        }
    }

    val decimal = try {
        list.distinct().joinToString(" ").binaryToDecimal.trim().split(" ")
    } catch (e: Exception) {
        listOf()
    }

    val listOfMinTerms = mutableListOf<Int>()
    for (int in decimal){
        try {
            listOfMinTerms.add(int.toInt())
        } catch (e: Exception){}
    }

    val minTerms = listOfMinTerms.toIntArray()
    //minTermsDao.insert(MinTerms(minTerms.joinToString(" "), 4))

    return minTerms
//    binding.kMap.setMinTerms(minTerms, iArr2)
//    executeKarnaugh(minTerms, iArr2)
}

@SuppressLint("ClickableViewAccessibility")
@Composable
fun Karnaugh4() {
    val viewModel: Karnaugh4ViewModel = viewModel()
    val prefs : SharedPreferences = LocalContext.current.getSharedPreferences("KarnaughMaps", Context.MODE_PRIVATE)
    Karnaugh(
        viewModel.booleanExp, 4,
        answers = viewModel.answers,
        selectedAnswer = viewModel.selectedAnswer
    )  {
        Image(
            modifier = Modifier.fillMaxWidth().wrapContentHeight(),
            painter = painterResource(id = R.drawable.kmap_4_var_cdab),
            contentDescription = "4 Var K Map",
            contentScale = ContentScale.FillWidth
        )
       /*AndroidView(
           modifier = Modifier
               .padding(vertical = 8.dp)
               .fillMaxWidth()
               .focusable(true)
               .wrapContentHeight(),
           factory = {
               val map = KMap4VariablesImageView(it).apply {
                   adjustViewBounds = true
               }
               *//*map.setOnTouchListener {  kMap, motionEvent ->
                   if (motionEvent.action == 0) {
                       true
                   } else if (motionEvent.action != 1) {
                       false
                   } else {
                       val x = motionEvent.x / kMap.width.toDouble()
                       val y = motionEvent.y / kMap.height.toDouble()
                       map.checkInversionBtn(x, y)
                       val findClosestMinTerm = map.findClosestMinterm(x, y)
                       if (findClosestMinTerm > -1) {
                           map.setMinterms(findClosestMinTerm)

                           //executeKarnaugh(viewModel, map.minterms, map.dontCares)
                       }
                       false
                   }
               }*//*

               map
           }
       ) {

       }*/
   }
}

