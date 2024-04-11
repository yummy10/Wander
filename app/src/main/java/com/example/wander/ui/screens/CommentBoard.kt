package com.example.wander.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withAnnotation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.wander.R
import com.example.wander.model.Comment
import com.example.wander.model.WanderScreen
import com.example.wander.network.BASE_URL
import com.example.wander.ui.NetsUiState
import com.example.wander.ui.WViewModel
import com.example.wander.ui.components.ErrorScreen
import com.example.wander.ui.components.LoadingScreen
import com.example.wander.ui.components.WanderBottomNavigation

@SuppressLint("StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageBoardScreen(
    viewModel: WViewModel, navController: NavHostController
) {
    viewModel.getMessages()
    Scaffold(topBar = {
        TopAppBar(
            title = { Text(stringResource(R.string.message_board)) },
        )

    }, bottomBar = {
        WanderBottomNavigation(navController)
    }, floatingActionButton = {
        FloatingActionButton(
            onClick = { viewModel.setCommentPlace()
                navController.navigate(WanderScreen.Addmessage.name)},
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium)),
            containerColor = MaterialTheme.colorScheme.primary
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_add),
                contentDescription = stringResource(R.string.add_place),
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    }) { paddingValues ->
        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            color = MaterialTheme.colorScheme.background
        ) {
            when (viewModel.messageBoardUiState) {
                is NetsUiState.Loading -> LoadingScreen()
                is NetsUiState.Success -> MessageBoardContent(
                    comments = viewModel.comment.value, onLikeClicked = viewModel::onLikeClicked
                )

                is NetsUiState.Error -> ErrorScreen(
                )
            }
        }
    }
}

@Composable
fun MessageBoardContent(
    comments: List<Comment>, onLikeClicked: (Int, Boolean) -> Unit, modifier: Modifier = Modifier
) {
    LazyColumn(
        contentPadding = PaddingValues(dimensionResource(id = R.dimen.padding_medium)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_medium)),
        modifier = modifier
    ) {
        items(comments) { message ->
            MessageItem(
                comment = message, onLikeClicked = onLikeClicked
            )
        }
    }
}

@OptIn(ExperimentalTextApi::class)
@Composable
fun MessageItem(
    comment: Comment, onLikeClicked: (Int, Boolean) -> Unit, modifier: Modifier = Modifier
) {
    val isLiked = remember { mutableStateOf(false) }
    val showMoreState = remember { mutableStateOf(false) }
    val annotatedString = buildAnnotatedString {
        withAnnotation(
            tag = "SHOW_MORE",
            annotation = "SHOW_MORE"
        ) {
            append(comment.text)
        }
    }
    Card(
        modifier = modifier
    ) {

        Column(
            modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_medium))
        ){
            Row(modifier = modifier){
                val imagePainter = rememberAsyncImagePainter(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data("$BASE_URL/users/images/${comment.userName}.jpg")
                        .error(R.drawable.default_avatar) // Use error for server-side errors
                        .build(),
                    placeholder = painterResource(R.drawable.loading_img),
                    error = painterResource(R.drawable.default_avatar) // Fallback for other errors
                )
                Image(
                    painter = imagePainter,
                    contentDescription = null,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(CircleShape)
                        .border(2.dp, Color.Gray, CircleShape),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = comment.userName, style = MaterialTheme.typography.titleLarge
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = comment.placeName, style = MaterialTheme.typography.titleLarge
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
            ) {
                Text(
                    stringResource(R.string.star), style = MaterialTheme.typography.titleMedium
                )
                repeat(5) { index ->
                        Icon(
                            painter = painterResource(
                                id = if (index < comment.star) R.drawable.ic_star_filled else R.drawable.ic_star_outline
                            ),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = annotatedString,
                style = MaterialTheme.typography.bodyMedium,
                maxLines = if (showMoreState.value) Int.MAX_VALUE else 3,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(8.dp))
            if(!showMoreState.value) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.weight(1f))
                    TextButton(onClick = { showMoreState.value = true }) {
                        Text(stringResource(R.string.get_more))
                    }
                }
            }
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
                    text = comment.mLike.toString(), style = MaterialTheme.typography.bodySmall
                )
                Spacer(modifier = Modifier.weight(1f))
                IconButton(onClick = {
                    onLikeClicked(comment.messageID, isLiked.value)
                    isLiked.value = !isLiked.value
                }) {
                    Icon(
                        imageVector = Icons.Filled.ThumbUp,
                        contentDescription = stringResource(R.string.like),
                        tint = if (isLiked.value) Color.Blue else Color.Gray
                    )
                }
            }
        }
    }
}