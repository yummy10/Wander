package com.example.wander.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.wander.R
import com.example.wander.ui.LoginViewModel
import com.example.wander.ui.WViewModel

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = viewModel(),
    wViewModel: WViewModel,
    onLoginSuccess: () -> Unit
) {
    val loginState by viewModel.loginState
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    if (loginState.loggedInFail) {
        AlertDialog(
            onDismissRequest = { viewModel.dismissFailDialog() },
            title = { Text(stringResource(R.string.fail_title)) },
            text = { Text(stringResource(R.string.fail_message)) },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.dismissFailDialog()
                    }
                ) {
                    Text(stringResource(R.string.ok))
                }
            }
        )
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Username") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { viewModel.login(username, password) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Login")
        }
        if (loginState.error != null) {
            Text(
                text = loginState.error!!,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
        if (loginState.isLoggedIn) {
            wViewModel.updateUsername(loginState.user)
            onLoginSuccess()
        }
    }
}