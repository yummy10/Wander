package com.example.wander.data
import com.example.wander.model.Comment
import com.example.wander.network.WanderApi

interface MessagesRepository {
    suspend fun getMessages(): List<Comment>
    suspend fun addMessage(comment: Comment)
    suspend fun addLike(messageId: Int)
    suspend fun subLike(messageId: Int)
}
class NetworkMessagesRepository():MessagesRepository{
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
}