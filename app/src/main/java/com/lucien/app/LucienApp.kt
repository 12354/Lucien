package com.lucien.app

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.lucien.app.navigation.LucienNavHost
import com.lucien.app.navigation.Screen
import com.lucien.app.ui.components.LucienBottomBar
import com.lucien.app.ui.components.LucienTopBar

@Composable
fun LucienApp() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    var bottomBarVisible by rememberSaveable { mutableStateOf(true) }
    bottomBarVisible = currentRoute in listOf(
        Screen.Home.route,
        Screen.Explore.route,
        Screen.Activity.route,
        Screen.Profile.route
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            if (bottomBarVisible) {
                LucienTopBar(currentRoute = currentRoute)
            }
        },
        bottomBar = {
            AnimatedVisibility(
                visible = bottomBarVisible,
                enter = slideInVertically(initialOffsetY = { it }),
                exit = slideOutVertically(targetOffsetY = { it })
            ) {
                LucienBottomBar(
                    currentRoute = currentRoute,
                    onNavigate = { route ->
                        navController.navigate(route) {
                            popUpTo(Screen.Home.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        LucienNavHost(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}
