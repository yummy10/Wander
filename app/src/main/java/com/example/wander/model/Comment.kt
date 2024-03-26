package com.example.wander.model

import kotlinx.serialization.Serializable

@Serializable
data class Comment(
    val messageID: Int,
    var userName: String,
    val placeName: String,
    val text: String,
    val mLike: Int,
)