package com.weather.features.search.domain.usecase

import com.weather.features.search.domain.model.Weather
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow

// JUAN: this uses case is responsible for determining the endpoint which we will fetch the
// weather information from based on whether the address is full, state, city or coordinates; it
// will call the corresponding function available from the WeatherProvider
interface GetWeatherUseCase<T> {

    operator fun invoke(data: T): Flow<Weather> {
        return emptyFlow()
    }

    operator fun invoke(): Flow<Weather> {
        return emptyFlow()
    }
}


