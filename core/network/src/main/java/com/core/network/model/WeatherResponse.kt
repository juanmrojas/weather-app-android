package com.core.network.model

import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    @SerializedName("coord")
    val coordinate: Coordinate,
    val weather: List<Weather>,
    val main: Main,
    val name: String
)
