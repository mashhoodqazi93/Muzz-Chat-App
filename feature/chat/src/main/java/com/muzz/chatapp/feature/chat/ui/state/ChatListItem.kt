package com.muzz.chatapp.feature.chat.ui.state

sealed interface ChatListItem {
    val key: String

    data class Header(
        override val key: String,
        val label: String,
    ) : ChatListItem

    data class Bubble(
        override val key: String,
        val text: String,
        val isMine: Boolean,
        val isTightlySpaced: Boolean,
    ) : ChatListItem
}
