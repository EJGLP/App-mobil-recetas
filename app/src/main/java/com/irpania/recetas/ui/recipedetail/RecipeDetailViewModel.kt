package com.irpania.recetas.ui.recipedetail

import androidx.annotation.StringRes
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.irpania.recetas.R
import com.irpania.recetas.data.network.MealDetail
import com.irpania.recetas.data.network.MealDbApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RecipeDetailUiState(
    val isLoading: Boolean = true,
    val meal: MealDetail? = null,
    @StringRes val error: Int? = null
)

@HiltViewModel
class RecipeDetailViewModel @Inject constructor(
    private val mealDbApi: MealDbApi,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(RecipeDetailUiState())
    val uiState = _uiState.asStateFlow()

    private val mealId: String = savedStateHandle.get<String>("mealId")!!

    init {
        fetchMealDetails()
    }

    private fun fetchMealDetails() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val response = mealDbApi.getMealDetails(mealId)
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        meal = response.meals.firstOrNull()
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = R.string.recipe_detail_load_error
                    )
                }
            }
        }
    }
}
