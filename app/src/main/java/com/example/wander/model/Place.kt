package com.example.wander.model

import kotlinx.serialization.Serializable

@Serializable
data class Place(
    val placeId: Int,
    val placeName: String,
    val placeDescription: String,
    val placeIntroduction: String,
    val cityId: Int,
    val placeImageName: String,
    val placeImagePath: String,
    val x:Double,
    val y:Double
)
@Serializable
data class PlaceRequest(
    val place: Place,
    val currentCityName: String,
)

