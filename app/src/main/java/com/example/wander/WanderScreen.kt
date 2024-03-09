package com.example.wander

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.wander.ui.Greetingview
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
            Greetingview()
        }
        composable(route = WanderScreen.selectlist.name) {
            palceApp()
        }
    }

}

