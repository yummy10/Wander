package com.example.wander.model

data class Place(
    val placeId: Int,
    val placeName: String,
    val placeDescription: String,
    val placeIntroduction: String,
    val cityId: Int,
    val placeImageName: String,
    val placeImagePath: String
)