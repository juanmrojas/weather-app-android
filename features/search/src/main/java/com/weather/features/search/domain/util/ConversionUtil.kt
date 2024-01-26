package com.weather.features.search.domain.util

import kotlin.math.roundToInt

object ConversionUtil {

    internal fun Double.mapToFahrenheit(): String {
        return ((this - 273.15) * 1.8 + 32.0).roundToInt().toString()
    }
}