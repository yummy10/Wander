package com.example.wander.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class PlaceList(
    /** Unique ID  **/
    val id: Long,
    @StringRes val stringResourceId: Int,
    @StringRes val description: Int,
    @StringRes val body: Int,
    @DrawableRes val imageResourceId: Int,
    var city: City= City.HongKong ,
)

