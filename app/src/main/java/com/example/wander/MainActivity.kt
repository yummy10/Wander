package com.example.wander


import android.app.AlertDialog
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
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.example.wander.ui.LoginViewModel
import com.example.wander.ui.PhotoViewModel
import com.example.wander.ui.WViewModel
import com.example.wander.ui.theme.WanderTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity(){

    val photoViewModel by viewModels<PhotoViewModel>()
//    private val cameraLauncher = registerForActivityResult(ActivityResultContracts.TakePicture()) { isSuccess ->
        // 处理拍摄的图片
//    }
    val wViewModel by viewModels<WViewModel>()
    private lateinit var mLocationClient: AMapLocationClient
    private var mLocationOption: AMapLocationClientOption? = null

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

    fun initLocation() {
        var coordinates: Pair<Double, Double>? = null
        val locationChannel = Channel<Pair<Double, Double>?>(Channel.CONFLATED)
  //      var isCoordinatesOK = true
        // 初始化定位客户端
        mLocationClient = AMapLocationClient(getApplicationContext())
        // 设置定位回调监听
        mLocationClient.setLocationListener { aMapLocation ->
            if (aMapLocation != null) {
                if (aMapLocation.errorCode == 0) {
                    // 获取经纬度并存储在coordinates中
                    coordinates = Pair(aMapLocation.latitude, aMapLocation.longitude)
                    locationChannel.trySend(coordinates).isSuccess
                    mLocationClient.stopLocation()
                } else {
                    showLocationErrorDialog(this)
                }
            }
        }
        // 启动定位
        mLocationClient.startLocation()
        CoroutineScope(Dispatchers.Main).launch {
            try {
                val coordinates = locationChannel.receive()
                coordinates?.let { wViewModel.setCoordinates(it) }
            } catch (e: Exception) {
            } finally {
                locationChannel.close() // 确保通道被关闭
                mLocationClient.onDestroy() // 销毁定位客户端
            }
        }
    }
    fun showLocationErrorDialog(context: Context) {
        AlertDialog.Builder(context).apply {
            setTitle("定位错误") // 设置弹窗标题
            setMessage("无法获取当前位置，请检查设备的定位服务是否开启，并授予应用定位权限。") // 设置弹窗消息内容
            setPositiveButton("确定") { dialog, which ->
                // 点击确定按钮的操作
                dialog.dismiss()
            }
            setNegativeButton("取消") { dialog, which ->
                // 点击取消按钮的操作
                dialog.dismiss()
            }
            setCancelable(false) // 设置弹窗不可取消
            show() // 显示弹窗
        }
    }

//    fun openCamera() {
//        // 启动相机
//       // cameraLauncher.launch(/* 相机参数 */)
//    }


}
