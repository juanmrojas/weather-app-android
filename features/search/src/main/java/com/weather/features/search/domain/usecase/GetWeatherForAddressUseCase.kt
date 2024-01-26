package com.weather.features.search.domain.usecase

import android.util.Log
import com.weather.data.search.datastore.WeatherDataStore
import com.weather.features.search.domain.model.Weather
import com.weather.features.search.domain.util.ConversionUtil.mapToFahrenheit
import com.weather.weather_api.WeatherProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

internal class GetWeatherForAddressUseCase @Inject constructor(
    private val weatherProvider: WeatherProvider,
    private val weatherDataStore: WeatherDataStore
): GetWeatherUseCase<String> {

    override fun invoke(data: String): Flow<Weather> {
        return getWeatherProviderForAddress(data.split(","))
            .onEach { dataModel ->
                weatherDataStore.saveData(
                    WeatherDataStore.SEARCH_LAST_SAVED_COORD,
                    dataModel.latitude.toString() + "," + dataModel.longitude.toString()
                )
            }.map { dataModel ->
                Weather(
                    dataModel.city,
                    dataModel.temperature.mapToFahrenheit(),
                    dataModel.iconCode.toIntOrNull() ?: 0 ,
                    false
                )
            }.catch { error ->
                Log.d("Weather Error", error.cause?.message.toString())
            }.flowOn(Dispatchers.IO)
    }

    private fun getWeatherProviderForAddress(addressArray: List<String>) =
        if (addressArray.size == 3) {
            weatherProvider.fetchWeatherForAddress(
                addressArray[0].trim(),
                addressArray[1].trim(),
                addressArray[2].trim()
            )
        } else if (addressArray.size == 2 && addressArray[1].uppercase() == "US") {
            weatherProvider.fetchWeatherForAddress(
                city = addressArray[0].trim(),
                country = addressArray[1].trim()
            )
        } else if (addressArray.size == 2) {
            weatherProvider.fetchWeatherForAddress(
                city = addressArray[0].trim(),
                state = addressArray[1].trim()
            )
        } else {
            weatherProvider.fetchWeatherForAddress(city = addressArray[0].trim())
        }
}