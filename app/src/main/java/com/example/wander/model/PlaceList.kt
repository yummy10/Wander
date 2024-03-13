package com.example.wander.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class PlaceList(
    val id: Long,
    @StringRes val stringResourceId: Int,
    @StringRes val description: Int,
    @StringRes val body: Int,
    @DrawableRes val imageResourceId: Int,
    val city: City,
    //val stringResourceName: String? = null
)
