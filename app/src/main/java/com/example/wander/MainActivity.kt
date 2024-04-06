package com.example.wander


import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.example.wander.ui.LoginViewModel
import com.example.wander.ui.PhotoViewModel
import com.example.wander.ui.theme.WanderTheme

class MainActivity : ComponentActivity() {
    val photoViewModel by viewModels<PhotoViewModel>()

    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
        // 处理拍摄的图片
    }

    private val galleryLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        photoViewModel.handleSelectedImage(uri)
    }
    fun openGallery() {
        galleryLauncher.launch("image/*")
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WanderTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    val context = LocalContext.current
                    val sharedPreferences =
                        context.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
                    val viewModel = LoginViewModel(sharedPreferences)
                    WanderApp(viewModel,mainActivity=this@MainActivity)

                }
            }
        }
    }



    fun openCamera() {
        // 启动相机
       // cameraLauncher.launch(/* 相机参数 */)
    }


}
