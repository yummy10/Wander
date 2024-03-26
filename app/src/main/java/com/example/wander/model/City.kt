package com.example.wander.model

import kotlinx.serialization.Serializable


@Serializable
data class City(
    val cityId: Int, val cityName: String, val cityImageName: String, val cityImagePath: String

)