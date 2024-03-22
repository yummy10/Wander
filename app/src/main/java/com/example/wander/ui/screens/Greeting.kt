package com.example.wander.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.wander.R
import com.example.wander.ui.WViewModel
import com.example.wander.ui.theme.WanderTheme


@Composable
fun Greeting(WViewModel: WViewModel,
             message: String,
             continueButtonClicked: () -> Unit,
             modifier: Modifier = Modifier
) {
    val image = painterResource(R.drawable.world)
    val uiState by WViewModel.uiState.collectAsState()
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {
        Image(
            painter = image,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            // 添加align修饰符
        )
        Text(
            text = "Welcome to",
            style = MaterialTheme.typography.displayMedium

        )
        Text(
            text = message,
            style = MaterialTheme.typography.displayLarge,
            color = Color.Blue
        )
        Spacer(modifier = Modifier.height(90.dp))
        //EditUserName(uiState.userName, onValueChange = {WViewModel.updateUsername(it)}, modifier = Modifier)
        Button(onClick = {continueButtonClicked()}) {
            Text(
                stringResource(R.string.continue_button),
                style = MaterialTheme.typography.displayMedium)
        }

    }
}

//@SuppressLint("UnrememberedMutableState")
//@Composable
//fun EditUserName(name:String,
//                 onValueChange: (String) -> Unit,
//                 modifier: Modifier = Modifier
//) {
//    TextField(
//        label = { Text(stringResource(R.string.user_name)) },
//        value = name,
//        onValueChange = onValueChange,
//        singleLine = true,
//        modifier = modifier,
//        keyboardOptions = KeyboardOptions(
//            keyboardType = KeyboardType.Text,
//            imeAction = ImeAction.Done,
//        )
//    )
//}



@Preview(showBackground = true)
@Composable
fun Greetingview() {
    WanderTheme(
        //darkTheme = true
    ) {
        Greeting(WViewModel = viewModel(),message=stringResource(R.string.app_name),
            continueButtonClicked = {},
            modifier = Modifier.verticalScroll(rememberScrollState())//建立垂直捲軸,
                .fillMaxSize()
                .wrapContentSize(Alignment.Center))
    }

}
