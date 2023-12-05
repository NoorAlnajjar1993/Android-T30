package com.dominate.thirtySecondsChallenge.utils.extensions

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.util.Base64
import java.security.MessageDigest

fun getSHA1(context: Context): String {
    try {
        val info: PackageInfo = context.packageManager.getPackageInfo(
            context.packageName,
            PackageManager.GET_SIGNATURES
        )
        for (signature in info.signatures) {
            val md: MessageDigest = MessageDigest.getInstance("SHA")
            md.update(signature.toByteArray())
            return Base64.encodeToString(md.digest(), Base64.NO_WRAP)
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return "SHA-1 not found"
}
