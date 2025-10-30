package com.irpania.recetas.data

class FakeUserPreferencesRepository : PreferencesRepository {

    private var onboardingCompleted = false

    override fun isOnboardingCompleted(): Boolean {
        return onboardingCompleted
    }

    override fun setOnboardingCompleted() {
        onboardingCompleted = true
    }
}
