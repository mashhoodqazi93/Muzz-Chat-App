package com.muzz.chatapp.feature.chat.ui.state

sealed interface ChatEffect {
    data object ScrollToBottom : ChatEffect
}
