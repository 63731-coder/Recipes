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

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    // Discover recipes
    private val _randomRecipes = MutableStateFlow<List<Recipe>>(emptyList())
    val randomRecipes: StateFlow<List<Recipe>> = _randomRecipes.asStateFlow()

    // Country recipes
    private val _homeCountryRecipes = MutableStateFlow<List<Recipe>>(emptyList())
    val homeCountryRecipes: StateFlow<List<Recipe>> = _homeCountryRecipes.asStateFlow()

    private val _selectedCountry = MutableStateFlow("")
    val selectedCountry: StateFlow<String> = _selectedCountry.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private var _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    init {
        // Setup the database repository since Home is the first screen
        RecipeRepository.initialize(application)

        // Listen for favorite changes to update the "heart" icons in real-time
        viewModelScope.launch {
            RecipeRepository.favoriteRecipes.collect { favList ->
                updateFavoritesState(_randomRecipes, favList)
                updateFavoritesState(_homeCountryRecipes, favList)
            }
        }

        // Initial data load
        loadRandomCountryRecipes()
        loadRandomRecipes()
    }

    fun loadRandomCountryRecipes(forceRefresh: Boolean = false) {
        if (!forceRefresh && _selectedCountry.value.isNotEmpty() && _homeCountryRecipes.value.isNotEmpty()) {
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                val areas = RecipeRepository.getAvailableAreas()

                val countryToLoad = _selectedCountry.value.ifEmpty {
                    if (areas.isNotEmpty()) areas.random() else "American"
                }

                _selectedCountry.value = countryToLoad

                val recipes = RecipeRepository.getRecipesByArea(countryToLoad)

                if (recipes.isNotEmpty()) {
                    _homeCountryRecipes.value = recipes
                } else {
                    _errorMessage.value = "No connection found."
                }
            } catch (_: Exception) {
                _errorMessage.value = "Error loading country recipes"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loadRandomRecipes() {
        viewModelScope.launch {
            try {
                val themes = RecipeRepository.getMealThemes()
                val randomTheme = if (themes.isNotEmpty()) themes.random() else "Beef"
                val result = RecipeRepository.searchRecipes(randomTheme)
                _randomRecipes.value = result
            } catch (_: Exception) {
                _errorMessage.value = "Error loading random recipes"
            }
        }
    }

    fun toggleFavorite(recipe: Recipe) {
        viewModelScope.launch {
            if (recipe.isFavorite) RecipeRepository.removeFavorite(recipe)
            else RecipeRepository.addFavorite(recipe)
        }
    }

    fun reloadAll() {
        _errorMessage.value = null
        loadRandomCountryRecipes(true)
        loadRandomRecipes()
    }

    // Helper to update the heart icons on the screen
    private fun updateFavoritesState(
        flowToUpdate: MutableStateFlow<List<Recipe>>, favList: List<Recipe>
    ) {
        // Get only the IDs of the favorite recipes (easier to compare)
        val favoriteIds = favList.map { it.id }

        // Update the current list
        flowToUpdate.value = flowToUpdate.value.map { recipe ->
            // Copy the recipe and set isFavorite to true if its ID is in the list
            recipe.copy(isFavorite = recipe.id in favoriteIds)
        }
    }
}