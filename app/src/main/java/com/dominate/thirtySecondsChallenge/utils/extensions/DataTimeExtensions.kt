package com.dominate.thirtySecondsChallenge.utils.extensions

import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


fun Long.minToMillisecond(): Long {
    return TimeUnit.MINUTES.toMillis(this)
}

fun Long.secondToMillisecond(): Long {
    return TimeUnit.SECONDS.toMillis(this)
}

fun Long.millisecondToSecond(): Long {
    return TimeUnit.MILLISECONDS.toSeconds(this)
}

fun Long.millisecondToHour(): Long {
    return TimeUnit.MILLISECONDS.toHours(this)
}

fun Long.hourToMinutes(): Long {
    return TimeUnit.HOURS.toMinutes(this)
}

fun Long.hourToSecond(): Long {
    return TimeUnit.HOURS.toSeconds(this)
}

fun Long.hourToMillisecond(): Long {
    return TimeUnit.HOURS.toMillis(this)
}

fun Long.millisecondToMinutes(): Long {
    return TimeUnit.MILLISECONDS.toMinutes(this)
}

fun Long.millisecondToDays(): Long {
    return TimeUnit.MILLISECONDS.toDays(this)
}

fun Long.secondFormatting(pattern: String): String {
    return this.secondToMillisecond().millisecondFormatting(pattern)
}

fun Long.millisecondFormatting(pattern: String = DateTimeUtil.FULL_DATE_TIME_FORMATTING): String {
    val dateFormat = SimpleDateFormat(pattern, Locale.ENGLISH)
    val dateTime = Date(this)
    return dateFormat.format(dateTime)
}

fun String?.toMillieSecconds(format: String = DateTimeUtil.FULL_DATE_TIME_FORMATTING): Long {
    val dateFormat = SimpleDateFormat(format, Locale.ENGLISH)
    val date = dateFormat.parse(this)
    return date!!.time
}

fun getTodayName(): String {
    val calendar = Calendar.getInstance()
    val dateFormat = SimpleDateFormat("EEEE", Locale.ENGLISH)
    return dateFormat.format(calendar.time)
}

fun String.getAgeInYears(): Int {
    val inFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH)
    inFormat.timeZone = TimeZone.getTimeZone("UTC")

    val outFormat = SimpleDateFormat("yyyy", Locale.ENGLISH)
    outFormat.timeZone = TimeZone.getDefault()
    val year = outFormat.format(inFormat.parse(this))
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    return currentYear - year.toInt()
}

fun String.getAgeInMonths(): Int {
    val inFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH)
    inFormat.timeZone = TimeZone.getTimeZone("UTC")

    val outFormat = SimpleDateFormat("MM", Locale.ENGLISH)
    outFormat.timeZone = TimeZone.getDefault()
    val month = outFormat.format(inFormat.parse(this))
    val currentMonth = Calendar.getInstance().get(Calendar.MONTH) + 1
    return when {
        currentMonth == month.toInt() -> {
            1
        }
        currentMonth > month.toInt() -> {
            currentMonth - month.toInt()
        }
        else -> {
            0
        }
    }
}

fun getCurrentTime(): String {
    val formatter = SimpleDateFormat("HH:mm:ss")
    val date = Date()
    return formatter.format(date)
}

fun getCurrentHour(): String {
    val formatter = SimpleDateFormat("HH")
    val date = Date()
    return formatter.format(date)
}

fun getCurrentDate(): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd")
    val date = Date()
    return formatter.format(date)
}

fun getHourAfter(hour: Int): String {
    val dateFormat = SimpleDateFormat("HH:mm:ss")
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.HOUR_OF_DAY, hour)
    return dateFormat.format(calendar.time)
}

fun getDaysAfter(day: Int): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss")
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DATE, day)
    return dateFormat.format(calendar.time)
}