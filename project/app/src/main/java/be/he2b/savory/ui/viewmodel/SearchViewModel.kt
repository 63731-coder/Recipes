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

class SearchViewModel(application: Application) : AndroidViewModel(application) {

    private val _searchResults = MutableStateFlow<List<Recipe>>(emptyList())
    val searchResults: StateFlow<List<Recipe>> = _searchResults.asStateFlow()

    private var rawSearchResults: List<Recipe> = emptyList()

    private val _availableCategories = MutableStateFlow<List<String>>(emptyList())
    val availableCategories: StateFlow<List<String>> = _availableCategories.asStateFlow()

    private val _availableAreas = MutableStateFlow<List<String>>(emptyList())
    val availableAreas: StateFlow<List<String>> = _availableAreas.asStateFlow()

    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory.asStateFlow()

    private val _selectedArea = MutableStateFlow<String?>(null)
    val selectedArea: StateFlow<String?> = _selectedArea.asStateFlow()

    // States UI
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private var isTextSearchActive = false

    init {
        RecipeRepository.initialize(application)
        loadFiltersData() // Load lists for the dropdown menus (categories and countries)

        // Automatically update the "heart" icons when favorites change
        viewModelScope.launch {
            RecipeRepository.favoriteRecipes.collect { favList ->
                val currentList = _searchResults.value
                if (currentList.isNotEmpty()) {
                    _searchResults.value = currentList.map { recipe ->
                        recipe.copy(isFavorite = favList.any { it.id == recipe.id })
                    }
                }
                // Also update the hidden raw list in memory
                if (rawSearchResults.isNotEmpty()) {
                    rawSearchResults = rawSearchResults.map { recipe ->
                        recipe.copy(isFavorite = favList.any { it.id == recipe.id })
                    }
                }
            }
        }
    }

    private fun loadFiltersData() {
        viewModelScope.launch {
            val cats = RecipeRepository.getMealThemes()
            _availableCategories.value = cats

            val areas = RecipeRepository.getAvailableAreas()
            _availableAreas.value = areas
        }
    }

    fun searchRecipes(query: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null

            // Turn on "Text Search" mode
            isTextSearchActive = true

            try {
                // Search for recipes using the API
                val result = RecipeRepository.searchRecipes(query)

                // Save the original results
                rawSearchResults = result

                // Apply active filters on top of the results
                applyFilters()

                if (rawSearchResults.isEmpty()) {
                    _errorMessage.value = "No recipes found for '$query'."
                }
            } catch (_: Exception) {
                _errorMessage.value = "Internet connection error."
                _searchResults.value = emptyList()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun selectCategory(category: String?) {
        _selectedCategory.value = category

        // If we are NOT searching by text, ask the API for this category
        if (!isTextSearchActive && category != null) {
            loadApiCategory(category)
        } else {
            // Otherwise, just filter the results we already have
            applyFilters()
        }
    }

    fun selectArea(area: String?) {
        _selectedArea.value = area

        // If we are NOT searching by text, ask the API for this country
        if (!isTextSearchActive && area != null) {
            loadApiArea(area)
        } else {
            applyFilters()
        }
    }

    private fun loadApiCategory(category: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                // Call API (filter by category)
                val result = RecipeRepository.getRecipesByCategory(category)

                rawSearchResults = result
                _searchResults.value = result // Show results directly

                // Reset country because the API cannot filter both at the same time here
                _selectedArea.value = null
            } catch (_: Exception) {
                _errorMessage.value = "Error loading category."
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun loadApiArea(area: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            try {
                // Call API (filter by country)
                val result = RecipeRepository.getRecipesByArea(area)

                rawSearchResults = result
                _searchResults.value = result // Show results directly

                // Reset category due to API limitations (cannot cross-filter)
                _selectedCategory.value = null
            } catch (_: Exception) {
                _errorMessage.value = "Error loading area."
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun applyFilters() {
        var filteredList = rawSearchResults

        // Filter by category (only if a category is selected)
        _selectedCategory.value?.let { cat ->
            filteredList = filteredList.filter { it.category.equals(cat, ignoreCase = true) }
        }

        // Filter by Country (only if a country is selected)
        _selectedArea.value?.let { area ->
            filteredList = filteredList.filter { it.area.equals(area, ignoreCase = true) }
        }

        _searchResults.value = filteredList

        if (rawSearchResults.isNotEmpty() && filteredList.isEmpty()) {
            _errorMessage.value = "No results match your filters."
        } else if (rawSearchResults.isNotEmpty()) {
            _errorMessage.value = null
        }
    }

    fun toggleFavorite(recipe: Recipe) {
        viewModelScope.launch {
            if (recipe.isFavorite) RecipeRepository.removeFavorite(recipe)
            else RecipeRepository.addFavorite(recipe)
        }
    }

    fun loadSuggestions() {
        if (_searchResults.value.isEmpty()) {
            // At start, this is not a text search
            isTextSearchActive = false
            // Load a default category (Beef) to fill the screen
            selectCategory("Beef")
        }
    }
}