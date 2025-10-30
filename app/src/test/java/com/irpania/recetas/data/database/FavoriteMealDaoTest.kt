package com.irpania.recetas.data.database

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner

@RunWith(RobolectricTestRunner::class)
class FavoriteMealDaoTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: RecetasDatabase
    private lateinit var dao: FavoriteMealDao

    @Before
    fun setup() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, RecetasDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = database.favoriteMealDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun `Probar que inserta un favorito`() = runBlocking {
        val favoriteMeal = FavoriteMeal("123")
        dao.insertFavoriteMeal(favoriteMeal)

        val favorites = dao.getFavoriteMeals().first()
        assertThat(favorites).contains(favoriteMeal)
    }

    @Test
    fun `Probar que elimina un favorito`() = runBlocking {
        val favoriteMeal = FavoriteMeal("123")
        dao.insertFavoriteMeal(favoriteMeal)
        dao.deleteFavoriteMeal(favoriteMeal)

        val favorites = dao.getFavoriteMeals().first()
        assertThat(favorites).doesNotContain(favoriteMeal)
    }
}
