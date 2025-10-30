package com.irpania.recetas.di

import android.content.Context
import androidx.room.Room
import com.irpania.recetas.data.PreferencesRepository
import com.irpania.recetas.data.UserPreferencesRepository
import com.irpania.recetas.data.database.FavoriteMealDao
import com.irpania.recetas.data.database.RecetasDatabase
import com.irpania.recetas.data.network.MealDbApi
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Binds
    abstract fun bindPreferencesRepository(impl: UserPreferencesRepository): PreferencesRepository

    companion object {
        @Singleton
        @Provides
        fun provideRecetasDatabase(@ApplicationContext context: Context): RecetasDatabase {
            return Room.databaseBuilder(
                context,
                RecetasDatabase::class.java,
                "recetas_database"
            ).build()
        }

        @Singleton
        @Provides
        fun provideFavoriteMealDao(database: RecetasDatabase): FavoriteMealDao {
            return database.favoriteMealDao()
        }
    }
}
