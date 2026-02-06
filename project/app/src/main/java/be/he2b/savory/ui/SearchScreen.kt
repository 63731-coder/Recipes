package be.he2b.savory.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import be.he2b.savory.ui.components.ErrorView
import be.he2b.savory.ui.components.SmallRecipeCard
import be.he2b.savory.ui.viewmodel.SearchViewModel
import be.he2b.savory.R


@Composable
fun SearchScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    val viewModel: SearchViewModel = viewModel()

    val recipes by viewModel.searchResults.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    // filter states
    val availableCategories by viewModel.availableCategories.collectAsState()
    val availableAreas by viewModel.availableAreas.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val selectedArea by viewModel.selectedArea.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var showFilters by remember { mutableStateOf(false) } // to open/close filter

    // Load initial recipe ideas when the screen starts
    LaunchedEffect(Unit) {
        if (recipes.isEmpty()) {
            viewModel.loadSuggestions()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        LazyColumn(
            contentPadding = PaddingValues(bottom = 80.dp), modifier = Modifier.fillMaxSize()
        ) {

            item {
                // The blue header gets taller if the filters are open
                val headerHeight = if (showFilters) 340.dp else 240.dp

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(headerHeight) // dynamic height based on filter state
                        .background(MaterialTheme.colorScheme.primary)
                ) {
                    IconButton(
                        onClick = { navController.navigate(ProjectScreen.Settings.name) },
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(start = 12.dp, top = 36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = stringResource(R.string.configuration),
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                    ) {
                        Spacer(modifier = Modifier.height(80.dp))

                        Text(
                            text = stringResource(R.string.find_a_recipe),
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimary,
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        // search bar + buttons
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            TextField(
                                value = searchQuery,
                                onValueChange = { searchQuery = it },
                                placeholder = { Text(text = stringResource(R.string.ex_pie_beef)) },
                                singleLine = true,
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.White,
                                    unfocusedContainerColor = Color.White,
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent
                                ),
                                // Use the "Enter" key on the virtual keyboard
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                                keyboardActions = KeyboardActions(
                                    onSearch = {
                                        if (searchQuery.isNotBlank()) viewModel.searchRecipes(
                                            searchQuery
                                        )
                                    }),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .weight(1f) // takes all the remaining space
                                    .height(56.dp)
                                    .shadow(4.dp, RoundedCornerShape(12.dp))
                            )

                            Spacer(modifier = Modifier.width(8.dp))

                            // 2. Search button
                            Button(
                                onClick = {
                                    if (searchQuery.isNotBlank()) {
                                        viewModel.searchRecipes(searchQuery)
                                    }
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.secondary
                                ),
                                contentPadding = PaddingValues(0.dp),
                                modifier = Modifier
                                    .size(56.dp)
                                    .shadow(4.dp, RoundedCornerShape(12.dp))
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = stringResource(R.string.search),
                                    tint = MaterialTheme.colorScheme.onSecondary
                                )
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            // filter buttons
                            Button(
                                onClick = { showFilters = !showFilters },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                                ),
                                contentPadding = PaddingValues(0.dp),
                                modifier = Modifier
                                    .size(56.dp) // square button
                                    .shadow(4.dp, RoundedCornerShape(12.dp))
                            ) {
                                Icon(
                                    imageVector = if (showFilters) Icons.Default.Close else Icons.Default.Menu,
                                    contentDescription = "Filters",
                                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                            }
                        }

                        // filters
                        AnimatedVisibility(visible = showFilters) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 16.dp),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                // Dropdown category
                                FilterDropdown(
                                    label = stringResource(R.string.category),
                                    options = availableCategories,
                                    selectedOption = selectedCategory,
                                    onOptionSelected = { viewModel.selectCategory(it) },
                                    modifier = Modifier.weight(1f)
                                )

                                // Dropdown country
                                FilterDropdown(
                                    label = stringResource(R.string.area),
                                    options = availableAreas,
                                    selectedOption = selectedArea,
                                    onOptionSelected = { viewModel.selectArea(it) },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
            }


            if (isLoading) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(50.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    }
                }
            } else if (errorMessage != null) {
                item {
                    ErrorView(
                        message = errorMessage ?: stringResource(R.string.unknown_error),
                        onRetry = {
                            if (searchQuery.isNotBlank()) {
                                viewModel.searchRecipes(searchQuery)
                            } else {
                                viewModel.loadSuggestions()
                            }
                        },
                        modifier = Modifier.padding(top = 50.dp)
                    )
                }
            } else {
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

// helper component for dropdown menu
@Composable
fun FilterDropdown(
    label: String,
    options: List<String>,
    selectedOption: String?,
    onOptionSelected: (String?) -> Unit,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Box(modifier = modifier) {
        Button(
            onClick = { expanded = true },
            colors = ButtonDefaults.buttonColors(containerColor = Color.White),
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = selectedOption ?: label, // shows category or selection
                    color = if (selectedOption != null) MaterialTheme.colorScheme.primary else Color.Gray,
                    fontSize = 12.sp,
                    maxLines = 1
                )
                Icon(
                    imageVector = Icons.Default.ArrowDropDown,
                    contentDescription = null,
                    tint = Color.Gray
                )
            }
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .background(Color.White)
                .heightIn(max = 300.dp) // limit height to avoid overflow
        ) {
            // Option to clear filter
            DropdownMenuItem(
                text = { Text(stringResource(R.string.all, label), fontWeight = FontWeight.Bold) },
                onClick = {
                    onOptionSelected(null)
                    expanded = false
                })
            HorizontalDivider()

            options.forEach { option ->
                DropdownMenuItem(text = { Text(option) }, onClick = {
                    onOptionSelected(option)
                    expanded = false
                })
            }
        }
    }
}