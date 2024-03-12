package com.example.wander.model

import com.example.wander.R

data class UiState(
    val isShowingPlaceList:Boolean = true,
    val placeDate:Map<City,List<PlaceList>> = emptyMap(),
    val currentPlace: City = City.HongKong,
    val currentSelectedPlace: PlaceList = PlaceList(id = -1L,
        R.string.PlaceList1,
        R.string.place_description_1,
        R.string.place_description_1, R.drawable.image1,)
)
{
    val currentPlaceList: List<PlaceList>
        get() = placeDate[currentPlace] ?: listOf()
}