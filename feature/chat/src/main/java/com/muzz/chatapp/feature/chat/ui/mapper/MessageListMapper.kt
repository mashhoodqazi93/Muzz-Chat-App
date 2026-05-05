package com.muzz.chatapp.feature.chat.ui.mapper

import com.muzz.chatapp.feature.chat.domain.Message
import com.muzz.chatapp.feature.chat.ui.state.ChatListItem
import javax.inject.Inject

class MessageListMapper @Inject constructor(
    private val headerFormatter: DayHeaderFormatter,
) {

    fun toListItems(messages: List<Message>, viewerId: String): List<ChatListItem> {
        if (messages.isEmpty()) return emptyList()

        val items = ArrayList<ChatListItem>(messages.size + messages.size / 4)
        for ((i, msg) in messages.withIndex()) {
            val previous = messages.getOrNull(i - 1)
            if (previous == null || msg.sentAt.toEpochMilli() - previous.sentAt.toEpochMilli() > HEADER_GAP_MS) {
                items += ChatListItem.Header(
                    key = "header-${msg.sentAt.toEpochMilli()}-${msg.id}",
                    label = headerFormatter.format(msg.sentAt),
                )
            }

            val next = messages.getOrNull(i + 1)
            val tightlySpaced = next != null &&
                next.senderId == msg.senderId &&
                next.sentAt.toEpochMilli() - msg.sentAt.toEpochMilli() < TIGHT_SPACING_MS

            items += ChatListItem.Bubble(
                key = "bubble-${msg.id}",
                text = msg.text,
                isMine = msg.senderId == viewerId,
                isTightlySpaced = tightlySpaced,
            )
        }
        return items
    }

    private companion object {
        const val HEADER_GAP_MS: Long = 60L * 60L * 1000L
        const val TIGHT_SPACING_MS: Long = 20L * 1000L
    }
}
