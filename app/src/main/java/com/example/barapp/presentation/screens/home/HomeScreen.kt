package com.example.barapp.presentation.screens.home

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Logout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.barapp.common.ADMIN_ROLE
import com.example.barapp.presentation.navigation.HomeNavGraph
import com.example.barapp.presentation.theme.BottomBarLabelFontEn

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeScreen(
    navController: NavHostController = rememberNavController(),
    onBackBtnClick: () -> Unit
) {
    val viewModel = hiltViewModel<HomeScreenViewModel>()
    val screens = arrayListOf(
        BottomBar.Tests,
        BottomBar.TechCards,
        BottomBar.Learning,
    )
    if (viewModel.getUserRole() == ADMIN_ROLE) {
        screens.add(BottomBar.Users)
    }
    var isConfirmDialogShowed by remember{
        mutableStateOf(false)
    }
    Scaffold(
        topBar = {
            TopBarContent(
                navController = navController,
                screens = screens,
                onBackBtnClick ={
                    isConfirmDialogShowed = true
                }
            )
        },
        bottomBar = {
            BottomBar(navController = navController, screens = screens)
        },
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            HomeNavGraph(navController = navController, onBackBtnClick = onBackBtnClick)
            if(isConfirmDialogShowed){
                LogoutConfirmDialog(
                    onDismiss = {
                        isConfirmDialogShowed = false
                    },
                    onConfirm = {
                        viewModel.saveUser(null)
                        onBackBtnClick()
                    }
                )
            }
        }
    }
}

@Composable
fun TopBarContent(
    navController: NavHostController,
    screens: ArrayList<BottomBar>,
    onBackBtnClick: () -> Unit
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = screens.find {
        backStackEntry?.destination?.route == it.route
    }
    currentScreen?.let {
        TopBar(
            title = {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = stringResource(id = currentScreen.titleLong),
                    style = MaterialTheme.typography.h5.copy(textAlign = TextAlign.Center)
                )
            },
            navigationIcon = {
                IconButton(onClick = {
                    onBackBtnClick()
                }) {
                    Icon(
                        imageVector = Icons.Rounded.Logout,
                        contentDescription = null
                    )
                }
            },
            menu = {},
            backgroundColor = MaterialTheme.colors.surface
        )
    }

}

@Composable
fun BottomBar(
    navController: NavHostController,
    screens: ArrayList<BottomBar>
) {

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination
    val bottomBarDestination = screens.any { it.route == currentDestination?.route }
    if (bottomBarDestination) {
        BottomNavigation(
            backgroundColor = MaterialTheme.colors.surface,
            contentColor = Color.White,
            modifier = Modifier.height(60.dp),
        ) {
            screens.forEach { screen ->
                AddItem(
                    screen = screen,
                    currentDestination = currentDestination,
                    navController = navController,
                )
            }
        }
    }
}

@SuppressLint("SuspiciousIndentation")
@Composable
fun RowScope.AddItem(
    screen: BottomBar,
    currentDestination: NavDestination?,
    navController: NavHostController,
) {
    val isSelected = currentDestination?.hierarchy?.any {
        it.route == screen.route
    } == true
    BottomNavigationItem(
        label = {
            Text(
                text = stringResource(id = screen.title),
                style = BottomBarLabelFontEn,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = (0).dp),
            )
        },
        icon = {
            Icon(
                modifier = Modifier.size(30.dp),
                painter = painterResource(id = screen.icon),
                contentDescription = "Navigation Icon",
            )
        },
        alwaysShowLabel = false,
        selected = isSelected,
        selectedContentColor = MaterialTheme.colors.primary,
        unselectedContentColor = Color.Black,
        onClick = {
            navController.navigate(screen.route) {
                navController.graph.startDestinationRoute?.let { route ->
                    popUpTo(route) {
                        saveState = true
                    }
                }
                launchSingleTop = true
                restoreState = true
            }
        },
    )
}
