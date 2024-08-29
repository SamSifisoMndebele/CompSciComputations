package com.compscicomputations.ui.main.dynamic_feature

import android.util.Log
import com.compscicomputations.data.source.local.AiResponse
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.ServerException
import com.google.ai.client.generativeai.type.content
import javax.inject.Inject

class GenerateContentUseCase @Inject constructor(
    private val generativeModel: GenerativeModel
) {
    class AiServerException(message: String?, cause: Throwable? = null): Exception(message, cause)

    /**
     * Generate content for all feature modules
     * @param module the feature module name.
     * @param currentTab the current tab on the feature.
     * @param convertFrom the type of value you converted from, eg Binary.
     * @param value the string value you are converted.
     * @param aiText the ai paragraph for generating content.
     * @throws AiServerException
     * @throws Exception
     */
    suspend operator fun invoke(
        module: String,
        currentTab: String,
        convertFrom: String,
        value: String,
        aiText: String
    ): AiResponse {
        val response = try {
            generativeModel.generateContent(
                content {
                    text(aiText)
                }
            )
        } catch (e: ServerException) {
            throw AiServerException(e.message, e.cause)
        }
        return response.text?.let { outputContent ->
            Log.d("AI::response", outputContent)
            AiResponse(
                module = module,
                currentTab = currentTab,
                convertFrom = convertFrom,
                value = value,
                text = outputContent
            )
        } ?: throw Exception("Empty content generated.")
    }
}