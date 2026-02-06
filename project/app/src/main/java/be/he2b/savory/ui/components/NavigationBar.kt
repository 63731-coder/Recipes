package be.he2b.savory.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import be.he2b.savory.R
import be.he2b.savory.ui.FavoritesScreen
import be.he2b.savory.ui.HomeScreen
import be.he2b.savory.ui.ProjectScreen
import be.he2b.savory.ui.RecipeDetailScreen
import be.he2b.savory.ui.SearchScreen
import be.he2b.savory.ui.SettingsScreen

sealed class BottomNavItem(
    val route: String, val icon: ImageVector, val labelResId: Int
) {
    object Home : BottomNavItem(
        ProjectScreen.Home.name, Icons.Filled.Home, R.string.home
    )

    object Search : BottomNavItem(
        ProjectScreen.Search.name, Icons.Filled.Search, R.string.search_screen
    )

    object Favorites : BottomNavItem(
        ProjectScreen.Favorites.name, Icons.Filled.Favorite, R.string.favorites_screen
    )

}

val bottomNavItems = listOf(BottomNavItem.Home, BottomNavItem.Search, BottomNavItem.Favorites)

@Composable
fun ProjectBottomBar(
    navController: NavHostController, currentDestination: NavDestination?
) {
    NavigationBar {
        bottomNavItems.forEach { item ->
            val isSelected = currentDestination?.hierarchy?.any { it.route == item.route } == true

            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = stringResource(item.labelResId)) },
                label = { Text(text = stringResource(item.labelResId)) },
                selected = isSelected,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                })
        }
    }
}

@Composable
fun ProjectNavGraph(
    navController: NavHostController, modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = ProjectScreen.Home.name,
        modifier = modifier
    ) {
        composable(route = ProjectScreen.Home.name) {
            HomeScreen(navController = navController)
        }
        composable(route = ProjectScreen.Search.name) {
            SearchScreen(navController = navController)
        }
        composable(route = ProjectScreen.Favorites.name) {
            FavoritesScreen(navController = navController)
        }
        composable(
            route = "${ProjectScreen.Detail.name}/{recipeId}",
            arguments = listOf(navArgument("recipeId") { type = NavType.StringType })
        ) { backStackEntry ->
            val recipeId = backStackEntry.arguments?.getString(stringResource(R.string.recipeid))

            RecipeDetailScreen(
                recipeId = recipeId, onBackClick = { navController.popBackStack() })
        }
        composable(route = ProjectScreen.Settings.name) {
            SettingsScreen(navController = navController)
        }
    }
}