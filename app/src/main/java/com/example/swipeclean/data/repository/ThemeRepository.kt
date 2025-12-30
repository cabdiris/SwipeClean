package com.example.swipeclean.data.repository


import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.swipeclean.utils.ThemeMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.themeDataStore: DataStore<Preferences> by preferencesDataStore("settings")

class ThemeRepository(private val context: Context) {

    companion object {
        private val THEME_KEY = stringPreferencesKey("theme_mode")
    }

    val themeFlow = context.themeDataStore.data.map { prefs ->
        when (prefs[THEME_KEY]) {
            ThemeMode.LIGHT.name -> ThemeMode.LIGHT
            ThemeMode.DARK.name -> ThemeMode.DARK
            else -> ThemeMode.SYSTEM
        }
    }

    suspend fun saveTheme(mode: ThemeMode) {
        context.themeDataStore.edit { prefs ->
            prefs[THEME_KEY] = mode.name
        }
    }
}
