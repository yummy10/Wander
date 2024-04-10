package com.example.wander.ui.screens

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.wander.R
import com.example.wander.model.Place
import com.example.wander.model.UiState
import com.example.wander.ui.WViewModel
import com.example.wander.ui.theme.WanderTheme

@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun PlaceDetail(wViewModel: WViewModel, uiState: UiState, modifier: Modifier = Modifier) {
    val onBackPressed = { wViewModel.resetHomeScreenStates() }
    wViewModel.commentNotOK()
    BackHandler {
        onBackPressed()
    }
    Box(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.inverseOnSurface)
                .padding(top = dimensionResource(R.dimen.padding_medium))
        ) {
            DetailsScreenTopBar(
                onBackPressed,
                uiState,
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = dimensionResource(R.dimen.padding_medium))
            )
            DetailsCard(
                wViewModel,
                place = uiState.currentSelectedPlace,
                modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.padding_medium))
            )
            if(uiState.isCommentOK) {
                MessageBoardContent(
                    comments = wViewModel.comment.value,
                    onLikeClicked = wViewModel::onLikeClicked,
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

    }
}

@Composable
private fun DetailsScreenTopBar(
    onBackButtonClicked: () -> Unit, UiState: UiState, modifier: Modifier = Modifier
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
                text = UiState.currentSelectedPlace.placeDescription,
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
        wViewModel.getPlaceComments(place.placeName)
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

@Preview
@Composable
private fun CardPreview() {
    WanderTheme {
        val wViewModel: WViewModel = viewModel()
        val uiState by wViewModel.uiState.collectAsState()
        PlaceDetail(wViewModel, uiState)
    }
}