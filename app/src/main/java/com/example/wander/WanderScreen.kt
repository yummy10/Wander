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
import com.example.wander.ui.WViewModel
import com.example.wander.ui.screens.AccountScreen
import com.example.wander.ui.screens.AddComment
import com.example.wander.ui.screens.AddPlaceScreen
import com.example.wander.ui.screens.CityApp
import com.example.wander.ui.screens.Greeting
import com.example.wander.ui.screens.LoginScreen
import com.example.wander.ui.screens.MessageBoardScreen
import com.example.wander.ui.screens.PlaceApp
import com.example.wander.ui.screens.SearchPlaceScreen

enum class WanderScreen {
    Greeting,
    Citylist,
    Selectlist,
    Addplace,
    Search,
    Message,
    Addmessage,
    Login,
    Account
}

@Composable
fun WanderApp(
    viewModel: WViewModel = viewModel(),
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        navController = navController,
        startDestination = WanderScreen.Greeting.name,
        modifier = Modifier
    ) {
        composable(route = WanderScreen.Greeting.name) {
            Greeting(viewModel,message= stringResource(R.string.app_name),
                continueButtonClicked = {navController.navigate(WanderScreen.Login.name)},
                modifier = Modifier.verticalScroll(rememberScrollState())//建立垂直捲軸,
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center
                    ),
            )
        }
        composable(route = WanderScreen.Citylist.name) {
            CityApp(continueButtonClicked = {navController.navigate(WanderScreen.Selectlist.name)},backButtonClicked = {navController.navigate(WanderScreen.Greeting.name)},navController,viewModel,)
        }
        composable(route = WanderScreen.Selectlist.name) {
            PlaceApp(backButtonClicked = {navController.navigate(WanderScreen.Citylist.name)},navigateToAddPlaceScreen = {navController.navigate(WanderScreen.Addplace.name)},viewModel)
        }
        composable(route = WanderScreen.Addplace.name) {
            AddPlaceScreen(onBackPressed = {navController.navigate(WanderScreen.Citylist.name)},viewModel)
        }
        composable(route = WanderScreen.Search.name) {
            SearchPlaceScreen(backButtonClicked = {navController.navigate(WanderScreen.Greeting.name)},navController,viewModel)
        }
        composable(route = WanderScreen.Message.name) {
            MessageBoardScreen(viewModel,navController)
        }
        composable(route = WanderScreen.Addmessage.name) {
            AddComment(onBackPressed = {navController.navigate(WanderScreen.Citylist.name)},viewModel)
        }
        composable(route = WanderScreen.Login.name) {
            LoginScreen(wViewModel=viewModel,onLoginSuccess={navController.navigate(WanderScreen.Citylist.name)})
        }
        composable(route = WanderScreen.Account.name) {
            AccountScreen(wViewModel=viewModel,navController)
        }

    }

}

