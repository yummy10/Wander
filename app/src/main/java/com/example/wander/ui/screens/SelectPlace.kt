package com.example.wander.ui.screens

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.wander.R
import com.example.wander.model.Place
import com.example.wander.network.BASE_URL
import com.example.wander.ui.WViewModel
import com.example.wander.ui.components.WanderTopAppBar

@Composable
fun PlaceApp(
    backButtonClicked: () -> Unit,
    navigateToAddPlaceScreen: () -> Unit,
    wViewModel: WViewModel
) {
    val uiState by wViewModel.uiState.collectAsState()

    if (uiState.isShowingPlaceList) {
        PlaceList(
            backButtonClicked = backButtonClicked,
            wViewModel = wViewModel,
            navigateToAddPlaceScreen = navigateToAddPlaceScreen
        )
    } else {
        PlaceDetail(wViewModel = wViewModel, uiState = uiState)
    }
}

@Composable
fun PlaceList(
    backButtonClicked: () -> Unit,
    wViewModel: WViewModel,
    navigateToAddPlaceScreen: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentPlaceList by wViewModel.places.collectAsState(initial = emptyList())

    Scaffold(
        topBar = { WanderTopAppBar(backButtonClicked = backButtonClicked) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = navigateToAddPlaceScreen,
                modifier = Modifier.padding(16.dp),
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_add),
                    contentDescription = stringResource(R.string.add_place),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(contentPadding = paddingValues, modifier = modifier) {
            items(currentPlaceList) { place ->
                PlaceListCard(
                    place = place,
                    wViewModel = wViewModel,
                    modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_small))
                )
            }
        }
    }
}

@Composable
fun PlaceListCard(wViewModel: WViewModel, place: Place, modifier: Modifier = Modifier) {
    var expanded by remember { mutableStateOf(false) }
    Card(modifier = modifier) {
        Column(
            modifier = Modifier.animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            )
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = "$BASE_URL${place.placeImagePath}"),
                contentDescription = place.placeImageName,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(194.dp)
                    .clickable { wViewModel.detailsScreenStates(place) },
                contentScale = ContentScale.Crop
            )
            Row {
                Text(
                    text = place.placeName,
                    modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_small)),
                    style = MaterialTheme.typography.displayMedium
                )
                Spacer(modifier = Modifier.weight(1f))
                ItemButton(expanded = expanded, onClick = { expanded = !expanded })
            }
            if (expanded) {
                PlaceIntro(
                    placeIntro = place.placeDescription,
                    modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium))
                )
            }
        }
    }
}

@Composable
private fun ItemButton(
    expanded: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick,
        modifier = modifier
    ) {
        Icon(
            imageVector = if (expanded) Icons.Filled.ExpandMore else Icons.Filled.ExpandLess,
            contentDescription = stringResource(R.string.expand_button_content_description),
            tint = MaterialTheme.colorScheme.secondary
        )
    }
}

@Composable
private fun PlaceIntro(
    placeIntro: String,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            text = stringResource(R.string.about),
            style = MaterialTheme.typography.labelSmall
        )
        Text(
            text = placeIntro,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

