package com.muzz.chatapp.feature.chat.data

import java.util.concurrent.TimeUnit.MINUTES
import java.util.concurrent.TimeUnit.SECONDS

internal const val SENDER_ME = "me"
internal const val SENDER_SARAH = "sarah"

internal fun seedMessages(now: Long): List<MessageEntity> {
    fun minutesAgo(m: Long, plusSeconds: Long = 0L): Long =
        now - MINUTES.toMillis(m) + SECONDS.toMillis(plusSeconds)

    return listOf(
        MessageEntity(senderId = SENDER_SARAH, text = "Wowsa sounds fun", sentAtMs = minutesAgo(120)),
        MessageEntity(senderId = SENDER_SARAH, text = "Yeh for sure that works. What time do you think?", sentAtMs = minutesAgo(55)),
        MessageEntity(senderId = SENDER_ME, text = "Does 7pm work for you? I've got to go pick up my little brother first from a party", sentAtMs = minutesAgo(54)),
        MessageEntity(senderId = SENDER_SARAH, text = "Ok cool!", sentAtMs = minutesAgo(52)),
        MessageEntity(senderId = SENDER_ME, text = "What are you up to today?", sentAtMs = minutesAgo(40)),
        MessageEntity(senderId = SENDER_SARAH, text = "Nothing much", sentAtMs = minutesAgo(38)),
        MessageEntity(senderId = SENDER_SARAH, text = "Actually just about to go shopping, got any recommendations for a good shoe shop? I'm a fashion disaster", sentAtMs = minutesAgo(38, plusSeconds = 10)),
        MessageEntity(senderId = SENDER_SARAH, text = "The last one went on for hours", sentAtMs = minutesAgo(38, plusSeconds = 25)),
    )
}
