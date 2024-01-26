package com.weather.features.search.viewmodel

import com.weather.ViewModelScopeTestRule
import com.weather.features.search.domain.model.Weather
import com.weather.features.search.domain.usecase.GetLastSavedWeatherUseCase
import com.weather.features.search.domain.usecase.GetWeatherUseCase
import com.weather.location.LocationProvider
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flow
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class WeatherSearchViewModelTest {

    @get:Rule
    val viewModelScopeTestRule = ViewModelScopeTestRule()

    private val getWeatherForAddressUseCase = mockk<GetWeatherUseCase<String>>()
    private val getWeatherForCoordUseCase = mockk<GetWeatherUseCase<Nothing>>()
    private val weather = mockk<Weather>()
    private val getLastSavedWeatherUseCase = mockk<GetLastSavedWeatherUseCase>()
    private val locationProvider = mockk<LocationProvider>()
    private lateinit var subject: WeatherSearchViewModel

    @Before
    fun setup() {
        every { getLastSavedWeatherUseCase.invoke() } returns flow { emit(weather) }

        subject = WeatherSearchViewModel(
            getWeatherForAddressUseCase,
            getWeatherForCoordUseCase,
            locationProvider,
            getLastSavedWeatherUseCase
        )
    }

    @Test
    fun onSearchClicked_givenLocationPermissionIsGrantedAsTrueAndSuccessOnGetWeatherForCoordUseCase_fetchedSuccessUiState() {
        every { locationProvider.isLocationPermissionGranted() } returns true
        every { getWeatherForCoordUseCase.invoke() } returns flow { emit(weather) }

        subject.onSearchClicked()

        assertEquals(subject.uiState.value, SearchUiState.Success(weather))
        verify(exactly = 1) {
            getLastSavedWeatherUseCase.invoke()
        }
        verify(exactly = 0) {
            getWeatherForAddressUseCase.invoke()
        }
    }

    @Test
    fun onSearchClicked_givenLocationPermissionIsGrantedAsTrueAndErrorOnGetWeatherForCoordUseCase_fetchedErrorUiState() {
        every { locationProvider.isLocationPermissionGranted() } returns true
        every { getWeatherForCoordUseCase.invoke() } returns flow { throw Exception() }

        subject.onSearchClicked()

        assertEquals(subject.uiState.value, SearchUiState.Error)
        verify(exactly = 1) {
            getLastSavedWeatherUseCase.invoke()
        }
        verify(exactly = 0) {
            getWeatherForAddressUseCase.invoke()
        }
    }

    @Test
    fun onSearchClicked_givenLocationPermissionIsGrantedAsFalseAndSuccessOnGetWeatherForAddressUseCase_fetchedSuccessUiState() {
        every { locationProvider.isLocationPermissionGranted() } returns false
        every { getWeatherForAddressUseCase.invoke("San Antonio, TX, US") } returns flow { emit(weather) }

        subject.address.value = "San Antonio, TX, US"
        subject.onSearchClicked()

        assertEquals(subject.uiState.value, SearchUiState.Success(weather))
        verify(exactly = 1) {
            getLastSavedWeatherUseCase.invoke()
        }
        verify(exactly = 0) {
            getWeatherForCoordUseCase.invoke()
        }
    }

    @Test
    fun onSearchClicked_givenLocationPermissionIsGrantedAsFalseAndErrorOnGetWeatherForAddressUseCase_fetchedErrorUiState() {
        every { locationProvider.isLocationPermissionGranted() } returns false
        every { getWeatherForAddressUseCase.invoke("San Antonio, TX, US") } returns flow { throw Exception() }

        subject.address.value = "San Antonio, TX, US"
        subject.onSearchClicked()

        assertEquals(subject.uiState.value, SearchUiState.Error)
        verify(exactly = 1) {
            getLastSavedWeatherUseCase.invoke()
        }
        verify(exactly = 0) {
            getWeatherForCoordUseCase.invoke()
        }
    }
}