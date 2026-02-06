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

class RecipeDetailViewModel(application: Application) : AndroidViewModel(application) {

    private val _recipe = MutableStateFlow<Recipe?>(null)
    val recipe: StateFlow<Recipe?> = _recipe.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        RecipeRepository.initialize(application)
    }

    fun fetchRecipeById(id: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val result = RecipeRepository.getRecipeById(id)
                if (result != null) {
                    _recipe.value = result
                } else {
                    _errorMessage.value = "Recipe not found. Please check your connection"
                }
            } catch (_: Exception) {
                _errorMessage.value = "Connection error."
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun toggleFavorite(currentRecipe: Recipe) {
        viewModelScope.launch {
            if (currentRecipe.isFavorite) {
                RecipeRepository.removeFavorite(currentRecipe)
                // "Refresh the screen immediately
                _recipe.value = currentRecipe.copy(isFavorite = false)
            } else {
                RecipeRepository.addFavorite(currentRecipe)
                _recipe.value = currentRecipe.copy(isFavorite = true)
            }
        }
    }
}