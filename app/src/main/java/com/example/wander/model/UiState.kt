package com.example.wander.model

data class UiState(
    val isShowingPlaceList:Boolean = true,
    val placedate:Map<City,List<PlaceList>> = emptyMap(),
    val currentplace: City = City.HongKong,
)
