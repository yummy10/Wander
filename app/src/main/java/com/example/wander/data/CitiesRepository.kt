package com.example.wander.data
import com.example.wander.model.City
import com.example.wander.network.WanderApi

interface CitiesRepository {
    suspend fun getCities(): List<City>
}
class NetworkCitiesRepository():CitiesRepository{
    override suspend fun getCities(): List<City>{
        return WanderApi.retrofitService.getAllCities()
    }
}