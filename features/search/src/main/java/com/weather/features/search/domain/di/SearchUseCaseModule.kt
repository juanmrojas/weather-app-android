package com.weather.features.search.domain.di

import com.weather.features.search.domain.usecase.GetWeatherForAddressUseCase
import com.weather.features.search.domain.usecase.GetWeatherForCoordinateUseCase
import com.weather.features.search.domain.usecase.GetWeatherUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Qualifier

@Module
@InstallIn(SingletonComponent::class)
abstract class SearchUseCaseModule {

    @Binds
    @WeatherAddress
    internal abstract fun bindGetWeatherUseCaseForAddress(bind: GetWeatherForAddressUseCase): GetWeatherUseCase<String>

    @Binds
    @WeatherCoord
    internal abstract fun bindGetWeatherUseCaseForCoord(bind: GetWeatherForCoordinateUseCase): GetWeatherUseCase<Nothing>
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class WeatherAddress

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class WeatherCoord