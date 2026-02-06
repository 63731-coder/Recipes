package be.he2b.savory.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.navigation.compose.rememberNavController
import org.junit.Rule
import org.junit.Test

class SearchScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun searchScreen_verifiesElementsAreDisplayed() {
        composeTestRule.setContent {
            val navController = rememberNavController()
            SearchScreen(navController = navController)
        }

        composeTestRule.onNodeWithText("Find a recipe").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Configuration").assertIsDisplayed()
    }

    @Test
    fun searchScreen_performSearchInput() {
        composeTestRule.setContent {
            val navController = rememberNavController()
            SearchScreen(navController = navController)
        }

        composeTestRule.onNodeWithText("Ex: Pie, Beef...").performTextInput("Pizza")
        composeTestRule.onNodeWithContentDescription("Search").performClick()
    }
}