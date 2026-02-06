package be.he2b.savory.data

import android.content.Context
import be.he2b.savory.database.AppDatabase
import be.he2b.savory.database.RecipeDao
import be.he2b.savory.model.Recipe
import be.he2b.savory.network.ApiClient
import be.he2b.savory.network.toRecipe
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

object RecipeRepository {
    private var recipeDao: RecipeDao? = null

    fun initialize(context: Context) {
        if (recipeDao == null) {
            val db = AppDatabase.getDatabase(context)
            recipeDao = db.recipeDao()
        }
    }

    // Api part
    suspend fun searchRecipes(query: String): List<Recipe> {
        return try {
            val response = ApiClient.api.searchMeals(query)
            val apiRecipes = response.meals?.map { it.toRecipe() } ?: emptyList()
            syncWithFavorites(apiRecipes)
        } catch (_: Exception) {
            emptyList()
        }
    }

    suspend fun getRecipeById(id: String): Recipe? {
        return try {
            val response = ApiClient.api.getMealById(id)
            val recipe = response.meals?.firstOrNull()?.toRecipe()
            if (recipe != null) {
                val isFav = recipeDao?.isFavorite(recipe.id) == true
                if (isFav) recipe.copy(isFavorite = true) else recipe
            } else null
        } catch (_: Exception) {
            null
        }
    }

    suspend fun getRecipesByArea(area: String): List<Recipe> {
        return try {
            val response = ApiClient.api.getMealsByArea(area)
            val apiRecipes = response.meals?.map { it.toRecipe() } ?: emptyList()
            syncWithFavorites(apiRecipes)
        } catch (_: Exception) {
            emptyList()
        }
    }

    // We check all recipes and if one of them has a red heart, we update the fav list
    private suspend fun syncWithFavorites(recipes: List<Recipe>): List<Recipe> {
        if (recipeDao == null) return recipes

        return recipes.map { recipe ->
            if (recipeDao?.isFavorite(recipe.id) == true) {
                recipe.copy(isFavorite = true)  // red heart button
            } else {
                recipe  // empty heart button
            }
        }
    }

    suspend fun getMealThemes(): List<String> {
        return try {
            val response = ApiClient.api.getMealCategories()
            response.meals?.map { it.strCategory } ?: emptyList()
        } catch (_: Exception) {
            emptyList()
        }
    }

    suspend fun getAvailableAreas(): List<String> {
        return try {
            val response = ApiClient.api.getMealCountries()
            response.meals?.map { it.strArea } ?: emptyList()
        } catch (_: Exception) {
            emptyList()
        }
    }

    suspend fun getRecipesByCategory(category: String): List<Recipe> {
        return try {
            val response = ApiClient.api.getMealsByCategory(category)
            val apiRecipes = response.meals?.map { it.toRecipe() } ?: emptyList()
            syncWithFavorites(apiRecipes)
        } catch (_: Exception) {
            emptyList()
        }
    }

    val favoriteRecipes: Flow<List<Recipe>>
        get() = recipeDao?.getFavoriteRecipes() ?: flowOf(emptyList())

    suspend fun addFavorite(recipe: Recipe) {
        recipeDao?.insertRecipe(recipe.copy(isFavorite = true))
    }

    suspend fun removeFavorite(recipe: Recipe) {
        recipeDao?.deleteRecipeById(recipe.id)
    }
}