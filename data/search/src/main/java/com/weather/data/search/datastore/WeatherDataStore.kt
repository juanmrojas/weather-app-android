package com.weather.data.search.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WeatherDataStore @Inject constructor(private val dataStore: DataStore<Preferences>) {

    fun <T> getData(
        key: Preferences.Key<T>,
        defaultValue: T
    ): Flow<T> =
        dataStore.data.map { preferences ->
            preferences[key] ?: defaultValue
        }

    suspend fun <T> saveData(key: Preferences.Key<T>, value: T) {
        dataStore.edit { it[key] = value }
    }

    companion object {
        val SEARCH_LAST_SAVED_COORD = stringPreferencesKey("search_last_saved_coord")
    }
}