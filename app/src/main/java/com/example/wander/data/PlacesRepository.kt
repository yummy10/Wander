package com.example.wander.data

import com.example.wander.model.Place
import com.example.wander.model.PlaceRequest
import com.example.wander.network.WanderApi

interface PlacesRepository {
    suspend fun getPlaces(): List<Place>
    suspend fun getSearchPlaces(name: String? = null, city: String? = null): List<Place>
    suspend fun addPlace(newPlace: Place, placeName: String)
}

class NetworkPlacesRepository() : PlacesRepository {
    override suspend fun getPlaces(): List<Place> {
        return WanderApi.retrofitService.getAllPlaces()
    }

    override suspend fun getSearchPlaces(name: String?, city: String?): List<Place> {
        return WanderApi.retrofitService.getAllSearchPlaces(name, city)
    }

    override suspend fun addPlace(newPlace: Place, placeName: String) {
        val placeRequest = PlaceRequest(place = newPlace, placeName = placeName)
        WanderApi.retrofitService.addPlace(placeRequest)
    }
}