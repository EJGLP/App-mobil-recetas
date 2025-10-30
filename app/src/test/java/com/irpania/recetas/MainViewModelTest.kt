package com.irpania.recetas

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import com.irpania.recetas.data.FakeUserPreferencesRepository
import com.irpania.recetas.data.database.FakeFavoriteMealDao
import com.irpania.recetas.data.database.FavoriteMeal
import com.irpania.recetas.data.network.FakeMealDbApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class MainViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: MainViewModel
    private lateinit var fakeMealDbApi: FakeMealDbApi
    private lateinit var fakeUserPreferencesRepository: FakeUserPreferencesRepository
    private lateinit var fakeFavoriteMealDao: FakeFavoriteMealDao

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        fakeMealDbApi = FakeMealDbApi()
        fakeUserPreferencesRepository = FakeUserPreferencesRepository()
        fakeFavoriteMealDao = FakeFavoriteMealDao()
        viewModel = MainViewModel(fakeUserPreferencesRepository, fakeMealDbApi, fakeFavoriteMealDao)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `Prueba añadir favorito`() = runTest {
        val mealId = "123"

        // Acción
        viewModel.toggleFavorite(mealId)

        // Ejecutar las corrutinas pendientes
        testDispatcher.scheduler.runCurrent()

        // Verificación
        val favorites = fakeFavoriteMealDao.getFavoriteMeals().first()
        assertThat(favorites).contains(FavoriteMeal(mealId))
    }

    @Test
    fun `Prueba quitar favorito`() = runTest {
        val mealId = "124"

        // Precondición: añadir el favorito primero
        fakeFavoriteMealDao.insertFavoriteMeal(FavoriteMeal(mealId))
        // Ejecutar la corrutina de `init` del ViewModel para que se entere del cambio
        testDispatcher.scheduler.runCurrent()

        // Acción
        viewModel.toggleFavorite(mealId)
        // Ejecutar la corrutina de `toggleFavorite`
        testDispatcher.scheduler.runCurrent()

        // Verificación
        val favorites = fakeFavoriteMealDao.getFavoriteMeals().first()
        assertThat(favorites).isEmpty()
    }
}
