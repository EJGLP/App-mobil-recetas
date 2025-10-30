package com.irpania.recetas.data

interface PreferencesRepository {
    fun isOnboardingCompleted(): Boolean
    fun setOnboardingCompleted()
}
