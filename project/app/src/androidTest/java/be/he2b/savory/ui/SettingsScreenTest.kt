package be.he2b.savory.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.rememberNavController
import org.junit.Rule
import org.junit.Test

class SettingsScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun settingsScreen_displaysOptions() {
        composeTestRule.setContent {
            val navController = rememberNavController()
            SettingsScreen(navController = navController)
        }

        composeTestRule.onNodeWithText("Settings").assertIsDisplayed()
        composeTestRule.onNodeWithText("Accessibility & Display").assertIsDisplayed()
        composeTestRule.onNodeWithText("Protanopia").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Back").assertIsDisplayed()
    }

    @Test
    fun settingsScreen_clickOption() {
        composeTestRule.setContent {
            val navController = rememberNavController()
            SettingsScreen(navController = navController)
        }
        composeTestRule.onNodeWithText("Deuteranopia").performClick()
    }
}