package com.weather.data.search.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.weather.data.search.datastore.WeatherDataStore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object WeatherDataStorageModule {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "weather_data_store_search")

    @[Singleton Provides]
    fun provideSearchStorage(@ApplicationContext context: Context) = WeatherDataStore(context.dataStore)
}