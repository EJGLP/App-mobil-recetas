package com.irpania.recetas.ui.home

import com.irpania.recetas.data.network.Meal

data class HomeScreenUiState(
    val meals: List<Meal> = emptyList(),
    val favoriteMealIds: Set<String> = emptySet()
)
