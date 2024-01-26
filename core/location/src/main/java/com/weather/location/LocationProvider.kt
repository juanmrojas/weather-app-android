package com.weather.location

import android.location.Location
import kotlinx.coroutines.flow.Flow
import java.util.*

interface LocationProvider {

    val lastKnownLocation: Flow<Optional<Location>>

    fun isLocationPermissionGranted(): Boolean

    fun fetchUserLocation()
}