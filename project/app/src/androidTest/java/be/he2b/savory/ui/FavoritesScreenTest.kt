package be.he2b.savory.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.navigation.compose.rememberNavController
import org.junit.Rule
import org.junit.Test

class FavoritesScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun favoritesScreen_displaysTitle_and_Settings() {
        composeTestRule.setContent {
            val navController = rememberNavController()
            FavoritesScreen(navController = navController)
        }

        composeTestRule.onNodeWithText("My favorites").assertIsDisplayed()

        composeTestRule.onNodeWithContentDescription("Configuration").assertIsDisplayed()
    }

    @Test
    fun favoritesScreen_displaysEmptyState_ifNoFavorites() {
        composeTestRule.setContent {
            val navController = rememberNavController()
            FavoritesScreen(navController = navController)
        }

        composeTestRule.onNodeWithText("Empty favorites list").assertIsDisplayed()
    }
}