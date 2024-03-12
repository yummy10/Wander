package com.example.wander.ui

import androidx.annotation.StringRes
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.example.wander.R
import com.example.wander.model.PlaceList
import com.example.wander.model.UiState
import com.example.wander.ui.components.WanderTopAppBar

@Composable
fun PalceApp(backButtonClicked:() -> Unit,wViewModel: WViewModel){
    val uiState by wViewModel.uiState.collectAsState()

    if (uiState.isShowingPlaceList) {
        PalceList(backButtonClicked,wViewModel,uiState)
    }else
    {
        PlaceDetail(wViewModel,uiState)
    }

}



//@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PalceList(backButtonClicked:() ->  Unit,wViewModel: WViewModel,uiState: UiState, modifier: Modifier = Modifier){

    val currentplacelist = uiState.currentPlaceList
    Scaffold(
        topBar = {
            WanderTopAppBar(backButtonClicked=backButtonClicked)
        }
    ){
        LazyColumn(contentPadding = it,modifier = modifier) {
            items(currentplacelist) {place->
                PalceListCard(
                    place=place,
                    wViewModel=wViewModel,
                    modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_small))
                )

            }
        }
    }

}

@Composable
fun PalceListCard(wViewModel: WViewModel,place:PlaceList, modifier: Modifier = Modifier) {
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
                    .clickable {wViewModel.detailsScreenStates(place)} ,
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
            imageVector = if(expanded)Icons.Filled.ExpandMore else Icons.Filled.ExpandLess,
            contentDescription = stringResource(R.string.expand_button_content_description),
            tint = MaterialTheme.colorScheme.secondary
        )
    }
}

@Composable
fun PlaceIntro(
    @StringRes placeIntro: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = stringResource(R.string.about),
            style = MaterialTheme.typography.labelSmall
        )
        Text(
            text = stringResource(placeIntro),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}

