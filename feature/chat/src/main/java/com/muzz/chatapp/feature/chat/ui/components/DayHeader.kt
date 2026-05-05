package com.muzz.chatapp.feature.chat.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.muzz.chatapp.core.designsystem.MuzzColors
import com.muzz.chatapp.core.designsystem.MuzzTheme

@Composable
fun DayHeader(
    label: String,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = annotateDayHeader(label),
            color = MuzzColors.OnSurfaceMuted,
            style = MaterialTheme.typography.labelMedium,
        )
    }
}

private fun annotateDayHeader(label: String): AnnotatedString {
    val splitIndex = label.lastIndexOf(' ')
    if (splitIndex <= 0) return AnnotatedString(label)
    val day = label.substring(0, splitIndex)
    val time = label.substring(splitIndex + 1)
    return buildAnnotatedString {
        withStyle(SpanStyle(fontWeight = FontWeight.SemiBold)) { append(day) }
        append(' ')
        append(time)
    }
}

@Preview
@Composable
private fun DayHeaderPreview() {
    MuzzTheme {
        Box(Modifier.background(Color.White)) {
            DayHeader(label = "Thursday 11:59")
        }
    }
}
