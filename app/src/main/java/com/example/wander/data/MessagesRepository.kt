package com.example.wander.data
import com.example.wander.model.Message
import com.example.wander.network.WanderApi

interface MessagesRepository {
    suspend fun getMessages(): List<Message>
}
class NetworkMessagesRepository():MessagesRepository{
    override suspend fun getMessages(): List<Message>{
        return WanderApi.retrofitService.getAllMessages()
    }
}