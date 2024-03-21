package com.example.wander.model

data class UiState(

    val isShowingPlaceList:Boolean = true,
    val currentPlace:String="",
    val currentId:Int=-1,
    val currentSelectedPlace:Place=Place(-1,"","","",-1,"",""),
    val search:String="",
    val userName:String=""
)
