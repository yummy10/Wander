package com.example.wander.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.wander.R
import com.example.wander.ui.WViewModel
import com.example.wander.ui.components.WanderBottomNavigation
import com.example.wander.ui.components.WanderTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchPlaceScreen(
    backButtonClicked: () -> Unit,
    navController: NavHostController,
    viewModel: WViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val places by viewModel.places.collectAsState()
    viewModel.clearPlaces()
    Scaffold(
        topBar = {
            WanderTopAppBar(backButtonClicked = backButtonClicked)
            TopAppBar(
                title = { Text(stringResource(R.string.search_places)) },
            )
        },
        bottomBar = {
            WanderBottomNavigation(navController,)
        }
    ) {
        paddingValues ->
        if (!uiState.isShowingPlaceList) {
            PlaceDetail(viewModel,uiState)
        }else
        {
        Column(
            modifier = modifier
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = uiState.search,
                onValueChange = { viewModel.getSearchPlaces(city = null, name = viewModel.updateSearch(it)) },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text(stringResource(R.string.search_places_hint)) },
                singleLine = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null
                    )
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (places.isNotEmpty()) {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        items(places) { place ->
                            PlaceListCard(
                                place = place,
                                wViewModel = viewModel,
                                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_small))
                            )
                        }
                    }


            } else {
                Text(
                    text = stringResource(R.string.no_results_found),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.fillMaxSize(),
                    textAlign = TextAlign.Center
                )
            }
        }
        }
    }

}