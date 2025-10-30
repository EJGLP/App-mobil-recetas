package com.irpania.recetas.data

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserPreferencesRepository @Inject constructor(@ApplicationContext context: Context) : PreferencesRepository {
    private val sharedPreferences = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    override fun isOnboardingCompleted(): Boolean {
        return sharedPreferences.getBoolean("onboarding_completed", false)
    }

    override fun setOnboardingCompleted() {
        with(sharedPreferences.edit()) {
            putBoolean("onboarding_completed", true)
            apply()
        }
    }
}
