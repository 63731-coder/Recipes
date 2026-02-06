package be.he2b.savory.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Represents a recipe table in the database.
 */
@Entity(tableName = "recipes")
data class Recipe(
    @PrimaryKey val id: String,
    val title: String,
    val category: String,
    val imageUrl: String,
    val isFavorite: Boolean = false,
    val instructions: List<String>,
    val area: String,
    val tags: List<String>,
    val ingredients: List<String> = emptyList()
)