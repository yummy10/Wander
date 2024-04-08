package com.example.wander.data

import android.content.Context
import android.net.Uri
import com.example.wander.model.MyString
import com.example.wander.model.User
import com.example.wander.network.ChangePasswordRequest
import com.example.wander.network.WanderApi
import com.google.gson.Gson
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.FileNotFoundException
import java.io.InputStream

interface AuthRepository {
    suspend fun login(username: String, password: String): User?
    suspend fun create(user: User): MyString
    suspend fun changePassword(
        encryptedUsername: String,
        encryptedPassword: String,
        encryptedNewPassword: String
    ): User?
    suspend fun addIcon(context: Context,name: String, imageUri: Uri)
}

class NetworkAuthRepository : AuthRepository {
    override suspend fun login(username: String, password: String): User? {
        return try {
            val response =
                WanderApi.retrofitService.login(User(userName = username, userPassword = password))
            if (response.isSuccessful && response.body() != null) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
    override suspend fun changePassword(username: String, password: String,newPassword: String): User? {
        return try {
            val response =
                WanderApi.retrofitService.change(ChangePasswordRequest(User(userName = username, userPassword = password),newPassword))
            if (response.isSuccessful && response.body() != null) {
                response.body()
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }
    override suspend fun create(user: User): MyString {
        return WanderApi.retrofitService.create(user)
    }
    override suspend fun addIcon(context: Context,name: String, imageUri: Uri) {
        val gson = Gson()
        val name = gson.toJson(name).toRequestBody("application/json; charset=utf-8".toMediaTypeOrNull())
        // 創建城市名稱的Part
        val namePart = MultipartBody.Part.createFormData("name","name.json", name)
        // 從Uri準備文件Part
        val imagePart = prepareFilePart(context, "image", imageUri)

        // 創建包含所有Part的請求體
        val requestBody = listOf(namePart, imagePart)

        // 使用Retrofit服務發送請求
        val response = WanderApi.retrofitService.addIcon(requestBody)
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