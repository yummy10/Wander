package com.example.wander.model

import kotlinx.serialization.Serializable

@Serializable
class User(
    val userID: Int=0,
    val userName: String,
    val userPassword: String,
    val vip: Boolean=true
) {
}