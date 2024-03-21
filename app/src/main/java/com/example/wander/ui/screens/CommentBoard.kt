package com.example.wander.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.wander.R
import com.example.wander.WanderScreen
import com.example.wander.model.Comment
import com.example.wander.ui.NetsUiState
import com.example.wander.ui.WViewModel
import com.example.wander.ui.components.ErrorScreen
import com.example.wander.ui.components.LoadingScreen
import com.example.wander.ui.components.WanderBottomNavigation

@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageBoardScreen(
    viewModel: WViewModel,
    navController: NavHostController
) {
    val uiState by viewModel.uiState.collectAsState()
    viewModel.getMessages()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.message_board)) },
            )

        },
        bottomBar = {
            WanderBottomNavigation(navController,)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {navController.navigate(WanderScreen.Addmessage.name)},
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
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
            when (viewModel.messageBoardUiState) {
                is NetsUiState.Loading -> LoadingScreen()
                is NetsUiState.Success -> MessageBoardContent(
                    comments = viewModel.comment.value,
                    onLikeClicked = viewModel::onLikeClicked
                )
                is NetsUiState.Error -> ErrorScreen(
                )
            }
        }
    }
}

@Composable
fun MessageBoardContent(
    comments: List<Comment>,
    onLikeClicked: (Int,Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
    ) {
        items(comments) { message ->
            MessageItem(
                comment = message,
                onLikeClicked = onLikeClicked
            )
        }
    }
}

@Composable
fun MessageItem(
    comment: Comment,
    onLikeClicked: (Int,Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val isLiked = remember { mutableStateOf(false) }
    Card(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(modifier = modifier) {
            Text(
                text = comment.userName,
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.width(32.dp))
            Text(
                text = comment.placeName,
                style = MaterialTheme.typography.titleMedium
            )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = comment.text,
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = stringResource(R.string.likes),
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text =comment.mLike.toString(),
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(modifier = Modifier.width(32.dp))
                IconButton(
                    onClick = { onLikeClicked(comment.messageID,isLiked.value)
                        isLiked.value = !isLiked.value}
                ) {
                    Icon(
                        imageVector = Icons.Filled.ThumbUp,
                        contentDescription = stringResource(R.string.like) ,
                        tint = if (isLiked.value) Color.Blue else Color.Gray
                    )
                }
            }
        }
    }
}