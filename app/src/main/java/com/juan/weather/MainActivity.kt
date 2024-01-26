package com.juan.weather

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.juan.weather.ui.theme.WeatherTheme
import com.weather.features.search.screen.WeatherSearchScreen
import com.weather.location.LocationProvider
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var locationProvider: LocationProvider

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        locationProvider.fetchUserLocation()
        setContent {
            WeatherTheme {
                WeatherSearchScreen()
            }
        }
    }
}