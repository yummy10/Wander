package com.example.wander.model

import kotlinx.serialization.Serializable

@Serializable
data class Message (
    val messageID: Int,
    val userName: String,
    val placeName: String,
    val text: String,
    val mLike: Int,
)