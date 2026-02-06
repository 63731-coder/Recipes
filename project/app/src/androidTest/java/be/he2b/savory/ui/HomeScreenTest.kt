package be.he2b.savory.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.navigation.compose.rememberNavController
import org.junit.Rule
import org.junit.Test

class HomeScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun homeScreen_verifiesStaticElements() {
        composeTestRule.setContent {
            val navController = rememberNavController()
            HomeScreen(navController = navController)
        }

        composeTestRule.onNodeWithContentDescription("Configuration").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Configuration").assertIsDisplayed()
    }
}