package com.weather.features.search.domain.model

data class Weather(
    val cityName: String,
    val temperature: String,
    val imageCode: Int,
    val isLocationPermissionGranted: Boolean
)