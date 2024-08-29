package com.compscicomputations.utils

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.compscicomputations.R
import com.compscicomputations.data.source.local.AiResponse
import com.compscicomputations.data.source.local.AiResponseDao
import com.compscicomputations.databinding.AiBottomSheetBinding
import com.compscicomputations.di.FeaturesModules
import com.compscicomputations.ui.main.dynamic_feature.GenerateContentUseCase
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.textview.MaterialTextView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class AIStepsSheetDialog: BottomSheetDialogFragment() {
    private lateinit var generateContent: GenerateContentUseCase
    private lateinit var aiResponseDao: AiResponseDao

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = AiBottomSheetBinding.inflate(inflater, container, false).root

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val generativeModel = FeaturesModules.provideGenerativeModel()
        generateContent = FeaturesModules.provideGenerateContentUseCase(generativeModel)
        val aiDatabase = FeaturesModules.provideAiDatabase(requireContext())
        aiResponseDao = FeaturesModules.provideAiResponseDao(aiDatabase)

        dialog?.setOnShowListener { dialogInterface ->
            (dialogInterface as BottomSheetDialog)
                .findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
                ?.let {
                    val behavior = BottomSheetBehavior.from(it)
                    behavior.state = BottomSheetBehavior.STATE_EXPANDED
                }
        }

        val expression = requireArguments().getString("expression") ?: ""
        val tap = requireArguments().getInt("tap")

        runBlocking(Dispatchers.IO) {
            try {
                val aiResponse = aiResponseDao.select("karnaugh_maps", tap.toString(), "FIELD", expression)
                if (aiResponse != null) {

                    val textView = view?.findViewById<MaterialTextView>(R.id.generated_text)
                    textView?.text = aiResponse.text
                    textView?.visibility = View.VISIBLE
//                binding.regenerateButton.visibility = View.VISIBLE
                    view?.findViewById<LinearLayout>(R.id.progress_layout)?.visibility = LinearLayout.GONE

                    return@runBlocking
                }
                val newAiResponse = generateContent.invoke(
                    "karnaugh_maps",
                    tap.toString(),
                    "FIELD",
                    expression,
                    "Simplify the boolean expression: $expression using theorems."
                )
                val textView = view?.findViewById<MaterialTextView>(R.id.generated_text)
                textView?.text = newAiResponse.text
                textView?.visibility = View.VISIBLE
//                binding.regenerateButton.visibility = View.VISIBLE
                view?.findViewById<LinearLayout>(R.id.progress_layout)?.visibility = LinearLayout.GONE
            } catch (e: Exception) {
                Log.e(TAG, "generativeModel::error", e)
            }
        }
        return super.onCreateDialog(savedInstanceState)
    }


    companion object {
        const val TAG = "AIStepsSheet"
    }
}