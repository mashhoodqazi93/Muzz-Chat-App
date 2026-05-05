package com.muzz.chatapp.feature.chat.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "messages")
internal data class MessageEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    @ColumnInfo(name = "sender_id") val senderId: String,
    val text: String,
    @ColumnInfo(name = "sent_at_ms") val sentAtMs: Long,
)
