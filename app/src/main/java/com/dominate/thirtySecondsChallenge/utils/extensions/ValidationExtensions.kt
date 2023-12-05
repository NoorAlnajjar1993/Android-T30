package com.dominate.thirtySecondsChallenge.utils.extensions

import java.util.regex.Pattern

fun String?.isMatchesRegex(regex: String): Boolean {
    return try {
        Pattern.compile(
            regex
        ).matcher(this!!)
            .matches()
    } catch (e: Exception) {
        false
    }
}

//fun String?.validTextAndNumbers(): Boolean {
//    return try {
//        Pattern.compile(
//            Constants.VALID_TEXT_REGEX
//        ).matcher(this!!)
//            .matches()
//    } catch (e: Exception) {
//        false
//    }
//}
//
//fun String?.validData(): Boolean {
//    return try {
//        Pattern.compile(
//            Constants.VALID_DATA_REGEX
//        ).matcher(this!!)
//            .matches()
//    } catch (e: Exception) {
//        false
//    }
//}