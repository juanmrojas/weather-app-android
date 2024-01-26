package com.weather.features.search.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.weather.features.search.domain.di.WeatherAddress
import com.weather.features.search.domain.di.WeatherCoord
import com.weather.features.search.domain.model.Weather
import com.weather.features.search.domain.usecase.GetWeatherUseCase
import com.weather.features.search.domain.usecase.GetLastSavedWeatherUseCase
import com.weather.location.LocationProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherSearchViewModel @Inject constructor(
    @WeatherAddress private val getWeatherForAddressUseCase: GetWeatherUseCase<String>,
    @WeatherCoord private val getWeatherForCoordUseCase: GetWeatherUseCase<Nothing>,
    private val locationProvider: LocationProvider,
    private val getLastSavedWeatherUseCase: GetLastSavedWeatherUseCase
) : ViewModel() {

    val uiState = mutableStateOf<SearchUiState>(SearchUiState.Loading)
    val address = mutableStateOf("")
    val shouldRequestForPermission = mutableStateOf(false)

    init {
        viewModelScope.launch {
            getLastSavedWeatherUseCase()
                .onStart { uiState.value = SearchUiState.Loading }
                .onEach { uiState.value = SearchUiState.Success(it) }
                .catch {
                    //JUAN: it's almost a rule of thumbs to have catch operator at the consumer level
                    // that way we prevent any crashes coming from downstream
                    it.printStackTrace()
                    uiState.value = SearchUiState.Error
                }.collect()
        }
    }

    //JUAN: When the location related permissions are granted then we rely on the
    // GetWeatherForCoordUseCase which is responsible for fetching the last known
    // device's location thus we can make a refresh call to retrieve the weather info
    // for the location
    fun onSearchClicked() {
        viewModelScope.launch {

            val weatherFlow = takeIf { locationProvider.isLocationPermissionGranted() }?.run {
                getWeatherForCoordUseCase()
            } ?: getWeatherForAddressUseCase(address.value)

            weatherFlow
                .onStart { uiState.value = SearchUiState.Loading }
                .onEach {
                    uiState.value = SearchUiState.Success(it) }
                .catch {
                    uiState.value = SearchUiState.Error
                    it.printStackTrace()
                }
                .collect()
        }
    }

    //JUAN: if I had a bit more time I would have come up with a regex to validate
    // the character input and thus ensure that the format for the value conforms
    // to the city, state code, country code one.
    fun onTextChange(value: String) {
        address.value = value
    }
}

sealed interface SearchUiState {

    object Loading : SearchUiState
    object Error : SearchUiState

    data class Success(val data: Weather) : SearchUiState
}