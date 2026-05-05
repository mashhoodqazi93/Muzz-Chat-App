package com.muzz.chatapp.feature.chat.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.muzz.chatapp.feature.chat.domain.ChatRepository
import com.muzz.chatapp.feature.chat.domain.User
import com.muzz.chatapp.feature.chat.ui.mapper.MessageListMapper
import com.muzz.chatapp.feature.chat.ui.state.ChatIntent
import com.muzz.chatapp.feature.chat.ui.state.ChatState
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val repository: ChatRepository,
    private val mapper: MessageListMapper,
) : ViewModel() {

    private val viewer: User = User.Me
    private val draft = MutableStateFlow("")
    private val activeSender = MutableStateFlow<User>(User.Me)

    val state: StateFlow<ChatState> = combine(
        repository.observeMessages(),
        draft,
        activeSender,
    ) { messages, currentDraft, sender ->
        ChatState(
            items = mapper.toListItems(messages, viewerId = viewer.id),
            draft = currentDraft,
            activeSender = sender,
            isLoading = false,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(SUBSCRIPTION_TIMEOUT_MS),
        initialValue = ChatState(),
    )

    fun onIntent(intent: ChatIntent) {
        when (intent) {
            is ChatIntent.DraftChanged -> draft.value = intent.text
            ChatIntent.SendClicked -> sendDraft()
            ChatIntent.ToggleUser -> activeSender.update { User.other(it) }
        }
    }

    private fun sendDraft() {
        val text = draft.value.trim()
        if (text.isEmpty()) return
        val sender = activeSender.value
        draft.value = ""
        viewModelScope.launch {
            repository.send(text = text, senderId = sender.id)
        }
    }

    private companion object {
        const val SUBSCRIPTION_TIMEOUT_MS = 5_000L
    }
}
