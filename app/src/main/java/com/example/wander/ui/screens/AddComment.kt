package com.example.wander.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.wander.R
import com.example.wander.model.Comment
import com.example.wander.ui.WViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("StateFlowValueCalledInComposition", "ProduceStateDoesNotAssignValue")
@Composable
fun AddComment(onBackPressed: () -> Unit,
               wViewModel: WViewModel,
               modifier: Modifier = Modifier
) {
    val uiState by wViewModel.uiState.collectAsState()
    var placeName by remember { mutableStateOf(uiState.commentPlace) }
    var text by remember { mutableStateOf("") }
    var rating by remember { mutableIntStateOf(0) }
    var isDropdownExpanded by remember { mutableStateOf(false) }
    val places by wViewModel.places.collectAsState(initial = emptyList())
    if (uiState.showSuccessDialog) {
        AlertDialog(
            onDismissRequest = { wViewModel.dismissSuccessDialog() },
            title = { Text(stringResource(R.string.success_dialog_title)) },
            text = { Text(stringResource(R.string.success_dialog_message)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        wViewModel.dismissSuccessDialog()
                        placeName=""
                        text=""
                    }
                ) {
                    Text(stringResource(R.string.ok))
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.add_comment)) },
                navigationIcon = {
                    IconButton(onClick = onBackPressed) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back_button)
                        )
                    }
                }
            )
        }
    ){ paddingValues ->
        Column(
            modifier = modifier
                .padding(paddingValues)
                .padding(dimensionResource(R.dimen.padding_medium)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium))
        ) {
            OutlinedTextField(
                value = placeName,
                onValueChange = {
                    placeName = it
                    wViewModel.getSearchPlaces(name = it, city = null)
                },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(R.string.place_name)) },
                supportingText = {
                    if (!uiState.isPlaceNameValid) {
                        Text(stringResource(R.string.place_name_not_found))
                    }
                },
                trailingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.ic_dropdown),
                        contentDescription = null,
                        modifier = Modifier.clickable {
                            isDropdownExpanded = !isDropdownExpanded
                        }
                    )
                }
            )
            DropdownMenu(
                expanded = isDropdownExpanded,
                onDismissRequest = { isDropdownExpanded = false },
                modifier = Modifier.fillMaxWidth()
            ) {
                places.forEach { place ->
                    DropdownMenuItem(
                        onClick = {
                            placeName = place.placeName
                            isDropdownExpanded = false
                        },
                        text = { Text(place.placeName) }
                    )
                }
            }
            OutlinedTextField(
                value = text,
                onValueChange = {text=it},
                label = { Text(stringResource(R.string.text))},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp),
                maxLines = Int.MAX_VALUE,
                singleLine = false
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(5) { index ->
                    IconButton(
                        onClick = { rating = index + 1 }, // 点击设置评分
                        modifier = Modifier.size(48.dp)
                    ) {
                        Icon(
                            painter = painterResource(
                                id = if (index < rating) R.drawable.ic_star_filled else R.drawable.ic_star_outline
                            ),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }
            Button(
                onClick = {
                    val newComment = Comment(
                        messageID = 0,
                        userName = "",
                        placeName = placeName,
                        text = text,
                        mLike = 0,
                        star = rating
                    )
                    if(placeName!="") {
                        wViewModel.addComment(newComment)
                    }else{
                        wViewModel.addEmptyComment()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.add_comment))
            }
        }
    }
}