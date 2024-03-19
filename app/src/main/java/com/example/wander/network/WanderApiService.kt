package com.example.wander.network
import com.example.wander.model.City
import com.example.wander.model.Place
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

const val BASE_URL = "http://192.168.3.13:80/api/"
val loggingInterceptor = HttpLoggingInterceptor().apply {
    level = HttpLoggingInterceptor.Level.BODY
}

val okHttpClient = OkHttpClient.Builder()
    .addInterceptor(loggingInterceptor)
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
    .baseUrl(BASE_URL)
    .client(okHttpClient)
    .build()

interface WanderApiService {
    @GET("cities")
    suspend fun getAllCities(): List<City>

    @GET("places")
    suspend fun getAllPlaces(): List<Place>

    @GET("places/search")
    suspend fun getAllSearchPlaces(
        @Query("name") name: String? = null,
        @Query("city") city: String? = null
    ): List<Place>

    @POST("places")
    suspend fun addPlace(@Body place: Place)
}

object WanderApi {
    val retrofitService: WanderApiService by lazy {
        retrofit.create(WanderApiService::class.java)
    }
}