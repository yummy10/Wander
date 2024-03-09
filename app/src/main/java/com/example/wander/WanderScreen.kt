package com.example.wander

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.wander.ui.Greeting

import com.example.wander.ui.WViewModel
import com.example.wander.ui.palceApp

enum class WanderScreen {
    greeting,
    selectlist
}
@Preview(showBackground = true)
@Composable
fun WanderApp(
    viewModel: WViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = WanderScreen.greeting.name,
        modifier = Modifier
    ) {
        composable(route = WanderScreen.greeting.name) {
            Greeting(message= stringResource(R.string.app_name),
                continueButtonClicked = {navController.navigate(WanderScreen.selectlist.name)},
                modifier = Modifier.verticalScroll(rememberScrollState())//建立垂直捲軸,
                    .fillMaxSize()
                    .wrapContentSize(Alignment.Center
                    )
            )
        }
        composable(route = WanderScreen.selectlist.name) {
            palceApp()
        }
    }

}

