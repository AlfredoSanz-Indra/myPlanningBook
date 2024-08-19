package com.alfred.myplanningbook.core.util

import com.alfred.myplanningbook.core.log.Klog
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

    fun formatDate(year: Int, month: Int, day: Int): String {
        var formateDay = day.toString()
        if(day < 10) {
            formateDay = "0$formateDay"
        }
        var formatedMonth = month.toString()
        if(month < 10) {
            formatedMonth = "0$formatedMonth"
        }
        return "$formateDay/$formatedMonth/${year.toString()}"
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
}