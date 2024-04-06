package com.example.wander.data

import android.content.Context
import android.net.Uri
import com.example.wander.model.Place
import com.example.wander.model.PlaceRequest
import com.example.wander.network.WanderApi
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.FileNotFoundException
import java.io.InputStream

interface PlacesRepository {

    suspend fun getPlaces(): List<Place>
    suspend fun getSearchPlaces(name: String? = null, city: String? = null): List<Place>
    suspend fun addPlace(newPlace: Place, currentCityName: String)
    suspend fun addPlaceWithImage(context: Context,newPlace: Place, currentCityName: String, imageUri: Uri)
}

class NetworkPlacesRepository() : PlacesRepository {
    override suspend fun getPlaces(): List<Place> {
        return WanderApi.retrofitService.getAllPlaces()
    }

    override suspend fun getSearchPlaces(name: String?, city: String?): List<Place> {
        return WanderApi.retrofitService.getAllSearchPlaces(name, city)
    }

    override suspend fun addPlace(newPlace: Place, currentCityName: String) {
        val placeRequest = PlaceRequest(place = newPlace, currentCityName = currentCityName)
        WanderApi.retrofitService.addPlace(placeRequest)
    }
    override suspend fun addPlaceWithImage(context: Context, newPlace: Place, currentCityName: String, imageUri: Uri) {
        val gson = Gson()
        val placeJson = gson.toJson(newPlace).toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        val currentCityName = gson.toJson(currentCityName).toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        // 創建Place的Part
        val placePart = MultipartBody.Part.createFormData("place", "place.json", placeJson)

        // 創建城市名稱的Part
        val cityNamePart = MultipartBody.Part.createFormData("currentCityName","currentCityName.json", currentCityName)

        // 從Uri準備文件Part
        val imagePart = prepareFilePart(context, "image", imageUri)

        // 創建包含所有Part的請求體
        val requestBody = listOf(placePart, cityNamePart, imagePart)

        // 使用Retrofit服務發送請求
        val response = WanderApi.retrofitService.addPlaceWithImage(requestBody)
    }


    private fun prepareFilePart(context: Context, partName: String, fileUri: Uri): MultipartBody.Part {
        // 获取文件的实际内容（InputStream）
        val inputStream: InputStream = context.contentResolver.openInputStream(fileUri) ?: throw FileNotFoundException("无法获取文件内容")

        // 创建 RequestBody
        val requestBody = inputStream.readBytes().toRequestBody(
            context.contentResolver.getType(fileUri)?.toMediaTypeOrNull()
        )

        // 获取文件名
        val fileName = fileUri.lastPathSegment ?: "file"

        // 创建 MultipartBody.Part
        return MultipartBody.Part.createFormData(partName, fileName, requestBody)
    }



}