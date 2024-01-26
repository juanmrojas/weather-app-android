package com.weather.features.search.domain.usecase

import com.weather.data.search.datastore.WeatherDataStore
import com.weather.data.search.datastore.WeatherDataStore.Companion.SEARCH_LAST_SAVED_COORD
import com.weather.location.LocationProvider
import com.weather.features.search.domain.model.Weather
import com.weather.features.search.domain.util.ConversionUtil.mapToFahrenheit
import com.weather.weather_api.WeatherProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class GetLastSavedWeatherUseCase @Inject constructor(
    private val weatherProvider: WeatherProvider,
    private val weatherDataStore: WeatherDataStore,
    private val locationProvider: LocationProvider
) {

    operator fun invoke(): Flow<Weather> {
        return weatherDataStore.getData(SEARCH_LAST_SAVED_COORD, "44.34,10.99")
            .filter { it.isNotEmpty() }
            .map { coordString ->
                val coordinates = coordString.split(",")
                coordinates[0].toDouble() to coordinates[1].toDouble()
            }.flatMapLatest { coordinates ->
                weatherProvider.fetchWeatherForCoordinates(coordinates.first, coordinates.second)
            }.map {
                Weather(it.city, it.temperature.mapToFahrenheit(), it.iconCode.toIntOrNull() ?: 0, locationProvider.isLocationPermissionGranted())
            }.flowOn(Dispatchers.IO)
    }
}