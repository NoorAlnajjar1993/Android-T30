package com.dominate.thirtySecondsChallenge.utils.pref

import android.util.Base64
import java.nio.charset.StandardCharsets


fun String.encodeTextToBase64(): String {
    return try {
        val data: ByteArray = this.toByteArray()
        Base64.encodeToString(data, Base64.DEFAULT)
    } catch (e: Exception) {
        this
    }
}

fun String.decodeBase64ToText(): String {
    return try {
        val data: ByteArray = Base64.decode(this, Base64.DEFAULT)
        String(data, StandardCharsets.UTF_8)
    } catch (e: Exception) {
        this
    }
}