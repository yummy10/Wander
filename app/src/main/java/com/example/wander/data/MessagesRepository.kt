package com.example.wander.data
import com.example.wander.model.Comment
import com.example.wander.network.WanderApi

interface MessagesRepository {
    suspend fun getMessages(): List<Comment>
    suspend fun addMessage(comment: Comment)
    suspend fun addLike(messageId: Int)
    suspend fun subLike(messageId: Int)
    suspend fun isPlaceNameValid(placeName: String):Boolean
    suspend fun showingComments(userID: Int): List<Comment>
}
class NetworkMessagesRepository:MessagesRepository{
    override suspend fun getMessages(): List<Comment>{
        return WanderApi.retrofitService.getAllMessages()
    }
    override suspend fun addMessage(comment: Comment) {
        return WanderApi.retrofitService.addMessage(comment)
    }

    override suspend fun addLike(messageId: Int) {
        return WanderApi.retrofitService.addLike(messageId)
    }
    override suspend fun subLike(messageId: Int) {
        return WanderApi.retrofitService.subLike(messageId)
    }
    override suspend fun isPlaceNameValid(placeName: String): Boolean {
        return WanderApi.retrofitService.isPlaceNameValid(placeName) != 0

    }
    override suspend fun showingComments(userID: Int): List<Comment>{
        return WanderApi.retrofitService.showingUserComments(userID)
    }
}