package com.muzz.chatapp.feature.chat.ui

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.muzz.chatapp.feature.chat.domain.ChatRepository
import com.muzz.chatapp.feature.chat.domain.Message
import com.muzz.chatapp.feature.chat.domain.User
import com.muzz.chatapp.feature.chat.ui.mapper.DayHeaderFormatter
import com.muzz.chatapp.feature.chat.ui.mapper.MessageListMapper
import com.muzz.chatapp.feature.chat.ui.state.ChatIntent
import java.time.Clock
import java.time.Instant
import java.time.ZoneOffset
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class ChatViewModelTest {

    private val dispatcher = UnconfinedTestDispatcher()
    private val repository = FakeChatRepository()
    private val mapper = MessageListMapper(
        DayHeaderFormatter(
            clock = Clock.fixed(Instant.parse("2024-03-07T12:00:00Z"), ZoneOffset.UTC),
            zoneId = ZoneOffset.UTC,
        ),
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `DraftChanged updates state draft`() = runTest {
        val vm = ChatViewModel(repository, mapper)

        vm.state.test {
            awaitItem() // initial empty state
            vm.onIntent(ChatIntent.DraftChanged("hello"))
            assertThat(awaitItem().draft).isEqualTo("hello")
        }
    }

    @Test
    fun `SendClicked with blank draft is a no-op`() = runTest {
        val vm = ChatViewModel(repository, mapper)

        vm.onIntent(ChatIntent.DraftChanged("   "))
        vm.onIntent(ChatIntent.SendClicked)

        assertThat(repository.sentMessages).isEmpty()
    }

    @Test
    fun `SendClicked sends trimmed draft as current user and clears the field`() = runTest {
        val vm = ChatViewModel(repository, mapper)

        vm.state.test {
            awaitItem() // initial
            vm.onIntent(ChatIntent.DraftChanged("  hello  "))
            awaitItem() // draft updated
            vm.onIntent(ChatIntent.SendClicked)
            val cleared = awaitItem()
            assertThat(cleared.draft).isEmpty()
        }

        assertThat(repository.sentMessages).containsExactly("me" to "hello")
    }

    @Test
    fun `ToggleUser flips current user between Me and Sarah`() = runTest {
        val vm = ChatViewModel(repository, mapper)

        vm.state.test {
            awaitItem()
            assertThat(vm.state.value.activeSender).isEqualTo(User.Me)

            vm.onIntent(ChatIntent.ToggleUser)
            assertThat(awaitItem().activeSender).isEqualTo(User.Sarah)

            vm.onIntent(ChatIntent.ToggleUser)
            assertThat(awaitItem().activeSender).isEqualTo(User.Me)
        }
    }

    @Test
    fun `toggling active sender does not flip alignment of existing messages`() = runTest {
        repository.emit(
            listOf(
                Message(1, "me", "mine", Instant.parse("2024-03-07T11:00:00Z")),
                Message(2, "sarah", "hers", Instant.parse("2024-03-07T11:00:10Z")),
            ),
        )
        val vm = ChatViewModel(repository, mapper)

        vm.state.test {
            // Drain initial states until we see the seeded messages reflected.
            val initial = expectMostRecentItem()
            val bubblesBefore = initial.items.filterIsInstance<com.muzz.chatapp.feature.chat.ui.state.ChatListItem.Bubble>()
            assertThat(bubblesBefore.map { it.isMine }).containsExactly(true, false).inOrder()

            vm.onIntent(ChatIntent.ToggleUser)
            val toggled = awaitItem()
            val bubblesAfter = toggled.items.filterIsInstance<com.muzz.chatapp.feature.chat.ui.state.ChatListItem.Bubble>()
            // Viewer is fixed (Me), so existing alignment must not change.
            assertThat(bubblesAfter.map { it.isMine }).containsExactly(true, false).inOrder()
        }
    }

    @Test
    fun `sending while active sender is Sarah persists message attributed to Sarah`() = runTest {
        val vm = ChatViewModel(repository, mapper)

        vm.onIntent(ChatIntent.ToggleUser) // active sender is now Sarah
        vm.onIntent(ChatIntent.DraftChanged("from sarah"))
        vm.onIntent(ChatIntent.SendClicked)

        assertThat(repository.sentMessages).containsExactly("sarah" to "from sarah")
    }

    @Test
    fun `messages from repo flow into state items`() = runTest {
        val vm = ChatViewModel(repository, mapper)

        vm.state.test {
            assertThat(awaitItem().items).isEmpty()
            repository.emit(
                listOf(
                    Message(1, "sarah", "hi", Instant.parse("2024-03-07T11:00:00Z")),
                ),
            )
            assertThat(awaitItem().items).isNotEmpty()
        }
    }

    private class FakeChatRepository : ChatRepository {
        private val flow = MutableStateFlow<List<Message>>(emptyList())
        val sentMessages = mutableListOf<Pair<String, String>>()

        override fun observeMessages() = flow

        override suspend fun send(text: String, senderId: String, sentAtMs: Long) {
            sentMessages += senderId to text
        }

        fun emit(messages: List<Message>) {
            flow.value = messages
        }
    }
}
