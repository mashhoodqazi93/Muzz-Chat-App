package com.muzz.chatapp.feature.chat.data

import com.muzz.chatapp.feature.chat.domain.ChatRepository
import com.muzz.chatapp.feature.chat.domain.Message
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Singleton
internal class DefaultChatRepository @Inject constructor(
    private val dao: MessageDao,
) : ChatRepository {

    override fun observeMessages(): Flow<List<Message>> =
        dao.observeAll().map { entities -> entities.map { it.toDomain() } }

    override suspend fun send(text: String, senderId: String, sentAtMs: Long) {
        dao.insert(
            MessageEntity(senderId = senderId, text = text, sentAtMs = sentAtMs),
        )
    }
}
