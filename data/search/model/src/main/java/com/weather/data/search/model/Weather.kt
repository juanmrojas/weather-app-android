package com.weather.data.search.model

data class Weather constructor(
    val city: String,
    val temperature: Double,
    val latitude: Double,
    val longitude: Double,
    val iconCode: String
)