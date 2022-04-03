package com.example.githublist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navArgument
import androidx.navigation.compose.rememberNavController
import com.example.githublist.gitrepolist.GitListScreen
import com.example.githublist.ui.theme.JetpackComposeGitListTheme
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetpackComposeGitListTheme {
                val navController = rememberNavController()
                NavHost(
                    navController = navController,
                    startDestination = "git_list_screen"
                ) {
                    composable("git_list_screen") {
                        GitListScreen(navController = navController)
                    }

                }
            }

        }
    }
}
