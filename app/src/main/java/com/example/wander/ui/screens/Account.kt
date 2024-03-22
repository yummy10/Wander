package com.example.wander.ui.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.wander.R
import com.example.wander.ui.components.WanderBottomNavigation
import com.example.wander.ui.WViewModel as WViewModel1

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(
    wViewModel: WViewModel1,
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    wViewModel.clearComments()
    val uiState by wViewModel.uiState.collectAsState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.Account)) },
            )
        },
        bottomBar = {
            WanderBottomNavigation(navController)
        },

    ){
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // 显示账户名称
            Text(
                text = "账户: ${uiState.user?.userName ?: "未登录"}",
                style = MaterialTheme.typography.displayLarge
            )
            Divider(
                modifier = Modifier.padding(vertical = 8.dp),
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
            )
            // 显示个人评论列表按钮
            TextButton(
                onClick = { wViewModel.getUserComments() }
            ) {
                Text("查看我的评论")
            }

            // 显示个人评论列表
            if (uiState.isShowingUserComments) {
                MessageBoardContent(comments = wViewModel.comment.value,
                    onLikeClicked = wViewModel::onLikeClicked)
            }
        }
    }

}