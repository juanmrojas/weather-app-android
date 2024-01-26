package com.weather.weather_api_impl

import com.core.network.WeatherServiceApi
import com.weather.data.search.model.Weather
import com.weather.weather_api.WeatherProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class WeatherProviderImpl @Inject constructor(private val weatherServiceApi: WeatherServiceApi) :
    WeatherProvider {

    override fun fetchWeatherForAddress(
        city: String?,
        state: String?,
        country: String?
    ): Flow<Weather> {
        return flow<Weather> {
            val address = "$city,$state,$country"
            val weatherResponse = weatherServiceApi.fetchWeatherForAddress(APP_ID, address)
            emit(
                Weather(
                    weatherResponse.name,
                    weatherResponse.main.temp,
                    weatherResponse.coordinate.lat,
                    weatherResponse.coordinate.lon,
                    weatherResponse.weather[0].id
                )
            )
        }.flowOn(Dispatchers.IO)
    }

    override fun fetchWeatherForCoordinates(latitude: Double, longitude: Double): Flow<Weather> {
        return flow<Weather> {
            val weatherResponse = weatherServiceApi.fetchLatLongWeather(APP_ID, latitude, longitude)
            emit(
                Weather(
                    weatherResponse.name,
                    weatherResponse.main.temp,
                    weatherResponse.coordinate.lat,
                    weatherResponse.coordinate.lon,
                    weatherResponse.weather[0].id
                )
            )
        }.flowOn(Dispatchers.IO)
    }

    //JUAN: I kept the app id at this level simulating as if it was fetched from
    // an auth token provider; we could have hidden it a bit more by putting it
    // in the network module
    companion object {
        internal const val APP_ID = "e24bfdf20e2bb5b1a9588ee0aa3fe588"
    }
}