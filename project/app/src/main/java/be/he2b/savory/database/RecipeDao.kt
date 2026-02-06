package be.he2b.savory.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import be.he2b.savory.model.Recipe
import kotlinx.coroutines.flow.Flow

/*
 * Data Access Object (DAO) for accessing recipe data in the database.
 * It provides methods to perform operations like inserting, deleting, and querying favorite recipes.
 */
@Dao
interface RecipeDao {

    // gets the favorite recipes and automatically notifies if there is any change (thanks to flow)
    @Query("SELECT * FROM recipes")
    fun getFavoriteRecipes(): Flow<List<Recipe>>

    @Query("SELECT EXISTS(SELECT 1 FROM recipes WHERE id = :id)") //check if recipe is favorite
    suspend fun isFavorite(id: String): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)// add a recipe or rewrite it if it already exists
    suspend fun insertRecipe(recipe: Recipe)

    @Query("DELETE FROM recipes WHERE id = :id")
    suspend fun deleteRecipeById(id: String)
}