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
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.wander.R
import com.example.wander.model.City
import com.example.wander.ui.components.WanderTopAppBar

@Composable
fun CityApp(continueButtonClicked: () -> Unit,wViewModel: WViewModel ){
    CityList(wViewModel,continueButtonClicked)

}

@Composable
fun CityList(wViewModel: WViewModel, continueButtonClicked: () -> Unit, modifier: Modifier = Modifier){

    Scaffold(
        topBar = {
            WanderTopAppBar()
        }
    ){
        LazyColumn(contentPadding = it,modifier = modifier) {
            items(City.entries) { place->
                CityItem(place,continueButtonClicked,wViewModel)
            }
        }
    }

}

@Composable
fun CityItem(city: City,continueButtonClicked: () -> Unit,wViewModel:WViewModel) {
    Column(modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_small))) {
        Image(
            painter = painterResource(id = city.imageResourceId),
            contentDescription = stringResource(id = city.stringResourceId),
            modifier = Modifier
                .fillMaxWidth()
                .height(194.dp)
                .clickable {ToSelctList(city,wViewModel)
                    continueButtonClicked()},
            contentScale = ContentScale.Crop
        )
        Text(text = city.name)
    }
}

fun ToSelctList(city: City,wViewModel:WViewModel){
    wViewModel.setCity(city)
}