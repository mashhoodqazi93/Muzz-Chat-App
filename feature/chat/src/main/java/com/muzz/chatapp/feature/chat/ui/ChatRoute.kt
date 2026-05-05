package com.muzz.chatapp.feature.chat.ui

import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.muzz.chatapp.feature.chat.ui.state.ChatEffect

@Composable
fun ChatRoute(
    onBackClick: () -> Unit = {},
    viewModel: ChatViewModel = hiltViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()

    LaunchedEffect(viewModel) {
        viewModel.effects.collect { effect ->
            when (effect) {
                ChatEffect.ScrollToBottom -> listState.animateScrollToItem(0)
            }
        }
    }

    ChatScreen(
        state = state,
        onIntent = viewModel::onIntent,
        onBackClick = onBackClick,
        listState = listState,
    )
}
