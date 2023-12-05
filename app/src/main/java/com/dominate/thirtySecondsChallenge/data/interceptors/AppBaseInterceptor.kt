package com.dominate.thirtySecondsChallenge.data.interceptors

import android.text.TextUtils
import com.dominate.thirtySecondsChallenge.data.common.NetworkConstants
import com.dominate.thirtySecondsChallenge.data.pref.configuration.ConfigurationPref
import com.dominate.thirtySecondsChallenge.data.pref.user.UserPref
import okhttp3.Interceptor
import okhttp3.Response
import java.util.*
import javax.inject.Inject


class AppBaseInterceptor @Inject constructor(
    private val configurationPref: ConfigurationPref,
    private val userPref: UserPref
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val newRequest = chain.request().newBuilder()

        if (TextUtils.isEmpty(original.header(NetworkConstants.SKIP_AUTHORIZATION_HEADER)) ||
            !TextUtils.isEmpty(original.header(NetworkConstants.SKIP_AUTHORIZATION_HEADER)) && original.header(
                NetworkConstants.SKIP_AUTHORIZATION_HEADER
            )!!.toLowerCase(Locale.ROOT) == "false"
        ) {

            newRequest.addHeader(
                NetworkConstants.AUTHORIZATION_HEADER_KEY,
                NetworkConstants.AUTHORIZATION_HEADER_STARTED_VALUE + getAccessToken()
            )
        }

        newRequest.addHeader(
            NetworkConstants.ACCEPT_LANGUAGE_HEADER_KEY,
            getAppLanguage()
        )

        return chain.proceed(newRequest.build())
    }

    private fun getAppLanguage(): String {
        return configurationPref.getAppLanguageValue()
    }

    private fun getAccessToken(): String {
        return userPref.getToken()
    }
}