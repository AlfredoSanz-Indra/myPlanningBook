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

    const val LUNES = "LU"
    const val MARTES = "MA"
    const val MIERCOLES = "MI"
    const val JUEVES = "JU"
    const val VIERNES = "VI"
    const val SABADO = "SA"
    const val DOMINGO = "DO"

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

    private fun Instant.asDayString(): String {
        return format("EEE")
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

    fun dateToDayString(date: Long): String {
        return date?.asInstant()?.asDayString() ?: ""
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

    fun currentHourPlusHours(hours: Long): Int {
        val zdtNow = ZonedDateTime.now()
        val zdtPlus = zdtNow.plusHours(hours)
        val ldt = LocalDateTime.ofInstant(zdtPlus.toInstant(), ZoneOffset.systemDefault())

        return ldt.hour
    }

    fun currentTimeFormatted(): String {
        val zdtNow = ZonedDateTime.now()
        val ldt = LocalDateTime.ofInstant(zdtNow.toInstant(), ZoneOffset.systemDefault())
        val result = ldt.hour.toString() + ":" + "00"

        return result
    }

    fun currentTimeFormattedPlusHours(hours: Long): String {
        val zdtNow = ZonedDateTime.now()
        val zdtPlus = zdtNow.plusHours(hours)
        val ldt = LocalDateTime.ofInstant(zdtPlus.toInstant(), ZoneOffset.systemDefault())
        val result = ldt.hour.toString() + ":" + "00"

        return result
    }

    fun formatDate(year: Int, month: Int, day: Int): String {
        var formatedDay = day.toString()
        if(day < 10) {
            formatedDay = "0$formatedDay"
        }
        var formatedMonth = month.toString()
        if(month < 10) {
            formatedMonth = "0$formatedMonth"
        }
        return "$formatedDay/$formatedMonth/${year.toString()}"
    }

    fun formatTime(hour: Int, min: Int): String {
        var formatedMin = min.toString()
        if(min < 10) {
            formatedMin = "0$formatedMin"
        }
        var formatedHour = hour.toString()
        if(hour < 10) {
            formatedHour = "0$formatedHour"
        }
        return "$formatedHour:$formatedMin"
    }

    fun currentDatePlusDays(days: Long): Long {
        val zdtNow = ZonedDateTime.now()
        return zdtNow.plusDays(days).with(LocalTime.MIDNIGHT).toInstant().toEpochMilli()
    }

    /*
     * Days of week start from 1 which is Monday,
     */
    fun currentDayOfWeekPlusDays(days: Long): Int {
        val zdtNow = ZonedDateTime.now()
        return zdtNow.plusDays(days).dayOfWeek.value
    }

    fun sortWeekDaysList(weekDaysList: MutableList<String>): MutableList<String> {

        var result: MutableList<String> = mutableListOf()

        if(weekDaysList.contains(LUNES)) {
            result.add(LUNES)
        }
        if(weekDaysList.contains(MARTES)) {
            result.add(MARTES)
        }
        if(weekDaysList.contains(MIERCOLES)) {
            result.add(MIERCOLES)
        }
        if(weekDaysList.contains(JUEVES)) {
            result.add(JUEVES)
        }
        if(weekDaysList.contains(VIERNES)) {
            result.add(VIERNES)
        }
        if(weekDaysList.contains(SABADO)) {
            result.add(SABADO)
        }
        if(weekDaysList.contains(DOMINGO)) {
            result.add(DOMINGO)
        }
        return result
    }

    fun castDayOfWeekToInt(cday: String): Int {
        var result = when(cday) {
            LUNES -> 1
            MARTES -> 2
            MIERCOLES -> 3
            JUEVES -> 4
            VIERNES -> 5
            SABADO -> 6
            DOMINGO -> 7
            else -> 0
        }

        return result
    }

    fun translateDaysToSpanish(day: String): String {
        return when(day) {
            "Mon" -> "Lunes"
            "Tue" -> "Martes"
            "Wed" -> "Miércoles"
            "Thu" -> "Jueves"
            "Fri" -> "Viernes"
            "Sat" -> "Sábado"
            "Sun" -> "Domingo"
            else -> ""
        }
    }
}