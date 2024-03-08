package com.example.wander

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.wander.ui.theme.WanderTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WanderTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    //Greetingview()
                    palceApp()
                }
            }
        }
    }
}



@Composable
fun Greeting(message: String,modifier: Modifier = Modifier) {
    val image = painterResource(R.drawable.world)
    var name by remember { mutableStateOf("") }
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
        EditUserName(name, onValueChange = {name = it}, modifier = Modifier)
        Button(onClick = {}) {
            Text(stringResource(R.string.continue_button),
                style = MaterialTheme.typography.displayMedium)
        }


    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun EditUserName(name:String,
                 onValueChange: (String) -> Unit,
                 modifier: Modifier = Modifier) {
    var name=name
    TextField(
        label = { Text(stringResource(R.string.user_name))},
        value = name,
        onValueChange = onValueChange,
        singleLine = true,
        modifier = modifier,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            imeAction = ImeAction.Done,
            )
    )
}



@Preview(showBackground = true)
@Composable
fun Greetingview() {
    WanderTheme(
        //darkTheme = true
        ) {
        Greeting("Wander",
                modifier = Modifier.verticalScroll(rememberScrollState())//建立垂直捲軸,
                .fillMaxSize()
                .wrapContentSize(Alignment.Center))
    }

}
