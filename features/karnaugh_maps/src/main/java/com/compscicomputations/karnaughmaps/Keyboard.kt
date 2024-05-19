package com.compscicomputations.karnaughmaps

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.ExtractedTextRequest
import android.view.inputmethod.InputConnection
import android.widget.Button
import android.widget.LinearLayout
import androidx.recyclerview.widget.ItemTouchHelper

class Keyboard @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    i: Int = 0
) : LinearLayout(context, attributeSet, i), View.OnClickListener {
    private var inputConnection: InputConnection? = null
    private val keyValues: SparseArray<String> = SparseArray()
    private fun init(context: Context) {
        LayoutInflater.from(context).inflate(R.layout.karnaugh_keyboard, this, true)
        val button13 = findViewById<Button>(R.id.button_OR)
        button13.setOnClickListener(this)
        val button14 = findViewById<Button>(R.id.button_NOT)
        button14.setOnClickListener(this)
        val button15 = findViewById<Button>(R.id.button_A)
        button15.setOnClickListener(this)
        val button16 = findViewById<Button>(R.id.button_B)
        button16.setOnClickListener(this)
        val button18 = findViewById<Button>(R.id.button_C)
        button18.setOnClickListener(this)
        val button19 = findViewById<Button>(R.id.button_D)
        button19.setOnClickListener(this)
        val imageButton = findViewById<Button>(R.id.btnBackspace)
        imageButton.setOnClickListener(this)
        val imageButton2 = findViewById<Button>(R.id.btnBackward)
        imageButton2.setOnClickListener(this)
        val imageButton3 = findViewById<Button>(R.id.btnForward)
        imageButton3.setOnClickListener(this)
        imageButton.setOnLongClickListener {
            inputConnection!!.getSelectedText(0)
            inputConnection!!.deleteSurroundingText(
                ItemTouchHelper.Callback.DEFAULT_DRAG_ANIMATION_DURATION,
                0
            )
            inputConnection!!.commitText("", 1)
            true
        }
        keyValues.put(R.id.button_OR, "+")
        keyValues.put(R.id.button_NOT, "'")
        keyValues.put(R.id.button_A, "A")
        keyValues.put(R.id.button_A, "B")
        keyValues.put(R.id.button_C, "C")
        keyValues.put(R.id.button_D, "D")
    }

    override fun onClick(view: View) {
        if (inputConnection != null) {
            if (view.id == R.id.btnBackspace) {
                if (TextUtils.isEmpty(inputConnection!!.getSelectedText(0))) {
                    inputConnection!!.deleteSurroundingText(1, 0)
                } else {
                    inputConnection!!.commitText("", 1)
                }
            } else if (view.id == R.id.button_OR) {
                inputConnection!!.commitText("+", 1)
            } else if (view.id == R.id.button_NOT) {
                inputConnection!!.commitText("'", 1)
            } else if (view.id == R.id.button_A) {
                inputConnection!!.commitText("A", 1)
            } else if (view.id == R.id.button_B) {
                inputConnection!!.commitText("B", 1)
            } else if (view.id == R.id.button_C) {
                inputConnection!!.commitText("C", 1)
            } else if (view.id == R.id.button_D) {
                inputConnection!!.commitText("D", 1)
            } else if (view.id == R.id.btnBackward) {
                val i = inputConnection!!.getExtractedText(ExtractedTextRequest(), 0).selectionStart - 1
                inputConnection!!.setSelection(i, i)
            } else if (view.id == R.id.btnForward) {
                val i2 = inputConnection!!.getExtractedText(ExtractedTextRequest(), 0).selectionStart + 1
                inputConnection!!.setSelection(i2, i2)
            }
        }
    }

    fun setInputConnection(inputConnection2: InputConnection?) {
        inputConnection = inputConnection2
    }

    init {
        init(context)
    }
}