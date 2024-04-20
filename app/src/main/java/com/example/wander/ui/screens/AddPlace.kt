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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.wander.R
import com.example.wander.model.Place
import com.example.wander.ui.WViewModel
import androidx.compose.ui.platform.LocalContext
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.TextButton
import androidx.compose.runtime.collectAsState
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import coil.compose.rememberAsyncImagePainter
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.produceState
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.compose.runtime.mutableDoubleStateOf
import kotlinx.coroutines.*

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun AddPlaceScreen(
    onBackPressed: () -> Unit,
    wViewModel: WViewModel,
    modifier: Modifier = Modifier,
    mainActivity: com.example.wander.MainActivity
) {
    val uiState by wViewModel.uiState.collectAsState()
    val currentCityName = wViewModel.uiState.value.currentPlace
    val currentCityId = wViewModel.uiState.value.currentId
    var placeName by remember { mutableStateOf("") }
    var placeDescription by remember { mutableStateOf("") }
    var placeBody by remember { mutableStateOf("") }
    val x = wViewModel.coordinates.value?.first ?:0.0
    val y = wViewModel.coordinates.value?.second ?:0.0
    val context = LocalContext.current
    val photoViewModel = mainActivity.photoViewModel
    val selectedImageUri by photoViewModel.selectedImageUri.observeAsState()
    // 获取当前的 Context
    if (uiState.isAddPlace) {
        AlertDialog(
            onDismissRequest = { wViewModel.dismissSuccessAddPlace() },
            title = { Text(stringResource(R.string.success_dialog_title)) },
            text = { Text(stringResource(R.string.success_add_place)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        wViewModel.dismissSuccessAddPlace()
                    }
                ) {
                    Text(stringResource(R.string.ok))
                }
            }
        )
    }
    if (uiState.isAddCoordinates) {
        AlertDialog(
            onDismissRequest = { wViewModel.noAddCoordinates() },
            title = { Text(stringResource(R.string.success_dialog_title)) },
            text = { Text(stringResource(R.string.success_upload_attraction_location)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        wViewModel.noAddCoordinates()
                    }
                ) {
                    Text(stringResource(R.string.ok))
                }
            }
        )
    }
    if (uiState.isAddEmptyPlace) {
        AlertDialog(
            onDismissRequest = { wViewModel.dismissAddEmptyComment() },
            title = { Text(stringResource(R.string.fail_empty_title)) },
            text = { Text(stringResource(R.string.fail_add_place)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        wViewModel.dismissAddEmptyComment()
                    }
                ) {
                    Text(stringResource(R.string.ok))
                }
            }
        )
    }

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
            TextField(
                value = currentCityName,
                onValueChange = {},
                enabled = false,
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(R.string.city_name)) })

            OutlinedTextField(
                value = placeName,
                onValueChange = { placeName = it },
                label = { Text(stringResource(R.string.place_name)) },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = placeDescription,
                onValueChange = { placeDescription = it },
                label = { Text(stringResource(R.string.place_description)) },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = placeBody,
                onValueChange = { placeBody = it },
                label = { Text(stringResource(R.string.place_body)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                maxLines = Int.MAX_VALUE,
                singleLine = false
            )
            selectedImageUri?.let { uri ->
                Image(
                    painter = rememberAsyncImagePainter(model = uri),
                    contentDescription = null,
                    modifier = Modifier
                        .size(120.dp),
                    contentScale = ContentScale.Crop
                )
            }
            Row{
                Button(
                    onClick = { mainActivity.openGallery() },
                    modifier = Modifier.weight(1f).fillMaxWidth()
                ) {
                    Text(stringResource(R.string.upload_picture))
                }
                Button(
                    onClick = {
                        mainActivity.initLocation()
                    },
                    modifier = Modifier.weight(1f).fillMaxWidth()
                ) {
                    Text(stringResource(R.string.upload_attraction_location))
                }
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
                        placeImagePath = "",
                        x = x,
                        y = y,
                    )
                    if(placeName!=""||placeDescription!=""||placeBody!="") {
                        val imageUri = mainActivity.photoViewModel.getSelectedImageUri()
                        wViewModel.addPlaceWithImage(context, newPlace, currentCityName, imageUri)
                        placeName = ""
                        placeDescription = ""
                        placeBody = ""
                    }else{
                        wViewModel.addEmptyPlace()
                    }

                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.add_place))
            }
        }
    }
}

@Composable
fun <T> LiveData<T>.observeAsState(): State<T?> {
    val initialValue: T? = value

    val state = produceState(initialValue = initialValue) {
        val observer = Observer<T> { value ->
            this@produceState.value = value
        }

        observeForever(observer)

        awaitDispose {
            removeObserver(observer)
        }
    }

    return state
}