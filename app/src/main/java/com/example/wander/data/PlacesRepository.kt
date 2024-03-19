package com.example.wander.data
import com.example.wander.model.Place
import com.example.wander.network.WanderApi

interface PlacesRepository {
    suspend fun getPlaces(): List<Place>
    suspend fun getSearchPlaces(name: String? = null, city: String? = null): List<Place>
    suspend fun addPlace(newPlace: Place)
}
class NetworkPlacesRepository():PlacesRepository{
    override suspend fun getPlaces(): List<Place>{
        return WanderApi.retrofitService.getAllPlaces()
    }
    override suspend fun getSearchPlaces(name: String?, city: String?): List<Place>{
        return WanderApi.retrofitService.getAllSearchPlaces(name, city)
    }

    override suspend fun addPlace(newPlace: Place) {
        WanderApi.retrofitService.addPlace(newPlace)
    }
}