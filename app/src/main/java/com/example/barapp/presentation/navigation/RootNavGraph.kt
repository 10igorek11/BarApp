package com.example.barapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.barapp.domain.usecases.auth.GetLoggedInUserUseCase
import com.example.barapp.presentation.screens.auth.LoginScreen
import com.example.barapp.presentation.screens.home.HomeScreen

@Composable
fun RootNavigationGraph(navController: NavHostController, getLoggedInUserUseCase: GetLoggedInUserUseCase) {
    val context = LocalContext.current
    val authUser = getLoggedInUserUseCase()
    NavHost(
        navController = navController,
        route = Graph.ROOT,
        startDestination = if(authUser==null) Graph.START else Graph.HOME
    ) {
        composable(route = Graph.HOME) {
            HomeScreen {
                navController.navigate(Graph.START)
            }
        }
        composable(route = Graph.START) {
            LoginScreen {
                navController.navigate(Graph.HOME)
            }
        }
    }
}

object Graph {
    const val ROOT = "root_graph"
    const val START = "start_graph"
    const val HOME = "home_graph"
    const val DETAILS = "details_graph"
}