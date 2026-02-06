package be.he2b.savory.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import be.he2b.savory.data.RecipeRepository
import be.he2b.savory.model.Recipe
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FavoritesViewModel(application: Application) : AndroidViewModel(application) {
    private val _favorites = MutableStateFlow<List<Recipe>>(emptyList())
    val favorites: StateFlow<List<Recipe>> = _favorites.asStateFlow()

    init {
        RecipeRepository.initialize(application)

        // Start a coroutine to listen for database changes
        viewModelScope.launch {
            RecipeRepository.favoriteRecipes.collect { favList ->
                // at each change in the db, we update our favorite list
                _favorites.value = favList
            }
        }
    }

    fun toggleFavorite(recipe: Recipe) {
        viewModelScope.launch {
            RecipeRepository.removeFavorite(recipe)
        }
    }
}