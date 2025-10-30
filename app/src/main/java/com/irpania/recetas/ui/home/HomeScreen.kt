package com.irpania.recetas.ui.home

import android.content.res.Configuration
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.RestaurantMenu
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.irpania.recetas.R
import com.irpania.recetas.data.network.Meal
import com.irpania.recetas.ui.theme.RecetasTheme

@Composable
fun HomeScreen(
    uiState: HomeScreenUiState,
    onToggleFavorite: (String) -> Unit,
    onMealClick: (String) -> Unit,
    showFavoritesOnly: Boolean = false
) {
    val mealsToShow = if (showFavoritesOnly) {
        uiState.meals.filter { uiState.favoriteMealIds.contains(it.id) }
    } else {
        uiState.meals
    }

    if (mealsToShow.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Outlined.RestaurantMenu,
                    contentDescription = null,
                    modifier = Modifier.size(96.dp),
                    tint = MaterialTheme.colorScheme.surfaceVariant
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = stringResource(id = R.string.empty_list_placeholder))
            }
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            items(mealsToShow, key = { it.id }) { meal ->
                MealItem(
                    meal = meal,
                    isFavorite = uiState.favoriteMealIds.contains(meal.id),
                    onToggleFavorite = { onToggleFavorite(meal.id) },
                    onMealClick = { onMealClick(meal.id) }
                )
            }
        }
    }
}

@Composable
fun MealItem(
    meal: Meal,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onMealClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .padding(vertical = 8.dp) // Changed padding for better spacing
            .clickable(onClick = onMealClick)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = meal.imageUrl,
                    contentDescription = null,
                    modifier = Modifier.size(100.dp),
                    contentScale = ContentScale.Crop
                )
                Column(modifier = Modifier.padding(start = 16.dp, end = 48.dp)) {
                    Text(
                        text = meal.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(//En realidad no sale del api es un aleatorio porque no traia
                        // descripciones este api y me di cuenta muy tarde.
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        text = meal.getRandomDescription()
                    )
                }
            }
            IconButton(
                onClick = onToggleFavorite,
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Icon(
                    imageVector = if (isFavorite) Icons.Filled.Star else Icons.Outlined.Star,
                    contentDescription = stringResource(id = R.string.togle_favorite_action),
                    tint = if (isFavorite) Color.Yellow else MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun MealItemLightPreview() {
    RecetasTheme {
        MealItem(
            meal = Meal(id = "1", name = "Receta en modo claro", imageUrl = ""),
            isFavorite = true,
            onToggleFavorite = {},
            onMealClick = {}
        )
    }
}

@Preview(showBackground = true, uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun MealItemDarkPreview() {
    RecetasTheme {
        MealItem(
            meal = Meal(id = "1", name = "Receta en modo oscuro", imageUrl = ""),
            isFavorite = false,
            onToggleFavorite = {},
            onMealClick = {}
        )
    }
}
