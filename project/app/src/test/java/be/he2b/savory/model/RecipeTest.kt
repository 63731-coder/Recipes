package be.he2b.savory.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class RecipeTest {

    @Test
    fun recipe_defaultsToNotFavorite() {
        val recipe = Recipe(
            id = "123",
            title = "Test Recipe",
            category = "Test",
            imageUrl = "test.jpg",
            instructions = emptyList(),
            area = "Test Area",
            tags = emptyList()
        )

        assertFalse(recipe.isFavorite)
    }

    @Test
    fun recipe_copyWithFavoriteTrue() {
        val recipe = Recipe(
            id = "123",
            title = "Test Recipe",
            category = "Test",
            imageUrl = "test.jpg",
            isFavorite = false,
            instructions = emptyList(),
            area = "Test Area",
            tags = emptyList()
        )

        val updatedRecipe = recipe.copy(isFavorite = true)

        assertTrue(updatedRecipe.isFavorite)
    }

    @Test
    fun recipe_copyWithFavoriteFalse() {
        val recipe = Recipe(
            id = "123",
            title = "Test Recipe",
            category = "Test",
            imageUrl = "test.jpg",
            isFavorite = true,
            instructions = emptyList(),
            area = "Test Area",
            tags = emptyList()
        )

        val updatedRecipe = recipe.copy(isFavorite = false)

        assertFalse(updatedRecipe.isFavorite)
    }

    @Test
    fun recipe_keepsIdWhenCopied() {
        val originalId = "123"
        val recipe = Recipe(
            id = originalId,
            title = "Test Recipe",
            category = "Test",
            imageUrl = "test.jpg",
            instructions = emptyList(),
            area = "Test Area",
            tags = emptyList()
        )

        val copiedRecipe = recipe.copy(isFavorite = true)

        assertEquals(originalId, copiedRecipe.id)
    }
}
