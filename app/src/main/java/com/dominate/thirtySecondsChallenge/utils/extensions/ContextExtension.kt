package com.dominate.thirtySecondsChallenge.utils.extensions

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.media.MediaPlayer
import android.net.ConnectivityManager
import android.net.Uri
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.dominate.thirtySecondsChallenge.R
import com.dominate.thirtySecondsChallenge.common.CommonEnums
import com.dominate.thirtySecondsChallenge.utils.pref.PrefConstants.APP_LANGUAGE_VALUE
import com.dominate.thirtySecondsChallenge.utils.pref.SharedPreferencesUtil
import java.util.*


fun Context?.longToast(toastMessage: String?) {
    if (toastMessage.isNullOrEmpty()) return
    Toast.makeText(this, toastMessage, Toast.LENGTH_LONG).show()
}

fun Context?.shortToast(toastMessage: String?) {
    if (toastMessage.isNullOrEmpty()) return
    Toast.makeText(this, toastMessage, Toast.LENGTH_SHORT).show()
}

fun Fragment.longToast(toastMessage: String?) {
    if (toastMessage.isNullOrEmpty()) return
    Toast.makeText(requireContext(), toastMessage, Toast.LENGTH_LONG).show()
}

fun Fragment.shortToast(toastMessage: String?) {
    if (toastMessage.isNullOrEmpty()) return
    Toast.makeText(requireContext(), toastMessage, Toast.LENGTH_SHORT).show()
}


fun Context?.copyText(view: View,textWillBeCopy: String) {
    this?.let {
        val clipboard: ClipboardManager? =
            ContextCompat.getSystemService(this, ClipboardManager::class.java)
        val clip = ClipData.newPlainText("label", textWillBeCopy)
        if (clipboard == null || clip == null) return
        clipboard.setPrimaryClip(clip)
    }
    view.showSnackbar("تم النسخ",R.drawable.snackbar_success)

}

fun Context.updateLanguage() {
    val local = Locale(
        SharedPreferencesUtil.getInstance(this)
            .getStringPreferences(APP_LANGUAGE_VALUE, CommonEnums.Languages.English.value)
    )
    Locale.setDefault(local)
    val configuration: Configuration = this.resources.configuration
    configuration.setLocale(local)
    this.resources.updateConfiguration(configuration, this.resources.displayMetrics)
}

fun Uri?.openInBrowser(context: Context) {
    this ?: return // Do nothing if uri is null

    val browserIntent = Intent(Intent.ACTION_VIEW, this)
    ContextCompat.startActivity(context, browserIntent, null)
}

fun String?.asUri(): Uri? {
    try {
        return Uri.parse(this)
    } catch (e: Exception) {
    }
    return null
}

fun Activity.refreshLocal() {
    try {

        val language = SharedPreferencesUtil.getInstance(this)
            .getStringPreferences(APP_LANGUAGE_VALUE, CommonEnums.Languages.English.value)

        updateLanguage()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (language == CommonEnums.Languages.English.value) {
                this.window.decorView.layoutDirection = View.LAYOUT_DIRECTION_LTR
            } else {
                this.window.decorView.layoutDirection = View.LAYOUT_DIRECTION_RTL

            }
        }

    } catch (ignore: Exception) {

    }
}

fun Context?.deviceIsConnectedToInternet(): Boolean {
    val netInfo =
        (this?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo

    if (netInfo != null) {
        if (netInfo.isConnected) {
            return true
        }
    }
    return false
}

//fun Context?.isGooglePlayServicesAvailable(): Boolean {
//    val googleApiAvailability = GoogleApiAvailability.getInstance()
//    val resultCode = googleApiAvailability.isGooglePlayServicesAvailable(this)
//    return resultCode == ConnectionResult.SUCCESS
//}

fun Context.getLocaleString(enString: String?, arString: String?): String? {
    return if (SharedPreferencesUtil.getInstance(this).getStringPreferences(
            APP_LANGUAGE_VALUE,
            CommonEnums.Languages.English.value
        ) == CommonEnums.Languages.English.value
    ) {
        enString
    } else {
        arString
    }
}

fun Vibrator?.vibrate(patternSize: Int = 5, maxDuration: Long = 1000L) {
    this?.let { vibrator ->
        val pattern = LongArray(patternSize) {
            (1..maxDuration).random()
        }
        val repeat = -1 // Repeat indefinitely
        vibrator.vibrate(pattern, repeat)
    }
}

fun vibrate(context: Context) {
    val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    if (Build.VERSION.SDK_INT >= 26) {
        vibrator.vibrate(VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE))
    } else {
        vibrator.vibrate(50)
    }
}

fun playSound(context: Context) {
    val mediaPlayer = MediaPlayer.create(context, R.raw.select)
    mediaPlayer.start()
    mediaPlayer.setOnCompletionListener { mp -> mp.release() }
}

fun playSoundSplash(context: Context) {
    val mediaPlayer = MediaPlayer.create(context, R.raw.champion1)
    mediaPlayer.start()
    mediaPlayer.setOnCompletionListener { mp -> mp.release() }
}

fun addPlaySound(context: Context, sound: Int) {
    val mediaPlayer = MediaPlayer.create(context, sound)
    mediaPlayer.start()
    mediaPlayer.setOnCompletionListener { mp -> mp.release() }
}

fun <T> areListsNotEqual(list1: List<T>, list2: List<T>?): Boolean {
    if (list1.size != list2?.size) {
        return true // Lists are of different sizes, so they cannot be equal
    }

    for (element in list1) {
        if (element !in list2) {
            return true // Element from list1 is not in list2, so the lists are not equal
        }
    }

    return false // Lists are equal
}

fun <T> getDifferentItems(list1: List<T>, list2: List<T>): List<T> {
    val differentItems = mutableListOf<T>()

    for (item in list1) {
        if (item !in list2) {
            differentItems.add(item)
        }
    }

    for (item in list2) {
        if (item !in list1) {
            differentItems.add(item)
        }
    }

    return differentItems
}

fun <T> getDifferentItemsAccept(list1: List<T>, list2: List<T>): List<T> {
    val differentItems = mutableListOf<T>()

    for (item in list1) {
        if (item !in list2) {
            differentItems.add(item)  //1 => 2
        }
    }
    return differentItems
}

fun <T> getDifferentItemsReject(list1: List<T>, list2: List<T>): List<T> {
    val differentItems = mutableListOf<T>()

    for (item in list2) {
        if (item !in list1) {
            differentItems.add(item)
        }
    }
    return differentItems
}