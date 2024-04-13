package com.example.wander.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.wander.R
import com.example.wander.model.City
import com.example.wander.network.BASE_URL
import com.example.wander.ui.NetsUiState
import com.example.wander.ui.WViewModel
import com.example.wander.ui.components.ErrorScreen
import com.example.wander.ui.components.LoadingScreen
import com.example.wander.ui.components.WanderBottomNavigation
import com.example.wander.ui.components.WanderTopAppBar


@Composable
fun CityList(
    continueButtonClicked: () -> Unit,
    backButtonClicked: () -> Unit,
    navController: NavHostController,
    wViewModel: WViewModel,
    modifier: Modifier = Modifier
) {
    val cities by wViewModel.cities.collectAsState(initial = emptyList())

    Scaffold(bottomBar = { WanderBottomNavigation(navController) },
        topBar = { WanderTopAppBar(backButtonClicked = backButtonClicked) }) {
        LazyColumn(contentPadding = it, modifier = modifier) {
            items(cities) { city ->
                CityItem(
                    city = city,
                    continueButtonClicked = continueButtonClicked,
                    wViewModel = wViewModel,
                    modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_small))
                )
            }
        }
    }
}

@Composable
fun CityItem(
    city: City,
    continueButtonClicked: () -> Unit,
    wViewModel: WViewModel,
    modifier: Modifier = Modifier
) {
    Card(modifier = modifier) {
        Column {
            Image(
                painter = rememberAsyncImagePainter(model = "$BASE_URL${city.cityImagePath}"),
                contentDescription = city.cityName,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(194.dp)
                    .clickable {
                        ToSelectList(city, wViewModel)
                        continueButtonClicked()
                    },
                contentScale = ContentScale.Crop
            )
            Text(
                text = city.cityName,
                style = MaterialTheme.typography.displayMedium,
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_small))
            )
        }
    }
}

fun ToSelectList(city: City, wViewModel: WViewModel) {
    wViewModel.getSearchPlaces(city = city.cityName, name = null)
    wViewModel.setCity(city)
}
