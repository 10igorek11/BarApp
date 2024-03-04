package com.example.barapp.presentation.navigation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.example.barapp.presentation.screens.home.BottomBar
import com.example.barapp.presentation.screens.learning.LearningMaterialsScreen
import com.example.barapp.presentation.screens.techcard.TechCardsScreen
import com.example.barapp.presentation.screens.tests.TestsScreen
import com.example.barapp.presentation.screens.users.UsersScreen

@Composable
fun HomeNavGraph(navController: NavHostController, onBackBtnClick:()->Unit) {
    NavHost(
        navController = navController,
        route = Graph.HOME,
        startDestination = BottomBar.Tests.route,
    ) {
        composable(route = BottomBar.Tests.route) {
            TestsScreen(navController = navController)
        }
        composable(route = BottomBar.TechCards.route) {
            TechCardsScreen(navController = navController)
        }
        composable(route = BottomBar.Learning.route) {
            LearningMaterialsScreen(navController = navController)
        }
        composable(route = BottomBar.Users.route) {
            UsersScreen(navController = navController){
                onBackBtnClick()
            }
        }
        detailsNavGraph(navController = navController)
    }
}

@Composable
fun ScreenContent(name: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            modifier = Modifier.clickable { onClick() },
            text = name,
            fontSize = MaterialTheme.typography.h3.fontSize,
            fontWeight = FontWeight.Bold,
        )
    }
}
