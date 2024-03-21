package com.example.wander.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.wander.R
import com.example.wander.WanderScreen


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WanderTopAppBar(modifier: Modifier = Modifier,backButtonClicked:() -> Unit) {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        ),
        title = {
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.displayLarge

            )
        },
        navigationIcon = {
            IconButton(onClick = backButtonClicked) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back_button)
                )
            }
        },
        modifier = modifier
    )
}

@Composable
fun WanderBottomNavigation(
    navController: NavHostController,
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route


    BottomNavigation(
        backgroundColor = MaterialTheme.colorScheme.tertiaryContainer
    ){
        BottomNavigationItem(

            icon = { Icon(Icons.Filled.List, contentDescription = null) },
            label = { Text(stringResource(R.string.city_list)) },
            selected = currentRoute == WanderScreen.Citylist.name,
            onClick = {
                navController.navigate(WanderScreen.Citylist.name) {
                    popUpTo(navController.graph.startDestinationId) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )
        BottomNavigationItem(
            icon = { Icon(Icons.Filled.Search, contentDescription = null) },
            label = { Text(stringResource(R.string.search)) },
            selected = currentRoute == WanderScreen.Search.name,
            onClick = {
                navController.navigate(WanderScreen.Search.name) {
                    popUpTo(navController.graph.startDestinationId) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )
        BottomNavigationItem(
            icon = { Icon(Icons.Filled.Chat, contentDescription = null) },
            label = { Text(stringResource(R.string.Chat)) },
            selected = currentRoute == WanderScreen.Message.name,
            onClick = {
                navController.navigate(WanderScreen.Message.name) {
                    popUpTo(navController.graph.startDestinationId) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )

    }
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Image(
        modifier = modifier.size(200.dp),
        painter = painterResource(R.drawable.loading_img),
        contentDescription = stringResource(R.string.loading)
    )
}

@Composable
fun ErrorScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_connection_error), contentDescription = ""
        )
        Text(text = stringResource(R.string.loading_failed), modifier = Modifier.padding(16.dp))
    }
}