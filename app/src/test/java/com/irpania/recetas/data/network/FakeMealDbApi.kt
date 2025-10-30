package com.irpania.recetas.data.network

import com.irpania.recetas.data.network.Meal
import com.irpania.recetas.data.network.MealDbApi
import com.irpania.recetas.data.network.MealDetailResponse
import com.irpania.recetas.data.network.MealsResponse

class FakeMealDbApi : MealDbApi {
    override suspend fun getMeals(): MealsResponse {
        return MealsResponse(emptyList())
    }

    override suspend fun getMealDetails(mealId: String): MealDetailResponse {
        // Not needed for this test
        return MealDetailResponse(emptyList())
    }
}
