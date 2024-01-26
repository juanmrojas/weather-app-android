package com.core.network

import com.core.network.model.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherServiceApi {

    @GET("weather")
    suspend fun fetchLatLongWeather(
        @Query("appId") appId: String,
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double
    ): WeatherResponse

    @GET("weather")
    suspend fun fetchWeatherForAddress(
        @Query("appId") appId: String,
        @Query("q") address: String
    ): WeatherResponse
}
