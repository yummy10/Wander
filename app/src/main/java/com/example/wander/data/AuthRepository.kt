package com.example.wander.data

import com.example.wander.model.User
import com.example.wander.network.WanderApi

interface AuthRepository {
    suspend fun login(username: String, password: String): User?
    suspend fun create(user: User): String
}

class NetworkAuthRepository:AuthRepository {
    override suspend fun login(username: String, password: String): User? {
        return try {
            val response = WanderApi.retrofitService.login(User(userName = username, userPassword = password))
            if (response.isSuccessful && response.body() != null) {
                response.body()
            } else {
                // 处理登录失败的情况
                null
            }
        } catch (e: Exception) {
            null
        }
    }
    override suspend fun create(user: User): String {
    return WanderApi.retrofitService.create(user)
    }
}