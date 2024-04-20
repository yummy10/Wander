package com.example.wander.ui.screens

import android.annotation.SuppressLint
import android.content.Intent
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.amap.api.maps.model.LatLng
import com.example.wander.MapActivity
import com.example.wander.R
import com.example.wander.model.Place
import com.example.wander.model.UiState
import com.example.wander.model.WanderScreen
import com.example.wander.ui.NetsUiState
import com.example.wander.ui.WViewModel
import com.example.wander.ui.components.ErrorScreen
import com.example.wander.ui.components.LoadingScreen

@SuppressLint("UnrememberedMutableState")
@Composable
fun PlaceDetail(wViewModel: WViewModel, uiState: UiState, modifier: Modifier = Modifier,navController: NavHostController
) {
    val placeName = derivedStateOf { uiState.currentSelectedPlace.placeName }


    LaunchedEffect(placeName.value) {
        wViewModel.getPlaceComments(placeName.value)
    }
    when (wViewModel.placeDetailUiState) {
        is NetsUiState.Loading -> LoadingScreen()
        is NetsUiState.Success -> PlaceDetail1(
            wViewModel,
            uiState,
            modifier,
            navController,
        )
        is NetsUiState.Error -> ErrorScreen()
    }

}

@SuppressLint("StateFlowValueCalledInComposition", "UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PlaceDetail1(
    wViewModel: WViewModel,
    uiState: UiState,
    modifier: Modifier = Modifier,
    navController: NavHostController,
) {
    val onBackPressed = { wViewModel.resetHomeScreenStates() }
    val context = LocalContext.current
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    wViewModel.setCommentPlace(uiState.currentSelectedPlace.placeName)
                    navController.navigate(WanderScreen.Addmessage.name)
                },
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium)),
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Text(text = stringResource(R.string.add_comment))
            }
        }
    ) {
        BackHandler {
            onBackPressed()
        }
        Box(modifier = modifier) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.inverseOnSurface)
                    .padding(top = dimensionResource(R.dimen.padding_medium))
            ) {
                item {
                    DetailsScreenTopBar(
                        onBackPressed,
                        uiState,
                        Modifier
                            .fillMaxWidth()
                            .padding(bottom = dimensionResource(R.dimen.padding_medium))
                    )
                }

                item {
                    DetailsCard(
                        wViewModel,
                        place = uiState.currentSelectedPlace,
                        modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.padding_medium))
                    )
                }
                item {
                    Button(
                        onClick = {
                            val intent = Intent(context, MapActivity::class.java)
                            intent.putExtra("LOCATION", LatLng(uiState.currentSelectedPlace.x,uiState.currentSelectedPlace.y))
                            intent.putExtra("NAME", uiState.currentSelectedPlace.placeName)
                            context.startActivity(intent)

                        },
                        modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.padding_medium))
                    ) {
                        Text(stringResource(R.string.open_map))
                    }
                }
                item {
                    Text(
                        text = stringResource(R.string.text),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.padding_medium))
                    )
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
                    )
                }

                items(wViewModel.comment.value) { message ->
                    MessageItem(
                        comment = message,
                        onLikeClicked = wViewModel::onLikeClicked,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                PaddingValues(
                                    dimensionResource(id = R.dimen.padding_medium)
                                )
                            )
                    )
                    Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_medium)))

                }
            }
        }
    }
}

@Composable
private fun DetailsScreenTopBar(
    onBackButtonClicked: () -> Unit, uiState: UiState, modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(
            onClick = onBackButtonClicked,
            modifier = Modifier
                .padding(horizontal = dimensionResource(R.dimen.padding_medium))
                .background(MaterialTheme.colorScheme.surface, shape = CircleShape),
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = stringResource(id = R.string.navigation_back)
            )
        }
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(end = dimensionResource(R.dimen.padding_medium))
        ) {
            Text(
                text = uiState.currentSelectedPlace.placeName,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun DetailsCard(
    wViewModel: WViewModel,
    place: Place,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.padding_small))
        ) {
            Text(
                text = place.placeIntroduction,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

        }
    }
}

