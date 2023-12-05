package com.dominate.thirtySecondsChallenge.utils.extensions

import java.net.URLDecoder
import java.util.*



fun String?.toDate(format: String = DateTimeUtil.FULL_DATE_TIME_FORMATTING): Date? {
    if (this == null) return null
    return Date(DateTimeUtil.getTimeStampFromStringDate(this, format))
}

fun String?.toUtf8(): String {
    return URLDecoder.decode(this, "UTF-8")
}

fun String.toInteger(): Int {
    return try {
        toInt()
    } catch (e: Exception) {
        return 0
    }
}

fun String.replaceArabicDigitsWithEnglish() :String{
    var result = ""
    var en = '0'
    for (ch in this) {
        en = ch
        when (ch) {
            '۰' -> en = '0'
            '۱' -> en = '1'
            '۲' -> en = '2'
            '۳' -> en = '3'
            '۴' -> en = '4'
            '۵' -> en = '5'
            '۶' -> en = '6'
            '۷' -> en = '7'
            '۸' -> en = '8'
            '۹' -> en = '9'
        }
        result = "${result}$en"
    }
    return result
}

