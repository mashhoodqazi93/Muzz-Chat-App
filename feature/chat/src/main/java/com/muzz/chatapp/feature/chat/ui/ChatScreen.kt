package com.muzz.chatapp.feature.chat.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.muzz.chatapp.core.designsystem.MuzzColors
import com.muzz.chatapp.core.designsystem.MuzzTheme
import com.muzz.chatapp.feature.chat.domain.User
import com.muzz.chatapp.feature.chat.ui.components.ChatTopBar
import com.muzz.chatapp.feature.chat.ui.components.MessageInputBar
import com.muzz.chatapp.feature.chat.ui.components.MessageList
import com.muzz.chatapp.feature.chat.ui.state.ChatIntent
import com.muzz.chatapp.feature.chat.ui.state.ChatListItem
import com.muzz.chatapp.feature.chat.ui.state.ChatState

@Composable
fun ChatScreen(
    state: ChatState,
    onIntent: (ChatIntent) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    listState: LazyListState = rememberLazyListState(),
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.systemBars)
                .imePadding(),
        ) {
            ChatTopBar(
                title = "Sarah",
                activeSender = state.activeSender,
                onBackClick = onBackClick,
                onToggleUser = { onIntent(ChatIntent.ToggleUser) },
            )
            MessageList(
                items = state.items,
                listState = listState,
                modifier = Modifier.weight(1f),
            )

            HorizontalDivider(thickness = 0.5.dp, color = MuzzColors.Divider)

            MessageInputBar(
                draft = state.draft,
                isSendEnabled = state.isSendEnabled,
                onDraftChange = { onIntent(ChatIntent.DraftChanged(it)) },
                onSendClick = { onIntent(ChatIntent.SendClicked) },
            )
        }
    }
}

@Preview(showBackground = true, heightDp = 720)
@Composable
private fun ChatScreenPreview() {
    MuzzTheme {
        ChatScreen(
            state = ChatState(
                items = previewItems,
                draft = "Hey, Sara looks great",
                activeSender = User.Me,
                isLoading = false,
            ),
            onIntent = {},
            onBackClick = {},
        )
    }
}

private val previewItems: List<ChatListItem> = listOf(
    ChatListItem.Bubble("a", "Wowsa sounds fun", isMine = false, isTightlySpaced = false),
    ChatListItem.Header("h1", "Thursday 11:59"),
    ChatListItem.Bubble("b", "Yeh for sure that works. What time do you think?", isMine = false, isTightlySpaced = false),
    ChatListItem.Bubble("c", "Does 7pm work for you? I've got to go pick up my little brother first from a party", isMine = true, isTightlySpaced = false),
    ChatListItem.Bubble("d", "Ok cool!", isMine = false, isTightlySpaced = false),
    ChatListItem.Bubble("e", "What are you up to today?", isMine = true, isTightlySpaced = false),
    ChatListItem.Bubble("f", "Nothing much", isMine = false, isTightlySpaced = true),
    ChatListItem.Bubble("g", "Actually just about to go shopping, got any recommendations for a good shoe shop? I'm a fashion disaster", isMine = false, isTightlySpaced = true),
    ChatListItem.Bubble("h", "The last one went on for hours", isMine = false, isTightlySpaced = false),
)
