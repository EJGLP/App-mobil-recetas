package com.irpania.recetas.data.network

import retrofit2.http.GET
import retrofit2.http.Query

interface MealDbApi {
    //Harcodeado a la categoria italiana porque cobran por listar todas las recectas
    @GET("api/json/v1/1/filter.php?a=italian")
    suspend fun getMeals(): MealsResponse

    @GET("api/json/v1/1/lookup.php")
    suspend fun getMealDetails(@Query("i") mealId: String): MealDetailResponse
}
