package com.example.wander.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.wander.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WanderTopAppBar(modifier: Modifier = Modifier,backButtonClicked:() -> Unit) {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.tertiaryContainer
        ),
        title = {
            Text(
                text = stringResource(R.string.app_name),
                style = MaterialTheme.typography.displayLarge

            )
        },
        navigationIcon = {
            IconButton(onClick = backButtonClicked) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back_button)
                )
            }
        },
        modifier = modifier
    )
}