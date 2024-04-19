package com.example.wander.model

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val userID: Int = 0, val userName: String, val userPassword: String, val vip: Boolean = true, val icon: String ="", val email: String ="",val phone: String ="",
)