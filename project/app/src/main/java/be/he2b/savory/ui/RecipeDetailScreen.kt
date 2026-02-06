package be.he2b.savory.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import be.he2b.savory.R
import be.he2b.savory.ui.viewmodel.RecipeDetailViewModel
import coil.compose.AsyncImage

@Composable
fun RecipeDetailScreen(
    recipeId: String?, onBackClick: () -> Unit
) {
    val viewModel: RecipeDetailViewModel = viewModel()

    val recipe by viewModel.recipe.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(recipeId) {
        if (recipeId != null) {
            viewModel.fetchRecipeById(recipeId)
        }
    }

    // Case error
    if (errorMessage != null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = errorMessage ?: stringResource(R.string.error),
                    color = MaterialTheme.colorScheme.error,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(16.dp),
                    textAlign = TextAlign.Center
                )
                Button(
                    onClick = onBackClick, colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text("Return", color = MaterialTheme.colorScheme.onPrimary)
                }
            }
        }
    }
    // CASE RECIPE FOUND
    // We check if the recipe exists AND if the app is not currently loading
    else if (recipe.takeIf { !isLoading } != null) {

        // Using 'let' to safely access the recipe as 'currentRecipe'
        recipe?.let { currentRecipe ->

            // Persists the scroll position of the recipe details across recompositions
            val scrollState = rememberScrollState()

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                AsyncImage(
                    model = currentRecipe.imageUrl,
                    contentDescription = stringResource(R.string.recipe_image),
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                )

                // buttons <- et <3
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp, start = 16.dp, end = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    GlassIconButton(
                        icon = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Return",
                        onClick = onBackClick,
                        tint = Color.Black
                    )

                    val heartTint =
                        if (currentRecipe.isFavorite) Color.Red else MaterialTheme.colorScheme.secondary

                    GlassIconButton(
                        icon = if (currentRecipe.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favorites",
                        onClick = { viewModel.toggleFavorite(currentRecipe) },
                        tint = heartTint
                    )
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 260.dp)
                        .clip(RoundedCornerShape(topStart = 30.dp, topEnd = 30.dp))
                        .background(MaterialTheme.colorScheme.surface)
                        .verticalScroll(scrollState)
                        .padding(24.dp)
                ) {
                    Text(
                        text = currentRecipe.title,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface,
                        lineHeight = 32.sp
                    )

                    Text(
                        text = stringResource(R.string.category, currentRecipe.category),
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                        modifier = Modifier.padding(top = 4.dp)
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 24.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        InfoItem(
                            icon = Icons.Default.Place, text = currentRecipe.area
                        )

                        val infoText =
                            if (currentRecipe.tags.isNotEmpty()) currentRecipe.tags.first() else currentRecipe.category
                        InfoItem(
                            icon = Icons.Default.Info, text = infoText
                        )
                    }

                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                    Spacer(modifier = Modifier.height(16.dp))

                    IngredientsSection(ingredients = currentRecipe.ingredients)

                    Spacer(modifier = Modifier.height(24.dp))
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                    Spacer(modifier = Modifier.height(16.dp))

                    InstructionsSection(instructions = currentRecipe.instructions)

                    Spacer(modifier = Modifier.height(50.dp))
                }
            }
        }
    }
    // Case charging
    else {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
        }
    }
}

@Composable
fun IngredientsSection(ingredients: List<String>) {
    Text(
        text = stringResource(R.string.ingredients),
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.padding(bottom = 12.dp)
    )

    if (ingredients.isNotEmpty()) {
        ingredients.forEach { ingredient ->
            IngredientRow(text = ingredient)
        }
    } else {
        Text(
            text = stringResource(R.string.unavailable_ingredients),
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            fontSize = 14.sp,
            fontStyle = FontStyle.Italic
        )
    }
}

@Composable
fun InstructionsSection(instructions: List<String>) {
    Text(
        text = stringResource(R.string.instructions), // Note: Assurez-vous d'utiliser R.string.instructions ici
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier.padding(bottom = 12.dp)
    )

    if (instructions.isNotEmpty()) {
        instructions.forEach { step ->
            StepRow(text = step)
        }
    } else {
        Text(
            text = stringResource(R.string.no_instructions_available),
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            fontStyle = FontStyle.Italic
        )
    }
}

@Composable
fun InfoItem(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(20.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
fun IngredientRow(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .background(
                    MaterialTheme.colorScheme.primary, CircleShape
                )
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            text = text, fontSize = 16.sp, color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun StepRow(text: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = stringResource(R.string.bar),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.width(25.dp),
            textAlign = TextAlign.Center
        )
        Text(
            text = text,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurface,
            lineHeight = 22.sp
        )
    }
}

@Composable
fun GlassIconButton(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    contentDescription: String,
    onClick: () -> Unit,
    tint: Color = Color.Black
) {
    IconButton(
        onClick = onClick, modifier = Modifier
            .size(40.dp)
            .background(
                Color.White.copy(alpha = 0.7f), CircleShape
            )
    ) {
        Icon(
            imageVector = icon, contentDescription = contentDescription, tint = tint
        )
    }
}