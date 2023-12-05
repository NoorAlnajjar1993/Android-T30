package com.dominate.thirtySecondsChallenge.data.util

import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.webkit.CookieManager
import com.dominate.thirtySecondsChallenge.utils.pref.SharedPreferencesUtil


abstract class DataUtil {

    companion object {

        fun handleLogoutUserData(context: Context){
            clearSharedPref(context)
            clearCookies()
            clearNotifications(context)
        }

        fun clearSharedPref(context: Context) {
            SharedPreferencesUtil.getInstance(context).clearPreference()
        }

        fun clearCookies() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                CookieManager.getInstance().removeAllCookies(null)
                CookieManager.getInstance().flush()
            }
        }

        fun clearNotifications(mContext: Context?) {
            val notificationManager =
                mContext!!.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.cancelAll()
        }
    }
}