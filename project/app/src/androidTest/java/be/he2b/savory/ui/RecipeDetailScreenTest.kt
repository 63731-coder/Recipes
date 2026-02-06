package be.he2b.savory.ui

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import org.junit.Rule
import org.junit.Test

class RecipeDetailScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun recipeDetailScreen_showsError_whenIdIsInvalid() {
        // 1. Launch the screen with an invalid ID
        val fakeId = "99999999"

        composeTestRule.setContent {
            RecipeDetailScreen(
                recipeId = fakeId, onBackClick = {})
        }

        // We set a timeout of 5 seconds to allow for the network call to fail
        composeTestRule.waitUntil(timeoutMillis = 5000) {
            composeTestRule.onAllNodesWithText("Return").fetchSemanticsNodes().isNotEmpty()
        }

        // 3. Verify that the button is displayed
        composeTestRule.onNodeWithText("Return").assertIsDisplayed()
    }
}