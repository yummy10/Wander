package com.example.wander.ui

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.example.wander.R
import com.example.wander.model.PlaceList
import com.example.wander.ui.components.WanderTopAppBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchPlaceScreen(
    backButtonClicked: () -> Unit,
    viewModel: WViewModel,
//    onPlaceSelected: (PlaceList) -> Unit,
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()
    val search by viewModel.search.collectAsState()
    Scaffold(
        topBar = {
            WanderTopAppBar(backButtonClicked = backButtonClicked)
            TopAppBar(
                title = { Text(stringResource(R.string.search_places)) },
            )
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
                value = search.searchQuery,
                onValueChange = { viewModel.updateSearchQuery(it) },
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

            if (search.searchResults.isNotEmpty()) {


                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(vertical = 8.dp)
                    ) {
                        items(search.searchResults) { place ->
                            PalceListCard1(
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

@Composable
fun PalceListCard1(wViewModel: WViewModel,place:PlaceList, modifier: Modifier = Modifier) {
    var expanded by remember { mutableStateOf(false) }
    Card(modifier = modifier,) {
        Column(
            modifier=Modifier.animateContentSize(//已展開和未展開狀態之間的轉場效果
                animationSpec = spring<IntSize>(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            )
        ) {
            Image(

                painter = painterResource(place.imageResourceId),
                contentDescription =stringResource(place.stringResourceId) ,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(194.dp)
                    .clickable { wViewModel.onPlaceSelected(place)
                        wViewModel.detailsScreenStates(place)} ,
                contentScale = ContentScale.Crop
            )
            Row{
                Text(
                    text = LocalContext.current.getString(place.stringResourceId),
                    modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_small)),
                    style = MaterialTheme.typography.displayMedium
                )
                Spacer(modifier = Modifier.weight(1f))
                ItemButton(
                    expanded = expanded,
                    onClick = { expanded = !expanded}
                )
            }
            if(expanded) {
                PlaceIntro(
                    placeIntro = place.description,
                    modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium))
                )
            }
        }
    }
}

