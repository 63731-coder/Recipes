package be.he2b.savory.network

import org.junit.Assert.assertEquals
import org.junit.Test

class MealDtoTest {

    @Test
    fun toRecipe_convertsIdCorrectly() {
        val mealDto = createTestMealDto(
            idMeal = "52772",
            strMeal = "Teriyaki Chicken"
        )

        val recipe = mealDto.toRecipe()

        assertEquals("52772", recipe.id)
    }

    @Test
    fun toRecipe_convertsTitleCorrectly() {
        val mealDto = createTestMealDto(
            strMeal = "Pasta Carbonara"
        )

        val recipe = mealDto.toRecipe()

        assertEquals("Pasta Carbonara", recipe.title)
    }

    @Test
    fun toRecipe_usesCategoryWhenProvided() {
        val mealDto = createTestMealDto(strCategory = "Italian")

        val recipe = mealDto.toRecipe()

        assertEquals("Italian", recipe.category)
    }

    @Test
    fun toRecipe_usesDefaultCategoryWhenNull() {
        val mealDto = createTestMealDto(strCategory = null)

        val recipe = mealDto.toRecipe()

        assertEquals("General", recipe.category)
    }

    @Test
    fun toRecipe_usesAreaWhenProvided() {
        val mealDto = createTestMealDto(strArea = "Italy")

        val recipe = mealDto.toRecipe()

        assertEquals("Italy", recipe.area)
    }

    @Test
    fun toRecipe_usesDefaultAreaWhenNull() {
        val mealDto = createTestMealDto(strArea = null)

        val recipe = mealDto.toRecipe()

        assertEquals("Unknown", recipe.area)
    }

    @Test
    fun getIngredientsList_combinesMeasureAndIngredient() {
        val mealDto = createTestMealDto(
            strIngredient1 = "Chicken",
            strMeasure1 = "500g"
        )

        val ingredients = mealDto.getIngredientsList()

        assertEquals("500g Chicken", ingredients[0])
    }

    @Test
    fun getIngredientsList_handlesIngredientWithoutMeasure() {
        val mealDto = createTestMealDto(
            strIngredient1 = "Salt",
            strMeasure1 = null
        )

        val ingredients = mealDto.getIngredientsList()

        assertEquals("Salt", ingredients[0])
    }

    @Test
    fun getIngredientsList_ignoresNullIngredients() {
        val mealDto = createTestMealDto(
            strIngredient1 = "Flour",
            strMeasure1 = "100g",
            strIngredient2 = null,
            strMeasure2 = "2 cups"
        )

        val ingredients = mealDto.getIngredientsList()

        assertEquals(1, ingredients.size)
        assertEquals("100g Flour", ingredients[0])
    }

    // Helper function to create test MealDto objects
    private fun createTestMealDto(
        idMeal: String = "123",
        strMeal: String = "Test Meal",
        strCategory: String? = "Test",
        strMealThumb: String = "https://example.com/image.jpg",
        strInstructions: String? = "Test instructions",
        strArea: String? = "Test Area",
        strTags: String? = null,
        strIngredient1: String? = null,
        strMeasure1: String? = null,
        strIngredient2: String? = null,
        strMeasure2: String? = null
    ): MealDto {
        return MealDto(
            idMeal = idMeal,
            strMeal = strMeal,
            strCategory = strCategory,
            strMealThumb = strMealThumb,
            strInstructions = strInstructions,
            strArea = strArea,
            strTags = strTags,
            strIngredient1 = strIngredient1,
            strMeasure1 = strMeasure1,
            strIngredient2 = strIngredient2,
            strMeasure2 = strMeasure2,
            strIngredient3 = null,
            strMeasure3 = null,
            strIngredient4 = null,
            strMeasure4 = null,
            strIngredient5 = null,
            strMeasure5 = null,
            strIngredient6 = null,
            strMeasure6 = null,
            strIngredient7 = null,
            strMeasure7 = null,
            strIngredient8 = null,
            strMeasure8 = null,
            strIngredient9 = null,
            strMeasure9 = null,
            strIngredient10 = null,
            strMeasure10 = null,
            strIngredient11 = null,
            strMeasure11 = null,
            strIngredient12 = null,
            strMeasure12 = null,
            strIngredient13 = null,
            strMeasure13 = null,
            strIngredient14 = null,
            strMeasure14 = null,
            strIngredient15 = null,
            strMeasure15 = null
        )
    }
}
