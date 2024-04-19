package com.example.wander.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.example.wander.R
import com.example.wander.ui.LoginViewModel
import com.example.wander.ui.WViewModel

@Composable
fun LoginScreen(
    viewModel: LoginViewModel, wViewModel: WViewModel, onLoginSuccess: () -> Unit
) {

    val loginState by viewModel.loginState
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isLogin by remember { mutableStateOf(true) }
    if (loginState.loggedInFail) {
        AlertDialog(onDismissRequest = { viewModel.dismissFailDialog() },
            title = { Text(stringResource(R.string.fail_login_title)) },
            text = { Text(stringResource(R.string.fail_login_message)) },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.dismissFailDialog()
                }) {
                    Text(stringResource(R.string.ok))
                }
            })
    }
    if (loginState.createFail) {
        AlertDialog(onDismissRequest = { viewModel.dismissFailDialog() },
            title = { Text(stringResource(R.string.fail_create_title)) },
            text = { Text(stringResource(R.string.fail_create_message)) },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.dismissFailDialog()
                }) {
                    Text(stringResource(R.string.ok))
                }
            })
    }
    if (loginState.emptyFail) {
        AlertDialog(onDismissRequest = { viewModel.dismissFailDialog() },
            title = { Text(stringResource(R.string.fail_empty_title)) },
            text = { Text(stringResource(R.string.fail_empty_message)) },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.dismissFailDialog()
                }) {
                    Text(stringResource(R.string.ok))
                }
            })
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(dimensionResource(id = R.dimen.padding_medium)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Card(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly, // 平均分配空间
                verticalAlignment = Alignment.CenterVertically // 垂直居中对齐
            ) {
                Button(
                    onClick = { isLogin = true }, colors = ButtonDefaults.buttonColors(
                        containerColor = if (isLogin) MaterialTheme.colorScheme.onPrimaryContainer else Color.Gray
                    ), modifier = Modifier.weight(1f) // 设置权重为 1
                ) {
                    Text(stringResource(R.string.login))
                }
                Spacer(modifier = Modifier.width(dimensionResource(id = R.dimen.padding_medium))) // 添加一些间距

                Button(
                    onClick = { isLogin = false }, colors = ButtonDefaults.buttonColors(
                        containerColor = if (!isLogin) MaterialTheme.colorScheme.onPrimaryContainer else Color.Gray
                    ), modifier = Modifier.weight(1f) // 设置权重为 1
                ) {
                    Text(stringResource(R.string.create))
                }

            }
        }
        OutlinedTextField(value = username,
            onValueChange = { username = it },
            label = { Text(stringResource(R.string.username)) },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_medium)))
        OutlinedTextField(value = password,
            onValueChange = { password = it },
            label = { Text(stringResource(R.string.password)) },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(dimensionResource(id = R.dimen.padding_medium)))
        if (isLogin) {
            Button(
                onClick = { viewModel.login(username, password) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.login))
            }
        } else {
            Button(
                onClick = { viewModel.create(username, password) },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.create))
            }
        }
        if (loginState.error != null) {
            Text(
                text = loginState.error!!,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = dimensionResource(id = R.dimen.padding_medium))
            )
        }
        if (loginState.isLoggedIn) {
            wViewModel.updateUsername(loginState.user)
            onLoginSuccess()
        }
    }
}