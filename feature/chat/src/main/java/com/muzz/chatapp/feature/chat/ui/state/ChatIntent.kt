package com.muzz.chatapp.feature.chat.ui.state

sealed interface ChatIntent {
    data class DraftChanged(val text: String) : ChatIntent
    data object SendClicked : ChatIntent
    data object ToggleUser : ChatIntent
}
