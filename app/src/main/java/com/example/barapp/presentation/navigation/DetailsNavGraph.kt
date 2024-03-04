package com.example.barapp.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.example.barapp.presentation.screens.learning.material_edit_screen.LearningMaterialEditScreen
import com.example.barapp.presentation.screens.learning.material_screen.LearningMaterialScreen
import com.example.barapp.presentation.screens.techcard.techcard_edit_recipe.TechCardEditScreen
import com.example.barapp.presentation.screens.techcard.techcard_screen.TechCardScreen
import com.example.barapp.presentation.screens.techcard.type.CocktailTypesScreen
import com.example.barapp.presentation.screens.tests.test_edit_screen.TestEditScreen
import com.example.barapp.presentation.screens.tests.test_screen.TestScreen
import com.example.barapp.presentation.screens.users.user.UserScreen

fun NavGraphBuilder.detailsNavGraph(navController: NavHostController) {
    navigation(
        route = Graph.DETAILS,
        startDestination = DetailsScreen.Overview.route,
    ) {
        composable(route = DetailsScreen.Information.route) {
            ScreenContent(name = DetailsScreen.Information.route) {
                navController.popBackStack()
            }
        }
        composable(
            route = DetailsScreen.UserScreen.route,
            arguments = listOf(
                navArgument(name = "id") {
                    type = NavType.IntType
                }
            )
        ) {
            val id = it.arguments?.getInt("id")
            if(id==null){
                navController.navigate(Graph.HOME)
            }
            else{
                UserScreen(navController = navController, userId = id)
            }
        }
        composable(
            route = DetailsScreen.TestScreen.route,
            arguments = listOf(
                navArgument(name = "id") {
                    type = NavType.IntType
                }
            )
        ) {
            val id = it.arguments?.getInt("id")
            if(id==null){
                navController.navigate(Graph.HOME)
            }
            else{
                TestScreen(navController = navController, testId = id)
            }
        }
        composable(route = DetailsScreen.TestEditScreen.route,
            arguments = listOf(
                navArgument(name = "id") {
                    type = NavType.IntType
                }
            )
        ) {
            val id = it.arguments?.getInt("id")
            if(id==null){
                navController.navigate(Graph.HOME)
            }
            else {
                TestEditScreen(navController = navController, testId = id)
            }
        }
        composable(route = DetailsScreen.LearningMaterialScreen.route,
            arguments = listOf(
                navArgument(name = "id") {
                    type = NavType.IntType
                }
            )
        ) {
            val id = it.arguments?.getInt("id")
            if(id==null){
                navController.navigate(Graph.HOME)
            }
            else {
                LearningMaterialScreen(navController = navController, materialId = id)
            }
        }
        composable(route = DetailsScreen.LearningMaterialEditScreen.route,
            arguments = listOf(
                navArgument(name = "id") {
                    type = NavType.IntType
                }
            )
        ) {
            val id = it.arguments?.getInt("id")
            if(id==null){
                navController.navigate(Graph.HOME)
            }
            else {
                LearningMaterialEditScreen(navController = navController, materialId = id)
            }
        }
        composable(route = DetailsScreen.TechCardScreen.route,
            arguments = listOf(
                navArgument(name = "id") {
                    type = NavType.IntType
                }
            )
        ) {
            val id = it.arguments?.getInt("id")
            if(id==null){
                navController.navigate(Graph.HOME)
            }
            else {
                TechCardScreen(navController = navController, techCardId = id)
            }
        }
        composable(route = DetailsScreen.TechCardEditScreen.route,
            arguments = listOf(
                navArgument(name = "id") {
                    type = NavType.IntType
                }
            )
        ) {
            val id = it.arguments?.getInt("id")
            if(id==null){
                navController.navigate(Graph.HOME)
            }
            else {
                TechCardEditScreen(navController = navController, techCardId = id)
            }
        }
        composable(route = DetailsScreen.Information2.route) {
            ScreenContent(name = DetailsScreen.Information2.route) {
                navController.popBackStack()
            }
        }
        composable(route = DetailsScreen.TypesScreen.route) {
            CocktailTypesScreen(navController = navController)
        }
        composable(route = DetailsScreen.Overview.route) {
            ScreenContent(name = DetailsScreen.Overview.route) {
                navController.popBackStack(
                    route = DetailsScreen.Information.route,
                    inclusive = false,
                )
            }
        }
    }
}

sealed class DetailsScreen(val route: String) {
    object Information : DetailsScreen(route = "INFORMATION")
    object UserScreen: DetailsScreen(route = "USER_SCREEN/{id}")
    object TechCardScreen: DetailsScreen(route = "TECH_CARD_SCREEN/{id}")
    object TechCardEditScreen: DetailsScreen(route = "TECH_CARD_EDIT_SCREEN/{id}")
    object LearningMaterialScreen: DetailsScreen(route = "LEARNING_MATERIAL_SCREEN/{id}")
    object LearningMaterialEditScreen: DetailsScreen(route = "LEARNING_MATERIAL_EDIT_SCREEN/{id}")
    object TestScreen: DetailsScreen(route = "TEST_SCREEN/{id}")
    object TestEditScreen : DetailsScreen(route = "TEST_EDIT_SCREEN/{id}")
    object TypesScreen : DetailsScreen(route = "TYPES_SCREEN")
    object Information2 : DetailsScreen(route = "INFORMATION2")
    object Overview : DetailsScreen(route = "OVERVIEW")
}
