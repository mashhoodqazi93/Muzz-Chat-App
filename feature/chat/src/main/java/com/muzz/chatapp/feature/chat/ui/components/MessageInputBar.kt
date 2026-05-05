package com.muzz.chatapp.feature.chat.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.muzz.chatapp.core.designsystem.MuzzColors
import com.muzz.chatapp.core.designsystem.MuzzTheme

@Composable
fun MessageInputBar(
    draft: String,
    isSendEnabled: Boolean,
    onDraftChange: (String) -> Unit,
    onSendClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 12.dp, vertical = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .weight(1f)
                .clip(RoundedCornerShape(24.dp))
                .border(1.dp, MuzzColors.Pink, RoundedCornerShape(24.dp))
                .padding(horizontal = 16.dp, vertical = 12.dp),
            contentAlignment = Alignment.CenterStart,
        ) {
            BasicTextField(
                value = draft,
                onValueChange = onDraftChange,
                singleLine = false,
                cursorBrush = SolidColor(MuzzColors.Pink),
                textStyle = LocalTextStyle.current.merge(MaterialTheme.typography.bodyLarge.copy(color = MuzzColors.OnSurface)),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
                keyboardActions = KeyboardActions(onSend = { if (isSendEnabled) onSendClick() }),
                modifier = Modifier.fillMaxWidth(),
            )
            if (draft.isEmpty()) {
                Text(
                    text = "Message",
                    color = MuzzColors.OnSurfaceMuted,
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }
        Spacer(Modifier.width(8.dp))
        SendButton(enabled = isSendEnabled, onClick = onSendClick)
    }
}

@Composable
private fun SendButton(enabled: Boolean, onClick: () -> Unit) {
    val background = if (enabled) MuzzColors.Pink else MuzzColors.PinkSoft
    Box(
        modifier = Modifier
            .size(44.dp)
            .clip(CircleShape)
            .background(background),
        contentAlignment = Alignment.Center,
    ) {
        IconButton(onClick = onClick, enabled = enabled) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.Send,
                contentDescription = "Send",
                tint = Color.White,
            )
        }
    }
}

@Preview
@Composable
private fun InputEmptyPreview() {
    MuzzTheme {
        MessageInputBar(draft = "", isSendEnabled = false, onDraftChange = {}, onSendClick = {})
    }
}

@Preview
@Composable
private fun InputFilledPreview() {
    MuzzTheme {
        MessageInputBar(draft = "Hey, Sara looks great", isSendEnabled = true, onDraftChange = {}, onSendClick = {})
    }
}
