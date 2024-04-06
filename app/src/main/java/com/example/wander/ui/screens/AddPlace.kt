package com.example.wander.ui.screens

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.wander.R
import com.example.wander.model.Place
import com.example.wander.ui.WViewModel
import androidx.compose.ui.platform.LocalContext

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun AddPlaceScreen(
    onBackPressed: () -> Unit, wViewModel: WViewModel, modifier: Modifier = Modifier,
    mainActivity: com.example.wander.MainActivity
) {
    val currentCityName = wViewModel.uiState.value.currentPlace
    val currentCityId = wViewModel.uiState.value.currentId
    var placeName by remember { mutableStateOf("") }
    var placeDescription by remember { mutableStateOf("") }
    var placeBody by remember { mutableStateOf("") }
    val context = LocalContext.current
    // 获取当前的 Context

    Scaffold(topBar = {
        TopAppBar(title = { Text(stringResource(R.string.add_place)) }, navigationIcon = {
            IconButton(onClick = onBackPressed) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back_button)
                )
            }
        })
    }) { paddingValues ->
        Column(
            modifier = modifier
                .padding(paddingValues)
                .padding(dimensionResource(R.dimen.padding_medium)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium))
        ) {
            TextField(value = currentCityName,
                onValueChange = {},
                enabled = false,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(currentCityName) })

            OutlinedTextField(value = placeName,
                onValueChange = { placeName = it },
                label = { Text(stringResource(R.string.place_name)) },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(value = placeDescription,
                onValueChange = { placeDescription = it },
                label = { Text(stringResource(R.string.place_description)) },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(value = placeBody,
                onValueChange = { placeBody = it },
                label = { Text(stringResource(R.string.place_body)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                maxLines = Int.MAX_VALUE,
                singleLine = false
            )
            Button(onClick = {
                mainActivity.openGallery()
            }) {
                Text(stringResource(R.string.upload_picture))
            }

            Button(
                onClick = {
                    val newPlace = Place(
                        placeId = 0,
                        placeName = placeName,
                        placeDescription = placeDescription,
                        placeIntroduction = placeBody,
                        cityId = currentCityId,
                        placeImageName = "",
                        placeImagePath = ""
                    )
                        val imageUri = mainActivity.photoViewModel.getSelectedImageUri()
                        wViewModel.addPlaceWithImage(context,newPlace, currentCityName,imageUri)
                    placeName = ""
                    placeDescription = ""
                    placeBody = ""
                }, modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.add_place))
            }
        }
    }
}
