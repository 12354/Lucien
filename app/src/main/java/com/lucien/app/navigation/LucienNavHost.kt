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
import com.lucien.app.ui.screens.notepad.NotepadScreen
import com.lucien.app.ui.screens.poop.PoopLoggerScreen

@Composable
fun LucienNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = "home",
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
        composable("home") {
            HomeScreen()
        }
        composable("explore") {
            ExploreScreen(onOpenNotepad = { navController.navigate("notepad") })
        }
        composable("activity") {
            ActivityScreen()
        }
        composable("profile") {
            ProfileScreen()
        }
        composable("poop_logger") {
            PoopLoggerScreen()
        }
        composable("notepad") {
            NotepadScreen(onBack = { navController.popBackStack() })
        }
    }
}
