package com.lucien.app.navigation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.lucien.app.ui.screens.ActivityScreen
import com.lucien.app.ui.screens.ExploreScreen
import com.lucien.app.ui.screens.HomeScreen
import com.lucien.app.ui.screens.ProfileScreen

@Composable
fun LucienNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier,
        enterTransition = {
            fadeIn(animationSpec = tween(200)) + slideIntoContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.Start,
                animationSpec = tween(200)
            )
        },
        exitTransition = {
            fadeOut(animationSpec = tween(200))
        },
        popEnterTransition = {
            fadeIn(animationSpec = tween(200))
        },
        popExitTransition = {
            fadeOut(animationSpec = tween(200)) + slideOutOfContainer(
                towards = AnimatedContentTransitionScope.SlideDirection.End,
                animationSpec = tween(200)
            )
        }
    ) {
        composable(Screen.Home.route) {
            HomeScreen()
        }
        composable(Screen.Explore.route) {
            ExploreScreen()
        }
        composable(Screen.Activity.route) {
            ActivityScreen()
        }
        composable(Screen.Profile.route) {
            ProfileScreen()
        }
    }
}
