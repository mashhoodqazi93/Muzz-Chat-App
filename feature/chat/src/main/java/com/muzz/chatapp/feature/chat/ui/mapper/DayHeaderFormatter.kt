package com.muzz.chatapp.feature.chat.ui.mapper

import java.time.Clock
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Locale
import javax.inject.Inject
import kotlin.math.abs

class DayHeaderFormatter @Inject constructor(
    private val clock: Clock,
    private val zoneId: ZoneId,
) {

    private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault())
    private val dayNameFormatter = DateTimeFormatter.ofPattern("EEEE", Locale.getDefault())
    private val olderFormatter = DateTimeFormatter.ofPattern("d MMM", Locale.getDefault())

    fun format(timestamp: Instant): String {
        val zoned = timestamp.atZone(zoneId)
        val today = LocalDate.now(clock.withZone(zoneId))
        val date = zoned.toLocalDate()
        val time = timeFormatter.format(zoned)

        val daysBetween = today.toEpochDay() - date.toEpochDay()

        val day = when {
            daysBetween == 0L -> "Today"
            daysBetween == 1L -> "Yesterday"
            daysBetween in 2L..6L -> dayNameFormatter.format(zoned)
            // Future-dated messages (e.g. clock skew): treat as a weekday rather than crash.
            daysBetween < 0L && abs(daysBetween) < 7L -> dayNameFormatter.format(zoned)
            else -> olderFormatter.format(zoned)
        }
        return "$day $time"
    }
}
