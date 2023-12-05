package com.dominate.thirtySecondsChallenge.utils.extensions

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.net.Uri

fun String.openFacebookPage(context: Context) {
    try {
        context.packageManager.getPackageInfo("com.facebook.katana", 0)
        context.startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("fb://page/$this")
            )
        )
    } catch (e: Exception) {
        context.startActivity(
            Intent(
                Intent.ACTION_VIEW,
//                Uri.parse(DEFAULT_FACEBOOK)
            )
        )
    }
}

fun String.openInstagramPage(context: Context) {
    val uri = Uri.parse("http://instagram.com/_u/$this")
    val likeIng = Intent(Intent.ACTION_VIEW, uri)
    likeIng.setPackage("com.instagram.android")
    try {
        context.startActivity(likeIng)
    } catch (e: ActivityNotFoundException) {
        context.startActivity(
            Intent(
                Intent.ACTION_VIEW,
//                Uri.parse(DEFAULT_INSTAGRAM)
            )
        )
    }
}

fun String.openTwitterPage(context: Context) {
    try {
        context.startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse("twitter://user?screen_name=$this")
            )
        )
    } catch (e: java.lang.Exception) {
        context.startActivity(
            Intent(
                Intent.ACTION_VIEW,
//                Uri.parse(DEFAULT_TWITTER)
            )
        )
    }
}