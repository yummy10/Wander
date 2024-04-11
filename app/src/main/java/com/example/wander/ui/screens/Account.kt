package com.example.wander.ui.screens

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.wander.R
import com.example.wander.model.WanderScreen
import com.example.wander.network.BASE_URL
import com.example.wander.ui.LoginViewModel
import com.example.wander.ui.WViewModel
import com.example.wander.ui.components.WanderBottomNavigation

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(
    wViewModel: WViewModel,
    navController: NavHostController,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel,
    mainActivity: com.example.wander.MainActivity
) {
    wViewModel.initAccountScreen()
    val uiState by wViewModel.uiState.collectAsState()
    val loginState by viewModel.loginState
    val context = LocalContext.current
    if (loginState.isChange) {
        AlertDialog(onDismissRequest = { viewModel.dismissFailDialog() },
            title = { Text(stringResource(R.string.success_change_title)) },
            text = { Text(stringResource(R.string.success_change_title)) },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.dismissFailDialog()
                }) {
                    Text(stringResource(R.string.ok))
                }
            })
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(R.string.Account)) }, navigationIcon = {
                if (uiState.isShowingUserComments||uiState.isChangingPassword||uiState.isUploadingIcon) {
                    IconButton(onClick = {
                        wViewModel.onBackPressed()
                    }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                }
            })
        },
        bottomBar = {
            WanderBottomNavigation(navController)
        },

        ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(dimensionResource(id = R.dimen.padding_medium))
        ) {
            // 显示账户名称
            Row {
                val imagePainter = rememberAsyncImagePainter(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data("$BASE_URL${uiState.user?.icon}")
                        .error(R.drawable.default_avatar) // Use error for server-side errors
                        .build(),
                    placeholder = painterResource(R.drawable.loading_img),
                    error = painterResource(R.drawable.default_avatar) // Fallback for other errors
                )
                Text(
                    stringResource(R.string.account_name) + (uiState.user?.userName ?: stringResource(R.string.unlogged)),
                    style = MaterialTheme.typography.displayLarge
                )
                Spacer(modifier = Modifier.weight(1f))
                Image(
                    painter = imagePainter,
                    contentDescription = null,
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            }
            HorizontalDivider(
                modifier = Modifier.padding(vertical = 8.dp),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
            )
            // 显示个人评论列表按钮
            if (!uiState.isShowingUserComments && !uiState.isChangingPassword && !uiState.isUploadingIcon) {
                TextButton(onClick = { wViewModel.getUserComments() }) {
                    Text(stringResource(R.string.get_user_comments))
                }
                TextButton(onClick = {
                    viewModel.logout()
                    navController.navigate(WanderScreen.Greeting.name)
                }) {
                    Text(stringResource(R.string.logout_title))
                }
                TextButton(onClick = {
                    wViewModel.changePassword()
                }) {
                    Text(stringResource(R.string.change_password))
                }
                TextButton(onClick = {
                    wViewModel.uploadIcon()
                }) {
                    Text(stringResource(R.string.upload_icon))
                }
            }
            // 显示个人评论列表
            AnimatedVisibility(
                visible = uiState.isShowingUserComments, enter = slideInHorizontally(
                    initialOffsetX = { fullWidth -> fullWidth },
                    animationSpec = tween(durationMillis = 300)
                ) + EnterTransition.None, exit = slideOutHorizontally(
                    targetOffsetX = { fullWidth -> fullWidth },
                    animationSpec = tween(durationMillis = 300)
                )
            ) {
                MessageBoardContent(
                    comments = wViewModel.comment.value,
                    onLikeClicked = wViewModel::onLikeClicked,
                    modifier = Modifier.fillMaxSize()
                )
            }
            AnimatedVisibility(
                visible = uiState.isChangingPassword, enter = slideInHorizontally(
                    initialOffsetX = { fullWidth -> fullWidth },
                    animationSpec = tween(durationMillis = 300)
                ) + EnterTransition.None, exit = slideOutHorizontally(
                    targetOffsetX = { fullWidth -> fullWidth },
                    animationSpec = tween(durationMillis = 300)
                )
            ) {
                EditPassword(viewModel,wViewModel)
            }
            AnimatedVisibility(
                visible = uiState.isUploadingIcon, enter = slideInHorizontally(
                    initialOffsetX = { fullWidth -> fullWidth },
                    animationSpec = tween(durationMillis = 300)
                ) + EnterTransition.None, exit = slideOutHorizontally(
                    targetOffsetX = { fullWidth -> fullWidth },
                    animationSpec = tween(durationMillis = 300)
                )
            ) {
                if (uiState.isUploadingIconSuccess) {
                    AlertDialog(onDismissRequest = { wViewModel.dismissAddIcon() },
                        title = { Text(stringResource(R.string.success_change_title)) },
                        text = { Text(stringResource(R.string.success_change_title)) },
                        confirmButton = {
                            TextButton(onClick = {
                                wViewModel.dismissAddIcon()
                            }) {
                                Text(stringResource(R.string.ok))
                            }
                        })
                }
                if (uiState.isUploadingIconFail) {
                    AlertDialog(onDismissRequest = { wViewModel.dismissAddIcon() },
                        title = { Text(stringResource(R.string.fail_create_title)) },
                        text = { Text(stringResource(R.string.fail_create_title)) },
                        confirmButton = {
                            TextButton(onClick = {
                                wViewModel.dismissAddIcon()
                            }) {
                                Text(stringResource(R.string.ok))
                            }
                        })
                }
                Column {
                    Button(
                        onClick = { mainActivity.openGallery() },
                    ) {
                        Text(stringResource(R.string.select_picture))
                    }
                    Button(
                        onClick = { uiState.user?.userName?.let { wViewModel.addIcon(context, it,mainActivity.photoViewModel.getSelectedImageUri())} },
                    ) {
                        Text(stringResource(R.string.upload_icon))
                    }
                }

            }
        }
    }
}

@Composable
fun EditPassword(
    viewModel:LoginViewModel,
    wViewModel: WViewModel,
) {
    var password by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    val loginState by viewModel.loginState
    val uiState by wViewModel.uiState.collectAsState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(dimensionResource(R.dimen.padding_medium)),
    ) {
        TextField(
            label = { Text(stringResource(R.string.user_old_password)) },
            value = password,
            onValueChange = { password = it },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            supportingText = { // 显示支持文本
                if (loginState.changeFail) {
                    Text(stringResource(R.string.wrong_password))
                }
                if(loginState.emptyFail){
                    Text(stringResource(R.string.empty_password))
                }
            }
        )
        TextField(
            label = { Text(stringResource(R.string.user_new_password)) },
            value = newPassword,
            onValueChange = { newPassword = it },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            supportingText = { // 显示支持文本
                if(loginState.newEmptyFail){
                    Text(stringResource(R.string.empty_password))
                }
            }
        )
        Button(
            onClick = {
                viewModel.changePassword(uiState.user?.userName ?: "", password,newPassword)
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(R.string.change_password))
        }
    }
}