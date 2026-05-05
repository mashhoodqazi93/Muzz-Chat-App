package com.muzz.chatapp.feature.chat.ui.state

import com.muzz.chatapp.feature.chat.domain.User

data class ChatState(
    val items: List<ChatListItem> = emptyList(),
    val draft: String = "",
    val activeSender: User = User.Me,
    val isLoading: Boolean = true,
) {
    val isSendEnabled: Boolean get() = draft.isNotBlank()
}
