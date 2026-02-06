package be.he2b.savory.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import be.he2b.savory.ui.components.ProjectBottomBar
import be.he2b.savory.ui.components.ProjectNavGraph

enum class ProjectScreen {
    Home, Search, Favorites, Settings, Detail
}

@Composable
fun SavoryApp() {
    val navController = rememberNavController()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        bottomBar = {
            ProjectBottomBar(
                navController = navController, currentDestination = currentDestination
            )
        }) { innerPadding ->
        ProjectNavGraph(
            navController = navController, modifier = Modifier.padding(innerPadding)
        )
    }
}