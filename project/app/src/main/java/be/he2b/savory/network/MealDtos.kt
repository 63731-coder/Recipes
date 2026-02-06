package be.he2b.savory.network

/**
 * Data classes to hold raw API responses and functions to
 * convert them into clean recipe objects for the app.
 */

data class MealResponse(
    val meals: List<MealDto>?
)

data class CategoryListResponse(
    val meals: List<CategoryItemDto>?
)

data class CategoryItemDto(
    val strCategory: String //a category
)

data class CountryListResponse(
    val meals: List<CountryItemDto>?
)

data class CountryItemDto(
    val strArea: String //a country
)


/**
 * The object just like it comes from the API
 */
data class MealDto(
    val idMeal: String,
    val strMeal: String,
    val strCategory: String?,
    val strMealThumb: String,
    val strInstructions: String?,
    val strArea: String?,
    val strTags: String?,
    val strIngredient1: String?, val strMeasure1: String?,
    val strIngredient2: String?, val strMeasure2: String?,
    val strIngredient3: String?, val strMeasure3: String?,
    val strIngredient4: String?, val strMeasure4: String?,
    val strIngredient5: String?, val strMeasure5: String?,
    val strIngredient6: String?, val strMeasure6: String?,
    val strIngredient7: String?, val strMeasure7: String?,
    val strIngredient8: String?, val strMeasure8: String?,
    val strIngredient9: String?, val strMeasure9: String?,
    val strIngredient10: String?, val strMeasure10: String?,
    val strIngredient11: String?, val strMeasure11: String?,
    val strIngredient12: String?, val strMeasure12: String?,
    val strIngredient13: String?, val strMeasure13: String?,
    val strIngredient14: String?, val strMeasure14: String?,
    val strIngredient15: String?, val strMeasure15: String?
)

// Converts the DTO (API) to our Model (App)
fun MealDto.toRecipe(): be.he2b.savory.model.Recipe {
    return be.he2b.savory.model.Recipe(
        id = idMeal,
        title = strMeal,
        category = strCategory ?: "General",
        imageUrl = strMealThumb,
        instructions = (strInstructions ?: "").split("\r\n", "\n").filter { it.isNotBlank() },
        isFavorite = false,
        area = strArea ?: "Unknown",
        tags = strTags?.split(",") ?: emptyList(),
        ingredients = getIngredientsList()
    )
}

fun MealDto.getIngredientsList(): List<String> {
    // We create a list of pairs (Ingredient, Measurement)
    val rawIngredients = listOf(
        strIngredient1 to strMeasure1,
        strIngredient2 to strMeasure2,
        strIngredient3 to strMeasure3,
        strIngredient4 to strMeasure4,
        strIngredient5 to strMeasure5,
        strIngredient6 to strMeasure6,
        strIngredient7 to strMeasure7,
        strIngredient8 to strMeasure8,
        strIngredient9 to strMeasure9,
        strIngredient10 to strMeasure10,
        strIngredient11 to strMeasure11,
        strIngredient12 to strMeasure12,
        strIngredient13 to strMeasure13,
        strIngredient14 to strMeasure14,
        strIngredient15 to strMeasure15
    )

    // Combines ingredients with their measurements and removes any empty items from the list.
    return rawIngredients.mapNotNull { (ingredient, measure) ->
        if (!ingredient.isNullOrBlank()) {
            val cleanMeasure = if (!measure.isNullOrBlank()) measure else ""
            "$cleanMeasure $ingredient".trim()
        } else {
            null
        }
    }
}