package com.example.wander.model

data class UiState(
    val isShowingPlaceList:Boolean = true,
    val currentPlace:String="",
    val currentSelectedPlace:Place=Place(-1,"","","",-1,"",""),
    val search:String=""
)
