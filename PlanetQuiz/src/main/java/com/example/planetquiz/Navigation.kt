package com.example.planetquiz

import android.util.Log
import androidx.compose.runtime.*
import androidx.compose.ui.res.stringArrayResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigate
import androidx.navigation.compose.rememberNavController

@Composable
fun NavComposeApp() {
    var answer by remember { mutableStateOf("") }
    val navController = rememberNavController()
    val questions = stringArrayResource(id = R.array.questions)
    val answers = stringArrayResource(id = R.array.answers)
    val verifyCallback = remember {
        object {
            fun verify(q: String, a: String) {
                Log.d("Verifying", "Verifying $q $a")
                answer = when ("$q?") {
                    questions[0] -> {
                        if (a.startsWith("Jupiter", ignoreCase = true)) {
                            "CORRECT! "
                        } else {
                            "WRONG! "
                        } + answers[0]
                    }
                    questions[1] -> {
                        if (a.startsWith("Saturn", ignoreCase = true)) {
                            "CORRECT! "
                        } else {
                            "WRONG! "
                        } + answers[1]
                    }
                    questions[2] -> {
                        if (a.startsWith("Uranus", ignoreCase = true)) {
                            "CORRECT! "
                        } else {
                            "WRONG! "
                        } + answers[2]
                    }
                    else -> ""
                }
            }
        }
    }


    NavHost(navController, startDestination = "questions") {
        composable("questions") {
            Content(title = "Planet Quiz", list = questions.toList(), null) { question ->
                navController.navigate("options/$question")
                answer = ""
            }
        }
        composable("options/{question}") { backStackEntry ->
            Content(
                title = backStackEntry.arguments?.getString("question").toString() + "?",
                list = stringArrayResource(id = R.array.planets).toList(),
                answer
            ) {
                verifyCallback.verify(
                    backStackEntry.arguments?.getString("question").toString(),
                    it
                )
            }
        }
    }
}




