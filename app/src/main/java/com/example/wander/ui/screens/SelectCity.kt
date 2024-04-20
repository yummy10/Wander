package com.example.wander.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.wander.R
import com.example.wander.model.City
import com.example.wander.network.BASE_URL
import com.example.wander.ui.WViewModel
import com.example.wander.ui.components.WanderBottomNavigation
import com.example.wander.ui.components.WanderTopAppBar


// 定义一个 Composable 函数，用于显示城市列表
@Composable
fun CityList(
    continueButtonClicked: () -> Unit, // 继续按钮点击事件处理器
    backButtonClicked: () -> Unit, // 返回按钮点击事件处理器
    navController: NavHostController, // 导航控制器
    wViewModel: WViewModel, // 视图模型
    modifier: Modifier = Modifier // 可选的修饰符
) {
    // 从视图模型中收集城市列表的状态
    val cities by wViewModel.cities.collectAsState(initial = emptyList())

    // 使用 Scaffold 创建基本的 UI 结构，包括顶部和底部应用栏
    Scaffold(bottomBar = { WanderBottomNavigation(navController) },
        topBar = { WanderTopAppBar(backButtonClicked = backButtonClicked) }) {
        // 使用 LazyColumn 显示城市列表，这是一个懒加载的垂直滚动列表
        LazyColumn(contentPadding = it, modifier = modifier) {
            // 遍历城市列表并为每个城市创建一个条目
            items(cities) { city ->
                CityItem(
                    city = city,
                    continueButtonClicked = continueButtonClicked,
                    wViewModel = wViewModel,
                    modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_small))
                )
            }
        }
    }
}

// 定义一个 Composable 函数，用于显示单个城市的卡片
@Composable
fun CityItem(
    city: City, // 城市数据模型
    continueButtonClicked: () -> Unit, // 继续按钮点击事件处理器
    wViewModel: WViewModel, // 视图模型
    modifier: Modifier = Modifier // 可选的修饰符
) {
    // 使用 Card 组件创建一个卡片
    Card(modifier = modifier) {
        // 使用 Column 组件在卡片内部垂直排列内容
        Column {
            // 使用 Image 组件显示城市图片
            Image(
                painter = rememberAsyncImagePainter(model = "$BASE_URL${city.cityImagePath}"),
                contentDescription = city.cityName,
                modifier = Modifier
                    .fillMaxWidth() // 图片填充最大宽度
                    .height(194.dp) // 设置图片高度
                    .clickable {
                        ToSelectList(city, wViewModel) // 点击图片时触发的事件
                        continueButtonClicked()
                    },
                contentScale = ContentScale.Crop // 图片裁剪方式
            )
            // 使用 Text 组件显示城市名称
            Text(
                text = city.cityName,
                style = MaterialTheme.typography.displayMedium, // 文本样式
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_small)) // 文本周围的填充
            )
        }
    }
}

// 定义一个函数，用于处理选择列表的逻辑
fun ToSelectList(city: City, wViewModel: WViewModel) {
    wViewModel.getSearchPlaces(city = city.cityName, name = null) // 获取搜索地点
    wViewModel.setCity(city) // 设置当前城市
}
