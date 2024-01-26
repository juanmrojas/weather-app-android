package com.weather.weather_api

import com.weather.data.search.model.Weather
import kotlinx.coroutines.flow.Flow

interface WeatherProvider {

    fun fetchWeatherForAddress(
        city: String? = null,
        state: String? = null,
        country: String? = null
    ): Flow<Weather>

    fun fetchWeatherForCoordinates(latitude: Double, longitude: Double): Flow<Weather>
}