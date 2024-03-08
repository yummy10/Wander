package com.example.wander.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class PlaceList(
    @StringRes val stringResourceId: Int,
    @StringRes val description: Int,
    @DrawableRes val imageResourceId: Int
)
