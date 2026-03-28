package com.lucien.app.ui.components

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import com.lucien.app.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LucienTopBar(currentRoute: String?) {
    val title = when (currentRoute) {
        Screen.Home.route -> "Lucien"
        Screen.Explore.route -> "Explore"
        Screen.Activity.route -> "Activity"
        Screen.Profile.route -> "Profile"
        else -> "Lucien"
    }

    CenterAlignedTopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge
            )
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onSurface
        )
    )
}
