package com.muzz.chatapp.feature.chat.ui

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun ChatRoute(
    onBackClick: () -> Unit = {},
    viewModel: ChatViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()

    // State-driven auto-scroll: when a new message lands in state, snap to the bottom.
    // `state.items` is oldest→newest, so the *last* item's key is what changes on send.
    // Keying on this (rather than emitting a one-shot effect from the VM on send) avoids
    // the race where the effect fires before the Room Flow has propagated the new list
    // through `combine` into Compose.
    //
    // The firstVisibleItemIndex guard preserves history-reading: if the user has scrolled
    // up to look at older messages, an incoming message won't yank them back down.
    val newestKey = state.items.lastOrNull()?.key
    LaunchedEffect(newestKey) {
        if (newestKey != null && listState.firstVisibleItemIndex <= 1) {
            listState.animateScrollToItem(0)
        }
    }

    ChatScreen(
        state = state,
        onIntent = viewModel::onIntent,
        onBackClick = onBackClick,
        listState = listState,
    )
}
