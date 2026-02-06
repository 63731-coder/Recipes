package be.he2b.savory.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import be.he2b.savory.R
import be.he2b.savory.ui.components.BigRecipeCard
import be.he2b.savory.ui.components.ErrorView
import be.he2b.savory.ui.components.SmallRecipeCard
import be.he2b.savory.ui.viewmodel.HomeViewModel

@Composable
fun HomeScreen(
    navController: NavHostController, modifier: Modifier = Modifier
) {
    val viewModel: HomeViewModel = viewModel()

    val recipes by viewModel.randomRecipes.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    val selectedCountry by viewModel.selectedCountry.collectAsState()
    val countryRecipes by viewModel.homeCountryRecipes.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadRandomCountryRecipes()
        if (recipes.isEmpty()) {
            viewModel.loadRandomRecipes()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
        ) {
            // HEADER green
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp)
                        .background(MaterialTheme.colorScheme.primary)
                ) {
                    IconButton(
                        onClick = { navController.navigate(ProjectScreen.Settings.name) },
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(
                                start = 12.dp, top = 36.dp
                            )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = stringResource(R.string.configuration),
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Text(
                        text = if (selectedCountry.isNotEmpty()) stringResource(
                            R.string.recipes, selectedCountry
                        ) else stringResource(
                            R.string.welcome
                        ),
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(start = 24.dp, bottom = 24.dp)
                    )
                }
            }

            if (isLoading) {
                // Case: Loading
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(300.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    }
                }
            } else if (errorMessage != null) {
                // Case: internet error
                item {
                    ErrorView(
                        message = errorMessage ?: stringResource(R.string.unknown_error),
                        onRetry = { viewModel.reloadAll() },
                        modifier = Modifier.padding(top = 50.dp)
                    )
                }
            } else {
                // Normal case: we show recipes
                // big cards
                if (countryRecipes.isNotEmpty()) {
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        LazyRow(
                            contentPadding = PaddingValues(horizontal = 24.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            items(countryRecipes) { recipe ->
                                BigRecipeCard(
                                    recipe = recipe,
                                    onClick = { navController.navigate("${ProjectScreen.Detail.name}/${recipe.id}") },
                                    onFavoriteClick = { viewModel.toggleFavorite(recipe) })
                            }
                        }
                    }
                }

                // Title section 2
                item {
                    Spacer(modifier = Modifier.height(32.dp))
                    Text(
                        text = stringResource(R.string.discover),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onBackground,
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Small cards
                items(recipes) { recipe ->
                    SmallRecipeCard(
                        recipe = recipe,
                        onClick = { navController.navigate("${ProjectScreen.Detail.name}/${recipe.id}") },
                        onFavoriteClick = { viewModel.toggleFavorite(recipe) })
                }
            }
        }
    }
}