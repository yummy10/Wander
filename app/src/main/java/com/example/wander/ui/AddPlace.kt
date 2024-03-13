package com.example.wander.ui

import android.annotation.SuppressLint
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
import com.example.wander.model.PlaceList


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun AddPlaceScreen(
   // onPlaceAdded: (PlaceList) -> Unit,
    onBackPressed: () -> Unit,
    wViewModel: WViewModel,
    modifier: Modifier = Modifier
) {
    val city_name = wViewModel.uiState.value.currentPlace
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var body by remember { mutableStateOf("") }
   // var imageUri by remember { mutableStateOf<Uri?>(null) }
    val city by remember { mutableStateOf(city_name) }
  //  val launcher = rememberLauncherForActivityResult(
 //       contract = ActivityResultContracts.GetContent(),
   //     onResult = { uri -> imageUri = uri }
   // )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.add_place)) },
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back_button)
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .padding(paddingValues)
                .padding(dimensionResource(R.dimen.padding_medium)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium))
        ) {
            TextField(
                value = stringResource(city.stringResourceId),
                onValueChange = { },
                enabled = false, // 禁用编辑
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(R.string.place_city)) }
            )

            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text(stringResource(R.string.place_name)) },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text(stringResource(R.string.place_description)) },
                modifier = Modifier.fillMaxWidth()
            )

            OutlinedTextField(
                value = body,
                onValueChange = { body = it },
                label = { Text(stringResource(R.string.place_body)) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp), // 设置文本框的高度为250dp
                maxLines = Int.MAX_VALUE, // 允许文本框显示多行文本
                singleLine = false, // 禁用单行模式

            )

//            IconButton(
//                onClick = { launcher.launch("image/*") },
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                Icon(
//                    painter = painterResource(R.drawable.ic_image),
//                    contentDescription = stringResource(R.string.select_image)
//                )
//            }

            Button(
                onClick = {
                    val newPlace = PlaceList(
                        id = System.currentTimeMillis(),
                        stringResourceId = R.string.app_name, // Replace with a proper string resource
                        description = R.string.app_name, // Replace with a proper string resource
                        body = R.string.app_name, // Replace with a proper string resource
                        imageResourceId = R.drawable.ic_launcher_background, // Replace with a proper image resource
                        city = city
                    )
    //                onPlaceAdded(newPlace)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.add_place))
            }
        }
    }
}