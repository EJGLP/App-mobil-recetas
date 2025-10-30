package com.irpania.recetas.data.network

import com.google.gson.annotations.SerializedName
import kotlin.random.Random

data class Meal(
    @SerializedName("idMeal") val id: String,
    @SerializedName("strMeal") val name: String,
    @SerializedName("strMealThumb") val imageUrl: String
) {
    fun getRandomDescription(): String {
        val descriptions = listOf(
            "Una deliciosa receta, fácil de preparar y perfecta para cualquier ocasión.",
            "Sorprende a tu familia y amigos con este plato exquisito y lleno de sabor.",
            "Un clásico reinventado que se convertirá en uno de tus platos favoritos.",
            "Perfecto para una cena rápida entre semana, nutritivo y sabroso.",
            "Una explosión de sabores que te transportará a la auténtica cocina italiana."
        )
        return descriptions[Random.nextInt(descriptions.size)]
    }
}
