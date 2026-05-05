package com.muzz.chatapp.feature.chat.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.muzz.chatapp.core.designsystem.MuzzColors
import com.muzz.chatapp.core.designsystem.MuzzTheme

@Composable
fun MessageBubble(
    text: String,
    isMine: Boolean,
    isTightlySpaced: Boolean,
    modifier: Modifier = Modifier,
) {
    val bubbleShape = RoundedCornerShape(
        topStart = 18.dp,
        topEnd = 18.dp,
        bottomStart = if (isMine) 18.dp else 4.dp,
        bottomEnd = if (isMine) 4.dp else 18.dp,
    )
    val background = if (isMine) MuzzColors.BubbleSent else MuzzColors.BubbleReceived
    val foreground = if (isMine) MuzzColors.OnBubbleSent else MuzzColors.OnBubbleReceived
    val bottomPadding = if (isTightlySpaced) 2.dp else 8.dp

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 0.dp)
            .padding(bottom = bottomPadding),
        horizontalAlignment = if (isMine) Alignment.End else Alignment.Start,
    ) {
        Box(
            modifier = Modifier
                .widthIn(max = 280.dp)
                .clip(bubbleShape)
                .background(background)
                .padding(horizontal = 14.dp, vertical = 10.dp),
        ) {
            Text(
                text = text,
                color = foreground,
                style = MaterialTheme.typography.bodyLarge,
            )
        }
    }
}

@Preview
@Composable
private fun BubbleSentPreview() {
    MuzzTheme {
        Box(Modifier.background(Color.White)) {
            MessageBubble(text = "Does 7pm work for you?", isMine = true, isTightlySpaced = false)
        }
    }
}

@Preview
@Composable
private fun BubbleReceivedPreview() {
    MuzzTheme {
        Box(Modifier.background(Color.White)) {
            MessageBubble(text = "Yeh for sure that works", isMine = false, isTightlySpaced = false)
        }
    }
}
