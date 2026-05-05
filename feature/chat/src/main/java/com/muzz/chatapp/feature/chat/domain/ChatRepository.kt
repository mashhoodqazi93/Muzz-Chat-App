package com.muzz.chatapp.feature.chat.domain

import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    fun observeMessages(): Flow<List<Message>>
    suspend fun send(text: String, senderId: String, sentAtMs: Long = System.currentTimeMillis())
}
