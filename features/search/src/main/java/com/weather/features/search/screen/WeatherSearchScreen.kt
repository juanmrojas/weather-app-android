package com.weather.features.search.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.weather.features.search.viewmodel.SearchUiState
import com.weather.features.search.viewmodel.WeatherSearchViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherSearchScreen() {
    val viewModel = hiltViewModel<WeatherSearchViewModel>()
    val uiState = viewModel.uiState.value
    Scaffold(
        topBar = {
            ConstraintLayout(
                modifier = Modifier
                    .padding(start = 16.dp, top = 20.dp, end = 16.dp, bottom = 40.dp)
                    .fillMaxWidth()
            ) {
                val (editTextAddressRef, searchButtonRef) = createRefs()
                val horizontalChain = createHorizontalChain(
                    editTextAddressRef,
                    searchButtonRef,
                    chainStyle = ChainStyle.SpreadInside
                )
                constrain(horizontalChain) {
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                TextField(
                    modifier = Modifier.constrainAs(editTextAddressRef) {
                        start.linkTo(parent.start)
                        end.linkTo(searchButtonRef.start)
                    },
                    value = viewModel.address.value,
                    onValueChange = viewModel::onTextChange,
                    shape = RoundedCornerShape(16.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = Color.Blue,
                        disabledTextColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                ElevatedButton(modifier = Modifier.constrainAs(searchButtonRef) {
                    start.linkTo(editTextAddressRef.end)
                    end.linkTo(parent.end)
                }, onClick = viewModel::onSearchClicked) {
                    Text(text = "Search")
                }
            }
        }) {
        when (uiState) {
            SearchUiState.Loading -> Loading()
            is SearchUiState.Success -> SearchContent(paddingValues = it, viewModel, uiState)
            is SearchUiState.Error -> {}
        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun SearchContent(paddingValues: PaddingValues, viewModel: WeatherSearchViewModel, state: SearchUiState) {
    state as SearchUiState.Success

    val locationGroupPermissionState = rememberPermissionState(
        android.Manifest.permission.ACCESS_COARSE_LOCATION
    )
    LaunchedEffect(locationGroupPermissionState) {
        val permissionResult = locationGroupPermissionState.status
        if (!permissionResult.isGranted) {
            if (permissionResult.shouldShowRationale) {
                // Show a rationale if needed (optional)
            } else {
                locationGroupPermissionState.launchPermissionRequest()
            }
        }
    }

    Card(
        shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp),
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
    ) {
        ConstraintLayout(
            modifier = Modifier
                .padding(
                    start = 16.dp,
                    top = 48.dp,
                    end = 16.dp,
                    bottom = 50.dp
                )
                .fillMaxSize()
        ) {
            val (weatherImageRef, tempRef, cityRef, permTextRef) = createRefs()
            val verticalChain =
                createVerticalChain(
                    weatherImageRef,
                    tempRef,
                    cityRef,
                    chainStyle = ChainStyle.Packed
                )
            constrain(verticalChain) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
            }
            Image(
                painter = state.data.imageCode.mapToPainter,
                modifier = Modifier.constrainAs(weatherImageRef) {
                    top.linkTo(parent.top)
                    bottom.linkTo(tempRef.top)
                    centerHorizontallyTo(parent)
                    centerVerticallyTo(parent)
                },
                contentDescription = "icon representing the current weather for the city"
            )
            Text(modifier = Modifier.constrainAs(tempRef) {
                top.linkTo(parent.top)
                bottom.linkTo(cityRef.top)
                centerHorizontallyTo(parent)
                centerVerticallyTo(parent)
            }, text = state.data.temperature, fontSize = 42.sp, fontWeight = FontWeight.Bold)
            Text(modifier = Modifier.constrainAs(cityRef) {
                top.linkTo(tempRef.bottom)
                bottom.linkTo(parent.bottom)
                centerHorizontallyTo(parent)
                centerVerticallyTo(parent)
            }, text = state.data.cityName, fontSize = 24.sp, fontWeight = FontWeight.Medium)
            Text(
                modifier = Modifier.constrainAs(permTextRef) {
                    bottom.linkTo(parent.bottom)
                    centerHorizontallyTo(parent)
                },
                text = if (locationGroupPermissionState.status.isGranted) "Permissions Granted" else "Permission not granted",
                fontSize = 24.sp, fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun Loading() {
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val (indicatorRef, loadingRef) = createRefs()
        val verticalChain =
            createVerticalChain(indicatorRef, loadingRef, chainStyle = ChainStyle.Packed)
        constrain(verticalChain) {
            top.linkTo(parent.top)
            bottom.linkTo(parent.bottom)
        }
        Text(
            modifier = Modifier.constrainAs(loadingRef) {
                top.linkTo(parent.top)
                bottom.linkTo(parent.bottom)
                centerHorizontallyTo(parent)
                centerVerticallyTo(parent)
            },
            text = "Loading...",
            fontSize = 14.sp,
            fontWeight = FontWeight.Light
        )
    }
}

private val Int.mapToPainter: Painter
    @Composable
    get() = when (this) {
        200, 201, 202, 210, 211, 212, 221, 230, 231, 232 -> painterResource(id = com.weather.image_utils.R.drawable.w11d)
        300, 301, 302, 310, 311, 312, 313, 314, 321, 520, 521, 522, 531 -> painterResource(id = com.weather.image_utils.R.drawable.w09d)
        500, 501, 502, 503, 504 -> painterResource(id = com.weather.image_utils.R.drawable.w10d)
        511, 600, 601, 602, 611, 612, 613, 615, 616, 620, 621, 622 -> painterResource(id = com.weather.image_utils.R.drawable.w13d)
        701, 711, 721, 731, 741, 751, 761, 762, 771, 781 -> painterResource(id = com.weather.image_utils.R.drawable.w50d)
        800 -> painterResource(id = com.weather.image_utils.R.drawable.w01d)
        801 -> painterResource(id = com.weather.image_utils.R.drawable.w02d)
        802 -> painterResource(id = com.weather.image_utils.R.drawable.w03d)
        803, 804 -> painterResource(id = com.weather.image_utils.R.drawable.w04d)
        else -> {
            painterResource(id = com.weather.image_utils.R.drawable.w01d)
        }
    }

