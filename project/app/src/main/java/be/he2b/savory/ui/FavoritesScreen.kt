package be.he2b.savory.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import be.he2b.savory.R
import be.he2b.savory.ui.components.SmallRecipeCard
import be.he2b.savory.ui.viewmodel.FavoritesViewModel

@Composable
fun FavoritesScreen(
    navController: NavHostController, modifier: Modifier = Modifier
) {
    val viewModel: FavoritesViewModel = viewModel()
    val favoriteList by viewModel.favorites.collectAsState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {

        LazyColumn(
            modifier = Modifier.fillMaxSize(), contentPadding = PaddingValues(bottom = 80.dp)
        ) {

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
                            .padding(start = 12.dp, top = 36.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = stringResource(R.string.configuration),
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    Text(
                        text = stringResource(R.string.my_favorites),
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(start = 24.dp, bottom = 24.dp)
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            if (favoriteList.isEmpty()) {
                // Case 1 -> empty list
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 64.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = stringResource(R.string.empty_favorites_list),
                            fontSize = 18.sp,
                            color = MaterialTheme.colorScheme.onBackground,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            } else {
                items(favoriteList) { recipe ->
                    SmallRecipeCard(
                        recipe = recipe,
                        onClick = { navController.navigate("${ProjectScreen.Detail.name}/${recipe.id}") },
                        onFavoriteClick = { viewModel.toggleFavorite(recipe) })
                }
            }
        }
    }
}