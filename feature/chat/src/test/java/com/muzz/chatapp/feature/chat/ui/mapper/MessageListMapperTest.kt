package com.muzz.chatapp.feature.chat.ui.mapper

import com.google.common.truth.Truth.assertThat
import com.muzz.chatapp.feature.chat.domain.Message
import com.muzz.chatapp.feature.chat.ui.state.ChatListItem
import java.time.Clock
import java.time.Instant
import java.time.ZoneOffset
import org.junit.Test

class MessageListMapperTest {

    private val baseInstant: Instant = Instant.parse("2024-03-07T10:00:00Z")
    private val mapper = MessageListMapper(
        headerFormatter = DayHeaderFormatter(
            clock = Clock.fixed(baseInstant.plusSeconds(3600), ZoneOffset.UTC),
            zoneId = ZoneOffset.UTC,
        ),
    )

    @Test
    fun `empty input produces empty output`() {
        assertThat(mapper.toListItems(emptyList(), viewerId = "me")).isEmpty()
    }

    @Test
    fun `single message produces a header followed by one bubble`() {
        val items = mapper.toListItems(listOf(msg(1, "sarah", "hi", baseInstant)), viewerId = "me")

        assertThat(items).hasSize(2)
        assertThat(items[0]).isInstanceOf(ChatListItem.Header::class.java)
        assertThat((items[1] as ChatListItem.Bubble).isMine).isFalse()
        assertThat((items[1] as ChatListItem.Bubble).isTightlySpaced).isFalse()
    }

    @Test
    fun `header inserted only on gap greater than one hour`() {
        val items = mapper.toListItems(
            listOf(
                msg(1, "sarah", "a", baseInstant),                              // always — header
                msg(2, "sarah", "b", baseInstant.plusSeconds(60 * 60)),          // exactly 1h after #1 — no header
                msg(3, "sarah", "c", baseInstant.plusSeconds(2 * 60 * 60 + 1)),  // 1h+1s after #2 — header
            ),
            viewerId = "me",
        )

        val headers = items.filterIsInstance<ChatListItem.Header>()
        assertThat(headers).hasSize(2)
    }

    @Test
    fun `tight spacing applies when next is same sender and within twenty seconds`() {
        val items = mapper.toListItems(
            listOf(
                msg(1, "sarah", "a", baseInstant),
                msg(2, "sarah", "b", baseInstant.plusSeconds(15)),
            ),
            viewerId = "me",
        )

        val bubbles = items.filterIsInstance<ChatListItem.Bubble>()
        assertThat(bubbles[0].isTightlySpaced).isTrue()
        assertThat(bubbles[1].isTightlySpaced).isFalse()
    }

    @Test
    fun `tight spacing does not apply when next is different sender`() {
        val items = mapper.toListItems(
            listOf(
                msg(1, "sarah", "a", baseInstant),
                msg(2, "me", "b", baseInstant.plusSeconds(5)),
            ),
            viewerId = "me",
        )

        val bubbles = items.filterIsInstance<ChatListItem.Bubble>()
        assertThat(bubbles[0].isTightlySpaced).isFalse()
    }

    @Test
    fun `tight spacing does not apply at exactly twenty seconds`() {
        val items = mapper.toListItems(
            listOf(
                msg(1, "sarah", "a", baseInstant),
                msg(2, "sarah", "b", baseInstant.plusSeconds(20)),
            ),
            viewerId = "me",
        )

        val bubbles = items.filterIsInstance<ChatListItem.Bubble>()
        assertThat(bubbles[0].isTightlySpaced).isFalse()
    }

    @Test
    fun `last message never tightly spaced`() {
        val items = mapper.toListItems(
            listOf(
                msg(1, "sarah", "only", baseInstant),
            ),
            viewerId = "me",
        )

        val bubble = items.filterIsInstance<ChatListItem.Bubble>().single()
        assertThat(bubble.isTightlySpaced).isFalse()
    }

    @Test
    fun `viewer id determines isMine flag`() {
        val items = mapper.toListItems(
            listOf(msg(1, "me", "hello", baseInstant)),
            viewerId = "me",
        )
        assertThat((items.last() as ChatListItem.Bubble).isMine).isTrue()

        val flipped = mapper.toListItems(
            listOf(msg(1, "me", "hello", baseInstant)),
            viewerId = "sarah",
        )
        assertThat((flipped.last() as ChatListItem.Bubble).isMine).isFalse()
    }

    private fun msg(id: Long, sender: String, text: String, sentAt: Instant) =
        Message(id = id, senderId = sender, text = text, sentAt = sentAt)
}
