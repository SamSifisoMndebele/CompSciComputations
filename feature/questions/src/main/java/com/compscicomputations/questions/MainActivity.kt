package com.compscicomputations.questions

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.compscicomputations.CSCActivity
import com.compscicomputations.questions.ui.details.DetailsScreen
import com.compscicomputations.questions.ui.details.DetailsViewModel
import com.compscicomputations.questions.ui.list.QuestionsScreen
import com.compscicomputations.questions.ui.list.QuestionsViewModel
import com.compscicomputations.theme.CompSciComputationsTheme

class MainActivity : CSCActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val userId = intent.getStringExtra("userId")!!
        val userName = intent.getStringExtra("userName")!!
        val questionsViewModel = QuestionsViewModel(userId, userName)

        setContent {
            val themeState by themeState.collectAsStateWithLifecycle()
            CompSciComputationsTheme(themeState) {
                val navController = rememberNavController()
                NavHost(
                    modifier = Modifier.fillMaxSize(),
                    navController = navController,
                    startDestination = QuestionList
                ) {
                    composable<QuestionList> {
                        QuestionsScreen(
                            viewModel = questionsViewModel,
                            navigateUp = { finish() },
                            navigateToDetails = { question ->
                                navController.navigate(route = question)
                            }
                        )
                    }
                    composable<Question> { backStackEntry ->
                        val question: Question = backStackEntry.toRoute()
                        val viewModel = DetailsViewModel(question)
                        DetailsScreen(
                            viewModel = viewModel,
                            navigateUp = { navController.navigateUp() },
                        )
                    }
                }
            }
        }
    }
}
