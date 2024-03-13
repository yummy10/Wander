package com.example.wander.model

data class Search(
    val searchQuery: String = "",
    val searchResults: List<PlaceList> = emptyList(),
    val allList: List<PlaceList> = emptyList()
)