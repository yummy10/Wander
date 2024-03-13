package com.example.wander

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.wander.ui.AddPlaceScreen
import com.example.wander.ui.CityApp
import com.example.wander.ui.Greeting
import com.example.wander.ui.PalceApp
import com.example.wander.ui.SearchPlaceScreen
import com.example.wander.ui.WViewModel

enum class WanderScreen {
    Greeting,
    Citylist,
    Selectlist,
    Addplace,
    Select,
}

@Composable
fun WanderApp(
    viewModel: WViewModel = viewModel(),
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = WanderScreen.Select.name,
        modifier = Modifier
    ) {
        composable(route = WanderScreen.Greeting.name) {
            Greeting(viewModel,message= stringResource(R.string.app_name),
                continueButtonClicked = {navController.navigate(WanderScreen.Citylist.name)},
                modifier = Modifier.verticalScroll(rememberScrollState())//建立垂直捲軸,
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center
                    ),
            )
        }
        composable(route = WanderScreen.Citylist.name) {
            CityApp(continueButtonClicked = {navController.navigate(WanderScreen.Selectlist.name)},backButtonClicked = {navController.navigate(WanderScreen.Greeting.name)},viewModel)
        }
        composable(route = WanderScreen.Selectlist.name) {
            PalceApp(backButtonClicked = {navController.navigate(WanderScreen.Citylist.name)},navigateToAddPlaceScreen = {navController.navigate(WanderScreen.Addplace.name)},viewModel)
        }
        composable(route = WanderScreen.Addplace.name) {
            AddPlaceScreen(onBackPressed = {navController.navigate(WanderScreen.Citylist.name)},viewModel)
        }
        composable(route = WanderScreen.Select.name) {
            SearchPlaceScreen(backButtonClicked = {navController.navigate(WanderScreen.Greeting.name)},viewModel)
        }

    }

}

