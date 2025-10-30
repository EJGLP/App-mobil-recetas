package com.irpania.recetas.data.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [FavoriteMeal::class], version = 1, exportSchema = false)
abstract class RecetasDatabase : RoomDatabase() {
    abstract fun favoriteMealDao(): FavoriteMealDao
}
