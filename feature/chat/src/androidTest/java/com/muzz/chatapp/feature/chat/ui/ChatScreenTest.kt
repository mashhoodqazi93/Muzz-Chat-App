package com.muzz.chatapp.feature.chat.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasSetTextAction
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.google.common.truth.Truth.assertThat
import com.muzz.chatapp.core.designsystem.MuzzTheme
import com.muzz.chatapp.feature.chat.domain.User
import com.muzz.chatapp.feature.chat.ui.state.ChatIntent
import com.muzz.chatapp.feature.chat.ui.state.ChatListItem
import com.muzz.chatapp.feature.chat.ui.state.ChatState
import org.junit.Rule
import org.junit.Test

class ChatScreenTest {

    @get:Rule
    val rule = createComposeRule()

    private val mineText = "Does 7pm work?"
    private val theirsText = "Yeh for sure"

    private fun stateWithBothBubbles(activeSender: User = User.Me): ChatState = ChatState(
        items = listOf(
            ChatListItem.Bubble(key = "1", text = theirsText, isMine = false, isTightlySpaced = false),
            ChatListItem.Bubble(key = "2", text = mineText, isMine = true, isTightlySpaced = false),
        ),
        draft = "",
        activeSender = activeSender,
        isLoading = false,
    )

    @Test
    fun bubbles_are_aligned_to_correct_side() {
        rule.setContent {
            MuzzTheme {
                ChatScreen(
                    state = stateWithBothBubbles(),
                    onIntent = {},
                    onBackClick = {},
                )
            }
        }

        val midpoint = rule.onRoot().fetchSemanticsNode().boundsInRoot.center.x
        val mineCenter = rule.onNodeWithText(mineText).fetchSemanticsNode().boundsInRoot.center.x
        val theirsCenter = rule.onNodeWithText(theirsText).fetchSemanticsNode().boundsInRoot.center.x

        assertThat(mineCenter).isGreaterThan(midpoint)
        assertThat(theirsCenter).isLessThan(midpoint)
    }

    @Test
    fun typing_enables_send_and_send_click_emits_SendClicked() {
        val intents = mutableListOf<ChatIntent>()
        var state by mutableStateOf(ChatState(draft = "", activeSender = User.Me, isLoading = false))

        rule.setContent {
            MuzzTheme {
                ChatScreen(
                    state = state,
                    onIntent = { intent ->
                        intents += intent
                        // Mirror the real reducer enough to make the button enable.
                        if (intent is ChatIntent.DraftChanged) state = state.copy(draft = intent.text)
                    },
                    onBackClick = {},
                )
            }
        }

        rule.onNodeWithContentDescription("Send").assertIsNotEnabled()

        rule.onNode(hasSetTextAction()).performTextInput("hello")
        rule.onNodeWithContentDescription("Send").assertIsEnabled().performClick()

        assertThat(intents).contains(ChatIntent.DraftChanged("hello"))
        assertThat(intents).contains(ChatIntent.SendClicked)
    }
}
