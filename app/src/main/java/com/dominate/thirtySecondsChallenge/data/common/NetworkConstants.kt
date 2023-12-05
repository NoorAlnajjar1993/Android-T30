package com.dominate.thirtySecondsChallenge.data.common

import com.dominate.thirtySecondsChallenge.BuildConfig


object NetworkConstants {

    const val APP_TIMEOUT_MINUTES = 1L
    const val APP_BASE_URL = BuildConfig.AppBaseUrl
    const val HUB_URL_RELEASE = BuildConfig.HubUrlRelease

    // Request Headers Common Constants

    // Authorization
    const val SKIP_AUTHORIZATION_HEADER = "Skip_Authorization"
    const val AUTHORIZATION_HEADER_KEY = "Authorization"
    const val AUTHORIZATION_HEADER_STARTED_VALUE =
        "Bearer " // Should Add Space After Bearer Keyword

    const val ACCEPT_LANGUAGE_HEADER_KEY = "Languages"
}