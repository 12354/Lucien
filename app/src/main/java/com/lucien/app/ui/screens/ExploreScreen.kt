package com.lucien.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Calculate
import androidx.compose.material.icons.outlined.CameraAlt
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.Restaurant
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Translate
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

private data class Category(
    val name: String,
    val icon: ImageVector,
    val description: String
)

private val categories = listOf(
    Category("Translate", Icons.Outlined.Translate, "Languages"),
    Category("Calculate", Icons.Outlined.Calculate, "Math tools"),
    Category("Notes", Icons.Outlined.Description, "Documents"),
    Category("Camera", Icons.Outlined.CameraAlt, "Scan & capture"),
    Category("Maps", Icons.Outlined.Map, "Navigation"),
    Category("Recipes", Icons.Outlined.Restaurant, "Cooking"),
    Category("Fitness", Icons.Outlined.FitnessCenter, "Health"),
    Category("Design", Icons.Outlined.Palette, "Creative tools"),
)

@Composable
fun ExploreScreen() {
    var searchQuery by remember { mutableStateOf("") }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item(span = { GridItemSpan(2) }) {
            Column {
                Text(
                    text = "Discover",
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Find tools and features",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search features...") },
                    leadingIcon = {
                        Icon(
                            Icons.Outlined.Search,
                            contentDescription = "Search",
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.large,
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainerHigh
                    ),
                    singleLine = true
                )
                Spacer(modifier = Modifier.height(8.dp))
            }
        }

        items(
            categories.filter {
                searchQuery.isBlank() || it.name.contains(searchQuery, ignoreCase = true)
            }
        ) { category ->
            CategoryCard(category)
        }
    }
}

@Composable
private fun CategoryCard(category: Category) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainer
        ),
        shape = MaterialTheme.shapes.large
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Surface(
                shape = MaterialTheme.shapes.medium,
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.size(44.dp)
            ) {
                Icon(
                    imageVector = category.icon,
                    contentDescription = category.name,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .padding(10.dp)
                        .size(24.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = category.name,
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = category.description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
