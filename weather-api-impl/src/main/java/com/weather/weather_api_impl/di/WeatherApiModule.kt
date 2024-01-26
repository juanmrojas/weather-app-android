package com.weather.weather_api_impl.di

import com.weather.weather_api.WeatherProvider
import com.weather.weather_api_impl.WeatherProviderImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class WeatherApiModule {

    @Binds
    internal abstract fun bindWeatherProvider(bind: WeatherProviderImpl): WeatherProvider
}