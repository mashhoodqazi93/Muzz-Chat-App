package com.muzz.chatapp.feature.chat.domain

import java.time.Instant

data class Message(
    val id: Long,
    val senderId: String,
    val text: String,
    val sentAt: Instant,
)
