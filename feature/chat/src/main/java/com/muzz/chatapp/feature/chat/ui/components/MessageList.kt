package com.muzz.chatapp.feature.chat.ui.components

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.muzz.chatapp.feature.chat.ui.state.ChatListItem

@Composable
fun MessageList(
    items: List<ChatListItem>,
    modifier: Modifier = Modifier,
    listState: LazyListState = rememberLazyListState(),
) {
    val reversed = remember(items) { items.asReversed() }
    LazyColumn(
        modifier = modifier,
        state = listState,
        reverseLayout = true,
        contentPadding = PaddingValues(vertical = 8.dp),
    ) {
        items(items = reversed, key = { it.key }) { item ->
            when (item) {
                is ChatListItem.Header -> DayHeader(label = item.label)
                is ChatListItem.Bubble -> MessageBubble(
                    text = item.text,
                    isMine = item.isMine,
                    isTightlySpaced = item.isTightlySpaced,
                )
            }
        }
    }
}
