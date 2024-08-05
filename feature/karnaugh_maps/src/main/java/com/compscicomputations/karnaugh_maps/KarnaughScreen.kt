package com.compscicomputations.karnaugh_maps

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.viewinterop.AndroidViewBinding
import com.compscicomputations.karnaugh_maps.databinding.Karnaugh4VariablesBinding
import com.compscicomputations.karnaugh_maps.utils.binaryToDecimalList

enum class NumVariables(val string: String) {
    TWO("2 variables"),
    THREE("3 variables"),
    FOUR("4 variables")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KarnaughScreen(
    navigateUp: () -> Unit,
) {
    var numVariables by rememberSaveable { mutableStateOf(NumVariables.FOUR) }

    Column(
        modifier = Modifier.padding(start = 8.dp, end = 8.dp, top = 2.dp),
    ) {
        var variablesDropdown by remember { mutableStateOf(false) }
        ExposedDropdownMenuBox(
            expanded = variablesDropdown,
            onExpandedChange = { variablesDropdown = !variablesDropdown }
        ) {
            OutlinedTextField(
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor()
                    .padding(top = 8.dp)
                    .clickable { variablesDropdown = !variablesDropdown }
                    .focusable(false),
                value = numVariables.string,
                onValueChange = {},
                trailingIcon =  {
                    Icon(imageVector = if (variablesDropdown) Icons.Filled.KeyboardArrowUp
                    else Icons.Filled.KeyboardArrowDown, contentDescription = null)
                },
                shape = RoundedCornerShape(22.dp),
            )
            ExposedDropdownMenu(
                expanded = variablesDropdown,
                onDismissRequest = { variablesDropdown = false }
            ) {
                NumVariables.entries.forEach {
                    DropdownMenuItem(
                        text = { Text(text = it.string) },
                        onClick = {
                            numVariables = it
                            variablesDropdown = false
                        }
                    )
                }
            }
        }

        Karnaugh4FragmentInCompose()
        /*when(numVariables) {
            NumVariables.TWO -> Karnaugh2()
            NumVariables.THREE -> Karnaugh3()
            NumVariables.FOUR -> Karnaugh4()
        }*/

    }
}



private fun Char.position(): Int{
    return if (this=='A') 0
    else if (this=='B') 1
    else if (this=='C') 2
    else if (this=='D') 3
    else -1
}
private fun String.simplifyExpression(): IntArray {
    val list = mutableListOf<String>()

    val split = this.split("+")
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

    val decimal = list.binaryToDecimalList()

    val listOfMinTerms = mutableListOf<Int>()
    for (int in decimal){
        try {
            listOfMinTerms.add(int.toInt())
        }catch (_: Exception){}
    }

    return listOfMinTerms.toIntArray()
}


@SuppressLint("ClickableViewAccessibility")
@Composable
fun Karnaugh4FragmentInCompose() {
    val context = LocalContext.current
    AndroidViewBinding(Karnaugh4VariablesBinding::inflate) {
        val karnaugh4Fragment = this.expressionEditText
        expressionInput.setBoxStrokeColorStateList(ColorStateList.valueOf(Color.Gray.toArgb()))

        val prefs = context.getSharedPreferences("KarnaughMaps", Context.MODE_PRIVATE)

        val initArrayString = prefs.getString("min_terms_4var", null)
//        val minTerms = initArrayString.simplifyExpression()

//        prefs.edit().putString("min_terms_4var", minTerms.joinToString(" ")).apply()
//        kMap.setMinTerms(minTerms, IntArray(0))
//        executeKarnaugh(minTerms, IntArray(0))

//        expressionEditText.setText(expressionString)
        if (initArrayString != null){
            val initArray = initArrayString.split(" ")
            val list = mutableListOf<Int>()
            for (int in initArray){
                try {
                    list.add(int.toInt())
                }catch (e: Exception){}
            }
            val iArr = list.toIntArray()
            val iArr2 = IntArray(0)
            kMap.setMinTerms(iArr, iArr2)
//            executeKarnaugh(iArr, iArr2)
        }
    }

//    AndroidView(factory = {
//        val inflater = LayoutInflater.from(it)
//        val binding = Karnaugh4VariablesBinding.inflate(inflater)
//
//        val karnaugh4Fragment = Karnaugh4Fragment()
//
//
//
//        karnaugh4Fragment.rootView
//    })
}