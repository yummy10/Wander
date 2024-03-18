package com.example.wander.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import com.example.wander.ui.components.WanderBottomNavigation
import com.example.wander.ui.components.WanderTopAppBar

@Composable
fun CityApp(continueButtonClicked: () -> Unit, backButtonClicked:() -> Unit, navController: NavHostController, wViewModel: WViewModel ){
    CityList(backButtonClicked,wViewModel,navController,continueButtonClicked)

}

@Composable
fun CityList(backButtonClicked:() -> Unit,wViewModel: WViewModel, navController: NavHostController,continueButtonClicked: () -> Unit, modifier: Modifier = Modifier){
    val cities by wViewModel.cities.collectAsState(initial = emptyList())

    Scaffold(
        bottomBar = {
            WanderBottomNavigation(navController, wViewModel)
        },
        topBar = {
            WanderTopAppBar(backButtonClicked=backButtonClicked)
        }
    ){
        LazyColumn(contentPadding = it,modifier = modifier) {
            items(cities) { place->
                CityItem(place,continueButtonClicked,wViewModel)
            }
        }
    }

}

@Composable
fun CityItem(city: City,continueButtonClicked: () -> Unit,wViewModel:WViewModel) {
    Column(modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_small))) {
        Image(
            painter = rememberAsyncImagePainter(model = city.imageUrl),
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
        Text(text = city.cityName)
    }
}


fun ToSelectList(city: City,wViewModel:WViewModel){
    wViewModel.setCity(city.cityName)
}