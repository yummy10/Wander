package com.example.wander.ui.screens

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
import com.example.wander.model.Comment
import com.example.wander.ui.WViewModel

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun AddComment(onBackPressed: () -> Unit,
               wViewModel: WViewModel,
               modifier: Modifier = Modifier
) {
    var placeName by remember { mutableStateOf("") }
    var text by remember { mutableStateOf("") }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.add_comment)) },
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
    ){ paddingValues ->
        Column(
            modifier = modifier
                .padding(paddingValues)
                .padding(dimensionResource(R.dimen.padding_medium)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.padding_medium))
        ) {
            OutlinedTextField(
                value = placeName,
                onValueChange = {placeName=it},
                modifier = Modifier.fillMaxWidth(),
                label = { Text(stringResource(R.string.place_name))}
            )
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
            Button(
                onClick = {
                    val newComment = Comment(
                        messageID= 0,
                        userName="",
                        placeName=placeName,
                        text=text,
                        mLike=0
                    )
                    wViewModel.addComment(newComment)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.add_comment))
            }
        }
    }
}