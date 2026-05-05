package com.muzz.chatapp.feature.chat.data

import com.muzz.chatapp.feature.chat.domain.Message
import java.time.Instant

internal fun MessageEntity.toDomain(): Message = Message(
    id = id,
    senderId = senderId,
    text = text,
    sentAt = Instant.ofEpochMilli(sentAtMs),
)
