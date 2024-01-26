package com.weather.features.search.domain.usecase

import android.util.Log
import com.weather.data.search.datastore.WeatherDataStore
import com.weather.features.search.domain.model.Weather
import com.weather.features.search.domain.util.ConversionUtil.mapToFahrenheit
import com.weather.location.LocationProvider
import com.weather.weather_api.WeatherProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
internal class GetWeatherForCoordinateUseCase @Inject constructor(
    private val weatherProvider: WeatherProvider,
    private val weatherDataStore: WeatherDataStore,
    private val locationProvider: LocationProvider
) : GetWeatherUseCase<Nothing> {

    override fun invoke(): Flow<Weather> {
        return locationProvider.lastKnownLocation
            .filter { it.isPresent }
            .map { it.get() }
            .flatMapLatest { location ->
                Log.d("Weather - Juan - Coord", "${location.latitude},${location.longitude}")
                weatherProvider.fetchWeatherForCoordinates(location.latitude, location.longitude)
            }
            .onEach { dataModel ->
                weatherDataStore.saveData(
                    WeatherDataStore.SEARCH_LAST_SAVED_COORD,
                    dataModel.latitude.toString() + "," + dataModel.longitude.toString()
                )
            }.map { dataModel ->
                Weather(
                    dataModel.city,
                    dataModel.temperature.mapToFahrenheit(),
                    dataModel.iconCode.toIntOrNull() ?: 0,
                    false
                )
            }.catch { error ->
                Log.d("Weather Error", error.cause?.message.toString())
            }.flowOn(Dispatchers.IO)
    }
}