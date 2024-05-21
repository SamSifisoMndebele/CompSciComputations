package com.compscicomputations.karnaugh_maps

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.core.widget.doOnTextChanged
import com.compscicomputations.karnaugh_maps.databinding.Karnaugh2VariablesBinding
import com.compscicomputations.karnaugh_maps.logic.Karnaugh2Variables
import com.compscicomputations.karnaugh_maps.utils.binaryToDecimalList

class Karnaugh2Fragment : KarnaughFragment() {

    private lateinit var binding: Karnaugh2VariablesBinding
    private lateinit var prefs :SharedPreferences
    
    private fun executeKarnaugh(iArr: IntArray, iArr2: IntArray?) {
        answers = Karnaugh2Variables(iArr, iArr2).executeKarnaugh()
        setAnswersSpinner()
        setAnswerView(0)
    }

    override fun onCreateView(
        layoutInflater: LayoutInflater,
        viewGroup: ViewGroup?,
        bundle: Bundle?
    ): View {
        binding = Karnaugh2VariablesBinding.inflate(layoutInflater, viewGroup, false)
        rootView = binding.root
        setHasOptionsMenu(true)
        return binding.root
    }

    private fun Char.position(): Int{
        return if (this=='A') 0
        else if (this=='B') 1
        else -1
    }
    private fun simplifyExpression( exp : String) {
        val list = mutableListOf<String>()

        val split = exp.uppercase().split("+")
        for (each in split){
            val product = each.trim()

            val charArray = charArrayOf('-','-')
            for (i in product.indices){
                if(product[i] == '\''){
                    charArray[product[i-1].position()] = '0'
                } else {
                    charArray[product[i].position()] = '1'
                }
            }

            var arr = arrayOf("00", "01", "10", "11")

            val bin = charArray.joinToString("")
            for (j in 0..1){
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
            }catch (e: Exception){}
        }

        val minTerms = listOfMinTerms.toIntArray()
        val iArr2 = IntArray(0)
        prefs.edit().putString("min_terms_2var", minTerms.joinToString(" ")).apply()
        binding.kMap.setMinTerms(minTerms, iArr2)
        executeKarnaugh(minTerms, iArr2)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prefs = requireContext().getSharedPreferences("KarnaughMaps", Context.MODE_PRIVATE)

        val initArrayString = prefs.getString("min_terms_2var", null)
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
            binding.kMap.setMinTerms(iArr, iArr2)
            executeKarnaugh(iArr, iArr2)
        }

        binding.keyboard.setInputConnection(binding.expressionEditText.onCreateInputConnection(
            EditorInfo()
        ))
        binding.btnHideKeyboard.setOnClickListener {
            binding.expressionEditText.clearFocus()
            binding.keyboardLayout.visibility = View.GONE
        }
        binding.expressionEditText.showSoftInputOnFocus = false
        binding.expressionEditText.setOnFocusChangeListener { _, z ->
            if (z && binding.keyboardLayout.visibility != View.VISIBLE)
                binding.keyboardLayout.visibility = View.VISIBLE
            else if (!z && binding.keyboardLayout.visibility == View.VISIBLE)
                binding.keyboardLayout.visibility = View.GONE
        }
        binding.expressionEditText.doOnTextChanged { text, _, _, _ ->
            val expression = text.toString()
            if (text != null &&
                expression.isNotEmpty() &&
                !expression.startsWith("+") &&
                !expression.startsWith("'") &&
                !expression.endsWith("+")){
                try {
                    simplifyExpression(expression)
                } catch (e: Exception) {
                }
            }
        }

        binding.kMap.setOnTouchListener { kMap, motionEvent ->
            if (motionEvent.action == 0) {
                true
            } else if (motionEvent.action != 1) {
                false
            } else {
                val findClosestMinTerm = binding.kMap.findClosestMinterm(
                    (motionEvent.x / kMap.width.toFloat()).toDouble(),
                    (motionEvent.y / kMap.height.toFloat()).toDouble()
                )
                if (findClosestMinTerm > -1) {
                    binding.kMap.setMinterms(findClosestMinTerm)
                    prefs.edit().putString("min_terms_2var", binding.kMap.minterms.joinToString(" ")).apply()
                    executeKarnaugh(binding.kMap.minterms, binding.kMap.dontCares)
                }
                false
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.history_menu, menu)
    }

    override fun onOptionsItemSelected(menuItem: MenuItem): Boolean {
        val itemId = menuItem.itemId
        if (itemId == R.id.item_clear) {
            /*binding.kMap.setMinTerms(
                 binding.kMap.allMinTerms,
                 binding.kMap.noMinterms
             )
             executeKarnaugh(binding.kMap.minterms, binding.kMap.dontCares)*/
            binding.kMap.setMinTerms(
                binding.kMap.noMinterms,
                binding.kMap.noMinterms
            )
            prefs.edit().putString("min_terms_2var", null).apply()
            executeKarnaugh(binding.kMap.minterms, binding.kMap.dontCares)
        }
        return super.onOptionsItemSelected(menuItem)
    }
}