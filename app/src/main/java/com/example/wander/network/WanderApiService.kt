package com.example.wander.network
import com.example.wander.model.City
import com.example.wander.model.Comment
import com.example.wander.model.MyString
import com.example.wander.model.Place
import com.example.wander.model.PlaceRequest
import com.example.wander.model.User
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
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

    @GET("messages")
    suspend fun getAllMessages(): List<Comment>

    @POST("places")
    suspend fun addPlace(@Body placeRequest: PlaceRequest)
    @POST("messages")
    suspend fun addMessage(@Body comment: Comment)

    @PUT("messages/{messageId}/like")
    suspend fun addLike(@Path("messageId") messageId: Int)
    @PUT("messages/{messageId}/sublike")
    suspend fun subLike(@Path("messageId") messageId: Int)
    @GET("messages/{placeName}")
    suspend fun isPlaceNameValid(@Path("placeName") placeName: String):Int
    @POST("users/login")
    suspend fun login(@Body user: User): Response<User>
    @POST("users/create")
    suspend fun create(@Body user: User): MyString
    @POST("users/change")
    suspend fun change(@Body user: ChangePasswordRequest): Response<User>
    @GET("messages/user/{userName}")
    suspend fun showingUserComments(@Path("userName") userName: String):List<Comment>
}

object WanderApi {
    val retrofitService: WanderApiService by lazy {
        retrofit.create(WanderApiService::class.java)
    }
}
@Serializable
data class ChangePasswordRequest(
    val user:User,
    val newPassword:String
)