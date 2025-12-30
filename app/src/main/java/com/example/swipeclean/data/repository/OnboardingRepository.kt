package com.example.swipeclean.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

private const val ON_BOARDING_NAME = "onboarding_preferences"
private const val ON_BOARDING_KEY = "onboarding_completed"

val Context.onboardingDataStore: DataStore<Preferences> by preferencesDataStore(name = ON_BOARDING_NAME)

class OnboardingRepository(private val context: Context) {

    private object PreferencesKey {
        val onBoardingKey = booleanPreferencesKey(ON_BOARDING_KEY)
    }

    private val dataStore = context.onboardingDataStore

    suspend fun saveOnBoardingState(isCompleted: Boolean) {
        dataStore.edit { prefs ->
            prefs[PreferencesKey.onBoardingKey] = isCompleted
        }
    }

    fun readOnBoardingState(): Flow<Boolean> = dataStore.data
        .catch { e ->
            if (e is IOException) emit(emptyPreferences())
            else throw e
        }
        .map { prefs ->
            prefs[PreferencesKey.onBoardingKey] ?: false
        }
}