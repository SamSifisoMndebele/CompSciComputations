package com.ssmnd.karnaughmap.ui

import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.fragment.app.Fragment
import com.ssmnd.karnaughmap.MinTermTextView
import com.ssmnd.karnaughmap.R
import com.ssmnd.karnaughmap.kMapView.KMapVariablesImageView
import com.ssmnd.karnaughmap.utils.ListOfMinterms

open class KarnaughFragment : Fragment() {
    var answers: ArrayList<ListOfMinterms>? = null
    lateinit var rootView: View
    private var selectedAnswer: ListOfMinterms? = null
    fun setAnswerTextView(i: Int) {
        val minTermTextView = rootView.findViewById<MinTermTextView>(R.id.answerTextView)
        minTermTextView.setSop(true)
        minTermTextView.setText(answers!![i].toString())
    }

    fun setAnswerView(i: Int) {
        val minTermTextView = rootView.findViewById<MinTermTextView>(R.id.answerTextView)
        val kMapVariablesImageView = rootView.findViewById<KMapVariablesImageView>(R.id.kMap)
        setAnswerTextView(i)
        selectedAnswer = answers!![i]
        kMapVariablesImageView.setDrawGroups(answers!![i])
        kMapVariablesImageView.setOnKmapAnimationListener(object :
            KMapVariablesImageView.OnKmapAnimationListener {
            override fun onAnimate() {
                minTermTextView.setAnimation(true)
                minTermTextView.setAnimationPart(0)
                setAnswerTextView(i)
            }

            override fun stopAnimate() {
                minTermTextView.setAnimation(false)
                setAnswerTextView(i)
            }

            override fun onTick(i: Int) {
                println("animationPart $i")
                minTermTextView.setAnimationPart(i)
                setAnswerTextView(i)
            }
        })
    }

    /*fun refresh() {
        setAnswerView(0)
    }*/

    fun setAnswersSpinner() {
        val spinner = rootView.findViewById<Spinner>(R.id.answerSpinner)
        val arrayList = ArrayList<Any?>()
        var i = 0
        while (i < answers!!.size) {
            i++
            arrayList.add("Answer " + i + " of " + answers!!.size)
        }
        val arrayAdapter: ArrayAdapter<*> = ArrayAdapter<Any?>(rootView.context, R.layout.spinner_item, arrayList)
        arrayAdapter.setDropDownViewResource(R.layout.spinner_item)
        spinner.onItemSelectedListener = null
        spinner.adapter = arrayAdapter
        spinner.setSelection(0, false)
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(adapterView: AdapterView<*>?) {}
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, j: Long) {
                setAnswerView(i)
            }
        }
    }
}