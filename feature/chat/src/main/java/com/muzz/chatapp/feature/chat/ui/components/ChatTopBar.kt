package com.muzz.chatapp.feature.chat.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.MoreHoriz
import androidx.compose.material.icons.rounded.SwapHoriz
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.muzz.chatapp.core.designsystem.MuzzColors
import com.muzz.chatapp.core.designsystem.MuzzTheme
import com.muzz.chatapp.feature.chat.domain.User

@Composable
fun ChatTopBar(
    title: String,
    activeSender: User,
    onBackClick: () -> Unit,
    onToggleUser: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 4.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(onClick = onBackClick) {
            Icon(
                imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                contentDescription = "Back",
                tint = MuzzColors.Pink,
            )
        }
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(MuzzColors.PinkSoft),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = title.take(1),
                color = MuzzColors.PinkDark,
                style = MaterialTheme.typography.labelMedium,
            )
        }
        Spacer(Modifier.width(12.dp))
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.weight(1f),
        )
        IconButton(onClick = onToggleUser) {
            Icon(
                imageVector = Icons.Rounded.SwapHoriz,
                contentDescription = "Send as ${User.other(activeSender).displayName}",
                // Pink tint when 'Sarah' is the active sender, signalling we're impersonating her.
                tint = if (activeSender == User.Me) MuzzColors.OnSurfaceMuted else MuzzColors.Pink,
            )
        }
        IconButton(onClick = { /* Not Implemented */ }) {
            Icon(
                imageVector = Icons.Rounded.MoreHoriz,
                contentDescription = "More",
                tint = MuzzColors.OnSurfaceMuted,
            )
        }
    }
    HorizontalDivider(thickness = 0.5.dp, color = MuzzColors.Divider)
}

@Preview
@Composable
private fun ChatTopBarPreview() {
    MuzzTheme {
        Box(Modifier.background(Color.White)) {
            ChatTopBar(title = "Sarah", activeSender = User.Me, onBackClick = {}, onToggleUser = {})
        }
    }
}
