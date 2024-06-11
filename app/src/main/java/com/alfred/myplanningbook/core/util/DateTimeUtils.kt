package com.alfred.myplanningbook.core.util

import java.time.Instant
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

/**
 * @author Alfredo Sanz
 * @time 2024
 */
object DateTimeUtils {

    fun formatDate(date: Long?): String {
        return date?.asInstant()?.asShortString() ?: "Seleccionar fecha"
    }

    private fun Instant.asShortString(): String {
        return format("dd/MM/yyyy")
    }

    private fun Instant.asMonthAndDayString(): String {
        return format("MMM dd").replaceFirstChar { it.titlecase() }
    }

    private fun Instant.format(pattern: String): String {
        return DateTimeFormatter
            .ofPattern(pattern)
            .withZone(ZoneOffset.UTC)
            .format(this)
    }

    private fun Long?.asInstant(): Instant? = this?.let { Instant.ofEpochMilli(it) }

    fun currentDate(): Long {
        return Instant.now().toEpochMilli()
    }

    fun currentDateFormatted(): String {
        return Instant.now().asShortString()
    }
}