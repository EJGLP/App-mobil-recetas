package com.irpania.recetas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.irpania.recetas.data.PreferencesRepository
import com.irpania.recetas.data.database.FavoriteMeal
import com.irpania.recetas.data.database.FavoriteMealDao
import com.irpania.recetas.data.network.MealDbApi
import com.irpania.recetas.ui.home.HomeScreenUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val preferencesRepository: PreferencesRepository,
    private val mealDbApi: MealDbApi,
    private val favoriteMealDao: FavoriteMealDao
) : ViewModel() {

    private val _isLoading = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    private val _showOnboarding = MutableStateFlow(false)
    val showOnboarding = _showOnboarding.asStateFlow()

    private val _homeScreenState = MutableStateFlow(HomeScreenUiState())
    val homeScreenState = _homeScreenState.asStateFlow()

    init {
        viewModelScope.launch {
            delay(2000)
            _showOnboarding.value = !preferencesRepository.isOnboardingCompleted()
            if (!preferencesRepository.isOnboardingCompleted()) {
                _isLoading.value = false
            } else {
                fetchMeals()
            }
        }

        viewModelScope.launch {
            favoriteMealDao.getFavoriteMeals().collect { favoriteMeals ->
                _homeScreenState.update { it.copy(favoriteMealIds = favoriteMeals.map { it.id }.toSet()) }
            }
        }
    }

    private fun fetchMeals() {
        viewModelScope.launch {
            try {
                val meals = mealDbApi.getMeals().meals
                _homeScreenState.update { it.copy(meals = meals) }
            } catch (e: Exception) {
                // Handle error
            }
            _isLoading.value = false
        }
    }

    fun setOnboardingCompleted() {
        preferencesRepository.setOnboardingCompleted()
        _showOnboarding.value = false
        fetchMeals()
    }

    fun toggleFavorite(mealId: String) {
        viewModelScope.launch {
            if (_homeScreenState.value.favoriteMealIds.contains(mealId)) {
                favoriteMealDao.deleteFavoriteMeal(FavoriteMeal(mealId))
            } else {
                favoriteMealDao.insertFavoriteMeal(FavoriteMeal(mealId))
            }
        }
    }
}
