package com.alfred.myplanningbook.core.util

import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atTime
import kotlinx.datetime.format
import kotlinx.datetime.format.DayOfWeekNames
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime


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

    val SpanishOfWeekNames = listOf(
        "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"
    )

    private fun getLocalTimeZone(): TimeZone {
        return TimeZone.of("Europe/Madrid")
    }

    private fun getLocalDateTime(date: Long): LocalDateTime {
        val instant = Instant.fromEpochMilliseconds(date)
        return instant.toLocalDateTime(getLocalTimeZone())
    }

    private fun getCurrentLocalDate(): LocalDate {
        val now: Instant = Clock.System.now()
        return now.toLocalDateTime(getLocalTimeZone()).date
    }

    private fun getCurrentLocalDateTime(): LocalDateTime {
        val now: Instant = Clock.System.now()
        return now.toLocalDateTime(getLocalTimeZone())
    }

    fun dateToYear(date: Long): Int {
        return getLocalDateTime(date).year
    }

    fun dateToMonth(date: Long): Int {
        return getLocalDateTime(date).monthNumber
    }

    fun dateToDay(date: Long): Int {
        return getLocalDateTime(date).dayOfMonth
    }

    fun dateToDayString(date: Long): String {
        return getDayOfWeekName(getLocalDateTime(date))
    }

    fun dateToDateString(date: Long): String {
        val dtFormat: kotlinx.datetime.format.DateTimeFormat<LocalDateTime> = getDateTimeFormat("dd/MM/yyyy")
        return getLocalDateTime(date).format(dtFormat)
    }

    fun dateToDateTimeString(date: Long): String {
        val dtFormat: kotlinx.datetime.format.DateTimeFormat<LocalDateTime> = getDateTimeFormat("dd/MM/yyyy HH:mm")
        return getLocalDateTime(date).format(dtFormat)
    }

    fun currentDate(): Long {
        val today = getCurrentLocalDate()
        val todayDateTime: LocalDateTime = today.atTime(0,0)
        return todayDateTime.toInstant(getLocalTimeZone()).toEpochMilliseconds()
    }

    fun currentDateFormatted(): String {
        val today = getCurrentLocalDate()
        val todayDateTime: LocalDateTime = today.atTime(0,0)
        val dtFormat: kotlinx.datetime.format.DateTimeFormat<LocalDateTime> = getDateTimeFormat("dd/MM/yyyy")
        val result: String = todayDateTime.format(dtFormat)
        return result
    }

    fun currentDatePlusDays(days: Long): Long {
        val today = getCurrentLocalDate()
        val todayDateTime: LocalDateTime = today.atTime(0,0)
        val todayInstant: kotlinx.datetime.Instant = todayDateTime.toInstant(getLocalTimeZone())
        val afterInstant: kotlinx.datetime.Instant = todayInstant.plus(days, DateTimeUnit.DAY, getLocalTimeZone())
        return afterInstant.toEpochMilliseconds()
    }

    fun currentDatePlusDaysDayOfWeek(days: Long): Int {
        val today = getCurrentLocalDate()
        val todayDateTime: LocalDateTime = today.atTime(0,0)
        val todayInstant: kotlinx.datetime.Instant = todayDateTime.toInstant(getLocalTimeZone())
        val afterInstant: kotlinx.datetime.Instant = todayInstant.plus(days, DateTimeUnit.DAY, getLocalTimeZone())
        val dayAfter: LocalDateTime = afterInstant.toLocalDateTime(getLocalTimeZone())
        return dayAfter.dayOfWeek.value
    }

    fun currentHour(): Int {
        val today = getCurrentLocalDateTime()
        return today.hour
    }

    fun currentHourPlusHours(hours: Long): Int {
        val today = getCurrentLocalDateTime()
        val todayInstant: kotlinx.datetime.Instant = today.toInstant(getLocalTimeZone())
        val todayAfterInstant = todayInstant.plus(hours, DateTimeUnit.HOUR, getLocalTimeZone())
        val todayAfter: LocalDateTime = todayAfterInstant.toLocalDateTime(getLocalTimeZone())
        return todayAfter.hour
    }

    fun currentTimeFormatted(): String {
        return formatTime(currentHour(), 0)
    }

    fun currentTimeFormattedPlusHours(hours: Long): String {
        return formatTime(currentHourPlusHours(hours), 0)
    }

    private fun getDateTimeFormat(format: String): kotlinx.datetime.format.DateTimeFormat<LocalDateTime> {
        @OptIn(FormatStringsInDatetimeFormats::class)
        val result = LocalDateTime.Format {
            byUnicodePattern(format)
        }
        return result
    }

    private fun getDayOfWeekName(localDateTime: LocalDateTime): String {
        val myDayOfWeekNames = DayOfWeekNames(SpanishOfWeekNames)
        val format = kotlinx.datetime.LocalDateTime.Format {
           // date(LocalDate.Formats.ISO)
           // chars(", ")
            dayOfWeek(myDayOfWeekNames)
        }
        return format.format(localDateTime)
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
}