package com.alfred.myplanningbook.core.util

import java.time.Instant
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneOffset
import java.time.ZonedDateTime
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

    private fun Instant.asYearNumber(): Int {
        return format("yyyy").toInt()
    }

    private fun Instant.asMonthNumber(): Int {
        return format("MM").toInt()
    }

    private fun Instant.asDayNumber(): Int {
        return format("dd").toInt()
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

    fun dateToYear(date: Long): Int {
        return date?.asInstant()?.asYearNumber() ?: 1900
    }

    fun dateToMonth(date: Long): Int {
        return date?.asInstant()?.asMonthNumber() ?: 1
    }

    fun dateToDay(date: Long): Int {
        return date?.asInstant()?.asDayNumber() ?: 1
    }

    fun currentDate(): Long {
        val zdtNow = ZonedDateTime.now()
        return zdtNow.with(LocalTime.MIDNIGHT).toInstant().toEpochMilli()
    }

    fun currentDateFormatted(): String {
        val zdtNow = ZonedDateTime.now()
        return zdtNow.with(LocalTime.MIDNIGHT).toInstant().asShortString()
    }

    fun currentHour(): Int {
        val zdtNow = ZonedDateTime.now()
        val ldt = LocalDateTime.ofInstant(zdtNow.toInstant(), ZoneOffset.systemDefault())

        return ldt.hour
    }

    fun currentTimeFormatted(): String {
        val zdtNow = ZonedDateTime.now()
        val ldt = LocalDateTime.ofInstant(zdtNow.toInstant(), ZoneOffset.systemDefault())
        val result = ldt.hour.toString() + ":" + "00"

        return result
    }

    fun formatTime(hour: Int, min: Int): String {
        return "$hour:$min"
    }
}