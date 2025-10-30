package com.irpania.recetas.data.database

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class FakeFavoriteMealDao : FavoriteMealDao {

    private val _favoriteMeals = MutableStateFlow<List<FavoriteMeal>>(emptyList())

    override fun getFavoriteMeals(): Flow<List<FavoriteMeal>> {
        return _favoriteMeals.asStateFlow()
    }

    override suspend fun insertFavoriteMeal(favoriteMeal: FavoriteMeal) {
        _favoriteMeals.update { it + favoriteMeal }
    }

    override suspend fun deleteFavoriteMeal(favoriteMeal: FavoriteMeal) {
        _favoriteMeals.update { it - favoriteMeal }
    }
}
